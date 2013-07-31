"""
VSASchema module

University of Edinburgh, WAFU
@author: Stelios Voutsinas
"""

from imageList.WSASchema import *

class VSASchema:
    WORLDCOMMDB = ""
    WORLDVHSDB = ""
    WORLDVVVDB = ""
    WORLDVMCDB = ""
    WORLDVIKINGDB = ""
    WORLDVIDEODB = ""
    WORLDULTRAVISTADB = ""
    WORLDSVORIONDB = ""
    WORLDSVNGC253DB = ""
    WORLDCALDB = ""
    WORLDGCSDB = ""

    # 
    #      * Offset between extension numbers held in database and what cfitsio
    #      * expects.
    #      
    LATESTUKIDSSDATABASE = "ukidssdr5plus"

    #  "ukidssedr";
    LATESTWORLDDATABASE = "ukidssdr3plus"
    SCEHMAVALIDDATABASES = ["ukidssdr5plus", "ukidssdr4plus", "ukidssdr3plus"]

    # ,"ukidssdr2plus","ukidssdr1plus","ukidssdr1","ukidssedrplus","ukidssedr","ukidssr2"}; // array of databases compatible with latest schema for source & detection
    LATESTUKIDSSSCHEMA = "ukidssdr5plus"

    #  "ukidssedr";
    LATESTWORLDSCHEMA = "ukidssdr3plus"

    #  latest world release have the same schema as this release
    LATESTNONSURVEYSCHEMA = "ukidssr2"

    #  all non survey progs have the same schema as this release
    PRERELEASESERVER = "ramses2"

    # "ahmose";
    PRERELEASEDATABASE = "VSA"

    # "WSA";"WSA copy"
    PRERELEASECOMMUNITY = "prerelease"
    NONSURVEYCOMMUNITY = "nonSurvey"
    PROPRIETARYCOMMUNITY = "Proprietary"
    NODBAVAILABLE = "No database available"

    #  ADD new DBS on right of array;
    COMMDB = []
    VHSDB = []
    VVVDB = []
    VMCDB = []
    VIKINGDB = []
    VIDEODB = []
    ULTRAVISTADB = []
    SVORIONDB = []

    # {"VISTASVORION","VISTASVORION2"};
    SVNGC253DB = []
    CALDB = []
    GCSDB = ["UKIDSSDR5PLUS", "UKIDSSDR3PLUS", "UKIDSSDR2PLUS"]
    SURVEYACRONYMS = ["VHS", "VVV", "VMC", "VIKING", "VIDEO", "ULTRAVISTA", "VVVE", "VVVS"]
    COMMVERISONSCHEMA = 0
    VHSVERISONSCHEMA = 0
    VVVVERISONSCHEMA = 0
    VMCVERISONSCHEMA = 0
    VIKINGVERISONSCHEMA = 0
    VIDEOVERISONSCHEMA = 0
    ULTRAVISTAVERISONSCHEMA = 0
    SVORIONVERISONSCHEMA = 0
    SVNGC253VERISONSCHEMA = 0
    CALVERSIONSCHEMA = 0
    ALLUKIDSSRDB = ["UKIDSSDR5PLUS", "UKIDSSDR4PLUS", "UKIDSSDR3PLUS", "UKIDSSDR2PLUS", "UKIDSSDR1PLUS", "UKIDSSDR1", "UKIDSSEDRPLUS", "UKIDSSEDR", "UKIDSSSV", "UKIDSSR1"]

    #  names for UKIDSS releaase
    UKIDSSVERSIONS = [7, 6, 5, 4, 3, 3, 2, 2, 1, 0]

    #  version numbers for UKIDSS releaase
    UKIDSSTESTERDB = ALLUKIDSSRDB
    VERSIONOFSOURCEVIEWS = 99
    PTSDB = "Transit"

    #  public static final String CALDB="Calibration";
    OPENDB = "WFCAMOPENTIME"
    extraDB = [PRERELEASEDATABASE]
    moreDB = [OPENDB]
    doExtraDB = True
    UKIDSSDB = ["UKIDSSDR5PLUS", "UKIDSSDR4PLUS", "UKIDSSDR3PLUS", "UKIDSSDR2PLUS", "UKIDSSDR1PLUS", "UKIDSSDR1", "UKIDSSEDRPLUS", "UKIDSSEDR", "UKIDSSSV"]
    WORLDDB = ["UKIDSSDR3PLUS", "UKIDSSDR2PLUS", "UKIDSSDR1PLUS", "UKIDSSDR1", "UKIDSSEDRPLUS", "UKIDSSEDR", "UKIDSSSV"]

    # "WORLDR2" names for world releaase
    WORLDVERSIONS = [5, 4, 3, 3, 2, 2, 1]

    #  version numbers for WORLD releaase
    PRERELEASEDB = [PRERELEASEDATABASE]
    accessToRollingDB = True

    #  should the archive listing etc give the option of querying a rolling DB 
    rollingDB = "VSA_DailySync"

    # "WSA";"WSA copy"
    actualRollingDB = "VISTAPROPRIETY"
    prevRollingDB = "VSA_DailySync"
    prevActualRollingDB = "VISTAPROPRIETY"
    rollingDBs = [rollingDB, prevRollingDB]
    actualRollingDBs = [actualRollingDB, prevActualRollingDB]
    rollingServer = "amenhotep"

    # "amenhotep";//"ahmose";
    rollingDBUser = "wsaro"
    rollingDBPasswd = "wsaropw"
    noAccessDB = "sorry no access"
    COMMISSIONINGPROGRAMMEID = 1
    CURATIONTESTPROGRAMMEID = 99
    ALLPROGRAMMEID = 0
    VHSPROGRAMMEID = 110
    VVVPROGRAMMEID = 120
    VMCPROGRAMMEID = 130
    VIKINGPROGRAMMEID = 140
    VIDEOPROGRAMMEID = 150
    ULTRAVISTAPROGRAMMEID = 160
    SVORIONPROGRAMMEID = 100
    SVNGC253PROGRAMMEID = 101
    CALPROGRAMMEID = 200
    ALLSURVEYS = "All VISTA surveys"
    MAXIMUMROWSTOFILE = 15000000

    #  maximum number of rows written to file by Queries not used yet
    FITS_OFFSET = 1
    VISTANONEFILTERID = 0
    VISTAZFILTERID = 1
    VISTAYFILTERID = 2
    VISTAJFILTERID = 3
    VISTAHFILTERID = 4
    VISTAKFILTERID = 5
    VISTAH2FILTERID = 6
    VISTABRFILTERID = 7
    VISTABLANKFILTERID = 8
    VISTANB118FILTERID = 9
    VISTANB980985FILTERID = 10
    VISTANSUNBLINDFILTERID = 11
    ALLFILTERID = [VISTANONEFILTERID, VISTAZFILTERID, VISTAYFILTERID, VISTAJFILTERID, VISTAHFILTERID, VISTAKFILTERID, VISTABLANKFILTERID, VISTANB118FILTERID, VISTANB980985FILTERID, VISTANSUNBLINDFILTERID]
    VISTAPROGS = [SVORIONPROGRAMMEID, SVNGC253PROGRAMMEID, VHSPROGRAMMEID, VVVPROGRAMMEID, VMCPROGRAMMEID, VIKINGPROGRAMMEID, VIDEOPROGRAMMEID, ULTRAVISTAPROGRAMMEID, CALPROGRAMMEID]
    VISTATESTERPROGS = []
    i = 0

    #  public static final int [] VISTATESTERPROGS={COMMISSIONINGPROGRAMMEID,VISTAPROGS};
    COMMDETECTIONTABLENAME = "commDetection"
    VHSDETECTIONTABLENAME = "vhsDetection"
    VVVDETECTIONTABLENAME = "vvvDetection"
    VMCDETECTIONTABLENAME = "vmcDetection"
    VIKINGDETECTIONTABLENAME = "vikingDetection"
    VIDEODETECTIONTABLENAME = "videoDetection"
    ULTRAVISTADETECTIONTABLENAME = "ultravistaDetection"
    SVORIONDETECTIONTABLENAME = "svOrionDetection"
    SVNGC253DETECTIONTABLENAME = "svNgc253Detection"
    CALDETECTIONTABLENAME = "calDetection"
    COMMSOURCETABLENAME = "commSource"
    VHSSOURCETABLENAME = "vhsSource"
    VVVSOURCETABLENAME = "vvvSource"
    VMCSOURCETABLENAME = "vmcSource"
    VIKINGSOURCETABLENAME = "vikingSource"
    VIDEOSOURCETABLENAME = "videoSource"
    ULTRAVISTASOURCETABLENAME = "ultravistaSource"
    SVORIONSOURCETABLENAME = "svOrionSource"
    SVNGC253SOURCETABLENAME = "svNgc253Source"
    CALSOURCETABLENAME = "NONE"
    COMMSOURCEVIEWNAME = "undef"
    VHSSOURCEVIEWNAME = "undef"
    VVVSOURCEVIEWNAME = "undef"
    VMCSOURCEVIEWNAME = "undef"
    VIKINGSOURCEVIEWNAME = "undef"
    VIDEOSOURCEVIEWNAME = "undef"
    ULTRAVISTASOURCEVIEWNAME = "undef"
    SVORIONSOURCEVIEWNAME = "undef"
    SVNGC253SOURCEVIEWNAME = "undef"
    CALSOURCEVIEWNAME = "undef"
    UKIDSSLASSOURCEVIEWNAME = "lasYJHKsource"
    UKIDSSGPSSOURCEVIEWNAME = "gpsJHKsource"
    UKIDSSGCSSOURCEVIEWNAME = "gcsZYJHKsource"
    UKIDSSDXSSOURCEVIEWNAME = "dxsJKSource"
    UKIDSSUDSSOURCEVIEWNAME = None
    PTSSOURCETABLENAME = "NONE"
    COMMISSIONINGMERGETABLENAME = "commMergeLog"
    UKIDSSLASMERGETABLENAME = "lasMergeLog"
    UKIDSSGPSMERGETABLENAME = "gpsMergeLog"
    UKIDSSGCSMERGETABLENAME = "gcsMergeLog"
    UKIDSSDXSMERGETABLENAME = "dxsMergeLog"
    UKIDSSUDSMERGETABLENAME = "udsMergeLog"
    UKIDSSPTSMERGETABLENAME = "NONE"
    VHSMERGETABLENAME = "vhsMergeLog"
    VVVMERGETABLENAME = "vvvMergeLog"
    VMCMERGETABLENAME = "vmcMergeLog"
    VIKINGMERGETABLENAME = "vikingMergeLog"
    VIDEOMERGETABLENAME = "videoMergeLog"
    ULTRAVISTAMERGETABLENAME = "ultravistaMergeLog"
    SVORIONMERGETABLENAME = "svOrionMergeLog"
    SVNGC253MERGETABLENAME = "svNgc253MergeLog"
    DEFAULTSQLSERVER = "amenhotep"
    UKIDSSLOGIN = "wsaro"
    WORLDLOGIN = "worldwsaro"
    WFCAMFILTERS = ["NONE", "Z", "Y", "J", "H", "K", "H2", "Br", "BLNK", "NBJ", "NBH", "NBK"]
    VISTAFILTERS = ["NONE", "Z", "Y", "J", "H", "Ks", "H2", "Br", "BLNK", "NB118", "NB980", "SUNBLIND"]
    filters = WFCAMFILTERS

    # deprecated
    COMMISSIONINGBands = ["Z", "Y", "J", "H", "K", "H2", "Br"]

    #                                                   0 1 2 3 4 5 6 7 8 9
    COMMISSIONINGREQFILTERS = [0, 1, 1, 1, 1, 1, 0, 0, 0, 1]
    SVORIONREQFILTERS = [0, 1, 1, 1, 1, 1, 0, 0, 0, 0]
    SVNGC253REQFILTERS = [0, 1, 0, 1, 0, 0, 0, 0, 0, 1]
    VHSREQFILTERS = [0, 0, 1, 1, 1, 1, 0, 0, 0, 0]
    VVVREQFILTERS = [0, 1, 1, 1, 1, 1, 0, 0, 0, 0]
    VMCREQFILTERS = [0, 0, 1, 1, 0, 1, 0, 0, 0, 0]
    VIKINGREQFILTERS = [0, 1, 1, 1, 1, 1, 0, 0, 0, 0]
    VIDEOREQFILTERS = [0, 1, 1, 1, 1, 1, 0, 0, 0, 0]
    ULTRAVISTAREQFILTERS = [0, 1, 1, 1, 1, 1, 0, 0, 0, 0]
    CALREQFILTERS = [0, 1, 1, 1, 1, 1, 0, 0, 0, 1]
    LASRGB = ["K", "H", "Y"]
    GPSRGB = ["K", "H", "J"]
    GCSRGB = ["K", "H", "Z"]
    DXSRGB = ["K", "H", "J"]
    UDSRGB = ["K", "H", "J"]
    VHSRGB = ["K", "H", "Y"]
    VVVRGB = ["Ks", "H", "J"]
    VHSBands = ["Y", "J", "H", "K"]
    VVVBands = ["Z", "Y", "J", "H", "Ks"]
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

    
    def getVersionNoOfRelease(self, schema, surveyID):
        """ generated source for method getVersionNoOfRelease """
        dbs = []
        if surveyID==VSASchema.COMMISSIONINGPROGRAMMEID:
            dbs = self.COMMDB
        elif surveyID==VSASchema.SVORIONPROGRAMMEID:
            dbs = self.SVORIONDB
        elif surveyID==VSASchema.SVNGC253PROGRAMMEID:
            dbs = self.SVNGC253DB
        elif surveyID==VSASchema.VHSPROGRAMMEID:
            dbs = self.VHSDB
        elif surveyID==VSASchema.VVVPROGRAMMEID:
            dbs = self.VVVDB
        elif surveyID==VSASchema.VMCPROGRAMMEID:
            dbs = self.VMCDB
        elif surveyID==VSASchema.VIKINGPROGRAMMEID:
            dbs = self.VIKINGDB
        elif surveyID==VSASchema.VIDEOPROGRAMMEID:
            dbs = self.VIDEODB
        elif surveyID==VSASchema.ULTRAVISTAPROGRAMMEID:
            dbs = self.ULTRAVISTADB
        elif surveyID==VSASchema.CALPROGRAMMEID:
            dbs = self.CALDB
        else:
            i = 0
            while i < dbs.length:
                if schema.lower() == dbs[i].lower():
                    return i
                i += 1
            if schema.lower() == self.rollingDB.lower():
                return 9999
            return -1

    
    def getReqFilters(self, database, surveyID):
        """ generated source for method getReqFilters """
        if surveyID==VSASchema.COMMISSIONINGPROGRAMMEID:
            return self.COMMISSIONINGREQFILTERS
        elif surveyID==VSASchema.SVORIONPROGRAMMEID:
            return self.SVORIONREQFILTERS
        elif surveyID==VSASchema.SVNGC253PROGRAMMEID:
            return self.SVNGC253REQFILTERS
        elif surveyID==VSASchema.VHSPROGRAMMEID:
            return self.VHSREQFILTERS
        elif surveyID==VSASchema.VVVPROGRAMMEID:
            return self.VVVREQFILTERS
        elif surveyID==VSASchema.VMCPROGRAMMEID:
            return self.VMCREQFILTERS
        elif surveyID==VSASchema.VIKINGPROGRAMMEID:
            return self.VIKINGREQFILTERS
        elif surveyID==VSASchema.VIDEOPROGRAMMEID:
            return self.VIDEOREQFILTERS
        elif surveyID==VSASchema.ULTRAVISTAPROGRAMMEID:
            return self.ULTRAVISTAREQFILTERS
        elif surveyID==VSASchema.CALPROGRAMMEID:
            return self.CALREQFILTERS
        elif surveyID==VSASchema.UKIDSSLASSVPROGRAMMEID:
            return self.LASREQFILTERS
        elif surveyID==VSASchema.UKIDSSGPSPROGRAMMEID:
            if self.getVersionNoOfRelease(database) >= self.getVersionNoOfRelease("ukidssdr3plus") or database == "" or database == None:
                return self.GPSREQFILTERS
            else:
                return self.GPSREQFILTERSDR2
        elif surveyID==VSASchema.UKIDSSGPSSVPROGRAMMEID:
            if self.getVersionNoOfRelease(database) >= self.getVersionNoOfRelease("ukidssdr3plus"):
                return self.GPSREQFILTERS
            else:
                return self.GPSREQFILTERSDR2
        else:
            return None

    
    def getReqFilters_0(self, surveyID):
        """ generated source for method getReqFilters_0 """
        if surveyID==VSASchema.COMMISSIONINGPROGRAMMEID:
            return self.COMMISSIONINGREQFILTERS
        elif surveyID==VSASchema.SVORIONPROGRAMMEID:
            return self.SVORIONREQFILTERS
        elif surveyID==VSASchema.SVNGC253PROGRAMMEID:
            return self.SVNGC253REQFILTERS
        elif surveyID==VSASchema.VHSPROGRAMMEID:
            return self.VHSREQFILTERS
        elif surveyID==VSASchema.VVVPROGRAMMEID:
            return self.VVVREQFILTERS
        elif surveyID==VSASchema.VMCPROGRAMMEID:
            return self.VMCREQFILTERS
        elif surveyID==VSASchema.VIKINGPROGRAMMEID:
            return self.VIKINGREQFILTERS
        elif surveyID==VSASchema.VIDEOPROGRAMMEID:
            return self.VIDEOREQFILTERS
        elif surveyID==VSASchema.ULTRAVISTAPROGRAMMEID:
            return self.ULTRAVISTAREQFILTERS
        elif surveyID==VSASchema.CALPROGRAMMEID:
            return self.CALREQFILTERS
        else:
            return None

    # 
    #         *   public static final int VHSPROGRAMMEID=110;
    #         public static final int VVVPROGRAMMEID=120;
    #         public static final int VMCPROGRAMMEID=120;
    #         public static final int VIKINGPROGRAMMEID=140;
    #         public static final int VIDEOPROGRAMMEID=150;
    #         public static final int ULTRAVISTAPROGRAMMEID=160;
    #         public static final int SVORIONPROGRAMMEID=100;
    #         public static final int SVNGC253PROGRAMMEID=101;
    #         
    
    def getDBs(self, programmeid, world):
        """ generated source for method getDBs """
        if programmeid==self.ALLPROGRAMMEID:
            if world:
                return None
            else:
                return self.PRERELEASEDB
        elif programmeid==VSASchema.COMMISSIONINGPROGRAMMEID:
            if world:
                return self.WORLDCOMMDB
            else:
                return self.COMMDB
        elif programmeid==VSASchema.SVORIONPROGRAMMEID:
            if world:
                return self.WORLDSVORIONDB
            else:
                return self.SVORIONDB
        elif programmeid==VSASchema.SVNGC253PROGRAMMEID:
            if world:
                return self.WORLDSVNGC253DB
            else:
                return self.SVNGC253DB
        elif programmeid==VSASchema.VHSPROGRAMMEID:
            if world:
                return self.WORLDVHSDB
            else:
                return self.VHSDB
        elif programmeid==VSASchema.VVVPROGRAMMEID:
            if world:
                return self.WORLDVVVDB
            else:
                return self.VVVDB
        elif programmeid==VSASchema.VMCPROGRAMMEID:
            if world:
                return self.WORLDVMCDB
            else:
                return self.VMCDB
        elif programmeid==VSASchema.VIKINGPROGRAMMEID:
            if world:
                return self.WORLDVIKINGDB
            else:
                return self.VIKINGDB
        elif programmeid==VSASchema.VIDEOPROGRAMMEID:
            if world:
                return self.WORLDVIDEODB
            else:
                return self.VIDEODB
        elif programmeid==VSASchema.ULTRAVISTAPROGRAMMEID:
            if world:
                return self.WORLDULTRAVISTADB
            else:
                return self.ULTRAVISTADB
        elif programmeid==VSASchema.CALPROGRAMMEID:
            if world:
                return self.WORLDCALDB
            else:
                return self.CALDB
        elif programmeid==WSASchema.UKIDSSGCSPROGRAMMEID:
            if world:
                return self.WORLDGCSDB
            else:
                return self.GCSDB
        else:
            return None

    
    def getDBsPlusRollingDB(self, inDBs):
        """ generated source for method getDBsPlusRollingDB """
        if inDBs == None:
            return None
        retDBs = [None]*inDBs.length + 1
        # 
        #         *   public static final int VHSPROGRAMMEID=110;
        #         public static final int VVVPROGRAMMEID=120;
        #         public static final int VMCPROGRAMMEID=120;
        #         public static final int VIKINGPROGRAMMEID=140;
        #         public static final int VIDEOPROGRAMMEID=150;
        #         public static final int ULTRAVISTAPROGRAMMEID=160;
        #         public static final int SVORIONPROGRAMMEID=100;
        #         public static final int SVNGC253PROGRAMMEID=101;
        #         
        i = 0
        while i < inDBs.length:
            # 
            #         *   public static final int VHSPROGRAMMEID=110;
            #         public static final int VVVPROGRAMMEID=120;
            #         public static final int VMCPROGRAMMEID=120;
            #         public static final int VIKINGPROGRAMMEID=140;
            #         public static final int VIDEOPROGRAMMEID=150;
            #         public static final int ULTRAVISTAPROGRAMMEID=160;
            #         public static final int SVORIONPROGRAMMEID=100;
            #         public static final int SVNGC253PROGRAMMEID=101;
            #         
            retDBs[i] = inDBs[i]
            i += 1
        retDBs[retDBs.length - 1] = VSASchema.rollingDB
        return retDBs



    
    def getSchemaValidVersion(self, progID):
        """ generated source for method getSchemaValidVersion """
        if progID==VSASchema.COMMISSIONINGPROGRAMMEID:
            return self.COMMVERISONSCHEMA
        elif progID==VSASchema.SVORIONPROGRAMMEID:
            return self.SVORIONVERISONSCHEMA
        elif progID==VSASchema.SVNGC253PROGRAMMEID:
            return self.SVNGC253VERISONSCHEMA
        elif progID==VSASchema.VHSPROGRAMMEID:
            return self.VHSVERISONSCHEMA
        elif progID==VSASchema.VVVPROGRAMMEID:
            return self.VVVVERISONSCHEMA
        elif progID==VSASchema.VMCPROGRAMMEID:
            return self.VMCVERISONSCHEMA
        elif progID==VSASchema.VIKINGPROGRAMMEID:
            return self.VIKINGVERISONSCHEMA
        elif progID==VSASchema.VIDEOPROGRAMMEID:
            return self.VIDEOVERISONSCHEMA
        elif progID==VSASchema.ULTRAVISTAPROGRAMMEID:
            return self.ULTRAVISTAVERISONSCHEMA
        else:
            return 9999

    
    
    def getDefaultList(self, reqFilters, table, database, progID):
        """ generated source for method getDefaultList """
        validSchema = False
        if self.getVersionNoOfRelease(database, progID) >= self.getSchemaValidVersion(progID):
            validSchema = True
        database = database.replaceAll("(?i)world", "ukidss")
        # 
        #         *   public static final int VHSPROGRAMMEID=110;
        #         public static final int VVVPROGRAMMEID=120;
        #         public static final int VMCPROGRAMMEID=120;
        #         public static final int VIKINGPROGRAMMEID=140;
        #         public static final int VIDEOPROGRAMMEID=150;
        #         public static final int ULTRAVISTAPROGRAMMEID=160;
        #         public static final int SVORIONPROGRAMMEID=100;
        #         public static final int SVNGC253PROGRAMMEID=101;
        #         
        i = 0
        while i < self.SCEHMAVALIDDATABASES.length:
            # 
            #         *   public static final int VHSPROGRAMMEID=110;
            #         public static final int VVVPROGRAMMEID=120;
            #         public static final int VMCPROGRAMMEID=120;
            #         public static final int VIKINGPROGRAMMEID=140;
            #         public static final int VIDEOPROGRAMMEID=150;
            #         public static final int ULTRAVISTAPROGRAMMEID=160;
            #         public static final int SVORIONPROGRAMMEID=100;
            #         public static final int SVNGC253PROGRAMMEID=101;
            #         
            if database.lower() == self.SCEHMAVALIDDATABASES[i].lower():
                validSchema = True
            i += 1
        if table.lower().indexOf("detection") >= 0:
            if validSchema:
                return "objID, multiframeID, filterID, RA, Dec, ell, pa, class, psfMag, hallMag,isoMag, petroMag, aperMag2, aperMag3"
                # 
            else:
                return table + ".*"
        elif table.lower().indexOf("source") >= 0:
            if validSchema:
                sb =  "sourceID, framesetID, RA, Dec, mergedClass, priOrSec"
                if reqFilters != None:
                    sb.append(self.getReqMags(reqFilters))
                return sb.__str__()
            else:
                return table + ".*"
        else:
            return table + ".*"

    
    def getDefaultList_0(self, reqFilters, table):
        """ generated source for method getDefaultList_0 """
        return self.getDefaultList(reqFilters, table, self.LATESTUKIDSSSCHEMA)

    
    def getReqMags(self, reqFilters):
        """ generated source for method getReqMags """
        sb1 = ""

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
                j = 0
                while j < reqFilters[i]:
                 
                    sb1.append(", " + self.getFilterName(i) + "_" + (j + 1) + "AperMag3")
                
                    sb1.append(", " + self.getFilterName(i) + "_" + (j + 1) + "AperMag3Err")
                    j += 1
              
            i += 1
      
        return sb1.__str__()

    
    
    def getBands(self, reqBands):
        """ generated source for method getBands """
        noBands = 0
   
        i = 0
        while i < reqBands.length:
         
            noBands = noBands + reqBands[i]
            i += 1
        bands = [None]*noBands
        bandCount = 0
  
        i = 0
        while i < reqBands.length:
          
            if reqBands[i] == 1:
                bands[bandCount] = self.getFilterName(i)
                bandCount += 1
            if reqBands[i] > 1:
                # 
                #         *   public static final int VHSPROGRAMMEID=110;
                #         public static final int VVVPROGRAMMEID=120;
                #         public static final int VMCPROGRAMMEID=120;
                #         public static final int VIKINGPROGRAMMEID=140;
                #         public static final int VIDEOPROGRAMMEID=150;
                #         public static final int ULTRAVISTAPROGRAMMEID=160;
                #         public static final int SVORIONPROGRAMMEID=100;
                #         public static final int SVNGC253PROGRAMMEID=101;
                #         
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
                    #         *   public static final int VHSPROGRAMMEID=110;
                    #         public static final int VVVPROGRAMMEID=120;
                    #         public static final int VMCPROGRAMMEID=120;
                    #         public static final int VIKINGPROGRAMMEID=140;
                    #         public static final int VIDEOPROGRAMMEID=150;
                    #         public static final int ULTRAVISTAPROGRAMMEID=160;
                    #         public static final int SVORIONPROGRAMMEID=100;
                    #         public static final int SVNGC253PROGRAMMEID=101;
                    #         
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
        #         *   public static final int VHSPROGRAMMEID=110;
        #         public static final int VVVPROGRAMMEID=120;
        #         public static final int VMCPROGRAMMEID=120;
        #         public static final int VIKINGPROGRAMMEID=140;
        #         public static final int VIDEOPROGRAMMEID=150;
        #         public static final int ULTRAVISTAPROGRAMMEID=160;
        #         public static final int SVORIONPROGRAMMEID=100;
        #         public static final int SVNGC253PROGRAMMEID=101;
        #         
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
            #         *   public static final int VHSPROGRAMMEID=110;
            #         public static final int VVVPROGRAMMEID=120;
            #         public static final int VMCPROGRAMMEID=120;
            #         public static final int VIKINGPROGRAMMEID=140;
            #         public static final int VIDEOPROGRAMMEID=150;
            #         public static final int ULTRAVISTAPROGRAMMEID=160;
            #         public static final int SVORIONPROGRAMMEID=100;
            #         public static final int SVNGC253PROGRAMMEID=101;
            #         
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
        #         *   public static final int VHSPROGRAMMEID=110;
        #         public static final int VVVPROGRAMMEID=120;
        #         public static final int VMCPROGRAMMEID=120;
        #         public static final int VIKINGPROGRAMMEID=140;
        #         public static final int VIDEOPROGRAMMEID=150;
        #         public static final int ULTRAVISTAPROGRAMMEID=160;
        #         public static final int SVORIONPROGRAMMEID=100;
        #         public static final int SVNGC253PROGRAMMEID=101;
        #         
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
            #         *   public static final int VHSPROGRAMMEID=110;
            #         public static final int VVVPROGRAMMEID=120;
            #         public static final int VMCPROGRAMMEID=120;
            #         public static final int VIKINGPROGRAMMEID=140;
            #         public static final int VIDEOPROGRAMMEID=150;
            #         public static final int ULTRAVISTAPROGRAMMEID=160;
            #         public static final int SVORIONPROGRAMMEID=100;
            #         public static final int SVNGC253PROGRAMMEID=101;
            #         
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
                #         *   public static final int VHSPROGRAMMEID=110;
                #         public static final int VVVPROGRAMMEID=120;
                #         public static final int VMCPROGRAMMEID=120;
                #         public static final int VIKINGPROGRAMMEID=140;
                #         public static final int VIDEOPROGRAMMEID=150;
                #         public static final int ULTRAVISTAPROGRAMMEID=160;
                #         public static final int SVORIONPROGRAMMEID=100;
                #         public static final int SVNGC253PROGRAMMEID=101;
                #         
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
                    #         *   public static final int VHSPROGRAMMEID=110;
                    #         public static final int VVVPROGRAMMEID=120;
                    #         public static final int VMCPROGRAMMEID=120;
                    #         public static final int VIKINGPROGRAMMEID=140;
                    #         public static final int VIDEOPROGRAMMEID=150;
                    #         public static final int ULTRAVISTAPROGRAMMEID=160;
                    #         public static final int SVORIONPROGRAMMEID=100;
                    #         public static final int SVNGC253PROGRAMMEID=101;
                    #         
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

    
    def getBands_1(self, surveyID):
        """ generated source for method getBands_1 """
        if surveyID==VSASchema.COMMISSIONINGPROGRAMMEID:
            return self.COMMISSIONINGBands
        elif surveyID==VSASchema.UKIDSSLASPROGRAMMEID:
            return self.LASBands
        elif surveyID==VSASchema.UKIDSSLASSVPROGRAMMEID:
            return self.LASBands
        elif surveyID==VSASchema.UKIDSSGPSPROGRAMMEID:
            return self.GPSBands
        elif surveyID==VSASchema.UKIDSSGPSSVPROGRAMMEID:
            return self.GPSBands
        elif surveyID==VSASchema.UKIDSSGCSPROGRAMMEID:
            return self.GCSBands
        elif surveyID==VSASchema.UKIDSSGCSSVPROGRAMMEID:
            return self.GCSBands
        elif surveyID==VSASchema.UKIDSSDXSPROGRAMMEID:
            return self.DXSBands
        elif surveyID==VSASchema.UKIDSSDXSSVPROGRAMMEID:
            return self.DXSBands
        elif surveyID==VSASchema.UKIDSSUDSPROGRAMMEID:
            return self.UDSBands
        elif surveyID==VSASchema.UKIDSSUDSSVPROGRAMMEID:
            return self.UDSBands
        else:
            return None

    
    def getMergeTableName(self, surveyID):
        """ generated source for method getMergeTableName """
        mergeTableName = ""
        if surveyID==VSASchema.COMMISSIONINGPROGRAMMEID:
            mergeTableName = self.COMMISSIONINGMERGETABLENAME
        elif surveyID==VSASchema.VHSPROGRAMMEID:
            mergeTableName = self.VHSMERGETABLENAME
        elif surveyID==VSASchema.VVVPROGRAMMEID:
            mergeTableName = self.VVVMERGETABLENAME
        elif surveyID==VSASchema.VMCPROGRAMMEID:
            mergeTableName = self.VMCMERGETABLENAME
        elif surveyID==VSASchema.VIKINGPROGRAMMEID:
            mergeTableName = self.VIKINGMERGETABLENAME
        elif surveyID==VSASchema.VIDEOPROGRAMMEID:
            mergeTableName = self.VIDEOMERGETABLENAME
        elif surveyID==VSASchema.ULTRAVISTAPROGRAMMEID:
            mergeTableName = self.ULTRAVISTAMERGETABLENAME
        elif surveyID==VSASchema.SVORIONPROGRAMMEID:
            mergeTableName = self.SVORIONMERGETABLENAME
        elif surveyID==VSASchema.SVNGC253PROGRAMMEID:
            mergeTableName = self.SVNGC253MERGETABLENAME
        else:
            mergeTableName = None
        return mergeTableName

    
    def getSourceSchemaVersion(self, surveyID):
        """ generated source for method getSourceSchemaVersion """
        schemaVersion = -1
        if surveyID==VSASchema.PTSPROGRAMMEID:
            schemaVersion = 99999
            #  change when sourec available
        elif surveyID==VSASchema.CALPROGRAMMEID:
            schemaVersion = 99999
            #  change when sourec available
        else:
            #  all the others have been in from the start
            schemaVersion = -1
        return schemaVersion

    
    def getDetectionSchemaVersion(self, surveyID):
        """ generated source for method getDetectionSchemaVersion """
        schemaVersion = -1
        if surveyID==VSASchema.PTSPROGRAMMEID:
            schemaVersion = -1
            #  set high as not to be released
        elif surveyID==VSASchema.CALPROGRAMMEID:
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
            #                 schemaVersion=5; 
            #                 break;
            #             case  WSASchema.UKIDSSUDSPROGRAMMEID:
            #                 schemaVersion=5; 
            #                 break;
            #                 
            #  all the others have been in from the start
            schemaVersion = 99999999
        return schemaVersion

    
    def getSourceTableName(self, surveyID):
        """ generated source for method getSourceTableName """
        if surveyID==VSASchema.COMMISSIONINGPROGRAMMEID:
            return self.COMMSOURCETABLENAME
        elif surveyID==VSASchema.VHSPROGRAMMEID:
            return self.VHSSOURCETABLENAME
        elif surveyID==VSASchema.VVVPROGRAMMEID:
            return self.VVVSOURCETABLENAME
        elif surveyID==VSASchema.VMCPROGRAMMEID:
            return self.VMCSOURCETABLENAME
        elif surveyID==VSASchema.VIKINGPROGRAMMEID:
            return self.VIKINGSOURCETABLENAME
        elif surveyID==VSASchema.VIDEOPROGRAMMEID:
            return self.VIDEOSOURCETABLENAME
        elif surveyID==VSASchema.ULTRAVISTAPROGRAMMEID:
            return self.ULTRAVISTASOURCETABLENAME
        elif surveyID==VSASchema.SVORIONPROGRAMMEID:
            return self.SVORIONSOURCETABLENAME
        elif surveyID==VSASchema.SVNGC253PROGRAMMEID:
            return self.SVNGC253SOURCETABLENAME
        elif surveyID==VSASchema.CALPROGRAMMEID:
            return self.CALSOURCETABLENAME
        else:
            return None

    
    def getDefaultRGB(self, surveyID):
        """ generated source for method getDefaultRGB """
        RGB = None
        if surveyID==VSASchema.VHSPROGRAMMEID:
            RGB = self.VHSRGB
        elif surveyID==VSASchema.VVVPROGRAMMEID:
            RGB = self.VVVRGB
        elif surveyID==VSASchema.UKIDSSLASPROGRAMMEID:
            RGB = self.LASRGB
        elif surveyID==VSASchema.UKIDSSGPSPROGRAMMEID:
            RGB = self.GPSRGB
        elif surveyID==VSASchema.UKIDSSGCSPROGRAMMEID:
            RGB = self.GCSRGB
        elif surveyID==VSASchema.UKIDSSDXSPROGRAMMEID:
            RGB = self.DXSRGB
        elif surveyID==VSASchema.UKIDSSUDSPROGRAMMEID:
            RGB = self.UDSRGB
        else:
            return RGB

    
    def getSourceViewName(self, surveyID):
        """ generated source for method getSourceViewName """
        if surveyID==VSASchema.COMMISSIONINGPROGRAMMEID:
            return self.COMMSOURCEVIEWNAME
        elif surveyID==VSASchema.SVORIONPROGRAMMEID:
            return self.SVORIONSOURCEVIEWNAME
        elif surveyID==VSASchema.SVNGC253PROGRAMMEID:
            return self.SVNGC253SOURCEVIEWNAME
        elif surveyID==VSASchema.VHSPROGRAMMEID:
            return self.VHSSOURCEVIEWNAME
        elif surveyID==VSASchema.VVVPROGRAMMEID:
            return self.VVVSOURCEVIEWNAME
        elif surveyID==VSASchema.VMCPROGRAMMEID:
            return self.VMCSOURCEVIEWNAME
        elif surveyID==VSASchema.VIKINGPROGRAMMEID:
            return self.VIKINGSOURCEVIEWNAME
        elif surveyID==VSASchema.VIDEOPROGRAMMEID:
            return self.VIDEOSOURCEVIEWNAME
        elif surveyID==VSASchema.ULTRAVISTAPROGRAMMEID:
            return self.ULTRAVISTASOURCEVIEWNAME
        elif surveyID==VSASchema.CALPROGRAMMEID:
            return self.CALSOURCEVIEWNAME
        else:
            return None

    
    def getDetectionTableName(self, surveyID):
        """ generated source for method getDetectionTableName """
        if surveyID==VSASchema.COMMISSIONINGPROGRAMMEID:
            return self.COMMDETECTIONTABLENAME
        elif surveyID==VSASchema.VHSPROGRAMMEID:
            return self.VHSDETECTIONTABLENAME
        elif surveyID==VSASchema.VVVPROGRAMMEID:
            return self.VVVDETECTIONTABLENAME
        elif surveyID==VSASchema.VMCPROGRAMMEID:
            return self.VMCDETECTIONTABLENAME
        elif surveyID==VSASchema.VIKINGPROGRAMMEID:
            return self.VIKINGDETECTIONTABLENAME
        elif surveyID==VSASchema.VIDEOPROGRAMMEID:
            return self.VIDEODETECTIONTABLENAME
        elif surveyID==VSASchema.ULTRAVISTAPROGRAMMEID:
            return self.ULTRAVISTADETECTIONTABLENAME
        elif surveyID==VSASchema.SVORIONPROGRAMMEID:
            return self.SVORIONDETECTIONTABLENAME
        elif surveyID==VSASchema.SVNGC253PROGRAMMEID:
            return self.SVNGC253DETECTIONTABLENAME
        elif surveyID==VSASchema.CALPROGRAMMEID:
            return self.CALDETECTIONTABLENAME
        else:
            return None

    
    def getShortSurveyName(self, surveyID):
        """ generated source for method getShortSurveyName """
        shortSurveyName = ""
        if surveyID==VSASchema.COMMISSIONINGPROGRAMMEID:
            shortSurveyName = "comm"
        elif surveyID==VSASchema.VHSPROGRAMMEID:
            shortSurveyName = "VHS"
        elif surveyID==VSASchema.VVVPROGRAMMEID:
            shortSurveyName = "VVV"
        elif surveyID==VSASchema.VMCPROGRAMMEID:
            shortSurveyName = "VMC"
        elif surveyID==VSASchema.VIKINGPROGRAMMEID:
            shortSurveyName = "VIKING"
        elif surveyID==VSASchema.VIDEOPROGRAMMEID:
            shortSurveyName = "VIDEO"
        elif surveyID==VSASchema.ULTRAVISTAPROGRAMMEID:
            shortSurveyName = "UltraVista"
        elif surveyID==VSASchema.SVORIONPROGRAMMEID:
            shortSurveyName = "svOrion"
        elif surveyID==VSASchema.SVNGC253PROGRAMMEID:
            shortSurveyName = "svNGC253"
        elif surveyID==VSASchema.CALPROGRAMMEID:
            shortSurveyName = "cal"
        else:
            shortSurveyName = None
        return shortSurveyName

    
    def getSurveyName(self, surveyID):
        """ generated source for method getSurveyName """
        surveyName = ""
        if surveyID==VSASchema.COMMISSIONINGPROGRAMMEID:
            surveyName = "Commissioning programme"
        elif surveyID==VSASchema.VHSPROGRAMMEID:
            surveyName = "VHS: VISTA Hemisphere Survey"
        elif surveyID==VSASchema.VVVPROGRAMMEID:
            surveyName = "VVV: VISTA Variables in the Via Lactea"
        elif surveyID==VSASchema.VMCPROGRAMMEID:
            surveyName = "VMC: VISTA Magellanic Clouds Survey"
        elif surveyID==VSASchema.VIKINGPROGRAMMEID:
            surveyName = "VIKING: VISTA Kilo-degree Infrared Galaxy Survey"
        elif surveyID==VSASchema.VIDEOPROGRAMMEID:
            surveyName = "VIDEO: VISTA Deep Extragalactic Observations"
        elif surveyID==VSASchema.ULTRAVISTAPROGRAMMEID:
            surveyName = "UltraVISTA: an ultra-deep survey with VISTA"
        elif surveyID==VSASchema.SVORIONPROGRAMMEID:
            surveyName = "ORION Science Verification data"
        elif surveyID==VSASchema.SVNGC253PROGRAMMEID:
            surveyName = "NGC253 Science Verification data"
        elif surveyID==VSASchema.CALPROGRAMMEID:
            surveyName = "Calibration data"
        elif surveyID==VSASchema.UKIDSSGCSPROGRAMMEID:
            surveyName = "GCS TEST"
        else:
            surveyName = "Survey name not found in lookup"
        return surveyName

    def getFilterName(self, filterID):
        """ generated source for method getFilterName """
        if filterID >= 0 and filterID <= self.VISTAFILTERS.length and self.VISTAFILTERS[filterID] != None:
            return self.VISTAFILTERS[filterID]
        else:
            return "NULL"
    

    
    def getRequiredFiltersSQL(self):
        """ generated source for method getRequiredFiltersSQL """
        return "select filterid,npass from requiredfilters"

    
    def getProprietaryCommunity(self, surveyID, remoteUser):
        """ generated source for method getProprietaryCommunity """
        if surveyID==VSASchema.COMMISSIONINGPROGRAMMEID:
            return "COMM"
        elif surveyID==VSASchema.VHSPROGRAMMEID:
            return "VHS"
        elif surveyID==VSASchema.VVVPROGRAMMEID:
            if remoteUser.lower().contains("vvve"):
                return "VVVE"
            elif remoteUser.lower().contains("vvvs"):
                return "VVVS"
            else:
                return "VVV"
        elif surveyID==VSASchema.VMCPROGRAMMEID:
            return "VMC"
        elif surveyID==VSASchema.VIKINGPROGRAMMEID:
            return "VIKING"
        elif surveyID==VSASchema.VIDEOPROGRAMMEID:
            return "VIDEO"
        elif surveyID==VSASchema.ULTRAVISTAPROGRAMMEID:
            return "ULTRAVISTA"
        elif surveyID==VSASchema.SVORIONPROGRAMMEID:
            return "SV_ORION"
        elif surveyID==VSASchema.SVNGC253PROGRAMMEID:
            return "SV_NGC53"
        elif surveyID==VSASchema.CALPROGRAMMEID:
            return "CAL"
        else:
            return None

    
    def getProgIDFromString(self, str_):
        """ generated source for method getProgIDFromString """
        if str_ != None:
            if str_.lower().contains("vvv"):
                return self.VVVPROGRAMMEID
            if str_.lower().contains("vmc"):
                return self.VMCPROGRAMMEID
            if str_.lower().contains("viking"):
                return self.VIKINGPROGRAMMEID
            if str_.lower().contains("vhs"):
                return self.VHSPROGRAMMEID
            if str_.lower().contains("video"):
                return self.VIDEOPROGRAMMEID
            if str_.lower().contains("ultravista"):
                return self.ULTRAVISTAPROGRAMMEID
            return -9999
        else:
            return -9999

    
    def getCrossIDTableTable(self, community, user, baseTable, intProgrammeID):
        """ generated source for method getCrossIDTableTable """
        if community.lower() == "nonSurvey".lower():
            return user.trim() + baseTable
        else:
            if baseTable.lower() == "sourceView".lower():
                return VSASchema.getSourceViewName(intProgrammeID)
            else:
                if baseTable.lower() == "source".lower():
                    return VSASchema.getSourceTableName(intProgrammeID)
                if baseTable.lower() == "detection".lower():
                    return VSASchema.getDetectionTableName(intProgrammeID)
            return ""

    
    def isInAcronyms(self, str_):
        """ generated source for method isInAcronyms """

        i = 0
        while i < len(self.SURVEYACRONYMS):
       
            if str_.lower() == self.SURVEYACRONYMS[i].lower():
                return True
            i += 1
        return False

    
    def isProprietary(self, community):
        """ generated source for method isProprietary """
        if community != None and (self.isInAcronyms(community) or community.lower().find(VSASchema.PROPRIETARYCOMMUNITY.lower())>0):
            return True
        else:
            return False

    
    
    def getFramesetSQL(self, rset, mergelogTable, multiframeTable):
        """ generated source for method getFramesetSQL """
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
                    mfidName = VSASchema.getFilterName(filterid) + "mfid"
                    if not first:
                        sbSQL.append(" or ")
                    first = False
                    sbSQL.append(mergelogTable + "." + mfidName + "=" + multiframeTable + ".multiframeid")
                else:
                    if npass > 1:
                        i = 0
                        while i < npass:
                
                            mfidName = VSASchema.getFilterName(filterid) + "_" + (i + 1) + "mfid"
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

    def getFramesetSQL_0(self, fID, mergelogTable, multiframeTable):
        """ generated source for method getFramesetSQL_0 """
        sbSQL = ""
        mfidName = None
        npass = int()
        filterid = int()
        row = 0
        first = True
     
        j = 0
        while j < fID.length:
   
            filterid = j
            npass = fID[j]
            row = row + npass
            if npass == 1:
                mfidName = VSASchema.getFilterName(filterid) + "mfid"
                if not first:
                    sbSQL.append(" or ")
                first = False
                sbSQL.append(mergelogTable + "." + mfidName + "=" + multiframeTable + ".multiframeid")
            else:
                if npass >= 1:
                    i = 0
                    while i < npass:
                  
                        mfidName = VSASchema.getFilterName(filterid) + "_" + (i + 1) + "mfid"
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

    