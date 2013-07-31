"""
misc module

Miscellaneous functions used by for freeform SQL query processing for use with TAP services and XML VOTable procesing

Created on Nov 30, 2011
@author: stelios
"""

import os
import urllib2
import urllib
import StringIO
import time
import xml.dom.minidom
import atpy
import json
import numpy
import html_functions
import xml_parser
from freeform_config import *
import re
from file_handler import File_handler
from helper_functions.string_functions import string_functions
string_functions = string_functions()
from config import survey_prefix,SURVEY_DB,base_location, debug_mode
from survey_globals import archive_input
from globals import MyOutputStream, logging
import datetime
import firethorn_config
from app import session


def clear_temp_folder():
    """
    Clean the temporary folder used for query data, from files that are older than one day
    
    """
    
    try:    
        path = (cur_static_dir + 'temp/')
        listing = os.listdir(path)
        curtime = time.time()
        for infile in listing:
            temp_file = path + infile
            ftime = os.path.getmtime(temp_file)
            difftime = curtime - ftime
            if difftime > 86400:
                if not os.path.isdir(temp_file):
                    os.unlink(temp_file)
    except Exception as e:
        logging.exception('Exception caught:')
       
       
       


def change_destruction_time(url,new_time):
    """ 
    Change the destruction time for a TAP job

    ATTENTION: Currently unused in this version
    
    @param url: The URL for the TAP job to destroy
    @param new_time : The current time to set for the destruction of the TAP job
    """
    
    f = ''
    try:
        params = urllib.urlencode({'DESTRUCTION' : new_time}) 
        url = url+'/destruction'
        req = urllib2.Request(url, params)
        f = urllib2.urlopen(req)
        f.close()
    except Exception:
        if f!='':
            f.close()
        logging.exception('Exception caught:')

 
 



def generate_form_data(registry):
    """
    Fetch array of available TAP endpoints from the input registry. Uses XQuery, and returns a tuple of web.form.Checkbox objects, to be added to the tap endpoint selection form
    
    @param registry: A string containin the Registry URL
    @return: An tuple containing the registry endpoints as web.checkbox elements
    """

    url_query = """
    //RootResource[@status='active' and capability[@standardID='ivo://ivoa.net/std/TAP']]/capability[@standardID='ivo://ivoa.net/std/TAP']/interface/accessURL
    """


    title_query = """
    //RootResource[@status='active' and capability[@standardID='ivo://ivoa.net/std/TAP']]/title
    """ 

  
    form_data=""
    title_results = []
    tap_url_results = []
    f=''
    try:
        # get title elements
        params = urllib.urlencode({'performquery' : 'true', 'ResourceXQuery' : title_query}) 
        req = urllib2.Request(registry, params)
        f = urllib2.urlopen(req)
        result_string = f.read()
       
        RE_XML_ILLEGAL = u'([\u0000-\u0008\u000b-\u000c\u000e-\u001f\ufffe-\uffff])' + \
                 u'|' + \
                 u'([%s-%s][^%s-%s])|([^%s-%s][%s-%s])|([%s-%s]$)|(^[%s-%s])' % \
                  (unichr(0xd800),unichr(0xdbff),unichr(0xdc00),unichr(0xdfff),
                   unichr(0xd800),unichr(0xdbff),unichr(0xdc00),unichr(0xdfff),
                   unichr(0xd800),unichr(0xdbff),unichr(0xdc00),unichr(0xdfff))
        result_string = re.sub(RE_XML_ILLEGAL, "?", result_string)
        
        result_string = result_string.decode('latin-1')
        dom = xml.dom.minidom.parseString(result_string.encode('utf-8'))
        title_results=dom.getElementsByTagName("title")

        # get TAP urls
        params2 = urllib.urlencode({'performquery' : 'true', 'ResourceXQuery' : url_query}) 
        req2 = urllib2.Request(registry, params2)
        f2 = urllib2.urlopen(req2)
        result_string2 = f2.read()
        dom2 = xml.dom.minidom.parseString(result_string2)
        tap_url_results=dom2.getElementsByTagName("accessURL")
      
        counter = 0
        for i in tap_url_results:
            form_data+='<li><span>' + xml_parser.getText(title_results[counter]) + '</span> as <input type="text" name="catalogue"  value="'  + xml_parser.getText(title_results[counter]) +  '" /><input type="hidden" name="ident" value="' + xml_parser.getText(i) + '"/>' +  remove_catalogue_html + '</li>'
            counter = counter +1
            
        f.close()
        f2.close()
    
    except Exception as e:
        if f!='':
            f.close()   
        logging.exception('Exception caught:')      
        return ""
    if form_data=="":
        return ""
    else:
        return form_data






