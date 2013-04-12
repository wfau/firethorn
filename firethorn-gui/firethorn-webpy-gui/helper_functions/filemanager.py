# -*- coding: utf-8 -*-
from __future__ import with_statement # This isn't required in Python 2.6     



from contextlib import closing, contextmanager 
import os, sys, traceback
import os.path
import urllib
from urlparse import *
import base64
import json
from datetime import date
import json
import traceback
import shutil
import time
from config import vospace_dir,types
import config
import urllib2
from app import session
from helper_functions import workspace_helpers
from helper_functions import type_helpers
from helper_functions.string_functions import string_functions
string_functions = string_functions()

today = date.today()

ver = sys.version_info
if ver[0]<2 and ver[1]<5:
    raise EnvironmentError('Must have Python version 2.5 or higher.')


try:
    import json
except ImportError:
    raise EnvironmentError('Must have the json module.  (It is included in Python 2.6 or can be installed on version 2.5.)')


try:
    from PIL import Image
except ImportError:
    raise EnvironmentError('Must have the PIL (Python Imaging Library).')
    

path_exists = os.path.exists
normalize_path = os.path.normpath
absolute_path = os.path.abspath 
split_path = os.path.split
split_ext = os.path.splitext


euncode_urlpath = urllib.quote_plus



def encodeURLsafeBase64(data):
    return base64.urlsafe_b64encode(data).replace('=','').replace(r'\x0A','')
       
def image(*args):
    raise NotImplementedError 



