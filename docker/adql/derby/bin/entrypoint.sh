#!/bin/sh
#
# Copyright (c) 2016, ROE (http://www.roe.ac.uk/)
# All rights reserved.
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

#
# Strict error checking.
# http://redsymbol.net/articles/unofficial-bash-strict-mode/
set -euo pipefail
IFS=$'\n\t'

#
# Install directory
: ${derbypath:=/var/local/derby}
derbybin="${derbypath}/db-derby-${derbyversion}-bin"
derbylib=${derbybin}/lib

#
# Database configuration.
: ${databaseconf:=/database.conf}
if [ -e "${databaseconf}" ]
then
    source "${databaseconf}"
fi

#
# Database initialization.
: ${databaseinit:=/database.init}

#
# Default setings
: ${systemuser:=derby}

: ${admindata:=derby}
: ${adminuser:=derby}
: ${adminpass:=$(pwgen 10 1)}

: ${serverhome:=/var/lib/derby}
: ${serverport:=1527}

: ${databasename:=$(pwgen 10 1)}}
: ${databaseuser:=$(pwgen 10 1)}}

if [ "${databaseuser}" == "${adminuser}" ]
then
    : ${databasepass:=${adminpass}}
else
    : ${databasepass:=$(pwgen 10 1)}
fi

#
# Save our settings
cat > /database.settings << EOF
#
# Admin settings
adminuser=${adminuser}
adminpass=${adminpass}

#
# System settings
serverhome=${serverhome}
serverport=${serverport}

#
# Derby settings
derbybin=${derbybin}
derbylib=${derbylib}
derbyversion=${derbyversion}

#
# Database settings
databasename=${databasename}
databaseuser=${databaseuser}
databasepass=${databasepass}
databaseinit=${databaseinit}
EOF

#
# Display our settings
cat /database.settings
#
# If the first argument is 'derby'.
if [ "${1:-derby}" = 'derby' ]
then

    #
    # Check the derby unix account.
    echo "Checking system user [${systemuser}]"
    if [ -z $(id -u "${systemuser}" 2> /dev/null) ]
    then
        echo "Creating system user [${systemuser}]"
        useradd \
            --system \
            --home-dir "${serverhome}" \
            "${systemuser}"
    fi

    #
    # Check the database directory.
    echo "Checking database directory [${serverhome}]"
    if [ ! -e "${serverhome:?}" ]
    then
        echo "Creating database directory [${serverhome}]"
        mkdir -p "${serverhome:?}"
    fi

    echo "Updating database directory [${serverhome}]"
    chown -R "${systemuser}" "${serverhome}"
    chgrp -R "${systemuser}" "${serverhome}"
    chmod 'u=rwx,g=,o=' "${serverhome}"

    #
    # Use the database directory
    pushd "${serverhome}"

#
# Create user accounts.
#derby.connection.requireAuthentication=true
#derby.user.sa=derbypass
#derby.user.mary=little7xylamb

#
# Set the serverhome directory.
#derby.system.home=${serverhome}

        #
        # Derby client command
        derbycmd=( gosu "${systemuser}" java -classpath "${derbylib}" -jar "${derbylib}/derbyrun.jar" ij )

        #
        # Derby connect command

        #
        # Check for user database.
        echo "Checking for database data [${databasename}]"
        if [ ! -d "${databasename}" ]
        then

            #
            # Create our user database.
            echo "Creating database data [${databasename}]"
            echo "connect 'jdbc:derby:${databasename};create=true';" | "${derbycmd[@]}"

        fi

        #
        # Derby client command
        derbycmd=( gosu "${systemuser}" )
        derbycmd+=( java -classpath "${derbylib}" )
        derbycmd+=( -Dij.database=jdbc:derby:${databasename} )
        derbycmd+=( -jar "${derbylib}/derbyrun.jar" )
        derbycmd+=( ij )

        echo "----"
        echo "${derbycmd[@]}"
        echo "----"

        echo
        echo "Checking init directory [${databaseinit}]"
        if [ -d "${databaseinit}" ]
        then
            echo ""
            echo "Running init scripts"
            for file in ${databaseinit}/*; do
                case "${file}" in
                    *.sh)     echo "$0: running [${file}]"; source "${file}" ; echo ;;
                    *.sql)    echo "$0: running [${file}]"; cat "${file}" | "${derbycmd[@]}" ; echo ;;
                    *.sql.gz) echo "$0: running [${file}]"; gunzip --stdout "${file}" | "${derbycmd[@]}" ; echo ;;
                    *)        echo "$0: ignoring [${file}]" ;;
                esac
            done
        fi

        echo ""
        echo "Initialization process complete."
        echo ""

    popd

    echo ""
    echo "Starting database service"
    pushd "${serverhome}"
        gosu "${systemuser}" \
            java \
            -classpath ${derbylib} \
            -jar ${derbylib}/derbyrun.jar \
            server \
            start
    popd
#
# If the first argument is not 'postgres'.
else

    echo ""
    echo "Running user command"
    
    exec "$@"

fi

