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

FROM firethorn/fedora:21-1.2
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

#
# Create our projects directory.
RUN mkdir /var/local/projects
#RUN chgrp users /var/local/projects
#RUN chmod g+rwx /var/local/projects
VOLUME /var/local/projects

#
# Create our cache directory.
RUN mkdir /var/local/cache
#RUN chgrp users /var/local/cache
#RUN chmod g+rwx /var/local/cache
VOLUME /var/local/cache

#
# Install system tools.
RUN yum -y install iputils
RUN yum -y install bind-utils

#
# Install Docker client.
RUN yum -y install docker-io

#
# Install Mercurial
RUN yum -y install mercurial

#
# Install Java
RUN yum -y install java-1.8.0-openjdk-devel
RUN yum -y install java-1.8.0-openjdk-headless

#
# Install Maven
COPY bin/install-maven.sh /tmp/install-maven.sh
RUN /bin/bash -c 'source /tmp/install-maven.sh'


