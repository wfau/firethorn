/*
 * myHotel
 * Customers JS page
 *
 */

	function afterAjax()
	{
	$.fancybox({
	        href : '#preview',
	        transitionIn : 'fade',
	        transitionOut : 'fade', 
	        autoSize : false,
	        width    : "960",
	        height   : "580",
	        //check the fancybox api for additonal config to add here   
	        onClosed: function() { $('#preview').html(''); }, //empty the preview div
	    	
	        helpers: {
	               
                overlay : {
                	closeClick : false 
                }
            },
	});
	
	}

	
	
	function afterAjaxDims(wid, heig)
	{
	$.fancybox({
	        href : '#preview',
	        transitionIn : 'fade',
	        transitionOut : 'fade', 
	        autoSize : false,
	        width    : wid,
	        height   : heig,
	        //check the fancybox api for additonal config to add here   
	        onClosed: function() { $('#preview').html(''); }, //empty the preview div
	        helpers: {
	               
                overlay : {
                	closeClick : false 
                }
            },
	      
	});
	
	}
	
	function afterAjaxDimsOverlay(wid, heig,overlay)
	{
	$.fancybox({
	        href : '#preview',
	        
	        autoSize : false,
	        width    : wid,
	        height   : heig,
	     
	        helpers: {
               
                overlay : overlay
            },
	        
	        //check the fancybox api for additonal config to add here   
	        onClosed: function() { $('#preview').html(''); }, //empty the preview div
	    	
	      
	});
	
	}

	
	
	function closeFancybox(){
		  $.fancybox.close();	
	}
