'''
Created on Nov 2, 2012

@author: stelios
'''
from cgi import parse_qs
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


class resource_actions:
    """
    Resource_actions class, handles requests for resources.
    
    Accepts GET and POST requests. 
  
    """
 
            
    def __get_children(self,  ident, _type, workspace):
     
        code = 1
        return_val = ''
        f= ""
        available_action = ""
                                
        error = json.dumps({
            'Code' : -1,
            'Content' : 'Unable to get selected object'
            })
        
        ok = json.dumps({
            'Code' : 1,
            'Content' : "OK"
            })
        
        try:
            ident =  parse_qs(urlparse(string_functions.decode(ident)).query)['id'][0]
            _type = string_functions.decode(_type)
       
            
            if ident!="" and _type!="" and (not type_helpers.isColumn(_type)):
               
                ident = ident + config.resource_uris[_type]
                
                request = urllib2.Request(ident, headers={"Accept" : "application/json"})
                f = urllib2.urlopen(request)
                json_data = json.loads(f.read())
                    
                if json_data == [] or json_data== None:
                    return_val = "No data found"
                    code = -1
                else:
                    for entry in json_data:
                        converted_dict = dict([(str(k), v) for k, v in entry.items()])
                        if type_helpers.isSchema(converted_dict["type"]) or type_helpers.isTable(converted_dict["type"]):
                            available_action = "Add to:" + workspace_helpers(session).generate_workspace_selection(workspace) + config.available_object_actions["add"] 
                        elif type_helpers.isRootType(converted_dict["type"]):
                            available_action = "<div id='open_new'> <a target='_blank' href='"  + config.local_hostname[converted_dict["type"]] + '?' + config.get_param + '='  + string_functions.encode(converted_dict["ident"]) +  "'>Open in new window</a></div>" 
                        else:
                            available_action = config.available_object_actions["none"]

                        sub_item = render.select_service_response('<a id="id_url" href=' + config.local_hostname[converted_dict["type"]] + '?' + config.get_param + '=' + string_functions.encode(converted_dict["ident"]) + '>' + converted_dict["name"] + '</a>', datetime.strptime(converted_dict["created"], "%Y-%m-%dT%H:%M:%S.%f").strftime("%d %B %Y at %H:%M:%S"), datetime.strptime(converted_dict["modified"], "%Y-%m-%dT%H:%M:%S.%f").strftime("%d %B %Y at %H:%M:%S"), converted_dict["type"], available_action, type_helpers.get_img_from_type(converted_dict["type"]))
                        return_val += str(sub_item)
                    code = 1

            else:
                return error
        except Exception:
            traceback.print_exc()
            if f!="":
                f.close()    
            return error
        
        finally:
            if f!="":
                f.close()  
       
        return json.dumps({
                'Code' : code,
                'Content' : return_val
                }) 
        
    def __resource_actions_handler(self, data):
        """
        Handle requests for a resource_actions requests
        """
        
        return_value = json.dumps({
            'Code' : 1,
            'Content' : 'OK'
            })
        
        f =''

        try:
            action = data.action
            if action=='expand':
                return_value = self.__get_children(data.ident,data._type,data.workspace)
                
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
    

    def POST(self):
        """ 
        POST function 
        
        """
        
        data = web.input(ident = "", _type = "", action="", workspace="")
        web.header('Content-Type', 'application/json')        
        return  self.__resource_actions_handler(data)
    
    