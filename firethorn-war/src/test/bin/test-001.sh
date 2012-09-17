#!/bin/bash
#
#

hostname=localhost

echo ""
echo "POST create for 001"
curl \
    -H 'Accept: application/json' \
    --data 'adql.services.create.name=srv-001' \
    http://${hostname}:8080/firethorn/adql/services/create 

echo ""
echo "POST create for 002"
curl \
    -H 'Accept: application/json' \
    --data 'adql.services.create.name=srv-002' \
    http://${hostname}:8080/firethorn/adql/services/create 

echo ""
echo "POST create for 003"
curl \
    -H 'Accept: application/json' \
    --data 'adql.services.create.name=srv-003' \
    http://${hostname}:8080/firethorn/adql/services/create 

echo ""
echo "POST create for 004"
curl \
    -H 'Accept: application/json' \
    --data 'adql.services.create.name=srv-004' \
    http://${hostname}:8080/firethorn/adql/services/create 

echo ""
echo "GET select for all"
curl \
    -H 'Accept: application/json' \
    http://${hostname}:8080/firethorn/adql/services/select

echo ""
echo "GET details for 001"
curl \
    -H 'Accept: application/json' \
    http://${hostname}:8080/firethorn/adql/service/1

echo ""
echo "GET details for 002"
curl \
    -H 'Accept: application/json' \
    http://${hostname}:8080/firethorn/adql/service/2

echo ""
echo "GET search for 'srv'"
curl \
    -H 'Accept: application/json' \
    http://${hostname}:8080/firethorn/adql/services/search?adql.services.search.text=srv

echo ""
echo "POST search for 'srv'"
curl \
    -H 'Accept: application/json' \
    --data 'adql.services.search.text=srv' \
    http://${hostname}:8080/firethorn/adql/services/search




