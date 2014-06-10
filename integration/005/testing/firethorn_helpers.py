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



def create_initial_workspace(initial_catalogue_fullname, initial_catalogue_alias, initial_catalogue_ident):
    query_schema =""
    workspace = ""
    from datetime import datetime
    t = datetime.now()
    logging.exception("test")
    try:
        ### Create workspace
        workspace_name = 'workspace-' + t.strftime("%y%m%d_%H%M%S") 
        data = urllib.urlencode({firethorn_config.resource_create_name_params['http://data.metagrid.co.uk/wfau/firethorn/types/entity/adql-resource-1.0.json'] : workspace_name})
        req = urllib2.Request( firethorn_config.workspace_creator, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : firethorn_config.test_email , "firethorn.auth.community" : "public (unknown)"})
        response = urllib2.urlopen(req)
        workspace = json.loads(response.read())["ident"]
        response.close()
    except Exception as e:
        logging.exception("Error creating workspace")

  
    try:    
        ### Create Query Schema 
        data = urllib.urlencode({firethorn_config.resource_create_name_params['http://data.metagrid.co.uk/wfau/firethorn/types/entity/adql-schema-1.0.json'] : "query_schema"})
        req = urllib2.Request( workspace +  firethorn_config.schema_create_uri, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : firethorn_config.test_email, "firethorn.auth.community" : "public (unknown)"})
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
      
        req = urllib2.Request( workspace + firethorn_config.workspace_import_uri, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : firethorn_config.test_email, "firethorn.auth.community" : "public (unknown)"}) 
        response = urllib2.urlopen(req)
    except Exception as e:
        logging.exception("Error importing catalogue")
   
    return query_schema
