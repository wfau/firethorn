/*
 * Display an error message
 */
var helper_functions = new function() {
	this.displayError = function(error_div_id, error_text){
		console.log(error_text);
		jQuery(error_div_id).html(error_text);
		jQuery(error_div_id).fadeIn('normal');
		jQuery(error_div_id).delay(3800).fadeOut('slow');
	}
}