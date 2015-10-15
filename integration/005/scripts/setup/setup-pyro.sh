source /root/chain.properties

docker run \
    --detach \
    --name "${storedqueriesname?}" \
    --env  "target=${storedquerieshost:?}" \
    firethorn/sql-proxy:1


docker run -d -t --name ${pyrosqlname:?} -p 3306:3306 firethorn/pyrosql


