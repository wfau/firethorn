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
# Create our sed functions.
#[root@tester]

        skip-headers()
            {
            sed '
                /^HTTP\/1.1 100/,/^\r$/ {
                    d
                    }
                '
            }

        split-headers()
            {
            sed '
                /^HTTP\/1.1 [0-9]\{3\}/,/^\r$/ {
                    /^\r$/ !{
                        w /tmp/response-headers.txt
                        d
                        }
                    }
                '
            }

        get-httpcode()
            {
            sed -n '
                s/\(^HTTP.*\) \r/\1/p
                ' /tmp/response-headers.txt
            }

        get-username()
            {
            sed -n '
                s/^firethorn.auth.username: \(.*\)\r/\1/p
                ' /tmp/response-headers.txt
            }

        get-group()
            {
            sed -n '
                s/^firethorn.auth.community: \(.*\)\r/\1/p
                ' /tmp/response-headers.txt
            }

        print-headers()
            {
            echo "----"
            echo "HTTP code [$(get-httpcode)]"
            echo "Community [$(get-group)]"
            echo "Username  [$(get-username)]"
            echo "----"
            }

# -----------------------------------------------------
# Make a HEAD request to check admin account works.
#[root@deployer]

        curl \
            --head \
            --silent \
            --header "firethorn.auth.username:${adminuser:?}" \
            --header "firethorn.auth.password:${adminpass:?}" \
            --header "firethorn.auth.community:${admingroup:?}" \
            "${endpointurl:?}/system/info" \
            | split-headers

        print-headers

# -----------------------------------------------------
# Make a HEAD request to get a guest username.
#[root@deployer]

        curl \
            --head \
            --silent \
            "${endpointurl:?}/system/info" \
            | split-headers

        guestuser=$(get-username)

        print-headers

# -----------------------------------------------------
# Make a HEAD request to choose our test username.
#[root@deployer]

        testuser=albert.augustus@example.com
        testpass=$(pwgen 20 1)

        curl \
            --head \
            --silent \
            --header "firethorn.auth.username:${testuser:?}" \
            --header "firethorn.auth.password:${testpass:?}" \
            "${endpointurl:?}/system/info" \
            | split-headers

        testgroup=$(get-group)

        print-headers

# -----------------------------------------------------
# Get the system status.
#[root@deployer]

        curl \
            --silent \
            --include \
            --header "firethorn.auth.username:${testuser:?}" \
            --header "firethorn.auth.password:${testpass:?}" \
            "${endpointurl:?}/system/info" \
            | split-headers \
            | jq '.' | tee /tmp/system-info.json

        print-headers

# -----------------------------------------------------
# Create the Atlas JdbcResource as admin user.
#[root@deployer]

        jdbcname="ATLAS JDBC resource"

        catalog="ATLASDR1"

        curl \
            --silent \
            --include \
            --header "firethorn.auth.username:${adminuser:?}" \
            --header "firethorn.auth.password:${adminpass:?}" \
            --header "firethorn.auth.community:${admingroup:?}" \
            --data   "jdbc.resource.name=${jdbcname:?}" \
            --data   "jdbc.resource.connection.database=${datadata:?}" \
            --data   "jdbc.resource.connection.catalog=${catalog:?}" \
            --data   "jdbc.resource.connection.type=${datatype:?}" \
            --data   "jdbc.resource.connection.host=${datahost:?}" \
            --data   "jdbc.resource.connection.user=${datauser:?}" \
            --data   "jdbc.resource.connection.pass=${datapass:?}" \
            "${endpointurl:?}/jdbc/resource/create" \
            | split-headers \
            | jq '.' | tee /tmp/atlas-jdbc.json

        print-headers

# -----------------------------------------------------
# Extract the JdbcResource URL.
#[root@deployer]

        atlasjdbc=$(
            jq -r '.self' /tmp/atlas-jdbc.json
            )

echo "---- ----
Atlas JDBC [${atlasjdbc:?}]
---- ----"

# -----------------------------------------------------
# Create an empty AdqlResource to represent the local JDBC database.
#[root@deployer]

        adqlname="ATLAS ADQL resource"

        curl \
            --silent \
            --include \
            --header "firethorn.auth.community:${admingroup:?}" \
            --header "firethorn.auth.username:${adminuser:?}" \
            --header "firethorn.auth.password:${adminpass:?}" \
            --data   "adql.resource.name=${adqlname:?}" \
            "${endpointurl:?}/adql/resource/create" \
            | split-headers \
            | jq '.' | tee /tmp/atlas-adql.json

        print-headers

# -----------------------------------------------------
# Extract the AdqlResource URL.
#[root@deployer]

        atlasadql=$(
            jq -r '.self' /tmp/atlas-adql.json
            )

echo "---- ----
Atlas ADQL [${atlasadql:?}]
---- ----"

# -----------------------------------------------------
# Locate the JdbcSchema based on catalog and schema name.
#[root@deployer]

        catalog="ATLASDR1"
        schema="dbo"
        
        curl \
            --silent \
            --include \
            --header "firethorn.auth.community:${admingroup:?}" \
            --header "firethorn.auth.username:${adminuser:?}" \
            --header "firethorn.auth.password:${adminpass:?}" \
            --data   "jdbc.schema.catalog=${catalog:?}" \
            --data   "jdbc.schema.schema=${schema:?}" \
            "${atlasjdbc:?}/schemas/select" \
            | split-headers \
            | jq '.' | tee /tmp/jdbc-schema.json

        print-headers

# -----------------------------------------------------
# Extract the JdbcSchema URL.
#[root@deployer]

        jdbcschema=$(
            jq -r '.self' /tmp/jdbc-schema.json
            )

echo "---- ----
JDBC schema [${jdbcschema:?}]
---- ----"

# -----------------------------------------------------
# Import the mapping between JDBC and ADQL tables.
#[root@deployer]

        metadoc="meta/ATLASDR1_TablesSchema.xml"

        curl \
            --silent \
            --include \
            --header "firethorn.auth.community:${admingroup:?}" \
            --header "firethorn.auth.username:${adminuser:?}" \
            --header "firethorn.auth.password:${adminpass:?}" \
            --form   "metadoc.base=${jdbcschema:?}" \
            --form   "metadoc.file=@${metadoc:?}" \
            "${atlasadql:?}/metadoc/import" \
            | skip-headers | split-headers \
            | jq '.' | tee /tmp/adql-schema.json

# -----------------------------------------------------
# Extract the AdqlSchema URL.
#[root@deployer]

        adqlschema=$(
            jq -r '.[].self' /tmp/adql-schema.json
            )

echo "---- ----
ADQL schema [${adqlschema:?}]
---- ----"

# -----------------------------------------------------
# Setup done - resources configured.
# -----------------------------------------------------

# -----------------------------------------------------
# List the top level ADQL resources.
#[root@tester]

        curl \
            --silent \
            --include \
            --header "firethorn.auth.username:${testuser:?}" \
            --header "firethorn.auth.password:${testpass:?}" \
            "${endpointurl:?}/adql/resource/select" \
            | split-headers \
            | jq '.' | tee /tmp/adql-list.json

        print-headers

# -----------------------------------------------------
# Find the Atlas ADQL resource.
#[root@tester]

    match='^ATLAS ADQL'

    atlasadql=$(
        jq -r "
            [
            .[] |
            if (.name | test(\"${match:?}\"))
            then
                .
            else
                empty
            end
            ] |
            .[0].self
            " /tmp/adql-list.json
            )

# -----------------------------------------------------
# Query the Atlas database.
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

