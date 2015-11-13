#!/bin/bash
#
# <meta:header>
#   <meta:licence>
#     Copyright (c) 2015, ROE (http://www.roe.ac.uk/)
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
# Error checking.
# http://stackoverflow.com/a/5195826
#[root@builder]

    set -e -u

# -----------------------------------------------------
# Build our pyrothorn container.
#[root@builder]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        source "bin/util.sh"

        if [ $(docker images | grep -c '^firethorn/pyrothorn') -eq 0 ]
        then
            echo "# ------"
            echo "# Building pyrothorn image"
            docker build \
                --tag firethorn/pyrothorn:$(getversion) \
                integration/005/testing/pyrothorn

        fi
    popd

