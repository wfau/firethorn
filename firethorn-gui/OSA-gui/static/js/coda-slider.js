/*
 * WFAU Web2.0 Project
 * Author: Stelios Voutsinas
 * Date: July 2012
 * 
 * Javascript Functions and Variables used for the OSA Interface
 * 
 * 
 */

// when the DOM is ready...
jQuery(document).ready(function () {

	
    var jQuerypanel = jQuery('#slider #dynamic_content');
    var jQuerycontainer = jQuery('#slider .scroll');
    
    /**
     * Handle nav selection
     * 
     */
    function selectNav(a) {
    	if (jQuery(a).parents('ul:first').attr('class')=='sub_menu_navigation'){
			  jQuery(a)
			   .parents('ul:first')
	            .parents('ul:first')
	                .find('a')
	                    .removeClass('selected')
	                .end()
	            .end()
	            .end()
	            .addClass('selected');
		} else {
	        jQuery(a)
	            .parents('ul:first')
	                .find('a')
	                    .removeClass('selected')
	                .end()
	            .end()
	            .addClass('selected');
		}
	}
    
    
    /** 
     * Handle nav selection with ID as input
     * 
     */
    function selectNavFromID(urlId) {
    	jQuery('#'+urlId)
          .parents('ul:first')
              .find('a')
                  .removeClass('selected')
                  .end()
           .end()
    	jQuery('#'+urlId).addClass('selected');
    }
    
    /**
     * Listener for #slider anchor clicks
     * 
     */
    jQuery('#slider .navigation a').bind('click',function(event){
    
    	var url = this.id;
    	var div_id = "#dynamic_content";
    	var is_footer_link = false;

    	if (!is_footer_link){
    		selectNav(this);
    	} else {
    		selectNavFromID(url);
    	}
    	if (url.indexOf("_footer")!=-1){
    		url = url.substring(0,url.indexOf('_footer'));
    		is_footer_link = true;
    	}
    	
    	if (url!="index" && url!="help" && url!="archive_explorer" && url!="documentation") {
    	
			jQuery.get(path + url, function(data) {
	    		 jQuery(div_id).html(data).fadeIn();		
	        });
    	}
   
    	
    	return false;

    });   
  
 
    /**
     * Find the navigation link that has this target and select the nav
     * 
     */
    function trigger(data) {
    	var url = data.id;
    	var div_id = '#' + data.id + '_div';
    	if (url!="index") {
    		if (jQuery(div_id ).html()==""){
    			jQuery.get(path + url, function(d) {
		    		 jQuery(div_id).html(d).fadeIn();		
		        });
    		}
    	}
    }

    if (window.location.hash) {
    	var loc = window.location.hash.substr(1);
    	var url = "";
    	if (loc.endsWith("_div")){
        	var div_id = "#dynamic_content";
    		var url = loc.substr(0,loc.indexOf("_div"));
    		if (url!="index" && url!="help" && url!="archive_explorer" && url!="documentation") {
				jQuery.get(path + url, function(data) {
					 jQuery(div_id).html(data).fadeIn();		
				});
    		}
    	}
    	

    	
    } else {
        jQuery('ul.navigation a:first').click();
    }
    

});