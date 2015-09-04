#!/bin/bash

mkdir logs

identity=${identity:-$(date '+%H:%M:%S')}
community=${community:-$(date '+%A %-d %B %Y')}

source "bin/01-01-init-rest.sh" 

database=ATLASDR1

source "bin/02-02-create-jdbc-space.sh" \
    'Atlas JDBC conection' \
    "jdbc:jtds:sqlserver://${datalink:?}/${database:?}" \
    "${datauser:?}" \
    "${datapass:?}" \
    "${datadriver:?}" \
    '*'
atlasjdbc=${jdbcspace:?}

source "bin/03-01-create-adql-space.sh" 'Atlas ADQL workspace'
atlasadql=${adqlspace:?}

source "bin/03-04-import-jdbc-metadoc.sh" "${atlasjdbc:?}" "${atlasadql:?}" 'ATLASDR1' 'dbo' "meta/ATLASDR1_AtlasSource.xml" 
atlasschema=${adqlschema:?}

source "bin/03-04-import-jdbc-metadoc.sh" "${atlasjdbc:?}" "${atlasadql:?}" 'ATLASDR1' 'dbo' "meta/ATLASDR1_AtlasTwomass.xml" 
atlascross=${adqlschema:?}

source "bin/04-01-create-query-space.sh" 'Test workspace'

source "bin/04-03-import-query-schema.sh" "${atlasadql:?}" 'ATLASDR1' 'atlas'
source "bin/04-03-import-query-schema.sh" "${atlasadql:?}" 'TWOMASS'  'twomass'

source "bin/04-03-create-query-schema.sh" 

echo "Press [CTRL+C] to stop.."

while :
do



    adqltext="SELECT atlasSource.ra, atlasSource.dec FROM atlas.atlasSource WHERE atlasSource.ra  BETWEEN 354 AND 355 AND atlasSource.dec BETWEEN -40 AND -39"

    echo "Running query.."
    #
    # Create the query.
    curl \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "adql.schema.query.create.mode=AUTO" \
    --data-urlencode "adql.schema.query.create.query=${adqltext:?}" \
    "${endpointurl:?}/${queryschema:?}/queries/create" \
     | bin/pp | tee query-job.json

    queryident=$(
    cat query-job.json | self | node
    )

    # Create the query.
    curl \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "adql.schema.query.create.mode=AUTO" \
    --data-urlencode "adql.schema.query.create.query=${adqltext:?}" \
    "${endpointurl:?}/${queryschema:?}/queries/create" \
     | bin/pp | tee query-job.json

    queryident=$(
    cat query-job.json | self | node
    )
 
    curl \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data-urlencode "adql.query.update.status=RUNNING" \
    "${endpointurl:?}/${queryident:?}" 
            
    sleep 1
done
 
