#!/bin/bash
#
#

hostname=localhost
hostport=8080

unique()
    {
    date '+%Y%m%d %H%M%S%N'
    }

for i in {1..100}
do
    curl -v \
        -H 'Accept: application/json' \
        --data "jdbc.resource.catalogs.create.name=jdbc-catalog-$(unique)" \
        http://${hostname}:${hostport}/firethorn/jdbc/resource/2/catalogs/create
done


