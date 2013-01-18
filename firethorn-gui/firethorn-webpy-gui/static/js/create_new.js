/*
 * 
 * On document ready, execute:
 * 
 */
jQuery(document).ready(function() {

			
	 jQuery(".input_form  :input").removeAttr("disabled");

	/*
	 * On input form submission, send ajax request and load result content into '#container'
	 * 
	 */
	
	 
	 jQuery('button').live('click', function(e){
		 var value = this.value;
		 if (value=="edit"){
			 action = "edit";
			 jQuery(".input_form  :input").removeAttr("disabled");
			 jQuery(".input_form :submit").remove();
			 jQuery(".input_form").append('<input type="submit" class="submit" value="Save">'); 
			 
		 } else if (value=="save"){
			 action = "save";
		 }
		 
	 });
	 
	 
	jQuery('.input_form').live('submit',function(e){
			helper_functions.initiate_loading('#load');
			var value = jQuery('.input_form :submit').val();
			json_data = jQuery(this).serialize();
			if (value=="Next"){
				
				var success = function(data) {
					  data = jQuery.parseJSON(data);
					  if (data.Code!=null){
						  if (data.Code==-1){
							 helper_functions.displayError("#error", data.Content);
						  } else {
							 jQuery(".input_form  :input").attr("disabled", true);
							 jQuery(".input_form  :submit").remove();
					         
					         jQuery('#step2').remove();
					         var br = '<br/>';
					         var start_div ='<div id="step2">';
					         var end_div = '</div>';
							 var link_to_connection ='<div>The following object has been created: <a href="' + data.Content + '">' + jQuery('.input_form #obj_name').val() + '</a></div><br/>'
							 var button_edit = ' <button type="button" value="edit" id="edit" class="button">Edit Details</button>';
							 jQuery('#input_form_container').append(start_div +br + link_to_connection + button_edit + end_div);

						  }
					  }
					helper_functions.loading_complete('#load');

	         	}

		       	xhr = helper_functions.ajaxCall(json_data , "POST",properties.getPath() +  "create_new", 1000000, function(e) { helper_functions.displayError("#error", e);} , success);
		       
			} else if (value=="Save"){

				var success = function(data) {
					  data = jQuery.parseJSON(data);
					  if (data.Code!=null){
						  if (data.Code==-1){
							 helper_functions.displayError("#error", data.Content);
						  } else {
						     jQuery('#step2').remove();
					         var br = '<br/>';
					         var start_div ='<div id="step2">';
					         var end_div = '</div>';
							 var link_to_connection ='<div>The following object has been created: <a href="' + data.Content + '">' + jQuery('.input_form #obj_name').val() + '</a></div><br/>'
							 var button_edit = ' <button type="button" value="edit" id="edit" class="button">Edit Details</button>';
							 jQuery('#input_form_container').append(start_div +br + link_to_connection + button_edit + end_div);
							 jQuery(".input_form  :input").attr("disabled", true);
							 jQuery(".input_form  :submit").remove();
						  }
					  }
					 helper_functions.loading_complete('#load');

	         	}

		       	xhr = helper_functions.ajaxCall(json_data , "POST",properties.getPath() +  "create_new", 1000000, function(e) { helper_functions.displayError("#error", e);} , success);
		     

			} else  if (value=="Submit"){
				var success = function(data) {
					  data = jQuery.parseJSON(data);

					  if (data.Code!=null){
						  if (data.Code==-1){
							 helper_functions.displayError("#error", data.Content);
						  } else {
							  jQuery('#input_form_container').html(data.Content);
						  }
					  }
						helper_functions.loading_complete('#load');

	         	}

		       	xhr = helper_functions.ajaxCall(json_data , "POST",properties.getPath() +  "create_new", 1000000, function(e) { helper_functions.displayError("#error", e);} , success);
		       
				
				
			}

	       	return false;
      
	});

	
	
});
