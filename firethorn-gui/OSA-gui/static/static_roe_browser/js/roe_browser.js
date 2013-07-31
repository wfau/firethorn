var plus_sign = survey_prefix + '/static/images/plus.gif';
var plus_sign2 = survey_prefix + '/static/images/plus.gif';
var minus_sign = survey_prefix + '/static/images/minus.gif';
var arrow_sign2 = survey_prefix + '/static/images/plus.gif';
var arrow_sign = survey_prefix + '/static/images/arrow.gif';

jQuery(document).ready(function() {
	
	if (typeof String.prototype.startsWith != 'function') {
		  String.prototype.startsWith = function (str){
		    return this.indexOf(str) == 0;
		  };
		}


	jQuery('#roe_browser .other_browsers').livequery('click',function(event){
		event.preventDefault();
		var url = path + this.href;  
		jQuery.get(url, function(data) {
    		 jQuery('#browser_content').html(data).fadeIn();		
        });
 		
	  	return false;
	
	  }); 
	
	jQuery('#roe_browser .hide_menu').livequery('click',function(event){
		event.preventDefault();
		jQuery('#roe_browser #browser_menu').hide('slow');
		jQuery('#roe_browser #browser_content').css({
			width:'100%',
			'max-width':'100%',
			'min-width':'100%',			
		});
		
	  	return false;
	
	  }); 
	
	jQuery('#roe_browser .show_menu').livequery('click',function(event){

		event.preventDefault();
		if (!jQuery('#roe_browser #browser_menu').is(":visible")){
			jQuery('#roe_browser #browser_menu').show("slide", { direction: "left" }, 1000);
			jQuery('#roe_browser #browser_content').css({
				width:'70%',
				'max-width':'70%',
				'min-width':'70%',			
			});

		}
		
		return false;
	
	  }); 
	
	
	
	jQuery('#browser_menu #toggle_browser').live('click',function(event){

		
		event.preventDefault();
		var _this = this;
		var loc = ""
		
		if ( jQuery(_this).is("a") ){
			loc = jQuery(this).attr('href');
		} else if ( jQuery(_this).is("img") ){
			loc = jQuery(this).parent().attr('href');
			_this = jQuery(this).parent();
		}
		
		var img_src = jQuery(jQuery(_this).html());
			
		if (loc.startsWith('http://surveys.roe.ac.uk')){
			if (jQuery(_this).next().next()[0].innerHTML==""){
				var xhr = jQuery.ajax({
			           type: "POST",
			           url: path + "getImageHandler",
			           data: { href : loc},
			           timeout: 1000000,
			           error: function() {
			             	
			           },
			           success: function(data) {
			        	 	var _div = document.createElement('html');
			        	
			        	 	jQuery(_div).html(data);
							
							var _all = _div.getElementsByTagName("body");
							var anchor_links = _div.getElementsByTagName('a');
							var _a_links = [];
							for (var i=0; i<anchor_links.length;i++){
								if (jQuery.trim(anchor_links[i].className) == 'ddrop'){
									_a_links.push(anchor_links[i]);
								}
							}
							
							for (var i2 = 0; i2<_a_links.length;i2++){
								jQuery(_this).next().next().append('<li>');
								jQuery(_this).next().next().append(_a_links[i2]);
								jQuery(_this).next().next().append('</li>');

							}
						
				     	}
			   	 	});
	        	
				
					if (img_src.attr("src") == arrow_sign || img_src.attr("src") == arrow_sign2 || img_src.attr("src") == plus_sign || img_src.attr("src") == plus_sign2){ 
						
						img_src.attr("src", minus_sign);  	
	
					} else {
						
						img_src.attr("src") = plus_sign;  	
				  	}
				
					jQuery(this).next().next().slideToggle('');
					
			}
		} else if (jQuery(_this).attr('class')!="dbinfo"){
			if (jQuery(_this).next().next().length==0){
				var xhr = jQuery.ajax({
			           type: "POST",
			           url: path + "schema_browser",
			           data: { href : loc},
			           timeout: 1000000,
			           error: function() {
			             	
			           },
			           success: function(data) {
			        	  
			        		jQuery(_this).next().after("<ul class='browser_ul'>" + data + "</ul>");
			        		if (img_src.attr("src") == plus_sign || img_src.attr("src")  == plus_sign2 || img_src.attr("src")  == arrow_sign || img_src.attr("src")  == arrow_sign2){ 
			        		
			        			jQuery(jQuery(_this).children()[0]).attr("src", minus_sign);  	

			        		} else {
			        			
			        			jQuery(jQuery(_this).children()[0]).attr("src", plus_sign);  	
			        	  	}
			        	
			        		jQuery(_this).next().next().slideToggle('fast');
	
			           }
			   	 	});
			} else {
				if (img_src.attr("src") == arrow_sign || img_src.attr("src") == arrow_sign2 || img_src.attr("src")== plus_sign || img_src.attr("src") == plus_sign2){ 
        			
					jQuery(jQuery(_this).children()[0]).attr("src", minus_sign);  	

        		} else {
        			
        			jQuery(jQuery(_this).children()[0]).attr("src", plus_sign);  	
        	  	}
        	
        		jQuery(_this).next().next().slideToggle('');
				
				
			}
			
		} else {
			
			if (img_src.attr("src") == plus_sign || img_src.attr("src") == plus_sign2 || img_src.attr("src") == arrow_sign || img_src.attr("src") == arrow_sign2){ 
				
				jQuery(jQuery(_this).children()[0]).attr("src", minus_sign);  	

			} else {
				
				jQuery(jQuery(_this).children()[0]).attr("src", plus_sign);  	
		  	}
		
			jQuery(this).next().next().slideToggle('');
		}
	

		return false;
	});

	
	jQuery('#schema_browser_back').livequery('click',function(event){
		event.preventDefault();
		var lastlinkclicked = readCookie('lastlinkclicked');
		
		var url = path + 'schema_browser/' + lastlinkclicked;
		
		
	  	jQuery.get(url, function(data) {
	   		 jQuery('#roe_browser #browser_content').html(data).fadeIn();		
     	 	});

		jQuery("#schema_browser_back").hide();
		
	});
	
	jQuery('#browser_menu .dhead').livequery('click',function(event){
		createCookie('lastlinkclicked',jQuery(this).attr('href'),1);
		var lastlinkclicked = readCookie('lastlinkclicked');
		jQuery("#schema_browser_back").html(jQuery(this).html());
		jQuery("#schema_browser_back").hide();
		
		if (jQuery(this).attr('target')==null || jQuery(this).attr('target')!="_blank"){
			if (!jQuery(this).attr('href').startsWith('http://surveys.roe.ac.uk')){
				event.preventDefault();
				
				var url = path + 'schema_browser/' + jQuery(this).attr('href');
				
				
			  	jQuery.get(url, function(data) {
			   		 jQuery('#roe_browser #browser_content').html(data).fadeIn();		
		      	});
				return false;
			}
		}
	});
	
	jQuery('#browser_menu .ddrop').livequery('click', function(event){

		event.preventDefault();
		var _this = this;
		var parent1 = jQuery(_this).parents('td').find('a.dhead:first').text();
		var url = jQuery(this).attr('href'); //parent1 + '/'  + jQuery(this).attr('href');
		
		var xhr = jQuery.ajax({
	           type: "POST",
	           url: path + 'schema_browser',
	           data: { href : url},
	           timeout: 1000000,
	           error: function() {
	             	
	           },
	           success: function(data) {
	       			jQuery('#roe_browser #browser_content').html(data).fadeIn();		

	         		
		     	}
	   	 	});
		
	
		return false;
	});
	
	jQuery('#roe_browser #browser_content a').livequery('click', function(event){
		var url;
		
		if (jQuery(this).attr('href')!=null){
			if (jQuery(this).attr('href').startsWith('gloss')){
				event.preventDefault();
				jQuery("#schema_browser_back").show();
				if (jQuery(this).attr('href').indexOf('.html') > 0){
					url = path + 'gloss/' + jQuery(this).attr('href').replace('.html','');
				} else {
					url = path + jQuery(this).attr('href');
				}
				
				jQuery.get(url, function(data) {
			   		var _div = document.createElement('html');
		        	jQuery(_div).html(data);
					var all_tables = _div.getElementsByTagName("table");
					var all_h3s = _div.getElementsByTagName("h3");
					
					var result = "";
					if (all_tables.length>2){
						result = all_h3s[0].outerHTML;
						result += all_tables[2].outerHTML;
					
					
					}
			   		jQuery('#roe_browser #browser_content').html(result).fadeIn();

			   		 oTable = jQuery('#roe_browser #browser_content table').dataTable( {
		            	"bJQueryUI" : true,
		        		"sPaginationType": "full_numbers",
		        		"bProcessing" : true,
		        		"sDom" : 'T<"clear">R<"H"Clfr>t<"F"ip>',
		        		"bAutoWidth": false,
		        		"sScrollX": "100%",
		    
	 				});
			   		 var hashtag_val = url.substr(url.indexOf('#')+1, url.length);
			   		 var split_hash = hashtag_val.split('_');
			   		 current_table = oTable;
			   		 oTable.fnFilterAll( split_hash[1] + ' ' +  split_hash[0] );
						
			    });
				return false;
			} else {
				if (jQuery(this).parent().attr('class')=='atab' || jQuery(this).parent().attr('class')=='func'){
					event.preventDefault();
					
					createCookie('lastlinkclicked',jQuery(this).attr('href'),1);
					var lastlinkclicked = readCookie('lastlinkclicked');	
					jQuery("#schema_browser_back").html(jQuery(this).html());
					jQuery("#schema_browser_back").hide();
					url = jQuery(this).attr('href')
					var xhr = jQuery.ajax({
				           type: "POST",
				           url: path + "schema_browser",
				           data: { href : url},
				           timeout: 1000000,
				           error: function() {
				             	
				           },
				           success: function(data) {
				     			jQuery('#roe_browser #browser_content').html(data).fadeIn();		
	
					     	}
				   	 	});
					
				    	return false;
				} else if (!jQuery(this).attr('href').startsWith('http://')) {
					
					event.preventDefault();
					createCookie('lastlinkclicked',jQuery(this).attr('href'),1);
					var lastlinkclicked = readCookie('lastlinkclicked');
					jQuery("#schema_browser_back").html(jQuery(this).html());
					jQuery("#schema_browser_back").show();
					url = path + 'schema_browser/' + jQuery(this).attr('href');
					jQuery.get(url, function(data) {
				   		 jQuery('#roe_browser #browser_content').html(data).fadeIn();		
				    });
					return false;
				}	
			
			}
		}
			
	});
	
});
