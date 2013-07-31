"""
SQLMethods module

University of Edinburgh, WAFU
@author: Stelios Voutsinas
"""
import VDFSSchema

class SQLMethods:   
    
    doubleQuotes = "\""
    MAXLENGTH = 20000
    WEBQUERIESLENGTH = 4096
    COLOURS = ["R", "G", "B"]
    
    
    @staticmethod
    def getRequiredFiltersSQL():
        """ generated source for method getRequiredFiltersSQL """
        return "select filterid,npass from requiredfilters"
    
    @staticmethod
    def getFramesetSQL(rset, mergelogTable, multiframeTable, archiveID = VDFSSchema.VDFSSchema.WSAARCHIVEID):
        """ generated source for method getFramesetSQL_0 """
        sbSQL = ""
        mfidName = None
        row = 0
        first = True
        vdfssInstance = VDFSSchema.VDFSSchema()
        
        try:
            for i in rset:
                row += 1
                filterid = int(i[0])
                npass = int(i[1])
                if npass == 1:
                    mfidName = VDFSSchema.VDFSSchema.getFilterName(vdfssInstance, filterid, archiveID) + "mfid"
                    if not first:
                        sbSQL+=" or "
                    first = False
                    sbSQL+= mergelogTable + "." + mfidName + "=" + multiframeTable + ".multiframeid"
                else:
                    if npass > 1:
                        i = 0 
                        while i < npass:
                            mfidName = VDFSSchema.VDFSSchema.getFilterName(vdfssInstance, filterid, archiveID) + "_" + str(i + 1) + "mfid"
                            if not first:
                                sbSQL+= " or "
                            first = False
                            sbSQL+=mergelogTable + "." + mfidName + "=" + multiframeTable + ".multiframeid"
                            i += 1
            if row > 0:
                return sbSQL
            else:
                return None
        except Exception as se:
            print se
            return None
