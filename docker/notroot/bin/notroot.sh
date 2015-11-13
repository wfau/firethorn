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
#[root@container]

    set -e -u

# -----------------------------------------------------
# Set the defaults.
# http://stackoverflow.com/a/5195826
#[root@container]

    useruid=${useruid:-1000}
    usergid=${usergid:-${useruid}}
    username=${username:-notroot}
    userhome=${userhome:-/home/${username}}
    usertype=${usertype:-normal}
    usershell=${usershell:-/bin/bash}

# -----------------------------------------------------
# Create the group account.
# http://stackoverflow.com/a/5195826
#[root@container]

    echo "Checking group [${usergid}]"
    if [ -z "$(getent group "${usergid}")" ] 
    then
        echo "Creating group [${username}][${usergid}]"
        groupadd \
            --gid "${usergid:?}" \
            "${username}"
    fi

# -----------------------------------------------------
# Create the user account.
# http://stackoverflow.com/a/5195826
#[root@container]

    echo "Checking user [${useruid}]"
    if [ -z "$(getent passwd "${useruid}")" ] 
    then

        case "${usertype}" in

            normal)

                echo "Creating user [${username}][${useruid}][${usergid}]"
                useradd \
                    --create-home \
                    --uid   "${useruid:?}" \
                    --gid   "${usergid:?}" \
                    --home  "${userhome:?}" \
                    --shell "${usershell:?}" \
                    "${username:?}"

                ;;

            system)

                echo "Creating system user [${username}][${useruid}][${usergid}]"
                useradd \
                    --system \
                    --uid   "${useruid:?}" \
                    --gid   "${usergid:?}" \
                    --home  "${userhome:?}" \
                    --shell "${usershell:?}" \
                    "${username:?}"
                ;;


            *)
                echo $"Unknown user type : [${usertype}] {normal|system}"
                exit 1

        esac
    fi

# -----------------------------------------------------
# Run the original command.
# http://stackoverflow.com/a/5195826
#[root@container]

if [ -z "$@" ]
then
    sudo -u "#${useruid:?}" -i
else
    sudo -u "#${useruid:?}" -i "$@"
fi


