"""
VDFSSchema module

University of Edinburgh, WAFU
@author: Stelios Voutsinas
"""

from imageList import VSASchema, WSASchema
from imageList.config import *
from imageList.VSASchema import *

class VDFSSchema:
    WSAARCHIVE = "WSA"
    VSAARCHIVE = "VSA"
    SSAARCHIVE = "SSA"
    OSAARCHIVE = "SSA"
    SSAARCHIVEID = 0
    WSAARCHIVEID = 1
    VSAARCHIVEID = 2
    OSAARCHIVEID = 3
    VSASUPPORT = "vsa-support@roe.ac.uk"
    WSASUPPORT = "wsa-support@roe.ac.uk"
    EXTERNALSURVEYS = ["bestdr1..", "bestdr2..", "bestdr3..", "bestdr5..", "bestdr7..", "bestdr8..", "seguedr6..", "ssa.."]
    # ,"first","glimpse","iras","mgc","nvss","seguedr6","twoxmm","twomass"};
    ALLSQLSERVERS = ["amenhotep", "ahmose", "thutmose", "hatshepsut", "ramses1", "ramses2", "ramses3", "ramses4", "ramses5", "ramses6", "ramses7", "ramses8", "ramses9", "ramses10", "ramses11"]
    UPLOADTABLE = "#userTable"
    
    def __init__(self):
        WSAARCHIVE = "WSA"
        VSAARCHIVE = "VSA"
        SSAARCHIVE = "SSA"
        OSAARCHIVE = "OSA"
        SSAARCHIVEID = 0
        WSAARCHIVEID = 1
        VSAARCHIVEID = 2
        OSAARCHIVEID = 2
        VSASUPPORT = "vsa-support@roe.ac.uk"
        WSASUPPORT = "wsa-support@roe.ac.uk"
        EXTERNALSURVEYS = ["bestdr1..", "bestdr2..", "bestdr3..", "bestdr5..", "bestdr7..", "bestdr8..", "seguedr6..", "ssa.."]

    @staticmethod
    def getArchiveID(self, archive):
        """ generated source for method getArchiveID """
        if archive.lower() == self.VSAARCHIVE.lower():
            return self.VSAARCHIVEID
        elif archive.lower() == self.SSAARCHIVE.lower():
            return self.SSAARCHIVEID
        else:
            return self.WSAARCHIVEID
   
    @staticmethod
    def getAllSurveys(self, archiveID):
        """ generated source for method getAllSurveys """
        if archiveID==self.VSAARCHIVEID:
            return VSASchema.ALLSURVEYS
        else:
            return WSASchema.ALLSURVEYS
    
    @staticmethod
    def getFilterName(self, filterID, archiveID):
        """ generated source for method getFilterName """
        if archiveID==self.VSAARCHIVEID:
            return VSASchema.getFilterName(VSASchema(), filterID)
        else:
            return WSASchema.getFilterName(WSASchema(), filterID)
    
    @staticmethod
    def getMergeTableName(self, surveyID, archive):
        """ generated source for method getMergeTableName """
        if archive.lower() == "vsa".lower():
            return VSASchema.getMergeTableName(VSASchema(),surveyID)
        else:
            return WSASchema.getMergeTableName(WSASchema(),surveyID)
    
    @staticmethod
    def getSurveyName(self, progID, archiveID):
        """ generated source for method getSurveyName """
        if archiveID==self.VSAARCHIVEID:
            return VSASchema.getSurveyName(VSASchema(),progID)
        else:
            return WSASchema.getSurveyName(WSASchema(),progID)
    
    @staticmethod
    def isProprietary(self, community):
        """ generated source for method isProprietary """
        return VSASchema.isProprietary(VSASchema(), community)
    
    @staticmethod
    def isReserved(self, community):
        """ generated source for method isReserved """
        if community != None and (self.isProprietary(self,community) or community.lower().contains(WSASchema.NONSURVEYCOMMUNITY.lower())):
            return True
        else:
            return False