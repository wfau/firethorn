#!/bin/sh
# Filter to update Mercurial comment during transplant.
#
sed -i '
    $ a\Imported change from [${srcbranch}]
    ' "$1"
