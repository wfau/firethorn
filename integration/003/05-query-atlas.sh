

cat > atlas-query-003.adql << 'EOF'

    SELECT
        twomass.ra AS tmra,
        source.ra  AS atra,
        twomass.ra - source.ra AS difra,
        twomass.dec AS tmdec,
        source.dec  AS atdec,
        twomass.dec - source.dec AS difdec,
        neighbour.distanceMins AS dist
    FROM
        twomass.twomass_psc AS twomass,
        atlas.atlasSource   AS source,
        atlas.atlasSourceXtwomass_psc AS neighbour
    WHERE
        twomass.ra  BETWEEN '324.0' AND '355.0'
    AND
        twomass.dec BETWEEN '-32.0' AND '-30.0'
    AND
        source.ra   BETWEEN '324.0' AND '355.0'
    AND
        source.dec  BETWEEN '-32.0' AND '-30.0'
    AND
        neighbour.masterObjID = source.sourceID
    AND
        neighbour.slaveObjID  = twomass.pts_key
    AND
        neighbour.distanceMins < 1E-4

EOF

#
# Create the query.
POST "${queryschema?}/queries/create" \
    --header "firethorn.auth.identity:${identity}" \
    --header "firethorn.auth.community:${community}" \
    --data-urlencode "adql.schema.query.create.name=query-$(unique)" \
    --data-urlencode "adql.schema.query.create.store=${metabasename?}/${userschema?}" \
    --data-urlencode "adql.schema.query.create.query@atlas-query-003.adql" \
    | tee atlas-query.json | ./pp
queryjob=$(cat atlas-query.json | ident)

#
# Run the query.
runquery "${queryjob?}"

#
# Access the VOTable results.
#echo "$(cat atlas-query.json | votable)"
#curl "$(cat atlas-query.json | votable)"


