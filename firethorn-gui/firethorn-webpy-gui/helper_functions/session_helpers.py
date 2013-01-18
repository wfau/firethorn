'''
Created on Sep 24, 2012

@author: stelios
'''

from config import create_menu_items
import urllib2
from helper_functions.string_functions import string_functions
from helper_functions import type_helpers
string_functions = string_functions()
import web

class session_helpers:
    """
    Function to provide assistance in handling login and session variables
    """
    
    def __init__(self, session=""):
        self.session = session if session != "" else web.ctx.session       
    
    
    def get_menu_items_by_permissions(self):
        
        html_content = ""
        role = self.session.get('role')
        
        for i in create_menu_items[role]:
            html_content+='<li><a href="create_new?obj_type=' +  string_functions.encode(i) + '" id="' + i + '"><span>' + i +'</span></a></li>'
        
        return html_content
    
    
    def get_log_notification(self):
        
        log_notification_true =""
        log_notification_false=""
        try:
            log_info = self.session.get('logged_in')
            username = self.session.get('user')
            log_notification_true = '<div id="log_notification">Logged in as:' + username +'</div>'
            log_notification_false = '<div id="log_notification">Not currently logged in</div>'
        except Exception:
            log_info = 'False'
            
        if log_info == 'True':
            return log_notification_true
        else :
            return log_notification_false
    
    
    def get_workspaces(self):

        workspaces =[]
        try:
            workspaces = self.session.get('workspaces')
        except Exception:
            workspaces = []
                    
        return workspaces
    
    
    def get_session_value(self, param):
        
        value=""
        try:
            value = self.session.get(param)
        except Exception:
            value = ""
                    
        return value
    
    
    def rename_session_value(self, ident, new, old, workspace, parent_folder,_type):
        error = -1
        ok = 1
        try:
            if workspace=="":
                return ok
            elif type_helpers.isRootType(_type):
                workspace_data = self.session.get('workspaces')
                for i in workspace_data:
                    if i["ident"] == ident and  i["name"] == old:
                        i["name"] = new
                        i["ident"] = new
                self.session.workspaces = workspace_data
                return ok
            elif parent_folder=="" :
                workspace_data = self.get_session_value(workspace)
                for i in workspace_data:
                    if i["ident"] == ident and i["name"] ==old:
                        i["name"] = new
                self.session[workspace] = workspace_data
                return ok
            else:
                workspace_data = self.get_session_value(workspace)
                for i in workspace_data:
                    if i["ident"] == parent_folder:
                        children_nodes = i["children"]
                        for x in children_nodes:
                            if x["ident"] == ident and  x["name"] == old:
                                x["name"] = new
                self.session[workspace] = workspace_data
                return ok
                 
        except Exception:
            return error
        
        return 1                

    
    def generate_workspace_selection(self, workspace):
        
        html_data = ""
        workspace_data = self.get_workspaces()
        if len(workspace_data)>0:
            html_data += '<select id="workspace_selection">'
            for i in workspace_data:
                if workspace==i["ident"]:
                    html_data += '<option value="'+ i["ident"] + '" selected="selected">' + i["name"] + '</option>'
                else :
                    html_data += '<option value="'+ i["ident"] + '">' + i["name"] + '</option>'
                    
            html_data += '</select>'
        return html_data


    def new_workspace(self, workspace_name , workspace):
        self.session.workspaces.append(workspace)
        self.session[workspace_name] = []
        