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
# Run all of the build scripts in sequence.
#[root@builder]

    setup.sh

    001-checkout.sh
    002-build-toolkits.sh
    003-build-java.sh
    004-docker-proxy.sh
    005-build-webapps.sh
    006-build-tester.sh
    007-build-pyrothorn.sh

