#!/bin/bash -eu
# -e: Exit immediately if a command exits with a non-zero status.
# -u: Treat unset variables as an error when substituting.
#
#  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
#
#  This program is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
#

cat > adql-query-001.adql << 'EOF'

    SELECT
        twomass.ra AS tmra,
        ukidss.ra  AS ukra,
        (twomass.ra - ukidss.ra) AS difra,
        twomass.dec AS tmdec,
        ukidss.dec  AS ukdec,
        (twomass.ra - ukidss.ra) AS difdec,
        neighbour.distanceMins AS dist
    FROM
        adql_schema.twomass_psc AS twomass,
        adql_schema.gcsPointSource AS ukidss,
        adql_schema.gcsSourceXtwomass_psc AS neighbour
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
POST "${adqlschema?}/queries/create" \
    --data-urlencode "adql.schema.query.create.name=query-$(unique)" \
    --data-urlencode "adql.schema.query.create.store=${metabasename?}/${userschema?}" \
    --data-urlencode "adql.schema.query.create.query@adql-query-001.adql" \
    | tee adql-query.json | ./pp
adqlquery=$(cat adql-query.json | ident)

#
# Run the ADQL query.
runquery "${adqlquery?}"

