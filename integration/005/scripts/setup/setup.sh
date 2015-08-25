
# -----------------------------------------
# Install admin tools.
#
    yum -y install htop
    yum -y install pwgen
    
# -----------------------------------------------------
# Install and start the HAVEGE entropy generator.
# http://redmine.roe.ac.uk/issues/828
# http://blog-ftweedal.rhcloud.com/2014/05/more-entropy-with-haveged/
# http://stackoverflow.com/questions/26021181/not-enough-entropy-to-support-dev-random-in-docker-containers-running-in-boot2d/
#
    yum install -y haveged
    systemctl start haveged.service

# -----------------------------------------------------
# Install and start Docker.
#
    yum -y install docker-io

    systemctl enable docker.service
    systemctl start  docker.service
    systemctl status docker.service


# -----------------------------------------------------
# Remove existing docker containers and images
#

# Delete all containers
docker rm -f $(docker ps -a -q)
# Delete all images
docker rmi -f $(docker images -q)


# -----------------------------------------------------
# Create our projects directory.
#
    if [ ! -e /var/local/projects ]
    then
        mkdir -p /var/local/projects
        chgrp -R users /var/local/projects
        chmod -R g+rwx /var/local/projects
    fi

# -----------------------------------------------------
# Create our cache directory.
#

    if [ ! -e /var/local/cache ]
    then
        mkdir -p /var/local/cache
        chgrp -R users /var/local/cache
        chmod -R g+rwx /var/local/cache
    fi

# -----------------------------------------------------
# Allow access to Docker containers.
#
    chcon -t svirt_sandbox_file_t "/var/local/projects" 
    chcon -t svirt_sandbox_file_t "/var/local/cache" 

# -----------------------------------------------------
# Install the selinux-dockersock SELinux policy.
# https://github.com/dpw/selinux-dockersock
#
    # Test if present
    # semodule -l | grep dockersock

    yum install -y git
    yum install -y make
    yum install -y checkpolicy
    yum install -y policycoreutils policycoreutils-python
    
    pushd /var/local/projects

        git clone https://github.com/dpw/selinux-dockersock

        pushd selinux-dockersock

            make dockersock.pp

            semodule -i dockersock.pp

        popd
    popd

    chmod a+r "/root/setup/build.sh" 
    chcon -t svirt_sandbox_file_t "/root/setup/build.sh" 


    cat > /tmp/chain.properties << EOF

    version=${version:?}

    metaname=bethany
    username=patricia
    dataname=elayne
    ogsaname=jarmila
    firename=gillian
    pyroname=pyrothorn
    storedqueriesname=maria
    pyrosqlname=mikaela

    metalink=albert
    userlink=edward
    datalink=sebastien
    ogsalink=timothy
    firelink=peter
    storedquerieslink=john
    pyrosqllink=mike

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

    datatype=mssql
    datahost=$(secret 'firethorn.data.host')
    datadata=$(secret 'firethorn.data.data')
    datauser=$(secret 'firethorn.data.user')
    datapass=$(secret 'firethorn.data.pass')
    datadriver=net.sourceforge.jtds.jdbc.Driver
    dataport=1433

    pyrosqlport=3306
    
    storedqueriesport=1433
    storedquerieshost=$(secret 'pyrothorn.storedqueries.host')
    storedqueriesdata=$(secret 'pyrothorn.storedqueries.data')
    storedqueriesuser=$(secret 'pyrothorn.storedqueries.user')
    storedqueriespass=$(secret 'pyrothorn.storedqueries.pass')
    
    testrundatabase=$(secret 'firethorn.data.data')
    testrun_ogsadai_resource=$(secret 'firethorn.data.data')

    tunneluser=$(secret 'ssh.tunnel.user')
    tunnelhost=$(secret 'ssh.tunnel.host')

EOF

    source /tmp/chain.properties



# -----------------------------------------------------
# Run our build container.
#
    docker run \
        -it \
        --name builder \
        --env "branch=${branch:?}" \
        --env "version=${version:?}" \
        --volume /var/local/cache:/cache \
        --volume /var/local/projects:/projects \
        --volume /var/run/docker.sock:/var/run/docker.sock \
        --volume /root/setup/build.sh:/build.sh \
        firethorn/builder:1 \
        bash ./build.sh
