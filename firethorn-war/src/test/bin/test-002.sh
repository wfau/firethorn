#!/bin/bash
#
#

hostname=localhost
hostport=8080

unique()
    {
    date '+%Y%m%d %H%M%S%N'
    }

#
# JDBC resources

#echo ""
#echo "GET resource service metadata (TBD)"
#curl -v \
#    -H 'Accept: application/json' \
#    http://${hostname}:${hostport}/firethorn/jdbc/resources

echo ""
echo "GET list of JDBC resources"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/select

echo ""
echo "POST create"
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.resources.create.name=jdbc-resource-$(unique)" \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/create 

echo ""
echo "GET details for resource 1"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1

# JDBC schema

echo ""
echo "GET list of schema for resource 1 (empty)"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/schemas/select

echo ""
echo "Create a schema for resource 1"
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.resource.schema.create.name=jdbc-schema-$(unique)" \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/schemas/create

echo ""
echo "Create a schema for resource 1"
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.resource.schema.create.name=jdbc-schema-$(unique)" \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/schemas/create

echo ""
echo "GET list of schema for resource 1"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/schemas/select


