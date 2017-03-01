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

jdbcspace=${1:?}
adqlspace=${2:?}
jdbccatalogname=${3:?}
jdbcschemaname=${4:?}
metadocfile=${5:?}

curl \
    --silent \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "jdbc.resource.schema.select.catalog=${jdbccatalogname:?}" \
    --data   "jdbc.resource.schema.select.schema=${jdbcschemaname:?}" \
    "${endpointurl:?}/${jdbcspace:?}/schemas/select" \
    | jq '.' | tee /tmp/jdbc-schema.json

jdbcschemaident=$(
    cat /tmp/jdbc-schema.json | self
    )

curl \
    --silent \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --form   "urn:schema.metadoc.base=${jdbcschemaident:?}" \
    --form   "urn:schema.metadoc.file=@${metadocfile:?}" \
    "${endpointurl:?}/${adqlspace:?}/metadoc/import" \
    | jq '.' | tee /tmp/adql-schema.json

adqlschema=$(
    cat /tmp/adql-schema.json | jq '.[]' | self | node
    )




