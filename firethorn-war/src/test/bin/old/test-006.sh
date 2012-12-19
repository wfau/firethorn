#!/bin/bash
#
#

hostname=localhost
hostport=8080

limit=2

unique()
    {
    date '+%Y%m%d %H%M%S%N'
    }

name()
    {
    sed 's/.*\"name\":\"\([^\"]*\)\".*/\1/'
    }

status()
    {
    sed 's/.*\"status\":\"\([^\"]*\)\".*/\1/'
    }

service="http://${hostname}:${hostport}/firethorn"

for (( i=1; i <= $limit; i++ ))
do

    jdbc_resource="$( \
        curl -s \
            -H 'Accept: application/json' \
            --data "jdbc.resources.create.name=jdbc-resource-$(unique)" \
            ${service}/jdbc/resources/create |
            sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
            )"

    jdbc_created="${jdbc_created} ${jdbc_resource}"
    echo "JDBC resource [${jdbc_resource}]"

    for (( j=1; j <= $limit; j++ ))
    do

        adql_resource="$( \
            curl -s \
                -H 'Accept: application/json' \
                --data "jdbc.resource.adql.create.name=adql-resource-$(unique)" \
                ${jdbc_resource}/adql/create |
                sed 's/.*\"ident\":\"\([^\"]*\)\".*/\1/'
                )"

        adql_created="${adql_created} ${adql_resource}"
        echo "-- ADQL resource [${adql_resource}]"

    done
done


#
# ....
for jdbc_resource in ${jdbc_created}
do

    echo "JDBC resource [${jdbc_resource}]"

    for adql_resource in $( \
        curl -s \
            -H 'Accept: application/json' \
            "${jdbc_resource}/adql/select" |
            sed 's/\(\"ident\":\)/\n\1/g'  |
            sed -n 's/.*\"ident\":\"\([^\"]*\)\".*/\1/p'
            )
    do
        echo "++ ADQL resource [${adql_resource}]"

        echo "   name [$( \
            curl -s \
                -H 'Accept: application/json' \
                "${adql_resource}" | name \
            )]"

    done
done

