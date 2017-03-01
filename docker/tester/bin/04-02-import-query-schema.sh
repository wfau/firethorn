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

baseschemaname=${1:?}
queryschemaname=${2:?}

curl \
    --silent \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "adql.resource.schema.select.name=${baseschemaname:?}" \
    "${endpointurl:?}/${adqlspace:?}/schemas/select" \
    | jq '.' | tee /tmp/base-schema.json

baseschema=$(
    cat /tmp/base-schema.json | self
    )

curl \
    --silent \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "urn:adql.copy.depth=${adqlcopydepth:-THIN}" \
    --data   "adql.resource.schema.import.name=${queryschemaname:?}" \
    --data   "adql.resource.schema.import.base=${baseschema:?}" \
    "${endpointurl:?}/${queryspace:?}/schemas/import" \
    | jq '.' | tee /tmp/query-schema.json


queryschema=$(
    cat /tmp/query-schema.json | self | node
    )

