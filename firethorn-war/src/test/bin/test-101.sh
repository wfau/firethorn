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
echo "GET details"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1

#
# Set the connection URL.
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.resource.connection.update.url=spring:PgsqlLocalTest" \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1

echo ""
echo "GET details"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1

echo ""
echo "GET list of schema"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/schemas/select

#
# Set the connection status.
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.resource.connection.update.status=ENABLED" \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1

echo ""
echo "GET list of schema"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/schemas/select

#
# Set the connection URL.
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.resource.connection.update.status=ENABLED" \
    --data "jdbc.resource.connection.update.url=spring:RoeLiveData" \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1

echo ""
echo "GET list of schema"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resource/1/schemas/select



