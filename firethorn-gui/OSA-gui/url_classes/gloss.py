'''
Created on Sep 20, 2012

@author: stelios
'''

from config import gloss_render





def gloss_parser(rendered_str, letter):
  
    
    remove_this2 = """<tr><th class='h'>Name</th><th class='h'>Schema Table</th><th class='h'>Database</th><th class='h'>Description</th><th class='h'>Type</th><th class='h'>Length</th><th class='h'>Unit</th><th class='h'>Default Value</th><th class='h'>Unified Content Descriptor</th></tr>"""
    
    with_this2 ="""<thead>
    <tr><th >Name</th><th >Schema Table</th><th >Database</th><th >Description</th><th >Type</th><th >Length</th><th >Unit</th><th >Default Value</th><th >Unified Content Descriptor</th></tr>
    </thead>
    """

    remove_this3 = "<tr><th class='h' colspan='9'><h3>" + letter +"</h3></th></tr>"

    temp_str = rendered_str.replace(remove_this2, with_this2 )
    temp_str2 = temp_str.replace(remove_this3,  "")
    temp_str3= temp_str2.replace('cellspacing=3 cellpadding=3',  'class="datatables"')
    temp_str4 = "<h3>" + letter +"</h3>" + temp_str3.replace("<h3>" + letter +"</h3>", "")
  
    return temp_str4
    

class gloss_a:
    def GET(self):
        """     
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "A"
        rendered_str = str(gloss_render.gloss_a())
        return gloss_parser(rendered_str, letter)
    
class gloss_b:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "B"
        rendered_str = str(gloss_render.gloss_b())
        return gloss_parser(rendered_str, letter)

class gloss_c:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "C"
        rendered_str = str(gloss_render.gloss_c())
        return gloss_parser(rendered_str, letter)
    
class gloss_d:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "D"
        rendered_str = str(gloss_render.gloss_d())
        return gloss_parser(rendered_str, letter)
    
class gloss_e:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "E"
        rendered_str = str(gloss_render.gloss_e())
        return gloss_parser(rendered_str, letter)
    
class gloss_f:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "F"
        rendered_str = str(gloss_render.gloss_f())
        return gloss_parser(rendered_str, letter)  

class gloss_g:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "G"
        rendered_str = str(gloss_render.gloss_g())
        return gloss_parser(rendered_str, letter)
    
class igloss_h:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "H"
        rendered_str = str(gloss_render.gloss_h())
        return gloss_parser(rendered_str, letter) 

class gloss_i:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "I"
        rendered_str = str(gloss_render.gloss_i())
        return gloss_parser(rendered_str, letter) 
     
class gloss_j:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "J"
        rendered_str = str(gloss_render.gloss_j())
        return gloss_parser(rendered_str, letter) 

class gloss_k:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "K"
        rendered_str = str(gloss_render.gloss_k())
        return gloss_parser(rendered_str, letter) 

    
class gloss_l:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "L"
        rendered_str = str(gloss_render.gloss_l())
        return gloss_parser(rendered_str, letter) 


class gloss_m:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "M"
        rendered_str = str(gloss_render.gloss_m())
        return gloss_parser(rendered_str, letter) 

    
class gloss_n:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "N"
        rendered_str = str(gloss_render.gloss_n())
        return gloss_parser(rendered_str, letter) 


class gloss_o:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "O"
        rendered_str = str(gloss_render.gloss_o())
        return gloss_parser(rendered_str, letter) 

    
class gloss_p:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "P"
        rendered_str = str(gloss_render.gloss_p())
        return gloss_parser(rendered_str, letter) 



class gloss_q:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "Q"
        rendered_str = str(gloss_render.gloss_q())
        return gloss_parser(rendered_str, letter) 

       
class gloss_r:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "R"
        rendered_str = str(gloss_render.gloss_r())
        return gloss_parser(rendered_str, letter) 

       
class gloss_s:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "S"
        rendered_str = str(gloss_render.gloss_s())
        return gloss_parser(rendered_str, letter) 
 
                     
class gloss_t:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "T"
        rendered_str = str(gloss_render.gloss_t())
        return gloss_parser(rendered_str, letter) 

   
class gloss_u:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "U"
        rendered_str = str(gloss_render.gloss_u())
        return gloss_parser(rendered_str, letter) 

     
class gloss_v:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "V"
        rendered_str = str(gloss_render.gloss_v())
        return gloss_parser(rendered_str, letter) 

       
class gloss_w:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "W"
        rendered_str = str(gloss_render.gloss_w())
        return gloss_parser(rendered_str, letter) 

              
class gloss_x:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "X"
        rendered_str = str(gloss_render.gloss_x())
        return gloss_parser(rendered_str, letter) 

    
class gloss_y:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "Y"
        rendered_str = str(gloss_render.gloss_y())
        return gloss_parser(rendered_str, letter) 

    
class gloss_z:
    def GET(self):
        """ 
        GET function 
        
        Handle an HTTP GET request
        """
        letter = "Z"
        rendered_str = str(gloss_render.gloss_z())
        return gloss_parser(rendered_str, letter) 
