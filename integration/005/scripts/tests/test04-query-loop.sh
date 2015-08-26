source /tmp/chain.properties

testname=tester

chmod a+r "/root/tests/test04-nohup.sh" 
chcon -t svirt_sandbox_file_t "/root/tests/test04-nohup.sh" 


    docker run \
        --detach \
        --volume "/root/tests/test04-nohup.sh:/scripts/test04-nohup.sh" \
        --name "${testname:?}" \
        --env "datalink=${datalink:?}" \
        --env "datauser=${datauser:?}" \
        --env "datapass=${datapass:?}" \
        --env "datadriver=${datadriver:?}" \
        --env "endpointurl=http://${firelink:?}:8080/firethorn" \
        --link "${firename:?}:${firelink:?}" \
        "firethorn/tester:${version:?}" \
        bash  -c '/scripts/test04-nohup.sh 2>&1 | tee output.log'

