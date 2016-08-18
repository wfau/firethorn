#!/bin/bash
#
#

hostname=localhost
hostport=8080
basename="http://${hostname}:${hostport}/firethorn"

unique()
    {
    date '+%Y%m%d-%H%M%S%N'
    }

# -------- --------

#
# Create the TWOMASS JDBC resource.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "jdbc.resource.create.url=spring:RoeTWOMASS" \
    --data "jdbc.resource.create.name=jdbc_twomass" \
    "${basename}/jdbc/resource/create"


#
# Create the TWOXMM JDBC resource.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "jdbc.resource.create.url=spring:RoeTWOXMM" \
    --data "jdbc.resource.create.name=jdbc_twoxmm" \
    "${basename}/jdbc/resource/create"

#
# Create the BestDR7 JDBC resource.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "jdbc.resource.create.url=spring:RoeBestDR7" \
    --data "jdbc.resource.create.name=jdbc_bestdr7" \
    "${basename}/jdbc/resource/create"


# -------- --------

#
# Create our ADQL worksace.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "adql.resource.create.name=test-workspace" \
    "${basename}/adql/resource/create"

#
# Import the TWOMASS tables into our workspace.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "adql.resource.schema.import.name=adql_twomass" \
    --data "adql.resource.schema.import.base=${basename}/jdbc/schema/1" \
    "${basename}/adql/resource/4/schemas/import"

#
# Import the TWOXMM tables into our workspace.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "adql.resource.schema.import.name=adql_twoxmm" \
    --data "adql.resource.schema.import.base=${basename}/jdbc/schema/2" \
    "${basename}/adql/resource/4/schemas/import"

#
# Import the BestDR7 tables into our workspace.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data "adql.resource.schema.import.name=adql_bestdr7" \
    --data "adql.resource.schema.import.base=${basename}/jdbc/schema/3" \
    "${basename}/adql/resource/4/schemas/import"

# -------- --------

#
# Create a 'DIRECT' ADQL query.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data-urlencode "adql.query.name=test-query" \
    --data-urlencode "adql.query.input@-" \
    "${basename}/adql/resource/4/queries/create"  \
<< EOF
    SELECT
        scn.date,
        scn.scan,
        scn.tile,
        psc.ra || ' - ' || psc.dec as position
    FROM
        adql_twomass.twomass_psc AS psc,
        adql_twomass.twomass_scn AS scn,
        adql_twomass.twomass_pscXBestDR7PhotoObjAll AS match
    WHERE
        (psc.scan_key = scn.scan_key)
    AND
        (match.masterObjID = psc.pts_key)
    AND
        (match.distanceMins < 0.01)
    ORDER BY
        psc.pts_key
EOF

#
# Create a 'DISTRIBUTED' ADQL query.
echo ""
echo "----"
echo ""
curl \
    -H 'Accept: application/json' \
    --data-urlencode "adql.query.name=test-query" \
    --data-urlencode "adql.query.input@-" \
    "${basename}/adql/resource/4/queries/create"  \
<< EOF
    SELECT
        scn.date,
        scn.scan,
        scn.tile,
        psc.ra || ' - ' || psc.dec as position
    FROM
        adql_twomass.twomass_psc AS psc,
        adql_twomass.twomass_scn AS scn,
        adql_twomass.twomass_pscXBestDR7PhotoObjAll AS match,
        adql_bestdr7.PhotoObjAll AS photo
    WHERE
        (psc.scan_key = scn.scan_key)
    AND
        (match.masterObjID = psc.pts_key)
    AND
        (match.slaveObjID  = photo.objID)
    AND
        (match.distanceMins < 0.01)
    ORDER BY
        psc.pts_key
EOF

echo ""
echo "----"
echo ""


