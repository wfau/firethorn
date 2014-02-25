#!/bin/bash
#
# Copyright (c) 2012, ROE (http://www.roe.ac.uk/)
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
# Shell script to rsync from our local Maven repository to our public repository.

#
# Load the local settings.
source "${HOME:?}/firethorn.settings"

#
# Update the Maven repo location.
MAVEN_REPO=${MAVEN_REPO:-"${HOME:?}/.m2/repository"}

#
# Sync the FireThorn binaries.
rsync \
    --recursive --copy-links --checksum \
    --stats --human-readable --progress \
    --prune-empty-dirs \
    --omit-dir-times \
    --include='/uk' \
    --include='/uk/ac' \
    --include='/uk/ac/roe' \
    --include='/uk/ac/roe/wfau' \
    --include='/uk/ac/roe/wfau/**' \
    --exclude='*' \
    --exclude='.cache' \
    "${MAVEN_REPO:?}/" \
    data.metagrid.co.uk:/var/local/websites/data/wfau/maven/firethorn

#
# Sync the OGSA-DAI binaries.
rsync \
    --recursive --copy-links --checksum \
    --stats --human-readable --progress \
    --prune-empty-dirs \
    --omit-dir-times \
    --include='/uk' \
    --include='/uk/org' \
    --include='/uk/org/ogsadai' \
    --include='/uk/org/ogsadai/**' \
    --exclude='*' \
    --exclude='.cache' \
    "${MAVEN_REPO:?}/" \
    data.metagrid.co.uk:/var/local/websites/data/wfau/maven/ogsadai

#
# Sync the 3rd party binaries.
rsync \
    --recursive --copy-links --checksum \
    --stats --human-readable --progress \
    --prune-empty-dirs \
    --omit-dir-times \
    --exclude='/uk/ac/roe/wfau/**' \
    --exclude='/uk/org/ogsadai/**' \
    --include='*' \
    --exclude='.cache' \
    "${MAVEN_REPO:?}/" \
    data.metagrid.co.uk:/var/local/websites/data/wfau/maven/external

