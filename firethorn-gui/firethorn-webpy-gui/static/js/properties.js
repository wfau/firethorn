/* Global variables File */
var properties = new function() {
	this.path = ''; //'osa/';
	this.base_url = 'http://localhost:8090'; //http://djer.roe.ac.uk/osa'; 
	this.getPath = function () {
		return this.path;
	};
	this.getBaseURL = function () {
		return this.base_url;
	};
}


