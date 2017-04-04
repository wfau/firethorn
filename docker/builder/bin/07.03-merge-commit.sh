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
# Commit the changes into main.

    #
    # ONLY DO THIS IF THE TESTS PASS
    #

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        source 'bin/util.sh'

        #
        # Commit the merge.
        message="Commit [$(getversion)]"
        confirm "${message:?}"
        if [ $? -ne 0 ]
        then
            echo "EXIT : Cancelled"
            exit 0
        fi

        hg commit -m "${message:?}"

        #
        # Close the dev branch.
        message="Close [branch-${devbranch:?}]"
        confirm "${message:?}"
        if [ $? -ne 0 ]
        then
            echo "EXIT : Cancelled"
            exit 0
        fi

        hg update "${devbranch:?}"
        hg commit --close-branch -m "${message:?}"

        #
        # Update the main tag.
        hg update 'default'
        hg tag -f "version-$(getversion)"

    popd

