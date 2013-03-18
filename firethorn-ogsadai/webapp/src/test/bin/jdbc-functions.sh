#!/bin/bash
#

    #
    # Shell script function to configure an OGSA-DAI JDBC resource.
    jdbcconfig()
        {
        local resourcename=${1?}
        local databaseconf=${2?}

        local databasetype="$(sed -n 's/^type=\(.*\)/\1/p' ${databaseconf?})"
        local databasename="$(sed -n 's/^name=\(.*\)/\1/p' ${databaseconf?})"
        local databaseuser="$(sed -n 's/^user=\(.*\)/\1/p' ${databaseconf?})"
        local databasepass="$(sed -n 's/^pass=\(.*\)/\1/p' ${databaseconf?})"
        local databasehost="$(sed -n 's/^host=\(.*\)/\1/p' ${databaseconf?})"
        local databaseport="$(sed -n 's/^port=\(.*\)/\1/p' ${databaseconf?})"

        local jdbcdriver
        local jdbcstring

        case ${databasetype?} in

            pgsql)
                echo "Postgresql database"
                echo " Name [${databasename}]"
                echo " Host [${databasehost:-localhost}]"
                echo " Port [${databaseport:-5432}]"

                jdbcdriver=org.postgresql.Driver
                jdbcstring=jdbc:postgresql://${databasehost:-localhost}:${databaseport:-5432}/${databasename?}
                ;;

            mssql)
                echo "SQLServer database"
                echo " Name [${databasename}]"
                echo " Host [${databasehost:-localhost}]"
                echo " Port [${databaseport:-1433}]"

                jdbcdriver=net.sourceforge.jtds.jdbc.Driver
                jdbcstring=jdbc:jtds:sqlserver://${databasehost:-localhost}:${databaseport:-1433}/${databasename?}
                ;;

            *)  echo "ERROR : unknown database type [${databasetype}]"
                ;;
        esac

        echo " URI  [${jdbcstring}]"

        #
        # Set the database driver and url.
        sed -i '
            s|^dai.driver.class=.*|dai.driver.class='${jdbcdriver?}'|
            s|^dai.data.resource.uri=.*|dai.data.resource.uri='${jdbcstring?}'|
            ' "target/ogsadai-webapp-1.0-SNAPSHOT/WEB-INF/etc/dai/resources/${resourcename?}"

        #
        # Set the login credentials.
        sed -i '
            /^id='${resourcename?}'/,/^END/ {
                s|^username=.*|username='${databaseuser?}'|
                s|^password=.*|password='${databasepass?}'|
                }
            ' "target/ogsadai-webapp-1.0-SNAPSHOT/WEB-INF/etc/dai/logins.txt"
        }


