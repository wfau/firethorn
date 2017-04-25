#!/bin/bash -eu
# -e: Exit immediately if a command exits with a non-zero status.
# -u: Treat unset variables as an error when substituting.
#
#  Copyright (C) 2017 Royal Observatory, University of Edinburgh, UK
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

# -----------------------------------------------------
# Calculate the next (minor) version.
newversion()
    {
    local original=${1:?}

    local versionhead=$(echo "${original}" | cut -d '-' -f '-1')
    local versiontail=$(echo "${original}" | cut -d '-' -f '2-')

    local versionmax=$(echo ${versionhead} | cut -d '.' -f 1)
    local versionmid=$(echo ${versionhead} | cut -d '.' -f 2)
    local versionmin=$(echo ${versionhead} | cut -d '.' -f 3)

    echo "${versionmax}.${versionmid}.$((${versionmin} + 1))"

    }

# -----------------------------------------------------
# Prompt the user, update our Maven and Docker files.

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        source 'bin/util.sh'

        source "${HOME:?}/merge.settings"
        newversion=$(newversion "${oldversion:?}")

        confirm "New version [${newversion:?}]"
        if [ $? -ne 0 ]
        then
            echo "EXIT : Cancelled"
            exit 0
        fi

cat > "${HOME:?}/merge.settings" << EOF
newversion=${newversion:?}
EOF

        pomversions "${newversion:?}"

        dockerfiles "${newversion:?}"

    popd

