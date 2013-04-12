/*
 * 
 * On document ready, execute:
 * 
 */
jQuery(document).ready(function() {

	
	 jQuery('#icon_helper').fadeIn('slow');

	 jQuery('.tree-collapsed, #tree-folder-indiv').live('click', function(e){
			
		 var _this = this;
		 if (this.id == 'tree-folder-indiv'){
			 _this = jQuery(_this).parent().find(".tree-arrow")[0];
		 }
		 
		 if (jQuery(_this).parent().find("#children").length>0){

				jQuery(_this).parent().children("#children").slideToggle("slow");

				if (jQuery(_this).hasClass("tree-expanded")){
					jQuery(_this).addClass('tree-collapsed');
					jQuery(_this).removeClass('tree-expanded');
				} else {
					jQuery(_this).addClass('tree-expanded');
					jQuery(_this).removeClass('tree-collapsed');
				}
				
		  
		 } else { 
				e.preventDefault();
				var id_url = encodeURIComponent(jQuery(_this).parent().find("#id_url")[0].href.trim());
				var db_type = encodeURIComponent(jQuery(_this).parent().find("#db_type")[0].innerHTML.trim());
				var workspace = jQuery("#cur_workspace").val();
				var action = 'expand';
				
				var success =  function(data) {
					if (data.Code!=null){
						if (data.Code==-1){
							 helper_functions.displayErrorFromParent(jQuery(_this).parent(), "#expand_error", data.Content);
						} else {
							
							if (jQuery(_this).parent().find("#children").length<=0){
								var children = jQuery("<div id='children'>" + data.Content + "</div>").hide();
								children.appendTo(jQuery(_this).parent()).slideDown();
							} else {
								jQuery(_this).parent().children("#children").slideToggle("slow");
							}

							jQuery(_this).addClass('tree-expanded');
							jQuery(_this).removeClass('tree-collapsed');

						}
					}
		         }
				
				e.preventDefault();
				helper_functions.ajaxCall( {ident : id_url, _type : db_type, action : action, workspace : workspace}, "POST", properties.getPath() + "resource_actions", 1000000, function(e) {helper_functions.displayError("#error", data.Content);} , success);

			}
		 
	 });
	
	 
	 jQuery('.tree-expanded').live('click', function(e){
		 	var _this = this;
		    e.preventDefault();
			jQuery(jQuery(_this).parent().find("#children")).slideUp('slow');
			jQuery(_this).addClass('tree-collapsed');
			jQuery(_this).removeClass('tree-expanded');
	 });
	
	
	
	  jQuery('.input_form').submit(function(){
		
	    	json_data = jQuery(this).serialize();
	    	json_data += '&db_type=' + jQuery('#db_type').val();
	    	var success = function(data) {  
	
				if (data.Code!=null){
					if (data.Code==-1){
						helper_functions.displayError("#import_error", data.Content);
				    } else {
				
				    	if (jQuery('#import_content').length==0){
				    	    jQuery('#import_dialog').append('<div id="import_content">' + data.Content + '</div>');
				    	} else {
				    	    jQuery('#import_content').html(data.Content);
				    	}
					}
			    }
				
			}
	    	
	      	xhr = helper_functions.ajaxCall(json_data, "POST",properties.getPath() +  "/", 1000000, function(e) { helper_functions.displayError("#import_error", e);} , success);
	
	    	
	    	return false;
  
	 });
	
	 jQuery('a').live('click', function(e){
		 
	 		var id_prevented = 'ignore';
	 		var workspace = 'workspace'
	 		var id_add_to = 'add_to';
	 		var expand = 'expand';
	 		var minimize = 'minimize';

	 		var _this = this;
			
	 		if  (this.id == id_prevented){
				e.preventDefault();
				
			} 
 	 
	 });
 	
});