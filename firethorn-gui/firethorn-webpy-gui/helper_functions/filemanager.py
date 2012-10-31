# -*- coding: utf-8 -*-
from __future__ import with_statement # This isn't required in Python 2.6     
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
        self.resource_url = "http://" + config.web_services_hostname + config.get_jdbc_resources_url
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
       
        """
        if not self.isvalidrequest(path,req):
            return (self.patherror, None, 'application/json')
        """
        path = urllib2.unquote(urllib2.quote(path.encode("utf8"))).decode("utf8")
        resource = kwargs['resource'] if 'resource' in kwargs else []
        _type = kwargs['type'] if 'type' in kwargs else ""
        resource_preview = ''
        
        if resource==[]:
        
            request = urllib2.Request(path,headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
            resource = json.loads(f.read())
            resource_preview = 'static/static_vospace/images/fileicons/txt.png'
            
        thefile = {
            'Filename' : resource['name'],
            'File Type' : resource['type'],
            'Preview' : resource_preview if not resource_preview =='' else 'static/static_vospace/images/fileicons/_Open.png',
            'Path' : path,
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
  
        path = urllib2.unquote(urllib2.quote(path.encode("utf8"))).decode("utf8")
        _type = kwargs['type'] if 'type' in kwargs else ""

        result = {}     
      
        resources = self.__get_jdbc_resources() if path == self.resource_url else self.__get_chilren_from_id(path, _type)
        
        for i in resources:
            complete_path = i['ident']
            result[i['name']] = self.getinfo(path=str(complete_path) , getsize=getsizes, resource = i)   
        
                                
        req['content_type'] = 'application/json'
        req['result']= result
        req['root_type'] = _type
        
        return req
    
    
    def rename(self, old=None, new=None, req=None, ident=None, _type=None):
        
        """  
        if not self.isvalidrequest(path=old,req=req):
            return (self.patherror, None, 'application/json')
        """
        
        f = ''
        new = urllib2.unquote(urllib2.quote(new.encode("utf8"))).decode("utf8")
        old = urllib2.unquote(urllib2.quote(old.encode("utf8"))).decode("utf8")
        ident = urllib2.unquote(urllib2.quote(ident.encode("utf8"))).decode("utf8")
        _type = urllib2.unquote(urllib2.quote(_type.encode("utf8"))).decode("utf8")

      
        try :
            encoded_args = urllib.urlencode({ config.type_update_params[_type] : new })
            request = urllib2.Request(ident, encoded_args, headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
         
        except Exception as e:
            if f!='':
                f.close()
            return {'Code' :-1,  'Error' : 'There was an error deleting the file.' }
        
     
       
        
        result = {
            'Code' : 0,
            'Old Path' : ident,
            'Old Name' : old,
            'New Path' : ident,
            'New Name' : new,
            'Error' : 'There was an error renaming the file.' # todo: get the actual error
        }
        
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
            print e
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
        
    
    def addfolder(self, path, name, req = None):        

        if not self.isvalidrequest(path,req):
            return (self.patherror, None, 'application/json')

        newName = urllib.quote_plus(name)
        newPath = path + newName + '/'
        
        if not path_exists(newPath):
            try:
                os.mkdir(newPath)
                result = {
                    'Code' : 0,
                    'Parent' : path,
                    'Name' : newName
                    }
            
            except:
            
                result = {
                    'Code' :-1,
                    'Path' : path,
                    'Parent' : newName,
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
    
    
    def __get_chilren_from_id(self, ident ,_type):
        """
        Get chilren nodes for an object based on it's id
        """

        f= ""
        json_data = []
        try:
            if ident!="" and _type!="":
                ident = ident + config.resource_uris[_type]
                request = urllib2.Request(ident, headers={"Accept" : "application/json"})
                f = urllib2.urlopen(request)
                json_data = json.loads(f.read())
        except Exception:
            print traceback.print_exc()
                
        finally:
            if f!="":
                f.close()         

        
        return json_data

       
    def __get_jdbc_resources(self):
        """
        Get all JDBC resources 
        """

        f = ''
        json_data = []
        try :
            request = urllib2.Request(self.resource_url, headers={"Accept" : "application/json"})
            f = urllib2.urlopen(request)
            json_data = json.loads(f.read())
        except Exception as e:
            if f!='':
                f.close()
            print e
            traceback.print_exc()
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

    