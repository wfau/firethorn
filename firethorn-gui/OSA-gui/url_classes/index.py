'''
Created on Sep 20, 2012

@author: stelios
'''
from app import session
from config import debug_mode, main_page_render, render, survey_sub_path, no_users, survey_full, survey_prefix
from survey_globals import programmeID
import web

    
class index:
    """
    Main index class, handles requests to the main page of the application.
    
    Accepts GET and POST requests. 
    Different parameters passed to the HTTP POST/GET functions determine the appropriate actions
    """
    
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request, by rendering the index template and returning the HTML content
        """
   
        web.header('Content-Type', 'text/html')
        
        if no_users == True:
            session.log_html_content = ""
            session.logged_in = "True"
        elif debug_mode == True:
            session.logged_in = "True"
            session.username = 'User'
            session.community_input = 'prerelease'
            session.email = 'test@test.com'
            session.programmeID = programmeID
            session.log_html_content = "<br>Logged in as: <a>" + session.get('username', "") + "</a><br />  Community: <a>" + session.get('community_input', "") + "</a><br /><a href='" + survey_sub_path + "logout'>Logout</a><br><br>"
        
        try:
            
            if session.logged_in != "True" and no_users !=True:
                session.log_html_content = "<div class='login navigation'><a id='dbaccess_login' href='#dbaccess_login_div'>Login</a></div>Not currently logged in"
            elif session.get('username', "")=="":
                session.log_html_content = "<div class='login navigation'><a id='dbaccess_login' href='#dbaccess_login_div'>Login</a></div>Not currently logged in"
                session.logged_in = "False"
                session.kill()
            else : 
                session.log_html_content = "<br>Logged in as: <a>" + session.get('username', "") + "</a><br />  Community: <a>" + session.get('community_input', "") + "</a><br /><a href='" + survey_sub_path + "logout'>Logout</a><br><br>"

        except Exception as e:
                session.log_html_content = "<div class='login navigation'><a id='dbaccess_login' href='#dbaccess_login_div'>Login</a></div>Not currently logged in"
                session.logged_in = "False"
                session.kill()

   
        return render.index(session.log_html_content, str(main_page_render.home(survey_prefix)), str(main_page_render.menu(survey_prefix)), str(main_page_render.footer(survey_prefix)), survey_full, survey_prefix)
    

    def POST(self):
        """
        POST function 
        
        Handle an HTTP POST request
        """

        web.header('Content-Type', 'text/html')
        session.log_html_content = ""
        session.logged_in = "True"

        return render.index(session.log_html_content, str(main_page_render.home(survey_prefix)), str(main_page_render.menu(survey_prefix)), str(main_page_render.footer(survey_prefix)), survey_full, survey_prefix)
        
        