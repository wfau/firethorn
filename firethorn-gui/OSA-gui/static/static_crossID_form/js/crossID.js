/*
 * WFAU Web2.0 Project
 * Author: Stelios Voutsinas
 * Date: July 2012
 * 
 * Javascript Functions and Variables used for the CrossID  Page, part of the OSA Interface
 * 
 * 
 */

var this_div_name = 'dbaccess_crossID_form';
init_scrolling_mechanism(this_div_name);
jQuery('a[rel*=facebox]').facebox(); 

var crossIDTable;	// DataTables object
var pathname_divId = '#pathname';
var content_div = '#content_' + this_div_name;
var error = "There was an error processing your request";
var gaiSelected =  [];

//SAMP Init Variables
var cc;
var callHandler;
var connector;
var tableUrl;
var tableId;
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
			fnSaveAsCustom('csv', crossIDTable, pathname_divId);
		},
		"fnSelect": null,
		"fnComplete": null,
		"fnInit": null,
		"fnAjaxComplete": function( json ) {
		}
	};


TableTools.BUTTONS.copy_to_fits_crossID_form = {
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
			fnSaveAsCustom('fits', crossIDTable, pathname_divId);
		
		},
		"fnSelect": null,
		"fnComplete": null,
		"fnInit": null,
		"fnAjaxComplete": function( json ) {
		}
	};
	
	 
	TableTools.BUTTONS.copy_to_votable_crossID_form = {
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
				fnSaveAsCustom('vo', crossIDTable, pathname_divId);
			
			},
			"fnSelect": null,
			"fnComplete": null,
			"fnInit": null,
			"fnAjaxComplete": function( json ) {
			}
	};
	
	 
	TableTools.BUTTONS.copy_to_html_crossID_form = {
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
				fnSaveAsCustom('html', crossIDTable, pathname_divId);
			
			},
			"fnSelect": null,
			"fnComplete": null,
			"fnInit": null,
			"fnAjaxComplete": function( json ) {
			}
	};
	
