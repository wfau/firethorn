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

class create_new:
    """
    Create new object class, handles requests for creating new objects.
    
    Accepts GET and POST requests. 
  
    """
    

    def GET(self):
        """ 
        GET function 
        
        """
        
        data = web.input(obj_type='')
        return render.create_new( str(render.header(login_helpers(session).get_log_notification())), str(render.side_menu(login_helpers(session).get_menu_items_by_permissions())), str(render.create_input_area(data.obj_type)), str(render.footer()))

    
    def POST(self):
        """ 
        POST function 
        
        """
        
        data = web.input(obj_type='')
        return render.create_new( str(render.header(login_helpers(session).get_log_notification())), str(render.side_menu(login_helpers(session).get_menu_items_by_permissions())), str(render.create_input_area(data.obj_type)), str(render.footer()))
    
    