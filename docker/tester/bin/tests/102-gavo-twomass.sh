#!/bin/sh
# <meta:header>
#   <meta:licence>
#     Copyright (c) 2017, ROE (http://www.roe.ac.uk/)
#
#     This information is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     (at your option) any later version.
#
#     This information is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
#  
#     You should have received a copy of the GNU General Public License
#     along with this program.  If not, see <http://www.gnu.org/licenses/>.
#   </meta:licence>
# </meta:header>
#
#

# -----------------------------------------------------
# Create the GAVO TWOMASS resource.
#

    #
    # Create the IvoaResource.
    source "bin/02-03-create-ivoa-space.sh" \
        'GAVO TAP service' \
        'http://dc.zah.uni-heidelberg.de/__system__/tap/run/tap'
    gavoivoa=${ivoaspace:?}

    #
    # Import the static VOSI file.
    vosifile='vosi/gavo/gavo-tableset.xml'
    curl \
        --header "firethorn.auth.identity:${identity:?}" \
        --header "firethorn.auth.community:${community:?}" \
        --form   "vosi.tableset=@${vosifile:?}" \
        "${endpointurl:?}/${gavoivoa:?}/vosi/import" \
        | bin/pp

    #
    # Find the GAVO TWOMASS schema.
    findname=twomass
    curl \
        --header "firethorn.auth.identity:${identity:?}" \
        --header "firethorn.auth.community:${community:?}" \
        --data   "ivoa.resource.schema.name=${findname:?}" \
        "${endpointurl:?}/${gavoivoa:?}/schemas/select" \
        | bin/pp | tee /tmp/gavo-schema.json

    gavoschema=$(
        cat /tmp/gavo-schema.json | self
        )

    #
    # Add the GAVO TWOMASS schema to our workspace.
    gavoname=gavo
    curl \
        --header "firethorn.auth.identity:${identity:?}" \
        --header "firethorn.auth.community:${community:?}" \
        --data   "urn:adql.copy.depth=${adqlcopydepth:-THIN}" \
        --data   "adql.resource.schema.import.name=${gavoname:?}" \
        --data   "adql.resource.schema.import.base=${gavoschema:?}" \
        "${endpointurl:?}/${queryspace:?}/schemas/import" \
        | bin/pp | tee /tmp/query-schema.json


