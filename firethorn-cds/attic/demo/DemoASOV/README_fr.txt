*********************************** Installation de la Base de Donnees "demoasov" ***********************************

## Pre-requis ##
	- PostgreSQL deja installe
	- PgSphere deja installe

Remarque: cette procedure d'installation a ete testee avec la version 8.4 de Postgres et 1.1.1 de PgSphere.

## Installation automatique ##

1. Executer le script bash "createDB.sh" avec comme le nom de l'utilisateur Postgres qui sera son proprietaire:
		./createDB.sh myself

IMPORTANT: la creation de la base peut parfois echouer sans le script ne le signale. Verifier toujours apres execution
que la base de donnees "demoasov" existe bien et qu'elle contient une seule table appelee "data" qui compte exactement
100 lignes.

Remarque: En cas d'echec, vous pouvez consulter le fichier de log "createDB.log".

## Installation manuelle ##

1. Creer une nouvelle base de donnees appelee: "demoasov".

2. Installer les fonctions et types de PgSphere dans cette base:
		psql -d demoasov -f pg_sphere.sql

3. Suivre les commandes SQL du fichier "createData_pattern.sql" en remplacant "<DUMPFILE>" par le chemin absolu vers le fichier
"dumpDemoBase.csv".

******************************************* Configuration des classes JAVA ******************************************
Seuls les constantes suivantes de la classe asov.DemoASOV ont besoin d'être modifié:

	public final static String DB_OWNER = System.getProperty("user.name");	// user name of the DB owner
	public final static String DB_PWD = "";									// password of the DB owner

	public final static String SERVER_NAME = "localhost:8080";				// name of the server on which the UWS and TAP examples will be executed

	public final static String RESULT_ID = "result";						// ID of the result
	public final static String RESULT_DIR = "DemoResults";					// path of the directory which will contains all result files
	public final static String RESULT_FILE_PREFIX = RESULT_ID;				// prefix of the result files' name
