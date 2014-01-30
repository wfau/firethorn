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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;

/**
 * Spring Controller to generate VOTable response for a table.
 * 
 * Based on the VOTable-1.3 specification.
 * http://www.ivoa.net/documents/VOTable/20130315/PR-VOTable-1.3-20130315.html
 *
 */
@Slf4j
public abstract class BaseTableVOTableController
extends AbstractController
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
     * Public interface for a field formatter.
     *
     */
    public interface FieldFormatter
        {
        /**
         * Format the field as a String.
         *
         */
        public String format(final ResultSet results)
        throws SQLException;
        }
    
    /**
     * Abstract base class for a FieldWriter.
     *
     */
    public static abstract class AbstractFormatter
    implements FieldFormatter
        {
        /**
         *  Protected constructor.
         *
         */
        public AbstractFormatter(final BaseColumn<?> column)
            {
            this.column  = column;
            }

        protected final BaseColumn<?> column  ;

        }

    /**
     * A simple formatter for objects.
     *
     */
    public static class SimpleFormatter
    extends AbstractFormatter
        {
        public SimpleFormatter(final BaseColumn<?> column)
            {
            super(
                column
                );
            }

        @Override
        public String format(final ResultSet results)
        throws SQLException
            {
            return results.getObject(
                column.root().name()
                ).toString();
            }
        }

    /**
     * Escape string values for XML.
     * http://commons.apache.org/proper/commons-lang/javadocs/api-release/org/apache/commons/lang3/StringEscapeUtils.html
     * http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/StringEscapeUtils.html#escapeXml%28java.lang.String%29
     *
     */
    public static class XmlStringFormatter
    extends AbstractFormatter
        {
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
                    column.root().name()
                    )
                );
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
            return iso.print(
                new DateTime(
                    results.getDate(
                        column.root().name()
                        )
                    )
                );
            }
        }

    /*
     * TODO refactor this to use our AdqlParser classes. 
     *
     */
    public String select(final BaseTable<?,?> table, final JdbcProductType type)
        {
        final StringBuilder builder = new StringBuilder();

        builder.append(
            "SELECT"
            );

        String glue = " ";
        for (final BaseColumn<?> column : table.columns().select())
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

    public void select(final StringBuilder builder, final BaseColumn<?> column, final JdbcProductType type)
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
     * Generate the table header.
     * 
     */
    public void head(final PrintWriter writer, final BaseTable<?,?> table)
        {
        writer.append("<?xml version='1.0' encoding='UTF-8'?>");
        writer.append("<vot:VOTABLE");
        writer.append(" xmlns:vot='http://www.ivoa.net/xml/VOTable/v1.3'");
        writer.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
        writer.append(" xsi:schemaLocation='http://www.ivoa.net/xml/VOTable/v1.3 http://www.ivoa.net/xml/VOTable/v1.3'");
        writer.append(" version='1.3'");
        writer.append(">");

        //
        // TODO Add the table query description and ADQL statement if available.
        
        writer.append("<RESOURCE");
        writer.append(" ID='table.");
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
        writer.append(JSON_CONTENT);
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
        }            
        
    /**
     * Generate the metadata for a column.
     * 
     */
    public void field(final PrintWriter writer, final BaseColumn<?> column)
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
            if (column.meta().adql().type() == AdqlColumn.Type.DATE)
                {
                writer.append(" datatype='char'");
                writer.append(" arraysize='*'");
                }
            if (column.meta().adql().type() == AdqlColumn.Type.TIME)
                {
                writer.append(" datatype='char'");
                writer.append(" arraysize='*'");
                }
            if (column.meta().adql().type() == AdqlColumn.Type.DATETIME)
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
                    if (column.meta().adql().arraysize() == BaseColumn.NON_ARRAY_SIZE)
                        {
                        }
                    else if (column.meta().adql().arraysize() == BaseColumn.VAR_ARRAY_SIZE)
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
            writer.append(" xtype='");
            writer.append(column.meta().adql().type().xtype());
            writer.append("'");
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
            writer.append(column.meta().adql().ucd().value());
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
        writer.append(" content-type='" + JSON_CONTENT + "'");
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

    /**
     * Generate the table rows.
     * 
     */
    public void rows(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException
        {
        while (results.next())
            {
            writer.append("<TR>");
            cells(
                formatters,
                writer,
                results
                );
            writer.append("</TR>");
            }
        }

    /**
     * Generate the table cells.
     * 
     */
    public void cells(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException
        {
        for (final FieldFormatter formatter : formatters)
            {
            cell(
                formatter,
                writer,
                results
                );
            }
        }

    /**
     * Generate a table cell.
     * 
     */
    public void cell(final FieldFormatter formatter, final PrintWriter writer, final ResultSet results)
    throws SQLException
        {
        writer.append("<TD>");
        writer.append(
            formatter.format(
                results
                )
            );
        writer.append("</TD>");
        }
    
    /**
     * Generate the table footer.
     * 
     */
    public void foot(final PrintWriter writer, final BaseTable<?,?> table)
        {
        writer.append("</TABLEDATA>");
        writer.append("</DATA>");
        writer.append("</TABLE>");
        writer.append("</RESOURCE>");
        writer.append("</vot:VOTABLE>");
        }

    /**
     * Generate the table body.
     * 
     */
    public void body(final PrintWriter writer, final BaseTable<?,?> base)
        {
        writer.append("<DATA>");
        writer.append("<TABLEDATA>");

        //
        // If the root table is a JDBC table.
        if (base.root() instanceof JdbcTable)
            {
            final JdbcTable jdbc = (JdbcTable) base.root();

            final JdbcProductType type  = jdbc.resource().connection().type();
            final Connection connection = jdbc.resource().connection().open();

            int isolation = Connection.TRANSACTION_NONE;

            try {
                //
                // Use isolation level to peek at live data.
                // http://redmine.roe.ac.uk/issues/324
                // http://www.precisejava.com/javaperf/j2ee/JDBC.htm#JDBC107
                isolation = connection.getTransactionIsolation();
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
                        base,
                        type
                        )
                    );

                //final ResultSetMetaData colmeta = results.getMetaData();
                //final int colcount = colmeta.getColumnCount();

                final List<FieldFormatter> formatters = new ArrayList<FieldFormatter>();
                for (final BaseColumn<?> column : base.columns().select())
                    {
                    switch(column.meta().adql().type())
                        {
                        case CHAR :
                        case UNICODE:
                            formatters.add(
                                new XmlStringFormatter(
                                    column
                                    )
                                );
                            break ;

                        case DATE :
                        case TIME:
                        case DATETIME :
                            formatters.add(
                                new XmlDateTimeFormatter(
                                    column
                                    )
                                );
                            break ;

                        default :
                            formatters.add(
                                new SimpleFormatter(
                                    column
                                    )
                                );
                            break ;
                        }
                    }

                rows(
                    formatters,
                    writer,
                    results
                    );
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
            }
        else {
            log.error("Unable to process root table type [{}]", base.root().getClass().getName());
            }
        }
    
    /**
     * Generate the table.
     *
     */
    public void table(final PrintWriter writer, final BaseTable<?,?> table) 
        {
        head(writer, table);
        body(writer, table);
        foot(writer, table);
        }
    }
