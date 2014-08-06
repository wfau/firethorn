'''
Created on Jun 4, 2014

@author: stelios
'''

import os
import sys, os
srcdir = '../../src/'
configdir = '../../'
testdir = os.path.dirname(__file__)
sys.path.insert(0, os.path.dirname(__file__))
sys.path.insert(0, os.path.abspath(os.path.join(testdir, srcdir)))
sys.path.insert(0, os.path.abspath(os.path.join(testdir, configdir)))
import os.path
import urllib2
import urllib
import StringIO
import pyodbc
try:
    import simplejson as json
except ImportError:
    import json
import numpy
import re
import logging
import json
import collections
import config
    
            
            
class Sql2Json(object):
    '''
    SQLEngine
    Class that handles database interactions
    '''


    def __init__(self, dbserver="", dbuser="", dbpasswd="", dbport='1433', driver='MySQL'):
        '''
        Constructor
        '''
        self.dbserver = dbserver
        self.dbuser = dbuser
        self.dbpasswd = dbpasswd
        self.dbport = dbport
        self.driver = driver
 
   
    def generateJson(self, database, table):
        '''
        Execute an SQL query
        @param query: The SQL Query
        @param database: The Database
        '''

         
        connstr = 'DRIVER={' +  self.driver + '};SERVER=' +  self.dbserver + ';UID=' + self.dbuser + ';PWD=' + self.dbpasswd +';DATABASE=' + database +' ;'
        conn = pyodbc.connect(connstr)
        cursor = conn.cursor()
         
        cursor.execute("""
                    SELECT *
                    FROM """ + table )
         
        rows = cursor.fetchall()
         
    
        # Convert query to objects of key-value pairs
        failed_list = []
        summary = [] 
        objects_list = []
        sum = collections.OrderedDict()
        total_failures = 0
        total_firethorn_duration = 0
        total_sql_duration = 0
        
        for row in rows:
            d = collections.OrderedDict()
            d['queryid'] = row.queryid
            d['queryrunID'] = row.queryrunID
            d['query_timestamp'] = row.query_timestamp
            d['query'] = row.query
            d['direct_sql_rows'] = row.direct_sql_rows
            d['firethorn_sql_rows'] = row.firethorn_sql_rows
            d['firethorn_duration'] = row.firethorn_duration
            d['sql_duration'] = row.sql_duration
            d['test_passed'] = row.test_passed
            d['firethorn_version'] = row.firethorn_version
            d['firethorn_error_message'] = row.firethorn_error_message
            d['sql_error_message'] = row.sql_error_message
            objects_list.append(d)
            queryrun = row.queryrunID
            

            if (row.queryrunID=="" or row.queryrunID==None):
                queryrun  = "No ID"
                
            if sum.get(queryrun,None)==None:
                sum[queryrun] = {}
                sum[queryrun]['total_queries_run'] = 0
                sum[queryrun]['total_failed'] = 0
                sum[queryrun]['firethorn_version'] = row.firethorn_version
                sum[queryrun]['average_sql_duration'] =  row.sql_duration
                sum[queryrun]['average_firethorn_duration'] = row.firethorn_duration
                
                
            sum[queryrun]['total_queries_run'] = sum[queryrun]['total_queries_run'] + 1
            sum[queryrun]['average_firethorn_duration'] = (float(sum[queryrun]['average_firethorn_duration']) + float(row.firethorn_duration)) / 2
            sum[queryrun]['average_sql_duration'] =  (float(sum[queryrun]['average_sql_duration']) + float(row.sql_duration)) / 2
            
            if row.test_passed!=1:
                failed_list.append(d)
                sum[queryrun]['total_failed'] = sum[queryrun]['total_failed'] + 1
                
   
           
        j = json.dumps(objects_list)
        objects_file = 'tmp/sql2json-all.js'
        f = open(objects_file,'w')
        print >> f, j
        f.close()
                  
        j2 = json.dumps(failed_list)
        failed_file = 'tmp/sql2json-failed.js'
        f2 = open(failed_file,'w')
        print >> f2, j2
        f2.close()
        
        summary.append(sum) 
        j3 = json.dumps(summary)
        summary_list = 'tmp/sql2json-summary.js'
        f3 = open(summary_list,'w')
        print >> f3, j3
        f3.close()
         
        conn.close()
                  
if __name__ == "__main__":
    sql2Json = Sql2Json(config.reporting_dbserver, config.reporting_dbserver_username, config.reporting_dbserver_password, config.reporting_dbserver_port, "MySQL")              
    sql2Json.generateJson(config.reporting_database, "queries")             
 
