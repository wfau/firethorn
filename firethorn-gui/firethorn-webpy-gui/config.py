'''
Created on Sep 17, 2012

@author: stelios
'''

import web
import os

web_services_host = 'localhost'
web_services_port = '8080'

webpy_gui_host = 'localhost'
webpy_gui_port = '8090'

web_services_url = 'http://' +  web_services_host + ':' + web_services_port
webpy_gui_url = 'http://' +  webpy_gui_host + ':' + webpy_gui_port

base_location = os.getcwd()
vospace_dir = base_location + '/static/static_vospace'

local_hostname = {'index' : webpy_gui_url ,'services' : webpy_gui_url + '/services', 'jdbc_resources' : webpy_gui_url + '/jdbc_resources',
                  'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : webpy_gui_url + '/services', 
                  'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : webpy_gui_url +'/jdbc_resources'}

get_jdbc_resources_url = "/firethorn/jdbc/resources/select"

get_param = 'id'

create_menu_items = {'admin' : ['Service','JDBC connection'] , 'user' : ['Service']}

create_params = {'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : 'jdbc.resources.create.name', 
                 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : 'adql.services.create.name'}

create_urls = {'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : web_services_url + '/firethorn/jdbc/resources/create', 
               'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : web_services_url + '/firethorn/adql/services/create'
               }

get_urls = {'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : web_services_url + '/firethorn/adql/service/',
            'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : web_services_url + '/firethorn/jdbc/resource/'
            }

db_select_by_name_urls = {'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : web_services_url + '/firethorn/adql/services/select?',
                       'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : web_services_url + '/firethorn/jdbc/resources/select?'
                       }

db_select_with_text_urls = {'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' :  web_services_url + '/firethorn/adql/services/search?',
                       'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' :  web_services_url + ' /firethorn/jdbc/resources/search?'
                       }
                       
resource_uris = {'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json': '/catalogs/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json': '/schemas/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json': '/tables/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json': '/columns/select'}

type_update_params = {'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json': 'jdbc.resource.update.name',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json': 'jdbc.catalog.update.name',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json': 'jdbc.schema.update.name',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json': 'jdbc.table.update.name',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-column-1.0.json': 'jdbc.column.update.name'
                 }

db_select_with_text_params = {'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json': 'jdbc.resources.search.text',
                    'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json': 'adql.services.search.text'
                    }

db_select_by_name_params = {'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json': 'jdbc.resources.select.name',
                              'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json': 'adql.services.select.name'
                              }

types = {'service' : 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json', 
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
         'adql_column' : 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-column-1.0.json'
         
         
         }

errors = {'INVALID_PARAM' : 'Parameter provided was invalid', 
          'INVALID_NETWORK_REQUEST' : 'Error processing a network request',
          'INVALID_REQUEST' : 'Invalid request to the server',
          'INVALID_TYPE' : 'Invalid type for the requested URL',
          'INVALID_ACTION' : 'Invalid action requested to server',
          'INVALID_CHECKBOX_VALUE' : 'Invalid checkbox value passed to server',
          'UNKNOWN_ERROR' : 'Unknown server error while processing a request',
          }

vospace_root = "/"

available_object_actions = { 'add' : """ <a id="add_to"><img src="static/images/plusButton.png" style="vertical-align:middle"/></a>
                                        <br/>
                                        <div id="add_notification" style="display:none;color:green;margin-left:30px;"></div>
                                        <div id="add_error" style="display:none;color:red;margin-left:30px;"></div>
                                        """,
                             'expand' :  """<a id="expand"><img src="static/images/16arrow-down.png" style="vertical-align:middle"/></a><br/>
                                        <div id="expand_error" style="display:none;color:red;margin-left:30px;"></div>
                                        """,
                             'none' : ''
                             }

types_as_images =  {'resource' : 'static/js/jquery-treeview/themes/default/images/res1.png',
                    'catalog' : 'static/js/jquery-treeview/themes/default/images/catalogue.png',
                    'schema' : 'static/js/jquery-treeview/themes/default/images/schema.png',
                    'table' : 'static/js/jquery-treeview/themes/default/images/table.png',
                    'column' :'static/js/jquery-treeview/themes/default/images/column.png',
                    "" : 'images/res.png'
                };