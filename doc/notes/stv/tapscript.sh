source secret.sh
 
 docker rm -vf $(docker ps -aq)

  source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        if [ $(docker images | grep -c '^firethorn/fedora') -eq 0 ]
        then
            echo "# ------"
            echo "# Building Fedora image"
            docker build \
                --tag firethorn/fedora:21.1 \
                docker/fedora/21
        fi

        if [ $(docker images | grep -c '^firethorn/java') -eq 0 ]
        then
            echo "# ------"
            echo "# Building Java image"
            docker build \
                --tag firethorn/java:8.1 \
                docker/java/8
        fi

        if [ $(docker images | grep -c '^firethorn/tomcat') -eq 0 ]
        then
            echo "# ------"
            echo "# Building Tomcat image"
            docker build \
                --tag firethorn/tomcat:8.1 \
                docker/tomcat/8
        fi

        if [ $(docker images | grep -c '^firethorn/postgres') -eq 0 ]
        then
            echo "# ------"
            echo "# Building Postgres image"
            docker build \
                --tag firethorn/postgres:9 \
                docker/postgres/9
        fi

        if [ $(docker images | grep -c '^firethorn/builder') -eq 0 ]
        then
            echo "# ------"
            echo "# Building Builder image"
            docker build \
                --tag firethorn/builder:1.1 \
                docker/builder
        fi

        if [ $(docker images | grep -c '^firethorn/docker-proxy') -eq 0 ]
        then
            echo "# ------"
            echo "# Building docker-proxy image"
            docker build \
                --tag firethorn/docker-proxy:1.1 \
                docker/docker-proxy
        fi

        if [ $(docker images | grep -c '^firethorn/sql-proxy') -eq 0 ]
        then
            echo "# ------"
            echo "# Building sql-proxy image"
            docker build \
                --tag firethorn/sql-proxy:1.1 \
                docker/sql-proxy
        fi

        if [ $(docker images | grep -c '^firethorn/sql-tunnel') -eq 0 ]
        then
            echo "# ------"
            echo "# Building sql-tunnel image"
            docker build \
                --tag firethorn/sql-tunnel:1.1 \
                docker/sql-tunnel
        fi

        if [ $(docker images | grep -c '^firethorn/ssh-client') -eq 0 ]
        then
            echo "# ------"
            echo "# Building ssh-client image"
            docker build \
                --tag firethorn/ssh-client:1.1 \
                docker/ssh-client
        fi

   	source "bin/util.sh"

        if [ $(docker images | grep -c '^integration/tester') -eq 0 ]
        then
            echo "# ------"
            echo "# Building tester image"
            docker build \
              --tag firethorn/tester:1.1 \
              integration/tester
        fi


    popd
popd

 docker run \
        --detach \
        --name "docker-proxy" \
        --volume /var/run/docker.sock:/var/run/docker.sock \
        firethorn/docker-proxy:1.1

    sleep 1
    dockerip=$(docker inspect -f '{{.NetworkSettings.IPAddress}}' docker-proxy)

    echo "${dockerip:?}"
    curl "http://${dockerip:?}:2375/version"

# -----------------------------------------------------
# Build our source code.
#[user@desktop]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        mvn clean install

    popd

# -----------------------------------------------------
# Build our webapp containers.
#[user@desktop]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        pushd firethorn-ogsadai/webapp
            mvn -D "docker.host=http://${dockerip:?}:2375" docker:package
        popd
        
        pushd firethorn-webapp
            mvn -D "docker.host=http://${dockerip:?}:2375" docker:package
        popd

    popd



# -----------------------------------------------------
# Get our branch and version number.
#[user@desktop]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        branch=$(hg branch)

        source "bin/util.sh"
        version=$(getversion)

    popd

