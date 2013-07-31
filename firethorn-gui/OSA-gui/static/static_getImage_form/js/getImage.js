/*
 * WFAU Web2.0 Project
 * Author: Stelios Voutsinas
 * Date: July 2012
 * 
 * Javascript Functions and Variables used for the getImage Page, part of the OSA Interface
 * 
 * 
 */

var this_div_name = 'dbaccess_getImage_form';
init_scrolling_mechanism(this_div_name);
var content_div = '#content_' + this_div_name;
jQuery('#getImage_results').html("");
var pathname_divId = '#pathname';
var gaiSelected =  [];
var getImageTable;

//SAMP Init Variables
var cc;
var callHandler;
var connector;
var tableUrl;
var tableId;

jQuery('a[rel*=facebox]').facebox(); 	// Initiate facebox functionality



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
			fnSaveAsCustom('csv', getImageTable, pathname_divId);
		},
		"fnSelect": null,
		"fnComplete": null,
		"fnInit": null,
		"fnAjaxComplete": function( json ) {
		}
	};


TableTools.BUTTONS.copy_to_fits_getImage_form = {
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
			fnSaveAsCustom('fits', getImageTable, pathname_divId);
		
		},
		"fnSelect": null,
		"fnComplete": null,
		"fnInit": null,
		"fnAjaxComplete": function( json ) {
		}
	};
	
	 
	TableTools.BUTTONS.copy_to_votable_getImage_form = {
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
				fnSaveAsCustom('vo', getImageTable, pathname_divId);
			
			},
			"fnSelect": null,
			"fnComplete": null,
			"fnInit": null,
			"fnAjaxComplete": function( json ) {
			}
	};
	
	 
	TableTools.BUTTONS.copy_to_html_getImage_form = {
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
				fnSaveAsCustom('html', getImageTable, pathname_divId);
			
			},
			"fnSelect": null,
			"fnComplete": null,
			"fnInit": null,
			"fnAjaxComplete": function( json ) {
			}
	};
	
/* End Custom TableTools buttons */
	
/**
 * Check if a given string is a parsable float
 * 
 * @param n
 * @returns {Boolean}
 */
function isNumber(n) {
		  return !isNaN(parseFloat(n)) && isFinite(n);
	}
	
/**
 * Check if a string ends with a given suffix
 * 
 * @param str
 * @param suffix
 * @returns {Boolean}
 */
function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

function checkAll(){
	var rows = jQuery('#getImage tbody tr');
	for (var i=0;i<rows.length;i++){
		if ( !jQuery(rows[i]).hasClass('row_selected') )
			jQuery(rows[i]).addClass('row_selected');
		var aData = getImageTable.fnGetData( rows[i] );
		var iId = aData[0];
		
		if ( jQuery.inArray(iId, gaiSelected) == -1 )
		{
			gaiSelected[gaiSelected.length++] = iId;
		}
		else
		{
			gaiSelected = jQuery.grep(gaiSelected, function(value) {
				return value != iId;
			} );
		}
	}
	
	
}

function uncheckAll(){

	var rows = jQuery('#getImage tbody tr');
	for (var i=0;i<rows.length;i++){
		if ( jQuery(rows[i]).hasClass('row_selected') )
			jQuery(rows[i]).removeClass('row_selected');	
	}
	gaiSelected = [];

}
/**
 * Validate the parameters taken from a getImage form submission
 * 
 * @param params
 * @returns {String}
 */
function validate_getImage(params){

	for (var i = 0;i<params.length;i++){
		if (params[i].name=='database'){
			if (params[i].value=='' || params[i].value==null){
				return 'Database selection was invalid';
			} 
		}  else if (params[i].name=='ra'){
			if (params[i].value=='' || params[i].value==null){
				return 'Empty ra input';
			} else {
				
				try {
					if (!isNumber(parseFloat ( params[i].value))){
						return 'Invalid ra input';
					} 
				} catch (err){
					return 'Invalid ra input';
				}
			}
		} else if (params[i].name=='dec'){
			if (params[i].value=='' || params[i].value==null){
				return 'Empty dec input';
			} else { 
				
				try {
					if (!isNumber(parseFloat ( params[i].value))){
						return 'Invalid dec input';
					} 
				} catch (err){
					return 'Invalid dec input';
				}
			}
		} else if (params[i].name=='xsize'){
			if (params[i].value=='' || params[i].value==null){
				return 'Empty x-size input';
			} else {
				
				try {
					if (!isNumber(parseFloat ( params[i].value))){
						return 'Invalid x-size input';
					} else if (parseFloat( params[i].value )>15.0 ){
						return "X-Size input was higher than 15 arcmin";
					} 
					
				} catch (err){
					return 'Invalid x-size input';
				}
			}
		} else if (params[i].name=='ysize'){
			if (params[i].value=='' || params[i].value==null){
				return 'Empty y-size input';
			} else {
				
				try {
					if (!isNumber(parseFloat ( params[i].value))){
						return 'Invalid y-size input';
					} else if (parseFloat( params[i].value )>90.0 ){
						return "Y-Size input was higher than 90 arcmin";
					} 
				} catch (err){
					return 'Invalid y-size input';
				}
			}
		} 
	} // loop complete
	

	return 'true';
}



