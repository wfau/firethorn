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

FROM firethorn/fedora:2.1.14
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

EXPOSE 1433

RUN dnf -y install curl
RUN dnf -y install socat
RUN dnf -y install iproute
RUN dnf -y install bind-utils
RUN dnf -y install openssh-clients

COPY bin/sql-proxy.sh /bin/sql-proxy.sh
RUN chmod a+x /bin/sql-proxy.sh

CMD /bin/sql-proxy.sh




