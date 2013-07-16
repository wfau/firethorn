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

baseresource=${1:?}
baseschemaname=${2:?}
basetablename=${3:?}

queryschemaid=${4:?}
querytablename=${5:?}

POST "${baseresource:?}/schemas/select" \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "adql.resource.schema.select.name=${baseschemaname:?}" \
    | ./pp | tee base-schema.json

baseschema=$(
    cat base-schema.json | ident | node
    )

POST "${baseschema:?}/tables/select" \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "adql.schema.table.select.name=${basetablename:?}" \
    | ./pp | tee base-table.json

basetable=$(
    cat base-table.json | ident
    )

POST "${queryschemaid:?}/tables/import" \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "adql.schema.table.import.base=${basetable:?}" \
    --data   "adql.schema.table.import.name=${querytablename:?}" \
    | ./pp | tee query-table.json


#    --data   'adql.table.depth=FULL' \

querytable=$(
    cat query-table.json | ident | node
    )