# -----------------------------------------------------
# Configure our docker chain.
#[user@desktop]

    cat > "${HOME:?}/chain.properties" << EOF

    metaname=bethany
    username=patricia
    dataname=elayne
    ogsaname=jarmila
    firename=gillian

    metalink=albert
    userlink=edward
    datalink=sebastien
    ogsalink=timothy
    firelink=peter

    metatype=mssql
    metadata=$(secret 'firethorn.meta.data')
    metauser=$(secret 'firethorn.meta.user')
    metapass=$(secret 'firethorn.meta.pass')
    metaport=1433
    metadriver=net.sourceforge.jtds.jdbc.Driver

    usertype=mssql
    userhost=$(secret 'firethorn.user.host')
    userdata=$(secret 'firethorn.user.data')
    useruser=$(secret 'firethorn.user.user')
    userpass=$(secret 'firethorn.user.pass')
    userdriver=net.sourceforge.jtds.jdbc.Driver
    userport=1433

    datatype=mssql
    datahost=$(secret 'firethorn.data.host')
    datadata=$(secret 'firethorn.data.data')
    datauser=$(secret 'firethorn.data.user')
    datapass=$(secret 'firethorn.data.pass')
    datadriver=net.sourceforge.jtds.jdbc.Driver
    dataport=1433	

    tunneluser=$(secret 'ssh.tunnel.user')
    tunnelhost=$(secret 'ssh.tunnel.host')

EOF

# -----------------------------------------------------
# Create our FireThorn config.
#[user@desktop]

    source "${HOME:?}/chain.properties"

    cat > "${HOME:?}/firethorn.properties" << EOF

firethorn.ogsadai.endpoint=http://${ogsalink:?}:8080/ogsadai/services


firethorn.limits.time.default=60000
firethorn.limits.time.absolute=600000
firethorn.limits.rows.default=1000
firethorn.limits.rows.absolute=100000

firethorn.meta.url=jdbc:jtds:sqlserver://${userlink:?}/${metadata:?}
firethorn.meta.user=${metauser:?}
firethorn.meta.pass=${metapass:?}
firethorn.meta.driver=net.sourceforge.jtds.jdbc.Driver
firethorn.meta.type=mssql

firethorn.user.url=jdbc:jtds:sqlserver://${userlink:?}/${userdata:?}
firethorn.user.user=${useruser:?}
firethorn.user.pass=${userpass:?}
firethorn.user.driver=net.sourceforge.jtds.jdbc.Driver
firethorn.user.type=mssql

EOF





# -----------------------------------------------------
# Start our userdata ambassador.
#[user@desktop]

    source "${HOME:?}/chain.properties"
    docker run \
        --detach \
        --interactive \
        --name "${username:?}" \
        --env  "tunneluser=${tunneluser:?}" \
        --env  "tunnelhost=${tunnelhost:?}" \
        --env  "targethost=${userhost:?}" \
        --volume "${SSH_AUTH_SOCK}:/tmp/ssh_auth_sock" \
        firethorn/sql-tunnel:1.1

# -----------------------------------------------------
# Start our science data ambassador.
#[user@desktop]

    source "${HOME:?}/chain.properties"
    docker run \
        --detach \
        --interactive \
        --name "${dataname:?}" \
        --env  "tunneluser=${tunneluser:?}" \
        --env  "tunnelhost=${tunnelhost:?}" \
        --env  "targethost=${datahost:?}" \
        --volume "${SSH_AUTH_SOCK}:/tmp/ssh_auth_sock" \
        firethorn/sql-tunnel:1.1

# -----------------------------------------------------
# Start our PostgreSQL metadata container.
#[user@desktop]

#    source "${HOME:?}/chain.properties"
#    docker run \
#        --detach \
#        --name "${metaname:?}" \
#        --env "POSTGRES_USER=${metauser:?}" \
#        --env "POSTGRES_PASSWORD=${metapass:?}" \
#        postgres

# -----------------------------------------------------
# Start our OGSA-DAI container.
#[root@virtual]

    source "${HOME:?}/chain.properties"

    docker run \
        --detach \
        --publish 8081:8080 \
        --name "${ogsaname:?}" \
        --link "${dataname:?}:${datalink:?}" \
        --link "${username:?}:${userlink:?}" \
        --volume /etc/localtime:/etc/localtime:ro \
        "firethorn/ogsadai:${version:?}"

# -----------------------------------------------------
# Start our FireThorn container.
#[user@desktop]

    source "${HOME:?}/chain.properties"

    properties=${HOME:?}/firethorn.properties

    docker run \
        --detach \
        --publish 8080:8080 \
        --name "${firename:?}" \
        --link "${ogsaname:?}:${ogsalink:?}" \
        --link "${dataname:?}:${datalink:?}" \
        --link "${username:?}:${userlink:?}" \
        --volume "${properties:?}:/etc/firethorn.properties" \
        --volume /etc/localtime:/etc/localtime:ro \
        "firethorn/firethorn:${version:?}"

