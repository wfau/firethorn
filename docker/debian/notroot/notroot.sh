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
# Shell script to create anon-root user.
#

useruid=${useruid:-1000}
usergid=${usergid:-${useruid:?}}
username=${username:-noname}

#
# Check the user group.
echo "Checking group [${usergid}]"
if [ -z "$(getent group "${usergid}")" ] 
then
    echo "Creating group [${username}][${usergid}]"
    groupadd \
        --gid "${usergid:?}" \
        "${username}"
fi

#
# Check the user account.
echo "Checking user [${useruid:?}]"
if [ -z "$(getent passwd "${useruid}")" ] 
then

    if [ -z "${userhome}" ]
    then
        echo "Creating user [${username}][${useruid:?}][${usergid:?}]"
        useradd \
            --create-home \
            --uid   "${useruid:?}" \
            --gid   "${usergid:?}" \
            --shell '/bin/bash' \
            "${username:?}"
    else
        echo "Creating user [${username:?}][${useruid:?}][${usergid:?}][${userhome}]"
        useradd \
            --home  "${userhome:?}" \
            --uid   "${useruid:?}" \
            --gid   "${usergid:?}" \
            --shell '/bin/bash' \
            "${username:?}"
    fi
fi

#
# Run the container command.
if [ -z "$@" ]
then
    sudo -u "${username}" -i
else
    sudo -u "${username}" -i "$@"
fi

