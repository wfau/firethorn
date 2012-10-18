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

