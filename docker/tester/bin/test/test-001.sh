#!/bin/bash -eu
# -e: Exit immediately if a command exits with a non-zero status.
# -u: Treat unset variables as an error when substituting.
#
#  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
#
#  This program is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
#

# -----------------------------------------------------
# Check the system info.
#[root@tester]

        curl \
            --silent \
            --header "firethorn.auth.identity:${identity:?}" \
            --header "firethorn.auth.community:${community:?}" \
            "${endpointurl:?}/system/info" \
            | jq '.' | tee /tmp/system-info.json

# -----------------------------------------------------
# Create a JdbcResource to represent the local JDBC database.
#[root@tester]

        jdbcname="ATLAS JDBC resource"
        jdbcurl="jdbc:jtds:sqlserver://${dataname:?}/ATLASDR1"

        curl \
            --silent \
            --header "firethorn.auth.identity:${identity:?}"   \
            --header "firethorn.auth.community:${community:?}" \
            --data   "jdbc.resource.name=${jdbcname:?}" \
            --data   "jdbc.connection.url=${jdbcurl:?}" \
            --data   "jdbc.connection.user=${datauser:?}" \
            --data   "jdbc.connection.pass=${datapass:?}" \
            --data   "jdbc.connection.driver=${datadriver:?}" \
            --data   "jdbc.resource.catalog=ATLASDR1" \
            "${endpointurl:?}/jdbc/resource/create" \
            | jq '.' | tee /tmp/atlas-jdbc.json

        atlasjdbc=$(
            jq -r '.self' /tmp/atlas-jdbc.json
            )

        echo "Atlas JDBC [${atlasjdbc:?}]"

        curl \
            --silent \
            ${atlasjdbc:?} \
            | jq '.'

# -----------------------------------------------------
# Create an empty AdqlResource to represent the local JDBC database.
#[root@tester]

        adqlname="ATLAS ADQL resource"

        curl \
            --silent \
            --header "firethorn.auth.identity:${identity:?}"   \
            --header "firethorn.auth.community:${community:?}" \
            --data   "adql.resource.name=${adqlname:?}" \
            "${endpointurl:?}/adql/resource/create" \
            | jq '.' | tee /tmp/atlas-adql.json

        atlasadql=$(
            jq -r '.self' /tmp/atlas-adql.json
            )

        echo "Atlas ADQL [${atlasadql:?}]"

        curl \
            --silent \
            ${atlasadql:?} \
            | jq '.'

# -----------------------------------------------------
# Locate the JdbcSchema based on catalog and schema name.
#[root@tester]

        catalog="ATLASDR1"
        schema="dbo"
        
        curl \
            --silent \
            --header "firethorn.auth.identity:${identity:?}"   \
            --header "firethorn.auth.community:${community:?}" \
            --data   "jdbc.schema.catalog=${catalog:?}" \
            --data   "jdbc.schema.schema=${schema:?}" \
            "${atlasjdbc:?}/schemas/select" \
            | jq '.' | tee /tmp/jdbc-schema.json

        jdbcschema=$(
            jq -r '.self' /tmp/jdbc-schema.json
            )

        echo "JDBC schema [${jdbcschema:?}]"

        curl \
            --silent \
            ${jdbcschema:?} \
            | jq '.'

# -----------------------------------------------------
# Import the mapping between JDBC and ADQL tables.
#[root@tester]

        metadoc="meta/ATLASDR1_TablesSchema.xml"

        curl \
            --silent \
            --header "firethorn.auth.identity:${identity:?}"   \
            --header "firethorn.auth.community:${community:?}" \
            --form   "metadoc.base=${jdbcschema:?}" \
            --form   "metadoc.file=@${metadoc:?}" \
            "${atlasadql:?}/metadoc/import" \
            | jq '.' | tee /tmp/adql-schema.json

        adqlschema=$(
            jq -r '.[].self' /tmp/adql-schema.json
            )

        echo "ADQL schema [${adqlschema:?}]"

        curl \
            --silent \
            ${adqlschema:?} \
            | jq '.'

# -----------------------------------------------------
# Create a new ADQL resource to act as a workspace.
#[root@tester]

        adqlname="Query workspace"

        curl \
            --silent \
            --header "firethorn.auth.identity:${identity:?}"   \
            --header "firethorn.auth.community:${community:?}" \
            --data   "adql.resource.name=${adqlname:?}" \
            "${endpointurl:?}/adql/resource/create" \
            | jq '.' | tee /tmp/query-space.json

        queryspace=$(
            jq -r '.self' /tmp/query-space.json
            )

        echo "Query space [${queryspace:?}]"

        curl \
            --silent \
            ${queryspace:?} \
            | jq '.'

# -----------------------------------------------------
# Find the AtlasDR1 schema by name.
#[root@tester]

        selector=ATLASDR1

        curl \
            --silent \
            --header "firethorn.auth.identity:${identity:?}" \
            --header "firethorn.auth.community:${community:?}" \
            --data   "adql.schema.name=${selector:?}" \
            "${atlasadql:?}/schemas/select" \
            | jq '.' | tee /tmp/atlas-schema.json

        atlasschema=$(
            jq -r '.self' /tmp/atlas-schema.json
            )

        echo "Atlas schema [${atlasschema:?}]"

        curl \
            --silent \
            ${atlasschema:?} \
            | jq '.'

# -----------------------------------------------------
# Add the Atlas DR1 schema.
#[root@tester]

        name=ATLASDR1
        base=${atlasschema}
        
        curl \
            --silent \
            --header "firethorn.auth.identity:${identity:?}"   \
            --header "firethorn.auth.community:${community:?}" \
            --data   "adql.schema.name=${name:?}" \
            --data   "adql.schema.base=${base:?}" \
            "${queryspace:?}/schemas/import" \
            | jq '.'

# -----------------------------------------------------
# List the workspace schema.
#[root@tester]

        curl \
            --silent \
            --header "firethorn.auth.identity:${identity:?}"   \
            --header "firethorn.auth.community:${community:?}" \
            "${queryspace:?}/schemas/select" \
            | jq '.'

