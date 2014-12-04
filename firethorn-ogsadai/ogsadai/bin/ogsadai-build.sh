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

# ------------------------------------------------------------
# Configuration settings.
#

#cat > ${HOME?}/ogsadai.settings << 'EOF'
#OGSADAI_REV=${OGSADAI_REV:-HEAD}
#OGSADAI_REPO=${OGSADAI_REPO:-http://svn.code.sf.net/p/ogsa-dai/code}
#OGSADAI_BASE=${OGSADAI_BASE:-/var/local/projects/edinburgh/ogsa-dai}
#OGSADAI_NAME=${OGSADAI_NAME:-code}
#OGSADAI_CODE=${OGSADAI_CODE:-${OGSADAI_BASE?}/${OGSADAI_NAME}}
#EOF

#
# Load our local settings.
source "${HOME:?}/ogsadai.settings"

echo "----"
echo "Starting OGSA-DAI build process"
echo "OGSADAI_CODE is [${OGSADAI_CODE:?}]"
echo "OGSADAI_REPO is [${OGSADAI_REPO:?}]"

#
# Check we have the required source code checked out.
echo "----"
echo "Checking OGSA-DAI source code"
if [ ! -e "${OGSADAI_BASE:?}" ]
then
    mkdir "${OGSADAI_BASE:?}"
fi
if [ ! -e "${OGSADAI_CODE:?}" ]
then
    mkdir "${OGSADAI_CODE:?}"
fi

if [ ! -e "${OGSADAI_CODE:?}" ]
then
    echo "ERROR : can't find OGSA-DAI source directory at [${OGSADAI_CODE:?}]"
else

    checksource()
        {
        local codepath=${1:?}
        pushd "${OGSADAI_CODE:?}"
            echo "Checking [${codepath:?}]"
            if [ ! -e "${codepath:?}" ]
            then
                echo "Checkout [${codepath:?}]"
                svn checkout -r "${OGSADAI_REV:?}" "${OGSADAI_REPO:?}/${codepath:?}" "${codepath:?}"
            else
                echo "Updating [${codepath:?}]"
                svn update -r "${OGSADAI_REV:?}" "${codepath:?}"
            fi
        popd
        }

    checksource 'ogsa-dai/trunk'
    checksource 'sandbox/dqp/server'
    checksource 'third-party/dependencies'

fi

echo "----"
echo "Preparing OGSA-DAI source release"
if [ ! -e "${OGSADAI_CODE?}" ]
then
    echo "ERROR : can't find OGSA-DAI source code at [${OGSADAI_CODE?}]"
else

    pushd "${OGSADAI_CODE?}/ogsa-dai/trunk/release-scripts/ogsa-dai/jersey"

        #
        # Add the astro extension.
        echo "----"
        echo "Checking for astro extension in 'build.xml'"
        if [[ $(grep -c 'ZRQ 20130201' build.xml) -gt 0 ]]
        then
            echo "Astro extension already added to 'build.xml'"
        else
            echo "Adding astro extension to 'build.xml'"
            #   <!--+ ZRQ 20130201 +-->
            #   <copyModule path="extensions/astro"/>
            sed -i '
                /<target name="copyModules"/,/<\/target>/ {
                    /<echo message="\** DONE/i \
                <!--+ ZRQ 20130201 +--> \
                <copyModule path="extensions/astro"/> \
                <!--+ ZRQ 20130201 +--> \
                
                    }
                ' build.xml
        fi

        echo "----"
        echo "Checking for astro extension in 'source-ant/build.xml'"
        if [[ $(grep -c 'ZRQ 20130201' source-ant/build.xml) -gt 0 ]]
        then
            echo "Astro extension already added to 'source-ant/build.xml'"
        else
            echo "Adding astro extension to 'source-ant/build.xml'"
            #   <!--+ ZRQ 20130201 +-->
            #   <deployModule name="extensions/astro/client"/>
            #   <deployModule name="extensions/astro/server"/>
            #   <!--+ ZRQ 20130201 +-->
            sed -i '
                /<echo message="\** Deploying modules/,/<echo message="\** modules deployed/ {
                    /<echo message="\** modules deployed/i \
                        <!--+ ZRQ 20130201 +--> \
                        <deployModule name="extensions/astro/client"/> \
                        <deployModule name="extensions/astro/server"/> \
                        <!--+ ZRQ 20130201 +--> \
                        
                    }
                ' source-ant/build.xml
        fi
    popd
fi

echo "----"
echo "Builing OGSA-DAI source release"
if [ ! -e "${OGSADAI_CODE?}" ]
then
    echo "ERROR : can't find OGSA-DAI source code at [${OGSADAI_CODE?}]"
else

    pushd "${OGSADAI_CODE?}/ogsa-dai/trunk/release-scripts/ogsa-dai/jersey"

        #
        # Build a clean source release.
        echo "----"
        echo "Building clean source release"
        ant -Ddependencies.dir=${OGSADAI_CODE?}/third-party/dependencies clean buildSourceRelease

    popd
