    echo "*** Initialising build script [build.sh] ***"
# -----------------------------------------------------
# Update our path.
#[root@builder]

    # ** this should be in the container **
    source /etc/bashrc

# -----------------------------------------------------
# Checkout a copy of our source code.
#[root@builder]
    echo "*** Checkout a copy of our source code. [build.sh] ***"

    #
    # Set the project path.
    if [ ! -e "${HOME:?}/firethorn.settings" ]
    then
        cat > "${HOME:?}/firethorn.settings" << EOF
FIRETHORN_CODE=/projects/firethorn
EOF
    fi

    #
    # Clone our repository.
    source "${HOME:?}/firethorn.settings"
    if [ ! -e "${FIRETHORN_CODE:?}" ]
    then
        pushd "$(dirname ${FIRETHORN_CODE:?})"

            hg clone 'http://wfau.metagrid.co.uk/code/firethorn'

        popd
    fi

    #
    # Pull and update from branch.
    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        hg pull
        hg update "${branch:?}"
        hg branch
    
    popd

    echo "*** Build our toolkit containers. [build.sh] ***"
# -----------------------------------------------------
# Build our toolkit containers.
#[root@builder]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        source "bin/util.sh"

        docker build \
            --tag firethorn/fedora:21 \
            docker/fedora/21

        docker build \
            --tag firethorn/java:8 \
            docker/java/8

        docker build \
            --tag firethorn/tomcat:8 \
            docker/tomcat/8

        docker build \
            --tag firethorn/postgres:9 \
            docker/postgres/9

        docker build \
            --tag firethorn/builder:1 \
            docker/builder

        docker build \
            --tag firethorn/docker-proxy:1 \
            docker/docker-proxy

        docker build \
            --tag firethorn/sql-proxy:1 \
            docker/sql-proxy

        docker build \
            --tag firethorn/sql-tunnel:1 \
            docker/sql-tunnel

        docker build \
            --tag firethorn/ssh-client:1 \
            docker/ssh-client

	docker build \
            --tag firethorn/pyrothorn:$(getversion) \
            integration/005/testing/pyrothorn
    popd

    echo "*** Start docker-proxy container. [build.sh] ***"
# -----------------------------------------------------
# Start our docker-proxy container.
#[root@builder]

    docker run \
        --detach \
        --name "docker-proxy" \
        --volume /var/run/docker.sock:/var/run/docker.sock \
        firethorn/docker-proxy:1

    dockerip=$(docker inspect -f '{{.NetworkSettings.IPAddress}}' docker-proxy)

    echo "${dockerip:?}"
    curl "http://${dockerip:?}:2375/version"


    echo "*** Build our webapp services. [build.sh] ***"
# -----------------------------------------------------
# Build our webapp services.
#[root@builder]

    #
    # Build our webapp services.
    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        mvn clean install

    popd


    echo "*** Build our webapp containers. [build.sh] ***"
# -----------------------------------------------------
# Build our webapp containers.
#[root@builder]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        pushd firethorn-ogsadai/webapp
            mvn -D "docker.host=http://${dockerip:?}:2375" docker:package
        popd
        
        pushd firethorn-webapp
            mvn -D "docker.host=http://${dockerip:?}:2375" docker:package
        popd

    popd


    echo "*** Build our tester container. [build.sh] ***"
# -----------------------------------------------------
# Build our tester container.
#[root@builder]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        source "bin/util.sh"

        docker build \
           --tag firethorn/tester:$(getversion) \
           integration/tester

    popd

# -----------------------------------------------------
# Exit our builder.
#[root@builder]

    exit

# -----------------------------------------------------
