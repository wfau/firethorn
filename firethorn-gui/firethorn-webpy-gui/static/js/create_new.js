/*
 * 
 * On document ready, execute:
 * 
 */
jQuery(document).ready(function() {
	
	
	/*
	 * On input form submission, send ajax request and load result content into '#container'
	 * 
	 */
	jQuery('.input_form').submit(function(){
		
        	json_data = jQuery(this).serialize();
         	
        	var success = function(data) {
				  data = jQuery.parseJSON(data);
				  if (data.Code!=null){
					  if (data.Code==-1){
						 helper_functions.displayError("#error", data.Content);
					  } else {
	                	 window.location.href = data.Content;

					  }
				  }
         	}
       	
	       	xhr = helper_functions.ajaxCall(json_data , "POST",properties.getPath() +  "create_new", 1000000, function(e) { helper_functions.displayError("#error", e);} , success);
<<<<<<< local
	       	
	       	return false;
=======
	        return false;
>>>>>>> other
      
	});

	
	
});
