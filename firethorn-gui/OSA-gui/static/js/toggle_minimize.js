/**
 *  Function to change the display and toggle the content of dynamic divs
 *  
 */
function toggle_minimize_button(id_link, id_content, toggle_on, toggle_off){
 
	  	if (jQuery(id_link).html() == toggle_on){ 
	  		jQuery(id_link).html(toggle_off);
	  	} else {
	  		jQuery(id_link).html(toggle_on);
	  	}
	

		jQuery(id_content).slideToggle("slow", function () {
     		update_size();
 		});
	  
	
}
