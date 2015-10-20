
echo "*** Initialising build scripts [setup-pyro.sh] ***"

source ${HOME:?}/chain.properties

echo "*** Create sql-proxy for stored queries database [setup-pyro.sh] ***"
# -----------------------------------------
# Create sql-proxy for stored queries database
#
docker run \
    --detach \
    --name "${storedqueriesname?}" \
    --env  "target=${storedquerieshost:?}" \
    firethorn/sql-proxy:1

echo "*** Run pyrosl MySQL container [setup-pyro.sh] ***"
# -----------------------------------------
# Run pyrosl MySQL container
#
docker run -d -t --name ${pyrosqlname:?} -p 3306:3306 firethorn/pyrosql


