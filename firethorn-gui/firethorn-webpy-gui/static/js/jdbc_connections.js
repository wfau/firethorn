/*
 * 
 * On document ready, execute:
 * 
 */
jQuery(document).ready(function() {
	jQuery('#add_adql_view').hide();
    
		var success = function(data) {
		
			data = jQuery.parseJSON(data);
			if (data.Code!=null){
				if (data.Code==-1){
					helper_functions.displayError("#error", data.Content);

				} else {
				
			       	jQuery('#vospace_area').html(data.Content).fadeIn('slow');

				}
			}
	}
		
		helper_functions.ajaxCall({}, "GET", properties.getPath() +'vospace', 1000000, function(e) {} , success);
	
	/*
	 * A link was clicked, Fetch the according service information via a POST request
	 * 
	 */ 
	
 	 jQuery('a').live('click', function(e){
 	 	var id_prevented = 'ignore';
		if  (this.id == id_prevented){
			e.preventDefault();
		} else if (this.id =='add_adql_view'){
			
			// Add ADQL View button was clicked, Fetch according data from the according POST URL handler
			
			e.preventDefault();
	  
      		if (jQuery('#adql_tree')!= []){
      			/*
          		jQuery('#add_adql_view').hide();
		      
          		var success = function(data) {
          		
          			data = jQuery.parseJSON(data);
          			if (data.Code!=null){
          				if (data.Code==-1){
          					helper_functions.displayError("#error", data.Content);

          				} else {
          				
          			       	jQuery('#vospace_area').html(data.Content).fadeIn('slow');

          				}
          			}
				}
          		
          		helper_functions.ajaxCall({}, "GET", properties.getPath() +'vospace', 1000000, function(e) {} , success);
            	*/
      		}
      		
           	return false;	        	

		}
		
 	
 	 });
 	 

 	
});