/* Custom TableTools buttons */

	
	

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
function validate_crossID(params){
	if (jQuery('#content_dbaccess_crossID_form #uploadFile').val()=="" || jQuery('#content_dbaccess_crossID_form #uploadFile').val()==null){
		return 'Please select a file to upload';
	}
	
	var uploadFile = "";
	
	for (var i = 0;i<params.length;i++){
		if (params[i].name=='database'){
			if (params[i].value=='' || params[i].value==null){
				return 'Database selection was invalid';
			} 
		} else if (params[i].name=='selectList') {
			if (params[i].value=='' || params[i].value==null){
				return 'Select input was invalid';
			} 
			
		} else if (params[i].name=='baseTable') {
			if (params[i].value=='' || params[i].value==null){
				return 'Please select a table';
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
	
	
	
	jQuery('#crossID_results').html("");

    cc = new samp.ClientTracker();
    callHandler = cc.callHandler;
	var meta = {
	    "samp.name": "OSA Results",
        "samp.description": "OSA Interface table results",
	    "samp.icon.url": "http://surveys.roe.ac.uk/osa/common/img/wfau_browser.gif"
        };

		var subs = cc.calculateSubscriptions();
    connector = new samp.Connector("OSA Results", meta, cc, subs);

	setInterval("updateStatus()", 1000);

	jQuery(content_div + ' #register').live('click',function(e){ 
	    connector.register();
	    updateStatus();
	    return false;
	});
	
	jQuery(content_div + ' #unregister').live('click',function(e){ 
	    connector.unregister();
	    updateStatus();
	    return false;
	});
	
	jQuery('#crossID_loadImage').live('click',function(e){ 
		var img_link = this.name;
        var imgUrl = img_link;
        var imgName = "ROE - Get Image";
        var msg = new samp.Message("image.load.fits",
                               {"image-id": imgUrl,
			       "url": imgUrl,
			       "name": imgName});
        connector.connection.notifyAll([msg]);

	    return false;
	});
	
	jQuery(window).unload( function () { connector.unregister(); } ); 
	//jQuery('form[name=crossID_form]').ajaxForm();	// Initialise ajaxForm object for main page form

	/**
	 * Handle minimizer click by toggling the visibility of the form content
	 * 
	 */
	jQuery('#content_dbaccess_crossID_form #crossID_minimizer').bind('click',function(){ 
   		toggle_minimize_button('#content_dbaccess_crossID_form #crossID_minimizer','#content_dbaccess_crossID_form .crossID_form', '[+]', '[-]');
   	});
	
	/**
	 * Handle file upload by checking that the size doesn't exceed 5 MB
	 * 
	 */
	jQuery('#content_dbaccess_crossID_form #uploadFile').change(function(){
	    var file = this.files[0];
	    name = file.name;
	    size = file.size;
		if (size>(1024*5)){
         	jQuery('#content_dbaccess_crossID_form #crossID_error').html('Upload file-size exceeded..Please try again');	
  			jQuery('#content_dbaccess_crossID_form #crossID_error').fadeIn('fast');
         	jQuery('#content_dbaccess_crossID_form #crossID_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_crossID_form #error_tooltip_wrapper').fadeIn('slow');
    		jQuery('#content_dbaccess_crossID_form #crossID_error').scrollMinimal('content_dbaccess_crossID_form');

		}
	   
	});

	/**
	 * Main crossID form submission
	 * Check if parameters input are valid
	 * If so, use proxy URL to submit an ajax request to a Java Servlet that will handle the crossID query
	 * 
	 */
	jQuery('form[name=crossID_form]').each(function() {
		
   	  	jQuery(this).submit(function(){

	  		var form = jQuery(this).serialize();
			var params_for_validation =  jQuery(this).serializeArray();
			var validated = validate_crossID(params_for_validation);
			jQuery('#content_dbaccess_crossID_form #error_tooltip_wrapper').hide();
			
			if (validated!='true' ){
				
				jQuery('#content_dbaccess_crossID_form #crossID_load').hide();
             	jQuery('#content_dbaccess_crossID_form #crossID_error').html('There was an error processing your request: ' + validated);	
       			jQuery('#content_dbaccess_crossID_form #crossID_error').fadeIn('fast');
              	jQuery('#content_dbaccess_crossID_form #crossID_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_crossID_form #error_tooltip_wrapper').fadeIn('slow');
              	jQuery('#crossID_results').hide();
        		jQuery('#content_dbaccess_crossID_form #crossID_error').scrollMinimal('content_dbaccess_crossID_form');
				return false;
			} else {
			

			jQuery('#content_dbaccess_crossID_form #crossID_load').fadeIn('normal');
			jQuery('#content_dbaccess_crossID_form #crossID_load').scrollMinimal('#content_dbaccess_crossID_form');

	  	    jQuery(this).ajaxSubmit({
	  	    	type: "POST",
		        url: path + "getImageHandler",
		        data : {action : "crossID"},
	  	    	error: function(e) {
	  	    		
	             	jQuery('#content_dbaccess_crossID_form #crossID_load').hide();
	             	jQuery('#content_dbaccess_crossID_form #crossID_error').html('There was an error processing your request');	
	      			jQuery('#content_dbaccess_crossID_form #crossID_error').fadeIn('fast');
	             	jQuery('#content_dbaccess_crossID_form #crossID_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_crossID_form #error_tooltip_wrapper').fadeIn('slow');
	             	jQuery('#crossID_results').hide();
	        		jQuery('#content_dbaccess_crossID_form #crossID_error').scrollMinimal('content_dbaccess_crossID_form');

	           },
	           success: function(data) {
	        	  
	        	   	if (data == "NOT_LOGGED_IN"){
                  		jQuery('#content_dbaccess_crossID_form  #crossID_load').hide();
                      	jQuery('#content_dbaccess_crossID_form  #crossID_error').html('Not logged in <br /> Please log in to get access to this form');	
                      	jQuery('#content_dbaccess_crossID_form  #crossID_error').fadeIn('fast');
                      	jQuery('#content_dbaccess_crossID_form  #crossID_error').delay(3500).fadeOut('slow');                           				                         		
                		jQuery('#content_dbaccess_crossID_form #crossID_error').scrollMinimal('content_dbaccess_crossID_form');

	        	   	} else if (data=="" || data==null || data=='None'){
	            	 	jQuery('#content_dbaccess_crossID_form #crossID_error').html('There was an error processing your request');	
	          			jQuery('#content_dbaccess_crossID_form #crossID_error').fadeIn('fast');
	                 	jQuery('#content_dbaccess_crossID_form #crossID_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_crossID_form #error_tooltip_wrapper').fadeIn('slow');
	                 	jQuery('#content_dbaccess_crossID_form #crossID_load').hide();
	            		jQuery('#content_dbaccess_crossID_form #crossID_error').scrollMinimal('content_dbaccess_crossID_form');

	        	   	} else {
	            	   	jQuery('form[name=crossID_form]').slideUp("slow", function () {
	                 		update_size();
                 		});
	            		jQuery('#content_dbaccess_crossID_form #crossID_minimizer').html('[+]');
	                	jQuery('#content_dbaccess_crossID_form #crossID_minimizer').show();
						jQuery('#content_dbaccess_crossID_form #crossID_load').hide();
						jQuery('#crossID_results').fadeIn('slow');
						
						var div = document.createElement('html');
						div.innerHTML = data;
						var all = div.getElementsByTagName("table");
						var html_table = "";
						var rows_tmp = div.getElementsByTagName("tr");
						var heading = '<thead><tr>';
						if (rows_tmp.length<=0){
		   				 
		                 	jQuery('#content_dbaccess_crossID_form #crossID_error').html('There was an error processing your request' );	
		          			jQuery('#content_dbaccess_crossID_form #crossID_error').fadeIn('fast');
		                 	jQuery('#content_dbaccess_crossID_form #crossID_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_crossID_form #error_tooltip_wrapper').fadeIn('slow');
		                 	jQuery('#content_dbaccess_crossID_form #crossID_load').hide();
		            		jQuery('#content_dbaccess_crossID_form #crossID_error').scrollMinimal('content_dbaccess_crossID_form');

						} else if (rows_tmp.length>1){
							
							
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
						
							html_table = '<table cellpadding="0" cellspacing="0" border="0" class="datatables" id="crossID_table">' + heading + rows + '</table>';
							var samp_content = '<div style="text-align:left;margin-top:10px;"><img id ="samp_connection" src="' + survey_prefix +'/static/images/red-button.png" height="17" width="17" /><button id="register">Connect to SAMP</button><button id="unregister" style="display: none;">Disconnect from SAMP</button>';
		                	samp_content += '</div><br />';
		                	
							jQuery('#crossID_results').html(html_table + samp_content).fadeIn('slow');
				
							var pathname = "";
							
							
							
							// Write HTML Table to file
							jQuery.ajax({
						           	type: "POST",
						            url: path + "writeHTMLtoFile",
						            timeout: 1000000,
						            data: {html_table : html_table},
						            error: function() {
						        		jQuery('#content_dbaccess_crossID_form #crossID_load').hide();
						             	jQuery('#content_dbaccess_crossID_form #crossID_error').html('There was an error processing your request');	
						       			jQuery('#content_dbaccess_crossID_form #crossID_error').fadeIn('fast');
						              	jQuery('#content_dbaccess_crossID_form #crossID_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_crossID_form #error_tooltip_wrapper').fadeIn('slow');
						              	jQuery('#crossID_results').hide();
						        		jQuery('#content_dbaccess_crossID_form #crossID_error').scrollMinimal('content_dbaccess_crossID_form');

						            },
						            success: function(data) {
										//Add hidden forms for save as options
						            	pathname = data;
						            	var hidden_button_forms = '<div style="display:none"><div id="save_as"><form method="post" action="' + path + 'save_as_vot" name="save_as_vot"> <input type="hidden" value="' + pathname +'" name="pathname"><input type="submit" value="Save as Votable" class="save_button"></form></div><div id="save_as"><form method="post" action="' + path + 'save_as_html" name="save_as_html">';
										hidden_button_forms += '<input type="hidden" value="' + pathname +'" name="pathname"><input type="submit" value="Save as HTML" class="save_button"></form></div><div id="save_as"><form method="post" action="' + path + 'save_as_fits" name="save_as_fits"> <input type="hidden" value="' + pathname +'" name="pathname" id="pathname"><input type="submit" value="Save as Fits" class="save_button"></form></div></div>';
										jQuery('#crossID_results').append(hidden_button_forms);
										
						            }
							});
						
						
							crossIDTable = jQuery('#crossID_results table').dataTable({
								"bJQueryUI" : true,
                        		"sPaginationType": "full_numbers",
                        		"sDom" : 'T<"clear">R<"H"Clfr>t<"F"ip>',
                        		"oLanguage": {
                        	         "sProcessing": "<img style='position:absolute;left:45px;top:-2px;' src='" + survey_prefix + "/static/static_vo_tool/ajax-loader.gif'>"
                        	       },
                        	 	"bAutoWidth": false,
                        		"sScrollX": "100%",
                        		"oColVis": {
                        			"bRestore": true,
                        			"sSize": "css"
                        		},
                        		"aaSorting": [],
                        		"oTableTools": {
                        			"aButtons": [
                        			      "copy_to_votable_crossID_form","copy_to_fits_crossID_form","copy_to_html_crossID_form", "csv_button", "copy"
                        			 ]
                        		}

		            		});
							current_table = crossIDTable;
						   	jQuery("#content_dbaccess_crossID_form .scroll-wrapper").remove();
	                        jQuery('#content_dbaccess_crossID_form .dataTables_scrollBody').doubleScroll();
							
						} 
						
	                }
		     	}
	   	 	});
		    
		 	
		 	
	  	    return false;

			} 
		 	
	  	 });
	  
	   });
	
	  
	
	
		
	   /**
	    * Click listener for anchor links in the crossID dynamic result content
	    * Uses the proxy to fetch the according images or content using ajax
	    * 
	    */
		 jQuery('#crossID_results a').live('click', function(e){
		    jQuery('#content_dbaccess_crossID_form #crossID_load').fadeIn('normal');
		   	 	 e.preventDefault();
		    	 jQuery.ajax({
		           	type: "POST",
		            url: path + "getImageHandler",
		            timeout: 1000000,
		            data: {href : this.href, action : 'crossID_read'},
		            error: function() {
		             	jQuery('#content_dbaccess_crossID_form #crossID_error').html('There was an error processing your request');	
		       			jQuery('#content_dbaccess_crossID_form #crossID_error').fadeIn('	fast');
		              	jQuery('#content_dbaccess_crossID_form #crossID_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_crossID_form #error_tooltip_wrapper').fadeIn('slow');
		              	jQuery('#content_dbaccess_crossID_form #crossID_load').hide();
		              	jQuery('#crossID_results_secondary').hide();
		        		jQuery('#content_dbaccess_crossID_form #crossID_error').scrollMinimal('content_dbaccess_crossID_form');

		            },
		            success: function(data) {
		            	if (data==""){
		            	
		                 	jQuery('#content_dbaccess_crossID_form #crossID_error').html('There was an error processing your request');	
		          			jQuery('#content_dbaccess_crossID_form #crossID_error').fadeIn('fast');
		                 	jQuery('#content_dbaccess_crossID_form #crossID_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_crossID_form #error_tooltip_wrapper').fadeIn('slow');
		                 	jQuery('#content_dbaccess_crossID_form #crossID_load').hide();
		                 	jQuery('#crossID_results_secondary').hide();
		            		jQuery('#content_dbaccess_crossID_form #crossID_error').scrollMinimal('content_dbaccess_crossID_form');

		            	} else {
		                	jQuery('#content_dbaccess_crossID_form #crossID_load').hide();

		           	    	var div = document.createElement('html');
							div.innerHTML = '<div id="crossID_facebox_wrapper">' + data + '</div>';
							
							var imgs = div.getElementsByTagName("img");

							for (var x = 0;x<imgs.length;x++){
								if  (connector.connection!=null){
									jQuery(imgs[x]).parent().after('<button id="crossID_loadImage" name ="' + jQuery(imgs[x]).attr('src') + '">Broadcast Image</button>');
								}	
							}
							
							jQuery('#crossID_results_secondary').html(div.getElementsByTagName("body"));
		                 	
		                 	jQuery.facebox({ div: '#crossID_results_secondary' });
		                }
			        }
		   	 	});
	  	
	  	return false;
	  });
		
	
	
	/**
	 * Handle a change in the baseTable div, by adding content or removing from the where Clause input
	 * 
	 */
	jQuery('#content_dbaccess_crossID_form #baseTable').change(function() {
		if (jQuery('#content_dbaccess_crossID_form #baseTable').val()==crossID_source_table){
			jQuery('#content_dbaccess_crossID_form #whereClause').val('(priOrSec<=0 OR priOrSec=frameSetID)');
		} else {
			jQuery('#content_dbaccess_crossID_form #whereClause').val('');

		}
	});
	
	jQuery("#content_dbaccess_crossID_form #error_tooltip_link").live ('click',function() {
 		jQuery('#content_dbaccess_crossID_form #crossID_error').slideToggle();
 		return false;

 	});
 	
	
});