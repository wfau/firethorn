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

#
# Fix for different param names.
if [ -n "${target}" ]
then
    targethost=${target}
fi

echo "Target host [${targethost}]"
echo "Tunnel host [${tunnelhost}]"
echo "Tunnel user [${tunneluser}]"

#
# Get our external IP address.
extern()
    {
    curl -4 --silent 'http://icanhazip.com/'
    }

#
# Check the route to a specific host.
route()
    {
    local target=${1:?}
    ip route get "${target:?}" |
    sed -n '
        /^'${target}'/ {
            s/^'${target}' via \([0-9.]*\)\b.*$/\1/p
            }
        '
    }

#
# Proxy a connection.
proxy()
    {
    local targethost=${1:?}

    echo "Proxy connection"
    echo "Target host [${targethost}]"

    targetip=$(host -t A ${targethost:?} | cut -d ' ' -f 4)

    echo "Target IP   [${targetip:?}]"

    socat "TCP-LISTEN:1433,fork,reuseaddr" "TCP:${targetip:?}:1433"
    }

#
# Tunnel a connection over SSH.
tunnel()
    {
    local targethost=${1:?}
    local tunnelhost=${2:?}
    local tunneluser=${3:?}

    echo "SSH connection"
    echo "Target host [${targethost}]"
    echo "Tunnel host [${tunnelhost}]"
    echo "Tunnel user [${tunneluser}]"

    export SSH_AUTH_SOCK=/tmp/ssh_auth_sock
    ssh-keyscan "${tunnelhost:?}" 2> /dev/null >> /etc/ssh/ssh_known_hosts

    ssh -v -C -L "*:1433:${targethost:?}:1433" "${tunneluser:?}@${tunnelhost:?}"
    }

#
# Get our external IP address.
external=$(extern)
echo "External IP [${external}]"

#
# If our address is in ROE.
if [[ ${external:?} == 129.215.* ]]
then
    echo "INTERNAL network"
    #
    # Check the route to ramses1.
    via=$(route '192.168.137.20')
    echo "VIA [${via}]"
    #
    # If the route is direct (has no via).
    if [ -z ${via} ]
    then
        echo "DIRECT route"
        #
        # Proxy the SQLServer connection.
        proxy(
            ${targethost}
            )
    else
        echo "INDIRECT route"
        #
        # Tunnel the SQLServer connection.
        tunnel(
            ${targethost}
            ${tunnelhost}
            ${tunneluser}
            )
    fi
else
    echo "EXTERNAL network"
    #
    # Tunnel the SQLServer connection.
    tunnel(
        ${targethost}
        ${tunnelhost}
        ${tunneluser}
        )
fi


