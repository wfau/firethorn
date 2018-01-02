#!/bin/bash -eu
# -e: Exit immediately if a command exits with a non-zero status.
# -u: Treat unset variables as an error when substituting.
#
#  Copyright (C) 2017 Royal Observatory, University of Edinburgh, UK
#
#  This program is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
#


# -------------------------------------------------------------------------------------------
# Push our changes to our Maven repository.

    mvnport=22
    mvnuser=Zarquan
    mvnhost=data.metagrid.co.uk
    mvnpath=/var/local/websites/data/wfau/maven
    mvnrepo=${mvnuser:?}@${mvnhost:?}:/${mvnpath:?}

    mvnlocal=${mvnlocal:-'/var/local/cache/maven'}

    #
    # Sync the FireThorn binaries.
    rsync \
        --verbose \
        --checksum \
        --recursive \
        --copy-links \
        --stats --human-readable --progress \
        --prune-empty-dirs \
        --omit-dir-times \
        --include='/uk' \
        --include='/uk/ac' \
        --include='/uk/ac/roe' \
        --include='/uk/ac/roe/wfau' \
        --include='/uk/ac/roe/wfau/**' \
        --exclude='*' \
        "${mvnlocal:?}/" \
        "${mvnrepo:?}/firethorn"

    #
    # Sync the OGSA-DAI binaries.
    rsync \
        --verbose \
        --checksum \
        --recursive \
        --copy-links \
        --stats --human-readable --progress \
        --prune-empty-dirs \
        --omit-dir-times \
        --include='/uk' \
        --include='/uk/org' \
        --include='/uk/org/ogsadai' \
        --include='/uk/org/ogsadai/**' \
        --exclude='*' \
        "${mvnlocal:?}/" \
        "${mvnrepo:?}/ogsadai"

    #
    # Sync the STIL binaries.
    rsync \
        --verbose \
        --checksum \
        --recursive \
        --copy-links \
        --stats --human-readable --progress \
        --prune-empty-dirs \
        --omit-dir-times \
        --include='/uk' \
        --include='/uk/ac' \
        --include='/uk/ac/starlink' \
        --include='/uk/ac/starlink/**' \
        --exclude='*' \
        "${mvnlocal:?}/" \
        "${mvnrepo:?}/archive"

    #
    # Sync the 3rd party binaries.
    rsync \
        --verbose \
        --checksum \
        --recursive \
        --copy-links \
        --stats --human-readable --progress \
        --prune-empty-dirs \
        --omit-dir-times \
        --exclude='/uk/ac/roe/wfau/**' \
        --exclude='/uk/org/ogsadai/**' \
        --exclude='/.cache' \
        "${mvnlocal:?}/" \
        "${mvnrepo:?}/external"


