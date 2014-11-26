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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.mar.FITSWriter;
import net.mar.FormatRS;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.votable.AbstractTableController.FieldFormatter;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryFITSController.BaseFieldHandler;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryFITSController.FieldHandler;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryFITSController.StringFieldHandler;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableOutput;
import uk.ac.starlink.table.StarTableWriter;
import uk.ac.starlink.table.jdbc.SequentialResultSetStarTable;

/**
 * Spring MVC Controller to generate a FITS response for a table.
 * 
 * Based on the VOTable-1.3 specification.
 * http://www.ivoa.net/documents/VOTable/20130315/PR-VOTable-1.3-20130315.html
 *
 */
@Slf4j
public abstract class BaseTableFITSController
extends AbstractTableController
    {
    /**
     * Public constructor.
     *
     */
    public BaseTableFITSController()
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

    OutputStream output = new OutputStream()
    {
        private StringBuilder string = new StringBuilder();
        @Override
        public void write(int b) throws IOException {
            this.string.append((char) b );
        }

        //Netbeans IDE automatically overrides this toString()
        public String toString(){
            return this.string.toString();
        }
    };
    
    void writeTableAsFITS( StarTable table, OutputStream out ) throws IOException {
        StarTableOutput sto = new StarTableOutput();
        StarTableWriter outputHandler = sto.getHandler( "fits" );
        sto.writeStarTable( table, out, outputHandler );
    }

    
    /**
     * Escape string values for XML.
     * http://commons.apache.org/proper/commons-lang/javadocs/api-release/org/apache/commons/lang3/StringEscapeUtils.html
     * http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/StringEscapeUtils.html#escapeXml%28java.lang.String%29
     *
     */
    public static class FITSFormatter
    extends AbstractFormatter
        {
        public FITSFormatter(final BaseColumn<?> column)
            {
            super(
                column
                );
            }

        @Override
        public String format(final ResultSet results)
        throws SQLException
            {
            return StringEscapeUtils.escapeCsv(
                results.getString(
                    index()
                    )
                );
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
        	    return '"' +  StringEscapeUtils.escapeCsv(results.getString(index())).trim() + '"';
            
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
    	
    		
        }            
        
    /**
     * Generate the metadata for a field.
     * 
     */
    public void field(final PrintWriter writer, final BaseColumn<?> column)
        {
    	 writer.append(column.name());
        }

    /**
     * Generate a table row.
     * 
     */
    @Override
    public void row(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException
        {
    	
    
       
    
        }
    
    
    /**
     * Generate the table cells.
     * 
     */
    @Override
    public void cells(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException
        {
    	
        	
        }
    
    /**
     * Generate the table rows.
     * 
     */
    @Override
    public void rows(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException
        {
    	
    
        
        }

    @Override
    public void body(final PrintWriter writer, final BaseTable<?,?> table)
    {
    //
    // If the root table is a JDBC table.
    if (table.root() instanceof JdbcTable)
        {
        final JdbcTable jdbc = (JdbcTable) table.root();

        final JdbcProductType type  = jdbc.resource().connection().type();
        final Connection connection = jdbc.resource().connection().open();

        // int isolation = Connection.TRANSACTION_NONE;

        try {
            //
            // Use isolation level to peek at live data.
            // http://redmine.roe.ac.uk/issues/324
            // http://www.precisejava.com/javaperf/j2ee/JDBC.htm#JDBC107
            // isolation = connection.getTransactionIsolation();
            connection.setTransactionIsolation(
                Connection.TRANSACTION_READ_UNCOMMITTED
                );
            //
            // Use statement params to prevent buffering of large data.
            // http://stackoverflow.com/questions/858836/does-a-resultset-load-all-data-into-memory-or-only-when-requested
            final Statement statement = connection.createStatement(
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY
                );
            statement.setFetchSize(
                100
                );
            final ResultSet results = statement.executeQuery(
                select(
                    table,
                    type
                    )
                );
            ResultSetMetaData rsmd=null;
            if(results.next())
            {
            	rsmd=results.getMetaData();
            }
            FITSWriter fw=null;
            
            FormatRS frs = new FormatRS(); 
            String [] comments=null;
            PrintWriter CSVWriter = null;
            OutputStream CSVOutputStream = null;
            int noCols;
            String [] strArray;
            
            	try {
                fw = new FITSWriter("","");
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            	 File FITSFile = new File("test.fits");
                 if (FITSFile.exists()) {
                     FITSFile.delete();
                 }

                 fw.setFileName("test.fits");
                
            	fw.setRSMD(rsmd);
                fw.setRS(results);
                
        		writer.append(fw.toString());
        		
        		fw.close();
        }

        catch (final SQLException ouch)
            {
            log.error("Exception reading SQL results [{}]", ouch.getMessage());
            }
        catch (final Exception ouch)
            {
            log.error("Exception writing VOTable output [{}]", ouch.getMessage());
            }
        finally {
            if (connection != null)
                {
                try {
                    connection.close();
/*
                    connection.setTransactionIsolation(
                        isolation
                        );
*/
                    }
                catch (final SQLException ouch)
                    {
                    log.error("Exception closing SQL connection [{}]", ouch.getMessage());
                    }
                }
            }
        }
    else {
        log.error("Unable to process root table type [{}]", table.root().getClass().getName());
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
    	 
        }
    
    /**
     * Generate the table footer.
     * 
     */
    @Override
    public void foot(final PrintWriter writer, final BaseTable<?,?> table)
        {
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
                return new SimpleFormatter(
                    column
                    );
            }
        }
    }
