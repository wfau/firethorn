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
# Create our join query ...
#

    cat > /tmp/join-query.adql << EOF

    SELECT
        gaia.tmass_best_neighbour.original_ext_source_id AS gaia_ident,
        wfau.twomass_psc.designation                     AS wfau_ident,

        gaia.tmass_best_neighbour.source_id              AS best_source_id,
        gaia.gaia_source.source_id                       AS gaia_source_id,

        wfau.twomass_psc.ra                              AS wfau_ra,
        gaia.gaia_source.ra                              AS gaia_ra,

        wfau.twomass_psc.ra - gaia.gaia_source.ra        AS delta_ra,

        wfau.twomass_psc.dec                             AS wfau_dec,
        gaia.gaia_source.dec                             AS gaia_dec,

        wfau.twomass_psc.dec - gaia.gaia_source.dec      AS delta_dec

    FROM
        gaia.gaia_source,
        gaia.tmass_best_neighbour,
        wfau.twomass_psc

    WHERE
        gaia.tmass_best_neighbour.source_id = gaia.gaia_source.source_id
    AND
        gaia.tmass_best_neighbour.original_ext_source_id = wfau.twomass_psc.designation
    AND
        gaia.gaia_source.ra  BETWEEN 0 AND 1.25
    AND
        gaia.gaia_source.dec BETWEEN 0 AND 1.25
    AND
        wfau.twomass_psc.ra  BETWEEN 0 AND 1.25
    AND
        wfau.twomass_psc.dec BETWEEN 0 AND 1.25

EOF

# -----------------------------------------------------
# Execute our join query.
#

        curl \
            --silent \
            --header "firethorn.auth.identity:${identity:?}" \
            --header "firethorn.auth.community:${community:?}" \
            --data-urlencode "adql.query.input@/tmp/join-query.adql" \
            --data "adql.query.status.next=COMPLETED" \
            --data "adql.query.wait.time=600000" \
            "${endpointurl:?}/${queryspace:?}/queries/create" \
            | bin/pp | tee /tmp/join-query.json

        #
        # Get the URL for the results as VOTable
        joindata=$(cat /tmp/join-query.json | votable)

        #
        # Get the results as VOTable
        curl --silent "${joindata:?}" | xmlstarlet fo > /tmp/join-data.xml

        #
        # Remove XML namespaces
        sed -i 's#<VOTABLE[^>]*>#<VOTABLE>#' /tmp/join-data.xml

        #
        # Select a specific row.
        xmlstarlet sel -t -c "//TR[TD='00012764+0028492']" /tmp/join-data.xml | xmlstarlet fo


