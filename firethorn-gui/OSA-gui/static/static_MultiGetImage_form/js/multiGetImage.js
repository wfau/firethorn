/*
 * WFAU Web2.0 Project
 * Author: Stelios Voutsinas
 * Date: July 2012
 * 
 * Javascript Functions and Variables used for the multiGetImage Page, part of the OSA Interface
 * 
 * 
 */
		var this_div_name = 'dbaccess_MultiGetImage_form';
		init_scrolling_mechanism(this_div_name);
		var content_div = '#content_' + this_div_name;
		jQuery('#MultiGetImage_results').html("");
		jQuery('a[rel*=facebox]').facebox(); 	// Initialize facebox links

		//SAMP Init Variables
		var cc;
		var callHandler;
		var connector;
		var tableUrl;
		var tableId;


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
		 * Validate email input for correct form
		 * 
		 * @param email1
		 * @param email2
		 * @returns {Boolean}
		 */
		function validate_emails(email1, email2) {
		
			if (email1!=email2){
				return false;
			} else {
				var atpos=email1.indexOf("@");
			    var dotpos=email1.lastIndexOf(".");
			    if (atpos<1 || dotpos<atpos+2 || dotpos+2>email1.length) {
			        return false;
			    } else {
			    	return true;
			    }
			}
		}
		
		/**
		 * Validate parameters passed to the multiGetImage form
		 * 
		 * @param params
		 * @returns {String}
		 */
		function validate_multiGetImage(params){
			var email1="";
			var email2="";
			var band="";
			if (jQuery('#content_dbaccess_MultiGetImage_form #uploadFile').val()=="" || jQuery('#content_dbaccess_MultiGetImage_form #uploadFile').val()==null){
				return 'Please select a file to upload';
			}
			
			for (var i = 0;i<params.length;i++){
				if (params[i].name=='database'){
					if (params[i].value=='' || params[i].value==null){
						return 'Database selection was invalid';
					} 
				}  else if (params[i].name=='userX'){
					if (params[i].value=='' || params[i].value==null){
						return 'Empty Size input';
					} else {
						
						try {
							if (!isNumber(parseFloat ( params[i].value))){
								return 'Invalid size input';
							} else if (parseFloat( params[i].value )>12.0 ){
								return "Size input was higher than 12 arcmin";
							} else if (parseFloat( params[i].value )<0.0){
								return "Size input was less than 0";
							} 
						} catch (err){
							return 'Invalid size input';
						}
					}
				} else if (params[i].name=='email'){
					email1 = params[i].value;
				} else if (params[i].name=='email1'){
					email2 = params[i].value;
				} else if (params[i].name=='band'){
					band = params[i].value;
				} 
			} // loop complete
			
			//check for empty entries
			if (band==""){
				return "No band selection was made";
			} 
			
			if (email1==""){
				return "No email [1] input";
			}

			if (email2==""){
				return "No email [2] input";
			}
			

			if (email1!="" && email2!=""){
				if (!validate_emails(email1,email2)) {
					return 'Invalid email input';
				}
			}

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
		 * Handle a multiImage check 
		 * 
		 * @param a_link
		 */
		function handleMultiImageCheck(a_link) {
			href_content = a_link.href;
	     	jQuery.ajax({
				  type: 'POST',
				  url: path + 'getImageHandler',
				  data:  { href: a_link.href },
				  error: function() {
		             	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').html('There was an error processing your request');	
		       			jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').fadeIn('fast');
		              	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_MultiGetImage_form #error_tooltip_wrapper').fadeIn('fast');
		              	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
                		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').scrollMinimal('content_dbaccess_MultiGetImage_form');

				  },
		          success: function(data) {
		        	  
		        	var secondary_div = document.createElement('html');
					secondary_div.innerHTML = data;
					var secondary_all = secondary_div.getElementsByTagName("body");
					var anchor_links = secondary_div.getElementsByTagName('a');
					var secondary_a_link = "";
					
					for (var i = 0;i<anchor_links.length;i++){
						if (anchor_links[i].innerHTML.trim()=="Check again"){
							anchor_links[i].href  = a_link.href;
							//anchor_links[i].href =  multiGetImageCGI + anchor_links[i].href.substring(anchor_links[i].href.indexOf('MultiGetImageCheck'), anchor_links[i].href.length);
							

						}	
					}
	
					
					var temp_index = href_content.lastIndexOf('&');
					if (temp_index <0) temp_index = href_content.length-1;

					var upload_directory = href_content.substr(href_content.indexOf('path=')+5,Math.min(temp_index,href_content.lastIndexOf('dir')));
					

					for (var i = 0;i<anchor_links.length;i++){
					
						if(endsWith(anchor_links[i].href, '.html')){
							anchor_links[i].href = multiGetImageTempURL + upload_directory  + anchor_links[i].href.substring(anchor_links[i].href.lastIndexOf('/'),anchor_links[i].href.length);
						}	
					}
					
					for (var i = 0;i<secondary_all.length;i++){
						body_content = secondary_all[i].innerHTML;
					}
					var samp_content = '<div style="text-align:left;margin-top:10px;"><img id ="samp_connection" src="' + survey_prefix +'/static/images/red-button.png" height="17" width="17" /><button id="register">Connect to SAMP</button><button id="unregister" style="display: none;">Disconnect from SAMP</button>';
                	samp_content += '</div><br />';
                	
					html_results =  body_content + samp_content +  '<br /><div style="font-style:italic">The results of your query have been sent to your email</div>';
		        	jQuery('#MultiGetImage_results').html(html_results).fadeIn('slow');
					jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
	
		          }
			});
     	
     	}
		
		/**
		 * On ready execute
		 * 
		 */
		jQuery(document).ready(function(){

		
			//jQuery('form[name=MultiGetImage_form]').ajaxForm();	//Initiate ajaxForm  for MultiGetImage form
			jQuery('#MultiGetImage_form_results').html("");

			

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
			
			jQuery('#multiget_loadImage').live('click',function(e){ 
				var img_link = this.name;
	            var imgUrl = img_link.replace('getImage.cgi','getFImage.cgi');
	            var imgName = "ROE - Get Image";
	            var msg = new samp.Message("image.load.fits",
		                               {"image-id": imgUrl,
					       "url": imgUrl,
					       "name": imgName});
	            connector.connection.notifyAll([msg]);

			    return false;
			});
			
			jQuery(window).unload( function () { connector.unregister(); } ); 
			
			
			/**
			 * Handle minimizer click by toggling the visibility of the form content
			 */
			jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_minimizer').bind('click',function(){ 
		   		toggle_minimize_button('#content_dbaccess_MultiGetImage_form #MultiGetImage_minimizer','#content_dbaccess_MultiGetImage_form .MultiGetImage_form', '[+]', '[-]');
		   	});
			
			/**
			 * Handle file upload by checking size
			 */
			jQuery('#content_dbaccess_MultiGetImage_form #uploadFile').change(function(){
			    var file = this.files[0];
			    name = file.name;
			    size = file.size;
				if (size>(1024*5)){
	             	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').html('Upload file-size exceeded..Please try again');	
	      			jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').fadeIn('fast');
	             	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_MultiGetImage_form #error_tooltip_wrapper').fadeIn('fast');
				}
			   
			});
		
			/**
			 * Handle multiGetImage form submission
			 * Send request to Java Servlet that handles the request and load content into according area
			 */
			jQuery('form[name=MultiGetImage_form]').each(function() {
		  	
					jQuery(this).submit(function(e){
						e.preventDefault();
						jQuery('#content_dbaccess_MultiGetImage_form #error_tooltip_wrapper').hide(	); 
						jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').hide();
						jQuery('#MultiGetImage_results').html("");
						var form = jQuery(this).serialize();
						var params_for_validation =  jQuery(this).serializeArray();
						var validated = validate_multiGetImage(params_for_validation);
						if (validated!='true'){
							
		                 	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').html('There was an error processing your request: ' + validated);	
		          			jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').fadeIn('fast');
		                 	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_MultiGetImage_form #error_tooltip_wrapper').fadeIn('fast');
		                 	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
	                		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').scrollMinimal('content_dbaccess_MultiGetImage_form');

		               
		                 	return false;
						
						} else {
						jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').fadeIn('normal');
				  	    jQuery(this).ajaxSubmit({
				  	    	type: "POST",
					        url: path + "getImageHandler",
					        data : {action : "multiGetImage"},
				  	    	error: function() {
				             	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').html('There was an error processing your request');	
				      			jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').fadeIn('fast');
				             	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_MultiGetImage_form #error_tooltip_wrapper').fadeIn('fast');
				             	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
		                		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').scrollMinimal('content_dbaccess_MultiGetImage_form');

				  	    	},
				           success: function(data) {
				        	    if (data == "NOT_LOGGED_IN"){
	                            	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
		                            jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').html('Not logged in <br /> Please log in to get access to this form');	
			            			jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').fadeIn('fast');
	                             	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_MultiGetImage_form #error_tooltip_wrapper').fadeIn('fast');                           				                         		
	                        		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').scrollMinimal('content_dbaccess_MultiGetImage_form');

				        	    } else if (data=="" || data==null || data=='None'){
				                 	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').html('There was an error processing your request');	
				          			jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').fadeIn('fast');
				                 	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_MultiGetImage_form #error_tooltip_wrapper').fadeIn('fast');
				                 	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
			                		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').scrollMinimal('content_dbaccess_MultiGetImage_form');

				        	    } else {
				            	   	jQuery('form[name=MultiGetImage_form]').slideUp('slow');
				            		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_minimizer').html('[+]');
				                	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_minimizer').show();
				                 	
						            var div = document.createElement('html');
									div.innerHTML = data;
									var all = div.getElementsByTagName("a");
									var multiStatus_link = "";
									var wget_link = "";
									
									for (var i = 0;i<all.length;i++){
										if (all[i].innerHTML.trim() == "MultiGetImage Status Check"){
											multiStatus_link = all[i];
										} else if (all[i].innerHTML.trim() == "Link to wget download script"){
											wget_link  = all[i];
										}
									}
									
								
									if (multiStatus_link!=""){
										handleMultiImageCheck(multiStatus_link);																		
									} else if (wget_link!="") {
										var wget_div = document.createElement('html');
										wget_div.innerHTML = data;
										var wget_all = wget_div.getElementsByTagName("body");
										
										// Add HTML content (Remove last line that says click your browsers back button) 
										jQuery('#MultiGetImage_results').html(wget_all[0].innerHTML.substring(0, wget_all[0].innerHTML.indexOf("<p>Use the browser's back button"))).fadeIn('slow');
										jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
									} else {
										jQuery('#MultiGetImage_results').html('<div style="color:red;font-size:11px">There was an error processing your request.. Please try again</div>').fadeIn('slow');
										jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
					                 	jQuery('#MultiGetImage_results').delay(3500).fadeOut('slow');

									
									}
								
									
									
				                }
					     	} //end success
				   	 		
				  	    }); // end ajaxSubmit
					    
				  	    return false;
						}
				  	 }); //end success 
			  		
					
			  
			   });
			
			/**
			 * Handle a click by an anchor with the dynamic_div class
			 * Fetch content from URL with AJAX request
			 * 
			 */ 
			jQuery('#content_dbaccess_MultiGetImage_form .dynamic_div a').live('click', function(e){
			  	   
			  	   
				 
				 	 if ( this.innerHTML.trim()=='Check again' ){
				 	 	handleMultiImageCheck(this);
				 	 	 return false;
				 	 } else if (endsWith(this.href, '.pdf') || endsWith(this.href, '.txt')){
				 		$(this).attr('target', '_blank');
				 	
			 		 } else if (endsWith(this.href, '.html') || endsWith(this.href, 'dir')){
			  	 		 jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').fadeIn('normal');
				   	 	 e.preventDefault();
				   	 	 url_loc = this.href;
				   	 	 
				   	 	 if (endsWith(this.href, 'dir') && (this.href.indexOf(base_host)>0)){
				   	 		 url_loc = multiGetImageCGI +this.href.substr(this.href.indexOf(base_url + '/') + (base_url + '/').length+1,this.href.length);
				   	 		 
				   	 		 jQuery.ajax({
								  type: 'POST',
								  url: path + 'getImageHandler',
								  data:  { href: url_loc },
								  error: function() {
						             	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').html('There was an error processing your request');	
						       			jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').fadeIn('fast');
						              	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_MultiGetImage_form #error_tooltip_wrapper').fadeIn('fast');
									    jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
									    jQuery('#MultiGetImage_results').hide();
				                		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').scrollMinimal('content_dbaccess_MultiGetImage_form');

								  },
						          success: function(data) {
					               
						        	
									var secondary_div = document.createElement('html');
									secondary_div.innerHTML = data;
									var secondary_all = secondary_div.getElementsByTagName("body");
									var anchor_links = secondary_div.getElementsByTagName('a');
									var upload_directory = href_content.substr(href_content.indexOf('path=')+5,Math.min(href_content.lastIndexOf('&'),href_content.lastIndexOf('dir')));
									
									for (var i = 0;i<anchor_links.length;i++){
									
										if(endsWith(anchor_links[i].href, '.html')){
											anchor_links[i].href = multiGetImageTempURL + upload_directory  + anchor_links[i].href.substring(anchor_links[i].href.lastIndexOf('/'),anchor_links[i].href.length);
										}	
									}
									
									for (var i = 0;i<secondary_all.length;i++){
										body_content = secondary_all[i].innerHTML;
									}
									
									html_results =  body_content + '<br /><div style="font-style:italic">The results of your query have been sent to your email</div>';
						        	jQuery('#MultiGetImage_results').html(html_results).fadeIn('slow');
						          
						          }
							});
									
				   	 	 } else {
					   	 	 
					    	 jQuery.ajax({
					           	type: "POST",
					            url: path + "getImageHandler",
					            timeout: 1000000,
					            data: {href : url_loc},
					            error: function() {
					             	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
					             	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').html('There was an error processing your request');	
					       			jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').fadeIn('fast');
					              	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_MultiGetImage_form #error_tooltip_wrapper').fadeIn('fast');
					              	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
			                		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').scrollMinimal('content_dbaccess_MultiGetImage_form');

					            },
					            success: function(data) {
					            	if (data==""){
					            		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
					                 	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').html('There was an error processing your request');	
					          			jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').fadeIn('fast');
					                 	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_MultiGetImage_form #error_tooltip_wrapper').fadeIn('fast');
					                 	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
					            	} else {
					                	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
					           	    	
					                	var div = document.createElement('html');
										div.innerHTML = data;
										var all = div.getElementsByTagName("a");
	
										var imgs = div.getElementsByTagName("img");

										for (var i = 0;i<all.length;i++){
											if (endsWith(all[i].href, '.html')){
												all[i].href =''; 
												jQuery(all[i]).addClass('close_links');
												
											}
										}
										
										

										for (var x = 0;x<imgs.length;x++){
											if  (jQuery(imgs[x]).attr('alt').trim()!="null image" && connector.connection!=null){
												
												jQuery(imgs[x]).parents("table:first").after('<button id="multiget_loadImage" name ="' + jQuery(imgs[x]).attr('src') + '">Broadcast Image</button>');
											}

										}	

										
										jQuery(".close_links").live('click', function(e){
											 jQuery.facebox.close(); 
										});
										
										jQuery('#MultiGetImage_results_secondary').html(div.getElementsByTagName("body"));
					                 	jQuery.facebox({ div: '#MultiGetImage_results_secondary' });
					                }
						        }
					   	 	}); //end ajax
											
				   	 	 }			
				   	 	 
						jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();

				    	 return false;
			 			}
				    	 
			    	
			    });
			 
			jQuery("#content_dbaccess_MultiGetImage_form #error_tooltip_link").live ('click',function() {
		 		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').slideToggle();
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
			              		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').html('There was an error processing your request');	
			        			jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').fadeIn('fast');
			            	   	jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_MultiGetImage_form #error_tooltip_wrapper').fadeIn('fast');
			               		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_load').hide();
		                		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').scrollMinimal('content_dbaccess_MultiGetImage_form');

			             	},
			             	success: function(data) {
			             		if (data==""){
					       		    jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').html('There was an error processing your request');	
			           				jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').fadeIn('fast');
			                  		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').delay(3500).fadeOut('slow');jQuery('#content_dbaccess_MultiGetImage_form #error_tooltip_wrapper').fadeIn('fast');
			                		jQuery('#content_dbaccess_MultiGetImage_form #MultiGetImage_error').scrollMinimal('content_dbaccess_MultiGetImage_form');

			             		} else {
			             			jQuery('#MultiGetImage_facebox_content').html(data);
				           	     	jQuery.facebox({ div: '#MultiGetImage_facebox_content' });
					           	    init_scrolling_mechanism('getMultiGetImageNotes_help');

			             		}
			             	}
				    	});
		   	 		});
			
		});
		
		
		var multiGetImage_loaded = true;