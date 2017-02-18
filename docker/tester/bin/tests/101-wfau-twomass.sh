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
# Load the local TWOMASS resource.
#

    source "bin/02-02-create-jdbc-space.sh" \
        'TWOMASS JDBC conection' \
        "jdbc:jtds:sqlserver://${dataname:?}/TWOMASS" \
        "${datauser:?}" \
        "${datapass:?}" \
        "${datadriver:?}" \
        '*'
    wfaujdbc=${jdbcspace:?}

    source "bin/03-01-create-adql-space.sh" 'TWOMASS ADQL workspace'
    wfauadql=${adqlspace:?}

    source "bin/03-04-import-jdbc-metadoc.sh" "${wfaujdbc:?}" "${wfauadql:?}" 'TWOMASS' 'dbo' "meta/TWOMASS_TablesSchema.xml"

# -----------------------------------------------------
# Create a workspace and add the local TWOMASS schema.
#

    source "bin/04-01-create-query-space.sh"  'Test workspace'
    source "bin/04-03-import-query-schema.sh" "${wfauadql:?}" 'TWOMASS' 'wfau'


