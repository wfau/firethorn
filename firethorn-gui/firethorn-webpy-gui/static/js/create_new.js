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
                url: properties.getPath() +  "create_new",
                data: data,
                timeout: 1000000,
                error: function(e) {
					 helper_functions.displayError("#error", e);

                },
                success: function(data) {
					  data = jQuery.parseJSON(data);
					  if (data.Code!=null){
						  if (data.Code==-1){
							 helper_functions.displayError("#error", data.Content);
						  } else {
		                	 jQuery('#container').hide().html(data.Content).fadeIn('slow');

						  }
					  }
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
			
			// Add ADQL View button was clicked, Fetch according data from the according POST URL handler
			
			e.preventDefault();
	  
      		if (jQuery('#adql_tree')!= []){
          		jQuery('#add_adql_view').hide();
          		var end_div = '</div>';
     
          		var jtree_div = '<div id="adql_tree" style="clear:both;text-align:left;"><ul id="tt" checkbox="true" class="easyui-tree" ></ul></div>';
          		var layout_div = '' +
          	    '<div id="cc" class="easyui-layout" style="width:100%;height:500px;">'  +
                '<div region="west" split="true" title="ADQL Navigator" style="width:250px;">  '  +
                   ' <p style="padding:5px;margin:0;font-style:italic">Add ADQL View:</p>  '  + jtree_div +
                '</div>  '  +
                '<div id="view_layout_content" region="center" title="Object content" style="padding:5px;">  '  +
               ' </div>  ';
                
          		jQuery('#container').append(layout_div);
          		jQuery('#container').append(end_div);
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
                    lines:true,
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
        				jQuery.ajax({
        					  type: 'POST',
        					  url:  properties.getPath() + 'create_view_edit_handler',
        					  data: {'id' : id,'checked' : checked, action : 'checkbox_edit' },
        					  timeout: 1000000,
        		              error: function(e) {
        		              },
        		              success: function(data) {
        		            	  data = jQuery.parseJSON(data);
        		            	  if (data.Code!=null){
            		            	  if (data.Code==-1){
            		            		 helper_functions.displayError("#error", data.Content);
            		            	  }
        		            	  }
        		              }
        					});
        				
        				},
        			onAfterEdit:function(node){
        				var text = node.text;
        				var id = node.id;
        				jQuery.ajax({
        					  type: 'POST',
        					  url:  properties.getPath() +'create_view_edit_handler',
        					  data: {'id' : id, 'text' : text, action : 'name_edit'},
        					  timeout: 1000000,
        		              error: function(e) {
        		              },
        		              success: function(data) {
        		            	  data = jQuery.parseJSON(data);
        		            	  if (data.Code!=null){
            		            	  if (data.Code==-1){
            		            		 helper_functions.displayError("#error", data.Content);
            		            	  }
        		            	  }
        		              }
        					});
        				}
                });  
          		
            	
      		}
		}
		
       	return false;	        	
 	
 	 });
 	 
});
