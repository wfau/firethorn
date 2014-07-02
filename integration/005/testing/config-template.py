'''
Created on May 3, 2013

@author: stelios
'''
import os

#------------------------- General Configurations -----------------------#


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

adql_copy_depth = "THIN"
resourcename = 'Atlas JDBC conection' 
resourceuri = 'spring:RoeATLAS'
adqlspacename = 'Atlas Workspace' 
catalogname = '*'
ogsadainame = 'atlas'
jdbccatalogname = 'ATLASDR1'
jdbcschemaname = 'dbo'
metadocfile = "/var/www/atlas/testing/metadocs/ATLASDR1_TablesSchema.xml"


### Firethorn Predefined test Configuration ###

jdbcspace = ""
adqlspace = ""
adqlschema = ""
starting_catalogue_id = ""
schema_name = ""
schema_alias = ""
