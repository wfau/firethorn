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

    def __generate_html_content(self, json_data):
        """
        Generate HTML response based on json data
        """
        
        return_html = ''
     
        data = json.loads(json_data)
                            
        if data == [] or data== None:
            return_html = "<div id='sub_item'>There was an error creating your service</div>"
        else :
            converted_dict = dict([(str(k), v) for k, v in data.items()])
            return_html = str(render.individual_service_response( '<a href=' + config.local_hostname['services'] + '?'+ config.service_get_param + '=' +  converted_dict["ident"] + '>' + converted_dict["name"] + '</a>', "Anonymous-identity" ))
        
        return return_html
    
    
    def GET(self):
        """ 
        GET function 
        
        """
        
        data = web.input(id='')
        return_value = ''
        id = data.id
        f= ""
        
        try:
            if id!="":
                request = urllib2.Request(id, headers={"Accept" : "application/json"})
                f = urllib2.urlopen(request)
                json_data = f.read()
                return_value = render.index(str(self.__generate_html_content(json_data))) 
        
            else :
                return_value = config.error_dict['INVALID_PARAM']
                
        except Exception as e:
            print e
            print traceback.print_exc()

        finally:
            if f!="":
                f.close()
        
        return  return_value
    
    
    def POST(self):
        """ 
        POST function 
        
        """
        
        data = web.input(id='')
        return_value = ''
        id = data.id
        f = ""
       
        try:
            if id!="":
                request = urllib2.Request(id, headers={"Accept" : "application/json"})
                f = urllib2.urlopen(request)
                json_data = f.read()
                f.close()
                return_value = str(self.__generate_html_content(json_data))
        
            else :
                return_value = config.error_dict['INVALID_PARAM']
                
        except Exception as e:
            print e
            print traceback.print_exc()

        finally:
            if f!="":
                f.close()
        return  return_value
    