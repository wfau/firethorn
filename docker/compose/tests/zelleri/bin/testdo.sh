#!/bin/sh
#
# <meta:header>
#   <meta:licence>
#     Copyright (c) 2018, ROE (http://www.roe.ac.uk/)
#
#     This information is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     (at your option) any later version.
#
#     This information is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
#  
#     You should have received a copy of the GNU General Public License
#     along with this program.  If not, see <http://www.gnu.org/licenses/>.
#   </meta:licence>
# </meta:header>
#
#

# -----------------------------------------------------
# -----------------------------------------------------
# Create a new JdbcResource for our test results.
#[root@deployer]

        jdbcname="Test results"

        curl \
            --silent \
            --include \
            --header "firethorn.auth.username:${adminuser:?}" \
            --header "firethorn.auth.password:${adminpass:?}" \
            --header "firethorn.auth.community:${admingroup:?}" \
            --data   "jdbc.resource.name=${jdbcname:?}" \
            --data   "jdbc.resource.connection.database=postgres" \
            --data   "jdbc.resource.connection.catalog=postgres" \
            --data   "jdbc.resource.connection.type=${zelltype:?}" \
            --data   "jdbc.resource.connection.host=${zellhost:?}" \
            --data   "jdbc.resource.connection.user=${zelluser:?}" \
            --data   "jdbc.resource.connection.pass=${zellpass:?}" \
            "${endpointurl:?}/jdbc/resource/create" \
            | split-headers \
            | jq '.' | tee /tmp/jdbc-zelleri-resource.json

        print-headers

# -----------------------------------------------------
# Extract the JdbcResource URL.
#[root@deployer]

        jdbczelleriresource=$(
            jq -r '.self' /tmp/jdbc-zelleri-resource.json
            )

echo "---- ----
Zelleri JdbcResource [${jdbczelleriresource:?}]
---- ----"

# -----------------------------------------------------
# Select our JdbcSchema by name.
#[root@deployer]

        catalog=postgres
        schema=public

        curl \
            --silent \
            --include \
            --header "firethorn.auth.username:${adminuser:?}" \
            --header "firethorn.auth.password:${adminpass:?}" \
            --header "firethorn.auth.community:${admingroup:?}" \
            --data   "jdbc.schema.catalog=${catalog:?}" \
            --data   "jdbc.schema.schema=${schema:?}" \
            "${jdbczelleriresource:?}/schemas/select" \
            | split-headers \
            | jq '.' | tee /tmp/jdbc-zelleri-schema.json

        print-headers

# -----------------------------------------------------
# Extract the JdbcSchema identifier.
#[root@deployer]

        jdbczellerischema=$(
            jq -r '.self' /tmp/jdbc-zelleri-schema.json
            )

        jdbczellerischemaident=$(
            echo $jdbczellerischema | sed -n 's|.*\/\([^/]*\)|\1|p'
            )

echo "---- ----
Zelleri JdbcSchema [${jdbczellerischema:?}]
Zelleri JdbcSchema [${jdbczellerischemaident:?}]
---- ----"

# -----------------------------------------------------
# Query the Atlas database, and push the results to our new JdbcSchema.
#[root@tester]

cat > /tmp/atlas-query.adql << EOF

        SELECT
            COUNT(sourceID),
            (ROUND( ra/10, 0) * 10) AS rablock,
            (ROUND(dec/10, 0) * 10) AS decblock
        FROM
            ATLASDR1.atlasSource
        WHERE
            sourceID <> 0
        GROUP BY
            (ROUND( ra/10, 0) * 10),
            (ROUND(dec/10, 0) * 10)
        ORDER BY
            rablock,
            decblock

EOF

        curl \
            --silent \
            --include \
            --header "firethorn.auth.username:${testuser:?}" \
            --header "firethorn.auth.password:${testpass:?}" \
            --data-urlencode "adql.query.input@/tmp/atlas-query.adql" \
            --data "jdbc.schema.ident=${jdbczellerischemaident:?}" \
            --data "adql.query.status.next=COMPLETED" \
            --data "adql.query.wait.time=600000" \
            "${atlasadql:?}/queries/create" \
            | split-headers \
            | jq '.' | tee /tmp/atlas-query.json

        print-headers

# -----------------------------------------------------
# Get the results as a VOTable.
#[root@tester]

        votableurl=$(
            jq -r '.results.formats.votable' "/tmp/atlas-query.json"
            )

        curl \
            --silent \
            ${votableurl:?} \
            | xmllint --format - \
            | tee /tmp/atlas-data.xml

