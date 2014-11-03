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


resourcename=${1:?}
resourceuri=${2:?}
resourceuser=${3:?}
resourcepass=${4:?}
drivername=${5:?}
catalogname=${6:?}
ogsadainame=${7:?}

curl \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "urn:jdbc.copy.depth=${jdbccopydepth:-FULL}" \
    --data   "jdbc.resource.create.url=${resourceuri:?}" \
    --data   "jdbc.resource.create.name=${resourcename:?}" \
    --data   "jdbc.resource.create.user=${resourceuser:?}" \
    --data   "jdbc.resource.create.pass=${resourcepass:?}" \
    --data   "jdbc.resource.create.driver=${drivername:?}" \
    --data   "jdbc.resource.create.catalog=${catalogname:?}" \
    --data   "jdbc.resource.create.ogsadai=${ogsadainame:?}" \
    "${endpointurl:?}/jdbc/resource/create" \
    | ./pp | tee jdbc-space.json

jdbcspace=$(
    cat jdbc-space.json | ident | node
    )



