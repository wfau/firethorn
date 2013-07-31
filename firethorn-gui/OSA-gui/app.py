"""
app module

Documentation for the main Atlas webpy app module.

University of Edinburgh, WAFU
@author: Stelios Voutsinas
"""


import sys, os
scos_path = ['', '/etc/apache2/sites-enabled', '/djer/stv/usr/local/wsa/trunk/bin', '/djer/scos/usr/local/wsa/lib', '/djer/scos/usr/local/lib/python2.7', '/djer/scos/usr/local/lib/python2.7/site-packages', '/djer/scos/usr/local/lib', '/djer/scos/usr/local/unixODBC/lib', '/djer/scos/usr/local/lib/python2.7/site-packages/mx', '/djer/scos/usr/local/lib/python2.7/site-packages/mx/ODBC', '/djer/stv/VDFS/trunk/src/curation', '/djer/scos/usr/local/lib/python27.zip', '/djer/scos/usr/local/lib/python2.7/plat-linux3', '/djer/scos/usr/local/lib/python2.7/lib-tk', '/djer/scos/usr/local/lib/python2.7/lib-old', '/djer/scos/usr/local/lib/python2.7/lib-dynload', '/djer/scos/usr/local/lib/python2.7/site-packages/PIL']
abspath = os.path.dirname(__file__)
sys.path.append(abspath)
for i in scos_path:
    sys.path.append(i)
if abspath: # Apache
    os.chdir(abspath)
else: # CherryPy
    abspath = os.getcwd()
os.environ['HOME'] = abspath + "/static/temp"

from config import * #@UnusedWildImport
import socket
import warnings
with warnings.catch_warnings():
    warnings.simplefilter("ignore")
    import atpy
socket.setdefaulttimeout(1200)
from operator import itemgetter, attrgetter
from globals import *


### Render URLs
urls = ( '', 'index', '/', 'index','/index', 'index','/startHere','startHere','/dboverview','dboverview','/knownIssues','knownIssues','/theSurveys','theSurveys','/roe_browser','roe_browser', 
        '/dbaccess','dbaccess','/dbaccess_login', 'dbaccess_login', '/dbaccess_ImageList_form', 'dbaccess_ImageList_form', '/dbaccess_getImage_form', 'dbaccess_getImage_form', '/dbaccess_pixel', 'dbaccess_pixel',
        '/dbaccess_MultiGetImage_form', 'dbaccess_MultiGetImage_form','/dbaccess_region_form', 'dbaccess_region_form','/dbaccess_SQL_form', 'dbaccess_SQL_form', '/dbaccess_voexplorer', 'dbaccess_voexplorer',
        '/dbaccess_crossID_form', 'dbaccess_crossID_form','/dbaccess_radial_form', 'dbaccess_radial_form', '/sqlcookbook', 'sqlcookbook','/qa', 'qa','/glossary' , 'glossary', '/releasehistory','releasehistory',
        '/gallery', 'gallery', '/pubs', 'pubs', '/monitor', 'monitor', '/downtime', 'downtime', '/links', 'links','/contactus', 'contactus','/policy', 'policy', '/flatFiles', 'flatFiles', '/defaults', 'defaults', 
        '/ppErrBits','ppErrBits','/sql_help','sql_help','/communityInfo','communityInfo', '/imageListNotes', 'imageListNotes','/getImage_help', 'getImage_help','/radial_help', 'radial_help',
        '/getMultiImageNotes_help', 'getMultiImageNotes_help', '/getRegion_help', 'getRegion_help', '/sqlFormNotes', 'sqlFormNotes', '/crossIDNotes_help', 'crossIDNotes_help', '/logout', 'logout',
        '/save_as_vot','save_as_vot','/save_as_fits','save_as_fits','/save_as_html','save_as_html','/viewer','viewer','/send_email','send_email', '/data_tables_processing','data_tables_processing',
        '/getImageHandler','getImageHandler', '/imcopy', 'imcopy', '/home', 'home', '/writeHTMLtoFile', 'writeHTMLtoFile', '/gloss/gloss_a', 'gloss_a','/gloss/gloss_b', 'gloss_b','/gloss/gloss_c', 'gloss_c',
        '/gloss/gloss_d', 'gloss_d','/gloss/gloss_e', 'gloss_e','/gloss/gloss_f', 'gloss_f','/gloss/gloss_g', 'gloss_g','/gloss/gloss_h', 'gloss_h','/gloss/gloss_i', 'gloss_i','/gloss/gloss_j', 'gloss_j','/gloss/gloss_k', 'gloss_k',
        '/gloss/gloss_l', 'gloss_l','/gloss/gloss_m', 'gloss_m','/gloss/gloss_n', 'gloss_n','/gloss/gloss_o', 'gloss_o','/gloss/gloss_p', 'gloss_p','/gloss/gloss_q', 'gloss_q','/gloss/gloss_r', 'gloss_r','/gloss/gloss_s', 'gloss_s',
        '/gloss/gloss_t', 'gloss_t','/gloss/gloss_u', 'gloss_u','/gloss/gloss_v', 'gloss_v','/gloss/gloss_w', 'gloss_w','/gloss/gloss_x', 'gloss_x','/gloss/gloss_y', 'gloss_y','/gloss/gloss_z', 'gloss_z',
        '/schema_browser/(.*)', 'schema_browser', '/schema_browser', 'schema_browser', '/vospace', 'vospace', '/browsermain', 'browsermain', '/F287','F287', '/f287tool', 'f287tool','/pssa','pssa', '/credits','credit',
        '/helpdesk', 'helpdesk', '/coverage_maps', 'coverage_maps', '/wfau','wfau', '/wfau/about','wfau_about', '/wfau/help','wfau_help', '/wfau/news','wfau_news', '/wfau/pubs','wfau_pubs', '/wfau/other','wfau_other')

### For apache production env
app = web.application(urls, globals())


### Session information
if web.config.get('_session') is None:
    session = web.session.Session(app, web.session.DiskStore(os.path.join(abspath,'sessions')),initializer={'logged_in': 'False'})
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

if live == True:
    sys.stdout = MyOutputStream()

application = app.wsgifunc()


from helper_functions import *
from url_classes import *


if __name__ == "__main__": app.run()



