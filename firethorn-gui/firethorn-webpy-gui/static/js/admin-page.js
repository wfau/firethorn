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
		
        	data = jQuery(this).serialize();

        	xhr = jQuery.ajax({
                type: "POST",
                url: properties.getPath() +  "/",
                data: data,
                timeout: 1000000,
                error: function(e) {
					 helper_functions.displayError("#error", e);
                },
                success: function(data) {  
                	data = jQuery.parseJSON(data);
					if (data.Code!=null){
						if (data.Code==-1){
							helper_functions.displayError("#error", data.Content);
					    } else {
				    	    jQuery('#container').hide().html(data.Content).fadeIn('slow');
						}
				    }
					
				}
        	});
        	return false;
      
	});
	
	
	/*
	 * A link was clicked, Fetch the according service information via a POST request
	 * 
	 */
	
 	 jQuery('a').live('click', function(e){
 		
 		var id_prevented = 'ignore';
		if  (this.id == id_prevented){
			e.preventDefault();
		}
 		 /*
		var create_new = base_url + '/create_new';
		
		if  (!(this.href.substring(0, create_new.length) === create_new)){
		
	 	 	e.preventDefault();
		    	jQuery.ajax({
		         	type: "POST",
		         	url:"/",
	                timeout: 1000000,
		          	data: {service_get : this.href},
		          	error: function(e) {
		          		console.log(e);
		          	},
		          	success: function(data) {
		          		jQuery('#container').hide().html(data).fadeIn('slow');
		          	}
			    });
		}
		*/
 	 });
 	 
 	
});