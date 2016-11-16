--=============================================================================
--
-- $Id: COMMON_CurationLogsSchema.sql 11445 2016-11-15 21:59:41Z NicholasCross $
--
--  Database schema file to create common WFAU curation tables in SQL.
--  NB: Tables are ordered by foreign key dependencies.
--
--  Original author: Nicholas Cross, WFAU, IfA, University of Edinburgh, based
--                   on WSA_CurationLogsSchema.sql
--
--=============================================================================

IF EXISTS (SELECT * FROM sysobjects where name='WFAUReleases') DROP TABLE WFAUReleases
CREATE TABLE WFAUReleases(
-------------------------------------------------------------------------------
--/H Contains the release details for each survey curated by WFAU.
--
--/T This includes tables in SSA, WSA, VSA, OSA, HATLAS, 2MPZ, WISExSCOSPZ,
--/T and external tables
--
--/T Required constraints: primary key is (archive,surveyID,releaseNum)
-------------------------------------------------------------------------------
archive      varchar(16) not null,  --/D the archive name, e.g. OSA, VSA, EXTERNAL --/C meta.code
surveyID     int not null,          --/D the unique identifier for the survey  --/C meta.id
surveyName   varchar(32) not null,   --/D The short name of the survey, eg. 2\
MASS, GLIMPSE etc. --/C meta.code
releaseNum   smallint not null,     --/D the release number   --/C meta.software
releaseDate  datetime not null,     --/D the release date --/U MM-DD-YYYY --/N 12-31-9999   --/C time.epoch
description  varchar(256) not null, --/D a brief description of the release  --/C meta.note
dbName       varchar(128) not null,  --/D the name of the SQL Server database containing this release  --/C ??
securityOpt  varchar(32) not null,   --/D the security option for the survey, e.g. PUBLIC, --/C meta.code
serverList   varchar(64) not null,   --/D the list of servers with copies of the database, e.g. ramses3,ramses4 --/C meta.code
CONSTRAINT pk_Rel_Date PRIMARY KEY (archive,surveyID,releaseNum)
)
GO
