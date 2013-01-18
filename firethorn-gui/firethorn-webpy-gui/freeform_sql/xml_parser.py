"""
xml_parser module

Documentation for the xml_parser
Used to parse an XML file, in the format of TAP table metadata XML files

Created on May 17, 2011
@author: Stelios Voutsinas
"""

import xml.dom.minidom
import xml.dom
import html_functions
from types import NoneType

def getText(nodelist):
    """
    Get the text content of an xml Node

    @param nodelist: List of nodes
    @return: The text as a string
    """
    rc = []
    if isinstance(nodelist,xml.dom.Node):
        if isinstance(nodelist.firstChild,NoneType) or isinstance(nodelist,NoneType):
            rc.append('None')
        else:    
            rc.append(nodelist.firstChild.data)
    else:    
        for node in nodelist:
            if node.firstChild == None:
                rc.append('None')
            elif node.firstChild.nodeType == node.TEXT_NODE:
                rc.append(node.firstChild.data)
    return ''.join(rc)

def handleXML(xml):
    """
    Handle an XML document. Specific to certain XML formatted file returned with Table metadata for a TAP endpoint
  
    @param xml: XML content
    @return: Return the result
    """

    res = ""
    tables = xml.getElementsByTagName("table")
    res += handleTables(tables)
    return res



def handleTables(tables):
    """ 
    Handle each table element from the input list
  
    @param tables: Handle a list of table elements
    @return: HTML content for tables
    """
    
    res = ""    
    for each_tbl in tables:
        res += '<div class="accordion">' + handleTable(each_tbl) + '</div>'
        res += "<br>"
    return res



def handleTable(table):
    """
    Return HTML content for a table node. Handle its name, description and column elements        
 
    @param table: A table DOM element
    @return: HTML content for element
    """
    
    res = ""
    res += handleTableName(table.getElementsByTagName("name")[0])
    res += html_functions.remove_bold(handleTableDescription(table.getElementsByTagName("description")[0]))
    res += handleTableColumns(table.getElementsByTagName("column"))
    return res
    
    
def handleTableName(name):
    """ 
    Handle a table element's name node   
    
    @param name: A name node
    @return: HTML content with the text of the node
    """
    
    return '<p class="heading">%s <img style="float:right;margin-top:5px" src="/static/static_vo_tool/asc.gif"/> </p>' % getText(name)


def handleTableDescription(desc):
    """ 
    Handle a table element's description node   
    
    @param name: A description node
    @return: HTML content with the text of the node
    """

    return '<div id="desc">Description: %s </div>' % getText(desc)



def handleTableColumns(columns):
    """
    Return HTML content containing the information for all column nodes taken as input   
    
    @param name: A list of columns
    @return: HTML content as a string
    """
    
    res='<div class="meta_content">'
    res += '<p style="background-color:#EDE8E8;color:#3E3E3E;padding-left:10px;padding-top:2px;margin-bottom:10px;border:1px solid gray;height:20px;">Table Columns:<br><br></p>'
    for column in columns:
        res += '<p class="column_heading">' + handleColumnName(column.getElementsByTagName("name")) + '</p>'
        res += '<ul class="column_content">'
        res += handleColumn(column)
        res += "</ul>"
   
    res += "</div>"    
    return res    


def handleColumn(column):
    """
    Handle a column node. Call appropriate function to handle its description, unit and ucd elements   

    @param table: A column DOM element
    @return: HTML content for element
    """
    
    res = ""
    res += handleColumnDescription(column.getElementsByTagName("description"))
    res += handleColumnUnit(column.getElementsByTagName("unit"))
    res += handleColumnUCD(column.getElementsByTagName("ucd"))
    res += handleColumnType(column.getElementsByTagName("dataType"))

    return res


def handleColumnName(name):
    """
    Get the text for the name of a column       

    @param name: A column node
    @return: HTML content with the text of the node
    """
    return '<a class="expand">%s</a> <img style="float:right;margin-top:5px" src="/static/static_vo_tool/asc.gif"/>' % getText(name)


def handleColumnDescription(desc):
    """
    Get the text of a description element and return it as HTML content   
    
    @param name: A description node
    @return: HTML content with the text of the node
    """
    
    return "<li>Description: %s</li>" % getText(desc)


def handleColumnUnit(unit):
    """ 
    Get the text of a unit element and return it as HTML content   

    @param name: A unit node
    @return: HTML content with the text of the node
    """

    return "<li>Unit: %s</li>" % getText(unit)


def handleColumnUCD(ucd):       
    """ 
    Get the text of a ucd element and return it as HTML content   

    @param name: A ucd node
    @return: HTML content with the text of the node
    """
    return "<li>UCD: %s</li>" % getText(ucd)

def handleColumnType(type):       
    """ 
    Get the text of a type element and return it as HTML content   

    @param name: A type node
    @return: HTML content with the text of the node
    """
    return "<li>Type: %s</li>" % getText(type)

