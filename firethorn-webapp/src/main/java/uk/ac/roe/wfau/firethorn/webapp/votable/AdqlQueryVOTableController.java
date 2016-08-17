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
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;
import uk.ac.roe.wfau.firethorn.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlQueryLinkFactory;
import uk.ac.roe.wfau.firethorn.job.Job.Status;

/**
 * Spring MVC controller to format the results of an {@link GreenQuery} as a <a href='http://www.ivoa.net/documents/VOTable/'>IVOA VOTable</a>.
 * <br/>Controller path : [{@value AdqlQueryLinkFactory#VOTABLE_PATH}].
 * @see <a href='http://www.ivoa.net/documents/VOTable/'>IVOA VOTable</a>
 * 
 *
 */
@Slf4j
@Controller
@Deprecated
@RequestMapping(AdqlQueryLinkFactory.VOTABLE_PATH)
public class AdqlQueryVOTableController
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
            writer.write("<TD>");
            writer.write(
                this.fetch()
                );
            writer.write("</TD>");
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
	    			return "";
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
            return StringEscapeUtils.escapeXml(
                resultset.getString(
                    colnum
                    )
                );
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
            final DateTime value = new DateTime(
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
            AdqlQueryLinkFactory.VOTABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlQueryVOTableController()
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
        log.debug("VOTABLE SQL [{}]", builder.toString());
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
    public void generateTAPVotable(
        final PrintWriter writer,
        final GreenQuery   query
        ){
        final AdqlTable table= query.results().adql();
        final JdbcTable jdbc = query.results().jdbc();

        // Based on VOTable-1.3 specification.
        // http://www.ivoa.net/documents/VOTable/20130315/PR-VOTable-1.3-20130315.html

        writer.append("<?xml version='1.0' encoding='UTF-8'?>");
        writer.append("<vot:VOTABLE");
        writer.append(" xmlns:vot='http://www.ivoa.net/xml/VOTable/v1.3'");
        writer.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
        writer.append(" xsi:schemaLocation='http://www.ivoa.net/xml/VOTable/v1.3 http://www.ivoa.net/xml/VOTable/v1.3'");
        writer.append(" version='1.3'");
        writer.append(">");

            writer.append("<RESOURCE type='results'");
            writer.append(" ID='query.");
            writer.append(query.ident().toString());
            writer.append("'");
            if (query.name() != null)
                {
                writer.append(" name='");
                writer.append(query.name());
                writer.append("'");
                }
            writer.append(">");
            writer.append("<LINK");
            writer.append(" content-type='");
            writer.append(JSON_MIME);
            writer.append("'");
            writer.append(" content-role='metadata'");
            writer.append(" href='");
            writer.append(query.link());
            writer.append("'");
            writer.append("/>");
            if (query.status()==Status.COMPLETED)
            {
                writer.append("<INFO name='QUERY_STATUS' value='OK'>");
            } else  {
                writer.append("<INFO name='QUERY_STATUS' value='ERROR'>");
                writer.append(query.syntax().friendly());
            }
            writer.append("</INFO>");
            if (query.input() != null)
            {
	        	writer.append("<INFO name='QUERY' value='" + query.input() + "' />");
            }
            if (query.text() != null)
                {
                writer.append("<DESCRIPTION>");
                writer.append("<![CDATA[");
                writer.append(query.text());
                writer.append("]]>");
                writer.append("</DESCRIPTION>");
                }

                writer.append("<TABLE ID='table.");
                if (table!=null){
	                writer.append(table.ident().toString());
	                writer.append("'");
	                if (table.name() != null)
	                    {
	                    writer.append(" name='");
	                    writer.append(table.name());
	                    writer.append("'");
	                    }
	                writer.append(">");
	                writer.append("<LINK");
	                writer.append(" content-type='");
	                writer.append(JSON_MIME);
	                writer.append("'");
	                writer.append(" content-role='metadata'");
	                writer.append(" href='");
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
	
	                final List<AdqlColumn> cols = new ArrayList<AdqlColumn>();
	
	                for (final AdqlColumn column : table.columns().select())
	                    {
	                    cols.add(column);
	
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
	                        if (column.meta().adql().type() == AdqlColumn.AdqlType.DATE)
	                            {
	                            writer.append(" datatype='char'");
	                            writer.append(" arraysize='*'");
	                            }
	                        if (column.meta().adql().type() == AdqlColumn.AdqlType.TIME)
	                            {
	                            writer.append(" datatype='char'");
	                            writer.append(" arraysize='*'");
	                            }
	                        if (column.meta().adql().type() == AdqlColumn.AdqlType.DATETIME)
	                            {
	                            writer.append(" datatype='char'");
	                            writer.append(" arraysize='*'");
	                            }
	                        else {
	                            writer.append(" datatype='");
	                            writer.append(column.meta().adql().type().votype());
	                            writer.append("'");
	
	                            if (column.meta().adql().arraysize() != null)
	                                {
	                                if (column.meta().adql().arraysize() == AdqlColumn.NON_ARRAY_SIZE)
	                                    {
	                                    }
	                                else if (column.meta().adql().arraysize() == AdqlColumn.VAR_ARRAY_SIZE)
	                                    {
	                                    writer.append(" arraysize='*'");
	                                    }
	                                else {
	                                    writer.append(" arraysize='");
	                                    writer.append(column.meta().adql().arraysize().toString());
	                                    writer.append("'");
	                                    }
	                                }
	                            }
	
	                        if (column.meta().adql() != null)
	                            {
	                            writer.append(" xtype='");
	                            writer.append(column.meta().adql().type().xtype());
	                            writer.append("'");
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
	               
	                    writer.append("<DATA>");
	                    writer.append("<TABLEDATA>");
	                    log.error("In Votable [{}]", query.link());
	                    final JdbcProductType type  = jdbc.resource().connection().type();
	                    final Connection connection = jdbc.resource().connection().open();
	                    int isolation = Connection.TRANSACTION_NONE;
	                    try {
	                        isolation = connection.getTransactionIsolation();
	                        connection.setTransactionIsolation(
	                            Connection.TRANSACTION_READ_UNCOMMITTED
	                            );
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
	                            final AdqlColumn adql = cols.get(colnum);
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
	
	                        while (results.next())
	                            {
	                            writer.append("<TR>");
	                            for (final FieldHandler handler : handlers)
	                                {
	                                handler.write(
	                                    writer
	                                    );
	                                }
	                            writer.append("</TR>");
	                            }
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
	                                connection.setTransactionIsolation(
	                                    isolation
	                                    );
	                                }
	                            catch (final SQLException ouch)
	                                {
	                                log.error("Exception closing SQL connection [{}]", ouch.getMessage());
	                                }
	                            }
	                        }
	
	                    writer.append("</TABLEDATA>");
	                    writer.append("</DATA>");
                } else {
                	writer.append("'>");
                	
                }
                writer.append("</TABLE>");
            writer.append("</RESOURCE>");
        writer.append("</vot:VOTABLE>");
    }
    /**
     * Generate a VOTable from query results.
     *
     */
    public void generateTAPVotable(
        final PrintWriter writer,
        final BlueQuery   query
        ){
        final AdqlTable table= query.results().adql();
        final JdbcTable jdbc = query.results().jdbc();

        // Based on VOTable-1.3 specification.
        // http://www.ivoa.net/documents/VOTable/20130315/PR-VOTable-1.3-20130315.html

        writer.append("<?xml version='1.0' encoding='UTF-8'?>");
        writer.append("<vot:VOTABLE");
        writer.append(" xmlns:vot='http://www.ivoa.net/xml/VOTable/v1.3'");
        writer.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
        writer.append(" xsi:schemaLocation='http://www.ivoa.net/xml/VOTable/v1.3 http://www.ivoa.net/xml/VOTable/v1.3'");
        writer.append(" version='1.3'");
        writer.append(">");
        	
            writer.append("<RESOURCE type='results'");
            writer.append(" ID='query.");
            writer.append(query.ident().toString());
            writer.append("'");

            if (query.name() != null)
                {
                writer.append(" name='");
                writer.append(query.name());
                writer.append("'");
                }
            writer.append(">");
            writer.append("<LINK");
            writer.append(" content-type='");
            writer.append(JSON_MIME);
            writer.append("'");
            writer.append(" content-role='metadata'");
            writer.append(" href='");
            if (query.link() != null)
            	{
	            writer.append(query.link());
            	}

            writer.append(query.link());
            writer.append("'");
            writer.append("/>");
            if (query.state()==TaskState.COMPLETED)
            {
                writer.append("<INFO name='QUERY_STATUS' value='OK'>");
            } else  {
                writer.append("<INFO name='QUERY_STATUS' value='ERROR'>");
                if (query.syntax().friendly() != null){
                	writer.append(query.syntax().friendly());
                }
            }

            writer.append("</INFO>");
            if (query.input() != null)
            {
	        	writer.append("<INFO name='QUERY' value='" + query.input() + "' />");
            }
            if (query.text() != null)
                {
                writer.append("<DESCRIPTION>");
                writer.append("<![CDATA[");
                writer.append(query.text());
                writer.append("]]>");
                writer.append("</DESCRIPTION>");
                }

                writer.append("<TABLE ID='table.");
                if (table!=null){
	                writer.append(table.ident().toString());
	                writer.append("'");
	                if (table.name() != null)
	                    {
	                    writer.append(" name='");
	                    writer.append(table.name());
	                    writer.append("'");
	                    }
	                writer.append(">");
	                writer.append("<LINK");
	                writer.append(" content-type='");
	                writer.append(JSON_MIME);
	                writer.append("'");
	                writer.append(" content-role='metadata'");
	                writer.append(" href='");
	                if (table.link()!= null)
                    {
	                    writer.append(table.link());
                    }
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
	
	                final List<AdqlColumn> cols = new ArrayList<AdqlColumn>();

	                for (final AdqlColumn column : table.columns().select())
	                    {

	                    cols.add(column);
	
	                    writer.append("<FIELD ID='column.");
	                    if (column.ident()!=null){
	                        writer.append(column.ident().toString());
	                    }
	                    writer.append("'");
	                    if (column.name() != null)
	                        {
	                        writer.append(" name='");
	                        writer.append(column.name());
	                        writer.append("'");
	                        }
	
	                    if (column.meta().adql().type() != null)
	                        {
	                        if (column.meta().adql().type() == AdqlColumn.AdqlType.DATE)
	                            {
	                            writer.append(" datatype='char'");
	                            writer.append(" arraysize='*'");
	                            }
	                        if (column.meta().adql().type() == AdqlColumn.AdqlType.TIME)
	                            {
	                            writer.append(" datatype='char'");
	                            writer.append(" arraysize='*'");
	                            }
	                        if (column.meta().adql().type() == AdqlColumn.AdqlType.DATETIME)
	                            {
	                            writer.append(" datatype='char'");
	                            writer.append(" arraysize='*'");
	                            }
	                        else {
	                            writer.append(" datatype='");
	                            writer.append(column.meta().adql().type().votype());
	                            writer.append("'");
	
	                            if (column.meta().adql().arraysize() != null)
	                                {
	                                if (column.meta().adql().arraysize() == AdqlColumn.NON_ARRAY_SIZE)
	                                    {
	                                    }
	                                else if (column.meta().adql().arraysize() == AdqlColumn.VAR_ARRAY_SIZE)
	                                    {
	                                    writer.append(" arraysize='*'");
	                                    }
	                                else {
	                                    writer.append(" arraysize='");
	                                    writer.append(column.meta().adql().arraysize().toString());
	                                    writer.append("'");
	                                    }
	                                }
	                            }
	
	                        if (column.meta().adql() != null)
	                            {
	                            writer.append(" xtype='");
	                            writer.append(column.meta().adql().type().xtype());
	                            writer.append("'");
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
	
	                    if (column.link() != null)
	                    	{
		                    writer.append("<LINK");
		                    writer.append(" content-type='" + JSON_MIME + "'");
		                    writer.append(" content-role='metadata'");
		                    writer.append(" href='" + column.link() + "'");
		                    writer.append("/>");
	                    	}
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

	                    writer.append("<DATA>");
	                    writer.append("<TABLEDATA>");
	                    if (query.link() != null)
                            {
	                        log.error("In Votable [{}]", query.link());
                            }
	                    final JdbcProductType type  = jdbc.resource().connection().type();
	                    final Connection connection = jdbc.resource().connection().open();
	                    int isolation = Connection.TRANSACTION_NONE;
	                    try {
	                        isolation = connection.getTransactionIsolation();
	                        connection.setTransactionIsolation(
	                            Connection.TRANSACTION_READ_UNCOMMITTED
	                            );
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
		    	           
	                            final AdqlColumn adql = cols.get(colnum);
		    	                if (adql.meta()!=null){
		    	                	if (adql.meta().adql() != null){
		    	                
			                            if (adql.meta().adql().type() != null){
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
		    	                	}
		    	                }
			                            
	                            }
	
	                        while (results.next())
	                            {

	                            writer.append("<TR>");
	                            for (final FieldHandler handler : handlers)
	                                {
	                                handler.write(
	                                    writer
	                                    );
	                                }
	                            writer.append("</TR>");
	                            }
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
	                                connection.setTransactionIsolation(
	                                    isolation
	                                    );
	                                }
	                            catch (final SQLException ouch)
	                                {
	                                log.error("Exception closing SQL connection [{}]", ouch.getMessage());
	                                }
	                            }
	                        }
	
	                    writer.append("</TABLEDATA>");
	                    writer.append("</DATA>");
                } else {
                	writer.append("'>");
                	
                }
                writer.append("</TABLE>");
            writer.append("</RESOURCE>");
        writer.append("</vot:VOTABLE>");
    }

    /**
     * Generate a VOTable from query results.
     *
     */
    public void generateVotable(
        final PrintWriter writer,
        final GreenQuery   query
        ){

        final AdqlTable table= query.results().adql();
        final JdbcTable jdbc = query.results().jdbc();

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
            writer.append(" ID='query.");
            writer.append(query.ident().toString());
            writer.append("'");
            if (query.name() != null)
                {
                writer.append(" name='");
                writer.append(query.name());
                writer.append("'");
                }
            writer.append(">");
            writer.append("<LINK");
            writer.append(" content-type='");
            writer.append(JSON_MIME);
            writer.append("'");
            writer.append(" content-role='metadata'");
            writer.append(" href='");
            writer.append(query.link());
            writer.append("'");
            writer.append("/>");
            if (query.text() != null)
                {
                writer.append("<DESCRIPTION>");
                writer.append("<![CDATA[");
                writer.append(query.text());
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
                writer.append("<LINK");
                writer.append(" content-type='");
                writer.append(JSON_MIME);
                writer.append("'");
                writer.append(" content-role='metadata'");
                writer.append(" href='");
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

                final List<AdqlColumn> cols = new ArrayList<AdqlColumn>();

                for (final AdqlColumn column : table.columns().select())
                    {
                    cols.add(column);

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
                        if (column.meta().adql().type() == AdqlColumn.AdqlType.DATE)
                            {
                            writer.append(" datatype='char'");
                            writer.append(" arraysize='*'");
                            }
                        if (column.meta().adql().type() == AdqlColumn.AdqlType.TIME)
                            {
                            writer.append(" datatype='char'");
                            writer.append(" arraysize='*'");
                            }
                        if (column.meta().adql().type() == AdqlColumn.AdqlType.DATETIME)
                            {
                            writer.append(" datatype='char'");
                            writer.append(" arraysize='*'");
                            }
                        else {
                            writer.append(" datatype='");
                            writer.append(column.meta().adql().type().votype());
                            writer.append("'");

                            if (column.meta().adql().arraysize() != null)
                                {
                                if (column.meta().adql().arraysize() == AdqlColumn.NON_ARRAY_SIZE)
                                    {
                                    }
                                else if (column.meta().adql().arraysize() == AdqlColumn.VAR_ARRAY_SIZE)
                                    {
                                    writer.append(" arraysize='*'");
                                    }
                                else {
                                    writer.append(" arraysize='");
                                    writer.append(column.meta().adql().arraysize().toString());
                                    writer.append("'");
                                    }
                                }
                            }

                        if (column.meta().adql() != null)
                            {
                            writer.append(" xtype='");
                            writer.append(column.meta().adql().type().xtype());
                            writer.append("'");
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

                    writer.append("<DATA>");
                    writer.append("<TABLEDATA>");
                    log.error("In Votable [{}]", query.link());
                    final JdbcProductType type  = jdbc.resource().connection().type();
                    final Connection connection = jdbc.resource().connection().open();
                    int isolation = Connection.TRANSACTION_NONE;
                    try {
                        isolation = connection.getTransactionIsolation();
                        connection.setTransactionIsolation(
                            Connection.TRANSACTION_READ_UNCOMMITTED
                            );
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
                            final AdqlColumn adql = cols.get(colnum);
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

                        while (results.next())
                            {
                            writer.append("<TR>");
                            for (final FieldHandler handler : handlers)
                                {
                                handler.write(
                                    writer
                                    );
                                }
                            writer.append("</TR>");
                            }
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
                                connection.setTransactionIsolation(
                                    isolation
                                    );
                                }
                            catch (final SQLException ouch)
                                {
                                log.error("Exception closing SQL connection [{}]", ouch.getMessage());
                                }
                            }
                        }

                    writer.append("</TABLEDATA>");
                    writer.append("</DATA>");
                writer.append("</TABLE>");
            writer.append("</RESOURCE>");
        writer.append("</vot:VOTABLE>");


    }


    /**
     * VOTable GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=TEXT_XML_MIME)
    public void votable(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident,
        final HttpServletResponse response
        ) throws EntityNotFoundException, IOException {
        response.setContentType(
            TEXT_XML_MIME
            );
        response.setCharacterEncoding(
            "UTF-8"
            );
		generateVotable(
		    response.getWriter(),
	        factories().adql().greens().entities().select(
	            factories().adql().greens().idents().ident(
	                ident
	                )
	            )
		    );
        }
    }
