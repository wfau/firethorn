"""
config module

Configuration variables for the imageList package

University of Edinburgh, WAFU
@author: Stelios Voutsinas
"""

## ImageList Information
ImageListSelect= "jpegBase,M.multiframeID,frameType,obstype,raBase,decBase,shortname,exptime,filename,catname,dateObs,project,numDetectors,object"
ImageListSelectAll= "substring(compfile,1, len(compfile)-charindex('_',reverse(compfile))) as jpegBase,m.*"
ImageListSelectAllFullList = "jpegBase,m.*"
ImageListFrom="Multiframe as M, ProgrammeFrame as PF, Programme as P"  
ImageListWhere="PF.multiframeID = M.multiframeID and PF.programmeID=P.programmeID"
ImageListOrderBy= "order by dateObs asc"
ImageListDateParameter="dateObs"
dateParameter="dateObs"