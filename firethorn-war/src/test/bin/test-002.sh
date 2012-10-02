#!/bin/bash
#
#

hostname=localhost
hostport=8080

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
echo "POST create for 001"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resources.create.name=jdbc-resource-001' \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/create 

echo ""
echo "GET details for resource 001"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1

echo ""
echo "GET resource catalog service metadata (TBD)"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs

echo ""
echo "GET list of catalogs for resource 001 (empty)"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/select

echo ""
echo "Create a catalog for resource 001"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resource.catalogs.create.name=jdbc-catalog-001' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/create

echo ""
echo "Create a catalog for resource 001"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resource.catalogs.create.name=jdbc-catalog-002' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/create

echo ""
echo "GET list of catalogs for resource 001"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/select

echo ""
echo "Select a specific catalog by name"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resource.catalogs.select.name=jdbc-catalog-002' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/select

echo ""
echo "Select a specific catalog by name"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/select?jdbc.resource.catalogs.select.name=jdbc-catalog-002

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
echo "GET details for catalog 001"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/catalog/1


