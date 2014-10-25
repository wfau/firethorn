#!/bin/bash
#
# Copyright (c) 2014, ROE (http://www.roe.ac.uk/)
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

echo "DEBUG : Create database script"

#
# Set our config filename.
config=${config:=/config}

#
# Create our user role and database.
if [ ! -e "${config:?}" ]
then
    echo "ERROR : Missing config file [${config:?}]"
else 
    echo "DEBUG : Loading config [${config:?}]"
    source "${config:?}"

    echo "DEBUG : database [{$database:?}]"
    echo "DEBUG : username [{$username:?}]"
    echo "DEBUG : password [{$password:?}]"

    echo "DEBUG : Creating user role"
    postgresl --single postgres << EOF
CREATE ROLE ${username:?} ENCRYPTED PASSWORD '$(md5pass ${password:?})' NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT LOGIN ;
EOF

    echo "DEBUG : Creating database"
    postgresl --single postgres << EOF
CREATE DATABASE ${database:?} OWNER ${username:?} ;
EOF
fi


