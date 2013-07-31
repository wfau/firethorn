'''
Created on Jul 22, 2013

@author: stelios
'''

import web
import urllib2
import json
import firethorn_config
import logging
import urllib

def generate_available_list_from_workspace(workspace,add_catalogue_html,exclude_schema,session):
    
    req = urllib2.Request( workspace + firethorn_config.type_select_uris["schemas"], headers={"Accept" : "application/json", "firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")})
    response = urllib2.urlopen(req) 
    workspace_schema_list = json.loads(response.read())
    response.close()
    available_list = ""
    for i in workspace_schema_list:
        if exclude_schema!=i["ident"]:
            available_list += """
                <li><span>""" + i["fullname"] + """</span> as <input type="text" name="catalogue" value='""" +  i["name"]  + """' /><input type="hidden" name="ident" value='""" + i["ident"] +"""'/>""" + add_catalogue_html + """</li>
             """ 
       
    
    return available_list



def generate_selected_list(fullname, alias, ident,remove_catalogue_html,session):
    selected_list = """
        <li><span>""" + fullname + """</span> as <input type="text" name="catalogue" value='""" + alias + """' /><input type="hidden" name="ident" value='""" + ident + """'/>""" + remove_catalogue_html + """</li>
     """ 
    return selected_list



def create_initial_workspace(initial_catalogue_fullname, initial_catalogue_alias, initial_catalogue_ident,session):
    query_schema =""
    
    from datetime import datetime
    t = datetime.now()
   
    try:
        ### Create workspace
        workspace_name = 'workspace-' + t.strftime("%y%m%d_%H%M%S") 
        data = urllib.urlencode({firethorn_config.resource_create_name_params['http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json'] : workspace_name})
        req = urllib2.Request( firethorn_config.workspace_creator, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")})
        response = urllib2.urlopen(req)
        workspace = json.loads(response.read())["ident"]
        response.close()
    except Exception as e:
        logging.exception("Error creating workspace")
   
    try:    
        ### Create Query Schema 
        data = urllib.urlencode({firethorn_config.resource_create_name_params['http://data.metagrid.co.uk/wfau/firethorn/types/adql-schema-1.0.json'] : "query_schema"})
        req = urllib2.Request( workspace +  firethorn_config.schema_create_uri, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")})
        response = urllib2.urlopen(req) 
        query_schema = json.loads(response.read())["ident"]
        response.close()
    except Exception as e:
        logging.exception("Error creating query space")
    
    importname = ""
    name = initial_catalogue_fullname
    alias = initial_catalogue_alias
    ident = initial_catalogue_ident
    
    try:   
        if alias!="":
            importname = alias
        else :
            importname = name
        
        if importname!="":
            data = urllib.urlencode({'urn:adql.copy.depth' : "THIN", firethorn_config.workspace_import_schema_base : ident, firethorn_config.workspace_import_schema_name : importname})
      
        req = urllib2.Request( workspace + firethorn_config.workspace_import_uri, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : session.get("email","unknown user"), "firethorn.auth.community" : session.get("community_input","public (unknown)")}) 
        response = urllib2.urlopen(req)
    except Exception as e:
        logging.exception("Error importing catalogue")
   
    return query_schema
