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

FROM firethorn/fedora:2.1.18
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

RUN dnf -y install perl
RUN dnf -y install perl-JSON

RUN dnf install -y jq
RUN dnf install -y xmlstarlet

COPY bin/  /tester/bin 
COPY meta/ /tester/meta 
COPY vosi/ /tester/vosi 

ADD https://github.com/micha/resty/raw/master/pp    /tester/bin/
ADD https://github.com/micha/resty/raw/master/resty /tester/bin/

RUN chmod a+x /tester/bin/pp
RUN chmod a+x /tester/bin/resty

WORKDIR /tester
