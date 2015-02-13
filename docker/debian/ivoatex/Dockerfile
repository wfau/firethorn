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

#
# Docker container to run the IvoaTex build environment. 
#
FROM notroot
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

#
# Disable interactive install.
ENV DEBIAN_FRONTEND noninteractive

#
# Install our build tools.
RUN apt-get update && apt-get -yq install \
    zip \
    make \
    gcc \
    libc-dev

#
# Install our HTML tools
RUN apt-get update && apt-get -yq install \
    xsltproc \
    libxml2-utils \
    imagemagick \
    ghostscript

#
# Install our LaTex tools.
RUN apt-get update && apt-get -yq install \
    texlive-latex-base \
    latex-xcolor \
    texlive-latex-extra \
    texlive-fonts-recommended \
    texlive-bibtex-extra

#
# Set our username and home directory.
ENV username texuser
ENV userhome /var/local/texdata

#
# Make our working directory a data volume.
VOLUME /var/local/texdata


