/*
 * WFAU Web2.0 Project
 * Author: Stelios Voutsinas
 * Date: July 2012
 * 
 * Javascript Functions and Variables used for the ImageList page, part of the OSA Interface
 * 
 * 
 */


	var this_div_name = 'dbaccess_ImageList_form';
	init_scrolling_mechanism(this_div_name);	//Initialise scrolling mechanism
	var ImageListTable;						//dataTables object placeholder
	var pathname_divId = '#content_dbaccess_ImageList_form #temp_file';
	var content_div = '#content_' + this_div_name;
	
	//Web SAMP elements
  	var cc;
  	var callHandler;
  	var connector;
  	var tableUrl;
  	var tableId;
	var _broadcast_url;

  	/* Custom Tabletools buttons */
  	
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
				fnSaveAsCustom('csv', ImageListTable, pathname_divId);
			},
			"fnSelect": null,
			"fnComplete": null,
			"fnInit": null,
			"fnAjaxComplete": function( json ) {
			}
		};
		
		
	TableTools.BUTTONS.copy_to_fits_ImageList_form = {
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
				fnSaveAsCustom('fits', ImageListTable, pathname_divId);
			},
			"fnSelect": null,
			"fnComplete": null,
			"fnInit": null,
			"fnAjaxComplete": function( json ) {
			}
		};
		
		 
		TableTools.BUTTONS.copy_to_votable_ImageList_form = {
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
					fnSaveAsCustom('vo', ImageListTable, pathname_divId);
				},
				"fnSelect": null,
				"fnComplete": null,
				"fnInit": null,
				"fnAjaxComplete": function( json ) {
				}
		};
		
		 
		TableTools.BUTTONS.copy_to_html_ImageList_form = {
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
					fnSaveAsCustom('html', ImageListTable, pathname_divId);
				},
				"fnSelect": null,
				"fnComplete": null,
				"fnInit": null,
				"fnAjaxComplete": function( json ) {
				}
		};
		
	/* End custom TableTools buttons */
		
	/**
	 * Check if a string ends with a given suffix
	 * 
	 * @param str, suffix
	 * @returns {Boolean}
	 */	
	function endsWith(str, suffix) {
	    return str.indexOf(suffix, str.length - suffix.length) !== -1;
	}

	/**
	 * Check if a string n can be parsed as a number ( float )
	 * 
	 * @param n
	 * @returns {Boolean}
	 */
	function isNumber(n) {
		  return !isNaN(parseFloat(n)) && isFinite(n);
	}

	/**
	 * Validate the parameters list passed through the ImageList form
	 * 
	 * @param params
	 * @returns {String}
	 */
	function validate_ImageList (params){
		
		for (var i = 0;i<params.length;i++){
			if (params[i].name=='database'){
				if (params[i].value=='' || params[i].value==null){
					return 'Database selection was invalid';
				} 
			} else if (params[i].name=='minRA'){
				if (params[i].value=='' || params[i].value==null){
					return 'Empty minimum RA input';
				} else {
					try {
						if (!isNumber(parseFloat ( params[i].value))){
							return 'Invalid minimum RA input';
						} 
					} catch (err){
						return 'Invalid minimum RA input';
					}
				}
			} else if (params[i].name=='maxRA'){
				if (params[i].value=='' || params[i].value==null){
					return 'Empty maximum RA input';
				} else {
					try {
						if (!isNumber(parseFloat ( params[i].value))){
							return 'Invalid maximum RA input';
						} 
					} catch (err){
						return 'Invalid maximum RA input';
					}
				}
			} else if (params[i].name=='maxDec'){
				if (params[i].value=='' || params[i].value==null){
					return 'Empty maximum Dec input';
				} else {
					try {
						if (!isNumber(parseFloat ( params[i].value))){
							return 'Invalid maximum Dec input';
						} 
					} catch (err){
						return 'Invalid maximum Dec input';
					}
				}
			} else if (params[i].name=='minDec'){
				if (params[i].value=='' || params[i].value==null){
					return 'Empty minimum Dec input';
				} else {
					try {
						if (!isNumber(parseFloat ( params[i].value))){
							return 'Invalid minimum Dec input';
						} 
					} catch (err){
						return 'Invalid minimum Dec input';
					}
				}
			} 
			
		} // loop complete
		

		return 'true';
	}
	

	
	/**
	 * On ready execute
	 * 
	 */
	jQuery(document).ready(function(){

		

        /* Samp Functionality */
      
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
		    jQuery(content_div + ' #votable tr').removeClass("highlight");
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

   		    // on ne soumet pas le FORMulaire
   		    return false;
   		});
   		
   		jQuery(content_div + ' #unregister').live('click',function(e){ 
   		    connector.unregister();
   		    updateStatus();

   		    return false;
   		});


   		jQuery(content_div + ' #loadvotable').live('click',function(e){ 
				var temp_stored_url = fnSaveAsGetURL('vo', ImageListTable, pathname_divId, base_url);
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
	    	    	   _broadcast_url = fnSaveAsGetURL('vo', ImageListTable, pathname_divId, base_url);
	     			  
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
					       "url": fnSaveAsGetURLforViewer('vo', ImageListTable, pathname_divId, base_url, x_expr, y_expr),
					       "row": ""+rowIdx});
	                connector.connection.notifyAll([msg]);
	               
	                
		}
		
		jQuery(content_div + ' #votable tr').live('click',function(e){
			
			jQuery(content_div + ' #votable tr').css('background-color', '');
			jQuery(this).css('background-color', '#B0BED9');
			var nTr = jQuery(this).closest("tr").get(0);
    		var rowIndex = ImageListTable.fnGetPosition( nTr);
    		var aData = ImageListTable.fnGetData( rowIndex )
    		var pathname = jQuery(pathname_divId).val();  
    		var params = [];
    		var sort_col = "";
    		var sData = ImageListTable.fnSettings();
    		var params2 = ImageListTable.oApi._fnAjaxParameters(sData);
    		var sortOrder = "asc";
    		
    		if ( ImageListTable.dataTableSettings[0].aaSorting.length>0){
        		if ( ImageListTable.dataTableSettings[0].aaSorting[0].length>0){
        			sortOrder = ImageListTable.dataTableSettings[0].aaSorting[0][1];
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
    	
    		params.push({'name' : 'getRowIndexes', 'value' : 'true'}); 
    	 	params.push({'name' : 'pathname', 'value' : pathname});
			params.push({'name' : '_broadcast_url', 'value' : _broadcast_url});
			
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
			
    	    
    	 	var xhr = jQuery.ajax({
    	       type: "POST",
    	       data: params,
    	       url : base_url + '/data_tables_processing',
    	       timeout: 1000000,
    	       error: function() {
    	       	alert('There was an error processing your request');
    	       },
    	       success: function(data) {
    	     	  var index_val = jQuery.parseJSON(data);
    	     	  SAMPRowClickHandler(index_val);
    	       }
    	 	}); 
    	
	              
	                
		});
		
		
       jQuery(window).unload( function () { connector.unregister(); } ); 

       /* End SAMP functionality */
       
       /**
        * On click minimize ImageList form
        */
		jQuery('#content_dbaccess_ImageList_form #ImageList_minimizer').bind('click',function(){ 
	   		toggle_minimize_button('#content_dbaccess_ImageList_form #ImageList_minimizer','#content_dbaccess_ImageList_form .ImageList_form', '[+]', '[-]');
	   	});
		
		/**
		 * On ImageList form submission, execute, parse result and load html content
		 * 
		 */
		jQuery('form[name=ImageList_form]').each(function() {
	   	  	jQuery(this).submit(function(){
		  		var form = jQuery(this).serialize();
				var params_for_validation =  jQuery(this).serializeArray();
				var validated = validate_ImageList(params_for_validation);
				
				if (validated=='true'){
					
			  		jQuery('#content_dbaccess_ImageList_form #ImageList_load').fadeIn('normal');
			  		jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').hide();
			  		jQuery('#content_dbaccess_ImageList_form #ImageList_error').hide();
			   		var xhr = jQuery.ajax({
			           type: "POST",
			           url:  path + "dbaccess_ImageList_form",
			           data: form,
			           timeout: 1000000,
			           error: function() {
			             	jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
			             	jQuery('#content_dbaccess_ImageList_form #ImageList_error').html('There was an error processing your request');	
			      			jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('fast');
			             	jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');
			             	jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
	                		jQuery('#content_dbaccess_ImageList_form #ImageList_error').scrollMinimal('content_dbaccess_ImageList_form');

			           },
			           success: function(data) {
		            		jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
		            		 if (data == "NOT_LOGGED_IN"){
	                    		jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
	                        	jQuery('#content_dbaccess_ImageList_form #ImageList_error').html('Not logged in <br /> Please log in to get access to this form');	
	                        	jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('fast');
	                        	jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');                           				                         		
		                		jQuery('#content_dbaccess_ImageList_form #ImageList_error').scrollMinimal('content_dbaccess_ImageList_form');

		            		 } else if (data=="" || data==null || data=='None'){
			                 	jQuery('#content_dbaccess_ImageList_form #ImageList_error').html('There was an error processing your request');	
			          			jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('fast');
			          			jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
			                 	jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');
		                		jQuery('#content_dbaccess_ImageList_form #ImageList_error').scrollMinimal('content_dbaccess_ImageList_form');

		            		 } else if (data.indexOf("<div class='error'>")>=0){
			                    jQuery('#content_dbaccess_ImageList_form #ImageList_results').html(data).slideDown('slow');
	                        	jQuery('#content_dbaccess_ImageList_form #ImageList_results').delay(3500).fadeOut('slow');                           				                         		

			            	} else {
			            	   	jQuery('form[name=ImageList_form]').slideUp("slow", function () {
			            	   		update_size();
		                 		});
			            		jQuery('#content_dbaccess_ImageList_form #ImageList_minimizer').html('[+]');
			                	jQuery('#content_dbaccess_ImageList_form #ImageList_minimizer').show();
			                 	jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide().html('');
			                       
		                   		try {
	                            	var result_array = [];
	                            	result_array = jQuery.parseJSON(data);
	                            	var row_length = parseInt(result_array[1]);
		                        } catch (ex) { 
		                     		result_array = "error";
		                        } 
			                        
		                        if (result_array=="error"){
		                        	jQuery('#content_dbaccess_ImageList_form #ImageList_error').html("Query returned invalid data.. Please try another query");
		        			 		jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('normal');
		        					jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');
		        					jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
		        					jQuery('#content_dbaccess_ImageList_form #ImageList_results').hide();
			                		jQuery('#content_dbaccess_ImageList_form #ImageList_error').scrollMinimal('content_dbaccess_ImageList_form');

	                    		} else if (result_array.length<1 || row_length <= 0 || row_length == null){
	                       			jQuery('#content_dbaccess_ImageList_form #ImageList_error').html('Your query returned 0 results');	
	                       			jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('fast');
	                             	jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');
	                             	jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
	    	                		jQuery('#content_dbaccess_ImageList_form #ImageList_error').scrollMinimal('content_dbaccess_ImageList_form');

	                    		} else if (result_array != "error"){
	                          		jQuery('#content_dbaccess_ImageList_form #ImageList_results').html(result_array[2][0]);
	                          		var pathname = jQuery('#content_dbaccess_ImageList_form #temp_file').val();
	            					var data_source = "";
									jQuery('#content_dbaccess_ImageList_form #ImageList_results').append('<table cellpadding="0" cellspacing="0" border="0" class="datatables" id="ImageList_table"></table>' ).fadeIn(1500);                            	
	                          		var col = [];
	                          		var error_bool = false;
	                          	  	
	                          		for (i=0;i<result_array[0].length;i++) {
	                          			col.push({"sTitle":  result_array[0][i], "mDataProp" : result_array[0][i]});
		                            }
	                          	
		                          	
	            					 ImageListTable = jQuery('#content_dbaccess_ImageList_form #ImageList_table').dataTable( {
			                        		"bJQueryUI" : true,
			                        		"sPaginationType": "full_numbers",
			                        		"sDom" : 'T<"clear">R<"H"Clfr>t<"F"ip>',
			                        		"oLanguage": {
			                        	         "sProcessing": "<img style='position:absolute;left:45px;top:-2px;' src='" + survey_prefix + "/static/static_vo_tool/ajax-loader.gif'>"
			                        	       },
			                        	    "bProcessing" : true,
			                        		"bServerSide" : true,
			                        		"aoColumns" : col,		
			                        		"aaSorting": [],
			                        		"bAutoWidth": false,
			                        		"oColVis": {
			                        			"bRestore": true,
			                        			"sSize": "css"
			                        		},
			                        		"sScrollX": "100%",
			                        		"oTableTools": {
			                        			"aButtons": [
			                        			      "copy_to_votable_ImageList_form","copy_to_fits_ImageList_form","copy_to_html_ImageList_form", "csv_button", "copy"
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
													                        jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
																            jQuery('#content_dbaccess_ImageList_form #ImageList_error').html("Query returned invalid data.. Please try another query");
							   					        					jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('normal');
							   					        					jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');   
							   					        					jQuery('#content_dbaccess_ImageList_form #ImageList_results').hide();
												 		          		}		
												 		          	},
													                 "success": function(data){
													                	
	  	 														          	 if (data==null){
	  	 														          		if (!error_bool){ 
														                		 	error_bool = true;
	  	  	 														          		jQuery('#content_dbaccess_ImageList_form #ImageList_error').html("There was an error processing your request.. Please try another query");
									   					        					jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('normal');
									   					        					jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');   
									   					        					jQuery('#content_dbaccess_ImageList_form #ImageList_results').hide();
															                        jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
															                		jQuery('#content_dbaccess_ImageList_form #ImageList_error').scrollMinimal('content_dbaccess_ImageList_form');

	  	 														          		}
	  	 														          	} else {
															                    error_bool = false;
														                        fnCallback(data);
														                        jQuery('#content_dbaccess_ImageList_form #ImageList_results').slideDown('slow');
														                    	jQuery("#content_dbaccess_ImageList_form .scroll-wrapper").remove();
														                        jQuery('#content_dbaccess_ImageList_form .dataTables_scrollBody').doubleScroll();
	
														                    															                	 
															           		}
													                 }
													              });  //end ajax call
																 
												
											} //end fnServerData function
			                        		
			                            }); //end ImageListTable	
			                            
		            					 ImageListTable.fnAdjustColumnSizing(true); 
		            					 current_table = ImageListTable;
				                        jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
	
		            					jQuery('#content_dbaccess_ImageList_form #toggle_query_info').bind('click',function(){ 
		            			   			toggle_minimize_button('#content_dbaccess_ImageList_form #toggle_query_info','#content_dbaccess_ImageList_form #toggle_query_info_div', '[+]', '[-]');
		            			      	});
		            					
	                          		 } // result array not equal to error
		                         
	                     	 
		                    	} //results does not contain error div
			                
				     	}
			   	 	});
			   	 	
			   		return false;

				} else {
				 
                 	jQuery('#content_dbaccess_ImageList_form #ImageList_error').html('There was an error processing your request: ' + validated);	
          			jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('fast');
                 	jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');
                 	jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
            		jQuery('#content_dbaccess_ImageList_form #ImageList_error').scrollMinimal('content_dbaccess_ImageList_form');

				}
				
			    return false;
		    });
		  
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
		              		jQuery('#content_dbaccess_ImageList_form #ImageList_error').html('There was an error processing your request');	
		        			jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('fast');
		            	   	jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');
		               		jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
	                		jQuery('#content_dbaccess_ImageList_form #ImageList_error').scrollMinimal('content_dbaccess_ImageList_form');

		             	},
		             	success: function(data) {
		             		if (data==""){
				       		    jQuery('#content_dbaccess_ImageList_form #ImageList_error').html('There was an error processing your request');	
		           				jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('fast');
		                  		jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');
		             		} else {
		             			jQuery('#ImageList_facebox_content').html(data);
			           	     	jQuery.facebox({ div: '#ImageList_facebox_content' });
				           	    init_scrolling_mechanism('imageListNotes');

		             		}
		             	}
			    	});
	   	 		});
		             			
		             		
	 /**
	  * On facebox element click get extension and load content
	  * 
	  */
   	 jQuery('#ImageList_facebox').live('click', function(e){
	    	 	e.preventDefault();
		    	jQuery.ajax({
	            	type: "POST",
	             	url: path + "getImageHandler",
	             	timeout: 1000000,
	             	data: {href : this.href},
	             	error: function() {
	              		jQuery('#content_dbaccess_ImageList_form #ImageList_error').html('There was an error processing your request');	
	        			jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('fast');
	            	   	jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');
	               		jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
                		jQuery('#content_dbaccess_ImageList_form #ImageList_error').scrollMinimal('content_dbaccess_ImageList_form');

	             	},
	             	success: function(data) {
	             		if (data==""){
			       		    jQuery('#content_dbaccess_ImageList_form #ImageList_error').html('There was an error processing your request');	
	           				jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('fast');
	                  		jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');
	                		jQuery('#content_dbaccess_ImageList_form #ImageList_error').scrollMinimal('content_dbaccess_ImageList_form');

	             		} else {
	             			
	             			
	             			var div = document.createElement('html');
							jQuery(div).html(data.substr(data.indexOf('<body'),data.length));
							var all = div.getElementsByTagName("a");
							for (var i=0, max=all.length; i < max; i++) {
							    all[i].href =  all[i].href.trim();
							    if (all[i].innerHTML.trim() == 'next extensions &gt;' || all[i].innerHTML.trim() == '&lt; previous extensions'){
							    	all[i].className='imageList_external';
							    }
							}
							
	             			var elements = div.childNodes;
	             			jQuery('#ImageList_getImage_results').html(elements);
		           	    	jQuery('#ImageList_getImage_results a[href=http://surveys.roe.ac.uk/wsa/imcopy.html]').attr('class', 'ext_links_facebox');
		           	    	jQuery('#ImageList_getImage_results a[href=http://surveys.roe.ac.uk/wsa/imcopy.html]').attr('href', '#imcopy_div');
		           	     	jQuery.facebox({ div: '#ImageList_getImage_results' });
			           	    init_scrolling_mechanism('facebox');
	             		
	             		}
	 	        	}
	    	 	});
		    	
    	 	
   		 });
   	 

   	
   	 /**
   	  * On .imageList_external element fetch and load content
   	  * 
   	  */
  	 jQuery('.imageList_external').live('click', function(e){
  		 	e.preventDefault();
	    	jQuery.ajax({
         	type: "POST",
          	url: path + "getImageHandler",
          	timeout: 1000000,
          	data: {href : this.href},
          	error: function() {
           		jQuery('#content_dbaccess_ImageList_form #ImageList_error').html('There was an error processing your request');	
     			jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('fast');
         	   	jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');
            	jQuery('#content_dbaccess_ImageList_form #ImageList_load').hide();
        		jQuery('#content_dbaccess_ImageList_form #ImageList_error').scrollMinimal('content_dbaccess_ImageList_form');

          	},
          	success: function(data) {
          		if (data==""){
		       		    jQuery('#content_dbaccess_ImageList_form #ImageList_error').html('There was an error processing your request');	
        				jQuery('#content_dbaccess_ImageList_form #ImageList_error').fadeIn('fast');
        				jQuery('#content_dbaccess_ImageList_form #ImageList_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_ImageList_form #error_tooltip_wrapper').fadeIn('fast');
                		jQuery('#content_dbaccess_ImageList_form #ImageList_error').scrollMinimal('content_dbaccess_ImageList_form');

          		} else {
          			
          			
          			var div = document.createElement('html');
          			jQuery(div).html(data.substr(data.indexOf('<body'),data.length));
						var all = div.getElementsByTagName("a");
						for (var i=0, max=all.length; i < max; i++) {
						    all[i].href =  all[i].href.trim();
						    if (all[i].innerHTML.trim() == 'next extensions &gt;' || all[i].innerHTML.trim() == '&lt; previous extensions'){
						    	all[i].className='imageList_external';
						    }
						}
						var elements = div.childNodes;
          		     	jQuery('#ImageList_getImage_results').html(elements);
	           	    	jQuery('#ImageList_getImage_results a[href=http://surveys.roe.ac.uk/wsa/imcopy.html]').attr('class', 'ext_links_facebox');
	           	    	jQuery('#ImageList_getImage_results a[href=http://surveys.roe.ac.uk/wsa/imcopy.html]').attr('href', '#imcopy_div');
	           	    	
	           	    	jQuery.facebox({ div: '#ImageList_getImage_results' });
		           	    init_scrolling_mechanism('facebox');
			         
          			}
	        	}
 	 	});
	    	
	 	
	 });

  	 
  	jQuery("#content_dbaccess_ImageList_form #error_tooltip_link").live ('click',function() {
 		jQuery('#content_dbaccess_ImageList_form #ImageList_error').slideToggle();
 		return false;

 	});
	
	});   