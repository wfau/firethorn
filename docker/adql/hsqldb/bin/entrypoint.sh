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
: ${servercode:=/var/local/hsqldb}
hsqldbdir=${servercode}/hsqldb-${hsqldbversion}/hsqldb
hsqldbbin=${hsqldbdir}/bin
hsqldblib=${hsqldbdir}/lib

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
: ${systemuser:=hsqldb}

: ${admindata:=hsqldb}
: ${adminuser:=hsqldb}
: ${adminpass:=$(pwgen 10 1)}

: ${serverdata:=/var/lib/hsqldb}
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
serverdata=${serverdata}
serverport=${serverport}

#
# HSQLDB settings
hsqldbbin=${hsqldbbin}
hsqldblib=${hsqldblib}
hsqldbversion=${hsqldbversion}

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
# Check the first argument.
if [ "${1:-start}" = 'start' ]
then

    #
    # Check the system user account.
    echo "Checking system user [${systemuser}]"
    if [ -z $(id -u "${systemuser}" 2> /dev/null) ]
    then
        echo "Creating system user [${systemuser}]"
        useradd \
            --system \
            --home-dir "${serverdata}" \
            "${systemuser}"
    fi

    #
    # Check the database directory.
    echo "Checking database directory [${serverdata}]"
    if [ ! -e "${serverdata:?}" ]
    then
        echo "Creating database directory [${serverdata}]"
        mkdir -p "${serverdata:?}"
    fi

    echo "Updating database directory [${serverdata}]"
    chown -R "${systemuser}" "${serverdata}"
    chgrp -R "${systemuser}" "${serverdata}"
    chmod 'u=rwx,g=,o=' "${serverdata}"

    #
    # Use the database directory
    pushd "${serverdata}"

        #
        # Derby client command
        derbycmd=( gosu "${systemuser}" java -classpath "${derbylib}" -jar "${derbylib}/derbyrun.jar" ij )

org.hsqldb.util.DatabaseManager
http://hsqldb.org/doc/2.0/util-guide/index.html

java -classpath ..\lib\hsqldb.jar org.hsqldb.util.%1 %2 %3 %4 %5 %6 %7 %8 %9

java -classpath ../lib/hsqldb.jar org.hsqldb.server.Server %1 %2 %3 %4 %5 %6 %7 %8 %9


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
    pushd "${serverdata}"
        gosu "${systemuser}" \
            java \
            -classpath ${derbylib} \
            -jar ${derbylib}/derbyrun.jar \
            server \
            start
    popd



 java -cp ../lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:mydb --dbname.0 xdb


#
# User command.
else

    echo ""
    echo "Running user command"
    
    exec "$@"

fi

