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
# Create a new ADQL schema for the TWOMASS tables.
twomassschema=$(
    POST "${adqlspace?}/schemas/create" \
        --data "adql.resource.schema.create.name=twomass" \
        | ident
        )
GET "${twomassschema?}" \
    | ./pp

#
# Import the TWOMASS 'twomass_psc' table.
jdbcschema=$(
    POST "${jdbcatlas?}/schemas/select" \
        -d "jdbc.resource.schema.select.name=TWOMASS.dbo" \
        | ident
        )
jdbctable=$(
    POST "${jdbcschema?}/tables/select" \
        -d "jdbc.schema.table.select.name=twomass_psc" \
        | ident
        )
POST "${twomassschema?}/tables/import" \
    --data "adql.schema.table.import.base=${metabasename?}/${jdbctable?}" \
    | ./pp


#
# Create a new ADQL schema for the UKIDSS tables.
ukidssschema=$(
    POST "${adqlspace?}/schemas/create" \
        --data "adql.resource.schema.create.name=ukidss" \
        | ident
        )
GET "${ukidssschema?}" \
    | ./pp

#
# Import the UKIDSS 'gcsPointSource' and  'gcsSourceXtwomass_psc' tables.
jdbcschema=$(
    POST "${jdbcatlas?}/schemas/select" \
        -d "jdbc.resource.schema.select.name=UKIDSSDR5PLUS.dbo" \
        | ident
        )
jdbctable=$(
    POST "${jdbcschema?}/tables/select" \
        -d "jdbc.schema.table.select.name=gcsPointSource" \
        | ident
        )
POST "${ukidssschema?}/tables/import" \
    --data "adql.schema.table.import.base=${metabasename?}/${jdbctable?}" \
    | ./pp

jdbctable=$(
    POST "${jdbcschema?}/tables/select" \
        -d "jdbc.schema.table.select.name=gcsSourceXtwomass_psc" \
        | ident
        )
POST "${ukidssschema?}/tables/import" \
    --data "adql.schema.table.import.base=${metabasename?}/${jdbctable?}" \
    | ./pp


#
# Create a new ADQL schema for the Atlas tables.
atlasschema=$(
    POST "${adqlspace?}/schemas/create" \
        --data "adql.resource.schema.create.name=atlas" \
        | ident
        )
GET "${atlasschema?}" \
    | ./pp

#
# Import the ATLAS 'atlasSource' and  'atlasSourceXtwomass_psc' tables.
jdbcschema=$(
    POST "${jdbcatlas?}/schemas/select" \
        -d "jdbc.resource.schema.select.name=ATLASv20130304.dbo" \
        | ident
        )
jdbctable=$(
    POST "${jdbcschema?}/tables/select" \
        -d "jdbc.schema.table.select.name=atlasSource" \
        | ident
        )
POST "${atlasschema?}/tables/import" \
    --data "adql.schema.table.import.base=${metabasename?}/${jdbctable?}" \
    | ./pp

jdbctable=$(
    POST "${jdbcschema?}/tables/select" \
        -d "jdbc.schema.table.select.name=atlasSourceXtwomass_psc" \
        | ident
        )
POST "${atlasschema?}/tables/import" \
    --data "adql.schema.table.import.base=${metabasename?}/${jdbctable?}" \
    | ./pp













