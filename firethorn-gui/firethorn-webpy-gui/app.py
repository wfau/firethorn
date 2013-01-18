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
urls = ('/', 'index','/index', 'index', '/services', 'services', '/create_new',
         'create_new', '/create_view','create_view', '/create_view_edit_handler', 
         'create_view_edit_handler', '/jdbc_resources','jdbc_resources', '/vospace', 'vospace',
         '/workspace', 'workspace', '/workspace_actions','workspace_actions', '/resource_actions','resource_actions',
         '/data_tables_processing','data_tables_processing')


### For apache production env
app = web.application(urls, globals())
application = app.wsgifunc()
      
if web.config.get('_session') is None:
    session = web.session.Session(app, web.session.DiskStore(os.path.join(base_location,'sessions')),initializer={'logged_in': 'True','user':'stelios', 'role' :'admin',
                                                                                                                  
              'workspaces': [{'ident' : 'workspace1','name' : 'workspace1','created': '2012-11-01T09:18:29.872', 'modified': '2012-11-01T11:18:29.873', 'type': 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json'},
                           {'ident' : 'workspace2', 'name' : 'workspace2','created': '2012-11-01T09:18:29.872', 'modified': '2012-11-01T11:18:29.873', 'type': 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json'}, 
                           {'ident' : 'workspace3','name' : 'workspace3','created': '2012-11-01T09:18:29.872', 'modified': '2012-11-01T11:18:29.873', 'type': 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json'}],
              'workspace1' : [{'ident' : 'container1','name' : 'container1','created': '2012-11-01T09:18:29.872', 'modified': '2012-11-01T11:18:29.873', 'type': 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json',
                                 'children': [{'ident' : 'Dec', 'name' : 'dec','created': '2012-11-01T09:18:29.872', 'modified': '2012-11-01T11:18:29.873', 'type': 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json'},
                                              {'ident' : 'filterID', 'name' : 'filterID','created': '2012-11-01T09:18:29.872', 'modified': '2012-11-01T11:18:29.873', 'type': 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json'}]},
                              {'ident' : 'Ra', 'name' : 'ra','created': '2012-11-01T09:18:29.872', 'modified': '2012-11-01T11:18:29.873', 'type': 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json'}],
              'workspace2' : [{'ident' : 'Source','name' : 'source','created': '2012-11-01T09:18:29.872', 'modified': '2012-11-01T11:18:29.873', 'type': 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json'}],
              'workspace3' : []})
    
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

from helper_functions import type_helpers
from config import types
### Render templates
render = web.template.render('templates/', globals={'context': session, 'type_helpers' : type_helpers, 'types' : types})  

from url_classes import *

        
if __name__ == "__main__": app.run()



