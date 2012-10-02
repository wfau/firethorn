'''
Created on Sep 18, 2012

@author: stelios
'''
from app import render, session
from helper_functions import login_helpers
import config
import json
import traceback
import urllib
import urllib2
import web
from datetime import datetime

class create_view:
    """
    Create new object class, handles requests for creating new objects.
    
    Accepts GET and POST requests. 
  
    """
    
    
    def POST(self):
        """ 
        POST function 
        
        """
        
        data = web.input()
        sample_data = [{
            "id":1,
            "text":"Sample Service [58] ADQL Metadata",
            "children":[{
                "id":2,
                "text":"catalog 1",
                "state":"closed",
                "children":[{
                    "text":"schema",
                    "checked":'false',
                    "children":[{
                            "id":2,
                            "text":"table: source",
                            "state":"closed",
                            "children":[{
                                "text":"column: ra",
                                "checked":'false'
                            },{
                                "text":"column: dec",
                                "checked":'false'
                            }]
                    }]
                }]
              
            },{
                "id":2,
                "text":"catalog 2",
                "state":"closed",
                "children":[{
                    "text":"schema",
                    "checked":'false',
                    "children":[{
                            "id":2,
                            "text":"table: detection",
                            "state":"closed",
                            "children":[{
                                "text":"column: multiframeID",
                                "checked":'false'
                            },{
                                "text":"column: filterID",
                                "checked":'false'
                            }]
                    }]
                }]
              
            }]
        }]
        
        return json.dumps(sample_data)