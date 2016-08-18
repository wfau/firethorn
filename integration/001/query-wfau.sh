

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
        twomass.ra  BETWEEN '55.0' AND '55.9'
    AND
        twomass.dec BETWEEN '20.0' AND '22.9'
    AND
        ukidss.ra   BETWEEN '55.0' AND '55.9'
    AND
        ukidss.dec  BETWEEN '20.0' AND '22.9'
    AND
        neighbour.masterObjID = ukidss.sourceID
    AND
        neighbour.slaveObjID = twomass.pts_key
    AND
        neighbour.distanceMins < 1E-3

EOF

#
# Create the ADQL query.
POST "${wfauschema?}/queries/create" \
    --data-urlencode "adql.query.name=query-$(unique)" \
    --data-urlencode "adql.query.input@wfau-query-001.adql" \
    | tee wfau-query.json | ./pp
wfauquery=$(cat wfau-query.json | ident)

#
# Run the ADQL query.
runquery "${wfauquery?}"



