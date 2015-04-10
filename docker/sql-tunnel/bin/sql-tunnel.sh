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

echo "Tunnel user [${tunneluser:?}]"
echo "Tunnel host [${tunnelhost:?}]"
echo "Tunnel scan [${tunnelscan:?}]"
echo "Target host [${targethost:?}]"

echo "${tunnelscan:?}" >> /etc/ssh/ssh_known_hosts

#
# Proxy a SQLServer connection.
SSH_AUTH_SOCK='/tmp/ssh_auth_sock'
ssh -v -C -L "*:1433:${targethost:?}:1433" "${tunneluser:?}@${tunnelhost:?}"


