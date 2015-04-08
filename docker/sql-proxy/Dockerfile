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

FROM firethorn/fedora:21
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

EXPOSE 1433

RUN yum -y install socat
RUN yum -y install bind-utils

# Add a proxy to the SQLServer server.
# http://www.dest-unreach.org/socat/doc/socat.html
CMD socat "TCP-LISTEN:1433,fork,reuseaddr" "TCP:$(host -t A \${target:?} | cut -d ' ' -f 4):1433" && top