def generate_endpoint_from_list(endpnt_list):
    """
    Takes an list of TAP endpoint URLs separated by a double space, and generates a merged TAP service from the OGSDA-DAI TAP factory(global tap_factory variable)
           
    @param endpnt_lits: A list of available TAP endpoints as strings
    @param prefixes: A list of prefixes as a json object, from which we extract URL & name and submit to the Registry
    @return: A string with the new URL to be used by the client
    """
    
    data = ""
    result= ""
    urls = endpnt_list
    response = ''
    counter = 0
    query_schema = ''
    importname=""
    
    from datetime import datetime
    t = datetime.now()
   
    try:
       
        ### Create workspace
        workspace_name = 'workspace-' + t.strftime("%y%m%d_%H%M%S") 
        data = urllib.urlencode({firethorn_config.resource_create_name_params['http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json'] : workspace_name})
        req = urllib2.Request( firethorn_config.workspace_creator, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")})
        response = urllib2.urlopen(req)
        workspace = json.loads(response.read())["ident"]
        response.close()
        
        ### Create Query Schema 
        data = urllib.urlencode({firethorn_config.resource_create_name_params['http://data.metagrid.co.uk/wfau/firethorn/types/adql-schema-1.0.json'] : "query_schema"})
        req = urllib2.Request( workspace +  firethorn_config.schema_create_uri, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")})
        response = urllib2.urlopen(req) 
        query_schema = json.loads(response.read())["ident"]
        response.close()
        
      
        for i in urls:
            importname = ""
            name = i['name']
            alias = i['alias']
            ident = i['ident']
   
        
            if alias!="":
                importname = alias
            else :
                importname = name
        
            if importname!="":
                data = urllib.urlencode({'urn:adql.copy.depth' : "THIN", firethorn_config.workspace_import_schema_base : ident, firethorn_config.workspace_import_schema_name : importname})
            else :
                data = urllib.urlencode({'urn:adql.copy.depth' : "THIN", firethorn_config.workspace_import_schema_base : ident})
            
          
            req = urllib2.Request( workspace + firethorn_config.workspace_import_uri, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")}) 
            response = urllib2.urlopen(req)
            response.close()
            
            
    except Exception:
        if response!='':
            response.close()
            result = "" 
        logging.exception("IOError")  
    
  
      
    return query_schema




#######################################
# Note: Not used in Firethorn version #
#######################################
def fetch_metadata(tap_endpoint):
    """
    Takes a TAP endpoint URL, and generates the table metadata. This is done by reading and parsing the XML table information using the xml_dom.minidom library and the xml_parser module
    
    @param tap_endpoint: A string with the TAP endpoint
    @return: HTML content with the metadata for the given URL
    """
    
    return_str = ''
    f = ''
    try:
        f = urllib.urlopen(tap_endpoint)
        dom_str = f.read()
        dom = xml.dom.minidom.parseString(dom_str)
        return_str = xml_parser.handleXML(dom)
        f.close()
    except Exception as e:
        if f!='':
            f.close()
        return_str =  ""   
        logging.exception(e)   
    return return_str


     
def get_children_metadata(ident, _type):
 
    import type_helpers
    from datetime import datetime
    
    error_processing = "Unable to fetch the table metadata"
    return_val = ''
    f= ""
    ident = string_functions.decode(ident)
    _type = string_functions.decode(_type)
     
    try:
        if ident!="" and _type!="":
       
            if not type_helpers.isColumn(_type):
                ident = ident + firethorn_config.resource_uris[_type]
            request = urllib2.Request(ident, headers={"Accept" : "application/json"})
            logging.exception(ident)
            f = urllib2.urlopen(request)
            json_data = json.loads(f.read())
                
            if json_data == [] or json_data== None:
                return_val = "No data found"
            else:
                if type_helpers.isRootType(_type):
                    return_val += '<p class="accordion_table_title">Catalogues:</p>'
                
                if type_helpers.isSchema(_type):
                    return_val += '<p class="accordion_table_title">Tables:</p>'
                
                if type_helpers.isTable(_type):
                    return_val += '<p class="accordion_table_title">Columns:</p>'
                
                if type_helpers.isColumn(_type):
                
                    return_val += "<div>"
                    return_val += "Description: "  + "</br>"
                    return_val += "Data Type: " + str(json_data["info"]["jdbc"]["type"]) +"</br>"
                    return_val += "UCD: " +  "</br>"
                    return_val += "Unit: " +   "</br>"
                    return_val += "Size: " +  str(json_data["info"]["jdbc"]["size"])  +"</br>"
                    return_val += "</div>"
                    return_val += "</br>"
                    
                else:
                                
                    for entry in json_data: 
                        if type_helpers.isRootType(_type):
                            return_val+='<div id="accordion_catalogue">'
                            converted_dict = dict([(str(k), v) for k, v in entry.items()])
                          
                            if converted_dict["fullname"]!='query_schema':
                                return_val += '<input type="hidden" name="resource_type" value="' + converted_dict["type"]+ '" /> <input type="hidden" name="resource_ident" value="' + string_functions.encode(converted_dict["ident"]) +'" />'   +  "<div  class='heading'>" + converted_dict["fullname"] + '<img style="float:right;margin-top:5px" src="' + survey_prefix + '/static/static_vo_tool/asc.gif"/> </div>'
                            return_val+='</div>'
                        elif type_helpers.isSchema(_type):
                            return_val+='<div id="accordion_schema">'
                            converted_dict = dict([(str(k), v) for k, v in entry.items()])
                            return_val += '<input type="hidden" name="resource_type" value="' + converted_dict["type"]+ '" /> <input type="hidden" name="resource_ident" value="' + string_functions.encode(converted_dict["ident"]) +'" />'   + "<div  class='heading'>" + converted_dict["fullname"] + '  <img style="float:right;margin-top:5px" src="' + survey_prefix + '/static/static_vo_tool/asc.gif"/> </div>'
                            return_val+='</div>'
                        elif type_helpers.isTable(_type):
                            return_val+='<div id="accordion_table">'
                            converted_dict = dict([(str(k), v) for k, v in entry.items()])
                            return_val += '<input type="hidden" name="resource_type" value="' + converted_dict["type"]+ '" /> <input type="hidden" name="resource_ident" value="' + string_functions.encode(converted_dict["root"]) +'" />'   + "<div  class='heading'>" + converted_dict["fullname"] + '  <img style="float:right;margin-top:5px" src="' + survey_prefix + '/static/static_vo_tool/asc.gif"/></div> '
                            return_val+='</div>'
                       
                    
    except Exception:
        if f!="":
            f.close()    
        logging.exception("Error fetching metadata")
        return error_processing
    
    finally:
        if f!="":
            f.close()  
   
    return return_val

def get_async_results(url, URI):
    """
    Open the given url and URI and read/return the result 

    @param url: A URL string to open
    @param URI: A URI string to attach to the URL request
    @return: The result of the HTTP request sent to the URI of the URL
    """
    
    res = ''
    f = ''
    try:
        req = urllib2.Request(url+URI)
        f = urllib2.urlopen(req)
        res =  f.read()
        f.close()
    except Exception as e:
        if f!='':
            f.close()
        logging.exception('Exception caught:')
              
    return res



def start_query_loop(url):
    """
    
    @param url: A URL string to be used
    @return: Results of query
    """
    
    def get_status(url):
        request2 = urllib2.Request(url, headers={"Accept" : "application/json", "firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")})
        f_read = urllib2.urlopen(request2)
        query_json = f_read.read()
        return query_json
    
    max_size_exceeded = False 
   
    f_read = ""
    return_vot = ''
    delay = 2
    start_time = time.time()
    elapsed_time = 0
    query_json = {'syntax' : {'friendly' : 'A problem occurred while running your query', 'status' : 'Error' }}
    
    try:
     
        data = urllib.urlencode({ firethorn_config.query_status_update : "RUNNING"})
        request = urllib2.Request(url, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")})
        f_update = urllib2.urlopen(request)
        query_json =  json.loads(f_update.read())
        query_status = query_json["status"]
        
        
        while query_status=="PENDING" or query_status=="RUNNING" and elapsed_time<3600:
            query_json = get_status(url)
            query_status= json.loads(query_json)["status"] 
            time.sleep(delay)
            elapsed_time = time.time() - start_time
       
        if query_status=="ERROR" or query_status=="FAILED":
            return {'Code' :-1,  'Content' : 'Query error: A problem occurred while running your query' }
        elif query_status=="CANCELLED":
            return {'Code' :1,  'Content' : 'Query error: Query has been canceled' }
        elif query_status=="EDITING":
            return {'Code' :-1,  'Content' : 'Query error: ' + query_json["syntax"]["status"] + ' - ' + query_json["syntax"]["friendly"] }
        elif query_status=="COMPLETED":
            return {'Code' :1,  'Content' : url + '/votable' }
        else :
            return {'Code' :-1,  'Content' : 'Query error: A problem occurred while running your query' }
        
    
    except Exception as e:
        logging.exception('Exception caught:')        
        return []

    if f_read != "":
        f_read.close() 
    return return_vot    



def start_async_loop(url):
    """
    Takes a TAP url and starts a loop that checks the phase URI and returns the results when completed. The loop is repeated every [delay=3] seconds

    @param url: A URL string to be used
    @return: A Votable with the results of a TAP job, or '' if error
    """
    
    max_size_exceeded = False 
    f = ""
    return_vot = ''
    try:
        while True:
            res = get_async_results(url,'/phase')
            if res=='COMPLETED':       
                req = urllib2.Request(url + '/results/result')
                f = urllib2.urlopen(req)
                f.read(MAX_FILE_SIZE) #100Mb = 104,857,600
                if len(f.read())>0:
                    max_size_exceeded = True
                if not max_size_exceeded:
                    return_vot = atpy.Table(url + '/results/result', type='vo')
           
                else:
                    return_vot = "MAX_ERROR" 
                break
            elif res=='ERROR' or res== '':
                return return_vot
            time.sleep(delay)
    except Exception as e:
        logging.exception('Exception caught:')
        
        if f != "":
            f.close()
        return_vot = ''
    return return_vot    

        




def print_save_HTML_table_info(pathname):
    """
    Generates HTML content for HTML table save button
        
    @param tbl: A table with the results of a query as a JSON, escaped array
    @return: The HTML content with a save as button
    """
        

    result = '<div id="save_as"><form name="save_as_html" action="' + survey_sub_path + 'save_as_html" method="post"> <input type="hidden" name="pathname" value ="' 
    result += pathname + '"/><input type="submit" class ="save_button" value="Save as HTML" /></form></div>'
    return result
    
    




def print_save_VOTable_info(pathname):    
    """
    Generates HTML content for VOTable save button    

    @param vot: A table with the results of a query as a JSON, escaped array
    @return: The HTML content with a save as button
    """

    result = '<div id="save_as"><form name="save_as_vot" action="' + survey_sub_path + 'save_as_vot" method="post"> <input type="hidden" name="pathname" value ="' 
    result += pathname + '"/><input type="submit" class ="save_button" value="Save as Votable" /></form></div>'
    return result





def print_save_Fits_info(pathname):    
    """
    Generates HTML content for VOTable save button    

    @param vot: A table with the results of a query as a JSON, escaped array
    @return: The HTML content with a save as button
    """
    
    result = '<div id="save_as"><form name="save_as_fits" action="' + survey_sub_path + 'save_as_fits" method="post"> <input type="hidden" name="pathname" value ="' 
    result += pathname + '"/><input type="submit" class ="save_button" value="Save as Fits" /></form></div>'
    return result




def execute_async_query(url,mode_local,q,_format="votable"):
    """
    Execute an ADQL query (q) against a TAP service (url + mode:sync|async)       
    Starts by submitting a request for an async query, then uses the received job URL to call start_async_loop, to receive the final query results 

    @param url: A string containing the TAP URL
    @param mode: sync or async to determine TAP mode of execution
    @param q: The ADQL Query to execute as string
    
    @return: Return a votable with the results, the TAP job ID and a temporary file path with the results stored on the server
    """    

    votable = ''
    jobId= 'None'
    file_path=''
    rows=[]
    now = datetime.datetime.now()
    
    try:

        # Return results as a votable object
       
        votable = run_query(q,"",query_space=url)
       
        if votable!='' and votable !="MAX_ERROR" and getattr(votable, "columns", None)!=None:
            
           
            clear_temp_folder()
            cols = list(votable.columns)
            
            lst = []
            
            rows = votable.data.tolist()
            for x in rows:
                second_counter = 0
                obj_list = {}
                for y in x:
                  
                    obj_list[cols[second_counter]] = y 
                    second_counter = second_counter + 1
                lst.append(obj_list)
            
            lst = [cols,lst] 
            file_handle = File_handler()
            file_handle.create_temp_file()
            file_handle.write_to_temp_file(json.dumps(lst))
            file_handle.close_handle()    
            file_path = file_handle.pathname
            
    except Exception as e:
        logging.exception('Exception caught:')

    
    

    try:
        if debug_mode != True:
            from web import ctx
           
            if votable=="" or votable==None or votable ==[] or getattr(votable, "columns", None)==None:
                userip =  "None" if str(web.ctx.ip)==None  else  str(web.ctx.ip)
                dbrelease = "Unknown" if url==None else url
                rows_error = -1
                MyOutputStream().log_queries(now.strftime('%Y-%m-%d %H:%M:%S.%f')[:-4], int((datetime.datetime.now() - now).total_seconds()), q, rows_error,rows_error, 
                                             session.get("username","unknown user"), dbrelease , userip)
                
            else:
                
                userip =  "None" if str(web.ctx.ip)==None  else  str(web.ctx.ip)
                dbrelease = "Unknown" if url==None else url
    
                MyOutputStream().log_queries(now.strftime('%Y-%m-%d %H:%M:%S.%f')[:-4], int((datetime.datetime.now() - now).total_seconds()), q, len(rows),len(votable.columns), 
                                             session.get("username","unknown user"), dbrelease , userip)
    except Exception:
        logging.exception('Exception caught:')


    return (votable,jobId,file_path)




def run_query(query=None, query_name="", query_space="", **kwargs):
    """
    Run a query on a resource
    """
    f=''
  
    query = string_functions.decode(query)
    query_space = string_functions.decode(query_space)
    result = ""
    max_size_exceeded = False
    try :
        from datetime import datetime
        t = datetime.now()
        if query_name=="":
            query_name = 'query-' + t.strftime("%y%m%d_%H%M%S")
     
        data = urllib.urlencode({ firethorn_config.query_name_param : query_name,  firethorn_config.query_param : query})
        request = urllib2.Request(query_space + firethorn_config.query_create_uri, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")})
        f = urllib2.urlopen(request)
        query_create_result = json.loads(f.read())
   
        query_loop_results = start_query_loop(query_create_result["ident"])
      
        if query_loop_results.get("Code", "") !="":
            if query_loop_results.get("Code", "") ==-1:
                result = query_loop_results.get("Content", "Error")
            elif query_loop_results.get("Code", "") ==1:
                req = urllib2.Request(query_loop_results.get("Content", ""), headers={"firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")})
                f = urllib2.urlopen(req)
                f.read(MAX_FILE_SIZE) #100Mb = 104,857,600
             
                if len(f.read())>0:
                    max_size_exceeded = True
                if not max_size_exceeded:
                    result = atpy.Table(query_loop_results.get("Content", ""), type='vo')
                else:
                    result = "MAX_ERROR" 
             
        else :
      
            return "Error"
            
    except Exception:
        logging.exception('Exception caught:')
        
        return "Error"
    
    if f!='':
        f.close()
    return result
    
    

def generate_JSON_from_query((votable,jobId,pathname),query="",tap_endpoint="",table_mode="static"):    
    """
    Generates HTML content for the results from an ADQL/TAP query    
    
    @param (votable,jobId,pathname): A tuple with the votable results, the jobID and the temporary file path
    @param query: The query that was executed
    @param tap_endpoint: The currently selected or generated TAP endpoint URL
    @return: The HTML content to be returned and asynchronously loaded on the users page after submitting a query
    """
    
    data_results = ""
    json_list = ""
    content = ""

    if votable == "MAX_ERROR" or (getattr(votable, "columns", None)==None and (votable!='' and votable!= None)):
        data_results = votable
        return data_results
    elif votable!='' and votable!= None:
        if len(votable)>0:
            cols = list(votable.columns)
            row_list = votable.data.tolist() 
            if tap_endpoint!="" and query!="":
                content = '<form class="launch_viewer" style="float:left;z-index:100;position: relative;" action="' + survey_prefix + '/viewer" method="post" target="_blank"><input type="hidden" name="query" value="'+ string_functions.encode(query) +'"/><input type="hidden" name="cols" value="'+ html_functions.escape(json.dumps(cols)) +'"/><input type="hidden" name="filepath" id="temp_file" value="'+ pathname +'"/><input type="hidden" name="tap_endpoint" value="'+ tap_endpoint +'"/>'
                if table_mode == 'interactive':
                    data_results = html_functions.escape_list(row_list)
                    json_list = html_functions.escape(json.dumps(data_results))
                    content += '<input type="hidden" id="hidden_json_results" name="results" value="' + json_list  + '" />'
                content += '<div style="float:left;clear:both;text-align:left;margin-left:20px;font-size:13px;color:#C2BEAD">Launch in Plotter<input type="submit" value="" /></form><br /><br /></div>'
            else:
                content = '<input type="hidden" name="filepath" id="temp_file" value="'+ pathname +'"/>'
            content += '<div id="save_as_information" style="display:none">' + print_save_VOTable_info(pathname) + print_save_HTML_table_info(pathname) + print_save_Fits_info(pathname) + '</div>'
            content += '<div style="text-align:left;margin-left:15px;float:left;z-index:100;position: relative;clear:both"><img id ="samp_connection" src="' + survey_prefix + '/static/images/red-button.png" height="17" width="17" /><button id="register">Connect to SAMP</button><button id="unregister" style="display: none;">Disconnect from SAMP</button><button id="loadvotable" style="display: none;">Broadcast results table</button></div>'

            data_results = [cols]
            data_results.append([len(row_list)])
            data_results.append([content])
            return json.dumps(data_results)
        else:
            return json.dumps([])
    else:
        return data_results
    



def execute_async_region_query(url,mode_local,q):
    """
    Execute an ADQL query (q) against a TAP service (url + mode:sync|async)       
    Starts by submitting a request for an async query, then uses the received job URL to call start_async_loop, to receive the final query results 

    @param url: A string containing the TAP URL
    @param mode: sync or async to determine TAP mode of execution
    @param q: The ADQL Query to execute as string
    
    @return: Return a votable with the results, the TAP job ID and a temporary file path with the results stored on the server
    """    

    params = urllib.urlencode({'REQUEST': request, 'LANG': lang, 'FORMAT': result_format, 'QUERY' : q}) 
    full_url = url+mode_local

    votable = ''
    jobId= 'None'
    file_path=''
    try:
        
        #Submit job and get job id 
        req = urllib2.Request(full_url, params)
        opener = urllib2.build_opener()
  
        f = opener.open(req)
        jobId = f.url
  
        #Execute job and start loop requests for results
        req2 = urllib2.Request(jobId+'/phase',urllib.urlencode({'PHASE' : 'RUN'}))
        f2 = opener.open(req2) #@UnusedVariable

        # Return results as a votable object
        votable = start_async_loop(jobId)
        
        if votable!='' and votable !="MAX_ERROR":
            clear_temp_folder()
            cols = list(votable.columns)
            cols.insert(0,"getFSLink")
            col_data_to_list =  list(votable.columns)
            
            ra_index = col_data_to_list.index('ra')
            dec_index = col_data_to_list.index('dec')
   
            if  'frameSetID' in col_data_to_list:
                framesetID_index = col_data_to_list.index('frameSetID')
            else:
                framesetID_index = -1
                if 'multiframeID' in col_data_to_list:
                    multiframeID_index = col_data_to_list.index('multiframeID')
                else:
                    multiframeID_index = -1


            db = SURVEY_DB
           
            temp_row=[]
            for i in votable.data.tolist():
                if framesetID_index == -1:
                    getFSLink = '<a target="getI" href="' + getImageURL +'?mode=show&ra=' + str(i[ra_index])  + '&dec=' + str(i[dec_index]) + '&mfid=' + str(i[multiframeID_index]) + '&database=' + db + '&archive=' + archive_input +'">view</a>'
                
                else :
                    getFSLink = '<a target="getI" href="' + getImageURL +'?mode=show&ra=' + str(i[ra_index])  + '&dec=' + str(i[dec_index]) + '&fsid=' + str(i[framesetID_index]) + '&database=' + db + '&archive=' + archive_input +'">view</a>'
                temp_row.append(getFSLink)
           
            votable.add_column(name="getFSLink",data=temp_row,position=0)
            
            
            lst = []
            for x in votable.data.tolist():
                second_counter = 0
                obj_list = {}
                for y in x:
                    obj_list[cols[second_counter]] = y 
                    second_counter = second_counter + 1
                lst.append(obj_list)
            
            #html_functions.escape_list(votable.data.tolist())
            lst = [cols,lst] 
            file_handle = File_handler()
            file_handle.create_temp_file()
            file_handle.write_to_temp_file(json.dumps(lst))
            file_handle.close_handle()    
            file_path = file_handle.pathname
            
    except Exception as e:
        logging.exception('Exception caught:')

    return (votable,jobId,file_path)



def execute_async_imagelist_query(cols,rows):
    """
    Generate results for imagelist query
    """    
     
 
    votable = ''
    file_path=''

    try:
        
        if cols!=None and rows !=None:
            clear_temp_folder()
 
            col_data_to_list =  cols
            votable = atpy.Table()
            cntr = 0
            
            for i in cols:
                temp_row = []
            
                for y in rows:
                    if type(y[cntr]) == long:
                        temp_row.append(float(y[cntr]))
                    elif type(y[cntr]) == int or type(y[cntr]) == float:
                        temp_row.append(y[cntr])
                    else:
                        temp_row.append(str(y[cntr]))
                   
                votable.add_column(i,data=temp_row,position=cntr)
                cntr = cntr + 1
                
            filename = col_data_to_list.index('filename')
            catname = col_data_to_list.index('catname')
            jpegBase = col_data_to_list.index('jpegBase')
            ext = col_data_to_list.index('numDetectors')
            mfid = col_data_to_list.index('multiframeID')
            rID = 1
            
            temp_row_view=[]
            temp_row_img=[]
            temp_row_cat=[]
            
            
            for i in votable.data.tolist():
                
                view = '<a id="ImageList_facebox" target="display" href="' + survey_cgi_bin + 'display.cgi?file=' + str(i[filename]) + '&cat=' + str(i[catname]) + '&comp=' + str(i[jpegBase]) + '&noExt=' + str(i[ext]) + '&MFID=' + str(i[mfid]) + '&rID=' + str(rID) +'">view</a>'
                img = '<a  href="' + survey_cgi_bin + 'fits_download.cgi?file=' + str(i[filename]) +'&MFID=' + str(i[mfid]) + '&rID=' + str(rID) +'">FITS</a>'
                cat = '<a  href="' + survey_cgi_bin + 'fits_download.cgi?file=' + str(i[catname]) + '&amp;MFID=' + str(i[mfid]) + '&amp;rID=' + str(rID) + '">FITS</a>'
                temp_row_view.append(view)
                temp_row_img.append(img)
                temp_row_cat.append(cat)
            
            votable.remove_columns(['jpegBase','filename','catname'])

            votable.add_column(name="View",data=temp_row_view,position=0)
            votable.add_column(name="Img",data=temp_row_img,position=1)
            votable.add_column(name="Cat",data=temp_row_cat,position=2)

            cols = list(votable.columns)
            
            lst = []
            for x in votable.data.tolist():
                second_counter = 0
                obj_list = {}
                for y in x:
                    obj_list[cols[second_counter]] = y 
                    second_counter = second_counter + 1
                lst.append(obj_list)
            
            #html_functions.escape_list(votable.data.tolist())
            final_lst = [cols,lst] 
            file_handle = File_handler()
            file_handle.create_temp_file()
            file_handle.write_to_temp_file(json.dumps(final_lst))
            file_handle.close_handle()    
            file_path = file_handle.pathname

           
    except Exception as e:
        logging.exception('Exception caught:')
    return (votable,"",file_path)
   

 
def write_format_from_json(to_format,json_list, visible_cols=None):
    """
    Generates HTML to_format for a json array of data
    
    @param to_format: The format to which to transform a JSON list of data
    @param json_list: The source json list
    """
    
    html_array = json_list
    fields = html_array[0] if visible_cols == None else visible_cols
    data = html_array[1]
    t = atpy.Table()
    
    if to_format == 'csv':
   
        csv = ''
        counter = 0 
        if visible_cols != None:
            for col in fields:
                counter = counter + 1
                if counter == len(fields):
                    csv += str(col)
                else:
                    csv += str(col) + ','
       
        csv += '\n'
       
        for row in data:
            values = []
            for k,v in row.items():
                if k in fields:
                    values.append(str(v))  
           
            csv += ",".join(values) 
            csv += '\n'
            
        result = csv
    
    else:    
        
        for val in fields:
            
            col_data = []
            for row in data:
              
                if(type(row[val])==unicode):
                    col_data.append(numpy.str(row[val]))
                    mytype = numpy.string_
                else:
                    col_data.append(row[val])
                    mytype = type(row[val])
            try:
                t.add_column(str(val),numpy.array(col_data),dtype=mytype)
            except Exception as e:
                t.add_column(str(val),numpy.array(col_data),dtype=numpy.string_)
    
        d = StringIO.StringIO()
        t.write(d, type=to_format)
        result = d.getvalue()
        d.close()

    
    return result            



def generate_query_add_secondary_order(query, colid):
    """
    Add a secondary order clause to an sql query to help with data consistency when data from query returned has the same value
    """
    query_as_string = query
    if query.lower().find(" order ")>0:
        query = query.replace('(', ' ( ')
        query = query.replace(')', ' ) ')
        query = query.replace(',', ' , ')
        query_list = query.split(" ")
        query_list = filter(None, query_list)
        new_query_list = []
        prev_item = ""
        prev_prev_item = ""
        secondary_order = "," + colid + " "
        is_order_expression = False
        insert_next = False

        for item in query_list:
            if prev_item.lower() == "by" and prev_prev_item.lower() == "order":
                is_order_expression = True
            
            if is_order_expression == True:
                if insert_next == True and item!="," and item.find(',')<0: 
                    new_query_list.append(secondary_order)
                    new_query_list.append(item + " ")
                    insert_next=False
                    is_order_expression = False
                elif item!="," and item!="" and item.find(',')<0 :
                    insert_next = True 
                    new_query_list.append(item + " ")
                else :
                    insert_next=False
                    new_query_list.append(item + " ")
                 
            else:
                new_query_list.append(item + " ")
            
          
                                                
            prev_prev_item = prev_item
            prev_item = item
        
        if is_order_expression == True:
            new_query_list.append(secondary_order)

        query_as_string = "".join(new_query_list)
    
    else :
        query_as_string = query 
        query_as_string += " order by " + colid 
    return query_as_string
    
    
def get_parent_workspace(schema):
    """
    Get the parent workspace of a schema
    """
    
    req = urllib2.Request( schema, headers={"Accept" : "application/json", "firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")} )
    response = urllib2.urlopen(req)
    workspace = json.loads(response.read())["parent"]
    response.close()
    return workspace


def get_info(obj):
    """
    Get the parent workspace of a schema
    """
    
    req = urllib2.Request( obj, headers={"Accept" : "application/json", "firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")} )
    response = urllib2.urlopen(req)
    info = json.loads(response.read())
    response.close()
    return info