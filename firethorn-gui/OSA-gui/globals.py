'''
Created on Dec 19, 2012

@author: stelios
'''
from config import log_directory, base_location, dbqueries
from helper_functions.dbHelper import dbHelper
import logging
import config
import traceback

LOG_FILENAME =  base_location + '/log/error.log'
logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG,format='%(asctime)s %(name)-12s %(levelname)-8s %(message)s', datefmt='%m-%d %H:%M',)


class MyOutputStream(object):

    def write(self, data):
        f = open(log_directory + 'error.log', 'a+')
        f.write(data)
        f.close()

    def log_queries(self, timenow, timetaken, qry, rows , cols, user, dbrelease , userip):
        
        f = None
        data = str(timenow)  + "\n"
        data += "Time taken: " +  str(timetaken) + "\n"
        data += "Query run: " + qry + "\n"
        data += "Rows: " + str(rows) + "\n"  
        data += "Columns: " + str(cols) + "\n"  
        data += "User: " + str(user) + "\n" 
        data += "Database Release: " +  str(dbrelease) + "\n"
        data += "User IP:" +  str(userip)  +  "\n"  
   
        f = open(log_directory + 'query.log', 'ab+')
        f.write(data + " \n\n")
       
   
        query = """
        INSERT INTO """ + config.dbtablequeries + """ 
        ([timestamp]
        ,[timeTaken]
        ,[rowsReturned]
        ,[colsReturned]
        ,[user]
        ,[DBRelease]
        ,[ipAddress]
        ,[query])
        VALUES ('""" + timenow + "'," +  str(timetaken) + "," + str(rows) + "," + str(cols) + ",'" + user + "','" + str(dbrelease) + "','" +  str(userip) + "','" + qry + "' )"
   
        f.write(query)
        mydb = dbHelper(config.dbqueries,config.userdb_user,config.userdb_pw)
        mydb.execute_insert(query.encode('utf-8'), config.dbnamequeries)
      
       
            
        if f!=None:
            f.close()
            
            