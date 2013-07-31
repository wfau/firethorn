/*
 * WFAU Web2.0 Project
 * Author: Stelios Voutsinas
 * Date: July 2012
 * 
 * Javascript Functions and Variables used for the CrossID  Page, part of the OSA Interface
 * 
 * 
 */

var this_div_name = 'dbaccess_radial_form';
init_scrolling_mechanism(this_div_name);
jQuery('a[rel*=facebox]').facebox(); 

var radialTable;	// DataTables object
var pathname_divId = '#pathname';
var content_div = '#content_' + this_div_name;
var dynamic_content_id = "#dynamic_content";
var error = "There was an error processing your request";

var radial_getImage_html_content = '\
									<p><br><input class="FontSans" name="selectall" value="&nbsp;check all&nbsp;" onclick="checkAll();" type="button">&nbsp;&nbsp;<input class="FontSans" name="selectall" value="uncheck all" onclick="uncheckAll();" type="button"></p> \
									<p><br> To extract images centred on the RA and Decs returned in the above table check the boxes of the objects you wish to view, select the waveband(s) required and enter a size.</p>\
									<p>Survey/Waveband (tick at least one box):<br></p>\
									<br><table border="0" style="color:#C2BEAD;" class="norm"><tbody><tr><td>Blue (UK-J/POSSII-B)</td><td><input type="checkbox" value="1" name="waveband1"></td></tr><tr><td>Red 1st epoch (ESO-R/POSSI-e)</td><td><input type="checkbox" value="1" name="waveband2"></td></tr><tr><td>Red 2nd epoch (UK-R/POSSII-R)</td><td><input type="checkbox" value="1" name="waveband3"></td></tr><tr><td>InfraRed (UK-I/POSSII-I)</td><td><input type="checkbox" value="1" name="waveband4"></td></tr></tbody></table>\
									<p><br>Size of extracted box: <input type="text" maxlength="4" size="4" value="1" name="size"> arcmin (maximum 3)</p>\
									<p><br><input type="submit" value="Get Thumbnails" class="submit_button"> &nbsp;&nbsp; <i>Click the get Thumbnails button to get images for the selected rows</i></p><br>';
									

var radial_getArea_form = '<form method id="radial_getArea" name="radial_getArea"><input type="submit" value="Get Area Image" class="submit_button"> &nbsp;&nbsp;<i>Click the Get Area Image button to retrieve an image of the searched area </i></form>';
	

/* Custom TableTools buttons */

TableTools.BUTTONS.csv_button  = {
		"sAction": "text",
		"sFieldBoundary": "",
		"sFieldSeperator": "\t",
		"sAjaxUrl": "save_as_html",
		"sToolTip": "",
		"sButtonClass": "DTTT_button_text",
		"sButtonClassHover": "DTTT_button_text_hover",
		"sButtonText": "CSV",
		"mColumns": "all",
		"bHeader": true,
		"bFooter": true,
		"fnMouseover": null,
		"fnMouseout": null,
		"fnClick": function( nButton, oConfig ) {
			fnSaveAsCustom('csv', radialTable, pathname_divId);
		},
		"fnSelect": null,
		"fnComplete": null,
		"fnInit": null,
		"fnAjaxComplete": function( json ) {
		}
	};


TableTools.BUTTONS.copy_to_fits_radial_form = {
		"sAction": "text",
		"sFieldBoundary": "",
		"sFieldSeperator": "\t",
		"sAjaxUrl": "save_as_fits",
		"sToolTip": "",
		"sButtonClass": "DTTT_button_text",
		"sButtonClassHover": "DTTT_button_text_hover",
		"sButtonText": "FITS",
		"mColumns": "all",
		"bHeader": true,
		"bFooter": true,
		"fnMouseover": null,
		"fnMouseout": null,
		"fnClick": function( nButton, oConfig ) {
			fnSaveAsCustom('fits', radialTable, pathname_divId);
		
		},
		"fnSelect": null,
		"fnComplete": null,
		"fnInit": null,
		"fnAjaxComplete": function( json ) {
		}
	};
	
	 
	TableTools.BUTTONS.copy_to_votable_radial_form = {
			"sAction": "text",
			"sFieldBoundary": "",
			"sFieldSeperator": "\t",
			"sAjaxUrl": "save_as_vot",
			"sToolTip": "",
			"sButtonClass": "DTTT_button_text",
			"sButtonClassHover": "DTTT_button_text_hover",
			"sButtonText": "VOTable",
			"mColumns": "all",
			"bHeader": true,
			"bFooter": true,
			"fnMouseover": null,
			"fnMouseout": null,
			"fnClick": function( nButton, oConfig ) {
				fnSaveAsCustom('vo', radialTable, pathname_divId);
			
			},
			"fnSelect": null,
			"fnComplete": null,
			"fnInit": null,
			"fnAjaxComplete": function( json ) {
			}
	};
	
	 
	TableTools.BUTTONS.copy_to_html_radial_form = {
			"sAction": "text",
			"sFieldBoundary": "",
			"sFieldSeperator": "\t",
			"sAjaxUrl": "save_as_html",
			"sToolTip": "",
			"sButtonClass": "DTTT_button_text",
			"sButtonClassHover": "DTTT_button_text_hover",
			"sButtonText": "HTML",
			"mColumns": "all",
			"bHeader": true,
			"bFooter": true,
			"fnMouseover": null,
			"fnMouseout": null,
			"fnClick": function( nButton, oConfig ) {
				fnSaveAsCustom('html', radialTable, pathname_divId);
			
			},
			"fnSelect": null,
			"fnComplete": null,
			"fnInit": null,
			"fnAjaxComplete": function( json ) {
			}
	};
	
