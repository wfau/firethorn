#!/bin/bash
#
#

hostname=localhost
hostport=8080

limit=4

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

        echo "-- Catalog [${catalog}]"

        for (( k=1; k <= $limit; k++ ))
        do

            schema="$( \
                curl -s \
                    -H 'Accept: application/json' \
                    --data "jdbc.catalog.schemas.create.name=jdbc-schema-$(name)" \
                    ${catalog}/schemas/create |
                    sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
                    )"

            echo "-- -- Schema [${schema}]"


            for (( l=1; l <= $limit; l++ ))
            do

                table="$( \
                    curl -s \
                        -H 'Accept: application/json' \
                        --data "jdbc.schema.tables.create.name=jdbc-table-$(name)" \
                        ${schema}/tables/create |
                        sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
                        )"

                echo "-- -- -- Table [${table}]"

                for (( m=1; m <= $limit; m++ ))
                do

                    column="$( \
                        curl -s \
                            -H 'Accept: application/json' \
                            --data "jdbc.table.columns.create.name=jdbc-column-$(name)" \
                            ${table}/columns/create |
                            sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
                            )"

                    echo "-- -- -- -- Column [${column}]"
            
                done
        
            done
        done
    done
done

for resource in $( \
    curl -s \
        -H 'Accept: application/json' \
        ${service}/resources/select |
        sed 's/\(\"ident\":\)/\n\1/g'  |
        sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
        )
    do
        echo "Resource [${resource}]"

        for catalog in $( \
            curl -s \
                -H 'Accept: application/json' \
                ${resource}/catalogs/select |
                sed 's/\(\"ident\":\)/\n\1/g'  |
                sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
                )
        do
            echo "++ Catalog [${catalog}]"

            for schema in $( \
                curl -s \
                    -H 'Accept: application/json' \
                    ${catalog}/schemas/select |
                    sed 's/\(\"ident\":\)/\n\1/g'  |
                    sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
                    )
            do
                echo "++ ++ Schema [${schema}]"

                for table in $( \
                    curl -s \
                        -H 'Accept: application/json' \
                        ${schema}/tables/select |
                        sed 's/\(\"ident\":\)/\n\1/g'  |
                        sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
                        )
                do
                    echo "++ ++ ++ Table [${table}]"

                    for column in $( \
                        curl -s \
                            -H 'Accept: application/json' \
                            ${table}/columns/select |
                            sed 's/\(\"ident\":\)/\n\1/g'  |
                            sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
                            )
                    do
                        echo "++ ++ ++ ++ Column [${column}]"

                    done
                done
            done
        done
    done


