#!/bin/bash
#
#

hostname=localhost
hostport=8080
basename="http://${hostname}:${hostport}/firethorn"

unique()
    {
    date '+%Y%m%d-%H%M%S%N'
    }

# -------- --------

#
# List the JDBC resources.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/jdbc/resource/select"

#
# Create our JDBC resource.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "jdbc.connection.url=spring:RoeTWOMASS" \
    --data "jdbc.resource.name=jdbc-resource-$(unique)" \
    "${basename}/jdbc/resource/create"

#
# Get the resource details.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/jdbc/resource/1"

#
# List the resource schema.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/jdbc/resource/1/schemas/select"

#
# List the schema tables.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/jdbc/schema/1/tables/select"

#
# Get a table details.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/jdbc/table/5"

# -------- --------

#
# List the ADQL resources.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/resource/select"

#
# Create an ADQL resource.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "adql.resource.create.name=adql-resource-$(unique)" \
    "${basename}/adql/resource/create"

#
# Get the resource details.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/resource/2"

#
# List the resource schema.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/resource/2/schemas/select"

#
# Create our ADQL schema.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "adql.resource.schema.create.name=created_schema" \
    "${basename}/adql/resource/2/schemas/create"

#
# List the resource schema.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/resource/2/schemas/select"

# -------- --------

#
# List the schema tables.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/schema/2/tables/select"

#
# Import a JDBC table into our ADQL schema.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "adql.schema.table.import.base=${basename}/jdbc/table/5" \
    "${basename}/adql/schema/2/tables/import"

#
# Import a JDBC table with a new name.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "adql.schema.table.import.name=imported_table" \
    --data "adql.schema.table.import.base=${basename}/jdbc/table/6" \
    "${basename}/adql/schema/2/tables/import"

#
# List the schema tables.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/schema/2/tables/select"

# -------- --------

#
# Get the details for a table.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/table/12"

#
# List the table columns.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/table/12/columns/select"

# -------- --------

#
# List the ADQL schema.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/resource/2/schemas/select"

#
# Import the tables from a JDBC schema.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "adql.resource.schema.import.name=imported_schema" \
    --data "adql.resource.schema.import.base=${basename}/jdbc/schema/1" \
    "${basename}/adql/resource/2/schemas/import"

#
# List the ADQL schema.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/resource/2/schemas/select"

# -------- --------

#
# List the tables in a schema.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/schema/3/tables/select"

#
# List the columns in a table.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/table/24/columns/select"

# -------- --------

#
# Create an ADQL query.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data-urlencode "adql.query.name=test-query" \
    --data-urlencode "adql.query.input@-" \
    "${basename}/adql/resource/2/queries/create"  \
<< EOF
    SELECT
        ra,
        dec,
        pts_key
    FROM
        imported_schema.twomass_psc
    WHERE
        ra  Between '56.0' AND '57.9'
    AND
        dec Between '24.0' AND '24.2'
EOF




