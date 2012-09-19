'''
Created on Sep 17, 2012

@author: stelios
'''

import web

web_services_hostname = 'localhost:8080'
local_hostname = {'index' : 'localhost:8090','services' : 'http://localhost:8090/services'}


service_select_by_name_url = 'http://' + web_services_hostname + '/firethorn/adql/services/select?'
service_select_by_name_param = 'adql.services.select.name'

service_select_with_text_url = 'http://' + web_services_hostname +' /firethorn/adql/services/search?'
service_select_with_text_param = 'adql.services.search.text'

service_create_url = 'http://'+ web_services_hostname + '/firethorn/adql/services/create'
service_create_param = 'adql.services.create.name'

service_get_url = 'http://'+ web_services_hostname + '/firethorn/adql/service/'
service_get_param = 'id'

### Render templates
render = web.template.render('templates/')



types = {'service' : 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json'}

errors = {'INVALID_PARAM' : 'Parameter provided was invalid', 'INVALID_NETWORK_REQUEST' : 'Error processing a network request','INVALID_REQUEST' : 'Invalid request to the server',
              'INVALID_TYPE' : 'Invalid type for the requested URL'}
