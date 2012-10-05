#!/bin/bash
#
#

hostname=localhost
hostport=8080

limit=5

name()
    {
    date '+%Y%m%d %H%M%S%N'
    }

service="http://${hostname}:${hostport}/firethorn/jdbc"

for (( i=1; i <= $limit; i++ ))
do

    resource="$( \
        curl -s \
            -H 'Accept: application/json' \
            --data "jdbc.resources.create.name=jdbc-resource-$(name)" \
            ${service}/resources/create |
            sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
            )"

    echo "Resource [${resource}]"

    for (( j=1; j <= $limit; j++ ))
    do

        catalog="$( \
            curl -s \
                -H 'Accept: application/json' \
                --data "jdbc.resource.catalogs.create.name=jdbc-catalog-$(name)" \
                ${resource}/catalogs/create |
                sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
                )"

        echo "Catalog [${catalog}]"

        for (( k=1; k <= $limit; k++ ))
        do

            schema="$( \
                curl -s \
                    -H 'Accept: application/json' \
                    --data "jdbc.catalog.schemas.create.name=jdbc-schema-$(name)" \
                    ${catalog}/schemas/create |
                    sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
                    )"

            echo "Schema [${schema}]"


            for (( l=1; l <= $limit; l++ ))
            do

                table="$( \
                    curl -s \
                        -H 'Accept: application/json' \
                        --data "jdbc.schema.tables.create.name=jdbc-table-$(name)" \
                        ${schema}/tables/create |
                        sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
                        )"

                echo "Table [${table}]"
        
            done
        done
    done
done