fi

echo "----"
echo "Modifying OGSA-DAI source release"

if [ ! -e "${OGSADAI_CODE?}" ]
then
    echo "ERROR : can't find OGSA-DAI source code at [${OGSADAI_CODE?}]"
else

    pushd "${OGSADAI_CODE?}/ogsa-dai/trunk/release-scripts/ogsa-dai/jersey"
        pushd 'build/ogsadai-4.2-jersey-1.10-src/'

            #
            # Replace the DQP server code with the sandbox version.
            echo "--"
            echo "Replacing DQP server code with sandbox version"
            pushd 'src/extensions/dqp'

                #
                # Replace dqp/server with the sandbox version.
                # Note - don't use a link, the Ant build assumes this directory as writeable. 
                # 'ant clean' will recursively delete everything from the original.
                rm -r server
                cp -r ${OGSADAI_CODE?}/sandbox/dqp/server .

                #
                # Remove references to 'ogsa-dai/trunk' from 'server/ant.properties'
                sed -i '
                    s|/ogsa-dai/trunk/|/|g
                    ' server/ant.properties
            popd

            #
            # Create a build.xml file for the astro client extension.
            echo "--"
            echo "Creating build.xml for the astro client extension"
            pushd 'src/extensions/astro/client'

                cp '../../basic/client/build.xml' '.'
                cp '../../basic/client/ant.properties' '.'

                sed -i '
                    s/OGSA-DAI basic extensions/OGSA-DAI astro extensions/
                    ' ant.properties

                sed -i '
                    s/name="basic-client"/name="astro-client"/
                    ' build.xml

                sed -i '
                    s/OGSA-DAI basic extensions/OGSA-DAI astro extensions/
                    ' build.xml

cat >> ant.properties << EOF
# ZRQ 20130201
apachehc.lib.dir=\${dependencies.dir}/org/apache/hc/4.0.1
starlink.lib.dir=\${dependencies.dir}/stil/2.8/
EOF

                sed -i '
                    /<path id="module.build.classpath">/,/<\/path>/ {
                        /<\/path>/i \
                            <!--+ ZRQ 20130201 +--> \
                            <fileset dir="${apachehc.lib.dir}"> \
                              <include name="**/*.jar"/> \
                            </fileset> \
                            <fileset dir="${starlink.lib.dir}"> \
                              <include name="**/*.jar"/> \
                            </fileset> \
                            <!--+ ZRQ 20130201 +--> \
                            
                        }
                    ' build.xml

            popd

            echo "--"
            echo "Creating build.xml for the astro server extension"
            pushd 'src/extensions/astro/server'

                cp '../../basic/server/build.xml' '.'
                cp '../../basic/server/ant.properties' '.'

                sed -i '
                    s/OGSA-DAI basic extensions/OGSA-DAI astro extensions/
                    ' ant.properties

                sed -i '
                    s/name="basic-server"/name="astro-server"/
                    ' build.xml

                sed -i '
                    s/OGSA-DAI basic extensions/OGSA-DAI astro extensions/
                    ' build.xml

cat >> ant.properties << EOF
# ZRQ 20130201
apachehc.lib.dir=\${dependencies.dir}/org/apache/hc/4.0.1
starlink.lib.dir=\${dependencies.dir}/stil/2.8/
EOF

                sed -i '
                    /<path id="module.build.classpath">/,/<\/path>/ {
                        /<\/path>/i \
                            <!--+ ZRQ 20130201 +--> \
                            <fileset dir="${apachehc.lib.dir}"> \
                              <include name="**/*.jar"/> \
                            </fileset> \
                            <fileset dir="${starlink.lib.dir}"> \
                              <include name="**/*.jar"/> \
                            </fileset> \
                            <!--+ ZRQ 20130201 +--> \
                            
                        }
                    ' build.xml

            popd

            #
            # Fix the astro module server-config.
            echo "--"
            echo "Fixing server-config for the astro server extension"
            pushd src/extensions/astro/server

                #
                # From the online documentation.
                # http://ogsa-dai.sourceforge.net/documentation/ogsadai4.2/ogsadai4.2-jersey/DeployerConfigFileSyntax.html

                    # Add activity
                    #Activity add ACTIVITY_ID CLASS DESCRIPTION

                    # Add activity
                    #Resource addActivity RESOURCE_ID ACTIVITY_NAME ACTIVITY_ID

                #
                # Add the CreateEmptyTupleList Activity to the list of activities. 
                sed -i '
                    /Activity add .*ADQLQuery/a \
Activity add uk.org.ogsadai.CreateEmptyTupleList uk.org.ogsadai.activity.astro.CreateEmptyTupleListActivity CreateEmptyTupleList
                    ' server-config/configuration.txt
                #
                # Add the CreateEmptyTupleList Activity to the list of activities. 
                sed -i '
                    /Resource addActivity .*ADQLQuery/a \
