"""
WSASchema module

University of Edinburgh, WAFU
@author: Stelios Voutsinas
"""

class WSASchema:
    #         String [] ALLUKIDSSRDB={"UKIDSSDR7PLUS","UKIDSSDR6PLUS","UKIDSSDR5PLUS","UKIDSSDR4PLUS","UKIDSSDR3PLUS","UKIDSSDR2PLUS","UKIDSSDR1PLUS","UKIDSSDR1","UKIDSSEDRPLUS","UKIDSSEDR","UKIDSSSV","UKIDSSR1"}; // names for UKIDSS releaase
    ALLUKIDSSRDB = []
    #       int [] UKIDSSVERSIONS={9,8,7,6,5,4,3,3,2,2,1,0}; // version numbers for UKIDSS releaase
    UKIDSSVERSIONS = []
    #      String [] UKIDSSDB={"UKIDSSDR7PLUS","UKIDSSDR6PLUS","UKIDSSDR5PLUS","UKIDSSDR4PLUS","UKIDSSDR3PLUS","UKIDSSDR2PLUS","UKIDSSDR1PLUS","UKIDSSDR1","UKIDSSEDRPLUS","UKIDSSEDR","UKIDSSSV"};
    UKIDSSDB = []
    #           String [] WORLDDB={"UKIDSSDR4PLUS","UKIDSSDR3PLUS","UKIDSSDR2PLUS","UKIDSSDR1PLUS","UKIDSSDR1","UKIDSSEDRPLUS","UKIDSSEDR","UKIDSSSV"}; //"WORLDR2" names for world releaase
    WORLDDB = []
    #           int [] WORLDVERSIONS={6,5,4,3,3,2,2,1}; // version numbers for WORLD releaase
    WORLDVERSIONS = []
    #           String [] SCEHMAVALIDDATABASES={"ukidssdr7plus","ukidssdr6plus","ukidssdr5plus","ukidssdr4plus","ukidssdr3plus"};//,"ukidssdr2plus","ukidssdr1plus","ukidssdr1","ukidssedrplus","ukidssedr","ukidssr2"}; // array of databases compatible with latest schema for source & detection
    SCEHMAVALIDDATABASES = []
    #  loads the parameters listed above here

    # "ukidssdr4plus"; // latest world release have the same schema as this release
    LATESTNONSURVEYSCHEMA = "ukidssr2"
    #  all non survey progs have the same schema as this release
    PRERELEASESERVER = "ahmose"
    # "ahmose";
    PRERELEASEDATABASE = "WSA"
    # "WSA";"WSA copy"
    PRERELEASECOMMUNITY = "prerelease"
    NONSURVEYCOMMUNITY = "nonSurvey"
    UKIDSSTESTERDB = ALLUKIDSSRDB
    VERSIONOFSOURCEVIEWS = 4
    PTSDB = "Transit"
    CALDB = "WFCAMCAL08B"
    OPENDB = "WFCAMOPENTIME"
    extraDB = [PTSDB]
    calDBs = [CALDB]
    worldCalDBs = [CALDB]
    moreDB = [OPENDB]
    doExtraDB = True
    PRERELEASEDB = [PRERELEASEDATABASE]
    accessToRollingDB = True
    #  should the archive listing etc give the option of querying a rolling DB 
    rollingDB = "WSA"
    # "WSA";"WSA copy"
    rollingServer = "amenhotep"
    # "amenhotep";//"ahmose";
    rollingDBUser = "wsaro"
    rollingDBPasswd = "wsaropw"
    noAccessDB = "sorry no access"
    actualRollingDB = "WFCAMPROPRIETY"
    # "WFCAMPROPRIETY";//"WSA";"TestWSArecal"
    ALLSURVEYS = "All UKIDSS surveys"
    MAXIMUMROWSTOFILE = 15000000
    #  maximum number of rows written to file by Queries not used yet
    FITS_OFFSET = 1
    WFCAMNONEFILTERID = 0
    WFCAMZFILTERID = 1
    WFCAMYFILTERID = 2
    WFCAMJFILTERID = 3
    WFCAMHFILTERID = 4
    WFCAMKFILTERID = 5
    WFCAMH2FILTERID = 6
    WFCAMBRFILTERID = 7
    WFCAMBLANKFILTERID = 8
    WFCAMNBJFILTERID = 9
    WFCAMNBHFILTERID = 10
    WFCAMNBKFILTERID = 11
    ALLFILTERID = [WFCAMNONEFILTERID, WFCAMZFILTERID, WFCAMYFILTERID, WFCAMJFILTERID, WFCAMHFILTERID, WFCAMKFILTERID, WFCAMH2FILTERID, WFCAMBRFILTERID, WFCAMBLANKFILTERID, WFCAMNBJFILTERID, WFCAMNBHFILTERID, WFCAMNBKFILTERID]
    COMMISSIONINGPROGRAMMEID = 1
    CURATIONTESTPROGRAMMEID = 99
    UKIDSSLASPROGRAMMEID = 101
    UKIDSSGPSPROGRAMMEID = 102
    UKIDSSGCSPROGRAMMEID = 103
    UKIDSSDXSPROGRAMMEID = 104
    UKIDSSUDSPROGRAMMEID = 105
    PTSPROGRAMMEID = 106
    CALPROGRAMMEID = 200
    OTHERCALPROGRAMMEID = 201
    UKIDSSLASSVPROGRAMMEID = 11
    UKIDSSGPSSVPROGRAMMEID = 12
    UKIDSSGCSSVPROGRAMMEID = 13
    UKIDSSDXSSVPROGRAMMEID = 14
    UKIDSSUDSSVPROGRAMMEID = 15
    LASRGB = ["K", "H", "Y"]
    GPSRGB = ["K", "H", "J"]
    GCSRGB = ["K", "H", "Z"]
    DXSRGB = ["K", "H", "J"]
    UDSRGB = ["K", "H", "J"]
    CALRGB = ["K", "H", "Z"]
    UKIDSSTESTERPROGS = [UKIDSSLASPROGRAMMEID, UKIDSSGPSPROGRAMMEID, UKIDSSGCSPROGRAMMEID, UKIDSSDXSPROGRAMMEID, UKIDSSUDSPROGRAMMEID, UKIDSSLASSVPROGRAMMEID, UKIDSSGPSSVPROGRAMMEID, UKIDSSGCSSVPROGRAMMEID, UKIDSSDXSSVPROGRAMMEID, UKIDSSUDSSVPROGRAMMEID, COMMISSIONINGPROGRAMMEID]
    UKIDSSPROGS = [UKIDSSLASPROGRAMMEID, UKIDSSGPSPROGRAMMEID, UKIDSSGCSPROGRAMMEID, UKIDSSDXSPROGRAMMEID, UKIDSSUDSPROGRAMMEID]
    WORLDPROGS = UKIDSSPROGS
    PRERELEASEPROGS = UKIDSSTESTERPROGS
    COMMISSIONINGDETECTIONTABLENAME = "commDetection"
    UKIDSSLASDETECTIONTABLENAME = "lasDetection"
    UKIDSSGPSDETECTIONTABLENAME = "gpsDetection"
    UKIDSSGCSDETECTIONTABLENAME = "gcsDetection"
    UKIDSSDXSDETECTIONTABLENAME = "dxsDetection"
    UKIDSSUDSDETECTIONTABLENAME = "udsDetection"
    PTSDETECTIONTABLENAME = "ptsDetection"
    CALDETECTIONTABLENAME = "calDetection"
    COMMISSIONINGSOURCETABLENAME = "commSource"
    UKIDSSLASSOURCETABLENAME = "lasSource"
    UKIDSSGPSSOURCETABLENAME = "gpsSource"
    UKIDSSGCSSOURCETABLENAME = "gcsSource"
    UKIDSSDXSSOURCETABLENAME = "dxsSource"
    UKIDSSUDSSOURCETABLENAME = "udsSource"
    UKIDSSLASSOURCEVIEWNAME = "lasYJHKsource"
    UKIDSSGPSSOURCEVIEWNAME = "gpsJHKsource"
    UKIDSSGCSSOURCEVIEWNAME = "gcsZYJHKsource"
    UKIDSSDXSSOURCEVIEWNAME = "dxsJKSource"
    UKIDSSUDSSOURCEVIEWNAME = None
    PTSSOURCETABLENAME = "NONE"
    CALSOURCETABLENAME = "calSource"
    CALMERGETABLENAME = "calMergeLog"
    COMMISSIONINGMERGETABLENAME = "commMergeLog"
    UKIDSSLASMERGETABLENAME = "lasMergeLog"
    UKIDSSGPSMERGETABLENAME = "gpsMergeLog"
    UKIDSSGCSMERGETABLENAME = "gcsMergeLog"    """ generated source for method main """
    #         String [] ALLUKIDSSRDB={"UKIDSSDR7PLUS","UKIDSSDR6PLUS","UKIDSSDR5PLUS","UKIDSSDR4PLUS","UKIDSSDR3PLUS","UKIDSSDR2PLUS","UKIDSSDR1PLUS","UKIDSSDR1","UKIDSSEDRPLUS","UKIDSSEDR","UKIDSSSV","UKIDSSR1"}; // names for UKIDSS releaase
    ALLUKIDSSRDB = []
    #       int [] UKIDSSVERSIONS={9,8,7,6,5,4,3,3,2,2,1,0}; // version numbers for UKIDSS releaase
    UKIDSSVERSIONS = []
    #      String [] UKIDSSDB={"UKIDSSDR7PLUS","UKIDSSDR6PLUS","UKIDSSDR5PLUS","UKIDSSDR4PLUS","UKIDSSDR3PLUS","UKIDSSDR2PLUS","UKIDSSDR1PLUS","UKIDSSDR1","UKIDSSEDRPLUS","UKIDSSEDR","UKIDSSSV"};
    UKIDSSDB = []
    #           String [] WORLDDB={"UKIDSSDR4PLUS","UKIDSSDR3PLUS","UKIDSSDR2PLUS","UKIDSSDR1PLUS","UKIDSSDR1","UKIDSSEDRPLUS","UKIDSSEDR","UKIDSSSV"}; //"WORLDR2" names for world releaase
    WORLDDB = []
    #           int [] WORLDVERSIONS={6,5,4,3,3,2,2,1}; // version numbers for WORLD releaase
    WORLDVERSIONS = []
    #           String [] SCEHMAVALIDDATABASES={"ukidssdr7plus","ukidssdr6plus","ukidssdr5plus","ukidssdr4plus","ukidssdr3plus"};//,"ukidssdr2plus","ukidssdr1plus","ukidssdr1","ukidssedrplus","ukidssedr","ukidssr2"}; // array of databases compatible with latest schema for source & detection
    SCEHMAVALIDDATABASES = []
    #  loads the parameters listed above here
    LATESTUKIDSSDATABASE = "ukidssdr7plus"
    # "ukidssdr7plus";// "ukidssedr";
    LATESTWORLDDATABASE ="ukidssdr4plus"
    # "ukidssdr4plus"; 
    LATESTUKIDSSSCHEMA ="ukidssdr7plus"
    # "ukidssdr7plus"; // "ukidssedr";
    LATESTWORLDSCHEMA ="ukidssdr4plus"
    # "ukidssdr4plus"; // latest world release have the same schema as this release
    LATESTNONSURVEYSCHEMA = "ukidssr2"
    #  all non survey progs have the same schema as this release
    PRERELEASESERVER = "ahmose"
    # "ahmose";
    PRERELEASEDATABASE = "WSA"
    # "WSA";"WSA copy"
    PRERELEASECOMMUNITY = "prerelease"
    NONSURVEYCOMMUNITY = "nonSurvey"
    UKIDSSTESTERDB = ALLUKIDSSRDB
    VERSIONOFSOURCEVIEWS = 4
    PTSDB = "Transit"
    CALDB = "WFCAMCAL08B"
    OPENDB = "WFCAMOPENTIME"
    extraDB = [PTSDB]
    calDBs = [CALDB]
    worldCalDBs = [CALDB]
    moreDB = [OPENDB]
    doExtraDB = True
    PRERELEASEDB = [PRERELEASEDATABASE]
    accessToRollingDB = True
    #  should the archive listing etc give the option of querying a rolling DB 
    rollingDB = "WSA"
    # "WSA";"WSA copy"
    rollingServer = "amenhotep"
    # "amenhotep";//"ahmose";
    rollingDBUser = "wsaro"
    rollingDBPasswd = "wsaropw"
    noAccessDB = "sorry no access"
    actualRollingDB = "WFCAMPROPRIETY"
    # "WFCAMPROPRIETY";//"WSA";"TestWSArecal"
    ALLSURVEYS = "All UKIDSS surveys"
    MAXIMUMROWSTOFILE = 15000000
    #  maximum number of rows written to file by Queries not used yet
    FITS_OFFSET = 1
    WFCAMNONEFILTERID = 0
    WFCAMZFILTERID = 1
    WFCAMYFILTERID = 2
    WFCAMJFILTERID = 3
    WFCAMHFILTERID = 4
    WFCAMKFILTERID = 5
    WFCAMH2FILTERID = 6
    WFCAMBRFILTERID = 7
    WFCAMBLANKFILTERID = 8
    WFCAMNBJFILTERID = 9
    WFCAMNBHFILTERID = 10
    WFCAMNBKFILTERID = 11
    ALLFILTERID = [WFCAMNONEFILTERID, WFCAMZFILTERID, WFCAMYFILTERID, WFCAMJFILTERID, WFCAMHFILTERID, WFCAMKFILTERID, WFCAMH2FILTERID, WFCAMBRFILTERID, WFCAMBLANKFILTERID, WFCAMNBJFILTERID, WFCAMNBHFILTERID, WFCAMNBKFILTERID]
    COMMISSIONINGPROGRAMMEID = 1
    CURATIONTESTPROGRAMMEID = 99
    UKIDSSLASPROGRAMMEID = 101
    UKIDSSGPSPROGRAMMEID = 102
    UKIDSSGCSPROGRAMMEID = 103
    UKIDSSDXSPROGRAMMEID = 104
    UKIDSSUDSPROGRAMMEID = 105
    PTSPROGRAMMEID = 106
    CALPROGRAMMEID = 200
    OTHERCALPROGRAMMEID = 201
    UKIDSSLASSVPROGRAMMEID = 11
    UKIDSSGPSSVPROGRAMMEID = 12
    UKIDSSGCSSVPROGRAMMEID = 13
    UKIDSSDXSSVPROGRAMMEID = 14
    UKIDSSUDSSVPROGRAMMEID = 15
    LASRGB = ["K", "H", "Y"]
    GPSRGB = ["K", "H", "J"]
    GCSRGB = ["K", "H", "Z"]
    DXSRGB = ["K", "H", "J"]
    UDSRGB = ["K", "H", "J"]
    CALRGB = ["K", "H", "Z"]
    UKIDSSTESTERPROGS = [UKIDSSLASPROGRAMMEID, UKIDSSGPSPROGRAMMEID, UKIDSSGCSPROGRAMMEID, UKIDSSDXSPROGRAMMEID, UKIDSSUDSPROGRAMMEID, UKIDSSLASSVPROGRAMMEID, UKIDSSGPSSVPROGRAMMEID, UKIDSSGCSSVPROGRAMMEID, UKIDSSDXSSVPROGRAMMEID, UKIDSSUDSSVPROGRAMMEID, COMMISSIONINGPROGRAMMEID]
    UKIDSSPROGS = [UKIDSSLASPROGRAMMEID, UKIDSSGPSPROGRAMMEID, UKIDSSGCSPROGRAMMEID, UKIDSSDXSPROGRAMMEID, UKIDSSUDSPROGRAMMEID]
    WORLDPROGS = UKIDSSPROGS
    PRERELEASEPROGS = UKIDSSTESTERPROGS
    COMMISSIONINGDETECTIONTABLENAME = "commDetection"
    UKIDSSLASDETECTIONTABLENAME = "lasDetection"
    UKIDSSGPSDETECTIONTABLENAME = "gpsDetection"
    UKIDSSGCSDETECTIONTABLENAME = "gcsDetection"
    UKIDSSDXSDETECTIONTABLENAME = "dxsDetection"
    UKIDSSUDSDETECTIONTABLENAME = "udsDetection"
    PTSDETECTIONTABLENAME = "ptsDetection"
    CALDETECTIONTABLENAME = "calDetection"
    COMMISSIONINGSOURCETABLENAME = "commSource"
    UKIDSSLASSOURCETABLENAME = "lasSource"
    UKIDSSGPSSOURCETABLENAME = "gpsSource"
    UKIDSSGCSSOURCETABLENAME = "gcsSource"
    UKIDSSDXSSOURCETABLENAME = "dxsSource"
    UKIDSSUDSSOURCETABLENAME = "udsSource"
    UKIDSSLASSOURCEVIEWNAME = "lasYJHKsource"
    UKIDSSGPSSOURCEVIEWNAME = "gpsJHKsource"
    UKIDSSGCSSOURCEVIEWNAME = "gcsZYJHKsource"
    UKIDSSDXSSOURCEVIEWNAME = "dxsJKSource"
    UKIDSSUDSSOURCEVIEWNAME = None
    PTSSOURCETABLENAME = "NONE"
    CALSOURCETABLENAME = "calSource"
    CALMERGETABLENAME = "calMergeLog"
    COMMISSIONINGMERGETABLENAME = "commMergeLog"
    UKIDSSLASMERGETABLENAME = "lasMergeLog"
    UKIDSSGPSMERGETABLENAME = "gpsMergeLog"
    UKIDSSGCSMERGETABLENAME = "gcsMergeLog"
    UKIDSSDXSMERGETABLENAME = "dxsMergeLog"
    UKIDSSUDSMERGETABLENAME = "udsMergeLog"
    UKIDSSPTSMERGETABLENAME = "NONE"
    DEFAULTSQLSERVER = "amenhotep"
    UKIDSSLOGIN = "wsaro"
    WORLDLOGIN = "worldwsaro"
    WFCAMFILTERS = ["NONE", "Z", "Y", "J", "H", "K", "H2", "Br", "BLNK", "NBJ", "NBH", "NBK"]
    filters = WFCAMFILTERS
    # deprecated
    COMMISSIONINGBands = ["Z", "Y", "J", "H", "K", "H2", "Br"]
    COMMISSIONINGREQFILTERS = [0, 1, 1, 1, 1, 1, 1, 1, 0]
    LASBands = ["Y", "J_1", "J_2", "H", "K"]
    LASREQFILTERS = [0, 0, 1, 2, 1, 1, 0, 0, 0]
    GPSBands = ["J", "H", "K_1", "K_2", "H2"]
    # {"J","H","K_1","K_2","K_3","H2_1","H2_2","H2_3"};
    GPSREQFILTERS = [0, 0, 0, 1, 1, 2, 1, 0, 0]
    # {0,0,1,1,3,3,0,0};
    GPSREQFILTERSDR2 = [0, 0, 1, 1, 3, 3, 0, 0]
    GCSBands = ["Z", "Y", "J", "H", "K_1", "K_2"]
    GCSREQFILTERS = [0, 1, 1, 1, 1, 2, 0, 0, 0]
    DXSBands = ["J", "H", "K"]
    DXSREQFILTERS = [0, 0, 0, 1, 1, 1, 0, 0, 0]
    UDSBands = ["J", "H", "K"]
    UDSREQFILTERS = [0, 0, 0, 1, 1, 1, 0, 0, 0]
    CALBands = ["Z", "Y", "J", "H", "K", "H2", "Br", "NBJ"]
    CALREQFILTERS = [0, 1, 1, 1, 1, 1, 1, 1, 0, 1]
    
    
    UKIDSSDXSMERGETABLENAME = "dxsMergeLog"
    UKIDSSUDSMERGETABLENAME = "udsMergeLog"
    UKIDSSPTSMERGETABLENAME = "NONE"
    DEFAULTSQLSERVER = "amenhotep"
    UKIDSSLOGIN = "wsaro"
    WORLDLOGIN = "worldwsaro"
    WFCAMFILTERS = ["NONE", "Z", "Y", "J", "H", "K", "H2", "Br", "BLNK", "NBJ", "NBH", "NBK"]
    filters = WFCAMFILTERS
    # deprecated
    COMMISSIONINGBands = ["Z", "Y", "J", "H", "K", "H2", "Br"]
    COMMISSIONINGREQFILTERS = [0, 1, 1, 1, 1, 1, 1, 1, 0]
    LASBands = ["Y", "J_1", "J_2", "H", "K"]
    LASREQFILTERS = [0, 0, 1, 2, 1, 1, 0, 0, 0]
    GPSBands = ["J", "H", "K_1", "K_2", "H2"]
    # {"J","H","K_1","K_2","K_3","H2_1","H2_2","H2_3"};
    GPSREQFILTERS = [0, 0, 0, 1, 1, 2, 1, 0, 0]
    # {0,0,1,1,3,3,0,0};
    GPSREQFILTERSDR2 = [0, 0, 1, 1, 3, 3, 0, 0]
    GCSBands = ["Z", "Y", "J", "H", "K_1", "K_2"]
    GCSREQFILTERS = [0, 1, 1, 1, 1, 2, 0, 0, 0]
    DXSBands = ["J", "H", "K"]
    DXSREQFILTERS = [0, 0, 0, 1, 1, 1, 0, 0, 0]
    UDSBands = ["J", "H", "K"]
    UDSREQFILTERS = [0, 0, 0, 1, 1, 1, 0, 0, 0]
    CALBands = ["Z", "Y", "J", "H", "K", "H2", "Br", "NBJ"]
    CALREQFILTERS = [0, 1, 1, 1, 1, 1, 1, 1, 0, 1]
    
    def isProgInDB(self, db, prog):
        """ generated source for method isProgInDB """
        if self.getVersionNoOfRelease(db) >= self.getDetectionSchemaVersion(prog) and self.getVersionNoOfRelease(db) <= self.getLastSchemaVersion(prog):
            return True
        else:
            return False

    
    def isSourceInDB(self, db, prog):
        """ generated source for method isSourceInDB """
        if self.getVersionNoOfRelease(db) >= self.getSourceSchemaVersion(prog):
            return True
        else:
            return False

    
    def getVersionNoOfRelease(self, schema):
        """ generated source for method getVersionNoOfRelease """
        i = 0
        while i <self. ALLUKIDSSRDB.length:
            if schema.lower() == self.ALLUKIDSSRDB[i].lower():
                return self.UKIDSSVERSIONS[i]
            i += 1
        if schema.lower() == self.rollingDB.lower():
            return 9999
        i = 0
        while i < self.WORLDDB.length:
            if schema.lower() == self.WORLDDB[i].lower():
                return self.WORLDVERSIONS[i]
            i += 1
        return -1

    
    
    def getReqFilters(self, database, surveyID):
        """ generated source for method getReqFilters """
        if surveyID==WSASchema.COMMISSIONINGPROGRAMMEID:
            return self.COMMISSIONINGREQFILTERS
        elif surveyID==WSASchema.CALPROGRAMMEID:
            return self.CALREQFILTERS
        elif surveyID==WSASchema.UKIDSSLASPROGRAMMEID:
            return self.LASREQFILTERS
        elif surveyID==WSASchema.UKIDSSLASSVPROGRAMMEID:
            return self.LASREQFILTERS
        elif surveyID==WSASchema.UKIDSSGPSPROGRAMMEID:
            if self.getVersionNoOfRelease(database) >= self.getVersionNoOfRelease("ukidssdr3plus") or database == "" or database == None:
                return self.GPSREQFILTERS
            else:
                return self.GPSREQFILTERSDR2
        elif surveyID==WSASchema.UKIDSSGPSSVPROGRAMMEID:
            if self.getVersionNoOfRelease(database) >= self.getVersionNoOfRelease("ukidssdr3plus"):
                return self.GPSREQFILTERS
            else:
                return self.GPSREQFILTERSDR2
        elif surveyID==WSASchema.UKIDSSGCSPROGRAMMEID:
            return self.GCSREQFILTERS
        elif surveyID==WSASchema.UKIDSSGCSSVPROGRAMMEID:
            return self.GCSREQFILTERS
        elif surveyID==WSASchema.UKIDSSDXSPROGRAMMEID:
            return self.DXSREQFILTERS
        elif surveyID==WSASchema.UKIDSSDXSSVPROGRAMMEID:
            return self.DXSREQFILTERS
        elif surveyID==WSASchema.UKIDSSUDSPROGRAMMEID:
            return self.UDSREQFILTERS
        elif surveyID==WSASchema.UKIDSSUDSSVPROGRAMMEID:
            return self.UDSREQFILTERS
        else:
            return None

    
    def getReqFilters_0(self, surveyID):
        """ generated source for method getReqFilters_0 """
        if surveyID==WSASchema.COMMISSIONINGPROGRAMMEID:
            return self.COMMISSIONINGREQFILTERS
        elif surveyID==WSASchema.CALPROGRAMMEID:
            return self.CALREQFILTERS
        elif surveyID==WSASchema.UKIDSSLASPROGRAMMEID:
            return self.LASREQFILTERS
        elif surveyID==WSASchema.UKIDSSLASSVPROGRAMMEID:
            return self.LASREQFILTERS
        elif surveyID==WSASchema.UKIDSSGPSPROGRAMMEID:
            return self.GPSREQFILTERS
        elif surveyID==WSASchema.UKIDSSGPSSVPROGRAMMEID:
            return self.GPSREQFILTERS
        elif surveyID==WSASchema.UKIDSSGCSPROGRAMMEID:
            return self.GCSREQFILTERS
        elif surveyID==WSASchema.UKIDSSGCSSVPROGRAMMEID:
            return self.GCSREQFILTERS
        elif surveyID==WSASchema.UKIDSSDXSPROGRAMMEID:
            return self.DXSREQFILTERS
        elif surveyID==WSASchema.UKIDSSDXSSVPROGRAMMEID:
            return self.DXSREQFILTERS
        elif surveyID==WSASchema.UKIDSSUDSPROGRAMMEID:
            return self.UDSREQFILTERS
        elif surveyID==WSASchema.UKIDSSUDSSVPROGRAMMEID:
            return self.UDSREQFILTERS
        else:
            return None

    
    def getDBsPlusRollingDB(self, inDBs):
        """ generated source for method getDBsPlusRollingDB """
        retDBs = [None]*inDBs.length + 1
        i = 0
        while i < inDBs.length:
            retDBs[i] = inDBs[i]
            i += 1
        retDBs[retDBs.length - 1] = WSASchema.rollingDB
        return retDBs

    

    
    
    def getDefaultList(self, reqFilters, table, database):
        """ generated source for method getDefaultList """
        validSchema = False
        if database.lower() == self.PTSDB.lower():
            database = self.LATESTUKIDSSDATABASE
        database = database.replaceAll("(?i)world", "ukidss")
        i = 0
        while i < self.SCEHMAVALIDDATABASES.length:
            if database.lower() == self.SCEHMAVALIDDATABASES[i].lower():
                validSchema = True
            i += 1
        if table.lower().indexOf("detection") >= 0:
            if validSchema:
                return "objID, multiframeID, filterID, RA, Dec, ell, pa, class, psfMag, hallMag,isoMag, petroMag, aperMag2, aperMag3"
                # 
            else:
                return table + ".*"
        else:
            if table.lower().indexOf("source") >= 0:
                if validSchema:
                    sb = "sourceID, framesetID, RA, Dec, mergedClass, priOrSec"
                    if reqFilters != None:
                        sb.append(self.getReqMags(reqFilters))
                    return sb.__str__()
                else:
                    return table + ".*"

    
    def getDefaultList_0(self, reqFilters, table):
        """ generated source for method getDefaultList_0 """
        return self.getDefaultList(reqFilters, table, self.LATESTUKIDSSSCHEMA)

    
    def getReqMags(self, reqFilters):
        """ generated source for method getReqMags """
        sb1 = ""
        # 
        i = 0
        while i < reqFilters.length:
            # 
            if reqFilters[i] == 1:
                # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
                # sb1.append(", "+getFilterName(i+1)+"PetroMag");
                #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
                sb1.append(", " + self.getFilterName(i) + "AperMag3")
                # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
                # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
                # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
                sb1.append(", " + self.getFilterName(i) + "AperMag3Err")
            if reqFilters[i] > 1:
                # 
                # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
                # sb1.append(", "+getFilterName(i+1)+"PetroMag");
                #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
                # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
                # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
                # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
                j = 0 
                while j < reqFilters[i]:
                    # 
                    # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
                    # sb1.append(", "+getFilterName(i+1)+"PetroMag");
                    #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
                    # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
                    # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
                    # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
                    sb1.append(", " + self.getFilterName(i) + "_" + (j + 1) + "AperMag3")
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
                    #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
                    sb1.append(", " + self.getFilterName(i) + "_" + (j + 1) + "AperMag3Err")
                    j += 1
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
                #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
            i += 1
        # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
        # sb1.append(", "+getFilterName(i+1)+"PetroMag");
        #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
        # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
        # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
        # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
        #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
        return sb1.__str__()

    
    
    def getBands(self, reqBands):
        """ generated source for method getBands """
        noBands = 0
        # 
        # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
        # sb1.append(", "+getFilterName(i+1)+"PetroMag");
        #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
        # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
        # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
        # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
        #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
        i = 0
        while i < reqBands.length:
            # 
            # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
            # sb1.append(", "+getFilterName(i+1)+"PetroMag");
            #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
            # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
            # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
            # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
            #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
            noBands = noBands + reqBands[i]
            i += 1
        bands = [None]*noBands
        bandCount = 0
        # 
        # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
        # sb1.append(", "+getFilterName(i+1)+"PetroMag");
        #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
        # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
        # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
        # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
        #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
        i = 0
        while i < reqBands.length:
            # 
            # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
            # sb1.append(", "+getFilterName(i+1)+"PetroMag");
            #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
            # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
            # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
            # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
            #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
            if reqBands[i] == 1:
                bands[bandCount] = self.getFilterName(i)
                bandCount += 1
            if reqBands[i] > 1:
                # 
                # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
                # sb1.append(", "+getFilterName(i+1)+"PetroMag");
                #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
                # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
                # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
                # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
                #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
                j = 0
                while j < reqBands[i]:
                    # 
                    # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
                    # sb1.append(", "+getFilterName(i+1)+"PetroMag");
                    #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
                    # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
                    # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
                    # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
                    #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
                    bands[bandCount] = self.getFilterName(i) + "_" + (j + 1)
                    bandCount += 1
                    j += 1
            i += 1
        return bands

    

    def getBands_0(self, database, surveyID):
        """ generated source for method getBands_0 """
        reqBands = self.getReqFilters(database, surveyID)
        noBands = 0
        # 
        # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
        # sb1.append(", "+getFilterName(i+1)+"PetroMag");
        #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
        # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
        # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
        # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
        #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
        i = 0
        while i < reqBands.length:
            # 
            # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
            # sb1.append(", "+getFilterName(i+1)+"PetroMag");
            #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
            # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
            # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
            # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
            #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
            noBands = noBands + reqBands[i]
            i += 1
        bands = [None]*noBands
        bandCount = 0
        # 
        # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
        # sb1.append(", "+getFilterName(i+1)+"PetroMag");
        #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
        # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
        # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
        # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
        #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
        i = 0
        while i < reqBands.length:
            # 
            # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
            # sb1.append(", "+getFilterName(i+1)+"PetroMag");
            #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
            # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
            # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
            # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
            #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
            if reqBands[i] == 1:
                #  bands[bandCount]=getFilterName(i+1);
                bands[bandCount] = self.getFilterName(i)
                bandCount += 1
            if reqBands[i] > 1:
                # 
                # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
                # sb1.append(", "+getFilterName(i+1)+"PetroMag");
                #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
                # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
                # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
                # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
                #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
                #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
                #  bands[bandCount]=getFilterName(i+1);
                j = 0
                while j < reqBands[i]:
                    # 
                    # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
                    # sb1.append(", "+getFilterName(i+1)+"PetroMag");
                    #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
                    # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
                    # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
                    # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
                    #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
                    #  bands[bandCount]=getFilterName(i+1);
                    #  bands[bandCount]=getFilterName(i+1)+"_"+(j+1);
                    bands[bandCount] = self.getFilterName(i) + "_" + (j + 1)
                    bandCount += 1
                    j += 1
                #  bands[bandCount]=getFilterName(i+1)+"_"+(j+1);
            i += 1
        #  bands[bandCount]=getFilterName(i+1);
        #  bands[bandCount]=getFilterName(i+1)+"_"+(j+1);
        return bands

    
    def getMergeTableName(self, surveyID):
        """ generated source for method getMergeTableName """
        mergeTableName = str()
        if surveyID==WSASchema.COMMISSIONINGPROGRAMMEID:
            mergeTableName = self.COMMISSIONINGMERGETABLENAME
        elif surveyID==WSASchema.CALPROGRAMMEID:
            mergeTableName = self.CALMERGETABLENAME
        elif surveyID==WSASchema.UKIDSSLASPROGRAMMEID:
            mergeTableName = self.UKIDSSLASMERGETABLENAME
        elif surveyID==WSASchema.UKIDSSGPSPROGRAMMEID:
            mergeTableName = self.UKIDSSGPSMERGETABLENAME
        elif surveyID==WSASchema.UKIDSSGCSPROGRAMMEID:
            mergeTableName = self.UKIDSSGCSMERGETABLENAME
        elif surveyID==WSASchema.UKIDSSDXSPROGRAMMEID:
            mergeTableName = self.UKIDSSDXSMERGETABLENAME
        elif surveyID==WSASchema.UKIDSSUDSPROGRAMMEID:
            mergeTableName = self.UKIDSSUDSMERGETABLENAME
        elif surveyID==WSASchema.UKIDSSLASSVPROGRAMMEID:
            mergeTableName = self.UKIDSSLASMERGETABLENAME
        elif surveyID==WSASchema.UKIDSSGPSSVPROGRAMMEID:
            mergeTableName = self.UKIDSSGPSMERGETABLENAME
        elif surveyID==WSASchema.UKIDSSGCSSVPROGRAMMEID:
            mergeTableName = self.UKIDSSGCSMERGETABLENAME
        elif surveyID==WSASchema.UKIDSSDXSSVPROGRAMMEID:
            mergeTableName = self.UKIDSSDXSMERGETABLENAME
        elif surveyID==WSASchema.UKIDSSUDSSVPROGRAMMEID:
            mergeTableName = self.UKIDSSUDSMERGETABLENAME
        else:
            mergeTableName = None
        return mergeTableName

    
    def getSourceSchemaVersion(self, surveyID):
        """ generated source for method getSourceSchemaVersion """
        schemaVersion = -1
        if surveyID==WSASchema.PTSPROGRAMMEID:
            schemaVersion = 99999
            #  change when sourec available
        elif surveyID==WSASchema.CALPROGRAMMEID:
            schemaVersion = 99999
            #  change when sourec available
        else:
            #  all the others have been in from the start
            schemaVersion = -1
        return schemaVersion

    
    def getDetectionSchemaVersion(self, surveyID):
        """ generated source for method getDetectionSchemaVersion """
        schemaVersion = -1
        if surveyID==WSASchema.PTSPROGRAMMEID:
            schemaVersion = -1
            #  set high as not to be released
        elif surveyID==WSASchema.CALPROGRAMMEID:
            schemaVersion = -1
            #  set high as not to be released
        else:
            #  all the others have been in from the start
            schemaVersion = -1
        return schemaVersion

    
    def getLastSchemaVersion(self, surveyID):
        """ generated source for method getLastSchemaVersion """
        schemaVersion = 99999999
        if True:
            #         case  WSASchema.UKIDSSGPSPROGRAMMEID:
            #             schemaVersion=5; 
            #             break;
            #         case  WSASchema.UKIDSSUDSPROGRAMMEID:
            #             schemaVersion=5; 
            #             break;
            #             
            #  all the others have been in from the start
            schemaVersion = 99999999
        return schemaVersion

    
    def getSourceTableName(self, surveyID):
        """ generated source for method getSourceTableName """
        sourceTableName = str()
        if surveyID==WSASchema.COMMISSIONINGPROGRAMMEID:
            sourceTableName = self.COMMISSIONINGSOURCETABLENAME
        elif surveyID==WSASchema.CALPROGRAMMEID:
            sourceTableName = self.CALSOURCETABLENAME
        elif surveyID==WSASchema.UKIDSSLASPROGRAMMEID:
            sourceTableName = self.UKIDSSLASSOURCETABLENAME
        elif surveyID==WSASchema.UKIDSSGPSPROGRAMMEID:
            sourceTableName = self.UKIDSSGPSSOURCETABLENAME
        elif surveyID==WSASchema.UKIDSSGCSPROGRAMMEID:
            sourceTableName = self.UKIDSSGCSSOURCETABLENAME
        elif surveyID==WSASchema.UKIDSSDXSPROGRAMMEID:
            sourceTableName = self.UKIDSSDXSSOURCETABLENAME
        elif surveyID==WSASchema.UKIDSSUDSPROGRAMMEID:
            sourceTableName = self.UKIDSSUDSSOURCETABLENAME
        elif surveyID==WSASchema.UKIDSSLASSVPROGRAMMEID:
            sourceTableName = self.UKIDSSLASSOURCETABLENAME
        elif surveyID==WSASchema.UKIDSSGPSSVPROGRAMMEID:
            sourceTableName = self.UKIDSSGPSSOURCETABLENAME
        elif surveyID==WSASchema.UKIDSSGCSSVPROGRAMMEID:
            sourceTableName = self.UKIDSSGCSSOURCETABLENAME
        elif surveyID==WSASchema.UKIDSSDXSSVPROGRAMMEID:
            sourceTableName = self.UKIDSSDXSSOURCETABLENAME
        elif surveyID==WSASchema.UKIDSSUDSSVPROGRAMMEID:
            sourceTableName = self.UKIDSSUDSSOURCETABLENAME
        else:
            sourceTableName = None
        return sourceTableName

    
    def getDefaultRGB(self, surveyID):
        """ generated source for method getDefaultRGB """
        RGB = None
        if surveyID==WSASchema.UKIDSSLASPROGRAMMEID:
            RGB = self.LASRGB
        elif surveyID==WSASchema.UKIDSSGPSPROGRAMMEID:
            RGB = self.GPSRGB
        elif surveyID==WSASchema.UKIDSSGCSPROGRAMMEID:
            RGB = self.GCSRGB
        elif surveyID==WSASchema.UKIDSSDXSPROGRAMMEID:
            RGB = self.DXSRGB
        elif surveyID==WSASchema.UKIDSSUDSPROGRAMMEID:
            RGB = self.UDSRGB
        elif surveyID==WSASchema.CALPROGRAMMEID:
            RGB = self.CALRGB
        else:
            return RGB

    
    def getSourceViewName(self, surveyID):
        """ generated source for method getSourceViewName """
        sourceViewName = str()
        if surveyID==WSASchema.UKIDSSLASPROGRAMMEID:
            sourceViewName = self.UKIDSSLASSOURCEVIEWNAME
        elif surveyID==WSASchema.UKIDSSGPSPROGRAMMEID:
            sourceViewName = self.UKIDSSGPSSOURCEVIEWNAME
        elif surveyID==WSASchema.UKIDSSGCSPROGRAMMEID:
            sourceViewName = self.UKIDSSGCSSOURCEVIEWNAME
        elif surveyID==WSASchema.UKIDSSDXSPROGRAMMEID:
            sourceViewName = self.UKIDSSDXSSOURCEVIEWNAME
        elif surveyID==WSASchema.UKIDSSUDSPROGRAMMEID:
            sourceViewName = self.UKIDSSUDSSOURCEVIEWNAME
        else:
            sourceViewName = None
        return sourceViewName

    
    def getDetectionTableName(self, surveyID):
        """ generated source for method getDetectionTableName """
        detectionTableName = str()
        if surveyID==WSASchema.COMMISSIONINGPROGRAMMEID:
            detectionTableName = self.COMMISSIONINGDETECTIONTABLENAME
        elif surveyID==WSASchema.UKIDSSLASPROGRAMMEID:
            detectionTableName = self.UKIDSSLASDETECTIONTABLENAME
        elif surveyID==WSASchema.UKIDSSGPSPROGRAMMEID:
            detectionTableName = self.UKIDSSGPSDETECTIONTABLENAME
        elif surveyID==WSASchema.UKIDSSGCSPROGRAMMEID:
            detectionTableName = self.UKIDSSGCSDETECTIONTABLENAME
        elif surveyID==WSASchema.UKIDSSDXSPROGRAMMEID:
            detectionTableName = self.UKIDSSDXSDETECTIONTABLENAME
        elif surveyID==WSASchema.UKIDSSUDSPROGRAMMEID:
            detectionTableName = self.UKIDSSUDSDETECTIONTABLENAME
        elif surveyID==WSASchema.PTSPROGRAMMEID:
            detectionTableName = self.PTSDETECTIONTABLENAME
        elif surveyID==WSASchema.CALPROGRAMMEID:
            detectionTableName = self.CALDETECTIONTABLENAME
        elif surveyID==WSASchema.UKIDSSLASSVPROGRAMMEID:
            detectionTableName = self.UKIDSSLASDETECTIONTABLENAME
        elif surveyID==WSASchema.UKIDSSGPSSVPROGRAMMEID:
            detectionTableName = self.UKIDSSGPSDETECTIONTABLENAME
        elif surveyID==WSASchema.UKIDSSGCSSVPROGRAMMEID:
            detectionTableName = self.UKIDSSGCSDETECTIONTABLENAME
        elif surveyID==WSASchema.UKIDSSDXSSVPROGRAMMEID:
            detectionTableName = self.UKIDSSDXSDETECTIONTABLENAME
        elif surveyID==WSASchema.UKIDSSUDSSVPROGRAMMEID:
            detectionTableName = self.UKIDSSUDSDETECTIONTABLENAME
        else:
            detectionTableName = None
        return detectionTableName

    
    def getSurveyName(self, surveyID):
        """ generated source for method getSurveyName """
        surveyName = str()
        if surveyID==WSASchema.COMMISSIONINGPROGRAMMEID:
            surveyName = "Commissioning programme"
        elif surveyID==WSASchema.CURATIONTESTPROGRAMMEID:
            surveyName = "Curation test programme"
        elif surveyID==WSASchema.UKIDSSLASPROGRAMMEID:
            surveyName = "UKIDSS Large Area Survey, LAS"
        elif surveyID==WSASchema.UKIDSSGPSPROGRAMMEID:
            surveyName = "UKIDSS Galactic Plane Survey, GPS"
        elif surveyID==WSASchema.UKIDSSGCSPROGRAMMEID:
            surveyName = "UKIDSS Galactic Clusters Survey, GCS"
        elif surveyID==WSASchema.UKIDSSDXSPROGRAMMEID:
            surveyName = "UKIDSS Deep Extragalactic Survey, DXS"
        elif surveyID==WSASchema.UKIDSSUDSPROGRAMMEID:
            surveyName = "UKIDSS Ultra Deep Survey, UDS"
        elif surveyID==WSASchema.PTSPROGRAMMEID:
            surveyName = "Planetary Transit Survey, PTS"
        elif surveyID==WSASchema.CALPROGRAMMEID:
            # surveyName="Calibration programme";
            surveyName = "UKIRT Faint Standard Observations"
        elif surveyID==WSASchema.UKIDSSLASSVPROGRAMMEID:
            surveyName = "UKIDSS Large Area Survey, LAS, Science Verification"
        elif surveyID==WSASchema.UKIDSSGPSSVPROGRAMMEID:
            surveyName = "UKIDSS Galactic Plane Survey, GPS, Science Verification"
        elif surveyID==WSASchema.UKIDSSGCSSVPROGRAMMEID:
            surveyName = "UKIDSS Galactic Clusters Survey, GCS, Science Verification"
        elif surveyID==WSASchema.UKIDSSDXSSVPROGRAMMEID:
            surveyName = "UKIDSS Deep Extragalactic Survey, DXS, Science Verification"
        elif surveyID==WSASchema.UKIDSSUDSSVPROGRAMMEID:
            surveyName = "UKIDSS Ultra Deep Survey, UDS, Science Verification"
        else:
            surveyName = "Survey name not found in lookup"
        return surveyName

    
    def getFilterName(self, filterID):
        """ generated source for method getFilterName """
        if filterID >= 0 and filterID <= len(self.WFCAMFILTERS) and self.WFCAMFILTERS[filterID] != None:
            return self.WFCAMFILTERS[filterID]
        else:
            return "NULL"
        #       String filterName;
        #         switch(filterID){        
        #         case  WSASchema.WFCAMZFILTERID:
        #         filterName="Z";
        #         break;     
        #         case  WSASchema.WFCAMYFILTERID:
        #         filterName="Y";
        #         break;
        #         case  WSASchema.WFCAMJFILTERID:
        #         filterName="J";
        #         break;
        #         case  WSASchema.WFCAMHFILTERID:
        #         filterName="H";
        #         break;
        #         case  WSASchema.WFCAMKFILTERID:
        #             filterName="K";
        #             break;
        #         case  WSASchema.WFCAMH2FILTERID:
        #             filterName="H2";
        #             break;
        #         case  WSASchema.WFCAMBRFILTERID:
        #             filterName="Br";
        #             break;
        #         case  WSASchema.WFCAMBLANKFILTERID:
        #             filterName="BLNK";
        #             break;
        #         case  WSASchema.WFCAMNBJFILTERID:
        #             filterName="BLNK";
        #             break;
        #         default :
        #         filterName="NULL";  
        #         break;
        #     }
        #         return filterName;

    
    def getRequiredFiltersSQL(self):
        """ generated source for method getRequiredFiltersSQL """
        return "select filterid,npass from requiredfilters"

    
    
    def getFramesetSQL(self, fID, mergelogTable, multiframeTable):
        """ generated source for method getFramesetSQL """
        sbSQL = ""
        mfidName = None
        npass = int()
        filterid = int()
        row = 0
        first = True
        # 
        # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
        # sb1.append(", "+getFilterName(i+1)+"PetroMag");
        #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
        # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
        # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
        # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
        #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
        #  bands[bandCount]=getFilterName(i+1);
        #  bands[bandCount]=getFilterName(i+1)+"_"+(j+1);
        #  change when sourec available
        #  change when sourec available
        #  all the others have been in from the start
        #  set high as not to be released
        #  set high as not to be released
        #  all the others have been in from the start
        #         case  WSASchema.UKIDSSGPSPROGRAMMEID:
        #             schemaVersion=5; 
        #             break;
        #         case  WSASchema.UKIDSSUDSPROGRAMMEID:
        #             schemaVersion=5; 
        #             break;
        #             
        #  all the others have been in from the start
        # surveyName="Calibration programme";
        #       String filterName;
        #         switch(filterID){        
        #         case  WSASchema.WFCAMZFILTERID:
        #         filterName="Z";
        #         break;     
        #         case  WSASchema.WFCAMYFILTERID:
        #         filterName="Y";
        #         break;
        #         case  WSASchema.WFCAMJFILTERID:
        #         filterName="J";
        #         break;
        #         case  WSASchema.WFCAMHFILTERID:
        #         filterName="H";
        #         break;
        #         case  WSASchema.WFCAMKFILTERID:
        #             filterName="K";
        #             break;
        #         case  WSASchema.WFCAMH2FILTERID:
        #             filterName="H2";
        #             break;
        #         case  WSASchema.WFCAMBRFILTERID:
        #             filterName="Br";
        #             break;
        #         case  WSASchema.WFCAMBLANKFILTERID:
        #             filterName="BLNK";
        #             break;
        #         case  WSASchema.WFCAMNBJFILTERID:
        #             filterName="BLNK";
        #             break;
        #         default :
        #         filterName="NULL";  
        #         break;
        #     }
        #         return filterName;
        j = 0
        while j < fID.length:
            # 
            # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
            # sb1.append(", "+getFilterName(i+1)+"PetroMag");
            #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
            # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
            # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
            # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
            #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
            #  bands[bandCount]=getFilterName(i+1);
            #  bands[bandCount]=getFilterName(i+1)+"_"+(j+1);
            #  change when sourec available
            #  change when sourec available
            #  all the others have been in from the start
            #  set high as not to be released
            #  set high as not to be released
            #  all the others have been in from the start
            #         case  WSASchema.UKIDSSGPSPROGRAMMEID:
            #             schemaVersion=5; 
            #             break;
            #         case  WSASchema.UKIDSSUDSPROGRAMMEID:
            #             schemaVersion=5; 
            #             break;
            #             
            #  all the others have been in from the start
            # surveyName="Calibration programme";
            #       String filterName;
            #         switch(filterID){        
            #         case  WSASchema.WFCAMZFILTERID:
            #         filterName="Z";
            #         break;     
            #         case  WSASchema.WFCAMYFILTERID:
            #         filterName="Y";
            #         break;
            #         case  WSASchema.WFCAMJFILTERID:
            #         filterName="J";
            #         break;
            #         case  WSASchema.WFCAMHFILTERID:
            #         filterName="H";
            #         break;
            #         case  WSASchema.WFCAMKFILTERID:
            #             filterName="K";
            #             break;
            #         case  WSASchema.WFCAMH2FILTERID:
            #             filterName="H2";
            #             break;
            #         case  WSASchema.WFCAMBRFILTERID:
            #             filterName="Br";
            #             break;
            #         case  WSASchema.WFCAMBLANKFILTERID:
            #             filterName="BLNK";
            #             break;
            #         case  WSASchema.WFCAMNBJFILTERID:
            #             filterName="BLNK";
            #             break;
            #         default :
            #         filterName="NULL";  
            #         break;
            #     }
            #         return filterName;
            filterid = j
            npass = fID[j]
            row = row + npass
            if npass == 1:
                mfidName = WSASchema.getFilterName(filterid) + "mfid"
                if not first:
                    sbSQL.append(" or ")
                first = False
                sbSQL.append(mergelogTable + "." + mfidName + "=" + multiframeTable + ".multiframeid")
            else:
                if npass > 1:
                    # 
                    # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
                    # sb1.append(", "+getFilterName(i+1)+"PetroMag");
                    #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
                    # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
                    # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
                    # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
                    #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
                    #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
                    #  bands[bandCount]=getFilterName(i+1);
                    #  bands[bandCount]=getFilterName(i+1)+"_"+(j+1);
                    #  change when sourec available
                    #  change when sourec available
                    #  all the others have been in from the start
                    #  set high as not to be released
                    #  set high as not to be released
                    #  all the others have been in from the start
                    #         case  WSASchema.UKIDSSGPSPROGRAMMEID:
                    #             schemaVersion=5; 
                    #             break;
                    #         case  WSASchema.UKIDSSUDSPROGRAMMEID:
                    #             schemaVersion=5; 
                    #             break;
                    #             
                    #  all the others have been in from the start
                    # surveyName="Calibration programme";
                    #       String filterName;
                    #         switch(filterID){        
                    #         case  WSASchema.WFCAMZFILTERID:
                    #         filterName="Z";
                    #         break;     
                    #         case  WSASchema.WFCAMYFILTERID:
                    #         filterName="Y";
                    #         break;
                    #         case  WSASchema.WFCAMJFILTERID:
                    #         filterName="J";
                    #         break;
                    #         case  WSASchema.WFCAMHFILTERID:
                    #         filterName="H";
                    #         break;
                    #         case  WSASchema.WFCAMKFILTERID:
                    #             filterName="K";
                    #             break;
                    #         case  WSASchema.WFCAMH2FILTERID:
                    #             filterName="H2";
                    #             break;
                    #         case  WSASchema.WFCAMBRFILTERID:
                    #             filterName="Br";
                    #             break;
                    #         case  WSASchema.WFCAMBLANKFILTERID:
                    #             filterName="BLNK";
                    #             break;
                    #         case  WSASchema.WFCAMNBJFILTERID:
                    #             filterName="BLNK";
                    #             break;
                    #         default :
                    #         filterName="NULL";  
                    #         break;
                    #     }
                    #         return filterName;
                    i  = 0
                    while i < npass:
                        # 
                        # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
                        # sb1.append(", "+getFilterName(i+1)+"PetroMag");
                        #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
                        # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
                        # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
                        # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
                        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
                        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
                        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
                        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
                        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
                        #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
                        #  bands[bandCount]=getFilterName(i+1);
                        #  bands[bandCount]=getFilterName(i+1)+"_"+(j+1);
                        #  change when sourec available
                        #  change when sourec available
                        #  all the others have been in from the start
                        #  set high as not to be released
                        #  set high as not to be released
                        #  all the others have been in from the start
                        #         case  WSASchema.UKIDSSGPSPROGRAMMEID:
                        #             schemaVersion=5; 
                        #             break;
                        #         case  WSASchema.UKIDSSUDSPROGRAMMEID:
                        #             schemaVersion=5; 
                        #             break;
                        #             
                        #  all the others have been in from the start
                        # surveyName="Calibration programme";
                        #       String filterName;
                        #         switch(filterID){        
                        #         case  WSASchema.WFCAMZFILTERID:
                        #         filterName="Z";
                        #         break;     
                        #         case  WSASchema.WFCAMYFILTERID:
                        #         filterName="Y";
                        #         break;
                        #         case  WSASchema.WFCAMJFILTERID:
                        #         filterName="J";
                        #         break;
                        #         case  WSASchema.WFCAMHFILTERID:
                        #         filterName="H";
                        #         break;
                        #         case  WSASchema.WFCAMKFILTERID:
                        #             filterName="K";
                        #             break;
                        #         case  WSASchema.WFCAMH2FILTERID:
                        #             filterName="H2";
                        #             break;
                        #         case  WSASchema.WFCAMBRFILTERID:
                        #             filterName="Br";
                        #             break;
                        #         case  WSASchema.WFCAMBLANKFILTERID:
                        #             filterName="BLNK";
                        #             break;
                        #         case  WSASchema.WFCAMNBJFILTERID:
                        #             filterName="BLNK";
                        #             break;
                        #         default :
                        #         filterName="NULL";  
                        #         break;
                        #     }
                        #         return filterName;
                        mfidName = WSASchema.getFilterName(filterid) + "_" + (i + 1) + "mfid"
                        if not first:
                            sbSQL.append(" or ")
                        first = False
                        sbSQL.append(mergelogTable + "." + mfidName + "=" + multiframeTable + ".multiframeid")
                        i += 1
            j += 1
        if row > 0:
            return sbSQL
        else:
            return None

    
    def getCrossIDTableTable(self, community, user, baseTable, intProgrammeID):
        """ generated source for method getCrossIDTableTable """
        if community.lower() == "nonSurvey".lower():
            return user.trim() + baseTable
        else:
            if baseTable.lower() == "sourceView".lower():
                return WSASchema.getSourceViewName(intProgrammeID)
            else:
                if baseTable.lower() == "source".lower():
                    return WSASchema.getSourceTableName(intProgrammeID)
                if baseTable.lower() == "detection".lower():
                    return WSASchema.getDetectionTableName(intProgrammeID)
            return ""

   


    def getFramesetSQL_0(self, rset, mergelogTable, multiframeTable):
        """ generated source for method getFramesetSQL_0 """
        sbSQL = ""
        mfidName = None
        npass = int()
        filterid = int()
        row = 0
        first = True
        try:
            while rset.next():
                row += 1
                filterid = rset.getInt("filterid")
                npass = rset.getInt("npass")
                if npass == 1:
                    mfidName = WSASchema.getFilterName(filterid) + "mfid"
                    if not first:
                        sbSQL.append(" or ")
                    first = False
                    sbSQL.append(mergelogTable + "." + mfidName + "=" + multiframeTable + ".multiframeid")
                else:
                    if npass > 1:
                        # 
                        # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
                        # sb1.append(", "+getFilterName(i+1)+"PetroMag");
                        #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
                        # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
                        # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
                        # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
                        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
                        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
                        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
                        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
                        #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
                        #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
                        #  bands[bandCount]=getFilterName(i+1);
                        #  bands[bandCount]=getFilterName(i+1)+"_"+(j+1);
                        #  change when sourec available
                        #  change when sourec available
                        #  all the others have been in from the start
                        #  set high as not to be released
                        #  set high as not to be released
                        #  all the others have been in from the start
                        #         case  WSASchema.UKIDSSGPSPROGRAMMEID:
                        #             schemaVersion=5; 
                        #             break;
                        #         case  WSASchema.UKIDSSUDSPROGRAMMEID:
                        #             schemaVersion=5; 
                        #             break;
                        #             
                        #  all the others have been in from the start
                        # surveyName="Calibration programme";
                        #       String filterName;
                        #         switch(filterID){        
                        #         case  WSASchema.WFCAMZFILTERID:
                        #         filterName="Z";
                        #         break;     
                        #         case  WSASchema.WFCAMYFILTERID:
                        #         filterName="Y";
                        #         break;
                        #         case  WSASchema.WFCAMJFILTERID:
                        #         filterName="J";
                        #         break;
                        #         case  WSASchema.WFCAMHFILTERID:
                        #         filterName="H";
                        #         break;
                        #         case  WSASchema.WFCAMKFILTERID:
                        #             filterName="K";
                        #             break;
                        #         case  WSASchema.WFCAMH2FILTERID:
                        #             filterName="H2";
                        #             break;
                        #         case  WSASchema.WFCAMBRFILTERID:
                        #             filterName="Br";
                        #             break;
                        #         case  WSASchema.WFCAMBLANKFILTERID:
                        #             filterName="BLNK";
                        #             break;
                        #         case  WSASchema.WFCAMNBJFILTERID:
                        #             filterName="BLNK";
                        #             break;
                        #         default :
                        #         filterName="NULL";  
                        #         break;
                        #     }
                        #         return filterName;
                        i = 0
                        while i < npass:
                            # 
                            # sb1.append(", "+getFilterName(i+1)+"HallMag"); 
                            # sb1.append(", "+getFilterName(i+1)+"PetroMag");
                            #  sb1.append(", "+getFilterName(i+1)+"AperMag3");
                            # sb1.append(", "+getFilterName(i+1)+"HallMagErr"); 
                            # sb1.append(", "+getFilterName(i+1)+"PetroMagErr");
                            # sb1.append(", "+getFilterName(i+1)+"AperMag3Err");
                            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMag");
                            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMag");
                            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3");
                            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"HallMagErr");
                            #   sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"PetroMagErr");
                            #  sb1.append(", "+getFilterName(i+1)+"_"+(j+1)+"AperMag3Err");
                            #  bands[bandCount]=getFilterName(i+1);
                            #  bands[bandCount]=getFilterName(i+1)+"_"+(j+1);
                            #  change when sourec available
                            #  change when sourec available
                            #  all the others have been in from the start
                            #  set high as not to be released
                            #  set high as not to be released
                            #  all the others have been in from the start
                            #         case  WSASchema.UKIDSSGPSPROGRAMMEID:
                            #             schemaVersion=5; 
                            #             break;
                            #         case  WSASchema.UKIDSSUDSPROGRAMMEID:
                            #             schemaVersion=5; 
                            #             break;
                            #             
                            #  all the others have been in from the start
                            # surveyName="Calibration programme";
                            #       String filterName;
                            #         switch(filterID){        
                            #         case  WSASchema.WFCAMZFILTERID:
                            #         filterName="Z";
                            #         break;     
                            #         case  WSASchema.WFCAMYFILTERID:
                            #         filterName="Y";
                            #         break;
                            #         case  WSASchema.WFCAMJFILTERID:
                            #         filterName="J";
                            #         break;
                            #         case  WSASchema.WFCAMHFILTERID:
                            #         filterName="H";
                            #         break;
                            #         case  WSASchema.WFCAMKFILTERID:
                            #             filterName="K";
                            #             break;
                            #         case  WSASchema.WFCAMH2FILTERID:
                            #             filterName="H2";
                            #             break;
                            #         case  WSASchema.WFCAMBRFILTERID:
                            #             filterName="Br";
                            #             break;
                            #         case  WSASchema.WFCAMBLANKFILTERID:
                            #             filterName="BLNK";
                            #             break;
                            #         case  WSASchema.WFCAMNBJFILTERID:
                            #             filterName="BLNK";
                            #             break;
                            #         default :
                            #         filterName="NULL";  
                            #         break;
                            #     }
                            #         return filterName;
                            mfidName = WSASchema.getFilterName(filterid) + "_" + (i + 1) + "mfid"
                            if not first:
                                sbSQL.append(" or ")
                            first = False
                            sbSQL.append(mergelogTable + "." + mfidName + "=" + multiframeTable + ".multiframeid")
                            i += 1
            if row > 0:
                return sbSQL
            else:
                return None
        except Exception as se:
            return None
