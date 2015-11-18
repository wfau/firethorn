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

#FROM firethorn/notroot:1.1
FROM firethorn/fedora:21.1
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

#
# Create our projects directory.
VOLUME /var/local/projects

#
# Create our cache directory.
VOLUME /var/local/cache

#
# Install system tools.
RUN yum -y install iputils
RUN yum -y install bind-utils

#
# Install Docker client.
# https://blog.docker.com/2015/07/new-apt-and-yum-repos/
COPY etc/yum.repos.d/docker.repo /etc/yum.repos.d/docker.repo
RUN yum -y remove  docker-io
RUN yum -y install docker-engine

#
# Install SSH clients.
RUN yum -y install openssh-clients

#
# Install Mercurial
RUN yum -y install mercurial

#
# Install Java
RUN yum -y install java-1.8.0-openjdk-devel
RUN yum -y install java-1.8.0-openjdk-headless

#
# Install Maven
COPY bin/install /usr/local/bin/install
RUN chmod -R a+x /usr/local/bin/install/*.sh
RUN /usr/local/bin/install/install-maven.sh

#
# Install our build scripts.
COPY bin/build   /usr/local/bin/build
RUN chmod -R a+x /usr/local/bin/build/*.sh

#
# Set the working directory.
WORKDIR /usr/local/bin/build

