'''
Created on Sep 18, 2012

@author: stelios
'''
import web
import json
from app import render
import config
import urllib2
import urllib
import traceback
from app import session
from helper_functions import session_helpers
from datetime import datetime
from helper_functions import string_functions
from helper_functions import type_helpers
string_functions = string_functions()


class jdbc_resources:
    """
    JDBC resources class, handles requests for specific jdbc resources.
    
    Accepts GET and POST requests. 
  
    """
    

    
    def __validate_type(self, type_param):
        """
        Check if the type parameter is the same as the type of this class
        """
        
        if type_param in config.type_update_params:
            return True
        else:
            return False



    def __generate_html_content(self, data):
        """
        Generate HTML response based on json data
        """
        
        return_html = ''
        available_action = ""       
        if data == [] or data== None:
            return_html = "<div id='sub_item'>There was an error creating your JDBC connection</div>"
        else :
            if type_helpers.isSchema(data["type"]) or type_helpers.isTable(data["type"]):
                available_action = "Add to:" + session_helpers(session).generate_workspace_selection("") + config.available_object_actions["add"] 
            elif type_helpers.isRootType(data["type"]):
                available_action = "<div id='open_new'> <a target='_blank' href='"  + config.local_hostname[data["type"]] + '?' + config.get_param + '='  + string_functions.encode(data["ident"]) +  "'>Open in new window</a></div>" 
            else:
                available_action = config.available_object_actions["none"]

            return_html = str(render.select_service_response('<a id="id_url" href=' + config.local_hostname['jdbc_resources'] + '?'+ config.get_param + '='  +  string_functions.encode(data["ident"])  + '>' + data["name"] + '</a>',datetime.strptime(data["created"], "%Y-%m-%dT%H:%M:%S.%f").strftime("%d %B %Y at %H:%M:%S"), datetime.strptime(data["modified"], "%Y-%m-%dT%H:%M:%S.%f").strftime("%d %B %Y at %H:%M:%S"), config.types["JDBC connection"], available_action,type_helpers.get_img_from_type(data["type"])))
        return return_html
    
    
    def __jdbc_connection_handler(self, data, request_type):
        """
        Handle requests for a service
        """
        return_value = ''
        _id = data.id
        f= ""

        try:
            if _id!="":
            
                request = urllib2.Request(string_functions.decode(_id), headers={"Accept" : "application/json"})
                f = urllib2.urlopen(request)
                json_data = json.loads(f.read())
                json_data = dict([(str(k), v) for k, v in json_data.items()])
                if self.__validate_type(json_data["type"]):
                    if request_type == "GET":
                        return_value = render.jdbc_connections( str(render.header(session_helpers(session).get_log_notification())), str(render.side_menu(session_helpers(session).get_menu_items_by_permissions())), str(render.footer()), str(self.__generate_html_content(json_data)))
                    else :
                        return_value = self.__generate_html_content(json_data) 
                        
                else: 
                    return_value = config.errors['INVALID_TYPE'] + ': ' + json_data["type"]
                                

            else :
                return_value = config.errors['INVALID_PARAM']
                              
                
        except Exception as e:
            traceback.print_exc()
            return_value = config.errors['INVALID_REQUEST']
                            
        finally:
            if f!="":
                f.close()
        
        return return_value
    

    def GET(self):
        """ 
        GET function 
        
        """
        
        data = web.input(id='')
        return  self.__jdbc_connection_handler(data, 'GET')
    
    
    def POST(self):
        """ 
        POST function 
        
        """
        
        data = web.input(id='')    
        return  self.__jdbc_connection_handler(data, 'POST')