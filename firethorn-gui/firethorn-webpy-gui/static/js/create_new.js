/*
 * 
 * On document ready, execute:
 * 
 */
jQuery(document).ready(function() {
	
	
	/*
	 * On input form submission, send ajax request and load result content into '#container'
	 * 
	 */
	jQuery('.input_form').submit(function(){
		
        	data = jQuery(this).serialize();

        	xhr = jQuery.ajax({
                type: "POST",
                url:"/create_new",
                data: data,
                timeout: 1000000,
                error: function(e) {
                	console.log(e);
                },
                success: function(data) {
                	jQuery('#container').hide().html(data).fadeIn('slow');
                }
        	});
        	return false;
      
	});
	
	
	/*
	 * A link was clicked, Fetch the according service information via a POST request
	 * 
	 */ 
	
 	 jQuery('a').live('click', function(e){
 	 	var id_prevented = 'ignore';
		if  (this.id == id_prevented){
			e.preventDefault();
		} else if (this.id =='add_adql_view'){

			e.preventDefault();
	    	jQuery.ajax({
	         	type: "POST",
	         	url:"create_view",
                timeout: 1000000,
	          	error: function(e) {
	          		console.log(e);
	          	},
	          	success: function(data) {
	          		var sample_data = jQuery.parseJSON(data);
	          		if (jQuery('#adql_tree')!= []){
		          		jQuery('#add_adql_view').hide();
	                	jQuery('#container').append('<div id="adql_tree" style="clear:both;text-align:left;width:600px;"><span style="font-style:italic">Add ADQL View:</span><br/><br /><ul id="tt" checkbox="true" class="easyui-tree" ></ul></div><a id="create_view" style="float:left" class="button">Create View</a>');
	                	jQuery('#tt').tree({  
	                        url:'create_view' ,
	                        animate:true,
	            			onClick:function(node){jQuery(this).tree('beginEdit',node.target)}
	                    });  
	                	
	          		}
	          	}
	    	});
		    
			   
		} else if (this.id =='create_view'){

			e.preventDefault();
			if (jQuery('#view_creation_info')!= []){
				
				jQuery('#create_view').hide();
				jQuery('#adql_tree').hide();

				var nodes = jQuery('#tt').tree('getChecked');	// get checked nodes
				var notify_html = "<div id ='view_creation_info' style='text-align:left'> <span style='font-style:italic'> A view has been created with the following items:</span> <br /><br />";
				for (var i=0;i<nodes.length;i++){
					notify_html += "<a>" + nodes[i].text + "</a><br />";
					
				}
				notify_html += "</div>";

            	jQuery('#container').append(notify_html);
            
      		}
			
		}
		/*
 		var create_new = base_url + '/create_new';
		if (!this.href.substring(0, create_new.length) === create_new){
		
	 	 	e.preventDefault();
		    	jQuery.ajax({
		         	type: "POST",
		         	url:"/",
	                timeout: 1000000,
		          	data: {service_get : this.href},
		          	error: function(e) {
		          		console.log(e);
		          	},
		          	success: function(data) {
		          		jQuery('#container').hide().html(data).fadeIn('slow');
		          	}
			    });
		}
		*/
 	 });
 	
 	 
});