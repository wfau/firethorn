'''
Created on Sep 20, 2012

@author: stelios
'''

from config import schema_browser_render,survey_prefix
import web
       
 
class schema_browser:
    pre =  "<div id='schema_wrapper'>"
    end_pre =  "</div>"
    
    def GET(self, name):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        
        if not name:
            return "Incorrect schema file requested"
        else :
            val = name
           
            if survey_prefix in name and survey_prefix!="":
                
                val = name.split(".html",1)[0].split(survey_prefix + '/',1)[1]
            else:
                val = name.split(".html",1)[0]
     
        return self.pre  + str(getattr(schema_browser_render, val)(survey_prefix)) + self.end_pre
                  
    
    def POST(self):
        """ 
        POST function 
        
        Handle an HTTP POST request
        """
        
        data = web.input(href="")
        val = data.href
        if survey_prefix in data.href and survey_prefix!="": 
            val = data.href.split(".html",1)[0].split(survey_prefix + '/',1 )[1]
        else :
            val = data.href.split(".html",1)[0]
 
   
        return self.pre +  str(getattr(schema_browser_render, val)(survey_prefix)) + self.end_pre

       
    
           
    