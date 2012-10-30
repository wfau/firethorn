/* Global variables File */
var properties = new function() {
	this.path = ''; 
	
	// The base url for this app
	this.base_url = 'http://localhost:8090'; 
	
	//The URL for the java web services used by the gui
	this.web_services_url = 'http://localhost:8080';
	
	// The default [base] Location
	this.vospace_dir = this.web_services_url + '/firethorn/jdbc/resources/select';
	this.vospace_fileConnector = 'vospace';
	
	// Root type
	this.root_type = 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json';
	
	// Type of object that has no children
	this.highest_type = 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-column-1.0.json';

	// Types that may have children
	this.types_as_folders = ['http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json',
	                         'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json',
	                         'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json',
	                         'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json']
	
	// Associative array containing types and their parent types
	this.parent_types = {'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json',
			'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json',
			'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json',
			'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json',
			'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-column-1.0.json' :'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json'
		};
	
	//Get the Path
	this.getPath = function () {
		return this.path;
	};
	
	//Get the Base URL
	this.getBaseURL = function () {
		return this.base_url;
	};
}


