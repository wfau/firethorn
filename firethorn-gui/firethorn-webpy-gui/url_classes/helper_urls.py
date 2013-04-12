'''
Created on Sep 20, 2012

@author: stelios
'''
from app import session
from config import mode_global, from_email
import StringIO
import freeform_sql
import json
import traceback
import urllib
import urllib2
import web

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
        raise web.seeother('/')


    def POST (self):
        """ 
        POST function
        
        Main Class functionality    
        """
        
        data = web.input(email="",query="",generated_tap="")
        if data.generated_tap !="" and data.email !="" and data.query !="":
            try:
                jobId = ""
                (votable,jobId,pathname) = freeform_sql.execute_async_query(data.generated_tap,mode_global,data.query) #@UnusedVariable
                if jobId =="":                
                    html = "<div style=\"color:red;margin-left:50px;\">Your ADQL request was not executed. Please check your query</div>"    
                else:
                    html = "The results of your query can be found at: " + jobId
                web.utils.sendmail(from_email, data.email, 'TAP Results', html, headers=({ 'Content-Type': 'text/html'})) #,attachments=[dict(filename="bug.xml", content=safestr(html_table))])
            except Exception as ex:
                traceback.print_exc()
            return ""   
        else:
            return ""        



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
            traceback.print_exc()        



