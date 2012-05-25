#!/bin/bash

testError(){
	if [ $? -ne 0 ];
	then
		echo "FAILED"
		exit $1
	else
		echo "OK"
	fi
}

if [ $# -ne 1 ];
then
	echo "Usage: bash createdb.sh <postgresUser>"
	exit 1
fi

DB_NAME="demoasov"
DB_OWNER=$1
LOG_FILE="createDB.log"

DUMP_FILE="`pwd`/dumpDemoBase.csv"
DUMP_FILE=${DUMP_FILE//\//\\\/}

# Create the database:
echo -n "1/4 - Creating the DB 'demoasov'..."
echo "***** 1/3 - CREATING THE DB 'DEMOASOV' *****" >$LOG_FILE
createdb -O $DB_OWNER $DB_NAME >>$LOG_FILE 2>&1
testError 2

# Install PgSphere types and functions in this DB:
echo -n "2/4 - Installing PgSphere features in 'demoasov'..."
echo "***** 2/4 - INSTALLING PGSPHERE FEATURES IN 'DEMOASOV' *****" >>$LOG_FILE
psql -U $DB_OWNER -d $DB_NAME -f pg_sphere.sql >>$LOG_FILE 2>&1
testError 3

# Create the file createData.sql from its pattern createData_pattern.sql (change the path of the dump file which must be an absolute path):
echo -n "3/4 - Configuring the table creation script..."
echo "***** 3/4 - CONFIGURING THE TABLE CREATION SCRIPT *****" >>$LOG_FILE
sed -e "s/<DUMPFILE>/$DUMP_FILE/" createData_pattern.sql > createData.sql 2>>$LOG_FILE
testError 4

# Update the column "coord" of demoasov (column type = spoint):
echo -n "4/4 - Creating the table 'data'..."
echo "***** 4/4 - CREATING THE TABLE 'DATA' *****" >>$LOG_FILE
psql -U $DB_OWNER -d $DB_NAME -f createData.sql >>$LOG_FILE 2>&1
testError 5

