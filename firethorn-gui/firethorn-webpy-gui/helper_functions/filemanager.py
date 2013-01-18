# -*- coding: utf-8 -*-
from __future__ import with_statement # This isn't required in Python 2.6     
from _pydev_log import Log
__metaclass__ = type


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
from config import vospace_dir
import config
import urllib2
from app import session
from helper_functions import session_helpers
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

    """Replacement for FCKEditor's built-in file manager."""
    
    def __init__(self, fileroot= '/static_vospace/'):
        self.fileroot = fileroot
        self.resource_url = "/"
        self.patherror = json.dumps(
                {
                    'Error' : 'No permission to operate on specified path.',
                    'Code' : -1
                }
            )

    def isvalidrequest(self, path, req):
        """Returns an error if the given path is not within the specified root path."""
        
        return path.startswith(self.fileroot) and (not req is None)
        


    def getinfo(self, path=None, getsize=True, **kwargs):
        """Returns a JSON object containing information about the given file."""
  
     
        resource = kwargs['resource'] if 'resource' in kwargs else []
        _type = string_functions.decode(kwargs['type']) if 'type' in kwargs else ""
        resource_preview = ''
        parent_folder = kwargs['parent_folder'] if 'parent_folder' in kwargs else ""
        workspace = kwargs['workspace'] if 'workspace' in kwargs else ""
        
        path = string_functions.decode(path)

        
        if resource==[]:
            children_from_id = self._get_info_from_id(path,_type, parent_folder, workspace)
            resource =  children_from_id[0] if children_from_id !=[] else []
            resource_preview = 'static/static_vospace/images/fileicons/txt.png'
        else :
            _type = resource['type'] if 'type' in resource else ""
            if _type==config.types['jdbc_table'] or _type==config.types['adql_table']:
                resource_preview = 'static/static_vospace/images/fileicons/txt.png'

        if resource!=[]:
            thefile = {
                'Filename' : resource['name'],
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
                        'Width' : '',
                        'Height' : '',
                        'Size' : '',
                        'Type' : resource['type']
                    }
                }
        else:
            
            thefile = {'Code' :-1,  'Error' : 'There was an error getting the file info.', 'result' : "" }
      
        """
        if not path_exists(path):
            thefile['Error'] = 'File does not exist.'
            return (json.dumps(thefile), None, 'application/json')
            
        previewPath = 'static/static_vospace/images/fileicons/' + thefile['File Type'].upper() + '.png'
        thefile['Preview'] = previewPath if path_exists('../../' + previewPath) else 'static/static_vospace/images/fileicons/default.png'

         """
        
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

        result = {}     
     
        resources = self.__get_chilren_from_id(path, _type, parent_folder, workspace) if (_type==config.types["adql_table"] or _type==config.types["jdbc_table"] or _type==config.types["adql_schema"] or _type==config.types["jdbc_schema"] and path!= config.vospace_root) else self.__get_workspace_children(path)
        if resources==[] or resources==None:
            req['content_type'] = 'application/json'
            req['result']= []
            req['root_type'] = _type
            
        else:    
            for i in resources:
                complete_path = string_functions.encode(i['ident'])    
                result[i['name']] = self.getinfo(path=str(complete_path) , getsize=getsizes, resource = i)   
                
        req['parent_name'] = parent_name        
        req['content_type'] = 'application/json'
        req['result']= result
        req['root_type'] = _type
        
        return req
    
    
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
            if (session_helpers(session).rename_session_value(ident, new, old, workspace, parent_folder,_type))>0:
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
        
        
        return result


    def delete(self, path=None, req=None):
    
        if not self.isvalidrequest(path,req):
            return (self.patherror, None, 'application/json')
        
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

    
    
    def add(self, currentpath=None, req=None, newfile='', upload_name = ''):     
        
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
                
            if workspace=="" or workspace==None:
                workspaces = session_helpers(session).get_workspaces()
                new_object = {'ident': newName, 'type': config.types["jdbc_schema"], 'name': newName, 'modified': time, 'created': time, 'children':[]}
                workspaces.append(new_object)
                result = {
                    'Code' : 0,
                    'Workspace' : workspace,
                    'Name' : newName
                    }
                
            elif parent_folder=="" or parent_folder==None:
                workspace_data = session_helpers(session).get_session_value(workspace)
                new_object = {'ident': newName, 'type': config.types["jdbc_schema"], 'name': newName, 'modified': time, 'created': time, 'children':[]}
                workspace_data.append(new_object)
                result = {
                    'Code' : 0,
                    'Workspace' : workspace,
                    'Name' : newName
                    }
            else :
                result = {
                  'Code' :-1,
                  'Path' : newName,
                  'Workspace' : workspace,
                  'Error' : 'Not allowed to create directory at that location.' # TODO grab the actual traceback.
                  }
           
    
        except:
        
            result = {
                'Code' :-1,
                'Path' : newName,
                'Workspace' : workspace,
                'Error' : 'There was an error creating the directory.' # TODO grab the actual traceback.
            }
             
        return result
    
    
    def download(self, path=None, req=None):
    
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
            if workspace == "":
                workspace_data = session_helpers(session).get_workspaces()
                for i in workspace_data:
                    if i["ident"] == ident:
                        json_data = [i]
            elif parent_folder=="" :
                workspace_data = session_helpers(session).get_session_value(workspace)
                if type_helpers.isTable(_type):
                    for i in workspace_data:
                        if i["ident"] == ident:
                            json_data = [i]
                
                elif  type_helpers.isSchema(_type):
                    for i in workspace_data:
                        if i["ident"] == ident:
                            json_data = [i]  
                               
            elif parent_folder>0:
                workspace_data = session_helpers(session).get_session_value(workspace)
           
                if len(workspace_data)>0:
                    for i in workspace_data:
                        if i["ident"] == parent_folder:
                            children_nodes = i["children"]
                            for x in children_nodes:
                                if x["ident"] == ident:
                                    json_data = [x]
                else :
                    json_data = []     

            else:
                json_data = []     
            
        except Exception:
            traceback.print_exc()
        
        return json_data
        
        
    def __get_chilren_from_id(self, ident ,_type, parent_folder, workspace):
        """
        Get chilren nodes for an object based on it's id, type, parent_folder and workspace
        """        
        json_data = []
        
        try:
            if workspace == "":
                workspace_data = session_helpers(session).get_workspaces()
                for i in workspace_data:
                    if i["ident"] == ident:
                        json_data = [i]
                        
            elif parent_folder=="" :
                
                workspace_data = session_helpers(session).get_session_value(workspace)

                if type_helpers.isTable(_type):
                    for i in workspace_data:
                        if i["ident"] == ident:
                            json_data = [i]
                
                elif  type_helpers.isSchema(_type):
                    for i in workspace_data:
                        if i["ident"] == ident:
                            json_data = i["children"]
                            
            elif parent_folder>0:
                workspace_data = session_helpers(session).get_session_value(workspace)
           
                if len(workspace_data)>0:
                    for i in workspace_data:
                        if i["ident"] == parent_folder:
                            children_nodes = i["children"]
                            for x in children_nodes:
                                if x["ident"] == ident:
                                    json_data = [x]
                else :
                    json_data = []     

            else:
                json_data = []     
            
        except Exception:
            traceback.print_exc()
        
        return json_data

       
    def __get_workspace_children(self, path):
        """
        Get all JDBC resources 
        """
        if path =="" or path == None or path==config.vospace_root:
            json_data = session_helpers(session).get_workspaces()
        else :
            json_data = session_helpers(session).get_session_value(path)
            
        return json_data
    
    

myFilemanager = Filemanager(fileroot=vospace_dir) 


def handler(req): 
    method=str(req['mode'])
    req['req']  =  {'time' : req.time}
    
    temp_dict = {key: value for key, value in req.items() if key is not 'mode'}
    temp_dict1 = {key: value for key, value in temp_dict.items() if key is not 'showThumbs'}
    temp_dict2 = {key: value for key, value in temp_dict1.items() if key is not 'method'}
    temp_dict3 = {key: value for key, value in temp_dict2.items() if key is not 'time'}
    methodKWargs=temp_dict3
    result = getattr(myFilemanager, method)(**methodKWargs)
    return result

    