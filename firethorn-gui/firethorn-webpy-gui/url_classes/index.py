'''
Created on Sep 18, 2012

@author: stelios
'''
import web
import json
from app import render
import traceback
import config
import urllib2
import urllib
from datetime import datetime
from app import session
from helper_functions import login_helpers

class index:
    """
    Main index class, handles requests to the main page of the application.
    
    Accepts GET and POST requests. 
  
    """
    
    def __generate_html_content(self, json_data, action, db_type):
        """
        Generate HTML response based on json data
        """
        print json_data
        print db_type
        print action
        print config.local_hostname[db_type] 
        return_html = ''
        if action == 'db_select_by_name' or action == 'db_select_with_text':
            data = json.loads(json_data)
            if data == [] or data== None:
                return_html = "<div id='sub_item'>No databases found</div>"
            else:
                for entry in data:
                    converted_dict = dict([(str(k), v) for k, v in entry.items()])

                    sub_item = render.select_service_response('<a href=' + config.local_hostname[db_type] + '?' + config.get_param + '='  + urllib2.quote(converted_dict["ident"].encode("utf8")) + '>' + converted_dict["name"] + '</a>', datetime.strptime(converted_dict["created"], "%Y-%m-%dT%H:%M:%S.%f").strftime("%d %B %Y at %H:%M:%S"), datetime.strptime(converted_dict["modified"], "%Y-%m-%dT%H:%M:%S.%f").strftime("%d %B %Y at %H:%M:%S"))
                    return_html += str(sub_item)
       
          
        elif action == 'service_get':
            data = json.loads(json_data)
                                
            if data == [] or data== None:
                return_html = "<div id='sub_item'>There was an error creating your database</div>"
            else :
                converted_dict = dict([(str(k), v) for k, v in data.items()])
                return_html = str(render.individual_service_response( '<a href='  + config.local_hostname[db_type] + '?' + config.get_param + '=' + converted_dict["ident"] + '>' + converted_dict["name"] + '</a>', "Anonymous-identity" ))
            
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
      
        return render.index( str(render.header(login_helpers(session).get_log_notification())), str(render.side_menu(login_helpers(session).get_menu_items_by_permissions())), str(render.index_input_area()), str(render.footer()), "")
         
         
    def POST(self):
        """
        POST function 
        
        Handle an HTTP POST request
        """
        
        data = web.input(db_select_with_text= '', db_select_by_name='',service_get='',db_type='all')
        return_string = ''
        action_stored = False
        action = ''
        action_value = ''
        f = ''
        
        
        for key in data:
                
            if key == 'db_select_with_text':
                if data.db_select_with_text!='':
                    if action_stored:
                        return json.dumps({
                                    'Code' : -1,
                                    'Content' : config.errors['INVALID_REQUEST']
                                })
                    else:
                        action = 'db_select_with_text'
                        action_value = data.db_select_with_text 
                        action_stored = True
                    
            elif key == 'db_select_by_name':
                if data.db_select_by_name!='':
                    if action_stored:
                        return json.dumps({
                                    'Code' : -1,
                                    'Content' : config.errors['INVALID_REQUEST']
                                })
                    else:
                        action = 'db_select_by_name'
                        action_value = data.db_select_by_name
                        action_stored = True
                        
            elif key == 'service_get':
                if data.service_get!='':
                    if action_stored:
                        return json.dumps({
                                    'Code' : -1,
                                    'Content' : config.errors['INVALID_REQUEST']
                                })
                    else:     
                        action = 'service_get'
                        action_value = data.service_get[data.service_get.index('id=')+3:]
                        action_stored = True
                    
            
        param_is_valid = self.__input_validator(action, action_value)
        
        try:
            
            if param_is_valid:
                encoded_args = urllib.urlencode({getattr(config, action + '_params')[data.db_type] :  action_value})
                if action == 'db_select_by_name' or action == 'db_select_with_text':
                    request = urllib2.Request(getattr(config, action + '_urls')[data.db_type] + encoded_args, headers={"Accept" : "application/json"})
                elif action == 'service_get':
                    request = urllib2.Request(config.local_hostname['services'], encoded_args)
            
                f = urllib2.urlopen(request)
                
                if action != 'service_get':
                    json_result = f.read()
                    return_string = self.__generate_html_content(json_result, action,data.db_type)
                else:
                    return_string = f.read()
                    
                f.close()
            else :
                return json.dumps({
                                    'Code' : -1,
                                    'Content' : config.errors['INVALID_PARAM']
                                })
            
        except Exception as e:
            print traceback.print_exc()
            if f!="":
                f.close()
            return json.dumps({
                                    'Code' : -1,
                                    'Content' : config.errors['INVALID_REQUEST']
                                })
 
        return json.dumps({
                        'Code' : 1,
                        'Content' : return_string
                    })
    
    