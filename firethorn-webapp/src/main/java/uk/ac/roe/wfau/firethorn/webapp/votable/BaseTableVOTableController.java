/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.webapp.votable;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery.ResultState;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 * Spring MVC controller to format a {@link BaseTable} as a <a href='http://www.ivoa.net/documents/VOTable/'>VOTable</a>.
 * Based on the VOTable-1.3 specification.
 * @see <a href='http://www.ivoa.net/documents/VOTable/'>IVOA VOTable</a>
 * @see <a href='http://www.ivoa.net/documents/VOTable/20130920/index.html'></a>
 *
 */
public abstract class BaseTableVOTableController
extends AbstractTableController
    {
    /**
     * Public constructor.
     *
     */
    public BaseTableVOTableController()
        {
        super();
        }

    /**
     * VOTable MIME type, {@value}.
     *
     */
    public static final String VOTABLE_MIME = "application/x-votable+xml" ;

    /**
     * TextXml MIME type, {@value}.
     *
     */
    public static final String TEXT_XML_MIME = "text/xml" ;

    /**
     * A {@link FieldFormatter} to escape {@link String} values using XML entities.
     * @see StringEscapeUtils
     * @see StringEscapeUtils#escapeXml(String)
     *
     */
    public static class XmlStringFormatter
    extends AbstractFormatter
        {
        /**
         * Public constructor.
         * @param column The {@link BaseColumn} this {@link FieldFormatter} will handle.
         *
         */
        public XmlStringFormatter(final BaseColumn<?> column)
            {
            super(
                column
                );
            }

        @Override
        public String format(final ResultSet results)
        throws SQLException
            {
            return StringEscapeUtils.escapeXml(
                results.getString(
                    index()
                    )
                );
            }
        }

    /**
     * A {@link FieldFormatter} to format {@link DateTime} values using ISO8601.
     * @see ISODateTimeFormat#dateTime()
     *
     */
    public static class XmlDateTimeFormatter
    extends AbstractFormatter
        {
        /**
         * Public constructor.
         * @param column The {@link BaseColumn} this {@link FieldFormatter} will handle.
         *
         */
        public XmlDateTimeFormatter(final BaseColumn<?> column)
            {
            super(
                column
                );
            }

        /**
         * Our {@link DateTimeFormatter}.
         * 
         */
        private static final DateTimeFormatter iso = ISODateTimeFormat.dateTime();
        @Override
        public String format(final ResultSet results)
        throws SQLException
            {
            return iso.print(
                new DateTime(
                    results.getDate(
                            index()
                        )
                    )
                );
            }
        }

    public static class ByteFormatter
    extends AbstractFormatter
        {
        public ByteFormatter(final BaseColumn<?> column)
            {
            super(
                column
                );
            }

        @Override
        public String format(final ResultSet results)
        throws SQLException, ProtectionException
            {
                String byteArrayString = "";
                int aux;
	            final StringBuilder builder = new StringBuilder(); 
                
                if (results.getObject(index())!=null){
                	
                   
            		boolean isarray = column.meta().adql().type().isarray();
            		int size =  column.meta().adql().arraysize();
			

					if (!isarray && size<=0) {
						byte[] bytes = results.getBytes(
					            index()
					        );
						
						int intval = java.nio.ByteBuffer.wrap(bytes).getInt();

					    builder.append(intval);       
							
					} else {
					    
					    byte[] bytes = results.getBytes(
					        index()
					    );
					    
					    for (byte b : bytes) {
					        if(b<0) aux=256+b; else aux=b; 
					            builder.append(aux  + " ");
					    } 
						  
					}
				
                }

                byteArrayString = builder.toString();
                return byteArrayString.trim();
            }
        }
    
    
    public static class BitFormatter
    extends AbstractFormatter
        {
        public BitFormatter(final BaseColumn<?> column)
            {
            super(
                column
                );
            }

        @Override
        public String format(final ResultSet results)
        throws SQLException
            {
	    		if (results.getObject(index())!=null){
	    			return  results.getString(index());
	    		} else {
	    			return "";
	    		}
	        	
            }
        }
    
    @Override
    public void head(final PrintWriter writer, final BaseTable<?,?> table)
    throws ProtectionException
        {
        writer.append("<?xml version='1.0' encoding='UTF-8'?>");
        writer.append("<VOTABLE");
        writer.append(" xmlns='http://www.ivoa.net/xml/VOTable/v1.3'");
        writer.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
        writer.append(" xsi:schemaLocation='http://www.ivoa.net/xml/VOTable/v1.3 http://www.ivoa.net/xml/VOTable/v1.3'");
        writer.append(" version='1.3'");
        writer.append(">");
        
        //
        // TODO Add the table query description and ADQL statement if available.
        
        writer.append("<RESOURCE type='results'");
        //writer.append(" ID='table.");
        //writer.append(table.ident().toString());
        //writer.append("'");
        //if (table.name() != null)
        //    {
        //    writer.append(" name='");
        //    writer.append(table.name());
        //    writer.append("'");
        //    }
        writer.append(">");

 
        if (table.bluequery()!=null){
	        if (!table.bluequery().results().state().equals(ResultState.NONE) && !table.bluequery().results().state().equals(ResultState.EMPTY)) {
	            writer.append("<INFO name='QUERY_STATUS' value='OK'>");
	        } else  {
	            writer.append("<INFO name='QUERY_STATUS' value='ERROR'>");
	            //writer.append(table.greenquery().syntax().friendly());
	        }
	        writer.append("</INFO>");
	        
	        // if (table.bluequery().input() != null)
	        //{
	        //	writer.append("<INFO name='QUERY' value='" + table.query().input() + "' />");
	        // }
        } else {
        	writer.append("<INFO name='QUERY_STATUS' value='OK'></INFO>");
        }
    
        writer.append("<INFO");
	writer.append(" name='link'");
        writer.append(" value='");
        writer.append(table.link());
        writer.append("'");
        writer.append("/>");	
     
        if (table.text() != null)
            {
            writer.append("<DESCRIPTION>");
            writer.append("<![CDATA[");
            writer.append(table.text());
            writer.append("]]>");
            writer.append("</DESCRIPTION>");
            }

        writer.append("<TABLE ID='table.");
        writer.append(table.ident().toString());
        writer.append("'");
        if (table.name() != null)
            {
            writer.append(" name='");
            writer.append(table.name());
            writer.append("'");
            }
        writer.append(">");

        for (final BaseColumn<?> column : table.columns().select())
            {
            field(
                writer,
                column
                );
            }

        writer.append("<DATA>");
        writer.append("<TABLEDATA>");
      
        
        }            

    /**
     * Write a the VOTable FIELD metadata for {@link BaseColumn} to a {@link PrintWriter}
     * @param writer  The {@link PrintWriter} to write the field to.
     * @param columnn The {@link BaseColumn} to write the FIELD metadata for.
     * @throws ProtectionException 
     *
     */
    protected void field(final PrintWriter writer, final BaseColumn<?> column)
    throws ProtectionException
        {
        writer.append("<FIELD ID='column.");
            writer.append(column.ident().toString());
            writer.append("'");

        if (column.name() != null)
            {
            writer.append(" name='");
            writer.append(column.name());
            writer.append("'");
            }

        if (column.meta().adql().type() != null)
            {

            writer.append(" datatype='");
            writer.append(column.meta().adql().type().votype());
            writer.append("'");

            if (column.meta().adql().arraysize() != null)
                {
            	
            	// Changed AdqlColumn.NON_ARRAY_SIZE and AdqlColumn.VAR_ARRAY_SIZE to 0 and 1, comparison was not working correctly
                if (column.meta().adql().arraysize()==0)
                    {
                    }
                else if (column.meta().adql().arraysize()==-1)
                    {
                    writer.append(" arraysize='*'");
                    }
                else {
                    writer.append(" arraysize='");
                    writer.append(column.meta().adql().arraysize().toString());
                    writer.append("'");
                }
            }
                
            if (column.meta().adql() != null)
                {
                    if (column.meta().adql().type().xtype() != null) {
	                    writer.append(" xtype='");
	                    writer.append(column.meta().adql().type().xtype());
	                    writer.append("'");
	                }
                }
            }

        if (column.meta().adql().units() != null)
            {
            writer.append(" unit='");
            writer.append(column.meta().adql().units());
            writer.append("'");
            }

        if (column.meta().adql().ucd() != null)
            {
            writer.append(" ucd='");
            writer.append(column.meta().adql().ucd());
            writer.append("'");
            }

        if (column.meta().adql().utype() != null)
            {
            writer.append(" utype='");
            writer.append(column.meta().adql().utype());
            writer.append("'");
            }

        writer.append(">");

        writer.append("<LINK");
        writer.append(" content-type='" + JSON_MIME + "'");
        writer.append(" content-role='metadata'");
        writer.append(" href='" + column.link() + "'");
        writer.append("/>");

        if (column.text() != null)
            {
            writer.append("<DESCRIPTION>");
            writer.append("<![CDATA[");
            writer.append(column.text());
            writer.append("]]>");
            writer.append("</DESCRIPTION>");
            }

        writer.append("</FIELD>");
        }

    @Override
    public void row(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException, ProtectionException
        {
        writer.append("<TR>");
        cells(
            formatters,
            writer,
            results
            );
        writer.append("</TR>");
        }

    @Override
    public void cell(final FieldFormatter formatter, final PrintWriter writer, final ResultSet results)
    throws SQLException, ProtectionException
        {
    	String content =  formatter.format(results);
        writer.append("<TD>");
        if (content!=null){
        	content = content.trim();
            writer.append(content);
        }
        writer.append("</TD>");
        }
    
    @Override
    public void foot(final PrintWriter writer, final BaseTable<?,?> table)
    throws ProtectionException
        {
        writer.append("</TABLEDATA>");
        writer.append("</DATA>");
        writer.append("</TABLE>");
        if (table.bluequery()!=null){
      	  
        	if (table.bluequery().results().state().equals(ResultState.TRUNCATED)){
		        writer.append("<INFO name=\"QUERY_STATUS\" value=\"OVERFLOW\" />");
	        }
	     
        }
        writer.append("</RESOURCE>");
        writer.append("</VOTABLE>");
        }

    @Override
    public FieldFormatter formatter(final BaseColumn<?> column)
    throws ProtectionException
        {
        switch(column.meta().adql().type())
            {
            case CHAR :
            case UNICODE:
                return new XmlStringFormatter(
                    column
                    );
            case BYTE:
                return new ByteFormatter(
                    column
                    );
            case BIT:
                return new BitFormatter(
                    column
                    );
            case DATE :
            case TIME:
            case DATETIME :
                return new XmlDateTimeFormatter(
                    column
                    );
    
            default :
                return new SimpleFormatter(
                    column
                    );
            }
        }
    }

