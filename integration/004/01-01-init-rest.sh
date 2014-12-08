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


#
# Download the 'resty' wrapper for curl.
curl -# -L http://github.com/micha/resty/raw/master/resty > resty
source resty

#
# Download the 'pp' pretty print script.
curl -# -L http://github.com/micha/resty/raw/master/pp > pp
chmod a+x pp


#
# The service endpoint URL.

endpointurl=${endpointurl:-'http://localhost:8080/firethorn'}


#
# Initialise our REST client.
#resty "${endpointurl:?}" -W -H 'Accept: application/json'

#
# Unique name generator 
unique()
    {
    date '+%Y%m%d-%H%M%S%N'
    }

#
# Function for setting heredoc variables.
# http://stackoverflow.com/questions/1167746/how-to-assign-a-heredoc-value-to-a-variable-in-bash
define()
    {
    IFS='\n' read -r -d '' ${1} || true;
    }

#
# Function to get the htp ident from a JSON response.
self()
    {
    ./pp | sed -n 's|^ *"self" : "\(.*\)"[^"]*|\1|p'
    }


#
# Function to get the REST node from an http URL.
node()
    {
    sed -n 's|\(https\{0,1\}\)://\([^/:]*\):\{0,1\}\([^/]*\)/\([^/]*\)/\(.*\)|/\5|p'
    }

#
# Function to get the name from a JSON response.
name()
    {
    ./pp | sed -n 's|^ *"name" : "\([^"]*\)".*|\1|p'
    }

#
# Function to get the job status from a JSON response.
status()
    {
    ./pp | sed -n '
        /^ *"syntax" : {/, /^ *}/ d
        s|^ *"status" : "\([^"]*\)".*|\1|p
        '
    }

#
# Function to get the votable URL from a query.
votable()
    {
    ./pp | sed -n 's|^ *"votable" : "\([^"]*\)".*|\1|p'
    }

#
# Run a query and poll the result.
runquery()
    {
    local query=${1:?}
    local status=$(
        curl \
            --header "firethorn.auth.identity:${identity:?}" \
            --header "firethorn.auth.community:${community:?}" \
            --data-urlencode "adql.query.update.status=RUNNING" \
            "${endpointurl:?}/${query:?}" \
            | status
            )

    while [ "${status:?}" == 'PENDING' -o "${status:?}" == 'RUNNING' ]
    do
        sleep 1
        status=$(
            curl \
                --header "firethorn.auth.identity:${identity:?}" \
                --header "firethorn.auth.community:${community:?}" \
                "${endpointurl:?}/${query:?}" \
                | status
                )
        echo "${status:?}"
    done

    }


