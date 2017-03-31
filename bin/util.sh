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
# Get the current version.
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
# Set the current version.
setversion()
    {
    local version=${1:?}
    local target=${2:-'.'}

    echo ""
    echo "Set version [${version:?}][${target:?}]"

    if [ -d "${target:?}" ]
    then

        pomversions "${version:?}" "${target:?}"

        dockerfiles "${version:?}" "${target:?}"

    else
        if [ "${target:?}" == 'pom.xml' ]
        then
            pomversion "${version:?}" "${target:?}"
        elif [ "${target:?}" == 'Dockertemp' ]
        then
            dockerfile "${version:?}" "${target:?}"
        fi
    fi

    }


#
# Set all the POM versions.
pomversions()
    {
    local version=${1:?}
    local target=${2:-'.'}

    echo ""
    echo "Maven POMs [${version:?}][${target:?}]"

    for pomfile in $(find "${target:?}" -name 'pom.xml')
    do
        pomversion "${version:?}" "${pomfile:?}"
    done
    }

#
# Set a POM version.
pomversion()
    {
    local version=${1:?}
    local pomfile=${2:-'pom.xml'}

    echo "Maven POM  [${version:?}][${pomfile:?}]"

    if [ -d "${pomfile:?}" ]
    then
        pomfile="${pomfile:?}/pom.xml"
    fi

    sed -i '
        s/<version project='\''firethorn'\''>.*<\/version>/<version project='\''firethorn'\''>'${version:?}'<\/version>/
        ' "${pomfile:?}"

    }

#
# Find and set all the Dockerfile versions.
dockerfiles()
    {
    local version=${1:?}
    local target=${2:-'.'}

    echo ""
    echo "Dockerfiles [${version}][${target:?}]"

    for docktemp in $(find "${target:?}" -name 'Dockertemp')
    do
        dockerfile "${version:?}" "${docktemp:?}"
    done
    }

#
# Set a Dockerfile version
dockerfile()
    {
    local version=${1:?}
    local docktemp=${2:-'Dockerfile'}

    dockfile="$(dirname ${docktemp:?})/Dockerfile"

    echo "Dockerfile  [${version:?}][${dockfile:?}]"

    sed '
        s/{BUILD_VERSION}/'${version:?}'/g
        ' "${docktemp:?}" > "${dockfile:?}"

    }

