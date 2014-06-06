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
# Get the current verion.
getversion()
    {
    local pomfile=${1:-'pom.xml'}

    if [ -d "${pomfile:?}" ]
    then
        pomfile="${pomfile:?}/pom.xml"
    fi

    sed -n '
        s/.*<version project='\''firethorn'\''>\(.*\)<\/version>.*$/\1/p
        ' "${pomfile:?}"

    }

#
# Set the current verion.
setversion()
    {
    local version=${1:?}
    local pompath=${2:-'.'}
    echo "setversion [${version:?}][${pompath:?}]"

    if [ -d "${pompath:?}" ]
    then

        for pomfile in $(find "${pompath:?}" -name 'pom.xml')
        do
            pomversion "${version:?}" "${pomfile:?}"
        done

    else
        pomversion "${version:?}" "${pompath:?}"
    fi

    }

#
# Set the current verion.
pomversion()
    {
    local version=${1:?}
    local pomfile=${2:-'pom.xml'}
    echo "pomversion [${version:?}][${pomfile:?}]"

    if [ -d "${pomfile:?}" ]
    then
        pomfile="${pomfile:?}/pom.xml"
    fi

    sed -i '
        s/<version project='\''firethorn'\''>.*<\/version>/<version project='\''firethorn'\''>'${version:?}'<\/version>/
        ' "${pomfile:?}"

    }


