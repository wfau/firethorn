#!/bin/bash
#

    #
    # Shell script function to configure an OGSA-DAI JDBC resource.
    jdbcconfig()
        {
        local resourcename=${1?}
        local propertyname=${2?}

        local webinfdir=${3:-'.'}
        local propsfile=${4:-"${HOME}/firethorn.properties"}

        local databaseurl="$(sed  -n 's|^'${propertyname?}'.url=\(.*\)|\1|p'  ${propsfile?})"
        local databasetype="$(sed -n 's|^'${propertyname?}'.type=\(.*\)|\1|p' ${propsfile?})"
        local databasename="$(sed -n 's|^'${propertyname?}'.name=\(.*\)|\1|p' ${propsfile?})"
        local databaseuser="$(sed -n 's|^'${propertyname?}'.user=\(.*\)|\1|p' ${propsfile?})"
        local databasepass="$(sed -n 's|^'${propertyname?}'.pass=\(.*\)|\1|p' ${propsfile?})"
        local databasedriver="$(sed -n 's|^'${propertyname?}'.driver=\(.*\)|\1|p' ${propsfile?})"

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
            ' "${webinfdir?}/etc/dai/resources/${resourcename?}"

        #
        # Set the login credentials.
        sed -i '
            /^id='${resourcename?}'/,/^END/ {
                s|^username=.*|username='${databaseuser?}'|
                s|^password=.*|password='${databasepass?}'|
                }
            ' "${webinfdir?}/etc/dai/logins.txt"
        }


