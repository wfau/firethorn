#!/bin/bash
#
#

hostname=localhost
hostport=8080

limit=4

unique()
    {
    date '+%Y%m%d %H%M%S%N'
    }

name()
    {
    sed 's/.*\"name\":\"\([^\"]*\)\".*/\1/'
    }

service="http://${hostname}:${hostport}/firethorn/jdbc"
created=""

for (( i=1; i <= $limit; i++ ))
do

    resource="$( \
        curl -s \
            -H 'Accept: application/json' \
            --data "jdbc.resources.create.name=jdbc-resource-$(unique)" \
            ${service}/resources/create |
            sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
            )"

    created="${created} ${resource}"
    echo "Resource [${resource}]"

    for (( j=1; j <= $limit; j++ ))
    do

        catalog="$( \
            curl -s \
                -H 'Accept: application/json' \
                --data "jdbc.resource.catalogs.create.name=jdbc-catalog-$(unique)" \
                ${resource}/catalogs/create |
                sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
                )"

        echo "-- Catalog [${catalog}]"

        for (( k=1; k <= $limit; k++ ))
        do

            schema="$( \
                curl -s \
                    -H 'Accept: application/json' \
                    --data "jdbc.catalog.schemas.create.name=jdbc-schema-$(unique)" \
                    ${catalog}/schemas/create |
                    sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
                    )"

            echo "-- -- Schema [${schema}]"


            for (( l=1; l <= $limit; l++ ))
            do

                table="$( \
                    curl -s \
                        -H 'Accept: application/json' \
                        --data "jdbc.schema.tables.create.name=jdbc-table-$(unique)" \
                        ${schema}/tables/create |
                        sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
                        )"

                echo "-- -- -- Table [${table}]"

                for (( m=1; m <= $limit; m++ ))
                do

                    column="$( \
                        curl -s \
                            -H 'Accept: application/json' \
                            --data "jdbc.table.columns.create.name=jdbc-column-$(unique)" \
                            ${table}/columns/create |
                            sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
                            )"

                    echo "-- -- -- -- Column [${column}]"
            
                done
        
            done
        done
    done
done

echo ""
echo "---- ---- ---- ---- ---- ---- ---- ----"
echo ""

#
# Change all of the names.
for resource in ${created}
do

    echo "Resource [${resource}]"
    echo "update name $( \
        curl -s \
            -H 'Accept: application/json' \
            --data "jdbc.resource.update.name=jdbc-resource-updated-$(unique)" \
            "${resource}" | name \
        )"

    for catalog in $( \
        curl -s \
            -H 'Accept: application/json' \
            "${resource}/catalogs/select" |
            sed 's/\(\"ident\":\)/\n\1/g'  |
            sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
            )
    do
        echo "++ Catalog [${catalog}]"
        echo "   update name $( \
            curl -s \
                -H 'Accept: application/json' \
                --data "jdbc.catalog.update.name=jdbc-catalog-updated-$(unique)" \
                "${catalog}" | name \
            )"

        for schema in $( \
            curl -s \
                -H 'Accept: application/json' \
                "${catalog}/schemas/select" |
                sed 's/\(\"ident\":\)/\n\1/g'  |
                sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
                )
        do
            echo "++ ++ Schema [${schema}]"
            echo "      update name $( \
                curl -s \
                    -H 'Accept: application/json' \
                    --data "jdbc.schema.update.name=jdbc-schema-updated-$(unique)" \
                    "${schema}" | name \
                )"

            for table in $( \
                curl -s \
                    -H 'Accept: application/json' \
                    "${schema}/tables/select" |
                    sed 's/\(\"ident\":\)/\n\1/g'  |
                    sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
                    )
            do
                echo "++ ++ ++ Table [${table}]"
                echo "         update name $( \
                    curl -s \
                        -H 'Accept: application/json' \
                        --data "jdbc.table.update.name=jdbc-schema-updated-$(unique)" \
                        "${table}" | name \
                    )"

                for column in $( \
                    curl -s \
                        -H 'Accept: application/json' \
                        "${table}/columns/select" |
                        sed 's/\(\"ident\":\)/\n\1/g'  |
                        sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
                        )
                do
                    echo "++ ++ ++ ++ Column [${column}]"
                    echo "            update name $( \
                        curl -s \
                            -H 'Accept: application/json' \
                            --data "jdbc.column.update.name=jdbc-schema-updated-$(unique)" \
                            "${column}" | name \
                        )"

                done
            done
        done
    done
done

#
# Check all of the names.
for resource in ${created}
do

    echo "Resource [${resource}]"
    echo "update name $( \
        curl -s \
            -H 'Accept: application/json' \
            --data "jdbc.resource.update.name=jdbc-resource-updated-$(unique)" \
            "${resource}" | name \
        )"

    for catalog in $( \
        curl -s \
            -H 'Accept: application/json' \
            "${resource}/catalogs/select" |
            sed 's/\(\"ident\":\)/\n\1/g'  |
            sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
            )
    do
        echo "-- Catalog [${catalog}]"
        echo "   check name $( \
            curl -s \
                -H 'Accept: application/json' \
                "${catalog}" | name \
            )"

        for schema in $( \
            curl -s \
                -H 'Accept: application/json' \
                "${catalog}/schemas/select" |
                sed 's/\(\"ident\":\)/\n\1/g'  |
                sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
                )
        do
            echo "-- -- Schema [${schema}]"
            echo "      check name $( \
                curl -s \
                    -H 'Accept: application/json' \
                    "${schema}" | name \
                )"

            for table in $( \
                curl -s \
                    -H 'Accept: application/json' \
                    "${schema}/tables/select" |
                    sed 's/\(\"ident\":\)/\n\1/g'  |
                    sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
                    )
            do
                echo "-- -- -- Table [${table}]"
                echo "         check name $( \
                    curl -s \
                        -H 'Accept: application/json' \
                        "${table}" | name \
                    )"

                for column in $( \
                    curl -s \
                        -H 'Accept: application/json' \
                        "${table}/columns/select" |
                        sed 's/\(\"ident\":\)/\n\1/g'  |
                        sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
                        )
                do
                    echo "-- -- -- -- Column [${column}]"
                    echo "            check name $( \
                        curl -s \
                            -H 'Accept: application/json' \
                            "${column}" | name \
                        )"

                done
            done
        done
    done
done


