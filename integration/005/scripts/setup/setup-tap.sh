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
        -it \
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
        "firethorn/buildtap" \
        bash ./build-tap.sh


