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
    /var/local/toolkits/maven/repository/ \
    data.metagrid.co.uk:/var/local/websites/data/wfau/maven/firethorn

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
    /var/local/toolkits/maven/repository/ \
    data.metagrid.co.uk:/var/local/websites/data/wfau/maven/ogsadai

rsync \
    --recursive --copy-links --checksum \
    --stats --human-readable --progress \
    --prune-empty-dirs \
    --omit-dir-times \
    --exclude='/uk/ac/roe/wfau/**' \
    --exclude='/uk/org/ogsadai/**' \
    --include='*' \
    /var/local/toolkits/maven/repository/ \
    data.metagrid.co.uk:/var/local/websites/data/wfau/maven/external

