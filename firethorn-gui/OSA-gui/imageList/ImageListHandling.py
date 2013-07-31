"""
ImageListHandling module

Module containing the main function responsible for taking data set with parameters from the ImageList form
and generating an SQL query for the OSA Atlas survey data according to those parameters

University of Edinburgh, WAFU
@author: Stelios Voutsinas
"""

import imageList
import datetime
import calendar
import re
from helper_functions import sql_functions
from app import session



def generate_SQL_from_input(data):
    '''
    Generate according SQL query string from input for Image Listing functionality
    '''
    return_val = "<h3>Query Results</h3>"
   

    programmeID = None
    database = data.database
    community = data.community
    archive = data.archive
    userSelect = data.userSelect
    filterID = data.filterID
    suppMinRA = data.minRA
    suppMaxRA = data.maxRA
    suppMinDec = data.minDec
    suppMaxDec = data.maxDec
    startYear = data.startYear
    startMonth = data.startMonth
    startDay = data.startDay
    endYear = data.endYear
    endMonth = data.endMonth
    endDay = data.endDay
    obsType = data.obsType
    frameType = data.frameType
    pCount = data.pCount
    suppliedMFID = data.mfid
    suppliedLMFID = data.lmfid
    framesetID = data.fsid
    publicOption = data.po
    rows = data.rows
    deprecated = "0"
    dateParameter = "m.dateObs"
    vdfssInstance = imageList.VDFSSchema()
    loginboolean = True # Get login info from session
    archiveID=imageList.VDFSSchema.getArchiveID(vdfssInstance,archive)
    possProj={"uukidss","userv","ucmp","u05a","u05b","u06a","u06b","u07a","u07b","u08a","u08b","u09a","u09b","u10a","u10b","u11a","u11b"};
    userSess="atlas"
    genUser="imageList.VDFSSession.VDFSSession.getUser(session,archiveID,True);"
    select =  imageList.config.ImageListSelect
    from_clause = imageList.config.ImageListFrom
    where_clause = imageList.config.ImageListWhere
    order = imageList.config.ImageListOrderBy
    output = ''
    error = ""
    
    
    if (archive=="osa" or archive=="OSA"):
        select+=", vstRunNo"
        order+=",vstRunNo asc"
        
    if (userSelect != None and userSelect != "" ):
        if (userSelect=="all"):
            select=imageList.config.ImageListSelectAllFullList
   
    if (community == "nonSurvey".lower() or imageList.VDFSSchema.isProprietary(vdfssInstance,community)) and "imageList.VDFSSession.getLoginBoolean(session, archive)":
        programmeID = session.get('programmeID', "170")
        select+=",object"
    else:
        programmeID = data.programmeID
  
  
    if programmeID != None:
        if programmeID.lower() == "openTime".lower():
            openTime = True
            programmeID = data.openTimeID
    if data.showCal != None and data.showCal.lower().startswith("n"):
        showCal = False
        
    showConf = False    
    if data.showConf != None and data.showConf.lower() == "y".lower():
        showConf = True


    if data.dep != None:
        deprecated = data.dep
        if not deprecated == "0":
            select= "m.deprecated," + select
    if deprecated == "0":
        where_clause+=" and (M.deprecated=0 or M.deprecated=111) "
   
    formatRA = "hours"
    if data.formatRA != None:
        formatRA = data.formatRA
    formatDec = "degrees"
    if data.formatDec != None:
        formatDec = data.formatDec
   
    if database.lower() == "none".lower():
        error = "<b>Error: No valid database.</b><br>"
    
 
    if programmeID != None:
        try:
            iPID = int(programmeID)
            if iPID < 1000:
                output+="<b>Survey: </b>" + imageList.VDFSSchema.getSurveyName(vdfssInstance,iPID, archiveID) + "<br>"
            if iPID == 1000:
                output+="<!-- opentime+uds --><b>Opentime: </b><br>"
            if not loginboolean and database.lower() == "ukidssdr5plus".lower() and (iPID == 102 or iPID == 105):
                output+="<b>Warning: GPS and UDS not yet publically available in UKIDSSDR5PLUS, please use UKIDSSDR4PLUS.</b><br>"
            if (iPID > 1000 and (not community.lower() == "nonsurvey".lower() and not community.lower() == imageList.VSASchema.VSASchema.PROPRIETARYCOMMUNITY.lower())) or (iPID < 1000 and community.lower() == "nonsurvey".lower()):
                error = "<b>Error: Invalid programmeID.</b><br>"
            if archiveID == imageList.VDFSSchema.VSAARCHIVEID:
                if iPID < 200:
                    where_clause+=" and (PF.programmeID=" + programmeID
                    if showCal:
                        where_clause+=" or PF.programmeID=" + imageList.VSASchema.VSASchema.CALPROGRAMMEID
                    where_clause+=")"
                else:
                    where_clause+=" and (PF.programmeID=" + programmeID + ")"
            elif archiveID == imageList.VDFSSchema.OSAARCHIVEID:
                if iPID < 200:
                    where_clause+=" and (PF.programmeID=" + programmeID
                    if showCal:
                        where_clause+=" or PF.programmeID=" + imageList.VSASchema.VSASchema.CALPROGRAMMEID
                    where_clause+=")"
                else:
                    where_clause+=" and (PF.programmeID=" + programmeID + ")"
            else:
                if iPID != imageList.WSASchema.WSASchema.CALPROGRAMMEID:
                    if community.lower() == "nonsurvey".lower() and iPID == 99999999:
                        i = 0
                        while i < len(possProj):
                            # out.print (userSess);
                            if userSess.lower().indexOf(possProj[i]) > -1:
                                proj = "u/" + possProj[i].substring(1) + "/" + len(userSess.substring(possProj[i]))
                                output+=" <b>" + proj + "</b><br>"
                            i += 1
                        where_clause+=" and m.project like '" + proj + "' "
                    else:
                        if iPID != 1000:
                            where_clause+=" and (PF.programmeID=" + programmeID + ")"
                        else:
                            where_clause+=" and (PF.programmeID>" + programmeID + " or PF.programmeID=" + imageList.WSASchema.WSASchema.UKIDSSUDSPROGRAMMEID + ")"
                else:
                    where_clause+=" and (PF.programmeID=" + imageList.WSASchema.WSASchema.CALPROGRAMMEID + " or PF.programmeID=" + imageList.WSASchema.WSASchema.OTHERCALPROGRAMMEID + ")"
        except Exception as e:           
            if programmeID.lower() == "all".lower():
                output+="<b>Survey/programme:</b> " + imageList.VDFSSchema.getAllSurveys(archiveID) + "<br>"
                if archiveID == imageList.VDFSSchema.VSAARCHIVEID:
                    where_clause+=" and (PF.programmeID < 1000 and PF.programmeID > 0) "
                else:
                    if not loginboolean and database.lower() == "ukidssdr5plus".lower():
                        where_clause+=" and (PF.programmeID < 1000 and PF.programmeID > 10 and PF.programmeID != 102 and  PF.programmeID != 105) "
                        output+="(LAS, GCS and DXS only)<br>"
                    else:
                        where_clause+=" and (PF.programmeID < 1000 and PF.programmeID > 10) "
            else:
                error = "<b>Error: Invalid programmeID.</b><br>"
    else:
        error = "<b>Error: Invalid programmeID.</b><br>"

    
    
    if suppliedMFID != None and not suppliedMFID == "":
        try:
            output+="<b>MultiframeID: </b>" + suppliedMFID + "<br>"
            where_clause+=" and (M.multiframeID=" + suppliedMFID + ")"
        except Exception:
            error = "<b>Error: The supplied multiframeID</b> " + suppliedMFID + " <b>is not a valid number.</b><br>"
    inClause = ""
    if suppliedLMFID != None and not suppliedLMFID == "" and not suppliedLMFID == " ":
        #  replace line feeds and commas
        #  replace line feeds and commas
        listmfid=suppliedLMFID.replace("\n"," ").replace(',',' ')
        mfidArray=listmfid.strip().split()
        
        first=True
        numValid=0
        n=0
      
        while n < len(mfidArray):
            #  replace line feeds and commas
            try:
                float(mfidArray[n])
                numValid += 1
                if not first:
                    inClause+="," + mfidArray[n]
                else:
                    inClause+=mfidArray[n]
                    first = False
            except Exception as nfe:
                output+="<br><b>Invalid MFID</b> " + mfidArray[n]
            n += 1
        output+="<br><b>Number of valid multiframeIDs</b> " + str(numValid) + "<br>"
        if numValid > 0 and numValid <= 100:
            where_clause+=" and (m.multiframeid in (" + inClause + "))"
        else:
            error = "<br><b>Please only supply up to 100 multiframeIDs</b><br>"
    
    
    mergeLog = None
    lPID = 0
    if framesetID != None and not framesetID == "":
        try:
            lFSID=long(framesetID.strip())
            maska=0xffff00000000L
            maskb=0x100000000L
            lPID=(lFSID & maska)/maskb
            
            if community != None:
                if community.lower() == "nonsurvey".lower():
                    mergeLog = userSess + "mergelog"
                else:
                    mergeLog = imageList.VDFSSchema.getMergeTableName(vdfssInstance, (lPID), archive)
                    if mergeLog == None:
                        mergeLog = genUser + "mergelog"
            if mergeLog == None:
                error = "<b>Error: Unable to resolve mergelog name from framesetID.</b><br>"
        except Exception as nfe:
            print nfe
            error = "<b>Error: The supplied framesetID</b> " + framesetID + " <b>is not a valid number.</b><br>"
    
    pCountInt = 0
    if pCount != None:
        try:
            pCountInt = int(pCount)
        except Exception:
            pCountInt = 0
    
    if publicOption != None and publicOption == "y":
        where_clause+=" and ( filename not like '%none%') "

    if obsType != None:
        if obsType.lower() == "object".lower():
            where_clause+=" and ( " + "m.obstype NOT LIKE 'BIAS%' AND m.frametype NOT LIKE 'BIAS%' AND " + "m.obstype NOT LIKE 'DARK%' AND m.frametype NOT LIKE 'DARK%' AND " + "m.obstype NOT LIKE 'SKY%' AND m.frametype NOT LIKE 'SKY%' AND " + "m.obstype NOT LIKE 'FOCUS%' AND m.frametype NOT LIKE 'FOCUS%' AND " + "m.obstype NOT LIKE 'CONFIDENCE%' AND m.frametype NOT LIKE 'CONFIDENCE%' AND " + "m.obstype NOT LIKE '%FLAT%' AND m.frametype NOT LIKE '%FLAT%'" + ") "
        elif (obsType.lower() == "bias"):
            where_clause+=" and ( m.obstype like 'BIAS%' or m.frametype like 'BIAS%') "             
        elif (obsType.lower == "dark"):
            where_clause+=" and ( m.obstype like 'DARK%' or m.frametype like 'DARK%') "             
        elif (obsType.lower == "flat"):
            where_clause+=" and ( m.obstype like '%FLAT%' or m.frametype like '%FLAT%') "              
        elif (obsType.lower() == "sky"):
            where_clause+=" and ( m.obstype like 'SKY%' or m.frametype like 'SKY%') "             
        elif (obsType.lower() == "focus"):
            where_clause+=" and ( m.obstype like 'FOCUS%' or m.frametype like 'FOCUS%') "             
        elif (obsType.lower() == "confidence"):
            where_clause+=" and ( m.obstype like 'CONFIDENCE%' or m.frametype like 'CONFIDENCE%') "              
        
    
    if frameType != None and not frameType.lower() == "all".lower():
        where_clause+=" and ( " + "m.frametype LIKE '%" + frameType + "%') "
    if filterID != None and not filterID == "all":
        output+="<b>Waveband: </b>" + imageList.VDFSSchema.getFilterName(vdfssInstance, int(filterID), archiveID) + "<br>"
        where_clause+=" and (M.filterID=" + filterID + ")"
    
    minRAValue = 0.0
    maxRAValue = 24.0
    minDecValue = -90.0
    maxDecValue = 90.0
    
    if suppMinRA == None or suppMinRA == "":
        suppMinRA = "0.0"
    if suppMaxRA == None or suppMaxRA == "":
        suppMaxRA = "24.0"
    if suppMinDec == None or suppMinDec == "":
        suppMinDec = "-90.0"
    if suppMaxDec == None or suppMaxDec == "":
        suppMaxDec = "90.0"
    
    if formatRA.lower() == "degrees".lower():
        try:
            minRAValue = float(suppMinRA) / 15.0
            maxRAValue = float(suppMaxRA) / 15.0
        except Exception:
            error = "<b>Error: check supplied minimum/maximum RA</b><br>"
    elif formatRA.lower() == "sexagesimal".lower():
        try:
            minRAValue = imageList.StringToRADec.StringToRADec.coordStringToDouble(suppMinRA, "RA", False) / 15.0
            maxRAValue = imageList.StringToRADec.StringToRADec.coordStringToDouble(suppMaxRA, "RA", False) / 15.0
        except Exception as e:
            print e
            error = "<b>Error: check supplied minimum/maximum RA, unable to parse sexagesimal string</b><br>"
            
    else:
        try:
            minRAValue = float(suppMinRA)
            maxRAValue = float(suppMaxRA)
        except Exception:
            error = "<b>Error: check supplied minimum/maximum RA</b><br>"
         
    minRA = str(minRAValue)
    maxRA = str(maxRAValue)
    
    if formatDec.lower() == "sexagesimal":
        try:
            minDecValue = imageList.StringToRADec.StringToRADec.coordStringToDouble(suppMinDec, "Dec", False)
            maxDecValue = imageList.StringToRADec.StringToRADec.coordStringToDouble(suppMaxDec, "Dec", False)
        except Exception:
            error = "<b>Error: check supplied minimum/maximum Dec, unable to parse sexagesimal string</b><br>"
    else:
        try:
            minDecValue = float(suppMinDec)
        except Exception:
            error = "<b>Error: check supplied minimum Dec</b><br>"
        try:
            maxDecValue = float(suppMaxDec)
        except Exception:
            error = "<b>Error: check supplied minimum/maximum Dec</b><br>"
            
    minDec = str(minDecValue)
    maxDec = str(maxDecValue)
   
    if minRA != None or maxRA != None:
        try:
            minra = float(minRA)
        except Exception:
            #  user can't enter a number
            minRA = None
        try:
            maxra = float(maxRA)
        except Exception:
            #  user can't enter a number
            maxRA = None
        if (minRA == None and maxRA == None):
            error = "<b> min and max Dec values == None"
        elif minRA == None and maxRA != None:
            where_clause+=" and (m.rabase <= " + maxRA + ")"
        elif maxRA == None and minRA != None:
            where_clause+=" and (m.rabase >= " + minRA + ")"
        else:
            if minra > maxra:
                where_clause+=" and ((m.rabase >= " + str(minra) + " and m.rabase <=24.0) or " + "(m.rabase <= " + str(maxra) + " ))"
            else:
                where_clause+=" and (m.rabase >= " + str(minra) + " and m.rabase <= " + str(maxra) + ") "
        if minra < 0.0 or maxra > 24.0 or minra > 24.0 or maxra < 0:
            error = "<b>Warning: The supplied RAs should be between 0.0 and 24.0 hours</b><br>"
           
    if minDec != None or maxDec != None:
        try:
            mindec = float(minDec)
        except Exception:
            #  user can't enter a number
            minDec = None
        try:
            maxdec = float(maxDec)
        except Exception:
            #  user can't enter a number
            maxDec = None
        if (minDec == None and maxDec == None):
            error = "<b> min and max Dec values == None"
        elif minDec == None and maxDec != None:
            where_clause+=" and (m.decbase <= " + maxDec + ")"
        elif maxDec == None and minDec != None:
            where_clause+=" and (m.decbase >= " + minDec + ")"
        else:
            where_clause+=" and (m.decbase >= " + minDec + " and m.decbase <= " + maxDec + " ) "
        if mindec < -90.0 or maxdec > 90.0:
            error = "<b>Warning: The supplied DECs should be between -90.0 and +90.0 degrees</b><br>"
        if mindec > maxdec:
            error = "<b>Warning: min Dec &gt; max Dec.</b><br>"
    
   
    
    maxDay = 31
    year = 0
    month = 0
    day = 0
    startTime = 0
    endTime = 0

    if startYear != None and startMonth != None and startDay != None:
        year = int(startYear)
        month = int(startMonth)
        day = int(startDay)
        if day == 0:
            day = 1
        if month == 0:
            month = 1
        if year != 0 and month != 0 and day != 0:
            cal = datetime.date(year, month, day)
            wk,maxDay = calendar.monthrange(year,month)
            if day > maxDay:
                cal = datetime.date(year, month, maxDay)
            else:
                cal = datetime.date(year, month, day)
            
            startTime = cal
            where_clause+=" and (" + dateParameter + " >= '" + str(cal) + "') "
            output+="<b>Start date: </b>" + str(cal) + "<br>"
            

    if endYear != None and endMonth != None and endDay != None:
        year = int(endYear)
        month = int(endMonth)
        day = int(endDay)
        if day == 0:
            day = 1
        if month == 0:
            month = 1
        if year != 0 and month != 0 and day != 0:
            cal = datetime.date(year, month, day)
            wk,maxDay = calendar.monthrange(year,month)
            if day > maxDay:
                cal = datetime.date(year, month, maxDay)
            else:
                cal = datetime.date(year, month, day)

            endTime = cal
            where_clause+=" and (" + dateParameter + " <= ' 23:59:59.9 " + str(cal) + "') "
            output+="<b>End date: </b>" + str(cal) + "<br>"
    
    if startTime > endTime and endTime != 0:
        error = "<b>Warning: start date is after end date.</b>"
    
    if mergeLog != None:
        rs =  [(2, 1), (3, 2), (4, 1), (5, 1)] #execute_query(imageList.SQLMethods.SQLMethods.getRequiredFiltersSQL() + " where programmeID= " + str(lPID))
        framesetSQL=imageList.SQLMethods.SQLMethods.getFramesetSQL(rs,mergeLog,"m",archiveID);
        if framesetSQL == None:
            error = "<br><b>Unable to construct frameset SQL.</b><br>"
        from_clause+="," + mergeLog
        where_clause+=" and framesetid=" + str(framesetID) + " and (" + str(framesetSQL) + ")"
       
    if showConf:
        select+=",cmf.filename as confFileName,m.confID "
    if (database.lower() == imageList.WSASchema.WSASchema.rollingDB.lower() or community.lower() == "prerelease".lower()) and not community.lower() == "nonsurvey".lower():
        select+=",cast (2.0*degrees(asin(sqrt(square(sin(radians(0.5*(m.decbase-m.decmoon)))) + cos(radians(m.decbase))*cos(radians (m.decmoon))* square(sin(radians(7.5*(m.rabase-m.ramoon))))))) as int) as moondist"
    
    if rows=="":
        top = "800"
    else :
        top = rows
        
    sql = ""
    
    #sql.append("select distinct top " + top + " " + select + " from " + from_clause + " where " + where_clause + " " + order)
    newSql = ""
    newSql+="select " + select + " from (select distinct substring(compfile,1, len(compfile)-charindex('_',reverse(compfile))) as jpegBase, t.multiframeid from " + "(select top " + top + " m.multiframeid from " + from_clause + " where " + where_clause + " " + order + ") as t,multiframedetector as mfd where mfd.multiframeid=t.multiframeid) as t1, multiframe as m, filter as F "
    if archive.lower() == imageList.VDFSSchema.VSAARCHIVE.lower():
        newSql+=",MultiframeEsoKeys as me "
    if showConf:
        newSql+=",multiframe as cmf "
    # 
    #                  *         from+=(",MultiframeEsoKeys as me");
    #             where+=(" and me.multiframeid=m.multiframeid");
    #                  +=  newSql+=("where t1.multiframeid=m.multiframeid and F.filterid=M.filterid ")
    newSql+="where t1.multiframeid=m.multiframeid and F.filterid=M.filterid "

    if archive.lower() == imageList.VDFSSchema.VSAARCHIVE.lower():
        newSql+=" and me.multiframeid=m.multiframeid "
    if showConf:
        newSql+=" and cmf.multiframeid=m.confid "
    newSql+=(order)
    
    print newSql
    
    return (newSql, error, select)