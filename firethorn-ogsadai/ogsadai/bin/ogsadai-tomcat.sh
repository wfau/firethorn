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
# Load our settings.
source "${HOME?}/firethorn.settings"

#
# Run the ogsa-dai webapp in Tomcat.
if [ ! -e "${FIRETHORN_CODE?}" ]
then
    echo "ERROR : can't find FIRETHORN source code at [${FIRETHORN_CODE?}]"
else

    pushd "${FIRETHORN_CODE?}/firethorn-ogsadai/webapp"

        #
        # Load the JDBC config functions.
        source src/test/bin/jdbc-functions.sh

        #
        # Create a clean war file.
        mvn clean compile war:war

        #
        # Configure our JDBC resources.
        jdbcconfig twomass firethorn.twomass "${HOME?}/firethorn.properties"
        jdbcconfig ukidss  firethorn.ukidss  "${HOME?}/firethorn.properties"
        jdbcconfig atlas   firethorn.atlas   "${HOME?}/firethorn.properties"
        jdbcconfig wfau    firethorn.wfau    "${HOME?}/firethorn.properties"
        jdbcconfig user    firethorn.user    "${HOME?}/firethorn.properties"

        #
        # Deploy the webapp in Tomcat.
        mvn tomcat6:run | tee /tmp/ogsadai-tomcat.log

    popd
fi


