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
    "${basename}/jdbc/resources/select"

#
# Create our JDBC resource.
curl \
    -H 'Accept: application/json' \
    --data "jdbc.resource.create.url=spring:RoeLiveData" \
    --data "jdbc.resource.create.name=jdbc-resource-$(unique)" \
    "${basename}/jdbc/resources/create"

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


# -------- --------

#
# List the ADQL resources.
curl \
    -H 'Accept: application/json' \
    "${basename}/adql/resources/select"

#
# Create our ADQL resource.
curl \
    -H 'Accept: application/json' \
    --data "adql.resource.create.name=adql-resource-$(unique)" \
    "${basename}/adql/resources/create"

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

# -------- --------

#
# Import a JDBC table into our ADQL schema.


