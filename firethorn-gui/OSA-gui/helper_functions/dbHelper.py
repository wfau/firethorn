"""
dbHelper module

University of Edinburgh, WAFU
@author: Stelios Voutsinas
"""


import web
#import mx.ODBC.unixODBC
import pyodbc

class dbHelper:
    """
    Class to provide necessary database functionality
    """
    
    def __init__(self, db_server, username, password):
        """
        Initialise dbHelper class instance
        """
        self.db_server = db_server
        self.username = username
        self.password = password
        
        
        
    def execute_qry_single_row (self, query, db_name):
        """
        Execute a query on a database & table, that will return a single row
        """
        return_val = []
        params = 'DRIVER={TDS};SERVER=' + self.db_server + ';Database=' + db_name +';UID=' + self.username + ';PWD=' + self.password +';TDS_Version=8.0;Port=1433;'
        cnxn = pyodbc.connect(params)  
        cursor = cnxn.cursor()
        cursor.execute(query)
        row = cursor.fetchone()
        
        if row:
            return_val = row

        cnxn.close()
        
        return return_val
    
    
    def execute_query_multiple_rows(self, query, db_name):
        """
        Execute a query on a database & table that may return any number of rows
        """
        return_val = []
        params = 'DRIVER={TDS};SERVER=' + self.db_server + ';Database=' + db_name +';UID=' + self.username + ';PWD=' + self.password +';TDS_Version=8.0;Port=1433;'
        cnxn = pyodbc.connect(params)  
        cursor = cnxn.cursor()
        cursor.execute(query)
        rows = cursor.fetchall()
        
        for row in rows:
            return_val.append(row)
        
        cnxn.close()
        
        return return_val
    
    def execute_insert (self, insert_query, db_name):           
        """
        Execute an insert on a database & table 
        """
        return_val = []
        params = 'DRIVER={TDS};SERVER=' + self.db_server + ';Database=' + db_name +';UID=' + self.username + ';PWD=' + self.password +';TDS_Version=8.0;Port=1433;'
        cnxn = pyodbc.connect(params)  
        cursor = cnxn.cursor()
        cursor.execute(insert_query)
        cnxn.commit()
        cnxn.close()
        
        
        