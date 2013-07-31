function HoverWatcher(selector){
  this.hovering = false;
  var self = this; 

  this.isHoveringOver = function() { 
    return self.hovering; 
  } 

    jQuery(selector).hover(function() { 
      self.hovering = true; 
    }, function() { 
      self.hovering = false; 
    }) 
} 


function getWindowHeight() {
	  var myWidth = 0, myHeight = 0;
	  if( typeof( window.innerWidth ) == 'number' ) {
	    //Non-IE
	    myWidth = window.innerWidth;
	    myHeight = window.innerHeight;
	  } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
	    //IE 6+ in 'standards compliant mode'
	    myWidth = document.documentElement.clientWidth;
	    myHeight = document.documentElement.clientHeight;
	  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
	    //IE 4 compatible
	    myWidth = document.body.clientWidth;
	    myHeight = document.body.clientHeight;
	  }
	  return myHeight;
	}



	function setMenuHeight(){
		
	
		var height = getWindowHeight();
		var menuHeight = 460;
		var headerHeight = 170;

		jQuery('#nav').height(height - headerHeight);
	
	
	}


	
jQuery(function(){
	
	  
	//Hide SubLevel Menus
	jQuery('#nav ul li ul').hide();

	var navWatcher = new HoverWatcher('#nav ul li ul');
	var main_links = jQuery('#nav ul li.main_menu_li');
	var hoverWatchers = new Array();

	for (var i = 0; i <main_links.length; i++){
		hoverWatchers[i] = new HoverWatcher(main_links[i]);
	}

	//OnHover Show SubLevel Menus
	jQuery('#nav ul li').hover(
		
		//OnHover
		function(){
			var element = this;
			var isHoveringOver = false;

			setTimeout(function() {

					var index_in_array = jQuery.inArray(element, main_links);
					if (index_in_array>0){
						isHoveringOver = hoverWatchers[index_in_array].isHoveringOver();
					}

					if (!jQuery('ul', element).is(":visible")){
					
						if (isHoveringOver){
					     	//Hide Other Menus
						    jQuery('#nav ul li').not(jQuery('ul', element)).stop();
 
							//Add the Arrow
							jQuery('ul li:first-child', element).before(
								'<li class="arrow">arrow</li>'
							);
	
							//Remove the Border
							jQuery('ul li.arrow', element).css('border-bottom', '0');
	
							// Show Hoved Menu
							jQuery('ul', element).slideDown('fast');
							
						} //else {
							//api.reinitialise();

						//}
					
					
					}
				
				
			}, 300);
			api.reinitialise();



		},
		//OnOut
		function(){
			

				if (this.firstChild.id=="dbaccess" || this.firstChild.id=="documentation" || this.firstChild.id=="help" || this.firstChild.id=="archive_explorer"  ){
					var elem = this;
					setTimeout(function() {
	
						if (!navWatcher.isHoveringOver()){
							// Hide Other Menus
							
						    jQuery('ul', elem).delay(50).slideUp('slow');
				
							//Remove the Arrow
							jQuery('ul li.arrow', elem).remove();
		
							jQuery('ul', elem).hide();
							api.reinitialise();
							
						}
					
					}, 400);
					
				

				}
			
			
	
			
		}
	);

});