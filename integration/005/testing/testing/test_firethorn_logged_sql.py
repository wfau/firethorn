try:
    import sys, os
    srcdir = '../src/'
    configdir = '../'
    testdir = os.path.dirname(__file__)
    sys.path.insert(0, os.path.dirname(__file__))
    sys.path.insert(0, os.path.abspath(os.path.join(testdir, srcdir)))
    sys.path.insert(0, os.path.abspath(os.path.join(testdir, configdir)))
    import os.path
    from selenium import webdriver
    from selenium.webdriver.common.by import By
    from selenium.webdriver.common.keys import Keys
    from selenium.webdriver.support.ui import Select
    from selenium.common.exceptions import NoSuchElementException
    import unittest, time, re
    from selenium.webdriver.common.action_chains import ActionChains
    from selenium.webdriver.support.ui import WebDriverWait # available since 2.4.0
    from selenium.webdriver.support import expected_conditions as EC # available since 2.26.0
    import logging
    import urllib2
    import json
    import pyrothorn
    import urllib
    from pyrothorn import firethornEngine
    from pyrothorn import queryEngine
    from mssql import sqlEngine
    import config
    import uuid
    from time import gmtime,  strftime
    import datetime
    import base64
    import collections
    # get a UUID - URL safe, Base64
    def get_a_uuid():
        r_uuid = base64.urlsafe_b64encode(uuid.uuid4().bytes)
        return r_uuid.replace('=', '')
except Exception as e:
    logging.exception(e)




