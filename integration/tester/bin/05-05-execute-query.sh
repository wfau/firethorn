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

createmode=${1:?}
createadql=${2:?}

#
# Create the query.
curl \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "adql.query.mode=${createmode:?}" \
    --data-urlencode "adql.query.input=${createadql:?}" \
    "${endpointurl:?}/${queryschema:?}/queries/create" \
     | bin/pp | tee query-job.json

queryident=$(
    cat query-job.json | self | node
    )

#
# Update the limits
curl \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data-urlencode "adql.query.delay.first=${delayfirst:-0}" \
    --data-urlencode "adql.query.delay.every=${delayevery:-0}" \
    --data-urlencode "adql.query.delay.last=${delaylast:-0}" \
    --data-urlencode "adql.query.limit.rows=${rowlimit:-0}" \
    --data-urlencode "adql.query.limit.time=${timelimit:-0}" \
    "${endpointurl:?}/${queryident:?}" \

#
# Run the query.
runquery "${queryident:?}"

#
# Get the VOTable results.
#curl "$(cat atlas-query.json | votable)"


