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
        
        wget "http://apache.mirrors.tds.net/maven/maven-3/3.3.1/binaries/${tarfile:?}"

        tar -xvzf "${tarfile:?}"
        ln -s "${current:?}" current

        mkdir repository
        chgrp users repository
        chmod g+rws repository

        #
        # Set the repository path.
        sed -n '
            s|<localRepository>.*</localRepository>|<localRepository>'$(pwd)'/repository<\/localRepository>|
            ' "${current:?}/conf/settings.xml"

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

