"""
misc module

Miscellaneous functions used by for freeform SQL query processing for use with TAP services and XML VOTable procesing

Created on Nov 30, 2011
@author: stelios
"""

import os
import urllib2
import urllib
import StringIO
import time
import xml.dom.minidom
from atpy import atpy
import pyodbc
try:
    import simplejson as json
except ImportError:
    import json
import numpy
import re
import logging
import datetime
from time import gmtime,  strftime




def get_async_results(url, URI):
    """
    Open the given url and URI and read/return the result 

    @param url: A URL string to open
    @param URI: A URI string to attach to the URL request
    @return: The result of the HTTP request sent to the URI of the URL
    """
    
    res = ''
    f = ''
    try:
        req = urllib2.Request(url+URI)
        f = urllib2.urlopen(req)
        res =  f.read() 
        f.close()
    except Exception as e:
        if f!='':
            f.close()
        logging.exception('Exception caught:')
              
    return res




def start_async_loop(url):
    """
    Takes a TAP url and starts a loop that checks the phase URI and returns the results when completed. The loop is repeated every [delay=3] seconds

    @param url: A URL string to be used
    @return: A Votable with the results of a TAP job, or '' if error
    """
    
    return_vot = []
    try:
        while True:
            res = get_async_results(url,'/phase')
            if res=='COMPLETED':      
                time.sleep(5) 
                return_vot = atpy.Table(url + '/results/result', type='vo')      
                break
            elif res=='ERROR' or res== '':
                return -1
            time.sleep(1)
    except Exception as e:
        logging.exception('Exception caught:')
        return -1

    return return_vot    


def get_votable_rowcount(votable):
    """
    Get table rowcount
    """
    if votable==-1:
        return -1
    return len(votable)

def execute_async_query(url, q, mode_local="async", request="doQuery", lang="ADQL", voformat="votable"):
    """
    Execute an ADQL query (q) against a TAP service (url + mode:sync|async)       
    Starts by submitting a request for an async query, then uses the received job URL to call start_async_loop, to receive the final query results 

    @param url: A string containing the TAP URL
    @param mode: sync or async to determine TAP mode of execution
    @param q: The ADQL Query to execute as string
    
    @return: Return a votable with the results, the TAP job ID and a temporary file path with the results stored on the server
    """    

    params = urllib.urlencode({'REQUEST': request, 'LANG': lang, 'FORMAT': voformat, 'QUERY' : q}) 
    full_url = url+"/"+mode_local

    votable = []
    jobId= 'None'
    file_path=''
    try:
        #Submit job and get job id 
        req = urllib2.Request(full_url, params)
        opener = urllib2.build_opener()
  
        f = opener.open(req)
        jobId = f.url
        logging.info("Jobid:" + jobId)
        #Execute job and start loop requests for results
        req2 = urllib2.Request(jobId+'/phase',urllib.urlencode({'PHASE' : 'RUN'}))
        f2 = opener.open(req2) #@UnusedVariable

        # Return results as a votable object
        votable = start_async_loop(jobId)
       
            
    except Exception as e:
        logging.exception('Exception caught:')
        return -1

    return votable



