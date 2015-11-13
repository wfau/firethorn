
# -----------------------------------------------------
# Update our path.
#[root@builder]

    # ** this should be in the container **
    source /etc/bashrc

# -----------------------------------------------------
# Checkout a copy of our source code.
#[root@builder]

    #
    # Set the project path.
    if [ ! -e "${HOME:?}/firethorn.settings" ]
    then
        cat > "${HOME:?}/firethorn.settings" << EOF
FIRETHORN_CODE=/var/local/projects/firethorn
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

# -----------------------------------------------------
# Build our toolkit containers.
#[root@builder]

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
                --tag firethorn/builder:1.2 \
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

    popd

# -----------------------------------------------------
# Start our docker-proxy container.
#[root@builder]

    docker run \
        --detach \
        --name "docker-proxy" \
        --volume /var/run/docker.sock:/var/run/docker.sock \
        firethorn/docker-proxy:1.1

    dockerip=$(docker inspect -f '{{.NetworkSettings.IPAddress}}' docker-proxy)
    echo "docker-proxy [${dockerip:?}]"

    sleep 1
    curl "http://${dockerip:?}:2375/version"

# -----------------------------------------------------
# Build our webapp services.
#[root@builder]

    #
    # Build our webapp services.
    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        mvn clean install

    popd

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

# -----------------------------------------------------
# Build our tester container.
#[root@builder]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        source "bin/util.sh"

        if [ $(docker images | grep -c '^firethorn/tester') -eq 0 ]
        then
            echo "# ------"
            echo "# Building tester image"
            docker build \
               --tag firethorn/tester:$(getversion) \
               integration/tester
        fi
    popd

# -----------------------------------------------------
# Build our pyrothorn container.
#[root@builder]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        if [ $(docker images | grep -c '^firethorn/pyrothorn') -eq 0 ]
        then
            echo "# ------"
            echo "# Building pyrothorn image"
            docker build \
                --tag firethorn/pyrothorn:$(getversion) \
                integration/005/testing/pyrothorn

        fi
    popd

# -----------------------------------------------------
# Exit our builder.
#[root@builder]

    exit

# -----------------------------------------------------
