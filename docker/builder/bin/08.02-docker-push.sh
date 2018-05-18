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

    source "${HOME:?}/merge.settings"

    #
    # Issues with running push from inside a container ?
    # Issues with access to secrets from inside a container ?
    #

    docker login \
        --username $(secret docker.io.user) \
        --password $(secret docker.io.pass)

    docker push "firethorn/fedora:${newversion:?}"
    docker push "firethorn/java:${newversion:?}"
    docker push "firethorn/tomcat:${newversion:?}"
    docker push "firethorn/firethorn:${newversion:?}"
    docker push "firethorn/ogsadai:${newversion:?}"

    docker push "firethorn/builder:${newversion:?}"
    docker push "firethorn/tester:${newversion:?}"
    docker push "firethorn/postgres:${newversion:?}"
    docker push "firethorn/sql-proxy:${newversion:?}"

    docker push "firethorn/firethorn-py:${newversion:?}"

    docker push "firethorn/fedora:latest"
    docker push "firethorn/java:latest"
    docker push "firethorn/tomcat:latest"
    docker push "firethorn/firethorn:latest"
    docker push "firethorn/ogsadai:latest"

    docker push "firethorn/builder:latest"
    docker push "firethorn/tester:latest"
    docker push "firethorn/postgres:latest"
    docker push "firethorn/sql-proxy:latest"

    docker push "firethorn/firethorn-py:latest"

