'''
Created on Sep 18, 2012

@author: stelios
'''
import web
import json
from config import render
import config

class services:
    """
    Services class, handles requests for specific services.
    
    Accepts GET and POST requests. 
  
    """

    def __generate_html_content(self, id, json_data):
        """
        Generate HTML response based on json data
        """
        
        return_html = ''
     
     
        data = json.loads(json_data)
                            
        if data == [] or data== None:
            return_html = "<div id='sub_item'>There was an error creating your service</div>"
        else :
            converted_dict = dict([(str(k), v) for k, v in data.items()])
            return_html = str(render.individual_service_response( '<a href=' + converted_dict["url"] + '>' + converted_dict["ident"] + '</a>', converted_dict["name"], "Anonymous-identity" ))
        
        return return_html
    
    
    def GET(self):
        """ 
        GET function 
        
        """
        
        data = web.input(id='')
        return_value = ''
        id = data.id
        print id
        if id=='':
            return_value = config.error_dict["INVALID_PARAM"]
        else :
            return_value = render.index(str(render.individual_service_response(id,id, id))) 
        
        return  return_value
    
    
    def POST(self):
        """ 
        POST function 
        
        """
        
        data = web.input(id='')
        return_value = ''
        id = data.id
        print id
        
        if id=='':
            return_value = config.error_dict["INVALID_PARAM"]
        else :
            return_value = str(render.individual_service_response(id,id, id)) 
        
        return  return_value
    