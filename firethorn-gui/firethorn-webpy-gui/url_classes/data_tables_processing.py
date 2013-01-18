'''
Created on Sep 20, 2012

@author: stelios
'''
from helper_functions import fourPn
import atpy
import freeform_sql
import json
import operator
import re
import traceback
import web
import urllib2
from config import host_temp_directory
import urllib
import urlparse

class data_tables_processing:
    """
    URL Class to handle the processing of JQuery Datatable data
    """
    
    
    def custom_filter (self, func, data, indexes=[]):
        """
        Custom filter function used to additionally calculate the index values of the list elements that pass the filter
        """
        final_list = []
        counter = 0
        new_index_list = []
        
        
        
        for i in data:
            if func(i)==True:
                final_list.append(i)
                new_index_list.append(counter)
            counter = counter + 1
        return (final_list,new_index_list)
    
    
    def getRowIndexList(self, jsn_encoded_stored_data, data, sort_col, sort_order,search_rexp,_broadcast_url):
        """
        Get the index of a row 
        """
        
        
      
        index = -1
        
        rows = jsn_encoded_stored_data[1]
        counter = 0
        
        if _broadcast_url!="" and _broadcast_url!=None:
            votable =  atpy.Table(_broadcast_url, type='vo')
        if votable!='' and votable !="MAX_ERROR":
            cols = list(votable.columns)
            lst = []
            
            for x in votable.data.tolist():
                second_counter = 0
                obj_list = {}
                for y in x:
                    obj_list[cols[second_counter]] = y 
                    second_counter = second_counter + 1
                lst.append(obj_list)
            rows = lst 
       
        """ 
        if search_rexp!="":
            rows,index_list = self.custom_filter(lambda x: self.search(x,str(search_rexp)), rows)
            
       
        if sort_col!="":
            
            if sort_order!="" and (sort_order=='asc' or sort_order == 'desc'):
                if sort_order=='asc':
                    rows = sorted(rows, key=lambda x: x[sort_col])
                else :
                    rows = sorted(rows, key=lambda x: x[sort_col], reverse=True)
            else:    
                rows = sorted(rows, key=lambda x: x[sort_col])
        """
        
        for i in rows:
            index = counter
        
            for col in i:
                if isinstance( i[col], ( int, long, float ) ):
                    
                    if abs(i[col]- float(getattr(data, 'col_' + col)))>0.0001:
                        index=-1
                
                elif i[col] != getattr(data, 'col_' + col):
                    index=-1
            
            
            if index!=-1:
                break

            counter = counter + 1
            
        return index
    
    def  column_search_math_expr_multiple_points(self, values, points, cols, expressions):
        """
        Search a list of points based on columns and keys which are derived as mathematical expressions based on the row values for the given columns 
        """
        counter = 0
        result = False
        vars_={} 
        
        for x in cols:
            vars_[x.lower()] = values[x]
        
        arith = fourPn.Arith( vars_ )
   
       
        if len(points)>1:
            for i in points[0]:
         
                if result != True:
                    if (abs(abs(arith.eval(expressions[0].lower()))-abs(float(i)))<0.001 and abs(abs(arith.eval(expressions[1].lower()))-abs(float(points[1][counter])))<0.001 ):
                        result = True
                counter = counter + 1
       
        return result
    
    def column_search_math_expr(self, values, points, cols):
        """
        
        Search the values for the rexp at the col_num index 
        
        @param values: Values to cast
        @param points: List of points
        @param cols: Columns
        @return: Formatted values
        """
        
        result = False
        vars_ = {} 
        
        for x in cols:
            vars_[x.lower()] = values[x]
        
        arith = fourPn.Arith( vars_ )
  
        if len(points)==2:
            
            result = (abs(abs(arith.eval(points[0][0].lower()))-abs(float(points[0][1])))<0.001) and (abs(abs(arith.eval(points[1][0].lower()))-abs(float(points[1][1])))<0.001)
                
        return result
    
    def multi_column_key_specific_search(self, values, rexps, col_names):
        """
        Search the values for the rexp at the col_num index 
        
        @param values: Values to cast
        @param rexps: Regular expressions
        @param col_names: Column names
        @return: Formatted values
        """
        
        counter = 0
        result = False
      
        for i in rexps:
            if result != True:
                result = (float(rexps[counter][0]) == float(repr(values[col_names[0]])) and float(rexps[counter][1]) == float(repr(values[col_names[1]])))
            counter = counter + 1
        
        return result
    
    def column_specific_search(self,values, rexp,col_name):
        """
        
        Search the values for the rexp at the col_num index 
        
        @param values: Values to cast
        @param rexp: Regular expression
        @param col_name: Column name
        @return: Formatted values
        """

       
        result =  float(rexp) == float(repr(values[col_name]))
        return result
    
    
    def column_search_multiple_keys(self, values, rexps, col_name):
        """
        
        Search the values for the rexp at the col_num index 
        
        @param values: Values to cast
        @param rexps: Regular expressions
        @param col_name: Column name
        @return: Formatted values
        """
        result = False
        
        for i in rexps:
            if result != True:
                result = float(i[1]) == float(repr(values[col_name]))
        return result
     
     
    def search(self,values, rexp):
        """
        Float values are converted to strings with two decimals.
    
        Search the values for the rexp
    
        @param values: Values to cast
        @return: Formated values
        """
  
        for v in values:
            if re.search(rexp,repr(values[v])):
                return True
        return False
    
    
    def filter_cols_math_expr(self, lst, sort_pairs, mDataProps = {}, x_expr="",y_expr="", rexp = "", sidx = 0, start=1, rows=10, sord="asc", echo=0, save_as = "", _broadcast_url ="" ):
        """
        Takes a list of parameters used for [JS:dataTables] table processing, and returns a filtered list based on multiple filter columns and a mathematical expression used to calculate the key values
        """
        data = lst[1]
        points = []
        cols = []
        items = []
        expressions = []
        sidx = int(sidx)
        start = int(start)
        rows = int(rows)
        echo = int(echo) 
        index_list = ""

        for pair in sort_pairs:
            filter_num = pair[0]
            cols.append(mDataProps["mDataProp_" + str(filter_num)])
      
     
        if len(sort_pairs)>0:
            counter = 0
            initial_data_split = sort_pairs[0][1].split('_')
           
            original_broadcast_table = data
        
            if _broadcast_url!="" and _broadcast_url!="undefined" and _broadcast_url!=None:
                votable =  atpy.Table(_broadcast_url, type='vo')
                if votable!='' and votable !="MAX_ERROR":
                    cols = list(votable.columns)
                    lst = []
                    
                    for x in votable.data.tolist():
                        second_counter = 0
                        obj_list = {}
                        for y in x:
                            obj_list[cols[second_counter]] = y 
                            second_counter = second_counter + 1
                        lst.append(obj_list)
                    original_broadcast_table = lst
                    
            for x in initial_data_split:
                items.append(x)
        
            
            if len(items)>1:
               
                for i in items:
                
                    temp_holder = []
                    for y in i.split('|'):
                        temp_holder.append(y)
                    points.append(temp_holder)
                    
                expressions = [x_expr,y_expr]
                
                data,index_list = self.custom_filter(lambda x: self.column_search_math_expr_multiple_points(x, points, cols, expressions), original_broadcast_table)
              
            else:
                for i in items[0].split('|'):
                    if counter==0:
                        points.append((x_expr,i))
                    elif counter==1:
                        points.append((y_expr,i))
                    counter = counter + 1
                data,index_list = self.custom_filter(lambda x: self.column_search_math_expr(x, points, cols), original_broadcast_table)
               
        if rexp!="" and rexp !=None:
            data = filter(lambda x: self.search(x,str(rexp)), data)
       
        r = True if sord == "desc" else False
        
        if sidx!=-1:
            temp_key = "mDataProp_" + str(sidx)
            s = sorted( data, key=operator.itemgetter(mDataProps[temp_key]), reverse=r )
        else:
            s = data
        
        if save_as =="" or save_as ==None :
            maxim = len(s)
            ff = start if start else 0
            tt = ff+rows if ff+rows < maxim else maxim
            f = s[ff:tt]   
        else :
            f = s
        # format data
        j = {
           
            "iTotalRecords" : str(len(data)),
            "iTotalDisplayRecords" : str(len(data)),
            "aaData": f,
            "index_list" : index_list
            }
        
        return j
        
    def filter_single_col_multiple_keys(self, lst, sort_pairs, mDataProps = {}, rexp = "", sidx = 0, start=1, rows=10, sord="asc", echo=0, save_as = "", _broadcast_url="" ):
        """
        Takes a list of parameters used for [JS:dataTables] table processing, and returns a filtered list based on a single filter column and multiple filter keys
        """
        data = lst[1]
        points = []
        counter = 0
        mDataProp_keys = []
        sidx = int(sidx)
        start = int(start)
        rows = int(rows)
        echo = int(echo)        
        index_list = ""

        for pair in sort_pairs:
            filter_col = pair[0]
            second_counter = 0
            
            for i in pair[1].split('|'):
                points.append((filter_col,i))
                second_counter = second_counter + 1
            mDataProp_keys.append(mDataProps["mDataProp_" + str(filter_col)])
            counter = counter + 1

        original_broadcast_table = data
        
        if _broadcast_url!="" and _broadcast_url!="undefined" and _broadcast_url!=None :
           
            votable =  atpy.Table(_broadcast_url, type='vo')
            if votable!='' and votable !="MAX_ERROR":
                cols = list(votable.columns)
                lst = []
                
                for x in votable.data.tolist():
                    second_counter = 0
                    obj_list = {}
                    for y in x:
                        obj_list[cols[second_counter]] = y 
                        second_counter = second_counter + 1
                    lst.append(obj_list)
                original_broadcast_table = lst
        
        data,index_list = self.custom_filter(lambda x: self.column_search_multiple_keys(x, points, mDataProp_keys[0]), original_broadcast_table)
        
        if rexp!="" and rexp !=None:
            data = filter(lambda x: self.search(x,str(rexp)), data)
   
        r = True if sord == "desc" else False
        
        if sidx!=-1:
            temp_key = "mDataProp_" + str(sidx)
            s = sorted( data, key=operator.itemgetter(mDataProps[temp_key]), reverse=r )
        else:
            s = data

        
        if save_as =="" or save_as==None:
            maxim = len(s)
            ff = start if start else 0
            tt = ff+rows if ff+rows < maxim else maxim
            f = s[ff:tt]
        else :
            f = s       
        # format data
        j = {
           
            "iTotalRecords" : str(len(data)),
            "iTotalDisplayRecords" : str(len(data)),
            "aaData": f,
            "index_list" : index_list
            }
        
        return j
    
    
    def filter_multiple_cols_and_keys(self, lst, sort_pairs, mDataProps = {}, rexp = "", sidx = 0, start=1, rows=10, sord="asc", echo=0, save_as = "", _broadcast_url = "" ):
        """
        Takes a list of parameters used for [JS:dataTables] table processing, and returns a filtered list based on multiple filter columns and keys
        """
        data = lst[1]
        sidx = int(sidx)
        start = int(start)
        rows = int(rows)
        echo = int(echo)
        filter_vals = []
        points = []
        counter = 0
        mDataProp_keys = []
        index_list = ""
                    
        for pair in sort_pairs:
            filter_col = pair[0]
            second_counter = 0
            
            for i in pair[1].split('|'):
                if counter>0:
                    points.append((filter_vals[second_counter],i))
                else:
                    filter_vals.append(i)
                second_counter = second_counter + 1
            mDataProp_keys.append(mDataProps["mDataProp_" + str(filter_col)])
            counter = counter + 1
        
        original_broadcast_table = data
        
        if _broadcast_url!="" and _broadcast_url!="undefined" and _broadcast_url!=None:
           
            votable =  atpy.Table(_broadcast_url, type='vo')
            if votable!='' and votable !="MAX_ERROR":
                cols = list(votable.columns)
                lst = []
                
                for x in votable.data.tolist():
                    second_counter = 0
                    obj_list = {}
                    for y in x:
                        obj_list[cols[second_counter]] = y 
                        second_counter = second_counter + 1
                    lst.append(obj_list)
                original_broadcast_table = lst
                
        data,index_list = self.custom_filter(lambda x: self.multi_column_key_specific_search(x, points, mDataProp_keys), original_broadcast_table)
        
        
        if rexp!="" and rexp !=None:
            data = filter(lambda x: self.search(x,str(rexp)), data)
   
       
        r = True if sord == "desc" else False
        
        if sidx!=-1:
            temp_key = "mDataProp_" + str(sidx)
            s = sorted( data, key=operator.itemgetter(mDataProps[temp_key]), reverse=r )
        else:
            s = data
        
        if save_as =="" or save_as==None:
            maxim = len(s)
            ff = start if start else 0
            tt = ff+rows if ff+rows < maxim else maxim
            f = s[ff:tt]
        else :
            f = s       
        
        # format data
        j = {
           
            "iTotalRecords" : str(len(data)),
            "iTotalDisplayRecords" : str(len(data)),
            "aaData": f,
            "index_list" : index_list
            }
        
        return j


    def filter_multiple_cols (self, lst, sort_pairs, mDataProps = {}, sidx = 0, start=1, rows=10, sord="asc", echo=0, save_as = "", _broadcast_url="" ):
        """
        Takes a list of parameters used for [JS:dataTables] table processing, and returns a filtered list based on multiple filter columns
        """
        data = lst[1]
        sidx = int(sidx)
        start = int(start)
        rows = int(rows)
        echo = int(echo)
        index_list = []

        original_broadcast_table = data
     
        if _broadcast_url!="" and _broadcast_url!="undefined" and  _broadcast_url!=None:
           
            votable =  atpy.Table(_broadcast_url, type='vo')
            if votable!='' and votable !="MAX_ERROR":
                cols = list(votable.columns)
                lst = []
                
                for x in votable.data.tolist():
                    second_counter = 0
                    obj_list = {}
                    for y in x:
                        obj_list[cols[second_counter]] = y 
                        second_counter = second_counter + 1
                    lst.append(obj_list)
                original_broadcast_table = lst
        
        for pair in sort_pairs:
            filter_col = pair[0]
            filter_val = pair[1]
            temp_key = "mDataProp_" + str(filter_col)
            data,index_list = self.custom_filter(lambda x: self.column_specific_search(x,str(filter_val), mDataProps[temp_key]), original_broadcast_table, index_list)
            
        r = True if sord == "desc" else False
        
        if sidx!=-1:
            temp_key = "mDataProp_" + str(sidx)
            s = sorted( data, key=operator.itemgetter(mDataProps[temp_key]), reverse=r )
        else:
            s = data
        
        if save_as =="" or save_as==None:
            maxim = len(s)
            ff = start if start else 0
            tt = ff+rows if ff+rows < maxim else maxim
            f = s[ff:tt]
        else :
            f = s       
           
           
        # format data
        j = {
           
            "iTotalRecords" : str(len(data)),
            "iTotalDisplayRecords" : str(len(data)),
            "aaData": f,
            "index_list" : index_list
            }
        
        
        return j


    def filter_cols (self, lst, filter_val, filter_col, mDataProps = {}, sidx = 0, start=1, rows=10, sord="asc", echo=0, save_as = "", _broadcast_url = "" ):
        """
        Takes a list of parameters used for [JS:dataTables] table processing, and returns a filtered list based on a filter value and column
        """
        sidx = int(sidx)
        start = int(start)
        rows = int(rows)
        echo = int(echo)
        index_list = ""

        temp_key = "mDataProp_" + str(filter_col)
        original_broadcast_table = lst[1]
        
        if _broadcast_url!="" and _broadcast_url!="undefined" and _broadcast_url!=None:
           
            votable =  atpy.Table(_broadcast_url, type='vo')
            if votable!='' and votable !="MAX_ERROR":
                cols = list(votable.columns)
                lst = []
                
                for x in votable.data.tolist():
                    second_counter = 0
                    obj_list = {}
                    for y in x:
                        obj_list[cols[second_counter]] = y 
                        second_counter = second_counter + 1
                    lst.append(obj_list)
                original_broadcast_table = lst
                 
        data,index_list = self.custom_filter(lambda x: self.column_specific_search(x,str(filter_val), mDataProps[temp_key]), original_broadcast_table)
        
        
        r = True if sord == "desc" else False
        
        if sidx!=-1:
            temp_key = "mDataProp_" + str(sidx)
            s = sorted( data, key=operator.itemgetter(mDataProps[temp_key]), reverse=r )
        else:
            s = data
        
        if save_as =="" or save_as==None:
            maxim = len(s)
            ff = start if start else 0
            tt = ff+rows if ff+rows < maxim else maxim
            f = s[ff:tt]
        else :
            f = s       
        
        # format data
        j = {
           
            "iTotalRecords" : str(len(data)),
            "iTotalDisplayRecords" : str(len(data)),
            "aaData": f,
            "index_list" : index_list
            }
        
        return j


    def process( self, lst, sidx, start=1, rows=10, sord="asc", echo=0, rexp=None, pfn=None,mDataProps = {}, save_as = "",_broadcast_url="" ):
        """ 
        Process a request with JQuery Datatables data and parameters, and filter/return the appropriate data
        
        @param sidx : Column used to sort data
        @param start: Data chunck/page number (default: 1)
        @param rows : How many values do we display (default: 50)
        @param sord : Sort order, set to desc for reversed (default: True)
        @param echo : Simple internal iterator used by datatables for rendering
        @param rexp:  An expression to Filter the results by
        @param pfn  : Pickle file name -- only used if PICKLE = True (default: None)
        @return:None
        """
    
        try :
            data=lst[1]   
            sidx = int(sidx)
            start = int(start)
            rows = int(rows)
            echo = int(echo)
            index_list = ""
            
            original_broadcast_table = data
            if _broadcast_url!="" and _broadcast_url!="undefined" and _broadcast_url!=None:
                
                votable =  atpy.Table(_broadcast_url, type='vo')
                if votable!='' and votable !="MAX_ERROR":
                    cols = list(votable.columns)
                    lst = []
                    
                    for x in votable.data.tolist():
                        second_counter = 0
                        obj_list = {}
                        for y in x:
                            obj_list[cols[second_counter]] = y 
                            second_counter = second_counter + 1
                        lst.append(obj_list)
                    original_broadcast_table = lst 
                    
            # filter data (search case)
            if rexp != '' and rexp!=None: # rexp is casted to str
                data,index_list = self.custom_filter(lambda x: self.search(x,str(rexp)), original_broadcast_table)
           
            r = True if sord == "desc" else False
            if sidx!=-1:
                temp_key = "mDataProp_" + str(sidx)
                s = sorted( data, key=operator.itemgetter(mDataProps[temp_key]), reverse=r )
            else:
                s = data
                
            if save_as =="" or save_as == None:
                maxim = len(s)
                ff = start if start else 0
                tt = ff+rows if ff+rows < maxim else maxim
                f = s[ff:tt]
            else :
                f = s    
                   
            # format data
            j = {
               
                "iTotalRecords" : str(len(s)),
                "iTotalDisplayRecords" : str(len(s)),
                "aaData": f,
                "index_list" : index_list
                }
            
        except Exception:
            traceback.print_exc()     
            j = None
        return j

    
    def save_as_temp_file(self, table):
        """
        Get the url of a generated table via dataTables processing
        Read url and store it in temporary file
        
        """
        return_val = ""
        try:
            #parsed = urlparse.urlparse(broadcast_url)
            #parsed_params =  urlparse.parse_qs(parsed.query)
            #final_params = {}
            ##for i in parsed_params:
            #    final_params[i] = parsed_params[i][0]
            #req = urllib2.Request(broadcast_url, urllib.urlencode(final_params))
            #req.add_header("Content-type", "application/x-www-form-urlencoded")
            #f = urllib2.urlopen(req)
            #table = f.read()
            file_handle = freeform_sql.file_handler.File_handler()
            file_handle.create_temp_file()
            file_handle.write_to_temp_file(table)
            filename = file_handle.get_file_name()
            file_handle.close_handle()    
            return_val = host_temp_directory   +  filename
        
        except Exception as e:
            traceback.print_exc()
            
        return return_val
                
                
    def GET(self):
        """
        GET function
        
        Take a pathname as a parameter, and generate the appropriate row data for use by jQuery Datatables
        """
        
        data = web.input(_action="process", _broadcast_url = "", _sSearch="", _sSortDir_0 ="" ,_get_sort_col ="", getRowIndexes = "false", pathname="", rexp = "" , iSortCol_0=-1,iDisplayStart=1, iDisplayLength=10, sSortDir_0="asc", sEcho=0, sSearch=None, pfn=None, y_expr="", x_expr="", save_as = "")
        return_val = ""
        f = ''
        col_to_sort = ""
        sort_val = ""
        mDataProps = {} 
        sort_pairs = []
        is_math_expr = False
             
        for key in data:    
            
            if key=='x_expr' or key=='y_expr':
                x_expr = data.x_expr
                y_expr = data.y_expr
                if x_expr!="" and y_expr !="" and x_expr!=None and y_expr !=None  :
                    is_math_expr = True
            if key.startswith('sSearch_'):
                temp_str = getattr(data, key)
                if temp_str != "" and temp_str != None:
                    col_to_sort = key[key.rfind('_')+1:len(key)] 
                    sort_val = temp_str 
                    sort_pairs.append([col_to_sort, sort_val])
            elif key.startswith('mDataProp_'):
                mDataProps[key] = data[key]
       
        try:       
            
                f = open(data.pathname, 'r')  
                val = f.read()
                jsn_encoded_stored_data = json.loads(val)
                sidx = data.iSortCol_0
                start = data.iDisplayStart
                rows = data.iDisplayLength
                sord = data.sSortDir_0
                echo = data.sEcho
                rexp = data.sSearch
                pfn = data.pfn
                save_as = data.save_as
                getRowIndexes = data.getRowIndexes
                sort_col = data._get_sort_col
                return_dict = {}
                sort_order =data._sSortDir_0
                search_rexp = data._sSearch
                _broadcast_url = data._broadcast_url
                
                if sort_col!="" and sort_col!=None :
                    sort_col = mDataProps["mDataProp_" + sort_col]
                
                
                if getRowIndexes =="true":
                    return_index = data_tables_processing.getRowIndexList(self, jsn_encoded_stored_data, data, sort_col,sort_order,search_rexp, _broadcast_url)
                    return_val = return_index
                elif is_math_expr and sort_pairs!=[]:
                    return_dict = data_tables_processing.filter_cols_math_expr(self, jsn_encoded_stored_data, sort_pairs , mDataProps = mDataProps, rexp = rexp, x_expr=x_expr, y_expr = y_expr,sidx=sidx, start=start, rows=rows, sord=sord, echo=echo,save_as = save_as)
                    return_val = json.dumps(return_dict)
                elif len(sort_pairs)>1:
                    if sort_pairs[0][1].find('|')>0 and sort_pairs[1][1].find('|')>0:
                        return_dict = data_tables_processing.filter_multiple_cols_and_keys(self, jsn_encoded_stored_data, sort_pairs , mDataProps = mDataProps, rexp = rexp,sidx=sidx, start=start, rows=rows, sord=sord, echo=echo,save_as = save_as)
                        return_val = json.dumps(return_dict)                      
                    else:
                        return_dict = data_tables_processing.filter_multiple_cols(self, jsn_encoded_stored_data, sort_pairs , mDataProps = mDataProps,sidx=sidx, start=start, rows=rows, sord=sord, echo=echo,save_as = save_as)
                        return_val = json.dumps(return_dict)
                elif len(sort_pairs)==1 and sort_pairs[0][1].find('|')>0: 
                    return_dict = data_tables_processing.filter_single_col_multiple_keys(self, jsn_encoded_stored_data, sort_pairs ,mDataProps = mDataProps, rexp = rexp,sidx=sidx, start=start, rows=rows, sord=sord, echo=echo,save_as = save_as)
                    return_val = json.dumps(return_dict)                      
                elif sort_val != "" and col_to_sort !="" and rexp=="" and sort_val != None and col_to_sort !=None and rexp==None:
                    return_dict = data_tables_processing.filter_cols(self, jsn_encoded_stored_data, sort_val, col_to_sort,mDataProps = mDataProps,sidx=sidx, start=start, rows=rows, sord=sord, echo=echo,save_as = save_as)
                    return_val = json.dumps(return_dict)
                else:
                    return_dict = data_tables_processing.process(self, jsn_encoded_stored_data, sidx=sidx, start=start, rows=rows, sord=sord, echo=echo, rexp=rexp, pfn=pfn,mDataProps = mDataProps,save_as = save_as)
                    return_val = json.dumps(return_dict) 
                
                
                if save_as != "" and save_as != None:
                    web.header('Content-Type', 'multipart/form-data')
                    if save_as=='html':
                        web.header('Content-disposition', 'attachment; filename=download.html')
                    elif save_as=='vo':
                        web.header('Content-disposition', 'attachment; filename=download.xml')
                    elif save_as=='fits':    
                        web.header('Content-disposition', 'attachment; filename=download.fits')
                    elif save_as=='csv':    
                        web.header('Content-disposition', 'attachment; filename=download.csv')
                    if jsn_encoded_stored_data !="" and jsn_encoded_stored_data !=None:
                        lst = []
                        lst.append(jsn_encoded_stored_data[0])
                        lst.append(return_dict['aaData'])
                        return_val = freeform_sql.write_format_from_json(save_as,lst )
                    else:
                        return_val = "No results returned"
                
                if f!='':
                    f.close()
           
        except Exception as e:
            print traceback.print_exc()
            if f!='':
                f.close()
            return_val = "" 
        
    
        return return_val

        
    def POST(self):
        """
        POST function
        
        Take a pathname as a parameter, and generate the appropriate row data for use by jQuery Datatables
        """
        
        data = web.input(_action = "process", _broadcast_url = "", _sSearch="", _sSortDir_0 ="" ,_get_sort_col ="", getRowIndexes = "false", pathname="", rexp = "" , iSortCol_0=-1,iDisplayStart=1, iDisplayLength=10, sSortDir_0="asc", sEcho=0, sSearch=None, pfn=None, y_expr="", x_expr="", save_as = "")
        return_val = ""
        f = ''
        col_to_sort = ""
        sort_val = ""
        mDataProps = {} 
        sort_pairs = []
        is_math_expr = False
        if data._action == "save_as_temp_file":
            qs = urlparse.parse_qs(data._broadcast_url)
            for i in urlparse.parse_qs(data._broadcast_url):
                data[i] = qs[i][0]
            data["_broadcast_url"] = ""
            
            
        for key in data:    
            if key=='x_expr' or key=='y_expr':
                x_expr = data.x_expr
                y_expr = data.y_expr
                if x_expr!="" and y_expr !="" and x_expr!=None and y_expr !=None:
                    is_math_expr = True
            if key.startswith('sSearch_'):
                temp_str = getattr(data, key)
                if temp_str != "" and temp_str != None:
                    col_to_sort = key[key.rfind('_')+1:len(key)] 
                    sort_val = temp_str 
                    sort_pairs.append([col_to_sort, sort_val])
            elif key.startswith('mDataProp_'):
                mDataProps[key] = data[key]
       
        try:     
                f = open(data.pathname, 'r')  
                val = f.read()    
                jsn = json.loads(val)
                sidx = data.iSortCol_0
                start = data.iDisplayStart
                
                if data._action == "save_as_temp_file":            
                    rows = len(jsn[1]) 
                else :
                    rows = data.iDisplayLength
                sord = data.sSortDir_0
                echo = data.sEcho
                rexp = data.sSearch
                pfn = data.pfn
                save_as = data.save_as
                getRowIndexes = data.getRowIndexes
                sort_col = data._get_sort_col
                return_dict = {}
                sort_order =data._sSortDir_0
                search_rexp = data._sSearch
                _broadcast_url = data._broadcast_url
                               
                if sort_col!="" and sort_col!=None:
                    sort_col = mDataProps["mDataProp_" + sort_col]
 
                if getRowIndexes =="true":
                    return_index = data_tables_processing.getRowIndexList(self, jsn, data, sort_col,sort_order,search_rexp, _broadcast_url)
                    return_val = return_index
                elif is_math_expr and sort_pairs!=[]:
                    return_dict = data_tables_processing.filter_cols_math_expr(self, jsn, sort_pairs , mDataProps = mDataProps, rexp = rexp, x_expr=x_expr, y_expr = y_expr,sidx=sidx, start=start, rows=rows, sord=sord, echo=echo,save_as = save_as, _broadcast_url=_broadcast_url)
                    return_val = json.dumps(return_dict)
                elif len(sort_pairs)>1:

                    if sort_pairs[0][1].find('|')>0 and sort_pairs[1][1].find('|')>0:
                        return_dict = data_tables_processing.filter_multiple_cols_and_keys(self, jsn, sort_pairs , mDataProps = mDataProps, rexp = rexp,sidx=sidx, start=start, rows=rows, sord=sord, echo=echo,save_as = save_as, _broadcast_url=_broadcast_url)
                        return_val = json.dumps(return_dict)                      
                    else:
                        return_dict = data_tables_processing.filter_multiple_cols(self, jsn, sort_pairs , mDataProps = mDataProps,sidx=sidx, start=start, rows=rows, sord=sord, echo=echo,save_as = save_as, _broadcast_url=_broadcast_url)
                        return_val = json.dumps(return_dict)
                elif len(sort_pairs)==1 and sort_pairs[0][1].find('|')>0: 
                    return_dict = data_tables_processing.filter_single_col_multiple_keys(self, jsn, sort_pairs ,mDataProps = mDataProps, rexp = rexp,sidx=sidx, start=start, rows=rows, sord=sord, echo=echo,save_as = save_as, _broadcast_url=_broadcast_url)
                    return_val = json.dumps(return_dict)                      
                elif sort_val != "" and col_to_sort !="" and rexp=="" and sort_val != None and col_to_sort !=None and rexp==None:
                    return_dict = data_tables_processing.filter_cols(self, jsn, sort_val, col_to_sort,mDataProps = mDataProps,sidx=sidx, start=start, rows=rows, sord=sord, echo=echo,save_as = save_as, _broadcast_url=_broadcast_url)
                    return_val = json.dumps(return_dict)
                else:

                    return_dict = data_tables_processing.process(self, jsn, sidx=sidx, start=start, rows=rows, sord=sord, echo=echo, rexp=rexp, pfn=pfn,mDataProps = mDataProps,save_as = save_as, _broadcast_url=_broadcast_url)
                    return_val = json.dumps(return_dict) 
            
                if data._action == "save_as_temp_file":
                    #print return_dict  
                    lst = []
                    lst.append(jsn[0])
                    lst.append(return_dict['aaData'])
                    return_val = freeform_sql.write_format_from_json('vo',lst )
                    return self.save_as_temp_file(return_val)
                
                if save_as != "" and save_as != None:
                    web.header('Content-Type', 'multipart/form-data')
                    if save_as=='html':
                        web.header('Content-disposition', 'attachment; filename=download.html')
                    elif save_as=='vo':
                        web.header('Content-disposition', 'attachment; filename=download.xml')
                    elif save_as=='fits':    
                        web.header('Content-disposition', 'attachment; filename=download.fits')
                    elif save_as=='csv':    
                        web.header('Content-disposition', 'attachment; filename=download.csv')
                    if jsn !="" and jsn!=None:
                        lst = []
                        lst.append(jsn[0])
                        lst.append(return_dict['aaData'])
                        return_val = freeform_sql.write_format_from_json(save_as,lst )
                    else:
                        return_val = "No results returned"
                
                if f!='':
                    f.close()
           
        except Exception as e:
            traceback.print_exc()
            if f!='':
                f.close()
            print e
            return_val = "" 
        
       
        return return_val

