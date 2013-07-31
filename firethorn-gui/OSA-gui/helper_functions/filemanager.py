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
from config import vospace_dir, survey_prefix

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
    
    def __init__(self, fileroot= survey_prefix + '/static_vospace/'):
        self.fileroot = fileroot
        self.patherror = json.dumps(
                {
                    'Error' : 'No permission to operate on specified path.',
                    'Code' : -1
                }
            )

    def isvalidrequest(self, path, req):
        """Returns an error if the given path is not within the specified root path."""
        
        return path.startswith(self.fileroot) and (not req is None)
        


    def getinfo(self, path=None, getsize=True, req=None):
        """Returns a JSON object containing information about the given file."""

        if not self.isvalidrequest(path,req):
            return (self.patherror, None, 'application/json')
        
        
        sub_folders = path.split('/')
        temp = []
        for item in sub_folders:
            if item != '':
                temp.append(item)
       
        last_item = temp[len(temp)-1]
        
        thefile = {
            'Filename' : last_item,
            'File Type' : '',
            'Preview' : path if not os.path.isdir(path) else survey_prefix + '/static/static_vospace/images/fileicons/_Open.png',
            'Path' : path,
            'Error' : '',
            'Code' : 0,
            'Properties' : {
                    'Date Created' : '',
                    'Date Modified' : '',
                    'Width' : '',
                    'Height' : '',
                    'Size' : ''
                }
            }
        
        
        imagetypes = ['gif','jpg','jpeg','png']
        
        
        if not path_exists(path):
            thefile['Error'] = 'File does not exist.'
            return (json.dumps(thefile), None, 'application/json')
     
        
        if os.path.isdir(path):
            thefile['File Type'] = 'Directory'
        else:
            thefile['File Type'] = split_ext(path)[1].strip('.')
            if thefile['File Type'] in imagetypes:
                img = Image.open(path)
                img = img.size
                thefile['Properties']['Width'] = img[0]
                thefile['Properties']['Height'] = img[1]
                thefile['Preview'] = survey_prefix + '/static/static_vospace/userfiles/' + last_item
            else:
                previewPath = survey_prefix + '/static/static_vospace/images/fileicons/' + thefile['File Type'].upper() + '.png'
                thefile['Preview'] = previewPath if path_exists('../../' + previewPath) else survey_prefix + '/static/static_vospace/images/fileicons/default.png'
        

        thefile['Properties']['Date Created'] = time.ctime(os.path.getctime(path) )
        thefile['Properties']['Date Modified'] = time.ctime(os.path.getmtime(path)) 
        thefile['Properties']['Size'] = os.path.getsize(path)
        return thefile


    def getfolder(self, path=None, getsizes=True, req=None, **kwargs):
        
        if not self.isvalidrequest(path,req):
            return (self.patherror, None, 'application/json')

        result = {}     
        filtlist = os.listdir(path)
        for i in filtlist:
            if path[len(path)-1] == '/':
                complete_path = path + i
                if  os.path.isdir(complete_path ):
                    if complete_path [len(complete_path )-1] != '/':
                        result[i] = self.getinfo(complete_path  + '/', getsize=getsizes, req=req)
                    else :
                        result[i] = self.getinfo(complete_path , getsize=getsizes, req=req)   
                else:
                    result[i] = self.getinfo(complete_path , getsize=getsizes, req=req)   
            else :
                complete_path = path + '/' +  i

                if os.path.isdir(complete_path):
                    if complete_path[len(complete_path)-1] != '/':
                        result[i] = self.getinfo(complete_path + '/', getsize=getsizes, req=req)
                    else :
                        result[i] = self.getinfo(complete_path, getsize=getsizes, req=req)   
                else:
                    result[i] = self.getinfo(complete_path, getsize=getsizes, req=req)   
        req['content_type'] = 'application/json'
        req['result']= result
        return req
    
    
    def rename(self, old=None, new=None, req=None):
        
          
        if not self.isvalidrequest(path=old,req=req):
            return (self.patherror, None, 'application/json')
      
        if old[-1]=='/':
            old=old[:-1]
        
        
        path = str(old) 
        oldname = split_path(path)[-1]
        path = split_path(path)[0]
     
        
        if not path[-1]=='/':
            path += '/'
       
     
        newname = urllib.quote_plus(new)
        newpath = path + newname
        os.rename(old, newpath)
       
        
        result = {
            'Code' : 0,
            'Old Path' : old,
            'Old Name' : oldname,
            'New Path' : newpath,
            'New Name' : newname,
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

    