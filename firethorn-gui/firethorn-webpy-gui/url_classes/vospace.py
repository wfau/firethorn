'''
Created on Sep 20, 2012

@author: stelios
'''
import json
import web
from app import render

class vospace:
    """
    VOSpace class
    Server-side URL class used to handle and serve requests for the VOSPace file management system of the OSA interface
    """
    
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        
        data = web.input(method="GET",mode="", showThumbs="", time="")
        from helper_functions import filemanager
        if data.mode=="":
            return json.dumps({
                              'Code' : 1,
                              'Content' : str(render.vospace())
                              })
        elif data.mode == "download":
            web.header('Content-Type', 'application/x-download')
            response = filemanager.handler(data)
            filename = response ["filename"]
            if filename==None:
                filename = "download"
            web.header('Content-disposition', 'attachment; filename=' + filename)
            return response['file']
        else:
            web.header('Content-Type', 'application/json')
            response = filemanager.handler(data)
            return json.dumps(response)
            
    def POST(self):
        """ 
        POST function 
        
        Handle an HTTP POST request
        """
        
        data = web.input(method="POST",mode="", showThumbs="", time="")
        from helper_functions import filemanager

        if data.mode == "add":
            web.header('Content-Type', 'text/html')
            response = filemanager.handler(data)
            return response
        
