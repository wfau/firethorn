#
# These scripts are experimental and not ready for production use.
# Do not rely on these for day to day use.
#


    # NotRoot doesn't work, can't write to docker.sock.
    # /var/run/docker.sock
    
    # --env "useruid=$(id -u)" \
    # --env "usergid=$(id -g)" \
    # --env "username=$(id -un)" \

    # Might be better with gosu
    # https://github.com/tianon/gosu/releases

    # Work on mapping users is ongoing
    # https://github.com/docker/docker/pull/4572
    # https://github.com/docker/docker/pull/12648
    # https://github.com/docker/docker/issues/15187
    # 
    # https://groups.google.com/forum/#!msg/docker-user/EUndR1W5EBo/4hmJau8WyjAJ
    #
    # ContainerCon August 2015 
    # https://events.linuxfoundation.org/sites/events/files/slides/User%20Namespaces%20-%20ContainerCon%202015%20-%2016-9-final_0.pdf
    #
    # Docker 1.9
    # https://github.com/docker/docker/releases/tag/v1.9.0

