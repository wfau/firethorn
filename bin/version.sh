#!/bin/bash
#
# Copyright (c) 2012, ROE (http://www.roe.ac.uk/)
# All rights reserved.
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

#
# Shell script to update the project version.
newversion=${1:?}
source ${HOME:?}/firethorn.settings
pushd "${FIRETHORN_CODE:?}"

    #
    # Check all is up to date ..
    
    # Update the Maven POMs.    
    for file in $(find . -name 'pom.xml')
    do
        sed -i "
            s/<version project='firethorn'>.*<\/version>/<version project='firethorn'>${newversion:?}<\/version>/
            " "${file:?}"
    done

    #
    # Commit the changes.
    #hg diff
    #hg commit -m "Version ${newversion:?}"
    #hg tag  -f "${newversion}"
    #hg push -f

popd

