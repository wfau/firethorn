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
import traceback
from config import * #@UnusedWildImport
from file_handler import File_handler


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
                os.unlink(temp_file)
    except Exception as e:
        print e
       
       
       


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
    except IOError:
        if f!='':
            f.close()
        print "A network connection error occurred"
 
 



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

  
    form_data=[]
    title_results = []
    tap_url_results = []
    f=''
    try:
        # get title elements
        params = urllib.urlencode({'performquery' : 'true', 'ResourceXQuery' : title_query}) 
        req = urllib2.Request(registry, params)
        f = urllib2.urlopen(req)
        result_string = f.read()
        result_string = result_string.decode('latin-1')
        dom = xml.dom.minidom.parseString(result_string)
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
            form_data.append(web.form.Checkbox(xml_parser.getText(title_results[counter]), value =xml_parser.getText(i)))
            counter = counter +1
            
        f.close()
        f2.close()
    
    except Exception as e:
        if f!='':
            f.close()
        print e
        return ""
    if form_data==[]:
        return ""
    else:
        return tuple(form_data)






def generate_endpoint_from_list(endpnt_list, prefixes):
    """
    Takes an list of TAP endpoint URLs separated by a double space, and generates a merged TAP service from the OGSDA-DAI TAP factory(global tap_factory variable)
           
    @param endpnt_lits: A list of available TAP endpoints as strings
    @param prefixes: A list of prefixes as a json object, from which we extract URL & name and submit to the Registry
    @return: A string with the new URL to be used by the client
    """
    
    final_lst = ""
    result=""
    urls = endpnt_list.split('  ')
    response = ''
    counter = 0
    
    if len(urls)==1:
        result = urls[0]+'/'
    else:     
        try:
            prfxs = json.loads(prefixes)
            for pre in prfxs:
                url = urls[counter]
                name = pre['name']
                prefix = pre['value']
                if prefix == "" or prefix == "undefined":
                    prefix = name
                metadata_url = ""
             
                if url.rfind('heidelberg')>0:
                    metadata_url = 'http://dc.zah.uni-heidelberg.de/__system__/tap/run/tableMetadata'
                elif url.rfind('voparis-srv.obspm')>0:
                    metadata_url =  url + "/VOSI/tables"
                else:
                    metadata_url = url[0:max(url.rfind('/TAP'),url.rfind('/tap'),url.rfind('/Tap'),0)]+'/VOSI/tables'
                final_lst+= prefix +"="+ url+'/sync '+ metadata_url +" \n"    
                counter = counter+1
                
            headers = {"Content-type": "text/plain"}
            req = urllib2.Request(tap_factory, final_lst, headers)
            response = urllib2.urlopen(req)
            result = response.read()+'/TAP/' 
            response.close()
        except Exception:
            if response!='':
                response.close()
            result = "" 
            print "Could not generate the TAP endpoint from the TAP factory. IOError"  
    return result






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
    except Exception:
        if f!='':
            f.close()
        return_str =  ""      
    return return_str




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
    except IOError:
        if f!='':
            f.close()
        print "A network connection error occurred"
    return res





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
        print e
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
        

    result = '<div id="save_as"><form name="save_as_html" action="' + osa_sub_path + 'save_as_html" method="post"> <input type="hidden" name="pathname" value ="' 
    result += pathname + '"/><input type="submit" class ="save_button" value="Save as HTML" /></form></div>'
    return result
    
    




def print_save_VOTable_info(pathname):    
    """
    Generates HTML content for VOTable save button    

    @param vot: A table with the results of a query as a JSON, escaped array
    @return: The HTML content with a save as button
    """

    result = '<div id="save_as"><form name="save_as_vot" action="' + osa_sub_path + 'save_as_vot" method="post"> <input type="hidden" name="pathname" value ="' 
    result += pathname + '"/><input type="submit" class ="save_button" value="Save as Votable" /></form></div>'
    return result





def print_save_Fits_info(pathname):    
    """
    Generates HTML content for VOTable save button    

    @param vot: A table with the results of a query as a JSON, escaped array
    @return: The HTML content with a save as button
    """
    
    result = '<div id="save_as"><form name="save_as_fits" action="' + osa_sub_path + 'save_as_fits" method="post"> <input type="hidden" name="pathname" value ="' 
    result += pathname + '"/><input type="submit" class ="save_button" value="Save as Fits" /></form></div>'
    return result




