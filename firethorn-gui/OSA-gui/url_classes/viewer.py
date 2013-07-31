'''
Created on Sep 20, 2012

@author: stelios
'''
from config import mode_global, render, survey_prefix, taps_using_binary
import ast
import freeform_sql
import json
import web
from helper_functions.string_functions import string_functions
from numpy.core.defchararray import startswith
string_functions = string_functions()
from globals import logging

class viewer:
    """
    This class serves as an additional URL class, which takes a set of parameters, and allows users to create a plot based on the results of an ADQL/TAP query

    """

    def tryeval(self,val):
        try:
            val = ast.literal_eval(val)
        except ValueError:
            pass
        return val

    
    
    def GET(self):
        """ 
        GET function

        Return empty string
        """
        return self.POST()       
        
        
    def POST (self):
        """ 
        POST function
        
        Main Class functionality
        """
        
        data = web.input(results="", cols="",query="",xaxis="",yaxis="",tap_endpoint="",filepath="",x_alias="",y_alias="", mode="interactive", plot_type="Scatter", bins="10", save_as_div = "", inverted = "false")
        return_value=''
        web.header('Content-Type', 'text/html')
        _format = 'votable'
        query = string_functions.decode(data.query)
        if ((data.xaxis != "" and data.yaxis!="" and data.x_alias!="" and data.y_alias!="" and (data.plot_type=="Scatter" or data.plot_type=="Density" or data.plot_type=="Mixed")) or (data.xaxis != "" and data.x_alias!="" and data.plot_type=="Histogram")):           
            if (data.plot_type == "Scatter" or data.plot_type == "Density" or data.plot_type == "Mixed"):
                new_query = 'SELECT ' + data.xaxis + ' as ' + data.x_alias + ',' + data.yaxis + ' as ' + data.y_alias + ' FROM ('+ query + ') AS query ORDER BY ' + data.x_alias
            else:
                # Histogram was selected
                new_query = 'SELECT ' + data.xaxis + ' as ' + data.x_alias + ' FROM ('+ query + ') AS query ORDER BY ' + data.x_alias
        
        
            for i in taps_using_binary:
                if data.tap_endpoint.startswith(i):
                    _format = 'votable/td'

            votable,jobid, plot_file = freeform_sql.execute_async_query(data.tap_endpoint,mode_global,new_query,_format) #@UnusedVariable
            if votable!='':            
                if len(votable)>0:
                    try:
                        data_to_list = votable.data.tolist()     
                        if (data.plot_type=="Scatter" or data.plot_type == "Density" or data.plot_type == "Mixed"):
                            if (type (data_to_list[0][0]) is str) or (type (data_to_list[0][1]) is str):
                                data_to_list = [map (self.tryeval,x) for x in data_to_list]
                                if (type (data_to_list[0][0]) is str) or (type (data_to_list[0][1]) is str):
                                    raise Exception
                        else: 
                            if (type (data_to_list[0][0]) is str):
                                data_to_list = [map (self.tryeval,x) for x in data_to_list]
                                if (type (data_to_list[0][0]) is str):
                                    raise Exception
                                
                        data_results = [list(votable.columns)]
                        data_results.append(data_to_list)
                        
                        if data.mode=="static":    
                            plot = freeform_sql.Matplotlib_plots()
                            if (data.plot_type=="Scatter" or data.plot_type == "Density"):
                                plot_img = plot.generate_plot([[(x[0]) for x in data_to_list],[(x[1]) for x in data_to_list]],data.x_alias,data.y_alias,data.plot_type)
                            else :
                                plot_img = plot.generate_plot(data_to_list,data.x_alias,data.y_alias,data.plot_type, int(data.bins))  
                            data_results.append([plot_img])     
                        elif data.plot_type=="Histogram" :
                            plot = freeform_sql.Matplotlib_plots()
                            data_results.append(plot.get_xy_values_for_hist(data_to_list, int(data.bins)))
                        return_value = json.dumps(data_results)        
                        
                    except Exception as e:
                        logging.exception('Exception caught:')
                        return_value = "<div style=\"color:red;margin-left:50px;font-size:12px\">There was an error processing your request</div>"   
            else : 
                return_value = "<div style=\"color:red;margin-left:50px;font-size:12px\">There was an error processing your request</div>"    
        table_cols = data.cols
        
        if table_cols == None or table_cols == "":
            table_cols = 0
        
        if return_value=='':
       
            return render.viewer(table_cols,'value="'+ data.query + '"','value="'+ data.tap_endpoint + '"','value="'+ data.filepath + '"', 'value="' + freeform_sql.html_functions.escape(data.results) +'"', survey_prefix)   
        else :
            return return_value

