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

version=3.3.1
current=apache-maven-${version:?}
tarfile=${current:?}-bin.tar.gz

#
# Install Maven
mkdir /toolkits
pushd /toolkits

    mkdir maven
    pushd maven
        
        wget "http://apache.mirrors.tds.net/maven/maven-3/${version:?}/binaries/${tarfile:?}"

        tar -xvzf "${tarfile:?}"
        ln -s "${current:?}" current

        # This doesn't work because /var/cache/ is a volume,
        # which gets recreated at runtime. 
        mkdir /var/local/cache/maven
        chgrp users /var/local/cache/maven
        chmod g+rws /var/local/cache/maven

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
        # Update the executable path.
cat > /etc/bashrc << EOF
#
# Apache Maven.
# http://maven.apache.org/
export M2_HOME=$(pwd)/current
export PATH=\${M2_HOME}/bin:\${PATH} 
EOF

    popd
popd

