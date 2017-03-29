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

# -----------------------------------------------------
# Initialise our paths.

    source /builder/bin/01.01-init.sh
    
# -----------------------------------------------------
# Checkout a copy of our source code.

    source /builder/bin/02.01-checkout.sh

# -----------------------------------------------------
# Set the build tag.

    source /builder/bin/03.01-buildtag.sh

# -----------------------------------------------------
# Update our POM version.

    source /builder/bin/03.02-versions.sh

# -----------------------------------------------------
# Build our base images.

    source /builder/bin/04.01-buildbase.sh

#---------------------------------------------------------------------
# Compile our Java code.

    source /builder/bin/05.01-javamaven.sh

# -----------------------------------------------------
# Build our Java containers.

    source /builder/bin/05.02-javadocker.sh

# -----------------------------------------------------
# Display our container images.

    docker \
        run -it \
            --rm \
            -v /var/run/docker.sock:/var/run/docker.sock \
            nate/dockviz \
            images --tree