class test_firethorn(unittest.TestCase):
            
               
    def setUp(self):
        self.use_preset_params = config.use_preset_params
        self.use_cached_firethorn_env = config.use_cached_firethorn_env
        self.firethorn_version = config.firethorn_version
        self.total_failed = 0
        self.include_neighbours = config.include_neighbour_import
        self.verificationErrors = []
        self.sample_query=config.sample_query
        self.sample_query_expected_rows=config.sample_query_expected_rows
        self.setUpLogging()
   
   
    def test_sql_logged_queries(self):
        
        try:
            # Set query run ID
            queryrunID = get_a_uuid() 
            logged_query_sqlEng = sqlEngine.SQLEngine(config.test_dbserver, config.stored_queries_dbserver_username, config.stored_queries_dbserver_password, config.stored_queries_dbserver_port)
            sqlEng = sqlEngine.SQLEngine(config.stored_queries_dbserver, config.test_dbserver_username, config.test_dbserver_password, config.test_dbserver_port)
            reporting_sqlEng = sqlEngine.SQLEngine(config.reporting_dbserver, config.reporting_dbserver_username, config.reporting_dbserver_password, config.reporting_dbserver_port, "MySQL")
            fEng=None
            
            log_sql_query = config.stored_queries_query
            logging.info("Setting up Firethorn Environment..")
            
            if (self.use_preset_params):
                fEng = pyrothorn.firethornEngine.FirethornEngine(config.jdbcspace, config.adqlspace, config.adqlschema, config.query_schema, config.schema_name, config.schema_alias)
                fEng.printClassVars()
                
            else:
                
                if (self.use_cached_firethorn_env):
                    
                    if (os.path.isfile(config.stored_env_config)):
                        data = []
                        valid_config_found = True
                        with open(config.stored_env_config) as data_file:    
                            data = json.load(data_file)
                        if ('jdbcspace' in data) and ('adqlschema' in data) and ('adqlspace' in data):
                            fEng = pyrothorn.firethornEngine.FirethornEngine(jdbcspace=data['jdbcspace'], adqlspace=data['adqlspace'], query_schema = data['query_schema'] )
                            valid_config_found = False
                    else :
                        valid_config_found = False   
                    
                    if (valid_config_found==False):  
                        fEng = pyrothorn.firethornEngine.FirethornEngine()
                        fEng.setUpFirethornEnvironment( config.resourcename , config.resourceuri, config.catalogname, config.ogsadainame, config.adqlspacename, config.jdbccatalogname, config.jdbcschemaname, config.metadocfile)
                        if (self.include_neighbours):
                            self.import_neighbours(sqlEng, fEng)
                            
                    fEng.printClassVars()
                            
                else:
                    fEng = pyrothorn.firethornEngine.FirethornEngine()
                    fEng.setUpFirethornEnvironment( config.resourcename , config.resourceuri, config.catalogname, config.ogsadainame, config.adqlspacename, config.jdbccatalogname, config.jdbcschemaname, config.metadocfile)
                    fEng.printClassVars()
                    if (self.include_neighbours):
                        self.import_neighbours(sqlEng, fEng)
                    self.store_environment_config(fEng, config.stored_env_config)
          
            logging.info("")
            logged_queries = logged_query_sqlEng.execute_sql_query(log_sql_query, config.stored_queries_database)            
            
                                                       
            for query in logged_queries:
                qEng = queryEngine.QueryEngine()
                query = query[0].strip()
                if (config.limit_query!=None):
                    query = "select top " + str(config.limit_query) + " * from (" + query + ") as q"
                logging.info("Query : " +  query)
                sql_start_time = time.time()
                query_timestamp = datetime.datetime.fromtimestamp(sql_start_time).strftime('%Y-%m-%d %H:%M:%S')
                logging.info("Starting sql query :::" +  strftime("%Y-%m-%d %H:%M:%S", gmtime()))
                sql_row_length = sqlEng.execute_sql_query_get_rows(query, config.test_database)
                logging.info("Completed sql query :::" +  strftime("%Y-%m-%d %H:%M:%S", gmtime()))
                logging.info("SQL Query: " + str(sql_row_length) + " row(s) returned. ")
                sql_duration = int(time.time() - sql_start_time)
                
                logging.info("")
                
                start_time = time.time()
                logging.info("Started Firethorn job :::" +  strftime("%Y-%m-%d %H:%M:%S", gmtime()))
                firethorn_row_length = qEng.run_query(query, "", fEng.query_schema)
                logging.info("Finished Firethorn job :::" +  strftime("%Y-%m-%d %H:%M:%S", gmtime()))
                logging.info("Firethorn Query: " + str(firethorn_row_length) + " row(s) returned. ")
                firethorn_duration = int(time.time() - start_time)
            

                logging.info("--- End Query Test ---")
                logging.info("")
                
                test_passed = (sql_row_length == firethorn_row_length)
                
                if (not test_passed):
                    self.total_failed = self.total_failed + 1
                
                error_message = ""
                params = (query.encode('utf-8'), queryrunID, query_timestamp, sql_row_length, firethorn_row_length, firethorn_duration, sql_duration, test_passed, self.firethorn_version, error_message )
                report_query = "INSERT INTO queries (query, queryrunID, query_timestamp, direct_sql_rows, firethorn_sql_rows, firethorn_duration, sql_duration, test_passed, firethorn_version, error_message) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" 
                reporting_sqlEng.execute_insert(report_query, config.reporting_database, params=params)
                
        except Exception as e:
            logging.exception(e)    
        
        # Test if total queries failed > 0            
        self.assertEqual(self.total_failed , 0, "Total queries failed (" + str(self.total_failed) + ") > 0 ")
   
                
    def setUpLogging(self):
        root = logging.getLogger()
        root.setLevel(logging.INFO)
        ch = logging.StreamHandler(sys.stdout)
        ch.setLevel(logging.DEBUG)
        formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
        ch.setFormatter(formatter)
        root.addHandler(ch)
    
            
                    
    def tearDown(self):
        self.assertEqual([], self.verificationErrors)
        
    
    def import_neighbours(self, sqlEng, fEng):
        neighbour_tables = sqlEng.execute_sql_query(config.neighbours_query, config.test_database)
        for i in neighbour_tables:
            logging.info("Importing " + i[0])
            fEng.import_jdbc_metadoc(fEng.adqlspace, fEng.jdbcspace, i[0], config.jdbcschemaname, config.metadocdirectory + "/" + i[0].upper() + "_TablesSchema.xml" )
      
    
    def store_environment_config(self, fEng, stored_config_file):
        stored_config = collections.OrderedDict()
        stored_config['jdbcspace'] = fEng.jdbcspace
        stored_config['adqlspace'] = fEng.adqlspace
        stored_config['query_schema'] = fEng.query_schema
        
        j = json.dumps(stored_config)
        f = open(stored_config_file,'w')
        print >> f, j
        f.close()       

if __name__ == "__main__":
    unittest.main()
