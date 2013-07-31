'''
Created on Sep 20, 2012

@author: stelios
'''
from app import session
from config import render, base_host, survey_prefix, survey_short
from helper_functions import sql_functions
import web
from globals import logging

class dbaccess_login: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        log_info = session.get('logged_in')
        
        if log_info == "True":
            return render.dbaccess_login("True", session.get("username","user"), survey_prefix)
        else :
            return render.dbaccess_login("False", "user", survey_prefix)

    def POST (self):
        """
        Check input parameters to see if user information is correct
        If yes: Return True and user login session information
           no: Return False
        """
        data = web.input(archive="",community="",username="",password="",community_input="")
        verified = []
        
        try:
            verified = sql_functions.verify_user(data.username, data.password, data.community_input, survey_short.lower())
        except Exception:
            logging.exception('Exception caught:')     
        
        if verified!=[]:
            session.logged_in = "True"
            session.username = data.username
            session.community_input = data.community_input
            session.email = verified[2]
            session.programmeID = verified[3]
            session.log_html_content = "<br>Logged in as: <a>" + data.username + "</a><br/>  Community: <a>" + data.community_input + "</a><br/><a href='" + survey_prefix + "/logout'>Logout</a><br><br>"
            return True
        else:
            return False
    
    


class logout:
    
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        session.kill()
        raise web.seeother('http://' +  base_host + survey_prefix)
    