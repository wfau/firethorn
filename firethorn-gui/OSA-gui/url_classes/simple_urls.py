'''
Created on Sep 20, 2012

@author: stelios
'''
from config import render, main_page_render, schema_browser_render, survey_prefix,wfau_page_render


class home: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return main_page_render.home(survey_prefix)    


class dboverview: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.dboverview(survey_prefix)
    

class startHere: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.startHere(survey_prefix)
    

class knownIssues: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.knownIssues(survey_prefix)


class theSurveys: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.theSurveys(survey_prefix)


class roe_browser: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.roe_browser(str(schema_browser_render.dbinfo(survey_prefix)), survey_prefix)


class dbaccess: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.dbaccess(survey_prefix)


class sqlcookbook: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.sqlcookbook(survey_prefix)


class qa: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.qa(survey_prefix)


class glossary: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.glossary(survey_prefix)    


    def POST(self):
        """ 
        POST function 
        
        Handle an HTTP POST request
        """
        return render.glossary(survey_prefix)  
    
class releasehistory: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.releasehistory(survey_prefix)


class gallery: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.gallery(survey_prefix)    


class pubs: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.pubs(survey_prefix)  
    
    
class monitor: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.monitor(survey_prefix)  
    
       
class downtime: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.downtime(survey_prefix)  
    
       
class links: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.links(survey_prefix)    
    
      
class contactus: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.contactus(survey_prefix)


class policy: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.policy(survey_prefix)


class flatFiles: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.flatFiles(survey_prefix)


class ppErrBits: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.ppErrBits(survey_prefix)
    

class sql_help: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.sql_help(survey_prefix)
    
    
class communityInfo: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.communityInfo(survey_prefix)


class imageListNotes: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.imageListNotes(survey_prefix)    
    
    
class getImage_help: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.getImage_help(survey_prefix)      
    
    
class getMultiImageNotes_help: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.getMultiImageNotes_help(survey_prefix)      
        
        
class getRegion_help: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.getRegion_help(survey_prefix)      
        
                
class sqlFormNotes:
    
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.sqlFormNotes(survey_prefix)      
        
        
class crossIDNotes_help:
    
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.crossIDNotes_help(survey_prefix)           


class imcopy: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return render.imcopy(survey_prefix)        
    
    
class browsermain:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """ 
        return render.browsermain(survey_prefix)   

    
class F287:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """ 
        return render.F287(survey_prefix)   
    
    
class f287tool:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """ 
        return render.f287tool(survey_prefix)   
    
    
        
class pssa:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """ 
        return render.pssa(survey_prefix)   
    
    
    
class credit:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """ 
        return render.credits(survey_prefix)
    
    
    
class radial_help:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """ 
        return render.radial_help(survey_prefix) 
    
    
    
class defaults:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """ 
        return render.defaults(survey_prefix) 
    
    
class helpdesk:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """ 
        return render.helpdesk(survey_prefix) 
    
class coverage_maps:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """ 
        return render.coverage_maps(survey_prefix) 
    
    
class wfau: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return wfau_page_render.wfau(survey_prefix, wfau_page_render.tooltip_about(),wfau_page_render.tooltip_astrotrop(),wfau_page_render.tooltip_euclid(),wfau_page_render.tooltip_gaia_eso(),wfau_page_render.tooltip_gaia(),wfau_page_render.tooltip_help(), wfau_page_render.tooltip_mywfau(),wfau_page_render.tooltip_news(), wfau_page_render.tooltip_osa(),wfau_page_render.tooltip_other(),wfau_page_render.tooltip_pubs(),wfau_page_render.tooltip_ssa(),wfau_page_render.tooltip_vista(), wfau_page_render.tooltip_vo(), wfau_page_render.tooltip_wfcam())        
        
        
        
class wfau_about: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return wfau_page_render.wfau_default_layout(survey_prefix,wfau_page_render.about())   
    
class wfau_news: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return wfau_page_render.wfau_default_layout(survey_prefix,wfau_page_render.news())  
    
class wfau_pubs: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return wfau_page_render.wfau_default_layout(survey_prefix,wfau_page_render.pubs())  
    
class wfau_other: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return wfau_page_render.wfau_default_layout(survey_prefix,wfau_page_render.other())  
    
class wfau_help: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        return wfau_page_render.wfau_default_layout(survey_prefix,wfau_page_render.help())      