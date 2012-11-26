/*
 * 
 * On document ready, execute:
 * 
 */
jQuery(document).ready(function() {
	
	//Check whether the container element contains any input through the templating system. If yes display	
	if (jQuery('#container').html().length>0){
		jQuery('#container').show();
	}
	
	/*
	 * On input form submission, send ajax request and load result content into '#container'
	 * 
	 */
	jQuery('.input_form').submit(function(){
		
        	json_data = jQuery(this).serialize();
        	json_data += '&db_type=' + jQuery('#db_type').val();
        	var success = function(data) {  

				if (data.Code!=null){
					if (data.Code==-1){
						helper_functions.displayError("#error", data.Content);
				    } else {
			    	    jQuery('#container').hide().html(data.Content).fadeIn('slow');
					}
			    }
				
			}
        	
	      	xhr = helper_functions.ajaxCall(json_data, "POST",properties.getPath() +  "/", 1000000, function(e) { helper_functions.displayError("#error", e);} , success);

        	
        	return false;
      
	});
	
	
	/*
	 * A link was clicked, Fetch the according service information via a POST request
	 * 
	 */
	
 	 jQuery('a').live('click', function(e){
 		var id_prevented = 'ignore';
 		var id_add_to = 'add_to';
 		var expand = 'expand';
 		var minimize = 'minimize';

 		var _this = this;
		if  (this.id == id_prevented){
			e.preventDefault();
		} else if  (this.id == id_add_to){
			
			var id_url = encodeURIComponent(jQuery(this).parent().parent().find("#id_url")[0].href.trim());
			var db_type = encodeURIComponent(jQuery(this).parent().parent().find("#db_type")[0].innerHTML.trim());
			var name = encodeURIComponent(jQuery(this).parent().parent().find("#id_url")[0].innerHTML.trim());
			var workspace = encodeURIComponent(jQuery("#workspace_selection").find(":selected").val());
			var action = 'add';
			
			var success =  function(data) {
				if (data.Code!=null){
					if (data.Code==-1){
						jQuery(jQuery(_this).parent().find("#add_error")[0]).html("Error adding to workspace");
						jQuery(jQuery(_this).parent().find("#add_error")[0]).fadeIn('normal');
						jQuery(jQuery(_this).parent().find("#add_error")[0]).delay(3800).fadeOut('slow');
						
					} else {
						jQuery(jQuery(_this).parent().find("#add_notification")[0]).html("Added to workspace");
						jQuery(jQuery(_this).parent().find("#add_notification")[0]).fadeIn('normal');
						jQuery(jQuery(_this).parent().find("#add_notification")[0]).delay(3800).fadeOut('slow');
						
					}
				}
	         }
			
			e.preventDefault();
			helper_functions.ajaxCall( {id_url : id_url, db_type : db_type, name : name, workspace : workspace, action : action}, "POST", properties.getPath() + "workspace_actions", 1000000, function(e) {console.log(e);} , success);

	    
		    
		    
		} else if  (this.id == expand){
			if (jQuery(_this).parent().parent().parent().find("#children").length>0){

				jQuery(_this).parent().parent().parent().children("#children").slideDown('slow');
				var expand_button = jQuery(_this).parent().parent().parent().find("#toggle_expand")[0];
				jQuery(expand_button).html('Minimize:<a id="minimize"><img src="static/images/16arrow-up.png" style="vertical-align:middle"/></a>');

			} else { 
				e.preventDefault();
				var id_url = encodeURIComponent(jQuery(_this).parent().parent().parent().find("#id_url")[0].href.trim());
				var db_type = encodeURIComponent(jQuery(_this).parent().parent().parent().find("#db_type")[0].innerHTML.trim());
				var action = 'expand';
				
				var success =  function(data) {
					if (data.Code!=null){
						if (data.Code==-1){
							 helper_functions.displayErrorFromParent(jQuery(_this).parent(), "#expand_error", data.Content);
						} else {
							
							if (jQuery(_this).parent().parent().parent().find("#children").length<=0){
								jQuery(_this).parent().parent().parent().append("<div id='children'>" + data.Content + "</div>" );
							} else {
								jQuery(_this).parent().parent().parent().children("#children").slideDown('slow');
							}
							
							var expand_button = jQuery(_this).parent().parent().parent().find("#toggle_expand")[0];
							jQuery(expand_button).html('Minimize:<a id="minimize"><img src="static/images/16arrow-up.png" style="vertical-align:middle"/></a>');
	
							
						}
					}
		         }
				
				e.preventDefault();
				helper_functions.ajaxCall( {ident : id_url, _type : db_type, action : action}, "POST", properties.getPath() + "resource_actions", 1000000, function(e) {helper_functions.displayError("#error", data.Content);} , success);

			}
		} else if  (this.id == minimize){
			e.preventDefault();
			jQuery(jQuery(_this).parent().parent().parent().find("#children")).slideUp('slow');
			var minimize_button = jQuery(_this).parent().parent().parent().find("#toggle_expand")[0];
			jQuery(minimize_button).html('Expand:<a id="expand"><img src="static/images/16arrow-down.png" style="vertical-align:middle"/></a>');
		}

 	 });
 	 
 	
});