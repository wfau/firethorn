/*
 * 
 * On document ready, execute:
 * 
 */
jQuery(document).ready(function() {

	
		function generate_view_interface(){
			
			if (jQuery('#adql_tree')!= []){
		  		var jtree_div = '<div id="adql_tree" style="clear:both;text-align:left;"><ul id="tt" checkbox="true" class="easyui-tree" ></ul></div>';
		  		var icon_helper = '<div id="icon_helper" style="text-align:center;margin-left:250px;width:700px;margin-bottom:20px;">' +
									'<span style="margin-right:35px;"><img style="vertical-align:bottom;" src="static/js/jquery-treeview/themes/default/images/res1-small.png"/> - Resource </span>' +
									'<span style="margin-right:35px;"><img style="vertical-align:bottom;" src="static/js/jquery-treeview/themes/default/images/catalogue-small.png"/> - Catalogue </span>' +
									'<span style="margin-right:35px;"><img style="vertical-align:bottom;" src="static/js/jquery-treeview/themes/default/images/schema-small.png"/> - Schema </span>' +
									'<span style="margin-right:35px;"><img style="vertical-align:bottom;" src="static/js/jquery-treeview/themes/default/images/table-small.png"/> - Table </span>' +
									'<span style="margin-right:35px;"><img style="vertical-align:bottom;" src="static/js/jquery-treeview/themes/default/images/column-small.png"/> - Column </span>' +
								'</div>'
								
		  		var layout_div = icon_helper +
		  	    '<div id="cc" class="easyui-layout" style="width:98%;height:500px;">'  +
		        '<div region="west" split="true" title="ADQL Navigator" style="width:650px;">  '  +
		           ' <br/><p style="padding:5px;margin:0;font-style:italic;text-align:left;margin-left:15px">Add ADQL View:</p><br/>  '  + jtree_div +
		        '</div>  '  +
		        '<div id="view_layout_content" region="center" title="Object content" style="padding:5px;">  '  +
		       ' </div>  ' +
		       ' </div>  ';
		        
		  		jQuery('#input_div').after(layout_div);
		  		jQuery('#cc').layout();  
		  		jQuery('#view_layout_content').html('Welcome to the default page. Please navigate and select objects via the tree on the left panel');
				
				
		  		
				//Create Dynamic tree view
		  		
		  		jQuery('#tt').tree({  
		            url: properties.getPath() +'create_view' ,
		            onBeforeLoad:function(node, param){
		            	if (node!=null){
		                    param.type = node['type'];
		                }
		
		            },
		            animate:true,
					onLoadSuccess:function(node, data){
						var type = "";
						if (data.length>0){
							type = data[0]["type"];
						}
						
						var tree_nodes = jQuery('.tree-node');
						
						for (var i=0;i<=tree_nodes.length;i++){
							if (jQuery(tree_nodes[i]).find('.tree-folder').find('img').length==0){
								jQuery(tree_nodes[i]).find('.tree-folder').append("<img src='" + properties.type_images[type] + "' />");
							}
						}
						
						
					},
		            onLoadError:function(arguments){
		        		 helper_functions.displayError("#error", "Error while loading the data");
		            },
		            onClick:function(node){
		            	jQuery('#view_layout_content').html(node.text);
						},
		            onDblClick:function(node){
						jQuery(this).tree('beginEdit',node.target);
						},
					onCheck:function(node, checked){
						var id = node.id;
						var success = function(data) {
						   data = jQuery.parseJSON(data);
						   if (data.Code!=null){
					    	   if (data.Code==-1){
					    	     	 helper_functions.displayError("#error", data.Content);
					    	   }
						   }
					    }
						
				       	helper_functions.ajaxCall({'id' : id,'checked' : checked, action : 'checkbox_edit' }, "POST",properties.getPath() + 'create_view_edit_handler', 1000000, function(e) {} , success);
						
						},
					onAfterEdit:function(node){
						var text = node.text;
						var id = node.id;
						var success = function(data) {
				            	  data = jQuery.parseJSON(data);
				            	  if (data.Code!=null){
			   		            	  if (data.Code==-1){
			   		            		 helper_functions.displayError("#error", data.Content);
			   		            	  }
				            	  }
						}
		
				       	helper_functions.ajaxCall({'id' : id, 'text' : text, action : 'name_edit'}, "POST",properties.getPath() +'create_view_edit_handler', 1000000, function(e) {} , success);
				               				
					}
		        });  
		  		var button_submit = '<br /><br /> <button style="float:right" type="button" value="Submit" id="submit" class="button">Submit</button>';
				jQuery('#cc').after(button_submit);
		  		jQuery('#add_view').remove();
	    	
			}
			

	 }
		
	 jQuery(".input_form  :input").removeAttr("disabled");

	/*
	 * On input form submission, send ajax request and load result content into '#container'
	 * 
	 */
	
	 
	 jQuery('button').live('click', function(e){
		 var value = this.value;
		 if (value=="add_view"){
			 action = "add_view";
		  	 // Add ADQL View button was clicked, Fetch according data from the according POST URL handler
			 generate_view_interface();
		     
		 } else if (value=="edit"){
			 action = "edit";
			 jQuery(".input_form  :input").removeAttr("disabled");
			 jQuery(".input_form :submit").remove();
			 jQuery(".input_form").append('<input type="submit" class="submit" value="Save">'); 
			 
		 } else if (value=="save"){
			 action = "save";
		 }
		 
	 });
	 
	 
	jQuery('.input_form').live('submit',function(e){
			var value = jQuery('.input_form :submit').val();
			json_data = jQuery(this).serialize();
        	
			if (value=="Next"){
				var success = function(data) {
					  data = jQuery.parseJSON(data);
					  if (data.Code!=null){
						  if (data.Code==-1){
							 helper_functions.displayError("#error", data.Content);
						  } else {
							 jQuery(".input_form  :input").attr("disabled", true);
							 jQuery(".input_form  :submit").remove();
					         
					         jQuery('#step2').remove();
					         var br = '<br/>';
					         var start_div ='<div id="step2">';
					         var end_div = '</div>';
							 var link_to_connection ='<div>The following object has been created: <a href="' + data.Content + '">' + jQuery('.input_form #obj_name').val() + '</a></div><br/>'
		                	 var button_add = ' <button type="button" value="add_view" id="add_view" class="button">Add view</button>';
							 var button_edit = ' <button type="button" value="edit" id="edit" class="button">Edit Details</button>';
							 jQuery('#input_form_container').append(start_div +br + link_to_connection + button_add + button_edit + end_div);

						  }
					  }
	         	}

		       	xhr = helper_functions.ajaxCall(json_data , "POST",properties.getPath() +  "create_new", 1000000, function(e) { helper_functions.displayError("#error", e);} , success);
		       
			} else if (value=="Save"){
				 jQuery(".input_form  :input").attr("disabled", true);
				 jQuery(".input_form  :submit").remove();

			}
			
	       	return false;
      
	});

	
	
});
