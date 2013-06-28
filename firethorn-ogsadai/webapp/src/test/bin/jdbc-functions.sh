#!/bin/bash
#

    #
    # Shell script function to configure an OGSA-DAI JDBC resource.
    jdbcconfig()
        {
        local resourcename=${1?}
        local propertyname=${2?}
        local propertyfile=${3?}

        local webappdir='target/firethorn-ogsadai-webapp-01.04-SNAPSHOT'

        local databaseurl="$(sed -n 's|^'${propertyname?}'.url=\(.*\)|\1|p'  ${propertyfile?})"
        local databasetype="$(sed -n 's|^'${propertyname?}'.type=\(.*\)|\1|p' ${propertyfile?})"
        local databasename="$(sed -n 's|^'${propertyname?}'.name=\(.*\)|\1|p' ${propertyfile?})"
        local databaseuser="$(sed -n 's|^'${propertyname?}'.user=\(.*\)|\1|p' ${propertyfile?})"
        local databasepass="$(sed -n 's|^'${propertyname?}'.pass=\(.*\)|\1|p' ${propertyfile?})"
        local databasedriver="$(sed -n 's|^'${propertyname?}'.driver=\(.*\)|\1|p' ${propertyfile?})"

        echo " Type [${databasetype?}]"
        echo " User [${databaseuser?}]"
        echo " Pass [${databasepass?}]"
        echo " Driv [${databasedriver?}]"
        echo " URL  [${databaseurl?}]"

        #
        # Set the database driver and url.
        sed -i '
            s|^dai.driver.class=.*|dai.driver.class='${databasedriver?}'|
            s|^dai.data.resource.uri=.*|dai.data.resource.uri='${databaseurl?}'|
            ' "${webappdir?}/WEB-INF/etc/dai/resources/${resourcename?}"

        #
        # Set the login credentials.
        sed -i '
            /^id='${resourcename?}'/,/^END/ {
                s|^username=.*|username='${databaseuser?}'|
                s|^password=.*|password='${databasepass?}'|
                }
            ' "${webappdir?}/WEB-INF/etc/dai/logins.txt"
        }


