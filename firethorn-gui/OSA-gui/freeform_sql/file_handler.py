'''
file_handler module

Class with generic file handling functionality

Created on Nov 30, 2011

@author: stelios
'''

import os
from tempfile import mkstemp
from config import cur_static_dir


class File_handler:
    """
    Documentation for the File_handler class
    
    Class containing methods that allow easier temporary file handling for this application.
    """
    
    
    def __init__(self):
        """
        Initialise File handler class
        """
        self.file_init = True
        self.fd = False
        self.pathname = ""
        
    def get_file_name(self):
        return os.path.basename(self.pathname)
            
    def create_temp_file(self):
        """
        Create temporary file, and store the pathname and id as  class members
        @param self: self
        """
        try:
            
            self.fd, self.pathname = mkstemp(dir = cur_static_dir +'temp')
        except Exception as e:
            print e
          
    def write_to_temp_file(self, data):
        """
        Write the data to a temporary file
        @param data: The data to write
        """
        try:
            write_file = open(self.pathname, 'w')
            data = write_file.write(data)
            write_file.close()
        except Exception as e:
            print e

    def close_handle(self):
        """
        Close a temporary file 
        @param self: self
        """
        try:
            os.close(self.fd)
        except Exception as e:
            print e
        #os.remove(self.pathname)
      
