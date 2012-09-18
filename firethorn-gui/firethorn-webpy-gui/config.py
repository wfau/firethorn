'''
Created on Sep 17, 2012

@author: stelios
'''


hostname = 'localhost:8080'

service_select_by_name_url = 'http://' + hostname + '/firethorn/adql/services/select?'
service_select_by_name_param = 'adql.services.select.name'

service_select_with_text_url = 'http://' + hostname +' /firethorn/adql/services/search?'
service_select_with_text_param = 'adql.services.search.text'

service_create_url = 'http://'+ hostname + '/firethorn/adql/services/create'
service_create_param = 'adql.services.create.name'

service_get_url = 'http://'+ hostname + '/firethorn/adql/service/'
service_get_param = ''


error_dict = {'INVALID_PARAM' : 'Parameter provided was invalid', 'INVALID_NETWORK_REQUEST' : 'Error processing a network request','INVALID_REQUEST' : 'Invalid request to the server'}
