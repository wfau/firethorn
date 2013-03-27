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
# Create a new ADQL workspace.
adqlspace=$(
    POST "/adql/resource/create" \
        --data "adql.resource.create.name=workspace-$(unique)" \
        | ident
        )
GET "${adqlspace?}" \
    | ./pp

#
# Create a new ADQL schema.
adqlschema=$(
    POST "${adqlspace?}/schemas/create" \
        --data "adql.resource.schema.create.name=adql_schema" \
        | ident
        )
GET "${adqlschema?}" \
    | ./pp

#
# Import the TWOMASS 'twomass_psc' table.
jdbcschema=$(
    POST "${jdbctwomass?}/schemas/select" \
        -d "jdbc.resource.schema.select.name=TWOMASS.dbo" \
        | ident
        )
jdbctable=$(
    POST "${jdbcschema?}/tables/select" \
        -d "jdbc.schema.table.select.name=twomass_psc" \
        | ident
        )
POST "${adqlschema?}/tables/import" \
    --data "adql.schema.table.import.base=${metabasename?}/${jdbctable?}" \
    | ./pp


#
# Import the UKIDSS 'gcsPointSource' table.
jdbcschema=$(
    POST "${jdbcukidss?}/schemas/select" \
        -d "jdbc.resource.schema.select.name=UKIDSSDR5PLUS.dbo" \
        | ident
        )
jdbctable=$(
    POST "${jdbcschema?}/tables/select" \
        -d "jdbc.schema.table.select.name=gcsPointSource" \
        | ident
        )
POST "${adqlschema?}/tables/import" \
    --data "adql.schema.table.import.base=${metabasename?}/${jdbctable?}" \
    | ./pp

#
# Import the UKIDSS 'gcsSourceXtwomass_psc' table.
jdbcschema=$(
    POST "${jdbcukidss?}/schemas/select" \
        -d "jdbc.resource.schema.select.name=UKIDSSDR5PLUS.dbo" \
        | ident
        )
jdbctable=$(
    POST "${jdbcschema?}/tables/select" \
        -d "jdbc.schema.table.select.name=gcsSourceXtwomass_psc" \
        | ident
        )
POST "${adqlschema?}/tables/import" \
    --data "adql.schema.table.import.base=${metabasename?}/${jdbctable?}" \
    | ./pp

#
# Check the ADQL tables.
#GET "/${adqlschema?}/tables/select" | ./pp

#
# Check the OGSA-DAI metadata for the gcsPointSource table.
#adqlalias=$(
#    POST "${adqlschema?}/tables/select" \
#        -d "adql.schema.table.select.name=gcsPointSource" \
#        | ./pp \
#        | sed -n 's/^ *"alias" : "\([^"]*\)"[^"]*/\1/p' \
#        )                
#GET "/meta/table/${adqlalias?}" | ./pp
#GET "/meta/table/${adqlalias?}/columns" | ./pp
#GET "/meta/table/${adqlalias?}/column/ra" | ./pp

