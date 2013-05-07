#!/bin/bash -eu
# -e: Exit immediately if a command exits with a non-zero status.
# -u: Treat unset variables as an error when substituting.
#
#  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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

#
# Create our user data store.
POST "/jdbc/resource/create" \
    -d "jdbc.resource.create.url=spring:FireThornUserData" \
    -d "jdbc.resource.create.name=userdate-$(unique)" \
    -d "jdbc.resource.create.ogsadai=user" \
    | tee jdbc-user-resource.json | ./pp
userresource=$(cat jdbc-user-resource.json | ident)

#
# Locale the user data schema.
# * Schema name is platform dependant.
# * Should come from the user accout or config anyway ....
#    -d "jdbc.resource.schema.select.name=public" \
#    -d "jdbc.resource.schema.select.name=PUBLIC.PUBLIC" \
#
# ** Needs at least one table in the schema.
userschema=$(
    GET "${userresource?}/schemas/select" \
    | ./pp  | ident
    )

