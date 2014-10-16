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

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.webapp.votable.AbstractTableController.FieldFormatter;

/**
 * Spring MVC controller to format a {@link BaseTable} as a HTMLTable
 *
 */
public abstract class BaseTableHTMLTableController
extends AbstractTableController
    {
    /**
     * Public constructor.
     *
     */
    public BaseTableHTMLTableController()
        {
        super();
        }

    /**
     * HTMLTable MIME type, {@value}.
     *
     */
    public static final String HTML_TABLE_MIME = " application/xhtml+xml" ;

    /**
     * TextHTML MIME type, {@value}.
     *
     */
    public static final String TEXT_HTML_MIME = "text/html" ;

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

    @Override
    public void head(final PrintWriter writer, final BaseTable<?,?> table)
        {
        writer.append("<html>");
        writer.append(" <head>");
        writer.append(" </head>");
        writer.append(" <body>");
        writer.append("  <table border='1'>");
        writer.append("   <thead>");
        for (final BaseColumn<?> column : table.columns().select())
        {
        field(
            writer,
            column
            );
        }
        writer.append("   </thead>");

        }            

    /**
     * Write a the HTMLTable FIELD metadata for {@link BaseColumn} to a {@link PrintWriter}
     * @param writer  The {@link PrintWriter} to write the field to.
     * @param columnn The {@link BaseColumn} to write the FIELD metadata for.
     *
     */
    protected void field(final PrintWriter writer, final BaseColumn<?> column)
        {
        writer.append("<TD>");
        writer.append("<b>");
        writer.append(column.name());
        writer.append("</b>");
        writer.append("</TD>");
        }

    @Override
    public void row(final List<FieldFormatter> formatters, final PrintWriter writer, final ResultSet results)
    throws SQLException
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
    
    @Override
    public void foot(final PrintWriter writer, final BaseTable<?,?> table)
        {
          writer.append("  </table>");
          writer.append(" </body>");
    	  writer.append("</html>");
        }

    @Override
    public FieldFormatter formatter(final BaseColumn<?> column)
        {
        switch(column.meta().adql().type())
            {
            case CHAR :
            case UNICODE:
                return new XmlStringFormatter(
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
