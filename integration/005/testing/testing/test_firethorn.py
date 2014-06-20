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
import pyrethorn
import urllib
from pyrethorn import firethornEngine
from pyrethorn import queryEngine
from mssql import sqlEngine
import sys
import config

class test_firethorn(unittest.TestCase):
            
               
    def setUp(self):
        self.use_preset_params = False
        self.verificationErrors = []
        self.query="SELECT TOP 10 * FROM Filter"
        self.expected_rows=10
        self.setUpLogging()
    

    def test_sql_queries(self):
        sqlEng = sqlEngine.SQLEngine(config.test_dbserver, config.test_dbserver_username, config.test_dbserver_password, config.test_dbserver_port)
        row_length = sqlEng.execute_sql_query( self.query, config.test_database)
        self.assertEqual(row_length, self.expected_rows)

        
    def test_firethorn_queries(self):
        if (self.use_preset_params):
            fEng = pyrethorn.firethornEngine.FirethornEngine(config.jdbcspace, config.adqlspace, config.adqlschema, config.starting_catalogue_id, config.schema_name, config.schema_alias)
            fEng.printClassVars()
        else:
            fEng = pyrethorn.firethornEngine.FirethornEngine()
            fEng.setUpFirethornEnvironment( config.resourcename , config.resourceuri, config.catalogname, config.ogsadainame, config.adqlspacename, config.jdbccatalogname, config.jdbcschemaname, config.metadocfile)
            fEng.printClassVars()
        qEng = queryEngine.QueryEngine()
        row_length = qEng.run_query(self.query, "", fEng.starting_catalogue_id)
        self.assertEqual(row_length, self.expected_rows)


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
