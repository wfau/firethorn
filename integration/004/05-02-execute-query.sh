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

adqltext=${1:?}
rowidcol=rowid

#    --data   "adql.schema.query.create.rowid=${rowidcol:?}" \
#
# Create the query.
POST "${queryschema:?}/queries/create" \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "adql.schema.query.create.rowid=${rowidcol:?}" \
    --data-urlencode "adql.schema.query.create.query=${adqltext:?}" \
     | ./pp | tee query-job.json

queryident=$(
    cat query-job.json | ident | node
    )

POST "${queryident:?}" \
    --header "firethorn.auth.identity:${identity:?}"    \
    --header "firethorn.auth.community:${community:?}"  \
    --data-urlencode "adql.query.update.delay.first=500" \
    --data-urlencode "adql.query.update.delay.every=100" \
    --data-urlencode "adql.query.update.delay.last=500"  \
     | ./pp

#
# Run the query.
runquery "${queryident:?}"

#
# Get the VOTable results.
#curl "$(cat atlas-query.json | votable)"