/**
 * On ready, execute
 * 
 */
jQuery(document).ready(function(){
	
	
	  
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
		
		jQuery(content_div +  ' #loadImage').live('click',function(e){ 
			var img_links = gaiSelected;
			for (var i=0;i<img_links.length;i++){
				var imgUrl = jQuery(img_links[i]).attr('href').replace('getImage.cgi','getFImage.cgi');
	            var imgName = "ROE - Get Image ";
				var msg = new samp.Message("image.load.fits",
		                               {"image-id": imgUrl,
					       "url": imgUrl,
					       "name": imgName});
	            connector.connection.notifyAll([msg]);
	        }
		    return false;
		});
		
		jQuery(window).unload( function () { connector.unregister(); } ); 

	
	/**
	 * Bind a click action for the minimizer link, which toggles the visibility of the getImage form
	 */
	jQuery('#content_dbaccess_getImage_form #getImage_minimizer').bind('click',function(){ 
   		toggle_minimize_button('#content_dbaccess_getImage_form #getImage_minimizer','#content_dbaccess_getImage_form .getImage_form', '[+]', '[-]');
   	});
	
	/**
	 * Handle submission of a getImage form
	 * Send an ajax request to a proxy URL, which is responsible for fetching and returning the results of a getImage query
	 * Parse and display results
	 * 	
	 */
	jQuery('form[name=getImage_form]').each(function() {
   	  	jQuery(this).submit(function(){
   	  		jQuery('#getImage_results').html("");
   	  		jQuery('#content_dbaccess_getImage_form #error_tooltip_wrapper').hide();
   	  		jQuery('#content_dbaccess_getImage_form #getImage_error').hide();
   	  		
	  		var form = jQuery(this).serialize();
	  		var params_for_validation =  jQuery(this).serializeArray();
			var validated = validate_getImage(params_for_validation);

			if (validated=='true'){
	  		
			jQuery('#content_dbaccess_getImage_form #getImage_load').fadeIn('normal');
			jQuery('#content_dbaccess_getImage_form #getImage_load').scrollMinimal('#content_dbaccess_getImage_form');
	   		
			var xhr = jQuery.ajax({
	           type: "POST",
	           url: path + "getImageHandler",
	           data: { params : form, action : "getImage"},
	           timeout: 1000000,
	           error: function() {
	             	jQuery('#content_dbaccess_getImage_form #getImage_error').html('There was an error processing your request');	
	      			jQuery('#content_dbaccess_getImage_form #getImage_error').fadeIn('fast');
	             	jQuery('#content_dbaccess_getImage_form #getImage_error').delay(3500).fadeOut('slow'); jQuery('#content_dbaccess_getImage_form #error_tooltip_wrapper').fadeIn('slow');
	             	jQuery('#content_dbaccess_getImage_form #getImage_load').hide();
	             	jQuery('#getImage_results').hide();
            		jQuery('#content_dbaccess_getImage_form #getImage_error').scrollMinimal('content_dbaccess_getImage_form');

	           },
	           success: function(data) {
	        	   if (data == "NOT_LOGGED_IN"){
                   		jQuery('#content_dbaccess_getImage_form #getImage_load').hide();
                       	jQuery('#content_dbaccess_getImage_form #getImage_error').html('Not logged in <br /> Please log in to get access to this form');	
                       	jQuery('#content_dbaccess_getImage_form #getImage_error').fadeIn('fast');
                    	jQuery('#content_dbaccess_getImage_form #getImage_error').delay(3500).fadeOut('slow'); jQuery('#content_dbaccess_getImage_form #error_tooltip_wrapper').fadeIn('slow');                           				                         		
                		jQuery('#content_dbaccess_getImage_form #getImage_error').scrollMinimal('content_dbaccess_getImage_form');

	        	   } else if (data=="" || data==null || data=='None'){
	                 	jQuery('#content_dbaccess_getImage_form #getImage_error').html('There was an error processing your request');	
	          			jQuery('#content_dbaccess_getImage_form #getImage_error').fadeIn('fast');
	                 	jQuery('#content_dbaccess_getImage_form #getImage_error').delay(3500).fadeOut('slow'); jQuery('#content_dbaccess_getImage_form #error_tooltip_wrapper').fadeIn('slow');
	                 	jQuery('#content_dbaccess_getImage_form #getImage_load').hide();
                		jQuery('#content_dbaccess_getImage_form #getImage_error').scrollMinimal('content_dbaccess_getImage_form');

	        	   } else {
	            		jQuery('#content_dbaccess_getImage_form #getImage_minimizer').html('[+]');
	                	jQuery('#content_dbaccess_getImage_form #getImage_minimizer').show();
	                 	jQuery('#content_dbaccess_getImage_form #getImage_load').hide();
	              
	            	   	
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
							if (all!=null){
								rows_tmp = all.getElementsByTagName("tr");
							}
							
							heading = '<thead><tr>';
			            }
		            	
						if (rows_tmp.length>1){
							
						
							heading_elems = div.getElementsByTagName("th");
							if (heading_elems.length<=0){
								
									var first_row = rows_tmp[0].childNodes;
									if (first_row.length>0){
									for  (var y=0;y<first_row.length;y++){
									
										

											if (first_row[y].childNodes.length>0){
												if (first_row[y].childNodes[0].innerHTML=="&nbsp;" || first_row[y].childNodes[0].innerHTML=="&nbsp" ){
													heading+='<th>' + "NONE" + '</th>';
												} else if (first_row[y].childNodes[0].innerHTML!="" && first_row[y].childNodes[0].innerHTML!=null){
													heading+='<th>' + first_row[y].childNodes[0].innerHTML + '</th>';
												}
											}
									
										
									}
								
								}
							}
							
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

											if (td_tmp[y].innerHTML=="&nbsp;" || td_tmp[y].innerHTML=="&nbsp" ){
												rows+='<td>' + "NONE" + '</td>';
											} else if (td_tmp[y].innerHTML!="" && td_tmp[y].innerHTML!=null){
												rows+='<td>' + td_tmp[y].innerHTML + '</td>';
											}
										
									}
									rows +='</tr>';
								}
							}
							
							var getImage_html_content = '\
								<div style="float:left;margin-top:10px;width:300px;"><input class="FontSans" name="selectall" value="&nbsp;Select all&nbsp;" onclick="checkAll();" type="button">&nbsp;&nbsp;<input class="FontSans" name="selectall" value="Unselect all" onclick="uncheckAll();" type="button"></div>'; 
								
							rows +='</tbody>'
							var get_Image_hidden_input = '<input type="hidden" id="get_Image_hidden_input" name="href" value="http://www-wfau.roe.ac.uk/sss/cgi-bin/checkbox.cgi" >'
							var datatable = '<table cellpadding="0" cellspacing="0" border="0" class="datatables" id="getImage_table">' + heading + rows + '</table>' ;
							
							html_table = '<form method id="getImage" name="getImage">'+ datatable + getImage_html_content + get_Image_hidden_input + '</form>';
						 	
		                 	var samp_content = '<div style="text-align:left;float:right;margin-top:10px;"><img id ="samp_connection" src="' + survey_prefix +'/static/images/red-button.png" height="17" width="17" /><button id="register">Connect to SAMP</button><button id="unregister" style="display: none;">Disconnect from SAMP</button>';
		                	samp_content += '<button id="loadImage">Broadcast Selected</button>';
		                	samp_content += '</div><br />';
							jQuery('#getImage_results').html(html_table +samp_content ).fadeIn('slow');
			              
							var pathname = "";
							
							
							
							// Write HTML Table to file
							jQuery.ajax({
						           	type: "POST",
						            url: path + "writeHTMLtoFile",
						            timeout: 1000000,
						            data: {html_table : datatable},
						            error: function() {
						        		jQuery('#content_dbaccess_getImage_form #getImage_load').hide();
						             	jQuery('#content_dbaccess_getImage_form #getImage_error').html('There was an error processing your request');	
						       			jQuery('#content_dbaccess_getImage_form #getImage_error').fadeIn('fast');
						              	jQuery('#content_dbaccess_getImage_form #getImage_error').delay(3500).fadeOut('slow'); jQuery('#content_dbaccess_getImage_form #error_tooltip_wrapper').fadeIn('slow');
						              	jQuery('#getImage_results').hide();
						            },
						            success: function(data) {
										//Add hidden forms for save as options
						            	pathname = data;
						            	var hidden_button_forms = '<div style="display:none"><div id="save_as"><form method="post" action="' + path + 'save_as_vot" name="save_as_vot"> <input type="hidden" value="' + pathname +'" name="pathname"><input type="submit" value="Save as Votable" class="save_button"></form></div><div id="save_as"><form method="post" action="' + path + 'save_as_html" name="save_as_html">';
										hidden_button_forms += '<input type="hidden" value="' + pathname +'" name="pathname"><input type="submit" value="Save as HTML" class="save_button"></form></div><div id="save_as"><form method="post" action="' + path + 'save_as_fits" name="save_as_fits"> <input type="hidden" value="' + pathname +'" name="pathname" id="pathname"><input type="submit" value="Save as Fits" class="save_button"></form></div></div>';
										jQuery('#getImage_results').append(hidden_button_forms);
										
						            }
							});
							
							/* Click event handler */
							$('#getImage tbody tr').live('click', function () {
								var aData = getImageTable.fnGetData( this );
								var iId = aData[0];
								
								if ( jQuery.inArray(iId, gaiSelected) == -1 )
								{
									gaiSelected[gaiSelected.length++] = iId;
								}
								else
								{
									gaiSelected = jQuery.grep(gaiSelected, function(value) {
										return value != iId;
									} );
								}
								
								$(this).toggleClass('row_selected');
							} );
						
							getImageTable = jQuery('#getImage_results #getImage_table').dataTable({
								"bJQueryUI" : true,
                        		"sPaginationType": "full_numbers",
                        		"sDom" : 'T<"clear">R<"H"Clfr>t<"F"ip>',
                        		"oLanguage": {
                        	         "sProcessing": "<img style='position:absolute;left:45px;top:-2px;' src='" + survey_prefix + "/static/static_vo_tool/ajax-loader.gif'>"
                        	       },
                        	 	"bAutoWidth": false,
                        	 	"oColVis": {
                        			"bRestore": true,
                        			"sSize": "css"
                        		},
                        		"sScrollX": "100%",
                        		"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
                        			if ( jQuery.inArray(aData[0], gaiSelected) != -1 )
                        			{
                        				$(nRow).addClass('row_selected');
                        			}
                        			return nRow;
                        		},
                        		"aaSorting": [],
                        		"oTableTools": {
                        			"aButtons": [
                        			      "copy_to_votable_getImage_form","copy_to_fits_getImage_form","copy_to_html_getImage_form", "csv_button", "copy"
                        			 ]
                        		}

		            		});
							
		                 	jQuery('form[name=getImage_form]').slideUp("slow", function () {
		                 		update_size();
		                 		});

						   	jQuery("#content_dbaccess_getImage_form .scroll-wrapper").remove();
	                        jQuery('#content_dbaccess_getImage_form .dataTables_scrollBody').doubleScroll();
	                        current_table = getImageTable;

						} else {
							jQuery('#getImage_results').html("<p style='color:red;'>0 rows returned.</p>").fadeIn('slow');
	                		jQuery('#content_dbaccess_getImage_form #getImage_error').scrollMinimal('content_dbaccess_getImage_form');

						}
	        	   
	        	   }
						
						
		     	}
	   	 	});
	   	 	
		    return false;

			} else {
			 
             	jQuery('#content_dbaccess_getImage_form #getImage_error').html('There was an error processing your request: ' + validated);	
      			jQuery('#content_dbaccess_getImage_form #getImage_error').fadeIn('fast');
             	jQuery('#content_dbaccess_getImage_form #getImage_error').delay(3500).fadeOut('slow'); jQuery('#content_dbaccess_getImage_form #error_tooltip_wrapper').fadeIn('slow');
             	jQuery('#content_dbaccess_getImage_form #getImage_load').hide();
        		jQuery('#content_dbaccess_getImage_form #getImage_error').scrollMinimal('content_dbaccess_getImage_form');

			}
			
		    return false;

	    });
	  
	   });
	  
	 /**
	  * Bind the click action of the #getImage_results links to a function that fetches the results (get images and info) via the proxy URL 
	  * Displays them using facebox 
	  * 
	  */   
	 jQuery('#getImage_results a').live('click', function(e){
  	    jQuery('#content_dbaccess_getImage_form #getImage_load').fadeIn('normal');
		jQuery('#content_dbaccess_getImage_form #getImage_load').scrollMinimal('content_dbaccess_getImage_form');

  	         var href = this.href;
	   	 	 e.preventDefault();
	    	 jQuery.ajax({
	           	type: "POST",
	            url: path + "getImageHandler",
	            timeout: 1000000,
	            data: {href : this.href},
	            error: function() {
	             	jQuery('#content_dbaccess_getImage_form #getImage_error').html('There was an error processing your request');	
	       			jQuery('#content_dbaccess_getImage_form #getImage_error').fadeIn('fast');
	              	jQuery('#content_dbaccess_getImage_form #getImage_error').delay(3500).fadeOut('slow'); jQuery('#content_dbaccess_getImage_form #error_tooltip_wrapper').fadeIn('slow');
	              	jQuery('#content_dbaccess_getImage_form #getImage_load').hide();
	              	jQuery('#getImage_results_secondary').hide();
	            },
	            success: function(data) {
	            	if (data==""){
	            		jQuery('#content_dbaccess_getImage_form #getImage_load').hide();
	                 	jQuery('#content_dbaccess_getImage_form #getImage_error').html('There was an error processing your request');	
	          			jQuery('#content_dbaccess_getImage_form #getImage_error').fadeIn('fast');
	                 	jQuery('#content_dbaccess_getImage_form #getImage_error').delay(3500).fadeOut('slow'); jQuery('#content_dbaccess_getImage_form #error_tooltip_wrapper').fadeIn('slow');
	                 	jQuery('#content_dbaccess_getImage_form #getImage_load').hide();
	            	} else {
	                	jQuery('#content_dbaccess_getImage_form #getImage_load').hide();
	                	jQuery('#getImage_results_secondary').html(data);
			            
	                	jQuery.facebox({ div: '#getImage_results_secondary' });
	                }
		        }
	   	 	});
    	
    	return false;
    });
	 
	 /**
	  * On facebox internal link click get and load content
	  * 
	  */
   	 jQuery('.facebox_links').live('click', function(e){
	    	 	e.preventDefault();
	    	 	var div_id = this.href.substring(this.href.indexOf('#')+1, this.href.length);
			    var url = path + div_id.substring(0,div_id.indexOf('_div'));  
			    var sub_nav =  url.substring(url.indexOf('#')+1, url.length);
			    var div_id_short = div_id;
			    
		    	jQuery.ajax({
	            	type: "GET",
	             	url: url,
	             	timeout: 1000000,
	             	data: {href : this.href},
	             	error: function() {
	              		jQuery('#content_dbaccess_getImage_form #getImage_error').html('There was an error processing your request');	
	        			jQuery('#content_dbaccess_getImage_form #getImage_error').fadeIn('fast');
	            	   	jQuery('#content_dbaccess_getImage_form #getImage_error').delay(3500).fadeOut('slow'); jQuery('#content_dbaccess_getImage_form #error_tooltip_wrapper').fadeIn('slow');
	               		jQuery('#content_dbaccess_getImage_form #getImage_load').hide();
                		jQuery('#content_dbaccess_getImage_form #getImage_error').scrollMinimal('content_dbaccess_getImage_form');

	             	},
	             	success: function(data) {
	             		if (data==""){
			       		    jQuery('#content_dbaccess_getImage_form #getImage_error').html('There was an error processing your request');	
	           				jQuery('#content_dbaccess_getImage_form #getImage_error').fadeIn('fast');
	                  		jQuery('#content_dbaccess_getImage_form #getImage_error').delay(3500).fadeOut('slow'); jQuery('#content_dbaccess_getImage_form #error_tooltip_wrapper').fadeIn('slow');
	             		} else {
	             			jQuery('#getImage_facebox_content').html(data);
	             			if (jQuery('#getImage_facebox_content').innerHTML!=""){
		             			jQuery.facebox ({ div: '#getImage_facebox_content' });
				           	    init_scrolling_mechanism('getImage_help');
		             		}

	             		}
	             	}
		    	});
   	 		});
   	 
   	jQuery("#content_dbaccess_getImage_form #error_tooltip_link").live ('click',function() {
 		jQuery('#content_dbaccess_getImage_form #getImage_error').slideToggle();
 		return false;

 	});
});   