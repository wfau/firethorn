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

FROM firethorn/tester:2.1.36
LABEL MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

ENV pysrc=/home/firethorn-py

RUN dnf -y install \
    python3 \
    python3-pip

COPY . ${pysrc}
RUN  pip3 install numpy astropy requests
RUN  pip3 install --editable ${pysrc}

WORKDIR ${pysrc}


