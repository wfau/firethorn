#!/bin/bash
#
#

hostname=localhost
hostport=8080

name()
    {
    date '+%Y%m%d %H%M%S%N'
    }

#
# JDBC resource

echo ""
echo "Create a resource"
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.resources.create.name=jdbc-resource-$(name)" \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/create 

echo ""
echo "Get details for resource 1"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1

#
# JDBC catalog

echo ""
echo "Create a catalog for resource 1"
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.resource.catalogs.create.name=jdbc-catalog-$(name)" \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/create

echo ""
echo "Get details for catalog 1"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/catalog/1

#
# JDBC schema

echo ""
echo "Get schemas for catalog 1"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/catalog/1/schemas/select

echo ""
echo "Create a schema for catalog 1"
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.catalog.schemas.create.name=jdbc-schema-$(name)" \
    http://${hostname}:${hostport}/firethorn/jdbc/catalog/1/schemas/create

echo ""
echo "Get schemas for catalog 1"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/catalog/1/schemas/select

echo ""
echo "Get details for schema 1"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/schema/1

