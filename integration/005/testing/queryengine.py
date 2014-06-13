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
import atpy
import pyodbc
try:
    import simplejson as json
except ImportError:
    import json
import numpy
import html_functions
import re
from helper_functions.string_functions import string_functions
string_functions = string_functions()
from config import survey_prefix, SURVEY_DB,base_location, debug_mode, MAX_DELAY, MAX_ELAPSED_TIME, MIN_ELAPSED_TIME_BEFORE_REDUCE, INITIAL_DELAY
import datetime
from firethorn_config import  *
from time import gmtime,  strftime
from helper_functions import sql_functions
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
        
        try :
            from datetime import datetime
            t = datetime.now()
            if query_name=="":
                query_name = 'query-' + t.strftime("%y%m%d_%H%M%S")
         
            #data = urllib.urlencode({ query_name_param : query_name,  query_param : query,'adql.query.update.delay.every' : '10000', 'adql.query.update.delay.first':'10000', 'adql.query.update.delay.last':'10000'})
            
            urlenc = { query_name_param : query_name,  query_param : query}
            data = urllib.urlencode(urlenc)
            request = urllib2.Request(query_space + query_create_uri, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : test_email, "firethorn.auth.community" : "public (unknown)"})
    
            f = urllib2.urlopen(request)
            query_create_result = json.loads(f.read())
            query_identity = query_create_result["ident"]
            query_loop_results = self.start_query_loop(query_identity)
            results_adql_url = query_create_result["results"]["adql"]
    
            
            if query_loop_results.get("Code", "") !="":
                if query_loop_results.get("Code", "") ==-1:
                    result = query_loop_results.get("Content", "Error")
                    
                    return (result,result_adql_table,query_identity)
            
            if results_adql_url!=None:
                   
                try :
                    req_rez_table = urllib2.Request( results_adql_url, headers={"Accept" : "application/json", "firethorn.auth.identity" : test_email, "firethorn.auth.community" : "public (unknown)"})
                    response_rez_table = urllib2.urlopen(req_rez_table) 
                    response_rez_table_json = response_rez_table.read()
                    result_adql_table = json.loads(response_rez_table_json)["fullname"]
                    response_rez_table.close()
                except Exception as e:
                    logging.exception(e)
            
             
            if query_loop_results.get("Code", "") !="":
                if query_loop_results.get("Code", "") ==-1:
                    result = query_loop_results.get("Content", "Error")
                elif query_loop_results.get("Code", "") ==1:
                    req = urllib2.Request(query_loop_results.get("Content", ""), headers={"firethorn.auth.identity" : test_email, "firethorn.auth.community" : "public (unknown)"})
                    f = urllib2.urlopen(req)
                    datatable = f.read(MAX_FILE_SIZE) 
                 
                    if len(f.read())>0:
                        max_size_exceeded = True
                    if not max_size_exceeded:
                        result = datatable
                    else:
                        result = ("MAX ERROR",result_adql_table,query_identity)
    
            else :
          
                return ("ERROR",result_adql_table,query_identity)
                
        except Exception as e:
            logging.exception('Exception caught in run query:')
            logging.exception(e) 
            return ("ERROR",result_adql_table,query_identity)
        
        if f!='':
            f.close()
        return (result,result_adql_table,query_identity)  
    
    
    
    def start_query_loop(self, url):
        """
        
        @param url: A URL string to be used
        @return: Results of query
        """
        
        def get_status(url):
            request2 = urllib2.Request(url, headers={"Accept" : "application/json", "firethorn.auth.identity" : test_email, "firethorn.auth.community" : "public (unknown)"})
            f_read = urllib2.urlopen(request2)
            query_json = f_read.read()
            return query_json
        
        max_size_exceeded = False 
       
        f_read = ""
        return_vot = ''
        delay = INITIAL_DELAY
        start_time = time.time()
        elapsed_time = 0
        query_json = {'syntax' : {'friendly' : 'A problem occurred while running your query', 'status' : 'Error' }}
        
        try:
            logging.info("Started Firethorn job :::" +  strftime("%Y-%m-%d %H:%M:%S", gmtime()))
    
            data = urllib.urlencode({ query_status_update : "RUNNING"})
            
            #data = urllib.urlencode({ query_status_update : "RUNNING", 'adql.query.update.delay.every' : '10000', 'adql.query.update.delay.first':'10000', 'adql.query.update.delay.last':'10000'})
            request = urllib2.Request(url, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : test_email, "firethorn.auth.community" : "public (unknown)"})
            f_update = urllib2.urlopen(request)
            query_json =  json.loads(f_update.read())
            query_status = query_json["status"]
            
            logging.info("Query started on Firethorn:")
            logging.info( url)
       
            while query_status=="PENDING" or query_status=="RUNNING" and elapsed_time<MAX_ELAPSED_TIME:
                query_json = json.loads(get_status(url))
                query_status= query_json["status"] 
                time.sleep(delay)
                if elapsed_time>MIN_ELAPSED_TIME_BEFORE_REDUCE and delay<MAX_DELAY:
                    delay = delay + delay
                elapsed_time = int(time.time() - start_time)
           
          
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
            return []
    
        if f_read != "":
            f_read.close() 
        logging.exception("return_vot in start_query_loop: " + return_vot)
        return return_vot    
    
          
            