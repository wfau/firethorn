#!/bin/bash
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

# -----------------------------------------------------
# Error checking.
# http://stackoverflow.com/a/5195826
#[root@builder]

    set -e -u

# -----------------------------------------------------
# Install Maven
#[root@builder]

    version=3.3.3
    current=apache-maven-${version:?}
    tarfile=${current:?}-bin.tar.gz

    mkdir /var/local/toolkits
    pushd /var/local/toolkits

        mkdir maven
        pushd maven
        
            wget --quiet "http://apache.mirrors.tds.net/maven/maven-3/${version:?}/binaries/${tarfile:?}"

            tar -xvzf "${tarfile:?}"
            ln -s "${current:?}" current

            # This doesn't work because it uses a volume which gets created at runtime. 
            #mkdir /var/local/cache/maven
            #chgrp users /var/local/cache/maven
            #chmod g+rws /var/local/cache/maven

            #
            # Set the repository path.
            # This doesn't work .. need to uncomment the element.
            sed -n '
                s|<localRepository>.*</localRepository>|<localRepository>/var/local/cache/maven</localRepository>|
                ' "${current:?}/conf/settings.xml"

            #
            # Temp fix.
            mkdir /root/.m2/
            cat > /root/.m2/settings.xml<< 'EOF'
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
        http://maven.apache.org/SETTINGS/1.0.0
        http://maven.apache.org/xsd/settings-1.0.0.xsd
        ">
    <localRepository>/var/local/cache/maven</localRepository>
</settings>
EOF

            #
            # Create our start script.
            cat > /usr/local/bin/mvn << EOF
# Maven start script
export M2_HOME=$(pwd)/current
\$M2_HOME/bin/mvn "\$@"
EOF
            chmod a+x /usr/local/bin/mvn

        popd
    popd

