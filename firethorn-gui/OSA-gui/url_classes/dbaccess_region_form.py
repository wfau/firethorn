'''
Created on Sep 20, 2012

@author: stelios
'''
from app import session
from config import render, SURVEY_TAP, mode_global, SURVEY_TAP_TITLE, survey_prefix, no_users,db_to_tap_map
import freeform_sql
import survey_globals
import web
import traceback
from globals import logging

class dbaccess_region_form: 

    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
       
        return render.dbaccess_region_form(survey_globals.region_db_options, survey_globals.programmes, survey_globals.region_table_options, survey_globals.region_table_input, survey_globals.archive_input,survey_globals.region_sys,survey_globals.region_select_clause, survey_globals.region_where_clause, survey_prefix)


    def POST(self):
        """ 
        POST function 
        
        Handle an HTTP POST request
        """     
        
        import region_form
        import math
        import coords
        error = False
        data = web.input(programmeID=survey_globals.programmeID)
        
        log_info = session.get('logged_in')
 
        if log_info == 'False' or log_info == False and no_users != True:
            return 'NOT_LOGGED_IN'
        else: 
            from_table = getattr(data,survey_globals.region_table_input)
            database = db_to_tap_map[data.database]
            programmeID = data.programmeID
            co_sys = survey_globals.coord_systems[getattr(data,survey_globals.region_sys)]
            radius = data.radius
            formaction = data.formaction
            dec = data.dec
            ra = data.ra
            galactic_long = data.galactic_long
            galactic_lat = data.galactic_lat
            where_clause = getattr(data,survey_globals.region_where_clause)
            archive = data.archive
            select_clause = getattr(data,survey_globals.region_select_clause)
            
            x_val = 0.0
            y_val = 0.0    
            system = "celestial"
            wc = ""
            rarad,decrad = "",""
            
            try: 
                if radius=="" or radius==None :
                    error = "Please enter a value for radius"
                    return "<div class='error'>" + error + '</div>'
                elif (float(radius) > 90 or float(radius) < 0 ):
                    error = 'Radius supplied should be greater than zero and less than 90 arcmin.'
                    return "<div class='error'>" + error + '</div>'
            except Exception:
                error = 'Radius supplied was not a number.'
                return "<div class='error'>" + error + '</div>'
            
            if select_clause == "default" or select_clause=="":
                if from_table == "Source":
                    select_clause = region_form.region_config.source_cols
                elif from_table == "Detection":
                    select_clause = region_form.region_config.detection_cols
                else :
                    select_clause = '*'
                
            try:
                if ra!="" and dec!="" and (co_sys =='J2000' or co_sys == 'B1950' or co_sys =='J' or co_sys == 'B'):  
                    if ':' in ra and ':' in dec:
                        wc = coords.Position(str(ra + ' ' + dec),equinox=co_sys)
                    else:
                        x_val = float(ra)   
                        y_val = float(dec)
                        
                        wc = coords.Position((x_val,y_val),equinox=co_sys)
                elif galactic_long!="" and galactic_lat!="" and (co_sys == 'Galactic' or co_sys == 'G'):    
                    system = 'galactic'
                    if ':' in galactic_long and ':' in galactic_lat:
                        wc = coords.Position(str(galactic_long.strip() + ' '+ galactic_lat.strip()),system=system)
                    else :
                        x_val = float(galactic_long)
                        y_val = float(galactic_lat)
                        wc = coords.Position((x_val,y_val),system=system)
            except Exception as e: 
                logging.exception('Exception caught:')
                error = "Please enter values for ra and dec or galactic long and latitude in degrees or sexadecimal format"
                return "<div class='error'>" + error + "</div>"
                
            if wc=="":
                error = "Please enter values for ra and dec or galactic long and latitude"
                return "<div class='error'>" + error + "</div>"
            
            if not error:
                rarad,decrad = wc.rad()
            
           
            if (rarad < 0) :
                error = "Supplied RA/Longitude &lt; 0"
            elif (rarad > 360) :
                error = "Supplied RA/Longitude &gt; 360"
            elif (decrad < -90) :
                error = "Supplied Dec/Latitude &lt; -90"         
            elif (decrad > 90) :
                error = "Supplied Dec/Latitude &gt; +90"
                
            if not error:
                radiusD=float(radius);
                if (co_sys == 'Galactic' or co_sys == 'G'):   
                    x,y = wc.j2000()
                else :
                    x,y= wc.dd()
                tr=region_form.TangentRectangle.TangentRectangle(x,y,radiusD)
                unitV = region_form.SLALIB.SLALIB.DCS2C(x*math.pi/(180.0),y*math.pi/(180.0))
                cX= '%.17f' % unitV[0]
                cY= '%.17f' % unitV[1]
                cZ= '%.17f' % unitV[2]
                cosRadius= '%.17f' % math.cos(radiusD*math.pi/(180.0*60.))
                minDec='%.17f' % tr.getMinLatitude()
                maxDec='%.17f' % tr.getMaxLatitude()
                minRA='%.17f' % tr.getMinLongitude()
                RASQL =tr.getRASQL()
                
                sqlDistance= "" #, 2*DEGREES(ASIN(sqrt(power("+cX+"-cx,2)+power("+ cY+"-cy,2)+power("+cZ+"-cz,2))/2))*60  as distance_ "
                
                if ((where_clause != None) and (where_clause!="") and (where_clause !=" ")):
                    sql="SELECT  "+ select_clause + sqlDistance + " FROM " + from_table +  " WHERE dec > "+minDec+" and dec < "+ maxDec +" and "+RASQL
                    sql +=" and ((cx * "+cX+" + cy * " + cY +" + cz * "+cZ+" ) >= "+cosRadius+") " + " and "+ where_clause ;
                else :
                    sql="SELECT  "+ select_clause + sqlDistance +" FROM " + from_table + " WHERE dec > "+minDec+" and dec < "+maxDec
                    sql += " and "+RASQL+ " and ((cx * "+cX+" + cy * " + cY +" + cz * "+cZ+" ) >= "+cosRadius+") ";    
        
                try:    
                   
                    
                    results_html_val = freeform_sql.generate_JSON_from_query(freeform_sql.execute_async_region_query(database,mode_global,sql),sql,database,mode_global)            
                    if results_html_val !="" and results_html_val !=None:
                        return results_html_val
                    
                except Exception as e:
                    print e
                    error = "An error occurred while executing your SQL query"
        
            if error:
                return "<div class='error'>" + error + "</div>"