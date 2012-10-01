#!/bin/bash
#
#

hostname=localhost
hostport=8080

#
# ADQL services

echo ""
echo "POST create for 001"
curl \
    -H 'Accept: application/json' \
    --data 'adql.services.create.name=adql-service-001' \
    http://${hostname}:${hostport}/firethorn/adql/services/create 

echo ""
echo "POST create for 002"
curl -v \
    -H 'Accept: application/json' \
    --data 'adql.services.create.name=adql-service-002' \
    http://${hostname}:${hostport}/firethorn/adql/services/create 

echo ""
echo "POST create for 003"
curl -v \
    -H 'Accept: application/json' \
    --data 'adql.services.create.name=adql-service-003' \
    http://${hostname}:${hostport}/firethorn/adql/services/create 

echo ""
echo "POST create for 004"
curl -v \
    -H 'Accept: application/json' \
    --data 'adql.services.create.name=adql-service-004' \
    http://${hostname}:${hostport}/firethorn/adql/services/create 

echo ""
echo "GET select for all"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/adql/services/select

echo ""
echo "GET details for 001"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/adql/service/1

echo ""
echo "GET details for 002"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/adql/service/2

echo ""
echo "GET search for 'adql'"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/adql/services/search?adql.services.search.text=adql

echo ""
echo "POST search for 'adql'"
curl -v \
    -H 'Accept: application/json' \
    --data 'adql.services.search.text=adql' \
    http://${hostname}:${hostport}/firethorn/adql/services/search

#
# JDBC resources

echo ""
echo "POST create for 001"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resources.create.name=jdbc-resource-001' \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/create 

echo ""
echo "POST create for 002"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resources.create.name=jdbc-resource-002' \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/create 

echo ""
echo "POST create for 003"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resources.create.name=jdbc-resource-003' \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/create 

echo ""
echo "POST create for 004"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resources.create.name=jdbc-resource-004' \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/create 

echo ""
echo "GET select for all"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/select

echo ""
echo "GET details for 001"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1

echo ""
echo "GET details for 002"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/2

echo ""
echo "GET search for 'jdbc'"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/search?jdbc.resources.search.text=jdbc

echo ""
echo "POST search for 'jdbc'"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resources.search.text=jdbc' \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/search

