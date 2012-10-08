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
# JDBC resources

echo ""
echo "GET resource service metadata (TBD)"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resources

echo ""
echo "GET list of JDBC resources"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/select

echo ""
echo "POST create"
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.resources.create.name=jdbc-resource-$(name)" \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/create 

echo ""
echo "GET details for resource 1"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1

#
# JDBC catalogs

echo ""
echo "GET resource catalog service metadata (TBD)"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs

echo ""
echo "GET list of catalogs for resource 1 (empty)"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/select

echo ""
echo "Create a catalog for resource 1"
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.resource.catalogs.create.name=jdbc-catalog-$(name)" \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/create

echo ""
echo "Create a catalog for resource 1"
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.resource.catalogs.create.name=jdbc-catalog-$(name)" \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/create

echo ""
echo "GET list of catalogs for resource 1"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/select

# todo check we get 404 for missing

echo ""
echo "Search for catalogs by name"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resource.catalogs.search.text=jdbc' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/search

echo ""
echo "Search for catalogs by name"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/select?jdbc.resource.catalogs.search.text=jdbc

echo ""
echo "GET details for catalog 1"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/catalog/1

#
# JDBC schema

echo ""
echo "GET list of schema for catalogs 1 (empty)"
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
echo "Create a schema for catalog 1"
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.catalog.schemas.create.name=jdbc-schema-$(name)" \
    http://${hostname}:${hostport}/firethorn/jdbc/catalog/1/schemas/create

echo ""
echo "GET list of schema for catalog 1"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/catalog/1/schemas/select

