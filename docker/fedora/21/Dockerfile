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

FROM fedora:21
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

#
# Declare the system cache as a volume.
VOLUME /var/cache/

#
# Update (almost) everything (excluding filesystem).
# https://bugzilla.redhat.com/show_bug.cgi?id=1171928
RUN yum -y -x filesystem update

#
# Enable delta rpms.
RUN yum -y install deltarpm

#
# Add common tools.
RUN yum -y install sed
RUN yum -y install tar
RUN yum -y install wget
RUN yum -y install gzip
RUN yum -y install pwgen

#
# Clear the cache.
RUN yum -y clean all


