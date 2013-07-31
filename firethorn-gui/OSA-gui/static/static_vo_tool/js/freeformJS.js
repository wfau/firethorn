/*
 * WFAU Web2.0 Project
 * Author: Stelios Voutsinas
 * Date: July 2012
 * 
 * Javascript Functions and Variables used for the FreeForm SQL Page, part of the OSA Interface
 * 
 * 
 */


	var this_div_name = 'dbaccess_SQL_form';
	init_scrolling_mechanism(this_div_name);
	var content_div = '#content_' + this_div_name;
	
	/*
     * 
     * On document ready, execute:
     * 
     */
	
	
	// SAMP Init Variables
  	var cc;
  	var callHandler;
	
	if (connector_coverage) {
		clearInterval(ping_interval);
		connector_coverage.unregister();
		connector_coverage = null;
	}

  	var connector;
  	var tableUrl;
  	var tableId;
  	var oTable; //Table object used for result Datatable
	var _broadcast_url;
	var processing = false;
	
    // Document loaded
    jQuery(document).ready(function() {
        	
    		
    	 	var pathname_divId = '#content_dbaccess_SQL_form #temp_file';

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
       		    return false;
       		});
       		
       		jQuery(content_div + ' #unregister').live('click',function(e){ 
       		    connector.unregister();
       		    updateStatus();
       		    return false;
       		});


       		jQuery(content_div + ' #loadvotable').live('click',function(e){ 
       		
       				var temp_stored_url = fnSaveAsGetURL('vo', oTable, pathname_divId, base_url);
       				var xhr = jQuery.ajax({
	        	       type: "POST",
	        	       data: {_broadcast_url : temp_stored_url, _action : "save_as_temp_file"},
	        	       url : base_url  + '/data_tables_processing',
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
	        	    	   _broadcast_url = fnSaveAsGetURL('vo', oTable, pathname_divId, base_url);
  	       			  
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
 					       "url": fnSaveAsGetURLforViewer('vo', oTable, pathname_divId, base_url, x_expr, y_expr),
 					       "row": ""+rowIdx});
 	                connector.connection.notifyAll([msg]);
 	               
 	                
    		}
    		
    
            
            /**
             * Votable row click functionality
             * 
             */
    		jQuery(content_div + ' #votable tr').live('click',function(e){
    			jQuery(content_div + ' #votable tr').css('background-color', '');
    			jQuery(this).css('background-color', '#B0BED9');
    			var nTr = jQuery(this).closest("tr").get(0);
        		var rowIndex = oTable.fnGetPosition( nTr);
        		var aData = oTable.fnGetData( rowIndex )
        		var pathname = jQuery(pathname_divId).val();  
        		var params = [];
        		var sort_col = "";
        		var sData = oTable.fnSettings();
        		var params2 = oTable.oApi._fnAjaxParameters(sData);
        		var sortOrder = "asc";
        		
        		if (oTable.dataTableSettings[0].aaSorting.length>0){
            		if (oTable.dataTableSettings[0].aaSorting[0].length>0){
	        			sortOrder = oTable.dataTableSettings[0].aaSorting[0][1];
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
				
				if (_broadcast_url!=null && _broadcast_url!=""){
    	 	
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
				}
 	              
 	                
    		});
    			
           jQuery(window).unload( function () { connector.unregister(); } ); 

           /* end SAMP functionality */
           
           
           /* TableTools library custom button declaration */
           
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
							fnSaveAsCustom('csv', oTable, pathname_divId);
						},
						"fnSelect": null,
						"fnComplete": null,
						"fnInit": null,
						"fnAjaxComplete": function( json ) {
						}
					};
	    		
	    	
			TableTools.BUTTONS.copy_to_fits_SQL_form  = {
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
							fnSaveAsCustom('fits', oTable, pathname_divId);
						},
						"fnSelect": null,
						"fnComplete": null,
						"fnInit": null,
						"fnAjaxComplete": function( json ) {
						}
					};

					 
				TableTools.BUTTONS.copy_to_votable_SQL_form  = {
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
								fnSaveAsCustom('vo', oTable, pathname_divId);
							},
							"fnSelect": null,
							"fnComplete": null,
							"fnInit": null,
							"fnAjaxComplete": function( json ) {
							}
					};
					
					 
				TableTools.BUTTONS.copy_to_html_SQL_form = {
							"sAction": "text",
							"sFieldBoundary": "",
							"sFieldSeperator": "\t",
							"sAjaxUrl": "save_as_html",
							"sToolTip": "",
							"sButtonClass": "DTTT_button_text",
							"sButtonClassHover": "DTTT_button_text_hover",
							"sButtonText": "HTML",
							"mColumns": "visible",
							"bHeader": true,
							"bFooter": true,
							"fnMouseover": null,
							"fnMouseout": null,
			                "sUrl": "/save_as_html",
							"fnClick": function( nButton, oConfig ) {
								fnSaveAsCustom('html', oTable, pathname_divId);
								
							},
							"fnSelect": null,
							"fnComplete": null,
							"fnInit": null,
							"fnAjaxComplete": function( json ) {
							}
					};
			
				/* end TableTools custom buttons */
		           
				
    			jQuery('#load').hide();
        		jQuery(content_div + ' a[rel*=facebox]').facebox();  //Initialize facebook objects
        		var table_names = [];
				jQuery('#content_dbaccess_SQL_form form').find("input[type='submit']").removeAttr('disabled');
	            var xhr; //JQuery ajax request object

        		/**
                 * ADQL Syntax Highlighter
                 */

	            // Get last run query from session
	            var lastquery = readCookie('lastquery');
		        if (lastquery!=null){
		        	jQuery("#textfield").val(lastquery);

		        }	      
		        
                if (editor==null && !jQuery('.CodeMirror').length > 0) {
        		
	                CodeMirror.commands.autocomplete = function(cm) {
	                    CodeMirror.simpleHint(cm, CodeMirror.adqlHint);
	                }
	                 
	                var editor = CodeMirror.fromTextArea(document.getElementById("textfield"), {
	                    mode: "text/x-adql",
	                    tabMode: "indent",
	                    matchBrackets: true,
	                    lineWrapping: true,
	                    textWrapping: true,
	                    extraKeys: {"Ctrl-Space": "autocomplete"}
	                   
	
	
	                  });
	                
                }
                
        		/**
        		 * Check whether an object (obj) is contained in a list (a) 
        		 * 
        		 */
        		function contains(a, obj) {
        		    var i = a.length;
        		    while (i--) {
        		       if (a[i] === obj) {
        		           return true;
        		       }
        		    }
        		    return false;
        		}

        		
        		/**
        		 * Split a space delimited value
        		 * 
        		 */
        		function split( val ) {
        			return val.split(' ');
        		}
        		
        		
        		/**
        		 * Extract the last value from (term)
        		 * 
        		 */
        		function extractLast( term ) {
        			return_val = split( term ).pop() 
        			if (return_val.indexOf(',')>-1){
        				return_val = return_val.split(',').pop();
        			}
        			return return_val;
        		}

        		/**
        		 * Get the medata content from (data) to be used by the auto-completion functions
        		 * Store the content in the availableTags list
        		 * 
        		 */
        		function get_metadata_content (data) {
        			var content = document.createElement('div');
        			content.innerHTML = data;
        		    var tr = content.getElementsByClassName('heading');
        		    for(var i=0;i<tr.length;i++){
        		    	var str = jQuery.trim(jQuery(tr[i]).text());
        		    	availableTags.push(str);
				    }
        		  
        		
        		}


			
				/**
				 * Store the table names, by getting the elements with a heading class, and store them in the table_names list
				 * 
				 */
				function save_table_names (){
					table_names = [];
					jQuery('.heading').each(function() {
					   table_names.push(jQuery(this).text().trim()); 
					});
				}
				
        		/**
        		 * Extract the table values from (term) 
        		 * 
        		 */
        		function extractTables (term) {
					var line = term;
        		    var table_list = [];
        		    for (x in table_names) {
						if ((term.indexOf(" "+ table_names[x]+ " ")>-1) || (term.indexOf(" "+ table_names[x]+ ",")>-1)						
					       || (term.indexOf(","+ table_names[x]+ " ") >-1) ||						
						   (term.indexOf(","+ table_names[x] + ",") >-1)){
							 table_list.push (table_names[x]);
						 }						
					}
        		    return table_list;
        		}
            		
	          
	             
	            /**
	             *  Handle ADQL form submission
	             * 
	             */
	        	jQuery('#content_dbaccess_SQL_form .form').each(function() {
	        		
	                    jQuery(this).submit(function(){
	                    	

	                    	if (jQuery('#content_dbaccess_SQL_form #metadata').css('display')=='none'){
	                    		jQuery('#content_dbaccess_SQL_form #settings_form').slideUp('slow');
	                    	}
	                    	
		            	    var submitButton = jQuery(this).find("input[type='submit']");
		            		var input_string = jQuery("#content_dbaccess_SQL_form textarea#textfield").val();
		            		var selected_endpoint = jQuery("#content_dbaccess_SQL_form #tap_endpoint").val();
		            		var table_mode = jQuery("input[name=table_mode]:checked").val();
		            		var endpoint_arrangement = jQuery("#endpoint_arrangement").val();
		            		
		            		jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').hide()	;
		            		jQuery('#content_dbaccess_SQL_form #error').hide()	;

		            		if (selected_endpoint==''){
		                     	jQuery('#content_dbaccess_SQL_form #error').html('Please select from the list of endpoints');	
		            			jQuery('#content_dbaccess_SQL_form #error').fadeIn('fast');
		                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
                             	jQuery('#content_dbaccess_SQL_form #error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
		            		} else if (input_string!=''){
			                    jQuery(submitButton).attr("disabled", "true");
			                    jQuery('#content_dbaccess_SQL_form #results').fadeOut('fast');
			                    jQuery('#content_dbaccess_SQL_form #load').fadeIn('normal');
			                    processing = true;
			                    jQuery('#content_dbaccess_SQL_form #loading_options').fadeIn('normal');
        					    jQuery("#content_dbaccess_SQL_form #metadata").slideUp();
								var ready = false;
								createCookie('lastquery',input_string,1);
			    				xhr = jQuery.ajax({
			                            type: "POST",
			                            url:  path + "dbaccess_SQL_form",
			                            data: {textfield : input_string,generated_tap : selected_endpoint, table_mode : table_mode, endpoint_arrangement : endpoint_arrangement},
			                            timeout: 1000000,
			                            error: function() {
				                               jQuery('form').find("input[type='submit']").removeAttr('disabled');
				                               jQuery('#content_dbaccess_SQL_form #loading_options').hide();
				                               jQuery('#content_dbaccess_SQL_form #load').hide();	
				                               jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');

				   		            		   processing = false;

			                            },
			                            success: function(data) {
			    		            		processing = false;

			                            	if (data==""){
				                            	jQuery('#content_dbaccess_SQL_form #loading_options').hide();
					                            jQuery('#content_dbaccess_SQL_form #load').hide();
					                            jQuery('#content_dbaccess_SQL_form form').find("input[type='submit']").removeAttr('disabled');	
			                            	} else if (data.startsWith("Query error")){
			                            		jQuery('#content_dbaccess_SQL_form #loading_options').hide();
					                            jQuery('#content_dbaccess_SQL_form #load').hide();
					                            jQuery('#content_dbaccess_SQL_form form').find("input[type='submit']").removeAttr('disabled');						                     
				                          		jQuery('#content_dbaccess_SQL_form #error').html(data);	
						                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
				                          		jQuery('#content_dbaccess_SQL_form #error').fadeIn('fast');
				                             	jQuery('#content_dbaccess_SQL_form #error').delay(3500).fadeOut('slow'); jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');                          
				                          
				                          	} else if (data=="Error" || data == null || data == "None") {
				                            	jQuery('#content_dbaccess_SQL_form #loading_options').hide();
					                            jQuery('#content_dbaccess_SQL_form #load').hide();
					                            jQuery('#content_dbaccess_SQL_form form').find("input[type='submit']").removeAttr('disabled');						                     
				                          		jQuery('#content_dbaccess_SQL_form #error').html('Your request returned an error.. Please check your query and try again');	
						                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
				                          		jQuery('#content_dbaccess_SQL_form #error').fadeIn('fast');
				                             	jQuery('#content_dbaccess_SQL_form #error').delay(3500).fadeOut('slow'); jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');                          
				                          	}  else if (data=="Query_Error") {
				                            	jQuery('#content_dbaccess_SQL_form #loading_options').hide();
					                            jQuery('#content_dbaccess_SQL_form #load').hide();
					        	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');

					                            jQuery('#content_dbaccess_SQL_form form').find("input[type='submit']").removeAttr('disabled');						                     
				                          		jQuery('#content_dbaccess_SQL_form #error').html('Please choose a column to order your results by');	
						            			jQuery('#content_dbaccess_SQL_form #error').fadeIn('fast');
						                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
				                             	jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');                           
				                          	}  else if (data == "MAX_ERROR"){
				                            	jQuery('#content_dbaccess_SQL_form #loading_options').hide();
					                            jQuery('#content_dbaccess_SQL_form #load').hide();
					        	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');

					                            jQuery('#content_dbaccess_SQL_form form').find("input[type='submit']").removeAttr('disabled');						                     
				                          		jQuery('#content_dbaccess_SQL_form #error').html('The results of the requested query exceeded the Max filesize limit.. Please try a different query');	
						            			jQuery('#content_dbaccess_SQL_form #error').fadeIn('fast');
						                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
				                             	jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');                           				                         		
			                            	}  else if (data == "NOT_LOGGED_IN"){
				                            	jQuery('#content_dbaccess_SQL_form #loading_options').hide();
					                            jQuery('#content_dbaccess_SQL_form #load').hide();
					                            jQuery('#content_dbaccess_SQL_form form').find("input[type='submit']").removeAttr('disabled');						                     
				                          		jQuery('#content_dbaccess_SQL_form #error').html('Not logged in <br /> Please log in to get access to this form');	
						            			jQuery('#content_dbaccess_SQL_form #error').fadeIn('fast');
						                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
						            			jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');                           				                         		
			                            	} else {
				                            	jQuery('#content_dbaccess_SQL_form #loading_options').hide();
					                            //jQuery('#load').hide();
				                          		jQuery('#content_dbaccess_SQL_form #results').hide();	
   					                            jQuery('#content_dbaccess_SQL_form form').find("input[type='submit']").removeAttr('disabled');				                          		
   					                            var row_length = 0;
   					                            var result_array = [];
   					                            try {
   					                            	result_array = jQuery.parseJSON(data);
   					                            	row_length = parseInt(result_array[1]);
   					                            } catch (ex) { 
   						                     		result_array = "error";
   						                     		row_length =-1;
   					                            } 
   					                            
   					                           
   					                          if (result_array.length<1 || row_length == 0 ){
				                          			jQuery('#content_dbaccess_SQL_form #error').html('Your query returned no results');	
				                          			jQuery('#content_dbaccess_SQL_form #error').fadeIn('fast');
				    		                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

				                          			jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
					                             	jQuery('#content_dbaccess_SQL_form #load').hide();
					            	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();
   					                          } else if (result_array == "error"){
		   					                       	jQuery('#content_dbaccess_SQL_form #error').html('The server was unable to process your request');	
				                          			jQuery('#content_dbaccess_SQL_form #error').fadeIn('fast');
				    		                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

				                          			jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
					                             	jQuery('#content_dbaccess_SQL_form #load').hide();
					            	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();
   					                    	   
				                          	  } else if (result_array != "error"){
					                          		jQuery('#content_dbaccess_SQL_form #results').html(result_array[2][0]);
					                          		var pathname = jQuery('#content_dbaccess_SQL_form #temp_file').val();
					            					var data_source = "";
					            					jQuery('#content_dbaccess_SQL_form #load').hide();
					            	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();

					            					jQuery('#content_dbaccess_SQL_form #results').append('<table cellpadding="0" cellspacing="0" border="0" class="datatables" id="votable"></table>' ).fadeIn(1500);                            	
					                          	
					                          		var error_bool = false;
					                          	
					            					if (table_mode == "interactive") {
					            						col = [];
					            						for (i=0;i<result_array[0].length;i++) {
						                          			col.push({"sTitle":  result_array[0][i]});
							                            }
							                      		
					            						var json_result_data = jQuery.parseJSON(jQuery('#content_dbaccess_SQL_form #hidden_json_results').val());
					            						if (json_result_data!=null){
						            						oTable = jQuery('#content_dbaccess_SQL_form .datatables').dataTable( {
								                        		"bJQueryUI" : true,
								                        		"sPaginationType": "full_numbers",
								                        		"sDom" : 'T<"clear">R<"H"Clfr>t<"F"ip>',
								                        		"oLanguage": {
							                        			  "sProcessing": "<img src='" + survey_prefix + "/static/static_vo_tool/ajax-loader.gif'>"
							                        	        },
								                        		"aaData" : json_result_data,
								                        		
								                        		"aaSorting": [],
								                        		"aoColumns" : col,					                        		
								                        		"bAutoWidth": false,
								                        		"sScrollX": "100%",
								                        		"oColVis": {
								                        			"bRestore": true,
								                        			"sSize": "css"
								                        		},
								                        		"oTableTools": {
								                        			"aButtons": [
								                        				"copy_to_votable_SQL_form","copy_to_fits_SQL_form","copy_to_html_SQL_form", "csv", "copy"
								                        				]
								                        		}
								                            }); //end oTable
						            						
						            						current_table = oTable;
								                      		jQuery('#content_dbaccess_SQL_form #results').fadeIn();
								                      		jQuery("#content_dbaccess_SQL_form .scroll-wrapper").remove();
									                        jQuery('#content_dbaccess_SQL_form .dataTables_scrollBody').doubleScroll();

					            						} else {
					    					          		jQuery('#content_dbaccess_SQL_form #error').html("There was an error processing your request.. Please try another query");
			   					        					jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
			   						                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

			   					        					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');   
			   					        					jQuery('#content_dbaccess_SQL_form #results').hide();
									                        jQuery('#content_dbaccess_SQL_form #load').hide();
							            	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();

						        						}
					            					} else {
					            						var col = [];
						                          		for (i=0;i<result_array[0].length;i++) {
						                          			col.push({"sTitle":  result_array[0][i], "mDataProp" : result_array[0][i]});
							                            }
					            						oTable = jQuery('#content_dbaccess_SQL_form .datatables').dataTable( {
							                        		"bJQueryUI" : true,
							                        		"sPaginationType": "full_numbers",
							                        		"sDom" : 'T<"clear">R<"H"Clfr>t<"F"ip>',
							                        		"oLanguage": {
							                        	         "sProcessing": "<img style='position:absolute;left:45px;top:-2px;' src='" + survey_prefix + "/static/static_vo_tool/ajax-loader.gif'>"
							                        	       },
							                        	     "bProcessing" : true,
							                        		"bServerSide" : true,
							                        		"aoColumns" : col,	
							                        		"bAutoWidth": false,
							                        		"aaSorting": [],
							                        		"sScrollX": "100%",
							                        		"oColVis": {
							                        			"bRestore": true,
							                        			"sSize": "css"
							                        		},
							                        		"oTableTools": {
							                        			"aButtons": [
							                        			      "copy_to_votable_SQL_form","copy_to_fits_SQL_form","copy_to_html_SQL_form", "csv_button", "copy"
							                        			 ]
							                        		},
							                        		"sAjaxSource" : path  + 'data_tables_processing',
															"fnServerData" : function ( sSource, aaData, fnCallback ) {
																				 aaData.push({'name' : 'pathname', 'value' : pathname});
																				  jQuery.ajax({
																	           		 "dataType": 'json',
																	                 "type": "POST",
																	                 "url": sSource,
																	                 "data": aaData, 
																                     "timeout": 1000000,
																                     "error": function(data){
																 		          		if (!error_bool){ 
																	                    	error_bool = true;
																				            jQuery('#content_dbaccess_SQL_form #error').html("Query returned invalid data.. Please try another query");
											   					        					jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
											   						                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

											   					        					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast'); 
											   	
											   					        					jQuery('#content_dbaccess_SQL_form #results').hide();
																	                        jQuery('#content_dbaccess_SQL_form #load').hide();
																	 		        
																 		          		}		
																 		          	},
																	                 "success": function(data){
																	                	
			  	  	 														          	 if (data==null){
			  	  	 														          		if (!error_bool){ 
																	                		 	error_bool = true;
				  	  	 														          		jQuery('#content_dbaccess_SQL_form #error').html("There was an error processing your request.. Please try another query");
												   					        					jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
												   						                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

												   					        					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');   
												   					        					jQuery('#content_dbaccess_SQL_form #results').hide();
																		                        jQuery('#content_dbaccess_SQL_form #load').hide();
																		                    																	             		
			  	  	 														          		}
			  	  	 														          	} else {
																		                    error_bool = false;
																	                        jQuery('#content_dbaccess_SQL_form #load').hide();
																	                        fnCallback(data);
																	                        jQuery('#content_dbaccess_SQL_form #results').fadeIn('fast');
																	                        jQuery("#content_dbaccess_SQL_form .scroll-wrapper").remove();
																	                        jQuery('#content_dbaccess_SQL_form .dataTables_scrollBody').doubleScroll();
																			      	  					                	 
																			           	}
																	                 }
																	              });  //end ajax call
																				 
																
															} //end fnServerData function
							                        		
							                            }); //end oTable	
					            					}
					            					
						                            oTable.fnAdjustColumnSizing(true);   
				            						current_table = oTable;

						        	       	        jQuery('#content_dbaccess_SQL_form #toggle_query_info').bind('click',function(){ 
						                     			  toggle_minimize_button('#content_dbaccess_SQL_form #toggle_query_info','#content_dbaccess_SQL_form #toggle_query_info_div', '[+]', '[-]');
						                            });
							        	       	 	
						                  	  			
				                          		}
				                          	}
			           					}
			                    });
			    			
	            			} else {
	                     		jQuery('#content_dbaccess_SQL_form #error').html("Please enter a query");
	        					jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
		                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

	        					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
	        					jQuery('#content_dbaccess_SQL_form #load').hide();
	            			}
	                   	    return false;
	            		
	                     });
	                 
	        	});
	            
	            /**
	             * Stop Query Button Clicked: Cancel previous request
	             * 
	       		 */
	       		jQuery("#content_dbaccess_SQL_form  .myButtonStop").live ('click',function() {
	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');
	                jQuery('#content_dbaccess_SQL_form #load').fadeOut('fast');
	                jQuery('#content_dbaccess_SQL_form #loading_options').fadeOut('normal');
	                xhr.abort();
	                return false;
	       		});
	            
 		       	/**
 		       	 * Email Query Button Clicked: Cancel previous request, Load Email submission contents
 		       	 *
 		       	 */
       		 	jQuery("#content_dbaccess_SQL_form .myButtonEmail").live ('click',function() {
	        	    xhr.abort();
	        		jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").attr("disabled", "true");
	                jQuery('#content_dbaccess_SQL_form #load').fadeOut('fast');
	                jQuery('#content_dbaccess_SQL_form #loading_options').fadeOut('normal');
	                jQuery('#content_dbaccess_SQL_form #send_email').slideDown();
	                return false;
	        	});
	            
	       		/**
	       		 * Cancel Email Button Clicked: Slide up the email submission content
	       		 * 
	       		 */
	        	jQuery("#content_dbaccess_SQL_form .email_cancel").live ('click',function() {
	        	    jQuery('#content_dbaccess_SQL_form #send_email').slideUp();
	                jQuery('#content_dbaccess_SQL_form form').find("input[type='submit']").removeAttr('disabled');
	        	    return false;
	       		});
	        	
	        	
	        	jQuery("#content_dbaccess_SQL_form #error_tooltip_link").live ('click',function() {
	        		 jQuery('#content_dbaccess_SQL_form #error').slideToggle();
	        	    return false;

	        	});
	        	
	        	
	        	/**  
	        	 * Email form submission: Validate for user endpoint, query selection and email input
	        	 * Send asynchronous POST request to re-initialize query, which will be sent to clients email 
	        	 */
	        	jQuery('#content_dbaccess_SQL_form .email_form').submit(function() {
	        		
	            	var input_query = jQuery('#content_dbaccess_SQL_form textarea#textfield.textfield').val();
	        		var input_email = jQuery("#content_dbaccess_SQL_form input#email").val();
            		var tap_endpoint = jQuery('#content_dbaccess_SQL_form #tap_endpoint').val();
	        		
            		var endpoint_arrangement = jQuery("#endpoint_arrangement").val();

            		
            		if (tap_endpoint==''){
                 		jQuery('#content_dbaccess_SQL_form #error').html("Please select from the list of catalogues");
    					jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
    					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
            		} else if (input_query==''){
                 		jQuery('#content_dbaccess_SQL_form #error').html("Please enter a query");
    					jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
    					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
	        		} else if (validateForm()){
	                    jQuery('#content_dbaccess_SQL_form form').find("input[type='submit']").removeAttr('disabled');
		        		jQuery('#content_dbaccess_SQL_form #send_email').slideUp();	 
		        	
		        		
		        		jQuery.ajax({
	                        type: "POST",
	                        url: path + "send_email",
	                        data: {email : input_email, query : input_query, generated_tap : tap_endpoint, endpoint_arrangement : endpoint_arrangement},
	                        timeout: 1000000,
	                        error: function() {
	                        	jQuery('#content_dbaccess_SQL_form #email_message').html('There was an error processing you request');
	                        	jQuery('#content_dbaccess_SQL_form #email_message').fadeIn('normal');
	    	                   	jQuery('#content_dbaccess_SQL_form #email_message').fadeOut(2500);
	                        	
	                        },
	                        success: function(data) {
	                        	jQuery('#content_dbaccess_SQL_form #email_message').html('An email with your results will arrive shortly');
	                        	jQuery('#content_dbaccess_SQL_form #email_message').fadeIn('normal');
	    	                   	jQuery('#content_dbaccess_SQL_form #email_message').fadeOut(2500);

	            	        }
	                    });
	        		} else {
                 		jQuery('#content_dbaccess_SQL_form #error').html("Please enter a valid email");
    					jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

    					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
	        		}
	        
	               	return false;	        	
	       		 });
	            

	          /**
	           * Hide Meta-data button: Toggle the metadata content  
	           *  
	           */
	      	  jQuery("#content_dbaccess_SQL_form .metadata_cancel").live ('click',function() {
	           		jQuery("#content_dbaccess_SQL_form #metadata").slideToggle();
					jQuery('#content_dbaccess_SQL_form #load').hide();
		
	          });    
	        
	      	  /** 
	      	   * Metadata Toggle button clicked: Send POST request to server to retrieve metadata for selected endpoint
	      	   * If Metadata already loaded, simply toggle displaying the content 
	      	   * 
	       	   */
	       	  jQuery("#content_dbaccess_SQL_form .toggle_metadata").live ('click',function() {
	       		    var type = "http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json";
          			var endpoint_arrangement = jQuery("#endpoint_arrangement").val();
            		var display = jQuery('#content_dbaccess_SQL_form #metadata').css('display');
            		
            		var tap_endpoint = jQuery('#content_dbaccess_SQL_form #tap_endpoint').val();
           		    if (jQuery('#content_dbaccess_SQL_form #tables').html()!=""){
					    jQuery("#content_dbaccess_SQL_form #metadata").slideToggle();
           		    } else 
            		if (tap_endpoint==''){
                 		jQuery('#content_dbaccess_SQL_form #error').html("Please select from the list of catalogues");
    					jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
    					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
    					jQuery('#content_dbaccess_SQL_form #load').hide();
           		    } else if (display=='none'){
						jQuery('#content_dbaccess_SQL_form #load').hide();
	                    jQuery('#content_dbaccess_SQL_form #load').fadeIn('normal');
		       			jQuery.ajax({
	                            type: "POST",
	                            url: path + "dbaccess_SQL_form",
	                            data: {display : 'yes',generated_tap : tap_endpoint, endpoint_arrangement : endpoint_arrangement, resource_type : type},
	                            timeout: 1000000,
	                            error: function() {
	                            	jQuery('#content_dbaccess_SQL_form #load').hide();	                          
			                 		jQuery('#content_dbaccess_SQL_form #error').html("There was an error executing your request. Please try again");
			                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

			                 		jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
			    					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
								},
	                            success: function(data) {
	                            	if (data==""){
	                            		jQuery('#content_dbaccess_SQL_form #load').hide();	                      
				                 		jQuery('#content_dbaccess_SQL_form #error').html("There was an error executing your request.The resource you are using may be unavailable" );
				                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

				                 		jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
				    					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
	                            		
	                            	} else if (data=="NOT_LOGGED_IN"){
					                	jQuery('#content_dbaccess_SQL_form #error').html("Please log in to access this information");
				  		  				jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
				                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

				  		  				jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
						            	jQuery('#content_dbaccess_SQL_form #toggle_endpoints_a').html('Choose Database(s)');

					    			 
					    			} else {
		                            	jQuery('#content_dbaccess_SQL_form #tables').html(data).hide().fadeIn('fast');
										jQuery("#content_dbaccess_SQL_form .meta_content").hide();
										jQuery("#content_dbaccess_SQL_form .column_content").hide();
										jQuery('#content_dbaccess_SQL_form #load').hide();
									    jQuery("#content_dbaccess_SQL_form #metadata").slideToggle();
									  
									    get_metadata_content(data);
										save_table_names();
									    //Toggle content loading for metadata information  
									  
									    jQuery('#content_dbaccess_SQL_form .column_heading').bind('click',function(){ 
									    	jQuery(this).next().slideToggle(500);
									    });

	                            	}
	                            }
	                    });
					} else {
						jQuery('#content_dbaccess_SQL_form #load').hide();
					    jQuery("#content_dbaccess_SQL_form #metadata").slideToggle();

					}
	               	return false;	        	
								
	       	  });       
				
	       	  jQuery('#content_dbaccess_SQL_form .heading').live('click',function(){ 
	       		  var _this = jQuery(this);
	       		  var inputs = jQuery(this).parent().children("input");
		    	  var ident = "";
		    	  var resource_type = "";
		    	  
		    	  if (_this.next().length>0){
		    		 if (_this.next().is(":visible")){
		    			 _this.nextAll().css('display','none');	 
		    		 } else {
		    		 _this.nextAll().css('display','block');
		    		 }
		    	  } else {
	
			    	  for (var i=0;i<inputs.length;i++){
			    		  if (jQuery(inputs[i]).attr("name")== "resource_type"){
			    			  resource_type =jQuery(inputs[i]).val();
			    		  }
			    		  if (jQuery(inputs[i]).attr("name")== "resource_ident"){
			    			  ident =jQuery(inputs[i]).val();
			    		  }
			    	  }
		       		  
	           		   
	            		if (ident==''){
	                 		jQuery('#content_dbaccess_SQL_form #error').html("There was an error processing your request");
	    					jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
	                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
	    					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
	    					jQuery('#content_dbaccess_SQL_form #load').hide();
	           		    } else {
							jQuery('#content_dbaccess_SQL_form #load').hide();
		                    jQuery('#content_dbaccess_SQL_form #load').fadeIn('normal');
			       			jQuery.ajax({
		                            type: "POST",
		                            url: path + "dbaccess_SQL_form",
		                            data: {display : 'yes',generated_tap : ident, resource_type : resource_type},
		                            timeout: 1000000,
		                            error: function() {
		                            	jQuery('#content_dbaccess_SQL_form #load').hide();	                          
				                 		jQuery('#content_dbaccess_SQL_form #error').html("There was an error executing your request. Please try again");
				                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
	
				                 		jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
				    					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
									},
		                            success: function(data) {
		                            	if (data!= "" && data!=null){
		                            		_this.after(data);
		                            		get_metadata_content(data);
		                            	}
		                            	jQuery('#content_dbaccess_SQL_form #load').hide();
		                            }
		                    });
	           		    }
		    	  }
	               	return false;	        	
			    });
	       	  
	       	  /**
	       	   * Handle action of change to #cache_or_fetch input element
	       	   * 
	       	   */
			  jQuery('#content_dbaccess_SQL_form #cache_or_fetch input').change(function() {
					    jQuery("#content_dbaccess_SQL_form #endpoints_form").slideUp(500);
				  		jQuery('#content_dbaccess_SQL_form #toggle_endpoints_a').html('Choose Database(s)');
				  	   	jQuery('#content_dbaccess_SQL_form #overflow').delay(3800).html("");   
			  });		    

			  	  
			  /**
	   	       * Toggle Endpoints button clicked:
	   	       * Send POST request with variables signaling to the server to fetch endpoints from Registry	  
			   * 
	       	   */
			  jQuery("#content_dbaccess_SQL_form #toggle_endpoints").click(function() {
       		
        			if (!processing){
        				jQuery('#content_dbaccess_SQL_form #load').hide();
    	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();
    	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');
        			}
        		

        			if (jQuery("#facebox #unselected_catalogues").html()!=null || jQuery("#facebox #selected_catalogues").html()!=null){
        				jQuery('#facebox').show();
        				
        			} else if (jQuery("#unselected_catalogues").html()=="" || jQuery("#selected_catalogues").html()==""){
        			
        				var use_cached_endpoints = "0";  
						use_cached_endpoints = jQuery("#content_dbaccess_SQL_form input[name=cache_selection]:checked").val();  
						var endpoint_arrangement = jQuery("#content_dbaccess_SQL_form #endpoint_arrangement").val();
						jQuery('#content_dbaccess_SQL_form #load').show();
					    jQuery.ajax({
				               		traditional: true,
				                    type: "POST",
				                    url: path + "dbaccess_SQL_form",
				                    dataType: "text",
				                    data: {endpoint_form : 'display', use_cached_endpoints : use_cached_endpoints,endpoint_arrangement : endpoint_arrangement},
				                    timeout: 1000000,
				                    error: function() {
					                    	jQuery('#content_dbaccess_SQL_form #error').html("There was an error executing your request. Please try again");
					  		  				jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
					                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
	
					  		  				jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
					            			jQuery('#content_dbaccess_SQL_form #load').hide();
				                    	
				                    },
				                    success: function(data) {	
				                    	
				                    	    if (!processing){
				                    		    jQuery('#content_dbaccess_SQL_form #load').hide();
				                        	}
				                    		if (data=="NOT_LOGGED_IN"){
							                	jQuery('#content_dbaccess_SQL_form #error').html("Please log in to access this information");
						  		  				jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
						                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
						                		jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
	
							    			 
							    			} else if (data!=""){
							    		     	jQuery.facebox({ div: '#database_selection' });
							    				var json_data = jQuery.parseJSON(data);
							    				var json_data_selected = json_data['selected'];
							    				var json_data_unselected = json_data['unselected'];
	
							    				if (json_data['selected']!=null){
							    					 jQuery("#facebox #selected_catalogues").html(json_data_selected);
							    				 }
							    				 if (json_data['unselected']!=null){
							    					 jQuery("#facebox #unselected_catalogues").html(json_data_unselected);
							    				 }
							          	        
							         	     	$("#facebox #selected_catalogues").sortable({
							         	     	    connectWith: "#facebox #unselected_catalogues"
							         	     	});
							         	     	$("#facebox #unselected_catalogues").sortable({
							         	     	    connectWith: "#facebox #selected_catalogues"
							         	     	});
	
							         	     	$('#facebox .delete, #facebox .add').live('click', function() {
							         	     	    item = $(this).parent();
							         	     

							         	     	    item.fadeOut(function() {
							         	     	    
							         	     	        if (item.parent().attr('id') == 'unselected_catalogues') {
							         	     	        
							         	     	            $('#facebox #selected_catalogues').append(item.fadeIn());
							         	     	        } else {
							         	     	            $('#facebox #unselected_catalogues').append(item.fadeIn());
							         	     	        }
							         	     	    });
							         	     	});
							         	     	
							    		         
							         	     	var content = $('#facebox .content').html();
							         	   	
										     	// Put the object into storage
										     	localStorage.setItem('content', JSON.stringify(content));
								
							                } else {
							    				if (jQuery("#content_dbaccess_SQL_form input[name=cache_selection]:checked").val()== use_cached_endpoints) {
								    				jQuery('#content_dbaccess_SQL_form #error').html("There was an error processing your request.<br> (The endoint or registry may be temporarily unavailable)<br><br><br>");
								    				jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
							                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
	
								    				jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
							    				}
							    			}
				                    	
				                    	
				                    }
				                    
				        });
					    
        			} else {
             	     	jQuery.facebox({ div: '#database_selection' });

        			}


				    return false;	        	

	   		  });
				 
	          jQuery('#facebox #database_selection_more').live('click',function(e){ 
	        	    e.preventDefault();
	        	    
	        	    var id_link = '#facebox #database_selection_more';
					var id_content = '#facebox #database_selection_additional';
					var toggle_on = 'More';
					var toggle_off = 'Less';
				
					if (jQuery(id_link).val() == toggle_on){ 
						jQuery(id_link).val(toggle_off);
						jQuery(id_content).show('slow');
					} else {
						jQuery(id_link).val(toggle_on);
						jQuery(id_content).hide('slow');
					}
					
					
			});
	        jQuery('#facebox #database_selection_cancel').live('click',function(e){ 
		     	    e.preventDefault(); 
			     	
			     	// Retrieve the object from storage
			     	var retrievedObject = localStorage.getItem('content');
	
			     	jQuery('#facebox .content').html( JSON.parse(retrievedObject));
			     	// retrievedObject should now replace the current DOM
	        });
	          
	        jQuery('#facebox #database_selection_ok').live('click',function(e){ 
	     	    e.preventDefault();
	     	  	var selected_endpoints = "";
        		var endpoint_arrangement = jQuery("#endpoint_arrangement").val();

	       		var count = 0;
	       		var db_list = [];
	       		jQuery('#facebox #selected_catalogues li').each(function() {
	       			
	       			var name = jQuery(this).children('span').html();
	       			var alias = jQuery(this).children('input:first').val();
	       			var ident =  jQuery(this).find('input').eq(1).val();
	       			count = count + 1;
	       			db_list.push({'name' : name, 'alias' : alias, 'ident' : ident })
	       		});
	       	
	       		
       			if (db_list==[]){
             		jQuery('#content_dbaccess_SQL_form #error').html("Please select from the list of catalogues");
					jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
            		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
            		jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
            		jQuery('#content_dbaccess_SQL_form #load').hide();
       			} else {       			
        			jQuery('#content_dbaccess_SQL_form #load').hide();
	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();
	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');
	                jQuery('#content_dbaccess_SQL_form #load').fadeIn('normal');
            		jQuery('#content_dbaccess_SQL_form #load').scrollMinimal('content_dbaccess_SQL_form ');

				    jQuery("#content_dbaccess_SQL_form #metadata").slideUp();
				    jQuery('#content_dbaccess_SQL_form #tables').html("");
				    
     				if (count>=1){
				        
   				            		jQuery('#content_dbaccess_SQL_form #load').scrollMinimal('content_dbaccess_SQL_form ');

   				   			    	prefix_array = JSON.stringify(db_list);
   				   			    	jQuery.ajax({
   					               		traditional: true,
   				                        type: "POST",
   				                        url: path + "dbaccess_SQL_form",
   				                        data: {tap_endpoints : prefix_array, endpoint_arrangement : endpoint_arrangement},
   				                        timeout: 1000000,
   				                        error: function() {
   				                     		jQuery('#content_dbaccess_SQL_form #error').html("There was an error executing your request");
   				    						jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
   					                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

   				    						jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
   				    		    			jQuery('#content_dbaccess_SQL_form #load').hide();
			            	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();
			            	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');

   				    		    			
   				    		    		    
   				                        },
   				                        success: function(data) {
   				                        	var content = $('#facebox .content').html();
									     	localStorage.setItem('content', JSON.stringify(content));

   				                        	if (data!=""){
   				                        		jQuery("#content_dbaccess_SQL_form #tap_endpoint").val(data);

   				                        		
   				                				jQuery('#content_dbaccess_SQL_form #load').hide();
				            	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();
				            	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');

   				                				
   				                			
   				                        	} else {
   				                        	
   				                         		jQuery('#content_dbaccess_SQL_form #error').html("There was an error executing your request. Please check your input and try again");
   					    						jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
   						                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

   					    						jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
   				                         		jQuery('#content_dbaccess_SQL_form #load').hide();
				            	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();
				            	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');


   				        					}

   				                        }
   				                    });

				   			    
				   			    } else {
					   			 	jQuery('#content_dbaccess_SQL_form #error').html("Please select one or more from the list of catalogues");
									jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
				            		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');
				            		jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
				            		jQuery('#content_dbaccess_SQL_form #load').hide();
	     					
				   			    }
				   			}
       			
				
       						$.facebox.close();
       	   					return false;
 	     	    
				   		
				
						
			});  
	          
	        jQuery('#facebox #database_selection_cancel').live('click',function(e){ 
	      	       e.preventDefault();
	      	       $.facebox.close();
						
			});
	       		  

			  
	          /** 
	           * Endpoint form submitted: Fetch Endpoint selection
	           * Send list of URLs to server, then load the returned data to a hidden input variable
       		   *
       		   */
       		  jQuery('#content_dbaccess_SQL_form .endpoints_form').submit(function() {
	       		var selected_endpoints = "";
        		var endpoint_arrangement = jQuery("#endpoint_arrangement").val();

	       		var count = 0;
	       		jQuery('#content_dbaccess_SQL_form #toggle_endpoints_a').html('Choose Database(s)');
	       		jQuery('#content_dbaccess_SQL_form #endpoints_form input:checked').each(function() {
	       			selected_endpoints += "  " + (jQuery(this).attr('value'));
	       			count = count + 1;
	       		});
       			if (selected_endpoints==""){
             		jQuery('#content_dbaccess_SQL_form #error').html("Please select from the list of catalogues");
					jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
            		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

					jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
        		} else {       			
        			jQuery('#content_dbaccess_SQL_form #load').hide();
	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();
	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');

                    jQuery('#content_dbaccess_SQL_form #load').fadeIn('normal');
            		jQuery('#content_dbaccess_SQL_form #load').scrollMinimal('content_dbaccess_SQL_form ');

				    jQuery("#content_dbaccess_SQL_form #metadata").slideUp();
				    jQuery('#content_dbaccess_SQL_form #tables').html("");
     				if (count>1){
				         // Display a div allowing user to enter prefixes
				    	 var content = "";
				   		 content = "<b> Enter a prefix for each endpoint, or leave empty for automatic generation</b><br><br>";	
				   		
				  
				   		 jQuery('#content_dbaccess_SQL_form #endpoints_form input:checked').each(function() {
			       			content+= jQuery(this).attr('name') + '&nbsp;&nbsp;<input type="textfield" name="' + (jQuery(this).attr('value')) +  '" id="' + (jQuery(this).attr('value')) +  '"/><br><br>';

				   		 });
				   		 
				    	 jQuery("#prefixes").html(content);
				   		 jQuery("#prefixes").dialog({
				   			resizable: false,
				   			width: 700,
				   			modal: true,
			     			closeOnEscape: false,
				    		open: function(event, ui) { jQuery(".ui-dialog-titlebar-close").hide(); },
				   			buttons: {			
				   			    Proceed: function() {
   				   			        jQuery(this).dialog("close");
   				   			        jQuery('#prefixes input').val(function( index, value ) {
   				   			        	if (value==""){
   				   			        		return "";
   				   			        	} else {
   				   			        		return value;
   				   			        	}
   				   			        });
   				   			        
   				   			        	
   				   			    	var pre_array = jQuery('#prefixes input').serializeArray();
   				            		jQuery('#content_dbaccess_SQL_form #load').scrollMinimal('content_dbaccess_SQL_form ');

   				   			    	prefix_array = JSON.stringify(pre_array);
   				   			    	jQuery.ajax({
   					               		traditional: true,
   				                        type: "POST",
   				                        url: path + "dbaccess_SQL_form",
   				                        data: {tap_endpoints : selected_endpoints, prefixes : prefix_array, endpoint_arrangement : endpoint_arrangement},
   				                        timeout: 100000,
   				                        error: function() {
   				                     		jQuery('#content_dbaccess_SQL_form #error').html("There was an error executing your request");
   				    						jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
   					                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

   				    						jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
   				    		    			jQuery('#content_dbaccess_SQL_form #load').hide();
			            	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();
			            	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');

   				    		    			
   				    		    		    
   				                        },
   				                        success: function(data) {
   				                    
   				                        	if (data!=""){
   				                        		jQuery("#content_dbaccess_SQL_form #tap_endpoint").val(data);

   				                        		jQuery('#selected_databases_content').html("");
				                				for (var i=0;i<pre_array.length;i++){
				                					if (pre_array[i]["value"]!= ""){
				                						jQuery('#selected_databases_content').append(pre_array[i]["value"] + '</br>');
	   				                				}
				                				}
				                				
				                				jQuery("#content_dbaccess_SQL_form #selected_databases_div").slideDown();
				                				
   				                				jQuery('#content_dbaccess_SQL_form #load').hide();
				            	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();
				            	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');

   				                				jQuery("#content_dbaccess_SQL_form #toggle_metadata").slideDown(500);
   				                				jQuery("#content_dbaccess_SQL_form #endpoints_form").slideToggle(500);
   				                			
   				                        	} else {
   				                        	
   				                         		jQuery('#content_dbaccess_SQL_form #error').html("There was an error executing your request. Please check your input and try again");
   					    						jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
   						                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

   					    						jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
   				                         		jQuery('#content_dbaccess_SQL_form #load').hide();
				            	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();
				            	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');


   				        					}

   				                        }
   				                    });

				   			    
				   			    },
				   			    Cancel: function() {
				   			    	jQuery('#content_dbaccess_SQL_form #load').hide();
	   			  			    	jQuery(this).dialog("close");
	   			  			    	jQuery( "#prefixes").html(" ");
				   			    }
				   			}
				   		});
				   		jQuery("#prefixes").slideDown('slow');
							
				    } else {
				    	jQuery.ajax({
			               		traditional: true,
		                        type: "POST",
		                        dataType: "text",
		                        url: path + "dbaccess_SQL_form",
		                        data: {tap_endpoints : selected_endpoints, endpoint_arrangement : endpoint_arrangement},
		                        timeout: 100000,
		                        error: function() {
		                     		jQuery('#content_dbaccess_SQL_form #error').html("There was an error executing your request");
		    						jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
			                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

		    						jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
		                        },
		                        success: function(data) {
		                        
							    
		                        	if (data!=""){
		                        		jQuery("#content_dbaccess_SQL_form #tap_endpoint").val(data);
		                				jQuery('#content_dbaccess_SQL_form #load').hide();
		            	                jQuery('#content_dbaccess_SQL_form #loading_options').hide();
		            	                jQuery('#content_dbaccess_SQL_form .form').find("input[type='submit']").removeAttr('disabled');

		                				jQuery("#content_dbaccess_SQL_form #toggle_metadata").slideDown(500);
			                			jQuery("#content_dbaccess_SQL_form #endpoints_form").slideToggle(500);
			                			jQuery('#selected_databases_content').html("");
			                			
			                			var single_db_content = '';

			                			jQuery('#content_dbaccess_SQL_form #endpoints_form input').each(function() {
			                				if (jQuery(this).attr('value')+'/' == data){
			     			       				single_db_content = jQuery(this).attr('name');
			     			       			}
			     				   		});
			                			jQuery('#selected_databases_content').append(single_db_content + '</br>'); 				                			
			                			jQuery("#selected_databases_div").slideDown();
		        						
		        					} else {
		                         		jQuery('#content_dbaccess_SQL_form #error').html("There was an error executing your request");
			    						jQuery('#content_dbaccess_SQL_form #error').fadeIn('normal');
				                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

			    						jQuery('#content_dbaccess_SQL_form #error').delay(3800).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
		                         		jQuery('#content_dbaccess_SQL_form #load').hide();

		        					}

		                        }
		                    });

				    }
        		}

               	return false;

       		  });
       	
    
	          /** 
	           * Toggle check-all/uncheck all checkboxes
	           * 
	           */
       		  jQuery('#content_dbaccess_SQL_form #select_all').click(function() {
       			  	if(jQuery('#content_dbaccess_SQL_form #select_all').html()=="Check All"){
             			jQuery('#content_dbaccess_SQL_form #endpoints_form').find(':checkbox').attr('checked', 'checked');
             			jQuery('#content_dbaccess_SQL_form #select_all').html("Uncheck All");
       			  	} else {
       				  	jQuery('#content_dbaccess_SQL_form #endpoints_form').find(':checkbox').removeAttr("checked");
         				jQuery('#content_dbaccess_SQL_form #select_all').html("Check All");
       			  	}
	      	  });
       		  
       		  
	   	      /**
	   	       * Toggle Endpoints button clicked:
	   	       * Send POST request with variables signaling to the server to fetch endpoints from Registry	  
       		   *
	   	       */
       		  jQuery("#content_dbaccess_SQL_form #settings").click(function() {
       			  toggle_minimize_button('#content_dbaccess_SQL_form #settings_a','#content_dbaccess_SQL_form #settings_form','Settings', 'Hide Settings');
       		  });
       		 
       		  /**
       		   * Bind Click listener to the metadata help link
       		   * 
       		   */
       		  jQuery('#content_dbaccess_SQL_form #metadata_help_link').bind('click',function(){ 
       			toggle_minimize_button('#content_dbaccess_SQL_form #metadata_help_link','#content_dbaccess_SQL_form #metadata_help', '[+]', '[-]');
       		  });
       		  
       		  
       		  /**
       		   * Listener attached to the selected databases button
       		   * Display the selected database in a JQuery dialog
       		   * 
       		   */
       		  jQuery('#content_dbaccess_SQL_form #selected_databases_div').click(function() {
       			  
       			  jQuery('#selected_databases_content').dialog({
       				  width:520,
       				  position:'center',
       				  title: 'Selected Databases',
       				 closeText: 'hide' 
       			  });
       			  
       			 jQuery(".ui-dialog-titlebar-close").show();
       			 jQuery('#selected_databases_content').fadeIn('slow');
       			
       		  });
       		  
       		   jQuery('#clear_query').live('click', function(e){
					if (editor!=null){
					    editor.setValue("");
				            jQuery("#textfield").val("");
		                            eraseCookie('lastquery');
		
					}
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
 			              		jQuery('#content_dbaccess_SQL_form #error').html('There was an error processing your request');	
 			        			jQuery('#content_dbaccess_SQL_form_form #error').fadeIn('fast');
		                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

 			            	   	jQuery('#content_dbaccess_SQL_form_form #error').delay(3500).fadeOut('slow');
 			               		jQuery('#content_dbaccess_SQL_form_form #load').hide();jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
 			             	},
 			             	success: function(data) {
 			             		if (data==""){
 					       		    jQuery('#content_dbaccess_SQL_form_form #error').html('There was an error processing your request');	
 			           				jQuery('#content_dbaccess_SQL_form_form #error').fadeIn('fast');		                		
 			                		jQuery('#content_dbaccess_SQL_form #error').scrollMinimal('content_dbaccess_SQL_form ');

 			                  		jQuery('#content_dbaccess_SQL_form_form #error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_SQL_form #error_tooltip_wrapper').fadeIn('fast');
 			             		} else {
 			             			jQuery('#sql_facebox_content').html(data);
 				           	     	jQuery.facebox({ div: '#sql_facebox_content' });
 					           	    init_scrolling_mechanism('sqlFormNotes');

 			             		}
 			             	}
 				    	});
 		   	 		});
       		  
       		  
      	  });
	          
	    
