'''
Created on Sep 18, 2012

@author: stelios
'''
from app import render, session
from helper_functions import session_helpers
import config
import json
import traceback
import urllib
import urllib2
import web
from datetime import datetime
from helper_functions.string_functions import string_functions
string_functions = string_functions()

"""
Sample Variables

"""

service58 = {
         "id":'service58',
         "text":"Sample Service [58] ADQL Metadata",
         "state":"closed",
         "checked": False
         }


   
class create_view_edit_handler:
    """
    handles requests for modifying metadata objects.
    
    Accepts POST requests. 
  
    """
    
    def __handler(self, _id, text, checked, action):
        """
        Handle edit to metadata of a resource 
        """
        f = ''
        return_val=''
        try :
            if action=='name_edit':
                param_key ='jdbc.resource.update.name'
                param_value = text
            elif action== 'checkbox_edit': 
                param_key = 'jdbc.resource.update.status'
                if checked.lower()=='true':
                    param_value = 'ENABLED'
                elif checked.lower()=='false':
                    param_value = 'DISABLED' 
                else :
                    return_val = json.dumps({
                                    'Code' : -1,
                                    'Content' : config.errors['INVALID_CHECKBOX_VALUE']
                                    })
                   

            else:
                return_val = json.dumps({
                              'Code' : -1,
                              'Content' : config.errors['INVALID_ACTION']
                               })
               
   
            encoded_args = urllib.urlencode({ param_key : param_value })
            request = urllib2.Request(_id, encoded_args, headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
            f.read()
            return_val = json.dumps({
                                'Code' : 1,
                                'Content' : 'OK'
                                 })
        except Exception:
            if f!='':
                f.close()
            return_val =  json.dumps({
                                'Code' : -1,
                                'Content' :  config.errors['UNKNOWN_ERROR']
                                })
            
            
        return return_val
        
    def POST(self):
        """ 
        POST function 
        
        """
        
        data = web.input(id='',text='', checked='', action='')
        
        return_val = ""
        try:
            return_val = self.__handler( data.id,  data.text, data.checked, data.action)
        except Exception:
            return_val =  json.dumps({
                              'Code' : -1,
                              'Content' : "Unknown server error while processing a request"
                              })
            
        return return_val    
        
        
        
class create_view:
    """
    Create new object class, handles requests for creating new objects.
    
    Accepts POST requests. 
  
    """


    def __format_for_jeasyui_tree_view(self,json_data, _type):
        formatted_json = []
        checkbox_transformations = {'ENABLED' : True,'DISABLED' : False, 'CREATED' : False}
        
        if _type=='head':
            for i in json_data:
                formatted_json.append({'text' : i['name'], 'state' : 'closed', 'checked' : checkbox_transformations[i['status']], 'id' : i['ident'],'type' : i['type'] })
      
        elif _type =='http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json':
            for i in json_data:
                formatted_json.append({'text' : i['name'], 'checked' : False, 'id' : i['ident'],'type' : i['type'] })

        else :
            for i in json_data:
                formatted_json.append({'text' : i['name'], 'state' : 'closed', 'checked' : False, 'id' : i['ident'],'type' : i['type'] })
        return formatted_json
    
        
    def __get_workspace_children(self):
        """
        Get all JDBC resources 
        """
        resource_url = config.web_services_url + config.get_jdbc_resources_url
        f = ''
        json_data = []
        try :
            request = urllib2.Request(resource_url, headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
            json_data = json.loads(f.read())
            json_data = self.__format_for_jeasyui_tree_view(json_data,'head')
        except Exception as e:
            if f!='':
                f.close()
            traceback.print_exc()
        return json_data
        
        
    def __get_chilren_from_id(self, ident ,_type):
        """
        Get chilren nodes for an object based on it's id
        """
        return_val= ''
        f= ""
        
        try:
            if ident!="" and _type!="":
                ident = ident + config.resource_uris[_type]
                request = urllib2.Request(ident, headers={"Accept" : "application/json"})
                f = urllib2.urlopen(request)
                json_data = json.loads(f.read())
                json_data = self.__format_for_jeasyui_tree_view(json_data,_type)
                return_val =  json_data
                
        except Exception:
            traceback.print_exc()
                
        finally:
            if f!="":
                f.close()         

        
        return return_val
            
    def POST(self):
        """ 
        POST function 
        
        """
        
        data = web.input(id = '0', type='')
        json_objects = []
        if data.id == '0':
            #json_objects = [service58]
            json_objects = self.__get_workspace_children()
            
        else :
            _id = data.id
            _type = data.type
            json_objects = self.__get_chilren_from_id(_id,_type)
                              
     
        
        return json.dumps(json_objects)