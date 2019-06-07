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

# FROM latest (fedora:29)
# https://github.com/docker-library/repo-info/blob/master/repos/fedora/tag-details.md#fedoralatest
# FROM fedora@sha256:2a60898a6dd7da9964b0c59fedcf652e24bfff04142e5488f793c9e8156afd33
FROM fedora@sha256:7ae08e5637170eb47c01e315b6e64e0d48c6200d2942c695d0bee61b38c65b39

MAINTAINER [Dave Morris <docker-admin@metagrid.co.uk>]

#
# Mount the system cache as a volume.
#VOLUME /var/cache

#
# Update the DNF cache.
RUN dnf -y makecache

#
# Install DNF admin tools.
RUN dnf install -y dnf-plugins-core

#
# Install common admin tools.
RUN dnf install -y \
    findutils  \
    gnupg \
    gzip  \
    less  \
    nano  \
    pwgen \
    sed   \
    tar   \
    wget  \
    which \
    zip

#
# Add our install scripts.
COPY bin /usr/local/bin/
RUN chmod a+x /usr/local/bin/*.sh

#
# Run our install scripts.
RUN /usr/local/bin/install-locale.sh

#
# Set the terminal type for exec.
# https://github.com/docker/docker/issues/9299
ENV TERM xterm

