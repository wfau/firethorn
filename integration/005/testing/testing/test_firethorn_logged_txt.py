import sys, os
srcdir = '../src/'
configdir = '../'
testdir = os.path.dirname(__file__)
sys.path.insert(0, os.path.dirname(__file__))
sys.path.insert(0, os.path.abspath(os.path.join(testdir, srcdir)))
sys.path.insert(0, os.path.abspath(os.path.join(testdir, configdir)))
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


class test_firethorn(unittest.TestCase):
            
               
    def setUp(self):
        self.use_preset_params = False
        self.verificationErrors = []
        self.sample_query=config.sample_query
        self.sample_query_expected_rows=config.sample_query_expected_rows
        self.setUpLogging()
    

    def test_logged_queries(self):
        sqlEng = sqlEngine.SQLEngine(config.test_dbserver, config.test_dbserver_username, config.test_dbserver_password, config.test_dbserver_port)
      
        logging.info("Setting up Firethorn Environment..")

        fEng = pyrothorn.firethornEngine.FirethornEngine(config.jdbcspace, config.adqlspace, config.adqlschema, config.query_schema, config.schema_name, config.schema_alias)
        fEng.printClassVars()
        
        logging.info("")
        
        with open(config.logged_queries_txt_file ) as f:
            
            for line in f:
                qEng = queryEngine.QueryEngine()
                logging.info("--- Start Query Test ---")
                query = line.strip()
                if (config.limit_query!=None):
                    query = "select top " + str(config.limit_query) + " * from (" + query + ") as q"
                    print query
                logging.info("Query : " +  query)
                sql_row_length, error_message = sqlEng.execute_sql_query_get_rows(query, config.test_database)
                logging.info("SQL Query: " + str(sql_row_length) + " row(s) returned. ")
                logging.info("")
                firethorn_row_length = qEng.run_query(query, "", fEng.query_schema)
                logging.info("Firethorn Query: " + str(firethorn_row_length) + " row(s) returned. ")
                self.assertEqual(sql_row_length, firethorn_row_length)
                logging.info("--- End Query Test ---")
                logging.info("")
                
                
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

if __name__ == "__main__":
    unittest.main()