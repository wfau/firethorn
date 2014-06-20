'''
Created on Jun 4, 2014

@author: stelios
'''

import os
import urllib2
import urllib
import StringIO
import time
import xml.dom.minidom
from mssql import pyodbc
try:
    import simplejson as json
except ImportError:
    import json
import numpy
from helper_functions import html_functions
import re
from helper_functions.string_functions import string_functions
string_functions = string_functions()
import datetime
from firethorn_config import  *
import firethorn_config as config
from time import gmtime,  strftime
import logging

class QueryEngine(object):
    '''
    classdocs
    '''


    def __init__(self):
        '''
        Constructor
        '''
        self.id = 1
            
            
    def _getRows(self, query_results):
        rows = json.loads(query_results)
        row_length = -1
        if len(rows)<=1:
            return row_length
        else :
            row_length = len(rows[1])
        return row_length
    
                
    def run_query(self, query=None, query_name="", query_space="", **kwargs):
        """
        Run a query on a resource
        """
        
        f=''
        query_space = string_functions.decode(query_space)
        result = ""
        max_size_exceeded = False
        result_adql_table = ""
        query_identity = ""
        datatable = []
        

        try :
            from datetime import datetime
            t = datetime.now()
            if query_name=="":
                query_name = 'query-' + t.strftime("%y%m%d_%H%M%S")
                     
            urlenc = { query_name_param : query_name,  query_param : query}
            data = urllib.urlencode(urlenc)
            request = urllib2.Request(query_space + query_create_uri, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : test_email, "firethorn.auth.community" : "public (unknown)"})
    
            f = urllib2.urlopen(request)
            query_create_result = json.loads(f.read())
            query_identity = query_create_result["ident"]
            
            logging.info("Started Firethorn job :::" +  strftime("%Y-%m-%d %H:%M:%S", gmtime()))

            query_loop_results = self.start_query_loop(query_identity)
            results_adql_url = query_create_result["results"]["adql"]
            
            logging.info("Finished Firethorn job :::" +  strftime("%Y-%m-%d %H:%M:%S", gmtime()))

            if query_loop_results.get("Code", "") !="":
                if query_loop_results.get("Code", "") ==-1:
                    error_message = query_loop_results.get("Content", "Error")
                    logging.exception('Query returned an error:' + error_message)
                    return -1
            
            if results_adql_url!=None:
                req_rez_table = urllib2.Request( results_adql_url, headers={"Accept" : "application/json", "firethorn.auth.identity" : test_email, "firethorn.auth.community" : "public (unknown)"})
                response_rez_table = urllib2.urlopen(req_rez_table) 
                response_rez_table_json = response_rez_table.read()
                result_adql_table = json.loads(response_rez_table_json)["fullname"]
                response_rez_table.close()
             
                if query_loop_results.get("Code", "") == 1:
                    req = urllib2.Request(query_loop_results.get("Content", ""), headers={"firethorn.auth.identity" : test_email, "firethorn.auth.community" : "public (unknown)"})
                    f = urllib2.urlopen(req)
                    datatable = f.read() 
                    f.close()
            else :
                logging.exception('Query returned an error.. ')
                return -1
                
        except Exception as e:
            logging.exception('Exception caught in run query:')
            logging.exception(e) 
            return -1
        
        if f!='':
            f.close()
            
        return self._getRows(datatable)  
    
    
    
    def start_query_loop(self, url):
        """
        
        @param url: A URL string to be used
        @return: Results of query
        """
        
        def get_status(url):
            request2 = urllib2.Request(url, headers={"Accept" : "application/json", "firethorn.auth.identity" : test_email, "firethorn.auth.community" : "public (unknown)"})
            f_read = urllib2.urlopen(request2)
            query_json = f_read.read()
            f_read.close()
            return query_json
        
        max_size_exceeded = False 
        f_read = ""
        return_vot = ''
        delay = INITIAL_DELAY
        start_time = time.time()
        elapsed_time = 0
        query_json = {'syntax' : {'friendly' : 'A problem occurred while running your query', 'status' : 'Error' }}
        
        try:
    
            data = urllib.urlencode({ query_status_update : "RUNNING"})
            
            #data = urllib.urlencode({ query_status_update : "RUNNING", 'adql.query.update.delay.every' : '10000', 'adql.query.update.delay.first':'10000', 'adql.query.update.delay.last':'10000'})
            request = urllib2.Request(url, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : test_email, "firethorn.auth.community" : "public (unknown)"})
            f_update = urllib2.urlopen(request)
            query_json =  json.loads(f_update.read())
            query_status = query_json["status"]
            logging.info("Started query:" + url)
            
       
            while query_status=="PENDING" or query_status=="RUNNING" and elapsed_time<MAX_ELAPSED_TIME:
                query_json = json.loads(get_status(url))
                query_status= query_json["status"] 
                time.sleep(delay)
                if elapsed_time>MIN_ELAPSED_TIME_BEFORE_REDUCE and delay<MAX_DELAY:
                    delay = delay + delay
                elapsed_time = int(time.time() - start_time)
            
            logging.info("Finished query:" + url)
          
            if query_status=="ERROR" or query_status=="FAILED":
                return {'Code' :-1,  'Content' : 'Query error: A problem occurred while running your query' }
            elif query_status=="CANCELLED":
                return {'Code' :1,  'Content' : 'Query error: Query has been canceled' }
            elif query_status=="EDITING":
                logging.exception( "Editing status in start_query_loop:" )
                return {'Code' :-1,  'Content' : 'Query error: ' + query_json["syntax"]["status"] + ' - ' + query_json["syntax"]["friendly"] }
            elif query_status=="COMPLETED":
                return {'Code' :1,  'Content' : query_json["results"]["datatable"] }
            elif elapsed_time>=MAX_ELAPSED_TIME:
                return {'Code' :-1,  'Content' : 'Query error: Max run time (' + str(MAX_ELAPSED_TIME) + ' seconds) exceeded' }
            else:
                return {'Code' :-1,  'Content' : 'Query error: A problem occurred while running your query' }
            
        
        except Exception as e:
            logging.exception('Exception caught in start query loop:')  
            logging.exception(e)     
            return {'Code' :-1,  'Content' : 'Query error: A problem occurred while running your query' }

    
    
          
            