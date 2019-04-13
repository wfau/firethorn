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
# Checkout a copy of our source code.

    if [  -e "${FIRETHORN_CODE:?}" ]
    then
        pushd "${FIRETHORN_CODE:?}"

            echo "Updating source code"

            hg pull
            hg update

        popd
    else
        pushd "$(dirname ${FIRETHORN_CODE:?})"

            hgrepo='http://wfau.metagrid.co.uk/code/firethorn'
            echo "Cloning source code from [${hgrepo:?}]"

            hg clone "${hgrepo:?}" "$(basename ${FIRETHORN_CODE:?})"

        popd
    fi

# -----------------------------------------------------
# Update the source code branch.

    pushd "${FIRETHORN_CODE:?}"

        if [[ -n "${branch:?}" ]]
        then

            echo "Updating branch [${branch:?}]"

            hg update "${branch:?}"
        fi

    popd


