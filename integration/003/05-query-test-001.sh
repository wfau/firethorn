
time source "${FIRETHORN_TEST?}/04-02-import-query-table.sh"  'ukidssdr5' 'lasSourceXDR7PhotoObj' 'ukidss' 'lasSourceXDR7PhotoObj'
time source "${FIRETHORN_TEST?}/04-02-import-query-table.sh"  'ukidssdr5' 'lasSource'             'ukidss' 'lasSource'

queryfile=query-test-001.adql

#
# This fails (unknown field SourceId).
# I think this is something in the ADQL parser ?
cat > "${queryfile?}" << 'EOF'

    SELECT
        ...
    FROM
        ...,
        (
        SELECT
            s.SourceId AS id
        FROM
            ....
        ) AS T
    WHERE
        ....
    AND
        T.id = ....

EOF

#
# With the 'AS id' alias on the nested query, we get the following ADQL and SQL.

#  "osql" : "SELECT CrossMatch.distanceMins AS DistanceMins\nFROM UKIDSSDR5PLUS.dbo.lasSourceXDR7PhotoObj AS CrossMatch , (SELECT s.sourceID AS id\nFROM UKIDSSDR5PLUS.dbo.lasSource AS s\nWHERE s.ra > 0 AND s.ra < 13.94 AND s.dec > -12.0 AND s.dec < -8.5 AND s.mergedClass = 1\nGROUP BY s.sourceID) AS T \nWHERE CrossMatch.distanceMins < 2/60.0 AND CrossMatch.sdssType = 3 AND CrossMatch.sdssPrimary = 1 AND T.sourceID = CrossMatch.masterObjID",
#  "adql" : "SELECT DistanceMins\nFROM ukidss.lasSourceXDR7PhotoObj AS CrossMatch CROSS JOIN (SELECT s.SourceId AS id\nFROM ukidss.lasSource AS s\nWHERE ra > 0 AND ra < 13.94 AND dec > -12.0 AND dec < -8.5 AND mergedclass = 1\nGROUP BY s.SourceId) AS T\nWHERE DistanceMins < 2/60.0 AND sdsstype = 3 AND sdssPrimary = 1 AND T.id = CrossMatch.masterObjID",

#
# The generated SQL contains 'AS id' in the nested query, but joins on the original column name 'T.sourceID' not 'T.id'.
"
    SELECT
        ...
    FROM
        UKIDSSDR5PLUS.dbo.lasSourceXDR7PhotoObj AS CrossMatch ,
        (
        SELECT
            s.sourceID AS id
        FROM
            ...
        WHERE
            ....
        GROUP BY
            s.sourceID
        ) AS T
    WHERE
        ....
    AND
        T.sourceID = CrossMatch.masterObjID
"

#
# This works (without the alias on s.SourceId)
cat > "${queryfile?}" << 'EOF'

    SELECT
        DistanceMins
    FROM
        ukidss.lasSourceXDR7PhotoObj as CrossMatch,
        (
        SELECT
            s.SourceId
        FROM
            ukidss.lasSource AS s
        WHERE
            ra > 0 AND ra < 13.94
        AND
	        dec > -12.0 AND dec < -8.5
        AND
            mergedclass = 1
	    GROUP BY
	        s.SourceId
        ) AS T
    WHERE
        DistanceMins < 2/60.0
    AND
        sdsstype =3
    AND
        sdssPrimary = 1
    AND
        T.SourceId = CrossMatch.masterObjID

EOF

#
# Create the query.
POST "${queryschema?}/queries/create" \
    --header "firethorn.auth.identity:${identity}" \
    --header "firethorn.auth.community:${community}" \
    --data-urlencode "adql.schema.query.create.name=query-$(unique)" \
    --data-urlencode "adql.schema.query.create.store=${metabasename?}/${userschema?}" \
    --data-urlencode "adql.schema.query.create.query@${queryfile?}" \
    | tee atlas-query.json | ./pp
queryjob=$(cat atlas-query.json | ident)

#
# Run the query.
runquery "${queryjob?}"

#
# Access the VOTable results.
curl "$(cat atlas-query.json | votable)"


