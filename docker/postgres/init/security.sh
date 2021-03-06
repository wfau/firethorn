#!/bin/bash
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
#

echo ""
echo "--------------------"
echo "DEBUG : Security script"

#
# Change 'trust' to 'md5' for TCP connections.
echo "DEBUG : Changing 'trust' to 'md5'"
sed -r -i '
    /^host\s+all\s+all/ {
        s/trust/md5/
        }
    ' $PGDATA/pg_hba.conf

echo ""
echo "--------------------"
echo ""

