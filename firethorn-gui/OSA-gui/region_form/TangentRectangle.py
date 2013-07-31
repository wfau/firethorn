"""
TangentRectangle module

University of Edinburgh, WAFU
@author: Stelios Voutsinas
"""

#!/usr/bin/env python

from SLALIB import *
import math
# 
#  * Class to calculate long/lat boundaries of a box on the sky
#  * 
#  * @author MAR
#  *
#  
class TangentRectangle(object):
    """ generated source for class TangentRectangle """
 

    
    def __init__(self, longitude, latitude, radius):
        """ generated source for method __init__ """
        pi = 4.0 * math.atan(1.0)
        RADIAN = 180.0 / math.pi
    
        # 
        #      * anything greater than  this set min/max long 0,360
        #      
        self.latitudeLimit = 90
       
        self.longitudeZ = longitude / RADIAN
        self.latitudeZ = latitude / RADIAN
        xi = abs(radius / (RADIAN * 60.0))
        eta =abs(radius / (RADIAN * 60.0))
        # correction for tangent projection
        ratio = eta / math.tan(eta)
        #  print ratio;
        #  calculate max min latitudes limit -90 to +90
        self.minLatitude = max(-90.0, (self.latitudeZ - eta) * RADIAN)
        self.maxLatitude = min(+90.0, (self.latitudeZ + eta) * RADIAN)
        #  print minLatitude+" "+maxLatitude;
        if self.minLatitude <= -self.latitudeLimit or self.maxLatitude >= self.latitudeLimit:
            self.minLongitude = 0.0
            self.maxLongitude = 360.0
        else:
            #  now estimate min/max longitude, not entirely convinced this is correct!
            if self.latitudeZ >= 0.0:
                topLeft=SLALIB.DTP2S(-xi/ratio,0,self.longitudeZ,self.maxLatitude/RADIAN);
                topRight=SLALIB.DTP2S(xi/ratio,0,self.longitudeZ,self.maxLatitude/RADIAN);  
                self.minLongitude = topLeft[0] * RADIAN
                self.maxLongitude = topRight[0] * RADIAN
            else:
                botLeft=SLALIB.DTP2S(-xi/ratio,0,self.longitudeZ,self.minLatitude/RADIAN);
                botRight=SLALIB.DTP2S(xi/ratio,0,self.longitudeZ,self.minLatitude/RADIAN);
                self.minLongitude = botLeft[0] * RADIAN
                self.maxLongitude = botRight[0] * RADIAN
        #  System.out.println(minLongitude+" "+maxLongitude+" "+
        #      minLatitude+" "+maxLatitude);

  
    def __init___0(self, longitude, latitude, x, y):
        """ generated source for method __init___0 """
        pi = 4.0 * math.atan(1.0)
        RADIAN = 180.0 / math.pi
        latitudeLimit = 90
        longitudeZ = 0.0
        latitudeZ = 0.0
        minLongitude = 0.0
        maxLongitude = 0.0
        minLatitude = 0.0
        maxLatitude = 0.0
        self.longitudeZ = longitude / RADIAN
        self.latitudeZ = latitude / RADIAN
        xi = abs(0.5 * x / (self.RADIAN * 60.0))
        eta = abs(0.5 * y / (self.RADIAN * 60.0))
        ratio = eta / math.tan(eta)
        topLeft = SLALIB.DTP2S(-xi, eta, self.longitudeZ, self.latitudeZ)
        topRight = SLALIB.DTP2S(xi, eta, self.longitudeZ, self.latitudeZ)
        topMid = SLALIB.DTP2S(0, eta, self.longitudeZ, self.latitudeZ)
        botLeft = SLALIB.DTP2S(-xi, -eta, self.longitudeZ, self.latitudeZ)
        botRight = SLALIB.DTP2S(xi, -eta, self.longitudeZ, self.latitudeZ)
        botMid = SLALIB.DTP2S(0, -eta, self.longitudeZ, self.latitudeZ)
        topMidLat = self.latitudeZ + eta
        botMidLat = self.latitudeZ - eta
        self.maxLatitude = max(topMidLat, max(topMid[1], max(topLeft[1], topRight[1])))
        self.minLatitude = min(botMidLat, min(botMid[1], min(botLeft[1], botRight[1])))
        self.minLatitude = minLatitude * RADIAN
        self.maxLatitude = maxLatitude * RADIAN
        self.minLatitude = max(-90.0, minLatitude)
        self.maxLatitude = min(90.0, maxLatitude)
        # 
        #         minLatitude=max(-90.0,(latitudeZ-eta)*RADIAN);
        #         maxLatitude=min(+90.0,(latitudeZ+eta)*RADIAN);
        #         
        # System.out.println(topLeft[1]*RADIAN+" "+topRight[1]*RADIAN+" "+
        #       topMid[1]*RADIAN+" "+topMidLat*RADIAN);
        # System.out.println(botLeft[1]*RADIAN+" "+botRight[1]*RADIAN+" "+
        #       botMid[1]*RADIAN+" "+botMidLat*RADIAN);
        # print minLatitude+" "+maxLatitude;
        # print minLatitude+" "+maxLatitude;
        if self.minLatitude <= -latitudeLimit or self.maxLatitude >= latitudeLimit:
            self.minLongitude = 0.0
            self.maxLongitude = 360.0
        else:
            if self.latitudeZ >= 0.0:
                self.minLongitude = topLeft[0] * RADIAN
                self.maxLongitude = topRight[0] * RADIAN
            else:
                self.minLongitude = botLeft[0] * RADIAN
                self.maxLongitude = botRight[0] * RADIAN
        #  print minLongitude+" "+maxLongitude; 

    # 
    #      * Gets the minimum latitude of search region
    #      * 
    #      * @return minimum latitude of search area
    #      
    def getMinLatitude(self):
        """ generated source for method getMinLatitude """
        return self.minLatitude

    # 
    #      * Gets the maximum latitude of search region
    #      * 
    #      * @return maximum latitude of search area
    #      
    def getMaxLatitude(self):
        """ generated source for method getMaxLatitude """
        return self.maxLatitude

    # 
    #      * Gets the minimum longitude of search region
    #      * 
    #      * @return minimum longitude of search area
    #      
    def getMinLongitude(self):
        """ generated source for method getMinLongitude """
        return self.minLongitude

    # 
    #      * Gets the maximum longitude of search region
    #      * 
    #      * @return maximum longitude of search area
    #      
    def getMaxLongitude(self):
        """ generated source for method getMaxLongitude """
        return self.maxLongitude

    def getRASQL(self):
        """ generated source for method getRASQL """
        if self.minLongitude <= self.maxLongitude:
            return "RA >= " + str(self.minLongitude) + " and RA <= " + str(self.maxLongitude)
        else:
            return "((RA >= " + str(self.minLongitude) + " and RA <=360) or (RA >= 0 and RA <= " + str(self.maxLongitude) + "))"

