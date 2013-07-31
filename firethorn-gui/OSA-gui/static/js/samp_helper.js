/*
 * WFAU Web2.0 Project
 * Author: Stelios Voutsinas
 * Date: July 2012
 * 
 * Functions used by several interface pages to enable Web SAMP functionality
 * Makes use of samp.js
 * 
 * 
 */


   
   var prev_connection_check = false; // Bool variable used to ensure the client does not send multiple requests for the button icons
   /**
    * On connection status change, update content
    */
   updateStatus = function() {
       var isConnected = !! connector.connection;
       if (isConnected) {
    	   jQuery('#register').hide();
           jQuery('#unregister').show();
           jQuery('#loadvotable').show();
          
           if (jQuery('#samp_connection').attr('src')== survey_prefix + '/static/images/red-button.png'){
        	   jQuery('#samp_connection').attr('src', survey_prefix + '/static/images/green-button.png');
           } 
           
           jQuery('#loadImage').show();
    	   prev_connection_check = true;
    	   first_time_check = false;
       }
       else {
    	   jQuery('#register').show();
           jQuery('#unregister').hide();
           jQuery('#loadvotable').hide();
           if (jQuery('#samp_connection').attr('src')== survey_prefix + '/static/images/green-button.png'){
        	   jQuery('#samp_connection').attr('src', survey_prefix + '/static/images/red-button.png');
           } 
           if (prev_connection_check!=false){
        	   jQuery('#samp_connection').attr('src', survey_prefix + '/static/images/red-button.png');        
           }
           
           jQuery('#loadImage').hide();
    	   prev_connection_check = false;
    	   first_time_check = false;


       }
   }

    getImageUrl = function(loadImgBtn) {
	return loadImgBtn.parent().find('a').attr('href');
    };

    getImageName = function(loadImgBtn) {
	return loadImgBtn.parent().find('a').text();
    };

    getRowId = function(tr) {
    	
    	var rowIndex = jQuery(tr)
        .closest('tr') // Get the closest tr parent element
        .prevAll() // Find all sibling elements in front of it
        .length; 
    	return rowIndex;
    };

   findRow = function(rowIdx) {
       var idx = parseInt(rowIdx)+1;
       return jQuery('#votable tr').eq(idx);

   };
   
