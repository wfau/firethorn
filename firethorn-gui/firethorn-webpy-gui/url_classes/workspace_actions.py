'''
Created on Nov 2, 2012

@author: stelios
'''
from cgi import parse_qs
import StringIO
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
from helper_functions import workspace_helpers
from helper_functions import type_helpers
from datetime import datetime
from urlparse import urlparse
from helper_functions.string_functions import string_functions
string_functions = string_functions()


class workspace_actions:
    """
    Workspace actions class, handles requests for user workspace resources.
    
    Accepts GET and POST requests. 
  
    """
    
    def __custom_error(self, msg):
        return json.dumps({
            'Code' : -1,
            'Content' : msg
            })
    
    
    def __add(self, data):
        """
        Add file to workspace
        """
        
        error = json.dumps({
            'Code' : -1,
            'Content' : 'Unable to add selected object'
            })
        
        ok = json.dumps({
            'Code' : 1,
            'Content' : "OK"
            })
        
        try:
            
            time = str(datetime.today())
            id_url =  parse_qs(urlparse(string_functions.decode(data.id_url)).query)['id'][0]
            db_type = string_functions.decode(data.db_type) 
            name = string_functions.decode(data.name)
            workspace =  string_functions.decode(data.workspace)
            workspace_helpers(session).add_new_to_path( workspace,  name, db_type, id_url)
           
            
            return ok  
        except Exception:
            traceback.print_exc()
            return error
     
        return ok  
     
     
     
    def __uploadfile(self, data):
        """ 
        Upload file to workspace
        """
        upload_name = string_functions.decode(data.upload_name)
        workspace = string_functions.decode(data.workspace)
        container = string_functions.decode(data.container)
        upload_file = string_functions.decode(data.newfile)
        time = str(datetime.today())
        obj= {}
        fileHandle = ''
        
        error = json.dumps({
            'Code' : -1,
            'Content' : 'Unable to upload selected file'
            })
        
        ok = json.dumps({
            'Code' : 1,
            'Content' : "File succesfully uploaded"
            })        
        
        try :
            if data.newfile!="":
                fileHandle=StringIO.StringIO(upload_file)
                obj = {'ident': upload_name, 'type': config.types["jdbc_table"], 'name': upload_name, 'modified': time, 'created': time}
                workspace_helpers(session).add_new_to_path( workspace, upload_name, config.types["adql_table"], upload_name)
            else:
                return self.__custom_error("Error: No file uploaded")
        except Exception as e:
            traceback.print_exc()
            fileHandle.close()
            return error
        
        if fileHandle!='':
            fileHandle.close()
        
        return ok


    
    
    
    def __move(self, data):
        
        drag_path =   string_functions.decode(data.drag_path)
        drag_name = string_functions.decode(data.drag_name)
        parent_folder =  string_functions.decode(data.parent_folder)
        workspace =  string_functions.decode(data.workspace)
        drop_path =  string_functions.decode(data.drop_path)
       
        error = json.dumps({
            'Code' : -1,
            'Content' : 'Unable to move selected object'
            })
        
        ok = json.dumps({
            'Code' : 1,
            'Content' : "OK"
            })
        
  
        
        try:
            # workspace_helpers(session).add_new_to_path( workspace,  drag_name, drag_path)
            workspace_helpers(session).add_new_to_schema( drop_path,  drag_name, drag_path)

        except Exception:
            traceback.print_exc()
            return error
  
        return ok
        
    def __workspace_actions_handler(self, data, request_type):
        """
        Handle requests for a workspace action
        """
        web.header('Content-Type', 'application/json')

        return_value = json.dumps({
            'Code' : 1,
            'Content' : 'OK'
            })
        
        f =''

        try:
            action = data.action
            if action=='add':
                return_value = self.__add(data)
            elif action=='move':
                return_value = self.__move(data)
            elif action=='uploadfile':
                return_value = self.__uploadfile(data)
                
        except Exception as e:
            traceback.print_exc()
            return_value = json.dumps({
                'Code' : 1,
                'Content' : 'OK'
            })
        finally:
            if f!="":
                f.close()
        
        return return_value
    

    def GET(self):
        """ 
        GET function 
        
        """
        
        data = web.input()
        web.header('Content-Type', 'application/json')
        return  self.__workspace_actions_handler(data, 'GET')
    
    
    def POST(self):
        """ 
        POST function 
        
        """
        
        data = web.input(workspace="", id_url = "", db_type = "", name="", action="", container="",upload_name="") 
        web.header('Content-Type', 'application/json')        
        return  self.__workspace_actions_handler(data, 'POST')