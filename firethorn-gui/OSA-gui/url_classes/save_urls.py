'''
Created on Sep 20, 2012

@author: stelios
'''
import freeform_sql
import json
import web
import traceback
from config import survey_prefix
from globals import logging

class save_as_vot:
    """
    This class is called as a URL request, when a user wishes to save the results of a query as VOTable format

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

        web.header('Content-Type', 'application/x-votable+xml')
        data = web.input(pathname="")
        return_val = "No results returned"
       
        f = ""
        try:
            if data.sData!="" :   
                temp_data_array = data.sData.replace('\r','').split('\n')
                data_array = []
                if  len(temp_data_array)>1:
                    counter = 0
                    json_lst = [] 
                    for i in temp_data_array:
                        value_lst = {}
                        temp_lst = []    
                        temp_lst = i.split('\t')
                          
                        if counter ==0:
                            fields = temp_lst
                            data_array.append( fields )
                        elif counter>0:
                            secondary_counter = 0
                            for val in temp_lst:
                                value_lst[fields[secondary_counter]] = val
                                secondary_counter = secondary_counter + 1
                            json_lst.append(value_lst)
                        counter = counter + 1
                    data_array.append(json_lst) 
                    return_val = freeform_sql.write_format_from_json('vo',data_array) 
                    
            elif data.pathname!="":
                f = open(data.pathname, 'r')
                strng = f.read()                
                return_val = freeform_sql.write_format_from_json('vo',json.loads(strng))
                f.close()
            else:
                return_val = "No results returned"
            
        except Exception as e:
            if f!="":
                f.close()
            logging.exception('Exception caught:')
            return_val = "No results returned"

        return return_val    



class save_as_html:
    """ 
    This class is called as a URL request, when a user wishes to save the results of a query as HTML format

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
        
        web.header('Content-Type', 'multipart/form-data')
        data = web.input(pathname="",sData="")    
        return_val = "No results returned"
        f = ""
        try:
            if data.sData!="" :   
                temp_data_array = data.sData.replace('\r','').split('\n')
                data_array = []
                if  len(temp_data_array)>1:
                    counter = 0
                    json_lst = [] 
                    for i in temp_data_array:
                        value_lst = {}
                        temp_lst = []    
                        temp_lst = i.split('\t')
                          
                        if counter ==0:
                            fields = temp_lst
                            data_array.append( fields )
                        elif counter>0:
                            secondary_counter = 0
                            for val in temp_lst:
                                value_lst[fields[secondary_counter]] = val
                                secondary_counter = secondary_counter + 1
                            json_lst.append(value_lst)
                        counter = counter + 1
                    data_array.append(json_lst) 
                    return_val = freeform_sql.write_format_from_json('html',data_array) 
                    
            elif data.pathname!="":
                f = open(data.pathname, 'r')
                strng = f.read()                
                return_val = freeform_sql.write_format_from_json('html',json.loads(strng))
                f.close()
            else:
                return_val = "No results returned"
            
        except Exception as e:
            if f!="":
                f.close()
            logging.exception('Exception caught:')
            return_val = "No results returned"

        return return_val    




class save_as_fits:
    """
    This class is called as a URL request, when a user wishes to save the results of a query as VOTable format

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

        web.header('Content-Type', 'application/fits')
        data = web.input(pathname="")
        return_val = "No results returned"
        f = ""
        try:
            if data.sData!="" :   
                temp_data_array = data.sData.replace('\r','').split('\n')
                data_array = []
                if  len(temp_data_array)>1:
                    counter = 0
                    json_lst = [] 
                    for i in temp_data_array:
                        value_lst = {}
                        temp_lst = []    
                        temp_lst = i.split('\t')
                          
                        if counter ==0:
                            fields = temp_lst
                            data_array.append( fields )
                        elif counter>0:
                            secondary_counter = 0
                            for val in temp_lst:
                                value_lst[fields[secondary_counter]] = val
                                secondary_counter = secondary_counter + 1
                            json_lst.append(value_lst)
                        counter = counter + 1
                    data_array.append(json_lst) 
                    return_val = freeform_sql.write_format_from_json('fits',data_array) 
                    
            elif data.pathname!="":
                f = open(data.pathname, 'r')
                strng = f.read()                
                return_val = freeform_sql.write_format_from_json('fits',json.loads(strng))
                f.close()
            else:
                return_val = "No results returned"
            
        except Exception as e:
            if f!="":
                f.close()
            logging.exception('Exception caught:')
            return_val = "No results returned"

        return return_val    


