package uk.ac.roe.wfau.firethorn.webapp.tap;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryVOTableController.FieldHandler;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryVOTableController.ObjectFieldHandler;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryVOTableController.StringFieldHandler;
 
public class XMLResponse {
	 
	 
		String name;
		
		public String getName() {
			return name;
		}
	 
		@XmlElement
		public void setName(String name) {
			this.name = name;
		}
	
		
		public XMLResponse(String name) {
			this.name = name;
		
		}
	 
		public XMLResponse() {
		}
		
		public static void writeErrorToVotable (String errorMessage, PrintWriter writer){
			
		    	

		        // Based on VOTable-1.3 specification.
		        // http://www.ivoa.net/documents/VOTable/20130315/PR-VOTable-1.3-20130315.html

		        writer.append("<?xml version='1.0' encoding='UTF-8'?>");
		        writer.append("<vot:VOTABLE");
		        writer.append(" xmlns:vot='http://www.ivoa.net/xml/VOTable/v1.3'");
		        writer.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
		        writer.append(" xsi:schemaLocation='http://www.ivoa.net/xml/VOTable/v1.3 http://www.ivoa.net/xml/VOTable/v1.3'");
		        writer.append(" version='1.3'");
		        writer.append(">");

		            writer.append("<RESOURCE");
		            writer.append(" type='results'");
		            writer.append(">");
			            writer.append("<INFO");
			            writer.append(" name='QUERY_STATUS'");
			            writer.append(" value='ERROR' >");
			            writer.append(errorMessage);
			            writer.append("</INFO>");
			            
		            
		            writer.append("</RESOURCE>");
		        writer.append("</vot:VOTABLE>");
		    	
		  
			
		}
	 
	
}
