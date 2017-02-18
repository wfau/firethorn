#!/bin/sh
# <meta:header>
#   <meta:licence>
#     Copyright (c) 2017, ROE (http://www.roe.ac.uk/)
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
# Load our configuration.
#

    source /etc/tester.properties

# -----------------------------------------------------
# Configure our identity.
#

    identity=${identity:-$(date '+%H:%M:%S')}
    community=${community:-$(date '+%A %-d %B %Y')}

    source "bin/01-01-init-rest.sh"

# -----------------------------------------------------
# Check the system info.
#

    curl \
        --silent \
        --header "firethorn.auth.identity:${identity:?}" \
        --header "firethorn.auth.community:${community:?}" \
        "${endpointurl:?}/system/info" \
        | bin/pp | tee /tmp/system-info.json


