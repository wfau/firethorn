/*
 * WFAU Web2.0 Project
 * Author: Stelios Voutsinas
 * Date: July 2012
 * 
 * Custom additions to the TableTools Libary
 * 
 * 
 */


var pathname_divId = "#temp_tbl_path"
/*
 * Get Filtered Data from (oSettings) object
 * 
 */
jQuery.fn.dataTableExt.oApi.fnGetFilteredData = function ( oSettings ) {
    var a = [];
    for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ) {
        a.push(oSettings.aoData[ oSettings.aiDisplay[i] ]._aData);
    }
    return a;
}

/**
 * Get Hidden Nodes from (oSettings) object
 * 
 */
jQuery.fn.dataTableExt.oApi.fnGetHiddenNodes = function ( oSettings )
{
    /* Note the use of a DataTables 'private' function thought the 'oApi' object */
    var anNodes = this.oApi._fnGetTrNodes( oSettings );
    var anDisplay = $('tbody tr', oSettings.nTable);
     
    /* Remove nodes which are being displayed */
    for ( var i=0 ; i<anDisplay.length ; i++ )
    {
        var iIndex = jQuery.inArray( anDisplay[i], anNodes );
        if ( iIndex != -1 )
        {
            anNodes.splice( iIndex, 1 );
        }
    }
     
    /* Fire back the array to the caller */
    return anNodes;
}

/**
 * Function: fnGetDisplayNodes
 * Purpose:  Return an array with the TR nodes used for displaying the table
 * Returns:  array node: TR elements
 *           or
 *           node (if iRow specified)
 * Inputs:   object:oSettings - automatically added by DataTables
 *           int:iRow - optional - if present then the array returned will be the node for
 *             the row with the index 'iRow'
 */
jQuery.fn.dataTableExt.oApi.fnGetDisplayNodes = function ( oSettings, iRow )
{
    var anRows = [];
    if ( oSettings.aiDisplay.length !== 0 )
    {
        if ( typeof iRow != 'undefined' )
        {
            return oSettings.aoData[ oSettings.aiDisplay[iRow] ].nTr;
        }
        else
        {
            for ( var j=oSettings._iDisplayStart ; j<oSettings._iDisplayEnd ; j++ )
            {
                var nRow = oSettings.aoData[ oSettings.aiDisplay[j] ].nTr;
                anRows.push( nRow );
            }
        }
    }
    return anRows;
};

/**
 * Generate the appropriate URL to access the current filtered data for use by the table viewer
 * 
 */
function fnSaveAsGetURLforViewer (save_type, oTable, pathname, base_url, x_expr, y_expr) { 
	var sData = oTable.fnSettings();
	sData['save_as'] = save_type;
	var params = oTable.oApi._fnAjaxParameters(sData);
	var pathname = jQuery(pathname).val();
	params.push({'name' : 'pathname', 'value' : pathname}); 
	if (x_expr!="" && y_expr!=""){
		var get_url = properties.getBaseURL() + '/' + 'data_tables_processing' + '?' + jQuery.param(params) + "&x_expr=" + encodeURIComponent(x_expr) + "&y_expr=" + encodeURIComponent(y_expr);
	} else {
		var get_url = properties.getBaseURL() + '/' + 'data_tables_processing' + '?' + jQuery.param(params);
			
	}
	sData['save_as'] = "";
	return get_url;
	
}

/**
 * Generate the appropriate URL to access the current filtered data
 * 
 */
function fnSaveAsGetURL (save_type, oTable, pathname, base_url) { 
	var sData = oTable.fnSettings();
	sData['save_as'] = save_type;
	var params = oTable.oApi._fnAjaxParameters(sData);
	var pathname = jQuery(pathname).val();
	params.push({'name' : 'pathname', 'value' : pathname}); 
	var get_url = properties.getBaseURL() + '/' + 'data_tables_processing' + '?' + jQuery.param(params);
	sData['save_as'] = "";
	return get_url;
	
}


/**
 * Custom Save as function
 * 
 */
function fnSaveAsCustom (save_type, oTable, pathname){
	var sData = oTable.fnSettings();
	sData['save_as'] = save_type;
	var params = oTable.oApi._fnAjaxParameters(sData);
	var pathname = jQuery(pathname).val();
	params.push({'name' : 'pathname', 'value' : pathname});
		var aoPost = params;
   
 
        /* Create an IFrame to do the request */
    nIFrame = document.createElement('iframe');
    nIFrame.setAttribute( 'id', 'RemotingIFrame' );
    nIFrame.style.border='0px';
    nIFrame.style.width='0px';
    nIFrame.style.height='0px';
         
    document.body.appendChild( nIFrame );
    var nContentWindow = nIFrame.contentWindow;
    nContentWindow.document.open();
    nContentWindow.document.close();
     
    var nForm = nContentWindow.document.createElement( 'form' );
    nForm.setAttribute( 'method', 'post' );
     
    /* Add POST data */
    for ( var i=0 ; i<aoPost.length ; i++ )
    {
        nInput = nContentWindow.document.createElement( 'input' );
        nInput.setAttribute( 'name', aoPost[i].name );
        nInput.setAttribute( 'type', 'text' );
        nInput.value = aoPost[i].value;
        nForm.appendChild( nInput );
    }
     
    nForm.setAttribute( 'action', properties.getBaseURL() + '/data_tables_processing');
     
    /* Add the form and the iframe */
    nContentWindow.document.body.appendChild( nForm );
 
   
    /* Send the request */
    nForm.submit();

	sData['save_as'] = "";
}	

