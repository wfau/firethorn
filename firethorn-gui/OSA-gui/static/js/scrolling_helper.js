jQuery.fn.scrollMinimal = function(this_div_name) {

  var cTop = this.offset().top;
  var div_scrollTop = $('#content-mid').scrollTop();

  $('#content-mid').scrollTop(div_scrollTop + cTop - 45);


};

String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

function init_scrolling_mechanism(this_div_name){
	    var ext_links_name = '.ext_links_' + this_div_name;
		var content_id = "#dynamic_content";
		$('#content-mid').scrollTop(0);
		
		$('#content_' + this_div_name +  ' a[href^="#"]').click(function(event){
	
			if (this.href!="#" & !this.href.endsWith("_div")){
				target = $('a[name="' + this.hash.substr(1) + '"]');
				if (target.length == 0) target = $('#' + this.hash.substr(1));
				if (target.length == 0) target = $('html');
				
				if (target.length !=0) {
					event.preventDefault();
				
			        var cTop = target.offset().top;
			    	var div_scrollTop = $('#content-mid').scrollTop();
			    	$('#content-mid').animate({ scrollTop:div_scrollTop +  cTop - 45 }, 500);
			        return false;
				}
			}
		  
			
		});

		
		jQuery(ext_links_name).live('click',function(event){
		  	event.preventDefault();
			var div_id = this.href.substring(this.href.indexOf('#')+1, this.href.length);
		    var url = path + div_id.substring(0,div_id.indexOf('_div'));  
		    var sub_nav =  url.substring(url.indexOf('#')+1, url.length);
		    var div_id_short = div_id;	
		    $(document).trigger('close.facebox');
		    if (this.id!="index") {
	  		
		    	jQuery.get(url, function(data) {
		    		 jQuery(content_id).html(data).fadeIn();
		    		  if (div_id.indexOf('#')>0){
		  		    	div_id_short = div_id.substring(0, div_id.indexOf('#'));
		  		    	jQuery('a[name*="' + sub_nav + '"]').scrollMinimal('#content_' + div_id_short);
			    		
		    		  }
		    		 
		        });
	  		
		  	}
		  	return false;
		
		  });   
}