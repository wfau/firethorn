#
# Copyright (c) 2015, ROE (http://www.roe.ac.uk/)
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

FROM firethorn/tomcat:2.1.12
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

#
# ADD our webapp.
ADD ${project.build.finalName} /var/local/tomcat/webapps/ogsadai

#
# Change to root to modify permissions.
USER root

#
# Make our webapp directory writeable.
RUN chown -R tomcat:tomcat /var/local/tomcat/webapps/ogsadai/WEB-INF/etc/dai/
RUN chmod -R u+rw,g+rw     /var/local/tomcat/webapps/ogsadai/WEB-INF/etc/dai/

#
# Create our data directory (mot used)
RUN mkdir /var/local/tomcat/userdir
RUN chown -R tomcat:tomcat /var/local/tomcat/userdir
RUN chmod -R u+rw,g+rw     /var/local/tomcat/userdir

#
# Change back to the firethorn user.
USER tomcat

#
# Add our config properties
COPY ogsadai.properties /var/local/tomcat/ogsadai.properties

#
# Update the service health check.
HEALTHCHECK \
    --interval=30s \
    --timeout=5s \
    CMD curl --silent --head --fail http://localhost:8080/ogsadai/services || exit 1

