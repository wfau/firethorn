'''
Created on Sep 20, 2012

@author: stelios
'''
from app import  session
from config import render, SURVEY_TAP_TITLE, registry_url, mode_global, no_users, survey_prefix,taps_using_binary
import freeform_sql
import web
import traceback
from globals import MyOutputStream, logging
import config
from helper_functions.dbHelper import *
import json
import freeform_sql.firethorn_config as firethorn_config

freeform_form = web.form.Form(web.form.Textarea('textfield',web.form.notnull,row=200,cols=30, class_='textfield', id='textfield',description=''),web.form.Hidden(name='tap_endpoint',id='tap_endpoint',value=""))
atlasworkspace = ""
atlasschema = ""
selected_cached_endpoints =""
unselected_cached_endpoints = ""
starting_catalogue_id = ""

  
class dbaccess_SQL_form: 

    """
    Freeform sql URL handling class.
    
    Accepts GET and POST requests. 
    Different parameters passed to the HTTP POST/GET functions determine the appropriate actions
    """
    
        
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request, by rendering the index template and returning the HTML content
        """
        
        web.header("Content-Type", "text/html")
        
        import freeform_sql.firethorn_helpers as firethorn_helpers
        from freeform_sql.freeform_config import add_catalogue_html, remove_catalogue_html
        
        try:
            atlasworkspace = config.firethorn_base + dict(line.strip().split('=') for line in open(config.userhomedir  + '/firethorn.testing'))['adqlspace']
            atlasschema = config.firethorn_base + dict(line.strip().split('=') for line in open(config.userhomedir  + '/firethorn.testing'))['atlasschema']
            selected_cached_endpoints = firethorn_helpers.generate_selected_list(config.SURVEY_DB, config.SURVEY_DB, atlasschema,remove_catalogue_html,session)
            unselected_cached_endpoints = firethorn_helpers.generate_available_list_from_workspace(atlasworkspace, add_catalogue_html, atlasschema,session )
            starting_catalogue_id = firethorn_helpers.create_initial_workspace(config.SURVEY_DB, config.SURVEY_DB, atlasschema,session)
        
        except Exception as e:
            atlasworkspace=""
            atlasschema=""
            selected_cached_endpoints = ""
            unselected_cached_endpoints = ""
            starting_catalogue_id = ""
            logging.exception("Error getting during firethorn initialization")
     
        ### Query form object
        freeform_form = web.form.Form(web.form.Textarea('textfield',web.form.notnull,row=200,cols=30, class_='textfield', id='textfield',description=''),web.form.Hidden(name='tap_endpoint',id='tap_endpoint',value=starting_catalogue_id))
        logging.exception(starting_catalogue_id)
   
        
        return render.dbaccess_SQL_form(freeform_form, "", SURVEY_TAP_TITLE, 'freeform' , survey_prefix )
       

    def POST(self):
        """
        POST function 
        
        Handle an HTTP POST request
        Check input data: 
        If the user has selected to toggle the endpoints selection, a variable named endpoint_form is passed to the class,
        in which case the server should generate the list from the registry URL (registry_url variable)
        If tap endpoints variable is defined, generate a merged TAP endpoint from the OGSA-DAI factory
        If the data display variable is set to true, generate TAP metadata, and return this HTML content to the user
        Else, if the main form validates, execute an asynchronous query as requested from the user input
        """

        web.header('Content-Type', 'text/html')
        data = web.input(tap_endpoints="none",display="no",generated_tap="none",endpoint_form="",prefixes="",table_mode="static", endpoint_arrangement = "freeform", resource_type= firethorn_config.types["workspace"])
        form = freeform_form     
        results_html_val = "No elements returned. Please check your query"
        metadata = ""
        tap_endpoint=""
        log_info = session.get('logged_in')
        next_order = "1"
        next_next_order = "2"
        _format = "votable"
        import freeform_sql.type_helpers as type_helpers
        
        if (log_info == 'False' or log_info == False) and no_users != True and data.endpoint_arrangement!="voexplorer":
            return 'NOT_LOGGED_IN'
        else: 
            if data.endpoint_form=="display":
                import freeform_sql.firethorn_helpers as firethorn_helpers
               
                from freeform_sql.freeform_config import add_catalogue_html, remove_catalogue_html
                try:
                    atlasworkspace = config.firethorn_base + dict(line.strip().split('=') for line in open(config.userhomedir  + '/firethorn.testing'))['adqlspace']
                    atlasschema = config.firethorn_base + dict(line.strip().split('=') for line in open(config.userhomedir  + '/firethorn.testing'))['atlasschema']
                    selected_cached_endpoints = firethorn_helpers.generate_selected_list(config.SURVEY_DB, config.SURVEY_DB, atlasschema,remove_catalogue_html,session)
                    unselected_cached_endpoints = firethorn_helpers.generate_available_list_from_workspace(atlasworkspace, add_catalogue_html, atlasschema,session )
                    
                except Exception as e:
                    atlasworkspace=""
                    atlasschema=""
                    selected_cached_endpoints = ""
                    unselected_cached_endpoints = ""
                    logging.exception("Error getting during firethorn initialization")
                   
                 
                if data.endpoint_arrangement =="voexplorer":
                    endpoint_list = json.dumps({'selected' : selected_cached_endpoints, 'unselected' : unselected_cached_endpoints  })  
           
                else :
                    endpoint_list = json.dumps({'selected' : selected_cached_endpoints, 'unselected' : unselected_cached_endpoints  })  
          
     
                
                if endpoint_list !="":
                    return endpoint_list
                else :
                    return ""
                    
            if data.tap_endpoints!="none":
                tap_endpoints = json.loads(data.tap_endpoints.strip())
                tap_endpoint = freeform_sql.generate_endpoint_from_list(tap_endpoints)            
                return tap_endpoint
            elif data.generated_tap!="none":
                tap_endpoint = data.generated_tap
            else:
                return "Please select from the list of TAP endpoints"
        
        
        
            if data.display == 'yes':
                if tap_endpoint!="": 
                    metadata_url = tap_endpoint
                    if type_helpers.isRootType(data.resource_type):
                        metadata_url = freeform_sql.get_parent_workspace(tap_endpoint) #+ "/vosi"
                    metadata = freeform_sql.get_children_metadata(metadata_url, data.resource_type)
                    
                    #metadata = freeform_sql.fetch_metadata(metadata_url)
              
                else:
                    return "No TAP endpoints selected"
                
                return metadata
            
            # Check for validation
            if tap_endpoint=="" or tap_endpoint==None or tap_endpoint=="none":
                return "Please select from the list of catalogues before submitting a query"
            elif form.validates():
                                    

                # Get query from textfield and execute it / Get mode based on radio buttons
                query = form['textfield'].value.strip()
    
                if query.lower().rfind('top') > -1:
                    if query.lower().rfind('order') < 0:
                        query += " ORDER BY 1"
                        next_order = "2"
                        next_next_order = "3"
                try:    
                    for i in taps_using_binary:
                        if tap_endpoint.startswith(i):
                            _format = 'votable/td'
                    
                    new_query_version1 = freeform_sql.generate_query_add_secondary_order(query,next_order)
                    results_html_val = freeform_sql.generate_JSON_from_query(freeform_sql.execute_async_query(tap_endpoint,mode_global,new_query_version1,_format),new_query_version1,tap_endpoint,data.table_mode)            
                    if results_html_val == "":
                        new_query_version2 = freeform_sql.generate_query_add_secondary_order(query,next_next_order )
                        results_html_val = freeform_sql.generate_JSON_from_query(freeform_sql.execute_async_query(tap_endpoint,mode_global,new_query_version2,_format),new_query_version2,tap_endpoint,data.table_mode)            
                    if results_html_val == "":
                        results_html_val = freeform_sql.generate_JSON_from_query(freeform_sql.execute_async_query(tap_endpoint,mode_global,query,_format),query,tap_endpoint,data.table_mode)            
                    if results_html_val == "":
                        results_html_val = freeform_sql.generate_JSON_from_query(freeform_sql.execute_async_query(tap_endpoint,mode_global,query,_format),query,tap_endpoint,data.table_mode)            
                except Exception as e:
                    logging.exception('Exception caught:')      
                if results_html_val !="":
                    return results_html_val
                else:
                    return "Error"
            else:
                return freeform_sql.freeform_config.ERROR_HTML

