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

class create_new:
    """
    Create new object class, handles requests for creating new objects.
    
    Accepts GET and POST requests. 
  
    """
    
    def __generate_html_content(self, json_data, obj_name, obj_type):
       
        return_html = ""
        
        if obj_type == 'Service':
            data = json.loads(json_data)
            if data == [] or data== None:
                return_html = "<div id='sub_item'>There was an error creating your service</div>"
            else :
                converted_dict = dict([(str(k), v) for k, v in data.items()])
                return_html += "<div style='text-align:left;font-style:italic'>The following Service has been created: </div><br/>"
                return_html += str(render.select_service_response('<a href=' + config.local_hostname['services'] + '?'+ config.service_get_param + '='  + converted_dict["ident"] + '>' + converted_dict["name"] + '</a>',datetime.strptime(converted_dict["created"], "%Y-%m-%dT%H:%M:%S.%f").strftime("%d %B %Y at %H:%M:%S"), datetime.strptime(converted_dict["modified"], "%Y-%m-%dT%H:%M:%S.%f").strftime("%d %B %Y at %H:%M:%S")))
                return_html += "<a class='button' style='float:right' id='add_adql_view'>Add ADQL View</a>"
                
        elif obj_type == 'JDBC connection':
            data = json.loads(json_data)
            if data == [] or data== None:
                return_html = "<div id='sub_item'>There was an error creating your jdbc connection</div>"
            else :
                converted_dict = dict([(str(k), v) for k, v in data.items()])
                return_html += "<div style='text-align:left;font-style:italic'>The following JDBC Connection has been created: </div><br/>"
                return_html += str(render.select_service_response('<a href=' + config.local_hostname['services'] + '?'+ config.service_get_param + '='  + converted_dict["ident"] + '>' + converted_dict["name"] + '</a>',datetime.strptime(converted_dict["created"], "%Y-%m-%dT%H:%M:%S.%f").strftime("%d %B %Y at %H:%M:%S"), datetime.strptime(converted_dict["modified"], "%Y-%m-%dT%H:%M:%S.%f").strftime("%d %B %Y at %H:%M:%S")))
     
        else:
            return_html = config.errors['INVALID_PARAM']
       
        return return_html
    
    def __is_length_ok(self, strng):
        """
        Check if length of string is less than 1
        """
        if len(strng)<1:
            return False
        else :
            return True
    
    
    def __input_validator(self, obj_name, obj_type):
        """
        Validate input for create page parameters
        """
        if obj_name!='' and obj_type!='':
            return self.__is_length_ok(obj_name)
        else :
            return False   
        
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
        
        data = web.input(obj_type='', obj_name='')
        return_string = ''
        f=''

        try:
            if  self.__input_validator(data.obj_name, data.obj_type):
                encoded_args = urllib.urlencode({config.service_create_param : data.obj_name})
                request = urllib2.Request(config.service_create_url, encoded_args, headers={"Accept" : "application/json"})
                f = urllib2.urlopen(request)
                return_string = self.__generate_html_content(f.read(), data.obj_name, data.obj_type)
            else:
                return_string = config.errors['INVALID_PARAM']
                
        except Exception as e:
            print e
            print traceback.print_exc()

            if f!="":
                f.close()
            return_string = config.errors['INVALID_NETWORK_REQUEST']
     
        return return_string