Resource addActivity DataRequestExecutionResource uk.org.ogsadai.CreateEmptyTupleList uk.org.ogsadai.CreateEmptyTupleList
                    ' server-config/configuration.txt

                #
                # Bug fix for the ADQLQuery activity ???
                # Don't know if this is right or not ....
                sed -i '
                    /Resource addActivity/ {
                        s/uk.org.ogsadai.activity.astro.ADQLQueryActivity/uk.org.ogsadai.ADQLQuery/g
                        }
                    ' server-config/configuration.txt

            popd
        popd
    popd
fi

echo "----"
echo "Building OGSA-DAI binary release"

if [ ! -e "${OGSADAI_CODE?}" ]
then
    echo "ERROR : can't find OGSA-DAI source code at [${OGSADAI_CODE?}]"
else

    pushd "${OGSADAI_CODE?}/ogsa-dai/trunk/release-scripts/ogsa-dai/jersey"
        pushd 'build/ogsadai-4.2-jersey-1.10-src/'

            #
            # Build the binary release using the modified source.
            echo "----"
            echo "Building binary release"
            ant -Ddependencies.dir=${OGSADAI_CODE?}/third-party/dependencies clean buildBinaryRelease

        popd
    popd
fi

echo "----"
echo "Deploying OGSA-DAI binaries into local Maven repository"

if [ ! -e "${OGSADAI_CODE?}" ]
then
    echo "ERROR : can't find OGSA-DAI source code at [${OGSADAI_CODE?}]"
else

    pushd "${OGSADAI_CODE?}/ogsa-dai/trunk/release-scripts/ogsa-dai/jersey"
        pushd 'build/ogsadai-4.2-jersey-1.10-src/'

            #
            # Transfer the resulting war and jar files into our local Maven repository.

            mvninstall()
                {
                local name=${1}
                local path=${2}
                local type=$(basename "${path?}" | sed 's/.*\.\([^.]*\)/\1/')

                local svninfo=$(svn info "${OGSADAI_CODE?}/ogsa-dai/trunk" | sed -n 's/Revision: \([:digit:]*\)/\1/p')
                local version="4.2.${svninfo?}"
                
                echo "Installing artifact"
                echo "  Name [${name}]"
                echo "  Type [${type}]"
                echo "  Vers [${version}]"
                echo "  File [${path}]"
                mvn install:install-file  \
                    -D "groupId=uk.org.ogsadai"  \
                    -D "artifactId=${name?}" \
                    -D "version=${version?}" \
                    -D "packaging=${type?}" \
                    -D "createChecksum=true" \
                    -D "file=${path?}"
                }

            mvninstall \
                'ogsadai-jersey-webapp' \
                'build/ogsadai-4.2-jersey-1.10-bin/dai.war'

            mvninstall \
                'ogsadai-jersey-client' \
                'src/presentation/jersey/client/build/lib/ogsadai-jersey-client-4.2.jar'

            mvninstall \
                'ogsadai-jersey-common' \
                'src/presentation/jersey/common/build/lib/ogsadai-jersey-common-4.2.jar'

            mvninstall \
                'ogsadai-server' \
                'src/core/server/build/lib/ogsadai-server-4.2.jar'

            mvninstall \
                'ogsadai-client' \
                'src/core/client/build/lib/ogsadai-client-4.2.jar'

            mvninstall \
                'ogsadai-clientserver' \
                'src/core/clientserver/build/lib/ogsadai-clientserver-4.2.jar'

            mvninstall \
                'ogsadai-common' \
                'src/core/common/build/lib/ogsadai-common-4.2.jar'

            mvninstall \
                'ogsadai-dqp-server' \
                'src/extensions/dqp/server/build/lib/ogsadai-dqp-server-4.2.jar'

            mvninstall \
                'ogsadai-basic-client' \
                'src/extensions/basic/client/build/lib/ogsadai-basic-client-4.2.jar'

            mvninstall \
                'ogsadai-basic-server' \
                'src/extensions/basic/server/build/lib/ogsadai-basic-server-4.2.jar'

            mvninstall \
                'ogsadai-astro-client' \
                'src/extensions/astro/client/build/lib/ogsadai-astro-client-4.2.jar'

            mvninstall \
                'ogsadai-astro-server' \
                'src/extensions/astro/server/build/lib/ogsadai-astro-server-4.2.jar'

            mvninstall \
                'ogsadai-relational-client' \
                'src/extensions/relational/client/build/lib/ogsadai-relational-client-4.2.jar'

            mvninstall \
                'ogsadai-relational-server' \
                'src/extensions/relational/server/build/lib/ogsadai-relational-server-4.2.jar'

            mvninstall \
                'ogsadai-dqp-client' \
                'src/extensions/dqp/client/build/lib/ogsadai-dqp-client-4.2.jar'

            mvninstall \
                'ogsadai-dqp-bindings' \
                'src/extensions/dqp/bindings/build/lib/ogsadai-dqp-jaxb-bindings-4.2.jar'

        popd
    popd
fi





