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


region_query_180_to_1 = """
          Query: 180 - 1
            <br /><br /><br /><br />
            SELECT  sourceID, framesetID, RA, Dec, mergedClass, priOrSec, YAperMag3,
YAperMag3Err, J_1AperMag3, J_1AperMag3Err, J_2AperMag3, J_2AperMag3Err,
HAperMag3, HAperMag3Err, KAperMag3,
KAperMag3Err,2*DEGREES(ASIN(sqrt(power(-0.9998476951563913-cx,2)+power(1.2244602795081332E-16-cy,2)+power(0.01745240643728351-cz,2))/2))*60
as distance  FROM lasSource WHERE dec > 0.9833333333333333 and dec <
1.0166666666666668 and RA >= 179.98333070918963 and RA <=
180.01666929081037 and ((cx * -0.9998476951563913 + cy *
1.2244602795081332E-16 + cz * 0.01745240643728351 ) >= 0.9999999576920253)"""
