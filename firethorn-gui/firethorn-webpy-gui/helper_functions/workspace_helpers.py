'''
Created on Sep 24, 2012

@author: stelios
'''

from config import *
import urllib2
import urllib
from helper_functions.string_functions import string_functions
from helper_functions import type_helpers
string_functions = string_functions()
import web
import json
import traceback

class workspace_helpers:
    """
    Function to provide assistance in handling login and workspace variables
    """
    
    def __init__(self, session=""):
        self.session = session if session != "" else web.ctx.session       
    
    
    def get_menu_items_by_permissions(self):
        
        html_content = ""
        role = self.session.get('role')
        
        for i in create_menu_items[role]:
            html_content+='<li><a href="create_new?obj_type=' +  string_functions.encode(i) + '" id="' + i + '"><span>' + i +'</span></a></li>'
        
        return html_content
    
    
    def get_log_notification(self):
        
        log_notification_true =""
        log_notification_false=""
        
        try:
            log_info = self.session.get('logged_in')
            username = self.session.get('user')
            log_notification_true = '<div id="log_notification">Logged in as:' + username +'</div>'
            log_notification_false = '<div id="log_notification">Not currently logged in</div>'
        except Exception:
            log_info = 'False'
            
        if log_info == 'True':
            return log_notification_true
        else :
            return log_notification_false
    
    
    def get_workspaces(self):

        workspaces =[]
        try:
            ### temp
            request = urllib2.Request(web_services_url + get_adql_resources_url, headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
            workspaces = json.loads(f.read())            
           
        except Exception:
            workspaces = None
                    
        return workspaces
    
    
    def get_workspace_value(self, param):
        
        value=""
        try:
            value = self.session.get(param)
        except Exception:
            value = ""
                    
        return value
    
    
    def rename_workspace_value(self, ident, new, old, workspace, parent_folder,_type):
        error = -1
        ok = 1
        f=""
        try:
            data = urllib.urlencode({type_update_params[_type] : new })
            request = urllib2.Request(ident, data, headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
            f.read()           
           
        except Exception:
            return error
        if f!="":
            f.close()  
        return 1                

    
    def generate_workspace_selection(self, workspace):
        
        html_data = ""
        workspace_data = self.get_workspaces()
        if len(workspace_data)>0:
            html_data += '<select id="workspace_selection">'
            for i in workspace_data:
                if workspace==i["ident"]:
                    html_data += '<option value="'+ i["ident"] + '" selected="selected">' + i["name"] + '</option>'
                else :
                    html_data += '<option value="'+ i["ident"] + '">' + i["name"] + '</option>'
                    
            html_data += '</select>'
        return html_data


    def new_workspace(self, workspace_name , workspace):
        f = ""
   
        try:
       
           
            ### temp
            data = urllib.urlencode({resource_create_name_params[types["workspace"]] : workspace["name"] })
            
            request = urllib2.Request(create_urls[types["workspace"]], data, headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
            
            f.read()           
           
        except Exception:
            traceback.print_exc()
        if f!="":
            f.close()                
        
        
    
    def add_new_to_path(self, workspace, name, _type, id_url=""):
     
        f = ""
       
        try:
            
            if _type == types["adql_table"] or  _type == types["jdbc_table"] :
                if id_url=="":
                    data = urllib.urlencode({resource_create_name_params[types["adql_table"]] : name })
                    request = urllib2.Request(workspace + table_create_uri, data, headers={"Accept" : "application/json"})
                    f = urllib2.urlopen(request)
                    f.read()  
                
                else:
                    data = urllib.urlencode({schema_import_schema_name : name ,schema_import_schema_base : id_url})
                    request = urllib2.Request(workspace + schema_import_uri, data, headers={"Accept" : "application/json"})
                    f = urllib2.urlopen(request)
                    f.read()            
                    
            elif _type == types["adql_schema"] or  _type == types["jdbc_schema"] :
                if id_url=="":
                    data = urllib.urlencode({resource_create_name_params[types["adql_schema"]] : name })
                    request = urllib2.Request(workspace + schema_create_uri, data, headers={"Accept" : "application/json"})
                    f = urllib2.urlopen(request)
                    f.read()  
                
                else:
                    data = urllib.urlencode({workspace_import_schema_name : name ,workspace_import_schema_base : id_url})
                    request = urllib2.Request(workspace + workspace_import_uri, data, headers={"Accept" : "application/json"})
                    f = urllib2.urlopen(request)
                    f.read()   
        except Exception:
            traceback.print_exc()
        if f!="":
            f.close()   
    
    
    def add_new_to_schema(self, schema, name, id_url):
     
        f = ""
        try:
            data = urllib.urlencode({schema_import_schema_name : name ,schema_import_schema_base : id_url})
            request = urllib2.Request(schema +schema_import_uri, data, headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
            f.read()            
          
        except Exception:
            traceback.print_exc()
        if f!="":
            f.close()         
            
            
    def get_object_name(self, _id):
        """
        Get the name of an object given its ID
        """
        f = ""
        result = ""
        
        try:
            
            if _id!="":
                request = urllib2.Request(_id, headers={"Accept" : "application/json"})
                f = urllib2.urlopen(request)
                result = json.loads(f.read())["name"]            
              
        except Exception:
            traceback.print_exc()
            result = ""

        if f!="":
            f.close()     
        return result