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
queryresource=${3:?}
queryschemaname=${4:?}


baseresource="${adqlspace:?}"
baseschemaname='TWOMASS'
queryresource="${queryspace:?}"
queryschemaname='twomass'


POST "${baseresource:?}/schemas/select" \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "adql.resource.schema.select.name=${baseschemaname:?}" \
    | ./pp | tee base-schema.json

baseschema=$(
    cat base-schema.json | ident
    )

POST "${queryresource:?}/schemas/import" \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   'adql.schema.depth=THIN' \
    --data   "adql.resource.schema.import.name=${queryschemaname:?}" \
    --data   "adql.resource.schema.import.base=${baseschema:?}" \
    | ./pp | tee query-schema.json

queryschema=$(
    cat query-schema.json | ident | node
    )

