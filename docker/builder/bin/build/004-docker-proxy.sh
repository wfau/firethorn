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
# Check for a stopped container.
#[root@builder]

    #
    # Check for existing container.
    echo "Checking docker-proxy container"
    status=$(docker ps --all --filter "name=docker-proxy" --format '{{.Status}}' | cut --delimiter ' ' --field 1)
    if [ -n "${status}" ]
    then
        if [ "${status}" != "Up" ]
        then
            echo "Removing inactive docker-proxy container [${status}]"
            docker rm --force --volumes "docker-proxy"
        fi

    fi

# -----------------------------------------------------
# Start a new container.
#[root@builder]

    #
    # Start a new container.
    status=$(docker ps --all --filter "name=docker-proxy" --format '{{.Status}}' | cut --delimiter ' ' --field 1)
    if [ -z "${status}" ]
    then
        echo "Starting docker-proxy container"
        docker run \
            --detach \
            --name "docker-proxy" \
            --volume /var/run/docker.sock:/var/run/docker.sock \
            firethorn/docker-proxy:1.1
    fi


