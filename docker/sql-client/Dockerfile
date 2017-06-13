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

# TODO replace this with a Fedora based container, building sqsh from source.
# http://www.bambams.ca/2010/07/sqshell-building-and-installing-sqsh-in.html

FROM centos:6
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

#
# Install the EPEL repository.
RUN yum -y install https://dl.fedoraproject.org/pub/epel/epel-release-latest-6.noarch.rpm

#
# Install misc tools.
RUN yum -y install sed
RUN yum -y install pwgen

#
# Install the sqsh binary.
#UN yum -y install http://downloads.sourceforge.net/project/sqsh/sqsh/sqsh-2.5/sqsh-2.5-1.el6.x86_64.x86_64.rpm
RUN yum -y install https://netix.dl.sourceforge.net/project/sqsh/sqsh/sqsh-2.5/sqsh-2.5-1.el6.x86_64.x86_64.rpm

#
# Install our sql scripts.
COPY sql/ /builder/sql 

WORKDIR /builder/sql
