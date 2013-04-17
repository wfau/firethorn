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
# Create our TWOMASS resource.
jdbctwomass=$(
    POST "/jdbc/resource/create" \
        -d "jdbc.resource.create.url=spring:RoeTWOMASS" \
        -d "jdbc.resource.create.name=twomass-$(unique)" \
        -d "jdbc.resource.create.ogsadai=twomass" \
        | ident
        )

#
# Check the TWOMASS resource.
GET "${jdbctwomass?}" | ./pp

#
# List the TWOMASS schema.
GET "${jdbctwomass?}/schemas/select" | ./pp

#
# List the TWOMASS tables.
for jdbcschema in $(
    GET "${jdbctwomass?}/schemas/select"  \
        | ident
    )
    do
        GET "${jdbcschema?}/tables/select" | ./pp
    done

#
# Create our UKIDSS resource.
jdbcukidss=$(
    POST "/jdbc/resource/create" \
        -d "jdbc.resource.create.url=spring:RoeUKIDSS" \
        -d "jdbc.resource.create.name=ukidss-$(unique)" \
        -d "jdbc.resource.create.ogsadai=ukidss" \
        | ident
        )

#
# Check the UKIDSS resource.
GET "${jdbcukidss?}"  | ./pp

#
# List the UKIDSS schema.
GET "${jdbcukidss?}/schemas/select" | ./pp

#
# List the UKIDSS tables.
for jdbcschema in $(
    GET "${jdbcukidss?}/schemas/select"  \
        | ident
    )
    do
        GET "${jdbcschema?}/tables/select" | ./pp
    done



