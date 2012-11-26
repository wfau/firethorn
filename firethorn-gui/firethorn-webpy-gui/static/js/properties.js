/* Global variables File */
var properties = new function() {
	
	
	this.web_services_host = 'localhost';
	this.web_services_port = '8080';

	this.webpy_gui_host = 'localhost';
	this.webpy_gui_port = '8090';

	this.path = '';
	// The base url for this app
	this.base_url = 'http://' +  this.webpy_gui_host + ':' + this.webpy_gui_port;
	
	//The URL for the java web services used by the gui
	this.web_services_url = 'http://' +  this.web_services_host + ':' + this.web_services_port;
	
	// The default [base] Location
	this.vospace_dir = '/';
	this.vospace_fileConnector = 'vospace';
	
	// Root type
	this.root_type = 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json';
	
	// Schems type
	this.schema_type = 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json';

	// Type of object that has no children
	this.table_type = 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json';

	// Types that may have children
	this.types_as_folders = ['http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json',
	                         'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json',
	                         'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json',
	                         'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json',
	                         'http://data.metagrid.co.uk/wfau/firethorn/types/adql-catalog-1.0.json',
	                         'http://data.metagrid.co.uk/wfau/firethorn/types/adql-schema-1.0.json'
	                         ]
	
	// Associative array containing types and their parent types
	this.parent_types = {'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json',
			'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json',
			'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json',
			'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json',
			'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-column-1.0.json' :'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json'
		};

	
	this.type_images = {'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : 'static/js/jquery-treeview/themes/default/images/res1.png',
			'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json' : 'static/js/jquery-treeview/themes/default/images/catalogue.png',
			'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json' : 'static/js/jquery-treeview/themes/default/images/schema.png',
			'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json' : 'static/js/jquery-treeview/themes/default/images/table.png',
			'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-column-1.0.json' :'static/js/jquery-treeview/themes/default/images/column.png',
			'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json' : 'static/js/jquery-treeview/themes/default/images/res.png',
			'http://data.metagrid.co.uk/wfau/firethorn/types/adql-catalog-1.0.json' : 'static/js/jquery-treeview/themes/default/images/catalogue.png',
			'http://data.metagrid.co.uk/wfau/firethorn/types/adql-schema-1.0.json' : 'static/js/jquery-treeview/themes/default/images/schema.png',
			'http://data.metagrid.co.uk/wfau/firethorn/types/adql-table-1.0.json' : 'static/js/jquery-treeview/themes/default/images/table.png',
			'http://data.metagrid.co.uk/wfau/firethorn/types/adql-column-1.0.json' :'static/js/jquery-treeview/themes/default/images/column.png',
			"" : 'images/res.png'
		};
	
	this.table_types = ['http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json',
	                    'http://data.metagrid.co.uk/wfau/firethorn/types/adql-table-1.0.json'];
	
	this.schema_types = ['http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json',
	                    'http://data.metagrid.co.uk/wfau/firethorn/types/adql-schema-1.0.json'];
	
	this.resource_types = ['http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json',
		                    'http://data.metagrid.co.uk/wfau/firethorn/types/adql-resource-1.0.json'];

	//Get the Path
	this.getPath = function () {
		return this.path;
	};
	
	//Get the Base URL
	this.getBaseURL = function () {
		return this.base_url;
	};
	
	this.isSchema = function (input_type) {
		return (this.schema_types.indexOf(input_type)>-1);
	};
	
	this.isTable = function (input_type) {
		return (this.table_types.indexOf(input_type)>-1);
	};
	
	this.isResource = function (input_type) {
		return (this.resource_types.indexOf(input_type)>-1);
	};
	
}