class Filemanager:

    """
    Virtual file manager for the Firethorn GUI
    """
    
    def __init__(self, fileroot= '/static_vospace/'):
        self.fileroot = fileroot
        self.resource_url = "/"
        self.patherror = {
                    'Error' : 'No permission to operate on specified path.',
                    'Code' : -1
                }
           
    def isvalidrequest(self, path, req):
        """Returns an error if the given path is not within the specified root path."""
       
        return path.startswith(self.fileroot) and (not req is None)
        


    def getinfo(self, path=None, getsize=True, **kwargs):
        """Returns a JSON object containing information about the given file."""
  
        try:
            resource = kwargs['resource'] if 'resource' in kwargs else []
            _type = string_functions.decode(kwargs['type']) if 'type' in kwargs else ""
            resource_preview = ''
            parent_folder = kwargs['parent_folder'] if 'parent_folder' in kwargs else ""
            workspace = kwargs['workspace'] if 'workspace' in kwargs else ""
            path = string_functions.decode(path)
            adql_size = ""
            adql_type = ""
            
            if resource==[]:
                children_from_id = self._get_info_from_id(path,_type, parent_folder, workspace)
                resource =  children_from_id
                resource_preview = 'static/static_vospace/images/fileicons/txt.png'
            else :
                _type = resource['type'] if 'type' in resource else ""
                if _type==config.types['jdbc_table'] or _type==config.types['adql_table']:
                    resource_preview = 'static/static_vospace/images/fileicons/txt.png'
            
            if resource!=[]:
                if getattr(resource, "info", "")!="":
                    if getattr(resource["info"], "adql", "")!="":
                        adql_type = getattr(resource["info"]["adql"], 'type', "")
                
                if getattr(resource, "info", "")!="":
                    if getattr(resource["info"], "adql", "")!="":
                        adql_size = getattr(resource["info"]["adql"], 'size', "")
                workspace_name = workspace_helpers(session).get_object_name(workspace) if workspace!="" else ""
                parent_folder_name =  workspace_helpers(session).get_object_name(parent_folder) if parent_folder!="" else ""
                if workspace_name=="":
                    parent_directory = ""
                elif parent_folder_name=="":
                    parent_directory = workspace_name
                else :
                    parent_directory = workspace_name + "/" + parent_folder_name
    
                    
                thefile = {
                    'Filename' : resource['name'],
                    'Parent_directory' :  parent_directory,
                    'File Type' : resource['type'],
                    'Preview' : resource_preview if not resource_preview =='' else 'static/static_vospace/images/fileicons/_Open.png',
                    'Path' : path,
                    'Workspace' : workspace,
                    'Parent_folder' : parent_folder,
                    'Error' : '',
                    'Code' : 0,
                    'Properties' : {
                            'Date Created' : resource['created'],
                            'Date Modified' : resource['modified'],
                            'ADQL_Size' : adql_size,
                            'ADQL_Type' : adql_type,
                            'Type' : resource['type']
                        }
                    }
            else:
                
                thefile = {'Code' :-1,  'Error' : 'There was an error getting the file info.', 'result' : "" }
        except Exception:
            traceback.print_exc()
            thefile = {'Code' :-1,  'Error' : 'There was an error getting the file info.', 'result' : "" }

        
        return thefile


    def getfolder(self, path=None, getsizes=True, req=None, **kwargs):
        """
        if not self.isvalidrequest(path,req):
            return (self.patherror, None, 'application/json')
        """    
        path = string_functions.decode(path)
        _type = kwargs['type'] if 'type' in kwargs else ""
        _id = kwargs['_id'] if '_id' in kwargs else ""
        parent_folder = kwargs['parent_folder'] if 'parent_folder' in kwargs else ""
        workspace = kwargs['workspace'] if 'workspace' in kwargs else ""
        parent_name = kwargs['parent'] if 'parent' in kwargs else ""
        pathinfo = self._get_info_from_id(path, _type, parent_folder, workspace)
        name = path
       
        if len(pathinfo)>0:
            name = pathinfo["name"]
        result = {}     
       
        resources = self.__get_chilren_from_id(path, _type, parent_folder, workspace) 
       
        if resources==[] or resources==None:
            req['content_type'] = 'application/json'
            req['result']= []
            req['root_type'] = _type
            
        else:    
            for i in resources:
                complete_path = string_functions.encode(i['ident'])    
                result[i['name']] = self.getinfo(path=str(complete_path) , getsize=getsizes, resource = i)   
            
      
        req['parent_name'] = parent_name if parent_name  != "" else workspace_helpers(session).get_object_name(workspace)       
        req['content_type'] = 'application/json'
        req['result']= result
        req['root_type'] = _type
        req['name'] = name
        
        return req
    
    def start_query_loop(self,url):
        """
      
        @param url: A URL string to be used
        @return: Results of query
        """
        
        def get_status(url):
            request2 = urllib2.Request(url, headers={"Accept" : "application/json"})
            f_read = urllib2.urlopen(request2)
            query_json = f_read.read()
            return query_json
        
        max_size_exceeded = False 
        f_new = ""
        f_read = ""
        return_vot = ''
        delay = 2
        start_time = time.time()
        elapsed_time = 0
        query_json = {'syntax' : {'friendly' : 'There was an error running your query', 'status' : 'Error' }}

        try:
            data = urllib.urlencode({ config.query_status_update : "RUNNING"})
            request = urllib2.Request(url, data, headers={"Accept" : "application/json"})
            f_update = urllib2.urlopen(request)
            query_json =  json.loads(f_update.read())
            query_status = query_json["status"]
            
            
            while query_status=="PENDING" or query_status=="RUNNING" and elapsed_time<3600:
                query_json = get_status(url)
                query_status= json.loads(query_json)["status"] 
                time.sleep(delay)
                elapsed_time = time.time() - start_time
                
          
            if query_status=="ERROR" or query_status=="FAILED":
                return {'Code' :-1,  'Error' : 'There was an error running your query' }
            elif query_status=="CANCELLED":
                return {'Code' :1,  'Error' : 'Query has been canceled' }
            elif query_status=="EDITING":
                return {'Code' :-1,  'Error' : query_json["syntax"]["status"] + ' - ' + query_json["syntax"]["friendly"] }
            elif query_status=="COMPLETED":
                return {'Code' :1,  'Error' : '' }
            else :
                return {'Code' :-1,  'Error' : 'There was an error running your query' }
            
            

        except Exception as e:
            traceback.print_exc()   
            return {'Code' :-1,  'Error' : 'There was an error running your query' }
        if f_new != "":
            f_new.close()
        if f_read != "":
            f_read.close() 
        return return_vot    
    
    def run_query(self, query=None, query_name="", workspace="", **kwargs):
        """
        Run a query on a resource
        """
        f=''
       
        query = string_functions.decode(query)
        workspace = string_functions.decode(workspace)
       
        try :
            from datetime import datetime
            t = datetime.now()
            if query_name=="":
                query_name = 'query-' + workspace + '-'+ t.strftime('%m-%d-%Y') 
            
            
            data = urllib.urlencode({ config.query_name_param : query_name, config.query_param : query})
            request = urllib2.Request(workspace + config.query_create_uri, data, headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
            query_create_result = json.loads(f.read())
            result = self.start_query_loop(query_create_result["ident"])
           
           

        except Exception:
            traceback.print_exc()
            
            return {'Code' :-1,  'Error' : 'There was an error running your query' }
        
        if f!='':
            f.close()
        return result
        
        
    
    
    def get_metadata(self, ident=None, _type=None, parent_folder="", workspace="", **kwargs):
        
        """
        Get the metadata of an object
        """
        
        """  
        if not self.isvalidrequest(path=old,req=req):
            return (self.patherror, None, 'application/json')
        """
        
       

        ident = string_functions.decode(ident)
        _type = string_functions.decode(_type)
        workspace = string_functions.decode(workspace)
        parent_folder = string_functions.decode(parent_folder)
        result = {'Code' :-1,  'Error' : 'There was an error fetching the metadata for this resource.' }
        try :
          
         
            request = urllib2.Request(ident, headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
            json_data = json.loads(f.read())
            content = "<div id='metadata_wrapper'>" +"Name: <a>" + getattr('json_data', "name", "Unknown") + "</a><br/>"  + "Created on:  <a>" + json_data["created"]  + "</a><br/>"  + "Modfied on: <a>" + json_data["modified"] + "</a><br/>"  
            
            if getattr(json_data, "info", None)!=None:
                content += "Type: <a>" + getattr(json_data["info"], "type", "") + "</a> <br/>"  +"Size: <a>" + getattr(json_data["info"], "size", "") + "<a/><br/>"  
            
            content += "</div>"
            result = {'Code' :1,  'Content' : content }
        except Exception:
            traceback.print_exc()
            
            result = {'Code' :-1,  'Error' : 'There was an error fetching the metadata for this resource.' }
        
        return result
        
        
    def rename(self, old=None, new=None, req=None, ident=None, _type=None, parent_folder="", workspace="", **kwargs):
        
        """  
        if not self.isvalidrequest(path=old,req=req):
            return (self.patherror, None, 'application/json')
        """
        
        f = ''
        new = string_functions.decode(new)
        old = string_functions.decode(old)
        ident = string_functions.decode(ident)
        _type = string_functions.decode(_type)
        workspace = string_functions.decode(workspace)
        parent_folder = string_functions.decode(parent_folder)

        try :
            if (workspace_helpers(session).rename_workspace_value(ident, new, old, workspace, parent_folder,_type))>0:
                result = {
                    'Code' : 0,
                    'Old Path' : ident,
                    'Old Name' : old,
                    'New Path' : ident,
                    'New Name' : new,
                    'Error' : 'There was an error renaming the file.' # todo: get the actual error
                }
            else :
                result = {'Code' :-1,  'Error' : 'There was an error renaming the file.' }

        except Exception:
            traceback.print_exc()
            if f!='':
                f.close()
            return {'Code' :-1,  'Error' : 'There was an error renaming the file.' }
          
        
        return result


    def delete(self, path=None, req=None, **kwargs):
        if not self.isvalidrequest(path,req):
            return self.patherror
        
        if os.path.isdir(path):
            shutil.rmtree(path)
        else:
            os.remove(path)
        
        result = {
            'Code' : 0,
            'Path' : path,
            'Error' : 'There was an error deleting the file.' # todo: get the actual error
        }
        return result

    
    
    def add(self, currentpath=None, req=None, newfile='', upload_name = '', **kwargs):     
        
        newName = currentpath + upload_name
        if not self.isvalidrequest(currentpath,req) or newfile=="":
            result = {
                'Code' : -1,
                'Path' : currentpath,
                'Name' : newName,
                'Error' : 'No file was uploaded.'
            }
            return '<textarea> ' + json.dumps(result) + '</textarea>'
       
        try:
            
            with open(newName, 'w+') as f:
                            f.write(newfile) 
            
        except Exception as e:
            traceback.print_exc()
            result = {
                'Path' : currentpath,
                'Name' : newName,
                'Error' : 'No file was uploaded.'
            }
            
        
        result = {
            'Code' : 0,
            'Path' : currentpath,
            'Name' : newName,
            'Error' : ''
        }
  
        return '<textarea> ' + json.dumps(result) + '</textarea>'
        
    
    def addfolder(self, name, workspace, parent_folder, req = None, **kwargs):        
        
     
        result=[]
        time = req["time"]
        newName = urllib.quote_plus(name)
        
        try:
           
            workspace_helpers(session).add_new_to_path( workspace,  newName, types["adql_schema"], "")
            
            result = {
                'Code' : 1,
                'Workspace' : workspace,
                'Name' : newName
                }
           
         
           
    
        except:
            traceback.print_exc()
            result = {
                'Code' :-1,
                'Path' : newName,
                'Workspace' : workspace,
                'Error' : 'There was an error creating the directory.' # TODO grab the actual traceback.
            }
             
        return result
    
    
    def download(self, path=None, req=None, **kwargs):
    
        if not self.isvalidrequest(path,req):
            return (self.patherror, None, 'application/json')
            
        name = path.split('/')[-1]
                  
        req['content_type'] = 'application/x-download'
        req['filename'] = name
        try:
            if os.path.exists(path):    
                f = open(path)
                req['file'] = f.read()
            else :
                req['file'] = "Error reading file"
        except Exception as e:
            req['file'] = "Error reading file"
        return req
    
    

    def _get_info_from_id(self, ident ,_type, parent_folder, workspace):
        """
        Get info for an object based on it's id, type, parent_folder and workspace
        """        
        json_data = []
        try:
            request = urllib2.Request(ident, headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
            json_data =json.loads(f.read()) 
        except Exception:
            traceback.print_exc()
        
        return json_data
        
        
    def __get_chilren_from_id(self, ident ,_type, parent_folder, workspace):
        """
        Get chilren nodes for an object based on it's id, type, parent_folder and workspace
        """        
        json_data = []
        
        try:
            if ident=="/" or ident =="":
                json_data = workspace_helpers(session).get_workspaces()
            if  type_helpers.isSchema(_type):
                request = urllib2.Request(ident + config.type_select_uris["tables"], headers={"Accept" : "application/json"})
                f = urllib2.urlopen(request)
                json_data =json.loads(f.read()) 
            elif  type_helpers.isTable(_type):
                request = urllib2.Request(ident + config.type_select_uris["columns"], headers={"Accept" : "application/json"})
                f = urllib2.urlopen(request)
                json_data = json.loads(f.read())  
            elif type_helpers.isRootType(_type):
                request = urllib2.Request(ident + config.type_select_uris["schemas"], headers={"Accept" : "application/json"})
                request2 = urllib2.Request(ident + config.type_select_uris["tables"], headers={"Accept" : "application/json"})
                f = urllib2.urlopen(request)
                #f2 = urllib2.urlopen(request2)
                
                json_data = json.loads(f.read())    
                #print f2.read()
           
        except Exception:
            traceback.print_exc()
        
        return json_data
        

       
    def __get_workspace_children(self, path):
        """
        Get all JDBC resources 
        """
     
        if path =="" or path == None or path==config.vospace_root:
            json_data = workspace_helpers(session).get_workspaces()
        else :
            json_data = workspace_helpers(session).get_(path)
            
        return json_data
    
    

myFilemanager = Filemanager(fileroot=vospace_dir) 


def handler(req): 
    result = ""
    try:
        method=str(req['mode'])
        req['req']  =  {'time' : req.time}
        
        temp_dict = {key: value for key, value in req.items() if key is not 'mode' and key is not 'showThumbs' and key is not 'method'}
        temp_dict3 = {key: value for key, value in temp_dict.items() if (key is not 'time') and (key is not 'import_input_area') }
        methodKWargs=temp_dict3
        result = getattr(myFilemanager, method)(**methodKWargs)
        
    except Exception as e:
        traceback.print_exc()
        
    return result

    