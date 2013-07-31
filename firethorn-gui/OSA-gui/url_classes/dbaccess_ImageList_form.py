'''
Created on Sep 20, 2012

@author: stelios
'''
from app import session
from config import render, SURVEY_DB, community, survey_prefix
from helper_functions import sql_functions
import freeform_sql
import traceback
import web
import survey_globals
from globals import logging

class dbaccess_ImageList_form: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.dbaccess_ImageList_form(survey_globals.db_options, survey_globals.imagelist_programmes , survey_globals.archive_input, survey_prefix)
    
    
    def POST (self):
        """ 
        POST function 
        
        Handle an HTTP POST request
        """
        import imageList
        return_val = ""      
        data = web.input(archive="", community = community, pCount="", showConf="", database = "", programmeID="", rows="100")
        
        log_info = session.get('logged_in')
        
        if log_info == 'False' or log_info == False:
            return 'NOT_LOGGED_IN'
        else:
           
            try:
                newSql, error, select = imageList.ImageListHandling.generate_SQL_from_input(data)
                cols = []
                if error=="":    
                    rows = sql_functions.execute_query(newSql)
                    select = select.replace('M.', '')
                    select = select.replace('m.', '')
                    cols = select.split(',')
                    return_val = freeform_sql.generate_JSON_from_query(freeform_sql.execute_async_imagelist_query(cols,rows),newSql)            
                    return return_val
                else:
                    error = "<div class='error'>" + error + "</div>" 
            except Exception as e:
                logging.exception('Exception caught:')
                error = "<div class='error'> An error occurred while executing your SQL query<div>"
                return error

