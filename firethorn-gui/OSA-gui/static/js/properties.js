/* Global Properties */
var sub_app_prefix='osa';
var base_host = 'localhost';
var port = "";
var survey_folder = 'osa';

var path = ((sub_app_prefix!="") ?  "/" + sub_app_prefix + '/' :  ''); //''
var survey_prefix = ((sub_app_prefix!="") ?  '/' + sub_app_prefix :  ''); //'';
var base_url = ((port=="") ?  'http://' + base_host + survey_prefix :  'http://' + base_host + ':' + port + survey_prefix);//'http://djer.roe.ac.uk/osa'; ; 
var vospace_dir = '/var/www/' + survey_folder + '/static/static_vospace/'; //'/home/stelios/Desktop/workspace/SSA-webpy/static/static_vospace/'; 
var vospace_fileConnector = path + 'vospace'; //'osa/vospace' ; 

var survey_short = 'OSA';
var survey_full = 'OmegaCAM Science Archive';

/* URLs */
var multiGetImageTempURL = "http://surveys.roe.ac.uk/osa/tmp/MultiGetImage/";
var multiGetImageCGI = "http://surveys.roe.ac.uk/wsa/cgi-bin/";

/* Global vars */
var availableTags = [
		         		"SELECT", "FROM", "ORDER BY","WHERE", "TOP","IN", "AND", "OR", "WITH", "DESC", "ASC", "JOIN", "AS", "HAVING", "ABS",
	         			"GROUP","BY", "INNER","OUTER","CROSS","LEFT","RIGHT","FULL","ON","USING","MIN","MAX","COUNT","DISTINCT","ALL","LIKE","ACOS","ASIN","ATAN","ATAN2","COS","SIN","TAN","COT","IS","NOT","NULL","NATURAL","EXISTS","BETWEEN","AREA","BOX","CENTROID","CIRCLE","CONTAINS","COORD1","COORD2","COORDSYS","DISTANCE","INTERSECTS","POINT","POLYGON","REGION"
	         		];

var current_table;
var connector_coverage;
var ping_interval;
var source_table = "ATLASsource";
var crossID_source_table = "source";

/* Global Functions */
var update_size = function() {
	if (current_table!=null) {

		jQuery(current_table).css({ width: "100%" });
		jQuery('.scroll-wrapper').css({ width: "100%" });
		current_table.fnAdjustColumnSizing(false );
	}
	return false;
}
