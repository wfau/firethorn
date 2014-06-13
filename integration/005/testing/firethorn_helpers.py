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
import urllib2
from datetime import datetime

class FirethornEngine(object):
    '''
    classdocs
    '''


    def __init__(self):
        '''
        Constructor
        '''
        self.id = 1
        self.initialise_metadata_import()
         
    def initialise_metadata_import(self):
        self.jdbcspace = self.create_jdbc_space('Atlas JDBC conection' ,'spring:RoeATLAS','*', 'atlas')
        self.adqlspace = self.create_adql_space('Atlas ADQL workspace')
        self.adqlschema = self.import_jdbc_metadoc(self.adqlspace, self.jdbcspace, 'ATLASDR1', 'dbo', "/var/www/atlas/testing/metadocs/ATLASDR1_TablesSchema.xml")
         
    def create_jdbc_space(self, resourcename = 'Atlas JDBC conection' ,resourceuri= 'spring:RoeATLAS', catalogname= '*', ogsadainame= 'atlas'):
        jdbcspace=""
        try:
         
            data = urllib.urlencode({firethorn_config.resource_create_name_params['http://data.metagrid.co.uk/wfau/firethorn/types/entity/jdbc-resource-1.0.json'] : resourcename,
                                     "urn:jdbc.copy.depth" : "THIN",
                                     "jdbc.resource.create.url" : resourceuri,
                                     "jdbc.resource.create.catalog" : catalogname,
                                     "jdbc.resource.create.ogsadai" : ogsadainame})
            req = urllib2.Request( firethorn_config.jdbc_creator, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : firethorn_config.test_email , "firethorn.auth.community" : "public (unknown)"})
            response = urllib2.urlopen(req)
            jdbcspace = json.loads(response.read())["ident"]
            response.close()
        except Exception as e:
            logging.exception("Error creating jdbc space")
        return jdbcspace    
    

    def import_jdbc_metadoc(self, adqlspace="", jdbcspace="", jdbccatalogname='ATLASDR1', jdbcschemaname='dbo',metadocfile="/var/www/atlas/testing/metadocs/ATLASDR1_TablesSchema.xml"):
        jdbcschemaident = ""
        adqlschema=""
        import pycurl
        import cStringIO
        
        buf = cStringIO.StringIO()
        try:
         
            data = urllib.urlencode({"jdbc.resource.schema.select.catalog" : jdbccatalogname,
                                     "jdbc.resource.schema.select.schema" : jdbcschemaname})
            req = urllib2.Request( jdbcspace + "/schemas/select", data, headers={"Accept" : "application/json", "firethorn.auth.identity" : firethorn_config.test_email , "firethorn.auth.community" : "public (unknown)"})
            response = urllib2.urlopen(req)
            jdbcschemaident = json.loads(response.read())["ident"]
            response.close()
        except Exception as e:
            logging.exception("Error creating selecting from JDBC space:  " + jdbcspace)
    
        try:
           
            c = pycurl.Curl()   
            
            url = adqlspace + "/metadoc/import"        
            values = [  
                      ("urn:schema.metadoc.base", str(jdbcschemaident)),
                      ("urn:schema.metadoc.file", (c.FORM_FILE, metadocfile))]
                       
            c.setopt(c.URL, str(url))
            c.setopt(c.HTTPPOST, values)
            c.setopt(c.WRITEFUNCTION, buf.write)
            c.setopt(pycurl.HTTPHEADER, [ "firethorn.auth.identity",firethorn_config.test_email,
                                          "firethorn.auth.community","public (unknown)"
                                          ])
            c.perform()

            c.close()
           
           

            
         
        except Exception as e:
            logging.exception("Error creating adql schema")
        #adqlschema = "http://localhost:8080/firethorn/adql/schema/49184921" 
        buf.close()
        json.loads(response.read())["ident"]
        adqlschema = json.loads(buf.getvalue())["ident"]
        return adqlschema
    
    
    
    def create_adql_space(self, adqlspacename=None):
        adqlspace = ""
        try:
            ### Create workspace
            if adqlspacename==None:
                t = datetime.now()
                adqlspacename = 'workspace-' + t.strftime("%y%m%d_%H%M%S") 
            data = urllib.urlencode({firethorn_config.resource_create_name_params['http://data.metagrid.co.uk/wfau/firethorn/types/entity/adql-resource-1.0.json'] : adqlspacename,
                                     "urn:adql.copy.depth": "THIN"})
            req = urllib2.Request( firethorn_config.workspace_creator, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : firethorn_config.test_email , "firethorn.auth.community" : "public (unknown)"})
            response = urllib2.urlopen(req)
            adqlspace = json.loads(response.read())["ident"]
            response.close()
        except Exception as e:
            logging.exception("Error creating ADQL space")
        return adqlspace
                         
                         
    def create_query_schema(self, resource=""):
        query_schema = ""
        try:    
            ### Create Query Schema 
            data = urllib.urlencode({firethorn_config.resource_create_name_params['http://data.metagrid.co.uk/wfau/firethorn/types/entity/adql-schema-1.0.json'] : "query_schema"})
            req = urllib2.Request( resource +  firethorn_config.schema_create_uri, data, headers={"Accept" : "application/json", "firethorn.auth.identity" : firethorn_config.test_email, "firethorn.auth.community" : "public (unknown)"})
            response = urllib2.urlopen(req) 
            query_schema = json.loads(response.read())["ident"]
            response.close()
        except Exception as e:
            logging.exception("Error creating query schema")
        return query_schema
    
        
    def create_initial_workspace(self, initial_catalogue_fullname, initial_catalogue_alias, initial_catalogue_ident):
        query_schema =""
        importname = ""
        t = datetime.now()
        workspace = self.create_adql_space()
        query_schema = self.create_query_schema(workspace)
        
        name = initial_catalogue_fullname
        alias = initial_catalogue_alias
        ident = initial_catalogue_ident
        data = None
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
