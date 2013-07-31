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

jdbcresource=${1:?}
jdbccatalogname=${2:?}
jdbcschemaname=${3:?}

adqlresource=${4:?}
metadocfile=${5:?}

POST "${jdbcresource:?}/schemas/select" \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "jdbc.resource.schema.select.catalog=${jdbccatalogname:?}" \
    --data   "jdbc.resource.schema.select.schema=${jdbcschemaname:?}" \
    | ./pp | tee jdbc-schema.json

jdbcschemaident=$(
    cat jdbc-schema.json | ident
    )

POST "${adqlresource:?}/metadoc/import" \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --form   "urn:schema.metadoc.base=${jdbcschemaident:?}" \
    --form   "urn:schema.metadoc.file=@${FIRETHORN_TEST:?}/metadoc/${metadocfile:?}" \
    | ./pp | tee adql-schema.json

adqlschema=$(
    cat adql-schema.json | ident | node
    )




