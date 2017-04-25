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

# -----------------------------------------------------
# Mark the current images as latest.

    source "${HOME:?}/merge.settings"

    docker tag "firethorn/fedora:${newversion:?}"     "firethorn/fedora:latest"
    docker tag "firethorn/java:${newversion:?}"       "firethorn/java:latest"
    docker tag "firethorn/tomcat:${newversion:?}"     "firethorn/tomcat:latest"
    docker tag "firethorn/ogsadai:${newversion:?}"    "firethorn/ogsadai:latest"
    docker tag "firethorn/firethorn:${newversion:?}"  "firethorn/firethorn:latest"

    docker tag "firethorn/postgres:${newversion:?}"   "firethorn/postgres:latest"
    docker tag "firethorn/builder:${newversion:?}"    "firethorn/builder:latest"
    docker tag "firethorn/tester:${newversion:?}"     "firethorn/tester:latest"
    docker tag "firethorn/sql-tunnel:${newversion:?}" "firethorn/sql-tunnel:latest"
    docker tag "firethorn/sql-proxy:${newversion:?}"  "firethorn/sql-proxy:latest"

