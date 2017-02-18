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
# Run a test query.
#

    curl \
        --silent \
        --header "firethorn.auth.identity:${identity:?}" \
        --header "firethorn.auth.community:${community:?}" \
        --data "adql.query.input=SELECT designation, ra, dec  FROM gaia.tmass_original_valid WHERE (ra BETWEEN 0 AND 0.5) AND (dec BETWEEN 0 AND 0.5)" \
        --data "adql.query.status.next=COMPLETED" \
        --data "adql.query.wait.time=600000" \
        "${endpointurl:?}/${queryspace:?}/queries/create" \
        | bin/pp | tee /tmp/gaia-query.json

    #
    # Get URL for the results as VOTable
    gaiadata=$(cat /tmp/gaia-query.json | votable)

    #
    # Get the results as VOTable files
    curl --silent "${gaiadata:?}" | xmlstarlet fo | tee /tmp/gaia-data.xml

    #
    # Remove XML namespaces
    sed -i 's#<VOTABLE[^>]*>#<VOTABLE>#' /tmp/gaia-data.xml

    #
    # Select a specific row.
    xmlstarlet sel -t -c "//TR[TD='00012764+0028492']" /tmp/gaia-data.xml | xmlstarlet fo


