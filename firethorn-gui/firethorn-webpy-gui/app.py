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


### Set to false when launching
web.config.debug = False 

### Render URLs
urls = ('/', 'index','/index', 'index', '/services', 'services')


### For apache production env
app = web.application(urls, globals())
application = app.wsgifunc()
        
  
from url_classes import *

        
if __name__ == "__main__": app.run()



