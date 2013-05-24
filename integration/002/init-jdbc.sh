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

#
# Create our ATLAS resource.
jdbcatlas=$(
    POST "/jdbc/resource/create" \
        --header "firethorn.auth.identity:${identity}" \
        --header "firethorn.auth.community:${community}" \
        --data   "jdbc.resource.create.url=spring:RoeATLAS" \
        --data   "jdbc.resource.create.name=atlas-$(unique)" \
        --data   "jdbc.resource.create.ogsadai=atlas" \
        --data   "jdbc.resource.create.catalog=*" \
        | ident
        )

##
## Check the ATLAS resource.
#GET "${jdbcatlas?}" \
#    | ./pp
#
##
## List the ATLAS schema.
#GET "${jdbcatlas?}/schemas/select" | ./pp
#
##
## List the ATLAS tables.
#for jdbcschema in $(
#    GET "${jdbcatlas?}/schemas/select"  \
#        | ident
#    )
#    do
#        GET "${jdbcschema?}/tables/select" | ./pp
#    done


