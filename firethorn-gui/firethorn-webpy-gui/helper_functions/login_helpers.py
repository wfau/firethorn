'''
Created on Sep 24, 2012

@author: stelios
'''

from config import create_menu_items
import urllib2

class login_helpers:
    """
    Function to provide assistance in handling login and session variables
    """
    
    def __init__(self,session):
        self.session = session
    
    
    def get_menu_items_by_permissions(self):
        
        html_content = ""
        role = self.session.get('role')
        
        for i in create_menu_items[role]:
            html_content+='<li><a href="create_new?obj_type=' +  urllib2.quote(i.encode("utf8")) + '" id="' + i + '"><span>' + i +'</span></a></li>'
        
        return html_content
    
    def get_log_notification(self):
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
        