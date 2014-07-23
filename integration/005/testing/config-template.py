'''
Created on May 3, 2013

@author: stelios
'''
import os

#------------------------- General Configurations -----------------------#


### Unit test specific configuration ###
use_preset_params = True
use_cached_firethorn_env = True
firethorn_version =  "1.10.8"
include_neighbour_import = True


### Directory and URL Information ###
firethorn_host = "localhost"
firethorn_port = "8080"
full_firethorn_host = firethorn_host if firethorn_port=='' else firethorn_host + ':' + firethorn_port
base_location = os.getcwd()

### Email ###
test_email = ""

### Queries ###
sample_query=""
sample_query_expected_rows=0
limit_query = None

#------------------- Test Configurations ----------------------------------#

### SQL Database Configuration ###

test_dbserver= ""
test_dbserver_username = ""
test_dbserver_password = ""
test_dbserver_port = ""
test_database = ""
neighbours_query = """
            SELECT DISTINCT
                ExternalSurvey.databaseName
            FROM
                RequiredNeighbours
            JOIN
                ExternalSurvey
            ON
                RequiredNeighbours.surveyID = ExternalSurvey.surveyID
            JOIN
                ExternalSurveyTable
            ON
                RequiredNeighbours.surveyID = ExternalSurveyTable.surveyID
            AND
                RequiredNeighbours.extTableID = ExternalSurveyTable.extTableID
            WHERE 
                ExternalSurvey.databaseName!='NONE'
            ORDER BY
                ExternalSurvey.databaseName
                """
                
                
### Reporting Database Configuration ###

reporting_dbserver= ""
reporting_dbserver_username = ""
reporting_dbserver_password = ""
reporting_dbserver_port = ""
reporting_database = "pyrothorn_testing"


### Logged Queries Configuration ###

stored_queries_dbserver= ""
stored_queries_dbserver_username = ""
stored_queries_dbserver_password = ""
stored_queries_dbserver_port = ""
stored_queries_database = ""
stored_queries_query = "select top 10 * from [table] where [dbname] like 'atlas%'"
logged_queries_txt_file = "query_logs/atlas-logged-queries-short.txt"


### Firethorn Live test Configuration ###

adql_copy_depth = "THIN"
resourcename = 'Atlas JDBC conection' 
resourceuri = 'spring:RoeATLAS'
adqlspacename = 'Atlas Workspace' 
catalogname = '*'
ogsadainame = 'atlas'
jdbccatalogname = 'ATLASDR1'
jdbcschemaname = 'dbo'
metadocfile = "metadocs/ATLASDR1_TablesSchema.xml"
metadocdirectory = "testing/metadocs/"
stored_env_config = 'conf/pyrothorn-stored.js'

### Firethorn Predefined test Configuration ###

jdbcspace = ""
adqlspace = ""
adqlschema = ""
query_schema = ""
schema_name = ""
schema_alias = ""
