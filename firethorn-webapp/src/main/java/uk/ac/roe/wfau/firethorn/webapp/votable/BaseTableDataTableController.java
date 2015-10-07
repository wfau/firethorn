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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.webapp.votable.AbstractTableController.FieldFormatter;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryDatatablesController.BaseFieldHandler;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryDatatablesController.FieldHandler;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryDatatablesController.StringFieldHandler;

/**
 * Spring MVC Controller to generate a Datatable response for a table.
 * 
 * Based on the VOTable-1.3 specification.
 * http://www.ivoa.net/documents/VOTable/20130315/PR-VOTable-1.3-20130315.html
 *
 */
@Slf4j
public abstract class BaseTableDataTableController
extends AbstractTableController
    {
    /**
     * Public constructor.
     *
     */
    public BaseTableDataTableController()
        {
        super();
        }

    /**
     * VOTable MIME type.
     *
     */
    public static final String VOTABLE_MIME = "application/x-votable+xml" ;

    /**
     * TextXml MIME type.
     *
     */
    public static final String TEXT_XML_MIME = "text/xml" ;

    /**
     * Escape string values for XML.
     * http://commons.apache.org/proper/commons-lang/javadocs/api-release/org/apache/commons/lang3/StringEscapeUtils.html
     * http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/StringEscapeUtils.html#escapeXml%28java.lang.String%29
     *
     */
    public static class DatatableFormatter
    extends AbstractFormatter
        {
        public DatatableFormatter(final BaseColumn<?> column)
            {
            super(
                column
                );
            }

        @Override
        public String format(final ResultSet results)
        throws SQLException
            {
    		log.debug("***DatatableFormatter****");

        	if (results.getObject(index())!=null){
        		log.debug("***1****");

        		return StringEscapeUtils.escapeJson(
                        results.getString(
                            index()
                            )
                        );
	   		} else {
	   			log.debug("***2****");

	   			return "\"\"";
	   		}
            
            }
        }
    
    /**
     * Escape string values for XML.
     * http://commons.apache.org/proper/commons-lang/javadocs/api-release/org/apache/commons/lang3/StringEscapeUtils.html
     * http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/StringEscapeUtils.html#escapeXml%28java.lang.String%29
     *
     */
    public static class StringFieldHandler
    extends AbstractFormatter
        {
        public StringFieldHandler(final BaseColumn<?> column)
            {
        	super(
                column
               );
            }
        @Override
        public String format(final ResultSet results)
        throws SQLException
            {
        	    return '"' +  StringEscapeUtils.escapeXml(results.getString(index())) + '"';
            
            }
     
        }
    /**
     * ISO8601 format for dates.
     *
     */
    public static class XmlDateTimeFormatter
    extends AbstractFormatter
        {
        public XmlDateTimeFormatter(final BaseColumn<?> column)
            {
            super(
                column
                );
            }

        protected static final DateTimeFormatter iso = ISODateTimeFormat.dateTime();

        @Override
        public String format(final ResultSet results)
        throws SQLException
            {
            return '"' + iso.print(
                new DateTime(
                    results.getDate(
                    		index()
                        )
                    )
                ) + '"';
            }
        }

    /**
     * Generate the table header.
     * 
     */
    @Override
    public void head(final PrintWriter writer, final BaseTable<?,?> table)
        {
    	
    		writer.append("[[");
    		AdqlColumn column;
			Iterator<?> itemIterator = table.columns().select().iterator();
			while (itemIterator.hasNext()) {
		
				  column = (AdqlColumn) itemIterator.next();
				  field(
		             writer,
		             column
		             );
				  if (itemIterator.hasNext()) {
					   writer.append(",");
		
				  }
			}

	       	 writer.append("],[");
        }            
        
    /**
     * Generate the metadata for a field.
     * 
     */
    public void field(final PrintWriter writer, final BaseColumn<?> column)
        {
    	 writer.append('"' + column.name() + '"');
        }

    /**
     * Generate a table row.
     * 
     */
    @Override
    public void row(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException
        {
    	
        writer.append("{");
        cells(
            formatters,
            writer,
            results
            );
    
        writer.append("}");
       
    
        }
    
    
    /**
     * Generate the table cells.
     * 
     */
    @Override
    public void cells(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException
        {
    	int size = 1;
    	int maxlen = formatters.size();
    	for (final FieldFormatter formatter : formatters)
            {
    			writer.append('"' + formatter.name() + '"' + " : ");
	            cell(
	                formatter,
	                writer,
	                results
	                );
	            size++;
	            
		        if (size<=maxlen){
		            writer.append(",");
		        }
            }
        	
        }
    
    /**
     * Generate the table rows.
     * 
     */
    @Override
    public void rows(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException
        {
        while (results.next())
            {
	            row(
	                formatters,
	                writer,
	                results
	                );
	            
	            if (!results.isLast()){
	                writer.append(",");
	            }
            }
        }

    /**
     * Generate a table cell.
     * 
     */
    @Override
    public void cell(final FieldFormatter formatter, final PrintWriter writer, final ResultSet results)
    throws SQLException
        {
    	  writer.append(
            formatter.format(
                results
                )
            );
        }
    
    /**
     * Generate the table footer.
     * 
     */
    @Override
    public void foot(final PrintWriter writer, final BaseTable<?,?> table)
        {
    	writer.append("]]");
        }

    /**
     * Select a formatter for a field.
     * 
     */
    @Override
    public FieldFormatter formatter(final BaseColumn<?> column)
        {
        switch(column.meta().adql().type())
            {
            case CHAR :
            case UNICODE:
            	 return new StringFieldHandler(
            			 		column
                              );
              
    
            case DATE :
            case TIME:
            case DATETIME :
                return new XmlDateTimeFormatter(
                    column
                    );
    
            default :
                return new DatatableFormatter(
                    column
                    );
            }
        }
    }
