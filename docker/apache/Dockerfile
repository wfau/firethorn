#
# Copyright (c) 2018, ROE (http://www.roe.ac.uk/)
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
# Based on example from the official httpd Docker container.
# https://github.com/docker-library/httpd/blob/master/2.4/Dockerfile
FROM firethorn/fedora:2.1.25-stv-tap-fix
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

#
# Install Apache web server
RUN dnf -y install httpd

#
# Install our startup script.
COPY bin/httpd-foreground /usr/local/bin/
RUN chmod a+x /usr/local/bin/httpd-foreground

#
# Install our template config.
COPY cfg/proxy-template /etc/httpd/conf.d/

#
# Expose the HTTP port
EXPOSE 80

#
# Call our startup script.
CMD ["/usr/local/bin/httpd-foreground"]