# -----------------------------------------------------
# Check the logs ..
#[user@desktop]

# Separate terminal
#   source "${HOME:?}/chain.properties"
#   docker exec -it gillian tail -f logs/firethorn.log 


# Separate terminal
#   source "${HOME:?}/chain.properties"
#   docker exec -it  jarmila tail -f logs/ogsadai.log 

# -----------------------------------------------------
# Build our tester container.
#[user@desktop]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        source "bin/util.sh"

        docker build \
            --tag firethorn/tester:1.1 \
            integration/tester

    popd

# -----------------------------------------------------
# Start our test container.
#[user@desktop]

    source "${HOME:?}/chain.properties"
    docker run \
        --rm \
        --tty \
        --interactive \
        --env "datadata=${datadata:?}" \
        --env "datalink=${datalink:?}" \
        --env "datauser=${datauser:?}" \
        --env "datapass=${datapass:?}" \
        --env "datadriver=${datadriver:?}" \
        --env "metadataurl=jdbc:jtds:sqlserver://${userlink:?}" \
        --env "metauser=${metauser:?}" \
        --env "metapass=${metapass:?}" \
        --env "metadata=${metadata?}" \
        --env "endpointurl=http://${firelink:?}:8080/firethorn" \
        --link "${firename:?}:${firelink:?}" \
        --volume /etc/localtime:/etc/localtime:ro \
        "firethorn/tester:1.1" \
        bash

# -----------------------------------------------------
# Configure our identity.
#[root@tester]

        identity=${identity:-$(date '+%H:%M:%S')}
        community=${community:-$(date '+%A %-d %B %Y')}

        source "bin/01-01-init-rest.sh"

# -----------------------------------------------------
# Check the system info.
#[root@tester]

        curl \
            --header "firethorn.auth.identity:${identity:?}" \
            --header "firethorn.auth.community:${community:?}" \
            "${endpointurl:?}/system/info" \
            | bin/pp | tee system-info.json

# -----------------------------------------------------
# Load the ATLASDR1 resource.
#[root@tester]

        database=ATLASDR1
        
        source "bin/02-02-create-jdbc-space.sh" \
            'Atlas JDBC conection' \
            "jdbc:jtds:sqlserver://${datalink:?}/${database:?}" \
            "${datauser:?}" \
            "${datapass:?}" \
            "${datadriver:?}" \
            '*'
        atlasjdbc=${jdbcspace:?}

        source "bin/03-01-create-adql-space.sh" 'Atlas ADQL workspace'
        atlasadql=${adqlspace:?}

        source "bin/03-04-import-jdbc-metadoc.sh" "${atlasjdbc:?}" "${atlasadql:?}" 'ATLASDR1' 'dbo' "meta/ATLASDR1_AtlasSource.xml"

        source "bin/03-04-import-jdbc-metadoc.sh" "${atlasjdbc:?}" "${atlasadql:?}" 'ATLASDR1' 'dbo' "meta/ATLASDR1_AtlasTwomass.xml"



# -----------------------------------------------------
# Create our workspace
#[root@tester]

        source "bin/04-01-create-query-space.sh" 'Test workspace'
        source "bin/04-03-import-query-schema.sh" "${atlasadql:?}" 'ATLASDR1' 'atlas'


# -----------------------------------------------------
# Run an ATLASDR1 query
#[root@tester]

        source "bin/04-03-create-query-schema.sh"

        source "bin/05-03-execute-query.sh" \
            "AUTO" \
            "
            SELECT
                atlasSource.ra,
                atlasSource.dec
            FROM
                atlas.atlasSource
            WHERE
                atlasSource.ra  BETWEEN 354 AND 355
            AND
                atlasSource.dec BETWEEN -40 AND -39
            "



	# -----------------------------------------------------
	# Testing TAP

	resourceid=$(basename ${queryspace:?}) 

	query="SELECT+top+5+ra+from+atlasSource"
	format=VOTABLE
	lang=ADQL
	request=doQuery

        # Get VOSI
	curl ${endpointurl:?}/tap/${resourceid:?}/tables
