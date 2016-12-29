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
version=${1:?}

    #
    # Check all is up to date ..
    
    # Update the Maven POMs.    
    for pom in $(find . -name 'pom.xml')
    do
        sed -i "
            s/<version project='firethorn'>.*<\/version>/<version project='firethorn'>${version:?}<\/version>/
            " "${pom:?}"
    done

    for temp in $(find . -name 'Dockertemp')
    do
        file="$(dirname ${temp:?})/Dockerfile"

        echo "temp [${temp:?}]"
        echo "file [${file:?}]"

        sed '
            s/{BUILD_VERSION}/'${version:?}'/g
            ' "${temp:?}" > "${file:?}"

    done

    #
    # Commit the changes.
    #hg diff
    #hg commit -m "Version ${newversion:?}"
    #hg tag  -f "${newversion}"
    #hg push -f


