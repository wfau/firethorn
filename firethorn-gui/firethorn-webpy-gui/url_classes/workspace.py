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
from helper_functions import workspace_helpers
from datetime import datetime
from helper_functions.string_functions import string_functions
string_functions = string_functions()


class workspace:
    """
    Workspace class, handles requests for user workspace resources.
    
    Accepts GET and POST requests. 
  
    """
    


    def __generate_html_content(self, data):
        """
        Generate HTML response based on json data
        """
        
        return_html = ''
        workspace_list = ''
                             
        if data== None:
            return_html = "<div id='sub_item'><p style='color:red'>There was an error fetching your workspaces</p></div>"
        elif data == [] :
            return_html = "<div id='sub_item'><p style='color:red'>You do not have any workspaces set up.</p><p style='font-style:italic;font-size:11px'> Use the  menu on the left if you wish to create a new workspace</p></div>"
        else :
            counter = 0
            for i in data:
                if counter>0:
                    workspace_list += "</br> "
                workspace_list += '<a id="workspace" href="workspace?_id=' + string_functions.encode(i["ident"]) + '">' + i["name"] + '</a>' 
                counter += 1
            return_html = "<div id ='sub_item' class='rounded'><h4>Welcome to your workspaces!</h4><br/>Available workspaces: <p style='word-wrap:break-word;'><i>" + workspace_list + "</i></p></div>"
        
        return return_html
    

    
    class ObjectClass( object ):
        """
        Empty Object class
        """
        pass


    def __workspace_handler(self, data, request_type):
        """
        Handle requests for a service
        """
        return_value = ''
        f= ""
        json_data = workspace_helpers().get_workspaces()
       
        try:
         
            if request_type == "GET":
                if getattr(data, '_id',"") == "":
                    return_value = render.workspace( str(render.header(workspace_helpers().get_log_notification())), str(render.side_menu(workspace_helpers().get_menu_items_by_permissions())), str(render.footer()), str(self.__generate_html_content(json_data)), "")
                else:
                    from vospace import vospace
                    vospace_instance = vospace()
                    encoded_args=self.ObjectClass()
                    setattr( encoded_args, 'method', 'GET' )
                    setattr( encoded_args, 'showThumbs', '' )
                    setattr( encoded_args, 'mode', '' )
                    setattr( encoded_args, 'time', '' )
                    setattr( encoded_args, '_id', string_functions.decode(getattr(data, '_id',"")) )
                    setattr( encoded_args, 'parent_folder', string_functions.decode(getattr(data, 'parent_folder',"")) )
                    setattr( encoded_args, 'workspace' , string_functions.decode(getattr(data, 'workspace',"")) )
                    setattr( encoded_args, 'import_input_area' , str(render.index_input_area() ))

                    req_result = vospace_instance.handle_vospace_template_request(encoded_args)
                    req_result_json = json.loads(req_result)["Content"]
                    return_value = render.workspace( str(render.header(workspace_helpers().get_log_notification())), str(render.side_menu(workspace_helpers().get_menu_items_by_permissions())), str(render.footer()), str(self.__generate_html_content(json_data)), req_result_json)
            else :
                return_value = self.__generate_html_content(json_data) 

         
                
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
        try:
            data = web.input(_id="", parent_folder="", workspace="")
            return  self.__workspace_handler(data, 'GET')
        except Exception as e:
            traceback.print_exc()
     
    
    
    def POST(self):
        """ 
        POST function 
        
        """

        data = web.input(id="", parent_folder="", workspace="")    
        return  self.__workspace_handler(data, 'POST')