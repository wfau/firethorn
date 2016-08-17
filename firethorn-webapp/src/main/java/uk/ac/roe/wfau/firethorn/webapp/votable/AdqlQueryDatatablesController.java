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

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlQueryLinkFactory;

/**
 * Spring MVC controller to format the results of an {@link AdqlQuery} as a <a href='http://datatables.net/index'>DataTable</a>.
 * @see <a href='http://datatables.net/index'>DataTables</a>
 * @deprecated Use the formatter on the query results themselves.
 * 
 */
@Slf4j
@Deprecated
@Controller
@RequestMapping(AdqlQueryLinkFactory.DATATABLE_PATH)
public class AdqlQueryDatatablesController
    extends AbstractController
    {

    public interface FieldHandler
        {
        public void write(final PrintWriter writer)
        throws SQLException;
        }

    public static abstract class BaseFieldHandler
    implements FieldHandler
        {
        public BaseFieldHandler(final ResultSet resultset, final ResultSetMetaData resultmeta, final int colnum)
            {
            this.resultset = resultset ;
            this.resultmeta = resultmeta ;
            this.colnum = colnum + 1 ;
            }
        protected ResultSet resultset ;
        protected ResultSetMetaData resultmeta ;
        protected int colnum ;

        protected abstract String fetch()
        throws SQLException;

        public void write(final PrintWriter writer)
        throws SQLException
            {
  
            writer.write(
                this.fetch()
                );
            }
        }

    public static class ObjectFieldHandler
    extends BaseFieldHandler
        {
        public ObjectFieldHandler(final ResultSet resultset, final ResultSetMetaData resultmeta, final int colnum)
            {
            super(
                resultset,
                resultmeta,
                colnum
                );
            }
        public String fetch()
        throws SQLException
            {
	        	if (resultset.getObject(colnum)!=null){
	   			    return resultset.getObject(colnum).toString();
		   		} else {
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
    extends BaseFieldHandler
        {
        public StringFieldHandler(final ResultSet resultset, final ResultSetMetaData resultmeta, final int colnum)
            {
            super(
                resultset,
                resultmeta,
                colnum
                );
            }
        public String fetch()
        throws SQLException
            {
            return '"' +  resultset.getString(colnum) + '"';
                
            }
        }

    /**
     * Consistent format for dates.
     *
     */
    public static class DateFieldHandler
    extends BaseFieldHandler
        {
        public DateFieldHandler(final ResultSet resultset, final ResultSetMetaData resultmeta, final int colnum)
            {
            super(
                resultset,
                resultmeta,
                colnum
                );
            }
        public String fetch()
        throws SQLException
            {
            DateTime value = new DateTime(
                resultset.getDate(
                    colnum
                    )
                );
            return value.toString();
            }
        }
    
    @Override
    public Path path()
        {
        return path(
            AdqlQueryLinkFactory.DATATABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlQueryDatatablesController()
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



    public String select(final AdqlTable table, final JdbcProductType type)
        {
        final StringBuilder builder = new StringBuilder();

        builder.append(
            "SELECT"
            );

        String glue = " ";
        for (final AdqlColumn column : table.columns().select())
            {
            log.debug("Column [{}][{}]", column.ident(), column.name());
            builder.append(
                glue
                );
            glue = ", ";
            select(
                builder,
                column,
                type
                );
            }
        builder.append(
            " FROM "
            );
        builder.append(
            table.root().namebuilder()
            );
        /*
         * Only if we have added a rownum column.
        builder.append(
            " ORDER BY 1"
            );
         */
        log.debug("Datatable SQL [{}]", builder.toString());
        return builder.toString();
        }

    public void select(final StringBuilder builder, final AdqlColumn column, final JdbcProductType type)
        {
    	log.debug("select(StringBuilder, AdqlColumn, JdbcProductType)");
        //
        // Postgresql dialect
        if (type == JdbcProductType.PGSQL)
            {
            builder.append('"');
            builder.append(
                column.root().name()
                );
            builder.append('"');
            }
        //
        // SQLServer dialect
        // http://technet.microsoft.com/en-us/library/ms174450.aspx
        else if (type == JdbcProductType.MSSQL)
            {
            builder.append(
                column.root().name()
                );
            }
        //
        // Generic SQL dialect
        else {
            builder.append(
                column.root().name()
                );
            }
        builder.append(
            " AS "
            );
        builder.append(
            column.name()
            );
        }

    
    /**
     * Generate a VOTable from query results.
     *
     */
    public void generateDatatable(
        final PrintWriter writer,
        final AdqlQuery   query
        ){
    	
        final AdqlTable table= query.results().adql();
        final JdbcTable jdbc = query.results().jdbc();

      
                writer.append("[[");

                List<AdqlColumn> cols = new ArrayList<AdqlColumn>();
                AdqlColumn column;
                Iterator<AdqlColumn> itemIterator = table.columns().select().iterator();
				while (itemIterator.hasNext()) {

					  column = itemIterator.next();
					  cols.add(column);

					  if (column.name() != null){
						  writer.append('"' + column.name() + '"');
					  }
               
					  if (itemIterator.hasNext()) {
						   writer.append(",");

					  }
                }
              
                	writer.append("],[");
                    final JdbcProductType type  = jdbc.resource().connection().type();
                    final Connection connection = jdbc.resource().connection().open();
                    try {
                        //
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

                        final ResultSetMetaData colmeta = results.getMetaData();
                        final int colcount = colmeta.getColumnCount();

                        final List<FieldHandler> handlers = new ArrayList<FieldHandler>();
                        for (int colnum = 0 ; colnum < colcount ; colnum++)
                            {
                            AdqlColumn adql = cols.get(colnum);
                            switch(adql.meta().adql().type())
                                {
                                case CHAR :
                                case UNICODE:
                                    handlers.add(
                                        new StringFieldHandler(
                                            results,
                                            colmeta,
                                            colnum
                                            ) 
                                        );
                                    break ;

                                default :
                                    handlers.add(
                                        new ObjectFieldHandler(
                                            results,
                                            colmeta,
                                            colnum
                                            ) 
                                        );
                                    break ;
                                }

                            }
                        
                        FieldHandler handler;
                       
                        while (results.next())
                            {
                            writer.append("{");
                            Iterator<FieldHandler> handlerIterator = handlers.iterator();
                            Iterator<AdqlColumn> colterator = cols.iterator();

            				while (handlerIterator.hasNext()) {

            					 if (colterator.hasNext()) {
	    							  column = colterator.next();

	    							  if (column.name() != null){
	    								  writer.append('"' + column.name() + '"');
	    								  writer.append(":");
	    							  }
            					 }
            		                
            					
            					

            					  handler = handlerIterator.next();
            					  handler.write(
                                     writer
                                  );

            					  if (handlerIterator.hasNext()) {
            						   writer.append(",");

            					  }
}
                            
                            writer.append("}");
                            if (!results.isLast()){
                                writer.append(",");
                            }
                        }

    			}	catch (final SQLException ouch)
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
                                }
                            catch (final SQLException ouch)
                                {
                                log.error("Exception closing SQL connection [{}]", ouch.getMessage());
                                }
                            }
                        }

                    writer.append("]]");
  
    }
    
    
    /**
     * Datatable GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET)
    public void datatable(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident,
        final HttpServletResponse response
        ) throws  IdentifierNotFoundException, IOException {

        response.setCharacterEncoding(
            "UTF-8"
            );
        
		

        final PrintWriter writer = response.getWriter();
       

        final AdqlQuery query = factories().adql().greens().entities().select(
            factories().adql().greens().idents().ident(
                ident
                )
            );		
        
		generateDatatable(writer,query);
        
        }
    }