/* End Custom TableTools buttons */

	
/**
 * Check all checkbox values in a dataTables table
 * 
 */	
function checkAll(){
	jQuery('input:checkbox[name="ch"]').attr('checked', 'checked');		
}

/**
 * Uncheck all checkbox values in a dataTables table
 * 
 */
function uncheckAll(){
	jQuery('input:checkbox[name="ch"]').attr('checked', '');
}
	

/**
 * Check if given string is a parsable float
 * 
 * @param n
 * @returns {Boolean}
 */	
function isNumber(n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
}

/**
 * Validate parameters passed to the CrossID form
 * 
 * @param params
 * @returns {String}
 */
function validate_radial(params){
	for (var i = 0;i<params.length;i++){
		if (params[i].name=='database'){
			if (params[i].value=='' || params[i].value==null){
				return 'Database selection was invalid';
			} 
		} else if (params[i].name=='ra'){
			
			if (params[i].value=='' || params[i].value==null || !isNumber(parseFloat ( params[i].value))){
				return 'Ra input was invalid';
			} 
		 
		} else if (params[i].name=='dec'){

			if (params[i].value=='' || params[i].value==null || !isNumber(parseFloat ( params[i].value))){
				return 'Dec input was invalid';
			} 
		 
		} else if (params[i].name=='select') {
			if (params[i].value=='' || params[i].value==null){
				return 'Select input was invalid';
			} 
			
		} else if (params[i].name=='rows') {
			if (params[i].value=='' || params[i].value==null || !isNumber(parseFloat ( params[i].value))){
				return 'Number of rows entered was invalid';
			} 
			
		} else if (params[i].name=='radius'){
			if (params[i].value=='' || params[i].value==null){
				return 'Empty Radius input';
			} else {
				try {
					if (!isNumber(parseFloat ( params[i].value))){
						return 'Invalid radius input';
					} else if (parseFloat( params[i].value )>300.0 ){
						return "Radius was higher than 12 arcmin";
					} else if (parseFloat( params[i].value )<0.0){
						return "Radius was less than 0";
					} 
				} catch (err){
					return 'Invalid radius input';
				}
			}
		} 
		
	} // loop complete
	

	return 'true';
}

/**
 * Check if a string (str) ends with a suffix (suffix)
 * @param str
 * @param suffix
 * @returns {Boolean}
 */
function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

/**
 * On ready execute
 * 
 */
