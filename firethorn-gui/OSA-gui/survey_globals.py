from config import SURVEY_DB

## Global HTML vars

"""
General form Vars
"""




db_options = """
 <option value='""" + SURVEY_DB + """' selected="">""" + SURVEY_DB + """</option>
 <option value="OSA_DailySync">OSA_DailySync</option>
"""
            
programmes = """<tr><td align="right"><b>Programme: </td></b><td> ATLAS (VST ATLAS) </td></tr>"""

table_options = """
             <option selected="" value="">Please choose from list</option>
            <option value="ATLASsource"> Source table (merged catalogue)</option>
            <option value="ATLASdetection">Detection (table unmerged catalogue)</option>
            """
            
archive_input = "OSA"  
filename = "uploadFile"
coord_systems = {'J':'J2000', 'B': 'B1950', 'G' : 'Galactic'}
programmeID = 170

"""
CrossID form Vars
"""
crossID_baseTable_options = """
<option selected="" value="">Please choose from list</option>
<option value="source"> Source table (merged catalogue)</option>
<option value="detection">Detection (table unmerged catalogue)</option>    
"""
       
            
crossID_programmes = """
<input value="170" name="programmeID" type="hidden" />
""" 
crossID_db_options = db_options 
crossID_table = "baseTable"          
crossID_archive =  archive_input   
crossID_select = "selectList"
crossID_where = "whereClause"
crossID_file_name = filename
crossID_nearest = "nearest"

"""
MultiGetImage form Vars
"""
multigetimage_archive = archive_input    
multigetimage_file_name = filename
multigetimage_bands = """
u - <input type="checkbox" value="u" name="band">    
g - <input type="checkbox" value="g" name="band">    
r - <input type="checkbox" value="r" name="band">    
i - <input type="checkbox" value="i" name="band">    
z - <input type="checkbox" value="z" name="band">    
"""

"""
Region form Vars
"""
region_table_options =  table_options
            
region_db_options = """
            <option value=""" + SURVEY_DB + """ selected="">""" + SURVEY_DB + """</option>
            """            
            
region_table_input = "from_table"
region_select_clause = "select"
region_sys = "sys"
region_where_clause = "where"

"""
ImageList form Vars
"""
imagelist_programmes ="""
<tr style="display:none">
    <td align="right" nowrap="">
    <b>UKIDSS survey:</b>
    </td>
    <td>
      <!--1--> <select name="programmeID"><option value="">choose survey from list
    </option><option selected="" value="170">VST ATLAS</option></select> 
    </td><td>&nbsp;</td>
</tr>
"""








""" ***************  WSA Test data ************** """
"""
    WSA databases
             <option value="UKIDSSDR8PLUS" selected="">UKIDSSDR8PLUS</option>
             <option value="UKIDSSDR7PLUS">UKIDSSDR7PLUS</option>
             <option value="UKIDSSDR6PLUS">UKIDSSDR6PLUS</option>
             <option value="UKIDSSDR5PLUS">UKIDSSDR5PLUS</option>
             <option value="UKIDSSDR4PLUS">UKIDSSDR4PLUS</option>
             <option value="UKIDSSDR3PLUS">UKIDSSDR3PLUS</option>
             <option value="UKIDSSDR2PLUS">UKIDSSDR2PLUS</option>
             <option value="UKIDSSDR1PLUS">UKIDSSDR1PLUS</option>
             <option value="UKIDSSDR1">UKIDSSDR1</option>
             <option value="UKIDSSEDRPLUS">UKIDSSEDRPLUS</option>
             <option value="UKIDSSEDR">UKIDSSEDR</option>
             <option value="UKIDSSSV">UKIDSSSV</option>
             <option value="WFCAMCAL08B">WFCAMCAL08B</option>
"""   

"""
<tr style="display:none">
    <td align="right" nowrap="">
    <b>UKIDSS survey:</b>
    </td>
    <td>
      <!--1--> <select name="programmeID"><option value="">choose survey from list
    </option><option selected="" value="101">UKIDSS Large Area Survey, LAS</option><option value="102">UKIDSS Galactic Plane Survey, GPS</option><option value="103">UKIDSS Galactic Clusters Survey, GCS</option><option value="104">UKIDSS Deep Extragalactic Survey, DXS</option><option value="105">UKIDSS Ultra Deep Survey, UDS</option></select> 
    </td><td>&nbsp;</td>
</tr>
"""    


"""
Y - <input type="checkbox" value="Y" name="band">    
J_1 - <input type="checkbox" value="J_1" name="band">    
J_2 - <input type="checkbox" value="J_2" name="band">    
H - <input type="checkbox" value="H" name="band">    
K - <input type="checkbox" value="K" name="band">    
"""
    