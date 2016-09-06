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
        -d "jdbc.connection.url=spring:RoeATLAS" \
        -d "jdbc.resource.name=atlas-$(unique)" \
        -d "jdbc.resource.catalog=*" \
        | ident
        )

#
# Check the ATLAS resource.
GET "${jdbcatlas?}" \
    | ./pp

#
# List the ATLAS schema.
GET "${jdbcatlas?}/schemas/select" | ./pp

#
# List the ATLAS tables.
for jdbcschema in $(
    GET "${jdbcatlas?}/schemas/select"  \
        | ident
    )
    do
        GET "${jdbcschema?}/tables/select" | ./pp
    done

#
# Create a new ADQL workspace.
atlasspace=$(
    POST "/adql/resource/create" \
        --data "adql.resource.create.name=workspace-$(unique)" \
        | ident
        )
GET "${atlasspace?}" \
    | ./pp

#
# Create a new ADQL schema.
atlasschema=$(
    POST "${atlasspace?}/schemas/create" \
        --data "adql.resource.schema.create.name=atlas_schema" \
        | ident
        )
GET "${atlasschema?}" \
    | ./pp

#
# Import the TWOMASS 'twomass_psc' table.
jdbcschema=$(
    POST "${jdbcatlas?}/schemas/select" \
        -d "jdbc.resource.schema.select.catalog=TWOMASS" \
        -d "jdbc.resource.schema.select.schema=dbo" \
        | ident
        )
jdbctable=$(
    POST "${jdbcschema?}/tables/select" \
        -d "jdbc.schema.table.select.name=twomass_psc" \
        | ident
        )
POST "${atlasschema?}/tables/import" \
    --data "adql.schema.table.import.base=${metabasename?}/${jdbctable?}" \
    | ./pp


#
# Import the UKIDSS 'gcsPointSource' table.
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
POST "${atlasschema?}/tables/import" \
    --data "adql.schema.table.import.base=${metabasename?}/${jdbctable?}" \
    | ./pp

#
# Import the UKIDSS 'gcsSourceXtwomass_psc' table.
jdbcschema=$(
    POST "${jdbcatlas?}/schemas/select" \
        -d "jdbc.resource.schema.select.name=UKIDSSDR5PLUS.dbo" \
        | ident
        )
jdbctable=$(
    POST "${jdbcschema?}/tables/select" \
        -d "jdbc.schema.table.select.name=gcsSourceXtwomass_psc" \
        | ident
        )
POST "${atlasschema?}/tables/import" \
    --data "adql.schema.table.import.base=${metabasename?}/${jdbctable?}" \
    | ./pp

#
# Import the ATLAS 'atlasSource' table.
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

#
# Import the ATLAS/TWOMASS 'atlasSourceXtwomass_psc' table.
jdbcschema=$(
    POST "${jdbcatlas?}/schemas/select" \
        -d "jdbc.resource.schema.select.name=ATLASv20130304.dbo" \
        | ident
        )
jdbctable=$(
    POST "${jdbcschema?}/tables/select" \
        -d "jdbc.schema.table.select.name=atlasSourceXtwomass_psc" \
        | ident
        )
POST "${atlasschema?}/tables/import" \
    --data "adql.schema.table.import.base=${metabasename?}/${jdbctable?}" \
    | ./pp




