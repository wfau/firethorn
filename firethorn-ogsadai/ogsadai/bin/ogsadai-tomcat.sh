#!/bin/bash
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
# Set our main path environment variables.
FIRETHORN_BASE=${FIRETHORN_BASE:-/var/local/projects/edinburgh/wfau/firethorn}
FIRETHORN_CODE=${FIRETHORN_BASE?}/devel

#
# Run the ogsa-dai webapp in Tomcat.
if [ ! -e "${FIRETHORN_CODE?}" ]
then
    echo "ERROR : can't find FIRETHORN source code at [${FIRETHORN_CODE?}]"
else
    pushd ${FIRETHORN_CODE?}/firethorn-ogsadai/webapp

        #
        # Create a clean war file.
        mvn clean compile war:war

        #
        # Set the database passwords.
        sed -i '
            s/{SQL-USER}/'$(cat ~/firethorn.properties | sed -n 's/firethorn.wfau.user=\(.*\)/\1/p')'/
            s/{SQL-PASS}/'$(cat ~/firethorn.properties | sed -n 's/firethorn.wfau.pass=\(.*\)/\1/p')'/
            ' target/ogsadai-webapp-1.0-SNAPSHOT/WEB-INF/etc/dai/logins.txt

        #
        # Run the webapp in Tomcat
        mvn tomcat6:run | tee /tmp/ogsadai-tomcat.log

    popd
fi


