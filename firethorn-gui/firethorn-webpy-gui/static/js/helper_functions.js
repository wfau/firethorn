/*
 * Javascript helper functions for firethorn-gui pages
 * 
 */

var helper_functions = new function() {
	/*
	 * Display an error message
	 */
	this.displayError = function(error_div_id, error_text){
		jQuery(error_div_id).html(error_text);
		jQuery(error_div_id).fadeIn('normal');
		jQuery(error_div_id).delay(3800).fadeOut('slow');
	}
	
	/*
	 * Display an error message
	 */
	this.displayErrorFromParent = function(parent, error_div_id, error_text){
		jQuery(parent).children(error_div_id).html(error_text);
		jQuery(parent).children(error_div_id).fadeIn('normal');
		jQuery(parent).children(error_div_id).delay(3800).fadeOut('slow');
	}
	/*
	 * Toggle a minimize button
	 */
	function toggle_minimize_button(id_link, id_content, toggle_on, toggle_off){
		 
	  	if (jQuery(id_link).html() == toggle_on){ 
	  		jQuery(id_link).html(toggle_off);
	  	} else {
	  		jQuery(id_link).html(toggle_on);
	  	}
		
		jQuery(id_content).slideToggle('slow');
	  
	
}
	/*
	 * Ajax call helper
	 */
	this.ajaxCall = function(data, type, url, timeout, error, success ){
		jQuery.ajax({
            type: type,
            url: url,
            data: data,
            timeout: timeout,
            error: error,
            success: success
    	});
	}
}

