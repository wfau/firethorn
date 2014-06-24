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
import sys
import config

class test_firethorn(unittest.TestCase):
            
               
    def setUp(self):
        self.use_preset_params = False
        self.verificationErrors = []
        self.sample_query=config.sample_query
        self.sample_query_expected_rows=config.sample_query_expected_rows
        self.setUpLogging()
    

    def test_sample_sql_query(self):
        sqlEng = sqlEngine.SQLEngine(config.test_dbserver, config.test_dbserver_username, config.test_dbserver_password, config.test_dbserver_port)
        row_length = sqlEng.execute_sql_query( self.query, config.test_database)
        self.assertEqual(row_length, self.expected_rows)


    def test_sample_firethorn_query(self):
        if (self.use_preset_params):
            fEng = pyrothorn.firethornEngine.FirethornEngine(config.jdbcspace, config.adqlspace, config.adqlschema, config.starting_catalogue_id, config.schema_name, config.schema_alias)
            fEng.printClassVars()
        else:
            fEng = pyrothorn.firethornEngine.FirethornEngine()
            fEng.setUpFirethornEnvironment( config.resourcename , config.resourceuri, config.catalogname, config.ogsadainame, config.adqlspacename, config.jdbccatalogname, config.jdbcschemaname, config.metadocfile)
            fEng.printClassVars()
        qEng = queryEngine.QueryEngine()
        row_length = qEng.run_query(self.query, "", fEng.starting_catalogue_id)
        self.assertEqual(row_length, self.expected_rows)


    def test_logged_queries(self):
        sqlEng = sqlEngine.SQLEngine(config.test_dbserver, config.test_dbserver_username, config.test_dbserver_password, config.test_dbserver_port)
        #row_length = sqlEng.execute_sql_query( self.query, config.test_database)
        row_length  = 10
        logging.info("Setting up Firethorn Environment..")

        fEng = pyrothorn.firethornEngine.FirethornEngine(config.jdbcspace, config.adqlspace, config.adqlschema, config.starting_catalogue_id, config.schema_name, config.schema_alias)
        fEng.printClassVars()
        
        logging.info("")
        
        with open("query_logs/atlas-logged-queries-short.txt") as f:
            
            for line in f:
                qEng = queryEngine.QueryEngine()
                logging.info("--- Start Query Test ---")
                query = line.strip()
                if (config.limit_query!=None):
                    query = "select top " + str(config.limit_query) + " * from (" + query + ") as q"
                    print query
                logging.info("Query : " +  query)
                sql_row_length = sqlEng.execute_sql_query(query, config.test_database)
                logging.info("SQL Query: " + str(sql_row_length) + " row(s) returned. ")
                logging.info("")
                firethorn_row_length = qEng.run_query(query, "", fEng.starting_catalogue_id)
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
