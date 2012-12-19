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
# List the JDBC resources
echo ""
echo "GET list of JDBC resources"
curl -v \
    -H 'Accept: application/json' \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/select

#
# Create JDBC resource with connection URL.
curl -v \
    -H 'Accept: application/json' \
    --data "jdbc.resources.create.url=spring:RoeLiveData" \
    --data "jdbc.resources.create.name=RoeLiveData" \
    http://${hostname}:${hostport}/firethorn/jdbc/resources/create 


