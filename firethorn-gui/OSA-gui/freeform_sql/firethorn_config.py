'''
Created on May 3, 2013

@author: stelios
'''
import os
import web


### Domain and URL Information
web_services_host = 'localhost'
web_services_port = '8080'

webpy_gui_host = 'localhost'
webpy_gui_port = '8090'

web_services_path = '/firethorn/'
web_services_url = 'http://' +  web_services_host + ':' + web_services_port + web_services_path
webpy_gui_url = 'http://' +  webpy_gui_host + ':' + webpy_gui_port  



### Directory Information (Do not change unless static directory is modified)
base_location = os.getcwd()
vospace_dir = base_location + '/static/static_vospace'
host_temp_directory = webpy_gui_url + 'static/static_vo_tool/temp/'
vospace_root = "/"
base_url = webpy_gui_url 


### General globals
mode_global = 'async'
web.config.smtp_server = 'mail.roe.ac.uk'
from_email = 'osa-support@roe.ac.uk'


source_workspace = "http://localhost:8080/firethorn/adql/resource/3"

### URL, Type and Parameter associations and Information
local_hostname = {
                  'index' : webpy_gui_url ,'services' : webpy_gui_url + '/services', 'jdbc_resources' : webpy_gui_url + '/jdbc_resources',
                  'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json' : webpy_gui_url + '/services',
                  'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : webpy_gui_url + '/services', 
                  'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : webpy_gui_url +'/jdbc_resources',
                  'http://data.metagrid.co.uk/wfau/firethorn/types/adql-catalog-1.0.json' : webpy_gui_url + '/services', 
                  'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json' : webpy_gui_url +'/jdbc_resources',
                  'http://data.metagrid.co.uk/wfau/firethorn/types/adql-schema-1.0.json' : webpy_gui_url + '/services', 
                  'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json' : webpy_gui_url +'/jdbc_resources',
                  'http://data.metagrid.co.uk/wfau/firethorn/types/adql-table-1.0.json' : webpy_gui_url + '/services', 
                  'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json' : webpy_gui_url +'/jdbc_resources',
                  'http://data.metagrid.co.uk/wfau/firethorn/types/adql-column-1.0.json' : webpy_gui_url + '/services', 
                  'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-column-1.0.json' : webpy_gui_url +'/jdbc_resources'
                  }


get_jdbc_resources_url = "/firethorn/jdbc/resource/select"
get_adql_resources_url = "/firethorn/adql/resource/select"

get_param = 'id'

workspace_import_schema_name = "adql.resource.schema.import.name"
workspace_import_schema_base = "adql.resource.schema.import.base"
workspace_import_uri = "/schemas/import"

schema_import_schema_name = "adql.schema.table.import.name"
schema_import_schema_base = "adql.schema.table.import.base"
schema_import_uri = "/tables/import"

query_create_uri = "/queries/create"
query_name_param = "adql.schema.query.create.name"
query_param = "adql.schema.query.create.query"
query_status_update = "adql.query.update.status"

schema_create_uri = '/schemas/create'
table_create_uri = '/tables/create'

table_import_uri = '/tables/import'

workspace_creator = web_services_url + "/adql/resource/create"


create_menu_items = {'admin' : ['TAP Connection','JDBC connection', 'Workspace'] , 'user' : ['TAP Connection', 'Workspace']}


resource_create_name_params = {
                               'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : 'jdbc.resource.create.name', 
                               'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json' : 'adql.resource.create.name',
                               'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : 'adql.resource.create.name',
                               'http://data.metagrid.co.uk/wfau/firethorn/types/adql-schema-1.0.json' : 'adql.resource.schema.create.name'
                               }


resource_create_url_params = {
                              'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : 'jdbc.resource.create.url',
                              'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json' : 'adql.resource.create.url',
                              'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : 'adql.resource.create.url'

                              }


resource_create_username_params = {
                                   'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : 'jdbc.resource.create.user',
                                   'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json' : 'adql.resource.create.user',
                                   'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : 'adql.resource.create.user'
                                   }


resource_create_password_params = {
                                   'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : 'jdbc.resource.create.pass',
                                   'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json' : 'adql.resource.create.pass',
                                   'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : 'adql.resource.create.pass'
                                   }


create_urls = {
               'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : web_services_url + '/firethorn/jdbc/resource/create', 
               'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json' : web_services_url + '/firethorn/adql/resource/create',
               'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : web_services_url + '/firethorn/adql/resource/create'
               }


get_urls = {
            'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : web_services_url + '/firethorn/adql/resource/',
            'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json' : web_services_url + '/firethorn/adql/resource/',
            'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : web_services_url + '/firethorn/jdbc/resource/'
            }


