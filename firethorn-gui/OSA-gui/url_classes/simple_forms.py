'''
Created on Sep 20, 2012

@author: stelios
'''

from config import render, SURVEY_DB, survey_prefix
import survey_globals
import web
from app import session
        
        
class dbaccess_getImage_form: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.dbaccess_getImage_form(survey_globals.db_options,survey_globals.programmes,survey_globals.archive_input, survey_prefix)


class dbaccess_MultiGetImage_form: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        
      
        return render.dbaccess_MultiGetImage_form(survey_globals.db_options, survey_globals.programmes,survey_globals.multigetimage_file_name,survey_globals.multigetimage_archive,survey_globals.multigetimage_bands,survey_prefix )

        
class dbaccess_crossID_form: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.dbaccess_crossID_form(survey_globals.crossID_db_options, survey_globals.crossID_baseTable_options, survey_globals.crossID_programmes ,survey_globals.crossID_select,survey_globals.crossID_where,survey_globals.crossID_file_name, survey_globals.crossID_archive, survey_globals.crossID_nearest, survey_globals.crossID_table, survey_prefix)



class dbaccess_radial_form: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.dbaccess_radial_form(survey_globals.db_options, survey_prefix)
    
    
class dbaccess_pixel: 

    def POST(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        data = web.input(ra="", dec="", radius=1, sys="J")
        return render.dbaccess_pixel(data.ra,data.dec,data.radius,data.sys, survey_prefix)




class dbaccess_voexplorer:
 
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """ 
      
        
        web.header("Content-Type", "text/html")
        import freeform_sql.firethorn_helpers as firethorn_helpers
        import logging
        from config import  (host_temp_directory,
                             firethorn_base, userhomedir, SURVEY_DB)
        from freeform_sql.freeform_config import add_catalogue_html, remove_catalogue_html
        
        try:
            atlasworkspace = firethorn_base + dict(line.strip().split('=') for line in open(userhomedir  + '/firethorn.testing'))['adqlspace']
            atlasschema = firethorn_base + dict(line.strip().split('=') for line in open(userhomedir  + '/firethorn.testing'))['atlasschema']
            selected_cached_endpoints = firethorn_helpers.generate_selected_list(SURVEY_DB, SURVEY_DB, atlasschema,remove_catalogue_html,session)
            unselected_cached_endpoints = firethorn_helpers.generate_available_list_from_workspace(atlasworkspace, add_catalogue_html, atlasschema,session )
            starting_catalogue_id = firethorn_helpers.create_initial_workspace(SURVEY_DB, SURVEY_DB, atlasschema,session)
        
        except Exception as e:
            atlasworkspace=""
            atlasschema=""
            selected_cached_endpoints = ""
            unselected_cached_endpoints = ""
            logging.exception("Error getting during firethorn initialization")
            
        ### Query form object
        voexplorer_form = web.form.Form(web.form.Textarea('textfield',web.form.notnull,row=200,cols=30, class_='textfield', id='textfield',description=''),web.form.Hidden(name='tap_endpoint',id='tap_endpoint',value=""))
         
        
        form = voexplorer_form()
        return render.dbaccess_VOExplorer_form(form, "", "", "voexplorer", survey_prefix)
       


