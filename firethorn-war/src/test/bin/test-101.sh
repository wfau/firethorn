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
curl \
    -H 'Accept: application/json' \
    "${basename}/jdbc/resource/select"

#
# Create our JDBC resource.
curl \
    -H 'Accept: application/json' \
    --data "jdbc.resource.create.url=spring:RoeLiveData" \
    --data "jdbc.resource.create.name=jdbc-resource-$(unique)" \
    "${basename}/jdbc/resource/create"

#
# Get the resource details.
curl \
    -H 'Accept: application/json' \
    "${basename}/jdbc/resource/1"

#
# List the resource schema.
curl \
    -H 'Accept: application/json' \
    "${basename}/jdbc/resource/1/schemas/select"

#
# List the schema tables.
curl \
    -H 'Accept: application/json' \
    "${basename}/jdbc/schema/1/tables/select"

#
# Get a table details.
curl \
    -H 'Accept: application/json' \
    "${basename}/jdbc/table/5"

# -------- --------

#
# List the ADQL resources.
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/resource/select"

#
# Create our ADQL resource.
curl \
    -H 'Accept: application/json' \
    --data "adql.resource.create.name=adql-resource-$(unique)" \
    "${basename}/adql/resource/create"

#
# Get the resource details.
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/resource/2"

#
# List the resource schema.
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/resource/2/schemas/select"

#
# Create our ADQL schema.
curl \
    -H 'Accept: application/json' \
    --data "adql.resource.schema.create.name=adql-schema-$(unique)" \
    "${basename}/adql/resource/2/schemas/create"

#
# List the resource schema.
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/resource/2/schemas/select"

# -------- --------

#
# List the schema tables.
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/schema/2/tables/select"

#
# Import a JDBC table into our ADQL schema.
curl \
    -H 'Accept: application/json' \
    --data "adql.schema.table.create.base=${basename}/jdbc/table/5" \
    "${basename}/adql/schema/2/tables/create"

#
# Get the table details.
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/table/12"

# -------- --------

#
# List the table columns.
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/table/12/columns/select"



