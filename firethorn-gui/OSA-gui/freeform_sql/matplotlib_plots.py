'''
matplotlib_plots

Class responsible for handling Matplotlib functionality
Created on Nov 30, 2011

@author: stelios
'''

import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import numpy
import base64 
import StringIO
import urllib
from file_handler import File_handler
import json
from freeform_config import publicly_visible_temp_dir, host_temp_directory

class Matplotlib_plots:
    """
    Contains the methods that allow the creation of matplotlib scatter or histogram plots.
    
    """
    
    
    def __init__(self):
        """
        Initialize class. Temporarily using a placeholder variable for future use
        @param self : self
        """
        self.scatter = 1
        
        
    def get_xy_values_for_hist(self, x_data, x_bins=10):
        """
        Generate the X and Y data values for a histogram, based on a given number of bins
        
        @param x_data: The x axis data values
        @param x_bins : The number of bins based on which the data columns will be generated. Defaults to: 10
        @return: An array of x,y points for use by a histogram
        """
        x = numpy.array(x_data)
        n, bins, patches = plt.hist(x,bins = x_bins,histtype='bar')
        new = []
        range_array=[]
        plt.clf()
        n = n.tolist()
        bins = bins.tolist()
        counter = 0
        
        for val in bins :
            if counter<len(bins)-2:
                range_array.append('>= ' + str(bins[counter]) + '<br />< ' + str(bins[counter+1]))
            elif counter==len(bins)-2:      
                range_array.append('>= ' + str(bins[counter]) + ' <br /><= ' + str(bins[counter+1]))
            counter = counter+1    
        
        return  [range_array,n]   
        
        
    def generate_plot(self,data, x_label="", y_label="", plot_type="Scatter", x_bins=10):
        """
        Generate a plot using Matplotlib. Type of plot can be either a Scatter plot or a Histogram.
        Returns the image as a png, encoded in base64 inline html using the img tag
        @param data: The data to plot. Must be a 2d array of X values and Y values for a Scatterplot, or an array of X values for a Histogram
        @param x_label : A string for the X label
        @param y_label : A string for the Y label
        @param plot_type : Should be either 'Scatter' or defaults to Histogram
        @param x_bins : The number of bins to separate the X values in for the Histogram

        @return: A String: The png img HTML tag
        """
        # definitions for the axes
        left, width = 0.1, 0.65
        bottom, height = 0.1, 0.65
        rect_scatter = [left, bottom, width, height]
    
        if plot_type == "Scatter":
            x = numpy.array(data[0])
            y = numpy.array(data[1])            
            
            mask=~numpy.isnan(x)
            maskedX=x[mask]
            
            mask2=~numpy.isnan(y)
            maskedY=y[mask2]
            
            # start with a rectangular Figure
            plt.figure(1, figsize=(8,8))
            axScatter = plt.axes(rect_scatter)     
            plt.ticklabel_format(style='sci', scilimits=(100000,100000))

            # the scatter plot:
            axScatter.scatter(maskedX, maskedY,c='c', marker='o', s=1)      
            plt.ylabel(y_label)

        elif plot_type == "Histogram":
            x = numpy.array(data)
            mask=~numpy.isnan(x)
            maskedA=x[mask]
            #plt.ticklabel_format(style='sci', scilimits=(100000,100000))
            n, bins, patches = plt.hist(maskedA, bins = x_bins, histtype='bar')
            plt.ylabel("Frequency")
            #plt.axis(rect_scatter)
        elif plot_type == "Density":
            
            x = numpy.array(data[0])
            y = numpy.array(data[1])   
           
            mask=~numpy.isnan(x)
            maskedX=x[mask]
            
            mask2=~numpy.isnan(y)
            maskedY=y[mask2]   
           
            #n = 100000
            #x = numpy.random.standard_normal(n)
            #y = 2.0 + 3.0 * x + 4.0 * numpy.random.standard_normal(n)
            xmin = maskedX.min()
            xmax = maskedX.max()
            ymin = maskedY.min()
            ymax = maskedY.max()
            plt.hexbin(maskedX, maskedY)
            
            plt.ticklabel_format(style='sci', scilimits=(100000,100000))
            plt.axis([xmin, xmax, ymin, ymax])
            plt.title("Density plot")
           
            cb = plt.colorbar()
            
            cb.set_label('counts')
            plt.ylabel(y_label)
            
        
        imgdata = StringIO.StringIO()
        plt.xlabel(x_label)
        plt.grid(True)
        #plt.savefig(imgdata, format='png', bbox_inches='tight', pad_inches=0.5)

        import uuid
        unique_filename = str(uuid.uuid4())
        img_path = host_temp_directory + "/" + unique_filename + ".png"
        urlpath = publicly_visible_temp_dir + "/"+ unique_filename + ".png"
        plt.savefig(img_path, format='png', bbox_inches='tight', pad_inches=0.5)
        #imgdata.seek(0)  # rewind the data
        #img_hash = urllib.quote(base64.b64encode(imgdata.buf))
        #img_hash = imgdata.getvalue().encode("base64").strip()
        #imgdata.close()
        #uri = img_hash
        
        #content = "<img src='data:image/png;base64,%s'/>" % uri
        content = "<img src='%s'/>" % urlpath
        plt.clf()
        return content
