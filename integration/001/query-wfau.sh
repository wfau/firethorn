

cat > wfau-query-001.adql << 'EOF'

    SELECT
        twomass.ra AS tmra,
        ukidss.ra  AS ukra,
        (twomass.ra - ukidss.ra) AS difra,
        twomass.dec AS tmdec,
        ukidss.dec  AS ukdec,
        (twomass.ra - ukidss.ra) AS difdec,
        neighbour.distanceMins AS dist
    FROM
        wfau_schema.twomass_psc AS twomass,
        wfau_schema.gcsPointSource AS ukidss,
        wfau_schema.gcsSourceXtwomass_psc AS neighbour
    WHERE
        twomass.ra  >= '55.0'
    AND
        twomass.ra  <= '55.9'
    AND
        twomass.dec >= '20.0'
    AND
        twomass.dec <= '22.9'
    AND
        ukidss.ra  >= '55.0'
    AND
        ukidss.ra  <= '55.9'
    AND
        ukidss.dec >= '20.0'
    AND
        ukidss.dec <= '22.9'
    AND
        neighbour.masterObjID = ukidss.sourceID
    AND
        neighbour.slaveObjID = twomass.pts_key
    AND
        neighbour.distanceMins < 1E-3

EOF

#
# Create the ADQL query.
wfauquery=$(
    POST "${wfauspace?}/queries/create" \
        --data-urlencode "adql.resource.query.create.name=query-$(unique)" \
        --data-urlencode "adql.resource.query.create.query@wfau-query-001.adql" \
        | ident
        )
GET "${wfauquery?}" \
    | ./pp

#
# Run ADQL query.
POST "${wfauquery?}" \
    --data-urlencode "adql.query.update.status=RUNNING" \
    | ./pp

GET "${wfauquery?}" \
    | ./pp

