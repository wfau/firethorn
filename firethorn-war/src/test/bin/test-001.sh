#!/bin/bash
#
#

hostname=localhost
hostport=8080
basename="http://${hostname}:${hostport}/firethorn"

#
# JDBC resources

echo ""
echo "POST create"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resource.create.name=jdbc-resource-001' \
    --data 'jdbc.resource.create.url=spring:PgsqlLocalTest' \
    "${basename}/jdbc/resources/create"

echo ""
echo "POST create"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resource.create.name=jdbc-resource-002' \
    --data 'jdbc.resource.create.url=spring:MysqlLocalTest' \
    "${basename}/jdbc/resources/create"

echo ""
echo "POST create for 003"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resource.create.name=jdbc-resource-003' \
    "${basename}/jdbc/resources/create"

echo ""
echo "POST create for 004"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resource.create.name=jdbc-resource-004' \
    "${basename}/jdbc/resources/create"

echo ""
echo "GET select for all"
curl -v \
    -H 'Accept: application/json' \
    "jdbc/resources/select"

echo ""
echo "GET details for 001"
curl -v \
    -H 'Accept: application/json' \
    "jdbc/resource/1"

echo ""
echo "GET details for 002"
curl -v \
    -H 'Accept: application/json' \
    "${basename}/jdbc/resource/2"

echo ""
echo "GET search for 'jdbc'"
curl -v \
    -H 'Accept: application/json' \
    "${basename}/jdbc/resources/search?jdbc.resource.search.text=jdbc"

echo ""
echo "POST search for 'jdbc'"
curl -v \
    -H 'Accept: application/json' \
    --data 'jdbc.resource.search.text=jdbc' \
    "${basename}/jdbc/resources/search"

