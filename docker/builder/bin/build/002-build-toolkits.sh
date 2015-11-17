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
# Define our docker image function.
#[root@builder]

    dockerimage()
        {
        local group=${1:?}
        local image=${2:?}
        local version=${3:?}
        docker inspect --type image --format {{.Id}} "${group}/${image}:${version}" 2> /dev/null    
        }

# -----------------------------------------------------
# Build our toolkit containers.
#[root@builder]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        if [ -z $(dockerimage firethorn fedora 21.1) ]
        then
            echo "# ------"
            echo "# Building Fedora image"
            docker build \
                --tag firethorn/fedora:21.1 \
                docker/fedora/21
        fi

        if [ -z $(dockerimage firethorn notroot 1.1) ]
        then
            echo "# ------"
            echo "# Building NotRoot image"
            docker build \
                --tag firethorn/notroot:1.1 \
                docker/notroot
        fi

        if [ -z $(dockerimage firethorn java 8.1) ]
        then
            echo "# ------"
            echo "# Building Java image"
            docker build \
                --tag firethorn/java:8.1 \
                docker/java/8
        fi

        if [ -z $(dockerimage firethorn tomcat 21.1) ]
        then
            echo "# ------"
            echo "# Building Tomcat image"
            docker build \
                --tag firethorn/tomcat:8.1 \
                docker/tomcat/8
        fi

        if [ -z $(dockerimage firethorn postgres 9) ]
        then
            echo "# ------"
            echo "# Building Postgres image"
            docker build \
                --tag firethorn/postgres:9 \
                docker/postgres/9
        fi

        if [ -z $(dockerimage firethorn builder 1.2) ]
        then
            echo "# ------"
            echo "# Building Builder image"
            docker build \
                --tag firethorn/builder:1.2 \
                docker/builder
        fi

        if [ -z $(dockerimage firethorn docker-proxy 1.1) ]
        then
            echo "# ------"
            echo "# Building docker-proxy image"
            docker build \
                --tag firethorn/docker-proxy:1.1 \
                docker/docker-proxy
        fi

        if [ -z $(dockerimage firethorn sql-proxy 1.1) ]
        then
            echo "# ------"
            echo "# Building sql-proxy image"
            docker build \
                --tag firethorn/sql-proxy:1.1 \
                docker/sql-proxy
        fi

        if [ -z $(dockerimage firethorn sql-tunnel 1.1) ]
        then
            echo "# ------"
            echo "# Building sql-tunnel image"
            docker build \
                --tag firethorn/sql-tunnel:1.1 \
                docker/sql-tunnel
        fi

        if [ -z $(dockerimage firethorn ssh-client 1.1) ]
        then
            echo "# ------"
            echo "# Building ssh-client image"
            docker build \
                --tag firethorn/ssh-client:1.1 \
                docker/ssh-client
        fi

        if [ -z $(dockerimage firethorn sqsh 1.1) ]
        then
            echo "# ------"
            echo "# Building sqsh-client image"

            docker build \
                --tag firethorn/sqsh:1.1 \
                firethorn-sqlserver/src

        fi

        if [ -z $(dockerimage firethorn tester 1.1) ]
        then
            echo "# ------"
            echo "# Building tester image"

            docker build \
                --tag firethorn/tester:1.1 \
                integration/tester

        fi

    popd

