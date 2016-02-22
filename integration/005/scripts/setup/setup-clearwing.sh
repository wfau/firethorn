#!/bin/bash -eu
# -e: Exit immediately if a command exits with a non-zero status.
# -u: Treat unset variables as an error when substituting.
#
#  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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


clearwinglog=clearwing
clearwinglogs="/var/logs/${clearwinglog:?}"

source "${HOME:?}/chain.properties"
source "${HOME:?}/firethorn.settings"


pushd "${FIRETHORN_CODE:?}"
    cd integration/005

    docker build \
        --tag "firethorn/ubuntu:14.04" \
        docker/ubuntu/14.04

    docker build \
        --tag "firethorn/python:3.4.2" \
        docker/python/3.4.2

    docker build \
        --tag "firethorn/pythonlibs" \
        docker/pythonlibs

popd


# ----------------------------------------------------
# Run builder

    docker run \
        -it \
        --name webpybuilder \
        --env "branch=${branch:?}" \
        --env "version=${version:?}" \
        --volume /var/local/cache:/cache \
        --volume /var/local/projects:/projects \
        --volume /var/run/docker.sock:/var/run/docker.sock \
        --volume "${HOME:?}/chain.properties:/root/chain.properties" \
        firethorn/builder:1 \
        bash ./build-clearwing.sh


