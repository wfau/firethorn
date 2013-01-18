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
from helper_functions import session_helpers
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
    
    def __add(self, data):
        
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
            if type_helpers.isTable(db_type):
                new_object = {'ident': id_url, 'type': db_type, 'name': name, 'modified': time, 'created': time}
            else :
                new_object = {'ident': id_url, 'type': db_type, 'name': name, 'modified': time, 'created': time, 'children' : []}
                
            getattr(session, workspace).append(new_object)

            ### temp
            ident = id_url + config.resource_uris[db_type]
            request = urllib2.Request(ident, headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
            json_data = json.loads(f.read())
            
            for i in getattr(session, workspace):
                if i["ident"] == id_url:
                    i["children"] = json_data
            
            ###
            
            return ok  
        except Exception:
            traceback.print_exc()
            return error
     
        return ok  
     
     
    def __move(self, data):
        
        drag_path =   string_functions.decode(data.drag_path)
        drag_name = string_functions.decode(data.drag_name)
        parent_folder =  string_functions.decode(data.parent_folder)
        workspace =  string_functions.decode(data.workspace)
        drop_path =  string_functions.decode(data.drop_path)
        workspace_data = session_helpers(session).get_session_value(workspace)
      
        error = json.dumps({
            'Code' : -1,
            'Content' : 'Unable to move selected object'
            })
        
        ok = json.dumps({
            'Code' : 1,
            'Content' : "OK"
            })
        temp_item_holder = ""
        temp_container_holder = []
        
        try:
           
            if workspace_data!="":
                
                if parent_folder=="":
                    counter = 0
                    
                    for i in workspace_data:
                        if i["ident"] == drag_path and i["name"] ==drag_name:
                            temp_item_holder = i
                            index_to_delete = counter
                        if i["ident"] == drop_path:
                            temp_container_holder = i
                        counter += 1
                    if temp_item_holder!="" and temp_container_holder!=[]:
                        temp_container_holder["children"].append(temp_item_holder)   
                        del workspace_data[index_to_delete]
                        return ok  
                    else:
                        workspace_data.append(temp_item_holder)
                        return error
              
                else:
                    for i in workspace_data:
                        if i["ident"] == parent_folder:
                            children_data= i["children"]
                            second_counter =0
                            for x in  children_data:
                                if x["ident"]== drag_path and x["name"] ==drag_name:
                                    temp_item_holder = x
                                    index_to_delete = second_counter
                                second_counter += 1
                                
                    if temp_item_holder!="":
                        workspace_data.append(temp_item_holder)   
                        del children_data[index_to_delete]
                        return ok
                    else:
                        workspace_data.children_data(temp_item_holder)
                        return error
              
                
                return ok
                    
                """
                else:
                    workspace_data = self.get_session_value(workspace)
                    for i in workspace_data:
                        if i["ident"] == parent_folder:
                            children_nodes = i["children"]
                            for x in children_nodes:
                                if x["ident"] == ident and  x["name"] == old:
                                    x["name"] = new
                    self.session[workspace] = workspace_data
                    return ok
                """
            else:
                return error
        except Exception:
            traceback.print_exc()
            return error
  
        return ok
        
    def __workspace_actions_handler(self, data, request_type):
        """
        Handle requests for a workspace action
        """
        
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
        
        data = web.input(workspace="", id_url = "", db_type = "", name="", action="") 
        web.header('Content-Type', 'application/json')        
        return  self.__workspace_actions_handler(data, 'POST')