db_select_by_name_urls = {
                          'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : web_services_url + '/firethorn/adql/resource/select?',
                          'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json' : web_services_url + '/firethorn/adql/resource/select?',
                          'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : web_services_url + '/firethorn/jdbc/resource/select?'
                          }


db_select_with_text_urls = {
                            'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' :  web_services_url + '/firethorn/adql/resource/search?',
                            'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json' :  web_services_url + '/firethorn/adql/resource/search?',
                            'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' :  web_services_url + ' /firethorn/jdbc/resource/search?'
                            }

type_select_uris = {'schemas' : '/schemas/select',
                    'tables' : '/tables/select',
                    'columns' : '/columns/select',
                    'workspaces' :'firethorn/adql/resources/select'
                    }                   
                       
resource_uris = {
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json': '/schemas/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json': '/schemas/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json': '/tables/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json': '/columns/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json': '/schemas/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json': '/schemas/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-catalog-1.0.json': '/schemas/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-schema-1.0.json': '/tables/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-table-1.0.json': '/columns/select',
                 }


type_update_params = {
                    'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json': 'jdbc.resource.update.name',
                    'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json': 'jdbc.catalog.update.name',
                    'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json': 'jdbc.schema.update.name',
                    'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json': 'jdbc.table.update.name',
                    'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-column-1.0.json': 'jdbc.column.update.name',
                    'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json': 'adql.resource.update.name',
                    'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json': 'adql.resource.update.name',
                    'http://data.metagrid.co.uk/wfau/firethorn/types/adql-catalog-1.0.json': 'adql.catalog.update.name',
                    'http://data.metagrid.co.uk/wfau/firethorn/types/adql-schema-1.0.json': 'adql.schema.update.name',
                    'http://data.metagrid.co.uk/wfau/firethorn/types/adql-table-1.0.json': 'adql.table.update.name',
                    'http://data.metagrid.co.uk/wfau/firethorn/types/adql-column-1.0.json': 'adql.column.update.name'
                    }


db_select_with_text_params = {
                              'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json': 'jdbc.resource.search.text',
                              'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json': 'adql.resource.search.text',
                              'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json': 'adql.resource.search.text'
                              }


db_select_by_name_params = {
                            'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json': 'jdbc.resource.select.name',
                            'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json': 'adql.resource.select.name',
                            'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json': 'adql.resource.select.name'
                            }
schema_select_by_name_param = "adql.resource.schema.select.name"
table_select_by_name_param = "adql.schema.table.select.name"

types = {
         'service' : 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json', 
         'Service' : 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json',
         'JDBC connection' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json',
         'resource' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json',
         'catalog' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json', 
         'jdbc_catalog' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json', 
         'adql_catalog' : 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-catalog-1.0.json', 
         'jdbc_table' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json',
         'adql_table' : 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-table-1.0.json',
         'jdbc_schema' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json',
         'adql_schema' : 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-schema-1.0.json',
         'jdbc_column' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-column-1.0.json',
         'adql_column' : 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-column-1.0.json',
         'Workspace' : 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json',
         'workspace' : 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json'

        }


available_object_actions = { 
                            'add' : """ <a id="add_to"><img src="static/images/plusButton.png" style="vertical-align:middle"/></a>
                                        <br/>
                                        <div id="add_notification" style="display:none;color:green;margin-left:30px;"></div>
                                        <div id="add_error" style="display:none;color:red;margin-left:30px;"></div>
                                        """,
                             'expand' :  """<a id="expand"><img src="static/images/16arrow-down.png" style="vertical-align:middle"/></a><br/>
                                        <div id="expand_error" style="display:none;color:red;margin-left:30px;"></div>
                                        """,
                             'none' : ''
                             }

types_as_images =  {
                    'resource' : 'static/js/jquery-treeview/themes/default/images/res1.png',
                    'schema' : 'static/js/jquery-treeview/themes/default/images/schema.png',
                    'table' : 'static/js/jquery-treeview/themes/default/images/table.png',
                    'column' :'static/js/jquery-treeview/themes/default/images/column.png',
                    "" : 'images/res.png'
                    }


### Error messages
errors = {
          'INVALID_PARAM' : 'Parameter provided was invalid', 
          'INVALID_NETWORK_REQUEST' : 'Error processing a network request',
          'INVALID_REQUEST' : 'Invalid request to the server',
          'INVALID_TYPE' : 'Invalid type for the requested URL',
          'INVALID_ACTION' : 'Invalid action requested to server',
          'INVALID_CHECKBOX_VALUE' : 'Invalid checkbox value passed to server',
          'UNKNOWN_ERROR' : 'Unknown server error while processing a request',
          }