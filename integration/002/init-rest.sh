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
# Set the base URL and options.
metahostname=localhost
metahostport=8080
metabasename="http://${metahostname?}:${metahostport?}/firethorn"

#
# Initialise our REST client.
resty "${metabasename?}" -W -H 'Accept: application/json'

#
# Unique name generator 
unique()
    {
    date '+%Y%m%d-%H%M%S%N'
    }

#
# Create a 'define' function for setting heredoc variables.
# http://stackoverflow.com/questions/1167746/how-to-assign-a-heredoc-value-to-a-variable-in-bash
define()
    {
    IFS='\n' read -r -d '' ${1} || true;
    }

#
# Create an 'ident' function to get the short ident from a json response.
ident()
    {
    ./pp | sed -n 's|^ *"ident" : "'${metabasename?}'\(.*\)"[^"]*|\1|p'
    }

#
# Create an 'name' function to get the name from a json response.
name()
    {
    ./pp | sed -n 's|^ *"name" : "\([^"]*\)".*|\1|p'
    }

#
# Create a 'status' function to get the job status from a json response.
status()
    {
    ./pp | sed -n '
        /^ *"syntax" : {/, /^ *}/ d
        s|^ *"status" : "\([^"]*\)".*|\1|p
        '
    }

#
# Create a 'votable' function to get the job status from a json response.
votable()
    {
    ./pp | sed -n 's|^ *"votable" : "\([^"]*\)".*|\1|p'
    }

#
# Run a query and poll the result.
runquery()
    {
    local query=${1?}
    local status=$(
        POST "${query?}" \
            --data-urlencode "adql.query.update.status=RUNNING" \
            | status
            )

    while [ "${status?}" == 'PENDING' -o "${status?}" == 'RUNNING' ]
    do
        sleep 1
        status=$(
            GET "${query?}" \
                | status
                )
        echo "${status?}"
    done
    }


