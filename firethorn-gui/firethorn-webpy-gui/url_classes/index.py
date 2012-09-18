'''
Created on Sep 18, 2012

@author: stelios
'''
import web
import json
from config import render
import traceback
import config
import urllib2
import urllib


class index:
    """
    Main index class, handles requests to the main page of the application.
    
    Accepts GET and POST requests. 
  
    """
    
    def __generate_html_content(self, json_data, action):
        """
        Generate HTML response based on json data
        """
        
        return_html = ''
     
        if action == 'service_select_by_name' or action == 'service_select_with_text':
            data = json.loads(json_data)
            if data == [] or data== None:
                return_html = "<div id='sub_item'>No services found</div>"
            else:
                for entry in data:
                    converted_dict = dict([(str(k), v) for k, v in entry.items()])
                    sub_item = render.select_service_response(converted_dict["url"], converted_dict["name"], converted_dict["ident"], converted_dict["created"], converted_dict["modified"])
                    return_html += str(sub_item)
       
        elif action == 'service_create':
            data = json.loads(json_data)
                                
            if data == [] or data== None:
                return_html = "<div id='sub_item'>There was an error creating your service</div>"
            else :
                converted_dict = dict([(str(k), v) for k, v in data.items()])
                return_html += "<div style='text-align:left;font-style:italic'>The following Service has been created: </div><br/>"
                return_html += str(render.select_service_response(converted_dict["url"], converted_dict["name"], '<a href=' + converted_dict["url"] + '>' + converted_dict["ident"] + '</a>', converted_dict["created"], converted_dict["modified"]))
        
        elif action == 'service_get':
            data = json.loads(json_data)
                                
            if data == [] or data== None:
                return_html = "<div id='sub_item'>There was an error creating your service</div>"
            else :
                converted_dict = dict([(str(k), v) for k, v in data.items()])
                return_html = str(render.individual_service_response( '<a href=' + converted_dict["url"] + '>' + converted_dict["ident"] + '</a>', converted_dict["name"], "Anonymous-identity" ))
            
            
        return return_html
    
    
    def __is_length_ok(self, strng):
        """
        Check if length of string is less than 1
        """
        if len(strng)<1:
            return False
        else :
            return True
        
        
    def __input_validator(self, action, action_value):
        """
        Validate input for index page parameters
        """
        if action_value!='' and action!='':
            return self.__is_length_ok(action_value)
        else :
            return False
    
    
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request, by rendering the index template and returning the HTML content
        """
      
        return render.index("")
    
    
    def POST(self):
        """
        POST function 
        
        Handle an HTTP POST request
        """
        
        data = web.input(service_select_with_text= '', service_select_by_name='',service_create='',service_get='')
        return_string = ''
        action_stored = False
        action = ''
        action_value = ''
        f = ''

        for key in data:
                
            if key == 'service_select_with_text':
                if data.service_select_with_text!='':
                    if action_stored:
                        return config.error_dict['INVALID_REQUEST']
                    else:
                        action = 'service_select_with_text'
                        action_value = data.service_select_with_text 
                        action_stored = True
                    
            elif key == 'service_select_by_name':
                if data.service_select_by_name!='':
                    if action_stored:
                        return config.error_dict['INVALID_REQUEST']
                    else:
                        action = 'service_select_by_name'
                        action_value = data.service_select_by_name
                        action_stored = True

            elif key == 'service_create':
                if data.service_create!='':
                    if action_stored:
                        return config.error_dict['INVALID_REQUEST']
                    else:     
                        action = 'service_create'
                        action_value = data.service_create
                        action_stored = True
                        
            elif key == 'service_get':
                if data.service_get!='':
                    if action_stored:
                        return config.error_dict['INVALID_REQUEST']
                    else:     
                        action = 'service_get'
                        action_value = data.service_get
                        action_stored = True
                    
            
        param_is_valid = self.__input_validator(action, action_value)
        
        try:
            
            if param_is_valid:
                encoded_args = urllib.urlencode({getattr(config, action + '_param') :  action_value})
                if action == 'service_create':
                    request = urllib2.Request(getattr(config, action+ '_url'), encoded_args, headers={"Accept" : "application/json"})
                elif action == 'service_select_by_name' or action == 'service_select_with_text':
                    request = urllib2.Request(getattr(config, action+ '_url') + encoded_args, headers={"Accept" : "application/json"})
                elif action == 'service_get':
                    request = urllib2.Request(config.local_hostname['services'], encoded_args)

                f = urllib2.urlopen(request)
                if action != 'service_get':
                    json_result = f.read()
                    return_string = self.__generate_html_content(json_result, action)
                elif action == 'service_get':
                    return_string = f.read()

                f.close()
            else :
                return_string = config.error_dict['INVALID_PARAM']
            
        except Exception as e:
            print e
            print traceback.print_exc()
            if f!="":
                f.close()
            return_string = config.error_dict['INVALID_NETWORK_REQUEST']
 
        return return_string
    
    