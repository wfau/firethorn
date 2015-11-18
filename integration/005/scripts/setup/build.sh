
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
# Define our docker image function.
#[root@builder]

    dockerimage()
        {
        local group=${1:?}
        local image=${2:?}
        local version=${3:?}
        docker inspect --type image --format {{.Id}} "${group}/${image}:${version}" 2> /dev/null    
        }

# -----------------------------------------------------
# Build our toolkit containers.
#[root@builder]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        if [ -z $(dockerimage firethorn fedora 21.1) ]
        then
            echo "# ------"
            echo "# Building Fedora image"
            docker build \
                --tag firethorn/fedora:21.1 \
                docker/fedora/21
        fi

        if [ -z $(dockerimage firethorn java 8.1) ]
        then
            echo "# ------"
            echo "# Building Java image"
            docker build \
                --tag firethorn/java:8.1 \
                docker/java/8
        fi

        if [ -z $(dockerimage firethorn tomcat 8.1) ]
        then
            echo "# ------"
            echo "# Building Tomcat image"
            docker build \
                --tag firethorn/tomcat:8.1 \
                docker/tomcat/8
        fi

        if [ -z $(dockerimage firethorn postgres 9) ]
        then
            echo "# ------"
            echo "# Building Postgres image"
            docker build \
                --tag firethorn/postgres:9 \
                docker/postgres/9
        fi

        if [ -z $(dockerimage firethorn builder 1.2) ]
        then
            echo "# ------"
            echo "# Building Builder image"
            docker build \
                --tag firethorn/builder:1.2 \
                docker/builder
        fi

        if [ -z $(dockerimage firethorn docker-proxy 1.1) ]
        then
            echo "# ------"
            echo "# Building docker-proxy image"
            docker build \
                --tag firethorn/docker-proxy:1.1 \
                docker/docker-proxy
        fi

        if [ -z $(dockerimage firethorn sql-proxy 1.1) ]
        then
            echo "# ------"
            echo "# Building sql-proxy image"
            docker build \
                --tag firethorn/sql-proxy:1.1 \
                docker/sql-proxy
        fi

        if [ -z $(dockerimage firethorn sql-tunnel 1.1) ]
        then
            echo "# ------"
            echo "# Building sql-tunnel image"
            docker build \
                --tag firethorn/sql-tunnel:1.1 \
                docker/sql-tunnel
        fi

        if [ -z $(dockerimage firethorn ssh-client 1.1) ]
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

        if [ -z $(dockerimage firethorn tester ${version:?}) ]
        then
            echo "# ------"
            echo "# Building tester image"
            docker build \
               --tag firethorn/tester:${version:?} \
               integration/tester
        fi
    popd

# -----------------------------------------------------
# Build our pyrothorn container.
#[root@builder]

    source "${HOME:?}/firethorn.settings"
    pushd "${FIRETHORN_CODE:?}"

        if [ -z $(dockerimage firethorn pyrothorn ${version:?}) ]
        then
            echo "# ------"
            echo "# Building pyrothorn image"
            docker build \
                --tag firethorn/pyrothorn:${version:?} \
                integration/005/testing/pyrothorn

        fi
    popd

# -----------------------------------------------------
# Exit our builder.
#[root@builder]

    exit

# -----------------------------------------------------
