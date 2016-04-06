#!/bin/sh
#
# Copyright (c) 2016, ROE (http://www.roe.ac.uk/)
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
#

#
# Strict error checking.
# http://redsymbol.net/articles/unofficial-bash-strict-mode/
set -euo pipefail
IFS=$'\n\t'

#
# Install directory
: ${derbypath:=/var/local/derby}

#
# Create our install directory.
echo "Checking Derby path [${derbypath}]"
if [ ! -e "${derbypath}" ]
then
    echo "Creating Derby path [${derbypath}]"
    mkdir -p "${derbypath}"
fi

#
# Use a temp directory.
tempdir=$(mktemp -d)
pushd "${tempdir:?}"

    echo ""
    echo "Downloading tar files"
    wget  --quiet "http://www-eu.apache.org/dist//db/derby/db-derby-${derbyversion}/db-derby-${derbyversion}-bin.tar.gz"
    wget  --quiet "http://www.apache.org/dist/db/derby/db-derby-${derbyversion}/db-derby-${derbyversion}-bin.tar.gz.asc"
    wget  --quiet "http://www.apache.org/dist/db/derby/db-derby-${derbyversion}/db-derby-${derbyversion}-bin.tar.gz.md5"

    #
    # Verify the signature.

    #
    # Unpack the tar file.
    tar --gzip \
        --extract \
        --directory "${derbypath}" \
        --file "db-derby-${derbyversion}-bin.tar.gz"

popd




