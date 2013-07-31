
var api;
jQuery(document).ready(function() {
	
	var sidebar = jQuery("#sidebar");
	var main = jQuery("#main");
	

	jQuery('#toggle_menu_bar').live('click', function(e){
		var expanded = 0.93, //50%
	    shrink = 0.77; //100%

		var status = $(this).attr("class");
		if (status == "showing"){
			
			 $(this).addClass("hidden").removeClass("showing");
	         sidebar.hide("slide", { direction: "left" }, 300);
			 main.delay(500).animate({width:$("#slider").width()* expanded}, 300);
			 $(this).animate({left:2}, 300);
			 this.innerHTML = '<img id="menu_bar_img" src="' + survey_prefix + '/static/images/scroll_right2.png"></img>';
			 jQuery('#footer').delay(500).animate({ marginLeft: '-=138px'},300, function(){
				 jQuery(window).resize();
				
			 });
			 
		} else {
			 $(this).addClass("showing").removeClass("hidden");
			
			 main.animate({width:$("#slider").width()* shrink},300);
			 sidebar.delay(400).show("slide", { direction: "left" }, 300);
			
			 $(this).delay(400).animate({left:249}, 300);
			 this.innerHTML = '<img id="menu_bar_img" src="' + survey_prefix + '/static/images/scroll_left2.png"></img>';
			 jQuery('#footer').delay(500).animate({ marginLeft: '+=138px'},300,function(){
				 jQuery(window).resize();
			 });

			
		}
		return false;
	});
	

	/* Disable right clicks for internal links */
	jQuery('a').live('contextmenu', function(e){
		var loc = this.href;
    	if (!loc.endsWith("_div") && loc.startsWith(base_url) ){
    		return false;
		} 
	});
	
	
	setMenuHeight();
	
	
	api = jQuery('#nav').jScrollPane(	{
		showArrows: true,
		horizontalGutter: 10,
		hideFocus: true
	}).data('jsp');

	
	jQuery(window).resize(function() {
		setMenuHeight();
		api.reinitialise();
		clearTimeout(window.refresh_size);
		window.refresh_size = setTimeout(function() { update_size(); }, 250);
	});


	
});
