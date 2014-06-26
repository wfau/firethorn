'''
Created on May 3, 2013

@author: stelios
'''
import os

#------------------------- General Configurations -----------------------#

### Query Runtime and Polling Configurations ###
MAX_FILE_SIZE = 2048576000 
delay = 3
MIN_ELAPSED_TIME_BEFORE_REDUCE = 40
MAX_ELAPSED_TIME = 18000
MAX_DELAY = 15
INITIAL_DELAY = 2

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


### Firethorn Live test Configuration ###

adql_copy_depth = ""
resourcename = "" 
resourceuri = ""
adqlspacename = ""
catalogname = ""
ogsadainame = ""
jdbccatalogname = ""
jdbcschemaname = ""
metadocfile = ""


### Firethorn Predefined test Configuration ###

jdbcspace = ""
adqlspace = ""
adqlschema = ""
starting_catalogue_id = ""
schema_name = ""
schema_alias = ""
