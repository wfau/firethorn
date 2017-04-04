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


# -------------------------------------------------------------------------------------------
# Push our containers to the Docker registry.

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        buildtag=$(getbuildtag)

    popd

    #
    # Issues with running push from inside a container ?
    # Issues with access to secrets from inside a container ?
    #

    docker login \
        --username $(secret docker.io.user) \
        --password $(secret docker.io.pass)

    docker push "firethorn/fedora:${buildtag:?}"
    docker push "firethorn/java:${buildtag:?}"
    docker push "firethorn/tomcat:${buildtag:?}"
    docker push "firethorn/ogsadai:${buildtag:?}"
    docker push "firethorn/firethorn:${buildtag:?}"

    docker push "firethorn/builder:${buildtag:?}"
    docker push "firethorn/tester:${buildtag:?}"
    docker push "firethorn/postgres:${buildtag:?}"
    docker push "firethorn/sql-tunnel:${buildtag:?}"
    docker push "firethorn/sql-proxy:${buildtag:?}"

    docker push "firethorn/fedora:latest"
    docker push "firethorn/java:latest"
    docker push "firethorn/tomcat:latest"
    docker push "firethorn/ogsadai:latest"
    docker push "firethorn/firethorn:latest"

    docker push "firethorn/builder:latest"
    docker push "firethorn/tester:latest"
    docker push "firethorn/postgres:latest"
    docker push "firethorn/sql-tunnel:latest"
    docker push "firethorn/sql-proxy:latest"

