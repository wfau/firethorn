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
# Create a new ADQL workspace for the query.
queryspace=$(
    POST "/adql/resource/create" \
        --header "firethorn.auth.identity:${identity}" \
        --header "firethorn.auth.community:${community}" \
        --data   "adql.resource.create.name=workspace-$(unique)" \
        | ident
        )
GET "${queryspace?}" \
    --header "firethorn.auth.identity:${identity}" \
    --header "firethorn.auth.community:${community}" \
    | ./pp

#
# Create a new ADQL schema.
queryschema=$(
    POST "${queryspace?}/schemas/create" \
        --header "firethorn.auth.identity:${identity}" \
        --header "firethorn.auth.community:${community}" \
        --data   "adql.resource.schema.create.name=query_schema" \
        | ident
        )
GET "${queryschema?}" \
    | ./pp


#
# Import the TWOMASS 'twomass_psc' table.
sourceschema=$(
    POST "${adqlspace?}/schemas/select" \
        --header "firethorn.auth.identity:${identity}" \
        --header "firethorn.auth.community:${community}" \
        --data   "adql.resource.schema.select.name=twomass" \
        | ident
        )
sourcetable=$(
    POST "${sourceschema?}/tables/select" \
        --header "firethorn.auth.identity:${identity}" \
        --header "firethorn.auth.community:${community}" \
        --data   "adql.schema.table.select.name=twomass_psc" \
        | ident
        )
POST "${queryschema?}/tables/import" \
    --header "firethorn.auth.identity:${identity}" \
    --header "firethorn.auth.community:${community}" \
    --data "adql.schema.table.import.base=${metabasename?}/${sourcetable?}" \
    | ./pp


#
# Import the UKIDSS 'gcsPointSource' and 'gcsSourceXtwomass_psc' tables.
sourceschema=$(
    POST "${adqlspace?}/schemas/select" \
        --header "firethorn.auth.identity:${identity}" \
        --header "firethorn.auth.community:${community}" \
        --data   "adql.resource.schema.select.name=ukidss" \
        | ident
        )
sourcetable=$(
    POST "${sourceschema?}/tables/select" \
        --header "firethorn.auth.identity:${identity}" \
        --header "firethorn.auth.community:${community}" \
        --data   "adql.schema.table.select.name=gcsPointSource" \
        | ident
        )
POST "${queryschema?}/tables/import" \
    --header "firethorn.auth.identity:${identity}" \
    --header "firethorn.auth.community:${community}" \
    --data   "adql.schema.table.import.base=${metabasename?}/${sourcetable?}" \
    | ./pp


sourcetable=$(
    POST "${sourceschema?}/tables/select" \
        --header "firethorn.auth.identity:${identity}" \
        --header "firethorn.auth.community:${community}" \
        --data   "adql.schema.table.select.name=gcsSourceXtwomass_psc" \
        | ident
        )
POST "${queryschema?}/tables/import" \
    --header "firethorn.auth.identity:${identity}" \
    --header "firethorn.auth.community:${community}" \
    --data   "adql.schema.table.import.base=${metabasename?}/${sourcetable?}" \
    | ./pp

#
# Import the ATLAS 'atlasSource' and 'atlasSourceXtwomass_psc' tables.
sourceschema=$(
    POST "${adqlspace?}/schemas/select" \
        --header "firethorn.auth.identity:${identity}" \
        --header "firethorn.auth.community:${community}" \
        --data   "adql.resource.schema.select.name=atlas" \
        | ident
        )
sourcetable=$(
    POST "${sourceschema?}/tables/select" \
        --header "firethorn.auth.identity:${identity}" \
        --header "firethorn.auth.community:${community}" \
        --data   "adql.schema.table.select.name=atlasSource" \
        | ident
        )
POST "${queryschema?}/tables/import" \
    --header "firethorn.auth.identity:${identity}" \
    --header "firethorn.auth.community:${community}" \
    --data   "adql.schema.table.import.base=${metabasename?}/${sourcetable?}" \
    | ./pp


sourcetable=$(
    POST "${sourceschema?}/tables/select" \
        --header "firethorn.auth.identity:${identity}" \
        --header "firethorn.auth.community:${community}" \
        --data   "adql.schema.table.select.name=atlasSourceXtwomass_psc" \
        | ident
        )
POST "${queryschema?}/tables/import" \
    --header "firethorn.auth.identity:${identity}" \
    --header "firethorn.auth.community:${community}" \
    --data   "adql.schema.table.import.base=${metabasename?}/${sourcetable?}" \
    | ./pp

