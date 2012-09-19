'''
Created on Sep 18, 2012

@author: stelios
'''
import web
import json
from config import render
import config
import urllib2
import urllib
import traceback


class services:
    """
    Services class, handles requests for specific services.
    
    Accepts GET and POST requests. 
  
    """
    def __init__(self):
        self.type = config.types["service"]

    
    def __validate_type(self, type_param):
        """
        Check if the type parameter is the same as the type of this class
        """
        if type_param == self.type:
            return True
        else:
            return False



    def __generate_html_content(self, data):
        """
        Generate HTML response based on json data
        """
        
        return_html = ''
                            
        if data == [] or data== None:
            return_html = "<div id='sub_item'>There was an error creating your service</div>"
        else :
            return_html = str(render.individual_service_response( '<a href=' + config.local_hostname['services'] + '?'+ config.service_get_param + '=' +  data["ident"] + '>' + data["name"] + '</a>', "Anonymous-identity" ))
        
        return return_html
    
    
    def __service_handler(self, data, request_type):
        """
        Handle requests for a service
        """
        return_value = ''
        id = data.id
        f= ""
        
        try:
            if id!="":
                request = urllib2.Request(id, headers={"Accept" : "application/json"})
                f = urllib2.urlopen(request)
                json_data = json.loads(f.read())
                json_data = dict([(str(k), v) for k, v in json_data.items()])
                if self.__validate_type(json_data["type"]):
                    if request_type == "GET":
                        return_value = render.index(str(self.__generate_html_content(json_data))) 
                    else :
                        return_value = self.__generate_html_content(json_data) 
                        
                else: 
                    return_value = config.errors['INVALID_TYPE'] + ': ' + json_data["type"]

            else :
                return_value = config.errors['INVALID_PARAM']
                
        except Exception as e:
            print e
            print traceback.print_exc()
            return_value = config.errors['INVALID_REQUEST']
                
        finally:
            if f!="":
                f.close()
        
        return  return_value
    

    def GET(self):
        """ 
        GET function 
        
        """
        
        data = web.input(id='')
        return  self.__service_handler(data, 'GET')
    
    
    def POST(self):
        """ 
        POST function 
        
        """
        
        data = web.input(id='')    
        return  self.__service_handler(data, 'POST')
    