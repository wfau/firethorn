'''
config module

Created on Nov 30, 2011
Configuration and Properties Variables for the FreeForm SQL processing page

@author: stelios
'''
import web
import os

### Configurations
use_config = 'singletap'
mode_global = 'async'
request = 'doQuery'
result_format = 'votable'
lang = 'ADQL'



base_location = os.getcwd()
cur_static_dir = base_location + '/static/static_vo_tool/'
#osa_sub_path = "osa/"
osa_sub_path = ""

registry_url = 'http://registry.astrogrid.org/astrogrid-registry/main/xqueryresults.jsp'
DR2TAPurl = 'http://wfaudata.roe.ac.uk/ukidssWorld-dsa/TAP/'
tap_factory="http://admire3.epcc.ed.ac.uk:8081/TAPFactory/create"
ukidss_db = 'UKIDSSDR6PLUS'
atlas_cgi_bin = 'http://surveys.roe.ac.uk/wsa/cgi-bin/'

### globals
MAX_FILE_SIZE = 204857600 #use : 104857600
delay = 3
use_cached_endpoints = 0
ERROR_HTML = "<div style=\"color:red;margin-left:50px;\">No elements returned. Please check your query</div>"

