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

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 * Spring Controller to generate VOTable response for a table.
 * 
 * Based on the VOTable-1.3 specification.
 * http://www.ivoa.net/documents/VOTable/20130315/PR-VOTable-1.3-20130315.html
 *
 */
@Slf4j
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

    /**
     * Generate the table header.
     * 
     */
    @Override
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

        writer.append("<DATA>");
        writer.append("<TABLEDATA>");
        }            
        
    /**
     * Generate the metadata for a field.
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
     * Generate a table row.
     * 
     */
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

    /**
     * Generate a table cell.
     * 
     */
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
    
    /**
     * Generate the table footer.
     * 
     */
    @Override
    public void foot(final PrintWriter writer, final BaseTable<?,?> table)
        {
        writer.append("</TABLEDATA>");
        writer.append("</DATA>");
        writer.append("</TABLE>");
        writer.append("</RESOURCE>");
        writer.append("</vot:VOTABLE>");
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