def execute_async_query(url,mode_local,q):
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
            
            lst = []
           
            for x in votable.data.tolist():
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
    except IOError as e:
        print "A network connection error occurred"  
        print e
    return (votable,jobId,file_path)




    
    

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
  
    if votable == "MAX_ERROR":
        data_results = votable
        return data_results
    elif votable!='' and votable!= None:
        if len(votable)>0:
            cols = list(votable.columns)
            row_list = votable.data.tolist() 
            if tap_endpoint!="" and query!="":
                content = '<form class="launch_viewer" action="' + osa_sub_path + 'viewer" method="post" target="_blank"><input type="hidden" name="query" value="'+ query +'"/><input type="hidden" name="cols" value="'+ html_functions.escape(json.dumps(cols)) +'"/><input type="hidden" name="filepath" id="temp_file" value="'+ pathname +'"/><input type="hidden" name="tap_endpoint" value="'+ tap_endpoint +'"/>'
                if table_mode == 'interactive':
                    data_results = html_functions.escape_list(row_list)
                    json_list = html_functions.escape(json.dumps(data_results))
                    content += '<input type="hidden" id="hidden_json_results" name="results" value="' + json_list  + '" />'
                content += '<div style="clear:both;text-align:left;margin-left:20px;font-size:13px;color:#C2BEAD">Launch in Viewer<input type="submit" value="" /></form><br /><br /></div>'
                if jobId != "" and jobId !=None:
                    content += '<div style="clear:both;text-align:left;margin-left:20px;font-size:13px;color:#C2BEAD">Query Metadata URL <a id="toggle_query_info">[+]</a><div id="toggle_query_info_div" style="display:none"> <a href="'+jobId+'">'+ jobId + '</a>' + '</div></div><br>'
            else:
                content = '<input type="hidden" name="filepath" id="temp_file" value="'+ pathname +'"/>'
            content += '<div id="save_as_information" style="display:none">' + print_save_VOTable_info(pathname) + print_save_HTML_table_info(pathname) + print_save_Fits_info(pathname) + '</div>'
            content += '<div style="text-align:left;margin-left:15px;clear:both"><img id ="samp_connection" src="static/images/red-button.png" height="17" width="17" /><button id="register">Connect to SAMP</button><button id="unregister" style="display: none;">Disconnect from SAMP</button><button id="loadvotable" style="display: none;">Broadcast results table</button><br /><br /></div><br />'

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
            framesetID_index = col_data_to_list.index('frameSetID')
            db = ukidss_db
           
            temp_row=[]
            for i in votable.data.tolist():
                getFSLink = '<a target="getI" href="http://surveys.roe.ac.uk:8080/wsa/GetImage?mode=show&ra=' + str(i[ra_index])  + '&dec=' + str(i[dec_index]) + '&fsid=' + str(i[framesetID_index]) + '&database=' + db +'">view</a>'
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
        print "A network connection error occurred"  
        print e
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
                
                view = '<a id="OImageList_facebox" target="display" href="' + atlas_cgi_bin + 'display.cgi?file=' + str(i[filename]) + '&cat=' + str(i[catname]) + '&comp=' + str(i[jpegBase]) + '&noExt=' + str(i[ext]) + '&MFID=' + str(i[mfid]) + '&rID=' + str(rID) +'">view</a>'
                img = '<a  href="' + atlas_cgi_bin + 'fits_download.cgi?file=' + str(i[filename]) +'&MFID=' + str(i[mfid]) + '&rID=' + str(rID) +'">FITS</a>'
                cat = '<a  href="' + atlas_cgi_bin + 'fits_download.cgi?file=' + str(i[catname]) + '&amp;MFID=' + str(i[mfid]) + '&amp;rID=' + str(rID) + '">FITS</a>'
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
        print traceback.print_exc()
        print e
    return (votable,"",file_path)
   

 
def write_format_from_json(to_format,json_list):
    """
    Generates HTML to_format for a json array of data
    
    @param to_format: The format to which to transform a JSON list of data
    @param json_list: The source json list
    """
    
    html_array = json_list
    fields = html_array[0]
    data = html_array[1]
    t = atpy.Table()
    
    if to_format == 'csv':
   
        csv = ''
        counter = 0 
        for val in data[0]:
            counter = counter + 1
            if counter == len(data[0]):
                csv += str(val)
            else:
                csv += str(val) + ','
        csv += '\n'
        
        for row in data:
            values = []
            for x in row.values():
                values.append(str(x))
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
        return query_as_string