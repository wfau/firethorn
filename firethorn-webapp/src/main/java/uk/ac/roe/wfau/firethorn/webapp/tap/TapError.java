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

import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryVOTableController.FieldHandler;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryVOTableController.ObjectFieldHandler;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryVOTableController.StringFieldHandler;
 
public class TapError {
	 
	 
		String message;
		
		public String getMessage() {
			return message;
		}
	 
		@XmlElement
		public void setMessage(String message) {
			this.message = message;
		}
	
		
		public TapError(String message) {
			this.message = message;
		
		}
	 
		public TapError() {
		}

		
		
		
		/**
		 * Write Error message to String in VOTable format
		 * @param errorMessage
		 * @param writer
		 */
		public static String writeErrorToVotable (String errorMessage){
			
				StringBuilder writer = new StringBuilder();

		        // Based on VOTable-1.3 specification.
		        // http://www.ivoa.net/documents/VOTable/20130315/PR-VOTable-1.3-20130315.html
				
				writer.append("<VOTABLE ");
				writer.append("version=\"1.2\" ");
				writer.append("xmlns=\"http://www.ivoa.net/xml/VOTable/v1.2\" ");
				writer.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
				writer.append("xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.2 http://www.ivoa.net/xml/VOTable/v1.2\">");
				//writer.append("<VOTABLE version=\"1.2\" xmlns=\"http://www.ivoa.net/xml/VOTable/v1.2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.2 http://vo.ari.uni-heidelberg.de/docs/schemata/VOTable-1.2.xsd\">");
		            writer.append("<RESOURCE type='results'>");
			            writer.append("<INFO");
			            writer.append(" name='QUERY_STATUS'");
			            writer.append(" value='ERROR' >");
			            writer.append(errorMessage);
			            writer.append("</INFO>");
			            
		            
		            writer.append("</RESOURCE>");
		        writer.append("</VOTABLE>");
		    	
		        return writer.toString();
			
		}
		
		
		/**
		 * Write Error message to PrintWriter in VOTable format
		 * @param errorMessage
		 * @param writer
		 */
		public static void writeErrorToVotable (String errorMessage, PrintWriter writer){
			
			
			// Based on VOTable-1.3 specification.
			// http://www.ivoa.net/documents/VOTable/20130315/PR-VOTable-1.3-20130315.html
			
			writer.append("<VOTABLE ");
			writer.append("version=\"1.2\" ");
			writer.append("xmlns=\"http://www.ivoa.net/xml/VOTable/v1.2\" ");
			writer.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
			writer.append("xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.2 http://www.ivoa.net/xml/VOTable/v1.2\">");
			writer.append("<RESOURCE type='results'>");
			    writer.append("<INFO");
			    writer.append(" name='QUERY_STATUS'");
			    writer.append(" value='ERROR' >");
			    writer.append(errorMessage);
			    writer.append("</INFO>");
			    
			
			writer.append("</RESOURCE>");
			writer.append("</VOTABLE>");

			
		}
	 
	
}
