/*
 * WFAU Web2.0 Project
 * Author: Stelios Voutsinas
 * Date: July 2012
 * 
 * Javascript Functions and Variables used for the multiGetImage Page, part of the OSA Interface
 * 
 * 
 */

	
	
	var this_div_name = 'dbaccess_region_form';
	init_scrolling_mechanism(this_div_name);
	jQuery('a[rel*=facebox]').facebox(); 	// Initialize facebox links
	var pathname_divId = '#content_dbaccess_region_form #temp_file';
	var content_div = '#content_' + this_div_name;
	jQuery('#content_dbaccess_region_form #region_results').html("");
  	var RegionTable;

	/* Web SAMP variables */
	var cc;
	var callHandler;
	var connector;
	var tableUrl;
	var tableId;
	var _broadcast_url;
	   
	
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
				fnSaveAsCustom('csv', RegionTable , pathname_divId);
			},
			"fnSelect": null,
			"fnComplete": null,
			"fnInit": null,
			"fnAjaxComplete": function( json ) {
			}
		};
	
	
	
	TableTools.BUTTONS.copy_to_fits_region_form = {
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
				fnSaveAsCustom('fits', RegionTable , pathname_divId);
			},
			"fnSelect": null,
			"fnComplete": null,
			"fnInit": null,
			"fnAjaxComplete": function( json ) {
			}
			
		};
	
		 
		TableTools.BUTTONS.copy_to_votable_region_form = {
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
				fnSaveAsCustom('vo', RegionTable , pathname_divId);
			},
			"fnSelect": null,
			"fnComplete": null,
			"fnInit": null,
			"fnAjaxComplete": function( json ) {
			}
		};
		
		 
		TableTools.BUTTONS.copy_to_html_region_form = {
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
				fnSaveAsCustom('html', RegionTable , pathname_divId);
			},
			"fnSelect": null,
			"fnComplete": null,
			"fnInit": null,
			"fnAjaxComplete": function( json ) {
			}
		};

		/**
		 * Check if given string is a parsable float
		 * 
		 * @param n
		 * @returns {Boolean}
		 */	
		function isNumber(n) {
			  return !isNaN(parseFloat(n)) && isFinite(n);
		}

		
		/* End Custom TableTools buttons */
		
		/**
		 * Validate the parameters taken from a getImage form submission
		 * 
		 * @param params
		 * @returns {String}
		 */
		function validate_region(params){
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
							console.log(err);
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
				} else if (params[i].name=='galactic_lat'){
					if (params[i].value=='' || params[i].value==null){
						return 'Empty galactic latitude input';
					}
				} else if (params[i].name=='galactic_long'){
					if (params[i].value=='' || params[i].value==null){
						return 'Empty galactic long input';
					} 
				} else if (params[i].name=='from_table'){
					if (params[i].value=='' || params[i].value==null){
						return 'Invalid table input';
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

		
		
	
	    /* Web Samp functionality */
	    
	    cc = new samp.ClientTracker();
	    callHandler = cc.callHandler;
		var meta = {
		    "samp.name": "OSA Results",
	        "samp.description": "OSA Interface table results",
		    "samp.icon.url": "http://surveys.roe.ac.uk/osa/common/img/wfau_browser.gif"
	        };
		
		callHandler["table.highlight.row"] = function(senderId, message, isCall) {
	    	var params = message["samp.params"];
		    if (params['table-id']!=tableId) {
				return;
		    }
		
		    findRow(params["row"]).css('background-color', 'red');
	    };
	    
	    callHandler["table.select.rowList"] = function(senderId, message, isCall) {
	        var params = message["samp.params"];
		    if (params['table-id']!=tableId) {
				return;
		    }
		    jQuery('#votable tr').removeClass("highlight");
		    for (var idx in params["row-list"]) {
		        findRow(params["row-list"][idx]).css('background-color', 'red');
		    }
	    };
	 	
	    var subs = cc.calculateSubscriptions();
	    connector = new samp.Connector("OSA Results", meta, cc, subs);
	
		setInterval("updateStatus()", 1000);
	
		jQuery(content_div + ' #register').live('click',function(e){ 
		    connector.register();
		    updateStatus();
	
		    return false;
		});
		
		jQuery(content_div +  ' #unregister').live('click',function(e){ 
		    connector.unregister();
		    updateStatus();
	
		    return false;
		});
	
	
		jQuery(content_div + ' #loadvotable').live('click',function(e){ 
			var temp_stored_url = fnSaveAsGetURL('vo', oRegionTable, pathname_divId, base_url);
			var xhr = jQuery.ajax({
    	       type: "POST",
    	       data: {_broadcast_url : temp_stored_url, _action : "save_as_temp_file"},
    	       url : base_url +  '/data_tables_processing',
    	       timeout: 1000000,
    	       error: function() {
    	       	alert('There was an error processing your request');
    	       },
    	       success: function(data) {
    	    	   if (data!=null){
    	    		   temp_stored_url = data;
    	    	   }
    	    		
    	    	   var msg = new samp.Message("table.load.votable",
 			                        {"table-id": "OSA Query Data",
 						"url": temp_stored_url,
 						"name": "OSA Query Data"});
 	             
    	    	   connector.connection.notifyAll([msg]);
    	    	   _broadcast_url = fnSaveAsGetURL('vo', oRegionTable, pathname_divId, base_url);
     			  
	    	   }
		 	}); 
			
			return false;
	
		});

	
		/**
		 * Send a Samp Message to highlight a row by its index (trNumber)
		 *
		 */
		function SAMPRowClickHandler(trNumber){
			
			if (!!!connector.connection) {
			    return;
			}
			
			jQuery(this).addClass('highlight');
			rowIdx = trNumber;
			if ( isNaN(rowIdx) ) {
			    return;
			}
		
			var x_expr = "";
			var y_expr = "";
			  
				var msg = new samp.Message("table.highlight.row",
	                   {"table-id": "OSA Query Data",
					       "url": fnSaveAsGetURLforViewer('vo', RegionTable, pathname_divId, base_url, x_expr, y_expr),
					       "row": ""+rowIdx});
	             connector.connection.notifyAll([msg]);
	            
	             
		}
		
		jQuery(content_div + ' #votable tr').live('click',function(e){
			jQuery(content_div + ' #votable tr').css('background-color', '');
			jQuery(this).css('background-color', '#B0BED9');
			var nTr = jQuery(this).closest("tr").get(0);
			var rowIndex = RegionTable.fnGetPosition( nTr);
			var aData = RegionTable.fnGetData( rowIndex )
			var pathname = jQuery(pathname_divId).val();  
			var params = [];
			var sort_col = "";
			var sData = RegionTable.fnSettings();
			var params2 = RegionTable.oApi._fnAjaxParameters(sData);
			var sortOrder = "asc";
			
			if (RegionTable.dataTableSettings[0].aaSorting.length>0){
	    		if (RegionTable.dataTableSettings[0].aaSorting[0].length>0){
	    			sortOrder = RegionTable.dataTableSettings[0].aaSorting[0][1];
	    		}
			}
			
			for (var i = 0; i<params2.length;i++){
				if (params2[i].name == 'iSortCol_0'){
					_get_sort_col = params2[i].value;
					params.push({'name' : '_get_sort_col', 'value' : _get_sort_col});
					params.push({'name' : '_sSortDir_0', 'value' : sortOrder}); 
	        		break;
				}  else if (params2[i].name == 'sSearch'){
					params.push({'name' : '_sSearch', 'value' : params2[i].value}); 
					
				} else if (params2[i].name.indexOf("mDataProp_") == 0){
					params.push({'name' : params2[i].name, 'value' : params2[i].value}); 
					
				}
			}
		
			params.push({'name' : '_broadcast_url', 'value' : _broadcast_url});
		 	params.push({'name' : 'getRowIndexes', 'value' : 'true'}); 
		 	params.push({'name' : 'pathname', 'value' : pathname});
		 	
		 	var counter = 0;
			if (sData.sAjaxSource=="" || sData.sAjaxSource==null){
				for (i in aData){
					params.push({'name' : "col_" + sData.aoColumns[counter].sTitle, 'value' : aData[counter]});
					counter++;
				}
				
			} else {
				for (i in aData){
					params.push({'name' : "col_" + i, 'value' : aData[i]});
				}
				
			}
			
			//if (_broadcast_url!=null && _broadcast_url!=""){
	
			    
			 	var xhr = jQuery.ajax({
			       type: "POST",
			       url : base_url + '/data_tables_processing',
			       data : params,
			       timeout: 1000000,
			       error: function() {
			       	jQuery('#load').hide();	
			       	alert('There was an error processing your request');
			       	return [];
			       },
			       success: function(data) {
			     	  var index_val = jQuery.parseJSON(data);
			     	  SAMPRowClickHandler(index_val);
			       }
			 	}); 
			//}
	           
	             
		});
		
	   jQuery(window).unload( function () { connector.unregister(); } ); 
	
	   /* End Web Samp functionality */
	   
	   
	   /**
	    * Main region form submission handling
	    * Fetch input, send request to appropriate URL that will handle the region query and return the results
	    * Fetch data, parse, generate result HTML table and apply dataTables library
	    * 
	    */
	   jQuery('form[name=region_form]').each(function() {
	        jQuery(this).submit(function(){
        		jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').hide();
        		jQuery('#content_dbaccess_region_form #region_error').hide();

	        	jQuery('#content_dbaccess_region_form #region_results').html("");
	        	var programmeID = jQuery(".region_form #programmeID option:selected").val();
	 	       	var form = jQuery(this).serialize();
		  		var params_for_validation =  jQuery(this).serializeArray();

	 	        jQuery('.region_form input[disabled]').each( function() {
	 	             form = form + '&' + jQuery(this).attr('name') + '=""';
	 	          });
	 	     
	 	        var validated = validate_region(params_for_validation);
	 	        
			    if (validated=='true'){
					
				
	 	        jQuery('#content_dbaccess_region_form #region_load').fadeIn('normal');
				jQuery('#content_dbaccess_region_form #region_load').scrollMinimal('#content_dbaccess_region_form');

	        	var xhr = jQuery.ajax({
	                type: "POST",
	                url: path + "dbaccess_region_form",
	                data: form,
	                timeout: 1000000,
	                error: function() {
	                	alert('There was an error processing your request');
	                	jQuery('#content_dbaccess_region_form #region_load').hide();
	                },
	                success: function(data) {
	                	jQuery('#content_dbaccess_region_form #region_load').hide();
	                	 if (data == "NOT_LOGGED_IN"){
	                     	jQuery('#content_dbaccess_region_form  #load').hide();
	                     	jQuery('#content_dbaccess_region_form #region_error').html('Not logged in <br /> Please log in to get access to this form');	
	                   		jQuery('#content_dbaccess_region_form #region_error').fadeIn('fast');
	                      	jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');     
		            		jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

	                 	} else if (data.indexOf("<div class='error'>")>=0){
	                     	jQuery('#content_dbaccess_region_form  #load').hide();
	                    	jQuery('#content_dbaccess_region_form #region_error').html(data).slideDown('slow');
                         	jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');  
		            		jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

	                	} else {
	                    	jQuery('form[name=region_form]').slideUp("slow", function () {
		                 		update_size();
	                 		});
	                    	
	                    	jQuery('#content_dbaccess_region_form #region_minimizer').html('[+]');
	                    	jQuery('#content_dbaccess_region_form #region_minimizer').show();
	                   		jQuery('#content_dbaccess_region_form #region_results').hide().html('');	
	                       
	                   		try {
	                        	var result_array = [];
	                        	result_array = jQuery.parseJSON(data);
	                        	var row_length = parseInt(result_array[1]);
	                        } catch (ex) { 
	                     		result_array = "error";
	                        } 
	                        
	                        if (result_array=="error"){
	                        	jQuery('#content_dbaccess_region_form #region_error').html("Query returned invalid data.. Please try another query");
	        			 		jQuery('#content_dbaccess_region_form #region_error').fadeIn('normal');
	        					jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');  
			            		jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

	                		} else if (result_array.length<1 || row_length <= 0 || row_length == null){
	                  			jQuery('#content_dbaccess_region_form #region_error').html('Your query returned 0 results');	
	                  			jQuery('#content_dbaccess_region_form #region_error').fadeIn('fast');
	                         	jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');  
	                         	jQuery('#content_dbaccess_region_form #region_load').hide();
			            		jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

	                		} else if (result_array != "error"){
	                      		jQuery('#content_dbaccess_region_form #region_results').html(result_array[2][0]);
	                      		var pathname = jQuery('#content_dbaccess_region_form #temp_file').val();
	        					var data_source = "";
								jQuery('#content_dbaccess_region_form #region_results').append('<table cellpadding="0" cellspacing="0" border="0" class="datatables" id="region_table"></table>' ).fadeIn(1500);                            	
	                      		var col = [];
	                      		var error_bool = false;
	                      	  	
	                      		for (i=0;i<result_array[0].length;i++) {
	                      			col.push({"sTitle":  result_array[0][i], "mDataProp" : result_array[0][i]});
	                            }
	                      	
	                      	
	        					RegionTable = jQuery('#content_dbaccess_region_form #region_table').dataTable( {
		                        		"bJQueryUI" : true,
		                        		"sPaginationType": "full_numbers",
		                        		"sDom" : 'T<"clear">R<"H"Clfr>t<"F"ip>',
		                        		"oLanguage": {
		                        	         "sProcessing": "<img style='position:absolute;left:45px;top:-2px;' src='" + survey_prefix + "/static/static_vo_tool/ajax-loader.gif'>"
		                        	       },
		                        	    "bProcessing" : true,
		                        		"bServerSide" : true,
		                        		"aoColumns" : col,			
		                        		"oColVis": {
		                        			"bRestore": true,
		                        			"sSize": "css"
		                        		},
		                        		"bAutoWidth": false,
		                        		"aaSorting": [],
		                        		"sScrollX": "100%",
		                        		"oTableTools": {
		                        			"aButtons": [
		                        			      "copy_to_votable_region_form","copy_to_fits_region_form","copy_to_html_region_form", "csv_button", "copy"
		                        			 ]
		                        		},
		                        	
		                        		"sAjaxSource" : path + 'data_tables_processing',
										"fnServerData" : function ( sSource, aaData, fnCallback ) {
															 aaData.push({'name' : 'pathname', 'value' : pathname});
															  jQuery.ajax({
												           		 "dataType": 'json',
												                 "type": "POST",
												                 "url": sSource,
												                 "data": aaData, 
											                     timeout: 1000000,
											                     "error": function(data){
											 		          		if (!error_bool){ 
											 		          			error_bool = true;
											 		          			console.log(data);
												                        jQuery('#content_dbaccess_region_form #region_load').hide();
															            jQuery('#content_dbaccess_region_form #region_error').html("Query returned invalid data.. Please try another query");
						   					        					jQuery('#content_dbaccess_region_form #region_error').fadeIn('normal');
						   					        					jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');  
						   					        					jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

											 		          		}		
											 		          	},
												                 "success": function(data){
												                	
	 														          	 if (data==null){
	 														          		if (!error_bool){ 
													                		 	error_bool = true;
														                        jQuery('#content_dbaccess_region_form #region_load').hide();
	  	 														          		jQuery('#content_dbaccess_region_form #region_error').html("There was an error processing your request.. Please try another query");
								   					        					jQuery('#content_dbaccess_region_form #region_error').fadeIn('normal');
								   					        					jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');     
								   					        					jQuery('#content_dbaccess_region_form #region_results').hide();
								   					        					jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

	 														          		}
	 														          	} else {
														                    error_bool = false;
													                        fnCallback(data);
													                        jQuery('#content_dbaccess_region_form #region_results').slideDown('slow');
													                    	jQuery("#content_dbaccess_region_form .scroll-wrapper").remove();
													                        jQuery('#content_dbaccess_region_form .dataTables_scrollBody').doubleScroll();
	
													             																	                	 
														           	}
												                 }
												              });  //end ajax call
															 
											
										} //end fnServerData function
		                        		
		                            }); //end RegionTable	
	        					RegionTable.fnAdjustColumnSizing(true); 
	        				  	current_Table = RegionTable;

	        					jQuery('#content_dbaccess_region_form #toggle_query_info').bind('click',function(){ 
	        		       			toggle_minimize_button('#content_dbaccess_region_form #toggle_query_info','#content_dbaccess_region_form #toggle_query_info_div', '[+]', '[-]');
	        			      	});
	                  		
	                  		} // result array not equal to error
	                     
	                  		                      
	                            
	                	 
	                	} //results does not contain error div
		                                  	
	                } // end success   
	                
				});
	          
	        	return false;
			   
			   }  else {
					 
	             	jQuery('#content_dbaccess_region_form #region_error').html('There was an error processing your request: ' + validated);	
	      			jQuery('#content_dbaccess_region_form #region_error').fadeIn('fast');
	             	jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');  
	             	jQuery('#content_dbaccess_region_form #region_load').hide();
   					jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

				}
				
			    return false;
			   
			   
			   
	        });
		});
	
	    /**
	     * Bind minimizer element click to function that will toggle the visibility of the region form
	     * 
	     */
		jQuery('#content_dbaccess_region_form #region_minimizer').bind('click',function(){ 
	   		toggle_minimize_button('#content_dbaccess_region_form #region_minimizer','#content_dbaccess_region_form .region_form', '[+]', '[-]');
	   	});
		
	
		/**
		 * Bind changes to the .sys element to a function that will toggle visibility of other inputs in accord to it's value
		 * 
		 */
	    jQuery('#content_dbaccess_region_form .sys').bind('change',function(){ 
			if (jQuery(this).val()== "J"  || jQuery(this).val()== "B"){
				jQuery("#content_dbaccess_region_form .galactic_long").attr("disabled", "disabled");
				jQuery("#content_dbaccess_region_form .galactic_lat").attr("disabled", "disabled");
				jQuery("#content_dbaccess_region_form .ra").attr("disabled", "");
				jQuery("#content_dbaccess_region_form .dec").attr("disabled", "");	
			} else {
				jQuery("#content_dbaccess_region_form .ra").attr("disabled", "disabled");
				jQuery("#content_dbaccess_region_form .dec").attr("disabled", "disabled");
				jQuery("#content_dbaccess_region_form .galactic_long").attr("disabled", "");
				jQuery("#content_dbaccess_region_form .galactic_lat").attr("disabled", "");
			}
				
	    });  
	    
	    /**
	     * Bind click of region_table links, to a function that uses ajax and a proxy URL to fetch and display the content of the page that the anchor links to
	     * 
	     */
	    jQuery('#region_table a').live('click', function(e){
	  	    jQuery('#content_dbaccess_region_form #region_load').fadeIn('normal');
			jQuery('#content_dbaccess_region_form #region_load').scrollMinimal('#content_dbaccess_region_form');

	   	 	e.preventDefault();
	    	jQuery.ajax({
	           	type: "POST",
	            url: path + "getImageHandler",
	            timeout: 1000000,
	            data: {href : this.href,  action : 'osa_request'},
	            error: function() {
	             	jQuery('#content_dbaccess_region_form #region_error').html('There was an error processing your request');	
	      			jQuery('#content_dbaccess_region_form #region_error').fadeIn('fast');
	             	jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');  
	             	jQuery('#content_dbaccess_region_form #region_load').hide();
	             	jQuery('#region_getImage_results').hide();
   					jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

	            },
	            success: function(data) {
	            	if (data==""){
	            		jQuery('#content_dbaccess_region_form #region_load').hide();
	                 	jQuery('#content_dbaccess_region_form #region_error').html('There was an error processing your request');	
	          			jQuery('#content_dbaccess_region_form #region_error').fadeIn('fast');
	                 	jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');  
	                	jQuery('#region_getImage_results').hide();
       					jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

	            	} else {
	                 	jQuery('#content_dbaccess_region_form #region_load').hide();
	           	    	jQuery('#region_getImage_results').html(data).fadeIn('slow');
						jQuery('#content_dbaccess_region_form #region_getImage_results').scrollMinimal('#content_dbaccess_region_form');

	                }
		        }
	   	 	});
	   	 	
	    	
	    	
	    	return false;
	    });
	    
	    
		 /**
		  * Bind a click to region getImage results that uses ajax and a proxy URL to display the images with a [#facebox] look
		  * 
		  */   
		 jQuery('#region_getImage_results a').live('click', function(e){
		    jQuery('#content_dbaccess_region_form #region_load').fadeIn('normal');
			jQuery('#content_dbaccess_region_form #region_load').scrollMinimal('#content_dbaccess_region_form');

		 	e.preventDefault();
			jQuery.ajax({
		       	type: "POST",
		        url: path + "getImageHandler",
		        timeout: 1000000,
		        data: {href : this.href, action : 'osa_request'},
		        error: function() {
		         	jQuery('#content_dbaccess_region_form #region_load').hide();
		         	jQuery('#content_dbaccess_region_form #region_error').html('There was an error processing your request');	
		   			jQuery('#content_dbaccess_region_form #region_error').fadeIn('fast');
		          	jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');  
		        	jQuery('#region_getImage_results_secondary').hide();
   					jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

		        },
		        success: function(data) {
		        	if (data==""){
		        		jQuery('#content_dbaccess_region_form #region_load').hide();
		             	jQuery('#content_dbaccess_region_form #region_error').html('There was an error processing your request');	
		      			jQuery('#content_dbaccess_region_form #region_error').fadeIn('fast');
		             	jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');  
		              	jQuery('#region_getImage_results_secondary').hide();
       					jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

		        	} else {
		             	jQuery('#content_dbaccess_region_form #region_load').hide();
		       	    	jQuery('#region_getImage_results_secondary').html(data);
		             	jQuery.facebox({ div: '#region_getImage_results_secondary' });
						jQuery('#content_dbaccess_region_form #region_getImage_results_secondary').scrollMinimal('#content_dbaccess_region_form');

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
		              		jQuery('#content_dbaccess_region_form #region_error').html('There was an error processing your request');	
		        			jQuery('#content_dbaccess_region_form #region_error').fadeIn('fast');
		            	   	jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');  
		               		jQuery('#content_dbaccess_region_form #region_load').hide();
		               		jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

		             	},
		             	success: function(data) {
		             		if (data==""){
				       		    jQuery('#content_dbaccess_region_form #region_error').html('There was an error processing your request');	
		           				jQuery('#content_dbaccess_region_form #region_error').fadeIn('fast');
		                  		jQuery('#content_dbaccess_region_form #region_error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_region_form #error_tooltip_wrapper').fadeIn('fast');  
		                  		jQuery('#content_dbaccess_region_form #region_error').scrollMinimal('content_dbaccess_region_form');

		             		} else {
		             			jQuery('#region_facebox_content').html(data);
			           	     	jQuery.facebox({ div: '#region_facebox_content' });
				           	    init_scrolling_mechanism('getRegion_help');

		             		}
		             	}
			    	});
	   	 		});
	   	 
	 	/**
	 	 * Handle a change in the baseTable div, by adding content or removing from the where Clause input
	 	 * 
	 	 */
	 	jQuery('#content_dbaccess_region_form #baseTable').change(function() {
	 		if (jQuery('#content_dbaccess_region_form #baseTable').val()==source_table){
	 			jQuery('#content_dbaccess_region_form #whereClause').val('(priOrSec<=0 OR priOrSec=frameSetID)');
	 		} else {
	 			jQuery('#content_dbaccess_region_form #whereClause').val('');

	 		}
	 	});
	 	
	 	jQuery("#content_dbaccess_region_form #error_tooltip_link").live ('click',function() {
	 		jQuery('#content_dbaccess_region_form #region_error').slideToggle();
	 		return false;

	 	});
	 	
		});   
