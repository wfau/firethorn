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

FROM firethorn/fedora:2.1.10-zrq-junit-clean
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

#
# Create our build directory.
RUN mkdir -p /var/local/build
VOLUME /var/local/build

#
# Create our cache directory.
RUN mkdir -p /var/local/cache
VOLUME /var/local/cache

#
# Install admin tools.
RUN dnf -y install iputils
RUN dnf -y install bind-utils

#
# Install Docker components.
#RUN dnf -y install docker-io
#RUN dnf -y install docker-compose

#
# Install Docker CE from the Docker repository.
RUN dnf -y install dnf-plugins-core
RUN dnf -y config-manager \
    --add-repo \
    https://download.docker.com/linux/fedora/docker-ce.repo
RUN dnf -y makecache fast
RUN dnf -y install docker-ce

#
# Install Docker compose from GitHub.
RUN dnf -y install git
#UN curl -L -s https://github.com/docker/compose/releases/download/1.11.2/docker-compose-$(uname -s)-$(uname -m) > /usr/bin/docker-compose
RUN curl -L -s https://github.com/docker/compose/releases/download/1.13.0/docker-compose-$(uname -s)-$(uname -m) > /usr/bin/docker-compose
RUN chmod +x /usr/bin/docker-compose

#
# Install Mercurial
RUN dnf -y install mercurial

#
# Install Java JDK
RUN dnf -y install java-1.8.0-openjdk
RUN dnf -y install java-1.8.0-openjdk-devel
RUN dnf -y install java-1.8.0-openjdk-headless

#
# Install Maven
RUN dnf -y install maven
COPY maven-cache.sh /tmp 
RUN chmod a+x /tmp/maven-cache.sh
RUN /tmp/maven-cache.sh

#
# Install our build scripts
RUN mkdir -p  /builder
COPY bin/     /builder/bin 
RUN chmod a+x /builder/bin/*