/**
 * Reset all the table filters
 * 
 */  
function fnResetAllFilters(oTable) {
    var oSettings = oTable.fnSettings();
    for(iCol = 0; iCol < oSettings.aoPreSearchCols.length; iCol++) {
        oSettings.aoPreSearchCols[ iCol ].sSearch = '';
    }
    oSettings.oPreviousSearch.sSearch = '';
    oTable.fnDraw();
}
  
  
/**
 * Get the rows which are currently selected through math expression evaluation 
 * 
 */
function fnGetSelectedEval( oTableLocal, x_axis, y_axis, cols ) {
    	
    	var aReturn = new Array();
    	var aTrs = oTableLocal.fnGetNodes();
    	var colNames = [];
		var x_value = "";
		var y_value = "";
		
    	for (var i = 0; i<cols.length; i++){
    		colNames.push(cols[i].sTitle.toLowerCase());
    	}
    	
    	for ( var i=0 ; i<aTrs.length ; i++ ) {
    		if ( jQuery(aTrs[i]).hasClass('row_selected') ) {
        		for (var x=0; x<colNames.length;x++){
        			window[colNames[x].toLowerCase()] = parseFloat(aTrs[i].children[x].innerHTML.trim());	
	        		
        		}
        		x_value = eval(x_axis.toLowerCase()).toString();
        		y_value = eval(y_axis.toLowerCase()).toString();
        		aReturn.push(x_value);
        		aReturn.push(y_value);
    		}
    	}
    	return aReturn;
    	
    }
    
/**
 * Get the rows which are currently selected 
 *
 */
function fnGetSelected( oTableLocal, col_x_index, col_y_index ) {
	
	var aReturn = new Array();
	var aTrs = oTableLocal.fnGetNodes();
	
	if (col_x_index<0 || col_y_index<0){
		throw "Index less than zero provided";
	}
	for ( var i=0 ; i<aTrs.length ; i++ ) {
		if ( jQuery(aTrs[i]).hasClass('row_selected') ) {
			aReturn.push( aTrs[i].children[col_x_index].innerHTML.trim() );
			aReturn.push( aTrs[i].children[col_y_index].innerHTML.trim() );        		
		}
	}
	
	return aReturn;
}

/**
 * Custom Filter for multiple columns
 * 
 */
jQuery.fn.dataTableExt.oApi.fnMultiEvalFilter = function( oSettings, oData ) {


    var axis_strings = "";
    
	for ( var key in oData ) {
        if ( oData.hasOwnProperty(key) ) {
			if (oData[key].indexOf('|')>0){
				if (oData[key]!="" && oData[key]!=null){
					axis_strings += oData[key] + '_'
				}
			} else {
				if (oData[key]!="" && oData[key]!=null){
        			axis_strings += oData[key] + '|';
				}
			}    
        }
    }
	
	if (axis_strings!=""){
    	axis_strings = axis_strings.slice(0,axis_strings.length-1);
	}
	
    for ( var key in oData ) {
        if ( oData.hasOwnProperty(key) ) {
        	var expr = Parser.parse(key);
       		var variables = expr.variables();
        	for ( var i=0, iLen=oSettings.aoColumns.length ; i<iLen ; i++ ) {
        		for ( var x =0;x<variables.length; x++ ) {
	        	   if ( oSettings.aoColumns[i].sTitle.toLowerCase() == variables[x].toLowerCase() ) {
	        		   oSettings.aoPreSearchCols[ i ].sSearch = axis_strings;
	                     break;
	      		   }
        		}
            }
			     
       	}
    }
    


	
	this.oApi._fnDraw( oSettings );
}

/**
 * Custom Filter for multiple columns
 * 
 */
jQuery.fn.dataTableExt.oApi.fnMultiFilter = function( oSettings, oData ) {
  
	for ( var key in oData ) {
        if ( oData.hasOwnProperty(key) ) {
            for ( var i=0, iLen=oSettings.aoColumns.length ; i<iLen ; i++ ) {
            	
                if( oSettings.aoColumns[i].sTitle.toLowerCase() == key.toLowerCase() ) {
                	
                    /* Add single column filter */
                    oSettings.aoPreSearchCols[ i ].sSearch = oData[key];
                    break;
                }
            }
        }
    }
    this.oApi._fnDraw( oSettings );
}

