'''
Created on Sep 20, 2012

@author: stelios
'''
from app import session
from config import mode_global, from_email, getImageURL, multiGetImageURL, \
    crossIDURL, radialURL, survey_prefix,survey_short, no_users, publicly_visible_temp_dir
import StringIO
import freeform_sql
import json
import traceback
import urllib
import urllib2
import web
from globals import logging
from url_classes.data_tables_processing import data_tables_processing


class send_email:
    """ 
    Class is used as an additional URL, which is called to send an email with the results of an ADQL/TAP query
    Expects input with the email, query, and TAP endpoint
    Uses Python's sendmail function

    """
     
    def GET(self):
        """
        GET function
        
        Raise error
        """
        raise web.seeother(survey_prefix)


    def POST (self):
        """ 
        POST function
        
        Main Class functionality    
        """
        from data_tables_processing import data_tables_processing
        from freeform_sql.misc import html_functions
        data = web.input(email="",query="",generated_tap="")
        if data.generated_tap !="" and data.email !="" and data.query !="":
            try:
                jobId = ""
                (votable,jobId,pathname) = freeform_sql.execute_async_query(data.generated_tap,mode_global,data.query) #@UnusedVariable
                if votable =="" or pathname=="":                
                    html = "<div style=\"color:red;margin-left:50px;\">Your query request was not executed. Please check your query</div>"    
                else: 
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
                    lst = [cols,lst] 
                    dtp = data_tables_processing()
                    return_val = freeform_sql.write_format_from_json('vo',lst )
                    
                    votable_url = data_tables_processing.save_as_temp_file(dtp, return_val)
                    
                    html = "The results of your query can be found at: " + votable_url
                web.utils.sendmail(from_email, data.email, 'Query Results', html, headers=({ 'Content-Type': 'text/html'})) #,attachments=[dict(filename="bug.xml", content=safestr(html_table))])
            except Exception as ex:
                logging.exception('Exception caught:')
            return ""   
        else:
            return ""        




class getImageHandler:
    """
    Proxy URL class that takes requests for a given Image URL, fetches the image or other content from that URL and returns it
    """
    def POST(self):
        """ 
        POST function
        
        Main Class functionality    
        """
        from helper_functions import MultipartPostHandler
        web.header('Content-Type', 'text/html')
        data = web.input(href="", params="", action="", uploadFile="", file1="")
        return_str = ''
        f = ''
        temp_URL = ''
        url_to_read = ''
        log_info = session.get('logged_in')
        
        
        if (log_info == 'False' or log_info == False) and no_users != True:
            return 'NOT_LOGGED_IN'
        else: 
            if data.href!="":
                url_to_read = data.href 
                if data.params!="":
                    url_to_read = url_to_read + '?' + data.params
                if data.action=="crossID_read" or data.action=="osa_request" :
                    url_to_read += "&passThrough=atlas737"
            else:
                if survey_short.lower() == "osa":
                    data.params += "&passThrough=atlas737"
        
                if data.action == "getImage" and data.params!="":
                    temp_URL = getImageURL
                    url_to_read = temp_URL + '?' + data.params 
                elif data.action == "multiGetImage":
                    url_to_read = multiGetImageURL 
                elif data.action == "crossID":
                    url_to_read = crossIDURL
                    
                elif data.action == "radial":
                    url_to_read = radialURL 
                    if data.params!="":
                        url_to_read = url_to_read + '?' + data.params 
                  
            try:
                if data.params!="" and data.action == "getImage" and url_to_read!="":
                    f = urllib.urlopen(url_to_read)
                    return_str = f.read()
                    f.close()
                elif data.action == "radial":
                    encoded_args = urllib.urlencode(data)
                    request = urllib2.Request(url_to_read, encoded_args)
                    f = urllib2.urlopen(request)
                    return_str = f.read()
                    f.close()
                                    
                elif data.action == "multiGetImage" or data.action == "crossID":
                    form = MultipartPostHandler.MultiPartForm()
                    for i in data:
                        if i!='href' and i!='params' and i!='action' and i!='file1' and  i!='uploadFile': 
                            form.add_field(i, data[i])
                    
                    if survey_short.lower() == "osa":
                        form.add_field("passThrough", "atlas737")
                    
                    if data.file1!="":
                        form.add_file('file1', 'file1',fileHandle=StringIO.StringIO(data.file1))
                    
                    if data.uploadFile!="":
                        form.add_file('uploadFile', 'uploadFile',fileHandle=StringIO.StringIO(data.uploadFile))
                    
                 
                    # Build the request
                    request = urllib2.Request(url_to_read)
                    body = str(form)
                    request.add_header('Content-type', form.get_content_type())
                    request.add_header('Content-length', len(body))
                    request.add_data(body)
                   
                    f = urllib2.urlopen(request)
                    return_str = f.read()
                    f.close()
                                    
                elif data.href !="":
                    f = urllib.urlopen(url_to_read)
                    return_str = f.read()
                    f.close()
                    
            except Exception as e:
                if f!='':
                    f.close()
                logging.exception('Exception caught:')
                return_str =  ""      
            return return_str



class writeHTMLtoFile :
    """
    URL class used to write query data to files
    
    """
    
    def is_number(self, s):
        """
        Check if supplied value is a valid number
        """
        
        try:
            float(s)
            return True
        except Exception as e:
            return False

    def POST(self):  
        """
        Handle HTTP POST request
        """
        
        from freeform_sql import File_handler
        from bs4 import BeautifulSoup
        
        data = web.input(html_table='')
        cols = []
        columns = []
        table_rows = []
        
        try :
            html_table = data.html_table
            html_table = BeautifulSoup(html_table)
       
            temp_cols = html_table.findAll('th')
            for i in temp_cols:
                columns.append(i.string)
           
            rows = html_table.findAll('tr')
            for tr in rows:
                cols = tr.findAll('td')
                tmp_lst = []
                counter = 0
                td_content =''                
                for td in cols:
                
                    if counter==0:
                        td_content = str(td.contents[0])
                    elif self.is_number(td.contents[0]):
                        if str(td.contents[0]).find('.')>0:
                            td_content = float(td.contents[0])
                        else :
                            td_content = int(td.contents[0])
                    else :
                        td_content = str(td.contents[0])
                        #td_content = td.contents[0]

                    tmp_lst.append(td_content)
                    

                    counter = counter+1
                if tmp_lst!=[]:
                    table_rows.append(tuple(tmp_lst))
       
            if html_table!='':
                cols = columns
                lst = []
                
              
                for x in table_rows:
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
                return file_path
        except Exception as e:
            print "A network connection error occurred"  
            logging.exception('Exception caught:')


