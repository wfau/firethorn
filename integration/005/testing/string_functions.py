'''
Created on Nov 7, 2012

@author: stelios
'''
from urllib import quote_plus, unquote, unquote_plus, string

class string_functions:
   
    
    def encode(self, _string):
        if _string!=None:
            return quote_plus(_string.encode("utf8"))
        else:
            return None
        
    def decode(self, _string):
        if _string!=None:
            return unquote_plus(_string.encode("utf8")).decode("utf8")
        else:
            return None

