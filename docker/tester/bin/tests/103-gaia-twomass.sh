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

# --------------------------------------
# Create the GAIA TAP resource.
#

    #
    # Create the IvoaResource
    source "bin/02-03-create-ivoa-space.sh" \
        'GAIA TAP service' \
        'http://gea.esac.esa.int/tap-server/tap'
    gaiaivoa=${ivoaspace:?}

    #
    # Import the static VOSI file
    vosifile='vosi/gaia/gaia-tableset.xml'
    curl \
        --header "firethorn.auth.identity:${identity:?}" \
        --header "firethorn.auth.community:${community:?}" \
        --form   "vosi.tableset=@${vosifile:?}" \
        "${endpointurl:?}/${gaiaivoa:?}/vosi/import" \
        | bin/pp

    #
    # Find the Gaia DR1 schema
    findname=gaiadr1
    curl \
        --header "firethorn.auth.identity:${identity:?}" \
        --header "firethorn.auth.community:${community:?}" \
        --data   "ivoa.resource.schema.name=${findname:?}" \
        "${endpointurl:?}/${gaiaivoa:?}/schemas/select" \
        | bin/pp | tee /tmp/gaia-schema.json

    gaiaschema=$(
        cat /tmp/gaia-schema.json | self
        )

    #
    # Add the Gaia DR1 schema to our workspace.
    gaianame=gaia
    curl \
        --header "firethorn.auth.identity:${identity:?}" \
        --header "firethorn.auth.community:${community:?}" \
        --data   "urn:adql.copy.depth=${adqlcopydepth:-THIN}" \
        --data   "adql.resource.schema.import.name=${gaianame:?}" \
        --data   "adql.resource.schema.import.base=${gaiaschema:?}" \
        "${endpointurl:?}/${queryspace:?}/schemas/import" \
        | bin/pp | tee /tmp/query-schema.json


