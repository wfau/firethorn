"""
StringToRADec module

University of Edinburgh, WAFU
@author: Stelios Voutsinas
"""
#!/usr/bin/env python

class StringToRADec(object):
    """ generated source for class StringToRADec """
    nullStr = "null"

    @classmethod
    def coordStringToDouble(self, str_, RADec, limits):
        """ generated source for method coordStringToDouble """
        st = str_.strip().split(":")
        noParts = len(st)
        parts = [None]*noParts
        # 
        #  * Created on 30-Aug-2005
        #  *
        #  * TODO To change the template for this generated file go to
        #  * Window - Preferences - Java - Code Style - Code Templates
        #  
        # 
        #  * @author mar
        #  *
        #  * TODO To change the template for this generated type comment go to
        #  * Window - Preferences - Java - Code Style - Code Templates
        #  
        i = 0
        while i < noParts:
            # 
            #  * Created on 30-Aug-2005
            #  *
            #  * TODO To change the template for this generated file go to
            #  * Window - Preferences - Java - Code Style - Code Templates
            #  
            # 
            #  * @author mar
            #  *
            #  * TODO To change the template for this generated type comment go to
            #  * Window - Preferences - Java - Code Style - Code Templates
            #  
            parts[i] = st[i]
            i += 1
        if noParts == 3:
            try:
               
                strHD=float(parts[0])
                strM=float(parts[1])                
                strS=float(parts[2])
                retValue=(abs(strHD)+(strM+strS/60.0)/60.0);
                if RADec.lower() == "RA".lower():
                    retValue = retValue * 15.0
                if parts[0].find('-') >= 0:
                    retValue = retValue * -1.0                
                if limits:
                    if (retValue >= 0.0 and retValue <= 360.0 and RADec.lower() == "RA".lower()) or (retValue >= -90.0 and retValue <= 90.0):
                        return retValue
                    else:
                        raise Exception("Coords out of range")
                else:
                    return retValue
            except Exception as e:
                print e
                raise Exception("Unable to parse string" )
        else:
            raise Exception("Unable to parse string: check number of parts")

    @classmethod
    def getRADec(self, str_, IDPresent):
        """ generated source for method getRADec_0 """
        objs = [];
        objs[3]="null";
        l=0
        coords = [None]*2
        st = str_.strip().split(" ,:\t")
        noParts = st.countTokens()
        parts = [None]*st.countTokens()
        # 
        #  * Created on 30-Aug-2005
        #  *
        #  * TODO To change the template for this generated file go to
        #  * Window - Preferences - Java - Code Style - Code Templates
        #  
        # 
        #  * @author mar
        #  *
        #  * TODO To change the template for this generated type comment go to
        #  * Window - Preferences - Java - Code Style - Code Templates
        #  
        i = 0
        while i < noParts:
            # 
            #  * Created on 30-Aug-2005
            #  *
            #  * TODO To change the template for this generated file go to
            #  * Window - Preferences - Java - Code Style - Code Templates
            #  
            # 
            #  * @author mar
            #  *
            #  * TODO To change the template for this generated type comment go to
            #  * Window - Preferences - Java - Code Style - Code Templates
            #  
            parts[i] = st.nextToken()
            i += 1
        if noParts >= 2 and noParts < 6:
            try:
                coords[0] = float(parts[0])
                coords[1] = float(parts[1])
                if noParts >= 3 and IDPresent:
                    l = float(parts[2])
                if noParts >= 3 and not IDPresent:
                    objs[3] = parts[2]
                if noParts >= 4 and IDPresent:
                    objs[3] = parts[3]
            except Exception as nfe:
                raise Exception("Unable to parse string:" + nfe + " " + IDPresent)
        elif noParts >= 6:
            try:
                raH=float(parts[0])
                raM=float(parts[1])                
                raS=float(parts[2])
                decD=float(parts[3])
                decM=float(parts[4])
                decS=float(parts[5])
                coords[0]=(raH+(raM+raS/60.0)/60.0)*15.0
                coords[1]=(abs(decD)+(decM+decS/60.0)/60.0)
                coords[0] = (raH + (raM + raS / 60.0) / 60.0) * 15.0
                coords[1] = (abs(decD) + (decM + decS / 60.0) / 60.0)
                if parts[3].find('-') >= 0:
                    coords[1] = coords[1] * -1.0
                if noParts >= 7 and IDPresent:
                    l = float(parts[6])
                if noParts >= 7 and not IDPresent:
                    objs[3] = parts[6]
                if noParts >= 8 and IDPresent:
                    objs[3] = parts[7]
            except Exception as nfe:
                raise Exception("Unable to parse string")
        elif noParts == 0:
            objs[0] = float(999.0)
            objs[1] = float(999.0)
            objs[2] = float(0)
            return objs
        else:
            raise Exception("Unable to parse string")
        if coords[0] >= 0.0 and coords[0] <= 360.0 and coords[1] >= -90.0 and coords[1] <= 90.0:
            objs[0] = float(coords[0])
            objs[1] = float(coords[1])
            objs[2] = float(l)
            return objs
        else:
            raise Exception("Unable to parse string into valid coords")

