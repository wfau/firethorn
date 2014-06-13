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
import firethorn_config
import urllib
import firethorn_helpers
from queryengine import QueryEngine

class firethorn_pytest(unittest.TestCase):
    
    def setUp(self):
        self.verificationErrors = []
        self.starting_catalogue_id = ""
        self.query="SELECT TOP 10 * FROM Filter"
        self.expected_rows=10
        
        try:
            fEng = firethorn_helpers.FirethornEngine()
            self.atlasschema = fEng.adqlschema
            self.schema_name = ""
            self.schema_alias = ""
            try :
                req_exc = urllib2.Request( self.atlasschema, headers={"Accept" : "application/json", "firethorn.auth.identity" : firethorn_config.test_email, "firethorn.auth.community" :"public (unknown)"})
                response_exc = urllib2.urlopen(req_exc) 
                response_exc_json = response_exc.read()
                self.schema_name = json.loads(response_exc_json)["fullname"]
                self.schema_alias = json.loads(response_exc_json)["name"]
                response_exc.close()
            except Exception as e:
                logging.exception(e)
            self.starting_catalogue_id = fEng.create_initial_workspace(self.schema_name, self.schema_alias, self.atlasschema)
            logging.info(self.starting_catalogue_id)
        except Exception as e:
       
            logging.exception("Error during firethorn initialization")
        
    def test_logged_queries(self):
        qEng = QueryEngine()
        query_results = json.dumps(qEng.run_query(self.query, "", self.starting_catalogue_id))
        json_data = json.loads(query_results)
        row_length = None
        if len(json_data)<1:
            row_length = None
        else :
            json_data = json.loads(json_data[0])
            if len(json_data)<2:
                row_length = None
            else :
                row_length = len(json_data[1])
      
        self.assertEqual(row_length, self.expected_rows)

    
        
    def tearDown(self):
        self.assertEqual([], self.verificationErrors)

if __name__ == "__main__":
    unittest.main()