jQuery(document).ready(function(){
	
	jQuery('#radial_results').html("");
	//jQuery('form[name=radial_form]').ajaxForm();	// Initialise ajaxForm object for main page form

	/**
	 * Handle minimizer click by toggling the visibility of the form content
	 * 
	 */
	jQuery('#content_dbaccess_radial_form #radial_minimizer').bind('click',function(){ 
   		toggle_minimize_button('#content_dbaccess_radial_form #radial_minimizer','#content_dbaccess_radial_form .radial_form', '[+]', '[-]');
   	});


	/**
	 * Main radial form submission
	 * Check if parameters input are valid
	 * If so, use proxy URL to submit an ajax request to a Java Servlet that will handle the radial query
	 * 
	 */
	jQuery('form[name=radial_form]').each(function() {
   	  	jQuery(this).submit(function(e){
   	  

   	  		jQuery('#radial_results').html("");
	  		var form = jQuery(this).serialize();
			var params_for_validation =  jQuery(this).serializeArray();
			var validated = validate_radial(params_for_validation);
		
			if (validated=='true' ){
			jQuery('#content_dbaccess_radial_form #radial_load').fadeIn('normal');
			jQuery('#content_dbaccess_radial_form #radial_load').scrollMinimal('#content_dbaccess_radial_form');

	  	    jQuery(this).ajaxSubmit({
	  	    	type: "POST",
		        url: path + "getImageHandler",
		        data : {action : "radial"},
	  	    	error: function() {
	             	jQuery('#content_dbaccess_radial_form #radial_load').hide();
	             	jQuery('#content_dbaccess_radial_form #radial_error').html('There was an error processing your request');	
	      			jQuery('#content_dbaccess_radial_form #radial_error').fadeIn('fast');
	             	jQuery('#content_dbaccess_radial_form #radial_error').delay(3500).fadeOut('slow');
	             	jQuery('#radial_results').hide();
	           },
	           success: function(data) {
	        	   	if (data == "NOT_LOGGED_IN"){
                  		jQuery('#content_dbaccess_radial_form  #radial_load').hide();
                      	jQuery('#content_dbaccess_radial_form  #radial_error').html('Not logged in <br /> Please log in to get access to this form');	
                      	jQuery('#content_dbaccess_radial_form  #radial_error').fadeIn('fast');
                      	jQuery('#content_dbaccess_radial_form  #radial_error').delay(3500).fadeOut('slow');                           				                         		
	        	   	} else if (data=="" || data==null || data=='None'){
	            	 	jQuery('#content_dbaccess_radial_form #radial_error').html('There was an error processing your request');	
	          			jQuery('#content_dbaccess_radial_form #radial_error').fadeIn('fast');
	                 	jQuery('#content_dbaccess_radial_form #radial_error').delay(3500).fadeOut('slow');
	                 	jQuery('#content_dbaccess_radial_form #radial_load').hide();
	            	} else {
	            	   	jQuery('form[name="radial_form"]').slideUp('slow');
	            		jQuery('#content_dbaccess_radial_form #radial_minimizer').html('[+]');
	                	jQuery('#content_dbaccess_radial_form #radial_minimizer').show();
						jQuery('#content_dbaccess_radial_form #radial_load').hide();
						jQuery('#radial_results').fadeIn('slow');
						
						var div = document.createElement('html');
						div.innerHTML = data;
						var error_message = div.getElementsByClassName("error");

	            		var all = [];
						var html_table = "";
						var rows_tmp = [];
						var heading = '';
						
		            	if  (error_message.length>0) {
		            		error = "";
		            		for (var el = 0; el<error_message.length;el++){
		            			if (error_message[el].innerHTML.trim().toLowerCase()!="error message"){
		            				error +=error_message[el].innerHTML;
		            			}
		            		}
		            		
		            	} else {
		            	
		            		all = div.getElementsByTagName("table")[0];
							html_table = "";
							rows_tmp = all.getElementsByTagName("tr");
							heading = '<thead><tr>';
			            }
		            	
						if (rows_tmp.length>1){
							
							
							heading_elems = div.getElementsByTagName("th");
							for (var c=0;c<heading_elems.length;c++){
								if (c>0){
									heading+='<th>' + heading_elems[c].innerHTML + '</th>';		
								}
							}
							
							heading+='</tr></thead>';
						
							var rows = '<tbody>'
							for  (var x=0;x<rows_tmp.length;x++){
								if (x>0){
									
									rows +='<tr>'
									var td_tmp = rows_tmp[x].childNodes;										
									
									for  (var y=0;y<td_tmp.length;y++){
										if (y>1){
											if (td_tmp[y].innerHTML=="&nbsp;" || td_tmp[y].innerHTML=="&nbsp" ){
												rows+='<td>' + "NONE" + '</td>';
											} else if (td_tmp[y].innerHTML!="" && td_tmp[y].innerHTML!=null){
												rows+='<td>' + td_tmp[y].innerHTML + '</td>';
											}
										}
										
									}
									rows +='</tr>';
								}
							}
							
							rows +='</tbody>'
							var get_Image_hidden_input = '<input type="hidden" id="get_Image_hidden_input" name="href" value="http://www-wfau.roe.ac.uk/sss/cgi-bin/checkbox.cgi" >'
							var datatable = '<table cellpadding="0" cellspacing="0" border="0" class="datatables" id="radial_table">' + heading + rows + '</table>' ;
							html_table = '<form method id="radial_getImage" name="radial_getImage">'+ datatable + radial_getImage_html_content + get_Image_hidden_input + '</form>' + radial_getArea_form;
							
							jQuery('#radial_results').html(html_table).fadeIn('slow');
				
							var pathname = "";
							
							
							
							// Write HTML Table to file
							jQuery.ajax({
						           	type: "POST",
						            url: path + "writeHTMLtoFile",
						            timeout: 1000000,
						            data: {html_table : datatable},
						            error: function() {
						        		jQuery('#content_dbaccess_radial_form #radial_load').hide();
						             	jQuery('#content_dbaccess_radial_form #radial_error').html('There was an error processing your request');	
						       			jQuery('#content_dbaccess_radial_form #radial_error').fadeIn('fast');
						              	jQuery('#content_dbaccess_radial_form #radial_error').delay(3500).fadeOut('slow');
						              	jQuery('#radial_results').hide();
						            },
						            success: function(data) {
										//Add hidden forms for save as options
						            	pathname = data;
						            	var hidden_button_forms = '<div style="display:none"><div id="save_as"><form method="post" action="' + path + 'save_as_vot" name="save_as_vot"> <input type="hidden" value="' + pathname +'" name="pathname"><input type="submit" value="Save as Votable" class="save_button"></form></div><div id="save_as"><form method="post" action="' + path + 'save_as_html" name="save_as_html">';
										hidden_button_forms += '<input type="hidden" value="' + pathname +'" name="pathname"><input type="submit" value="Save as HTML" class="save_button"></form></div><div id="save_as"><form method="post" action="' + path + 'save_as_fits" name="save_as_fits"> <input type="hidden" value="' + pathname +'" name="pathname" id="pathname"><input type="submit" value="Save as Fits" class="save_button"></form></div></div>';
										jQuery('#radial_results').append(hidden_button_forms);
										
						            }
							});
						
						
							radialTable = jQuery('#radial_results #radial_table').dataTable({
								"bJQueryUI" : true,
                        		"sPaginationType": "full_numbers",
                        		"sDom" : 'T<"clear">R<"H"Clfr>t<"F"ip>',
                        		"oLanguage": {
                        	         "sProcessing": "<img style='position:absolute;left:45px;top:-2px;' src='" + survey_prefix + "/static/static_vo_tool/ajax-loader.gif'>"
                        	       },
                        	 	"bAutoWidth": false,
                        		"sScrollX": "100%",
                        		
                        		"aaSorting": [],
                        		"oTableTools": {
                        			"aButtons": [
                        			      "copy_to_votable_radial_form","copy_to_fits_radial_form","copy_to_html_radial_form", "csv_button", "copy"
                        			 ]
                        		}

		            		});
							
							
						   	jQuery("#content_dbaccess_radial_form .scroll-wrapper").remove();
	                        jQuery('#content_dbaccess_radial_form .dataTables_scrollBody').doubleScroll();
							
						} else {
							jQuery('#content_dbaccess_radial_form #radial_load').hide();
			             	jQuery('#content_dbaccess_radial_form #radial_error').html('There was an error processing your request');	
			       			jQuery('#content_dbaccess_radial_form #radial_error').fadeIn('fast');
			              	jQuery('#content_dbaccess_radial_form #radial_error').delay(3500).fadeOut('slow');
			              	jQuery('#radial_results').hide();
							
						}
						
	                }
		     	}
	   	 	});
		   
		 	

			} else {
			 //invalid params
				
             	jQuery('#content_dbaccess_radial_form #radial_error').html(validated);	
      			jQuery('#content_dbaccess_radial_form #radial_error').fadeIn('fast');
             	jQuery('#content_dbaccess_radial_form #radial_error').delay(3500).fadeOut('slow');
             	jQuery('#content_dbaccess_radial_form #radial_load').hide();
			 
			}
	  	    
			return false;

	  	 });
	  
	   });
	
	
	/**
     * Click listener for the radial get Area button
     * Get the according form content via ajax request to the server
     * 
     */
		 
	jQuery('form[name=radial_getArea]').live("submit", function() { 
		
			jQuery.ajax({
		           	type: "POST",
		            url: path + "dbaccess_pixel",
		            timeout: 1000000,
		            data: {} ,
		            error: function() {
		             	jQuery('#content_dbaccess_radial_form #radial_error').html('There was an error processing your request');	
		       			jQuery('#content_dbaccess_radial_form #radial_error').fadeIn('	fast');
		              	jQuery('#content_dbaccess_radial_form #radial_error').delay(3500).fadeOut('slow');
		              	jQuery('#content_dbaccess_radial_form #radial_load').hide();
		              	jQuery('#radial_results_secondary').hide();
		            },
		            success: function(data) {
		            	if (data==""){
		            	
		                 	jQuery('#content_dbaccess_radial_form #radial_error').html('There was an error processing your request');	
		          			jQuery('#content_dbaccess_radial_form #radial_error').fadeIn('fast');
		                 	jQuery('#content_dbaccess_radial_form #radial_error').delay(3500).fadeOut('slow');
		                 	jQuery('#content_dbaccess_radial_form #radial_load').hide();
		                 	jQuery('#radial_results_secondary').hide();
		            	} else {
				    		 jQuery(dynamic_content_id).html(data).fadeIn();
		                }
			        }
	   	 	});
	 	   	return false;


	});
	
	/**
     * Click listener for the radial get Image button
     * Uses the proxy to fetch the according images or content using ajax
     * 
     */
		 
	jQuery('form[name=radial_getImage]').live("submit", function() { 
		
			jQuery('#content_dbaccess_radial_form #radial_load').fadeIn('normal');
			jQuery('#content_dbaccess_radial_form #radial_load').scrollMinimal('#content_dbaccess_radial_form');

	 		var form = jQuery(this).serialize();
	 		
			jQuery.ajax({
		           	type: "POST",
		            url: path + "getImageHandler",
		            timeout: 1000000,
		            data: {params : form, href : jQuery('#get_Image_hidden_input').val()} ,
		            error: function() {
		             	jQuery('#content_dbaccess_radial_form #radial_error').html('There was an error processing your request');	
		       			jQuery('#content_dbaccess_radial_form #radial_error').fadeIn('	fast');
		              	jQuery('#content_dbaccess_radial_form #radial_error').delay(3500).fadeOut('slow');
		              	jQuery('#content_dbaccess_radial_form #radial_load').hide();
		              	jQuery('#radial_results_secondary').hide();
		            },
		            success: function(data) {
		            	if (data==""){
		            	
		                 	jQuery('#content_dbaccess_radial_form #radial_error').html('There was an error processing your request');	
		          			jQuery('#content_dbaccess_radial_form #radial_error').fadeIn('fast');
		                 	jQuery('#content_dbaccess_radial_form #radial_error').delay(3500).fadeOut('slow');
		                 	jQuery('#content_dbaccess_radial_form #radial_load').hide();
		                 	jQuery('#radial_results_secondary').hide();
		            	} else {
		                	jQuery('#content_dbaccess_radial_form #radial_load').hide();
		           	    	jQuery('#radial_results_secondary').html(data);
		                 	jQuery.facebox({ div: '#radial_results_secondary' });
		                }
			        }
	   	 	});
	 	   	return false;


	});
	
     /**
      * Click listener for anchor links in the radial dynamic result content
      * Uses the proxy to fetch the according images or content using ajax
      * 
      */
	 jQuery('#radial_results a').live('click', function(e){
  	    jQuery('#content_dbaccess_radial_form #radial_load').fadeIn('normal');
		jQuery('#content_dbaccess_radial_form #radial_load').scrollMinimal('#content_dbaccess_radial_form');

	   	 	 e.preventDefault();
	    	 jQuery.ajax({
	           	type: "POST",
	            url: path + "getImageHandler",
	            timeout: 1000000,
	            data: {href : this.href},
	            error: function() {
	             	jQuery('#content_dbaccess_radial_form #radial_error').html('There was an error processing your request');	
	       			jQuery('#content_dbaccess_radial_form #radial_error').fadeIn('	fast');
	              	jQuery('#content_dbaccess_radial_form #radial_error').delay(3500).fadeOut('slow');
	              	jQuery('#content_dbaccess_radial_form #radial_load').hide();
	              	jQuery('#radial_results_secondary').hide();
	            },
	            success: function(data) {
	            	if (data==""){
	            	
	                 	jQuery('#content_dbaccess_radial_form #radial_error').html('There was an error processing your request');	
	          			jQuery('#content_dbaccess_radial_form #radial_error').fadeIn('fast');
	                 	jQuery('#content_dbaccess_radial_form #radial_error').delay(3500).fadeOut('slow');
	                 	jQuery('#content_dbaccess_radial_form #radial_load').hide();
	                 	jQuery('#radial_results_secondary').hide();
	            	} else {
	                	jQuery('#content_dbaccess_radial_form #radial_load').hide();
	           	    	jQuery('#radial_results_secondary').html(data);
	                 	jQuery.facebox({ div: '#radial_results_secondary' });
	                }
		        }
	   	 	});
    	
    	return false;
    });
	
	

	
});