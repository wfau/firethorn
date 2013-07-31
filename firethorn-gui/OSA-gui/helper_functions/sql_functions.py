"""
sql_functions module

University of Edinburgh, WAFU
@author: Stelios Voutsinas
"""

import web
from dbHelper import dbHelper
from config import *


def verify_user (username, password, community, community_prefix = "") :
    """
    Execute a query that checks whether a row exists in the username tables with the given username, password and community
    """
    if community_prefix!="": community = community_prefix + "::" + community  
    query = "select username, community, email, programmeID from " + table_users + " where username='" + username + "' and password='" + password + "' and community like '" + community + "'"
    mydb = dbHelper(dbserver,userdb_user,userdb_pw)
    user_row = mydb.execute_qry_single_row(query.encode('utf-8'), database_users)
    return user_row 


def execute_query (qry):
    """
    Execute a query (qry) against a db and table, the information of which is stored as global variables
    """
    mydb = dbHelper(dbserver,dbuser ,dbpasswd)
    table_data = mydb.execute_query_multiple_rows(qry.encode('utf-8'), SURVEY_DB)
    return table_data