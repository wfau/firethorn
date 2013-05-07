

cat > atlas-query-002.adql << 'EOF'

    SELECT
        twomass.ra AS tmra,
        atlas.ra   AS atra,
        (twomass.ra - atlas.ra) AS difra,
        twomass.dec AS tmdec,
        atlas.dec   AS atdec,
        (twomass.dec - atlas.dec) AS difdec,
        neighbour.distanceMins AS dist
    FROM
        query_schema.twomass_psc AS twomass,
        query_schema.atlasSource AS atlas,
        query_schema.atlasSourceXtwomass_psc AS neighbour
    WHERE
        twomass.ra  BETWEEN '324.0' AND '355.0'
    AND
        twomass.dec BETWEEN '-32.0' AND '-30.0'
    AND
        atlas.ra   BETWEEN '324.0' AND '355.0'
    AND
        atlas.dec  BETWEEN '-32.0' AND '-30.0'
    AND
        neighbour.masterObjID = atlas.sourceID
    AND
        neighbour.slaveObjID = twomass.pts_key
    AND
        neighbour.distanceMins < 1E-4

EOF

#
# Create the ADQL query.
POST "${queryschema?}/queries/create" \
    --data-urlencode "adql.schema.query.create.name=query-$(unique)" \
    --data-urlencode "adql.schema.query.create.store=${metabasename?}/${userschema?}" \
    --data-urlencode "adql.schema.query.create.query@atlas-query-002.adql" \
    | tee atlas-query.json | ./pp
atlasquery=$(cat atlas-query.json | ident)

#
# Run the ADQL query.
runquery "${atlasquery?}"

#
# Access the VOTable results.
#echo "$(cat atlas-query.json | votable)"
#curl "$(cat atlas-query.json | votable)"


