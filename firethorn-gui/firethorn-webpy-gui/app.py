#!/usr/bin/env python

"""
app module

Documentation for the main firethorn-webpy-gui app module.

University of Edinburgh, WAFU
@author: Stelios Voutsinas
"""

__author__ = [
    "Stelios Voutsinas <steliosvoutsinas@yahoo.com>"
]


import web
import socket
socket.setdefaulttimeout(1200)
from config import base_location
abspath = base_location
import os

### Set to false when launching
web.config.debug = False 

### Render URLs
urls = ('/', 'index','/index', 'index', '/services', 'services', '/create_new', 'create_new', '/create_view', 'create_view')


### For apache production env
app = web.application(urls, globals())
application = app.wsgifunc()
      
if web.config.get('_session') is None:
    session = web.session.Session(app, web.session.DiskStore(os.path.join(base_location,'sessions')),initializer={'logged_in': 'True','user':'stelios', 'role' :'admin'})
    web.config._session = session
else:
    session = web.config._session

web.config.session_parameters['timeout'] = (60 * 60 * 5)


def session_hook():
    """
    Session hook function, used to store a global session variable
    """
    web.ctx.session = session
    web.template.Template.globals['session'] = session
    
app.add_processor(web.loadhook(session_hook))  

### Render templates
render = web.template.render('templates/', globals={'context': session})  

from url_classes import *

        
if __name__ == "__main__": app.run()



