*********************************** Installation of the database "demoasov" ***********************************

## Pre-requisites ##
	- PostgreSQL installed
	- PgSphere installed

Note: this installation procedure has been tested with Postgres 8.4 et PgSphere 1.1.1.

## Automatic installation ##

1. Execute the bash script "createDB.sh" with the name of the Postgres user who will own the database:
		./createDB.sh myself

IMPORTANT: the creation of the database may fail without warning. So, after the script execution, you should check that
the database "demoasov" exists and has only one table "data" which contains exactly 100 rows.

Note: This script writes the result of its commands in the log file "createDB.log".

## Manual installation ##

1. Create a new database called: "demoasov".

2. Install the types and functions of PgSphere into this new database:
		psql -d demoasov -f pg_sphere.sql

3. Follow the SQL commands of the file "createData_pattern.sql" after having replaced "<DUMPFILE>" by the absolute path of 
"dumpDemoBase.csv".

******************************************* Configuration of the JAVA classes ******************************************
Only the following constants of asov.DemoASOV need to be modified:

	public final static String DB_OWNER = System.getProperty("user.name");	// user name of the DB owner
	public final static String DB_PWD = "";									// password of the DB owner

	public final static String SERVER_NAME = "localhost:8080";				// name of the server on which the UWS and TAP examples will be executed

	public final static String RESULT_ID = "result";						// ID of the result
	public final static String RESULT_DIR = "DemoResults";					// path of the directory which will contains all result files
	public final static String RESULT_FILE_PREFIX = RESULT_ID;				// prefix of the result files' name
