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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlQueryLinkFactory;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.TableFormatException;
import uk.ac.starlink.table.jdbc.SequentialResultSetStarTable;
import uk.ac.starlink.table.jdbc.StarResultSet;
import uk.ac.starlink.votable.VOTableWriter;

/**
 * Spring MVC controller for <code>AdqlTable</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlQueryLinkFactory.VOTABLE_PATH)
public class AdqlQueryVOTableController
    extends AbstractController
    {

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
        builder.append(
            " ORDER BY 1"
            );

        log.debug("VOTABLE SQL [{}]", builder.toString());

        return builder.toString();

        }
    
    public void select(final StringBuilder builder, final AdqlColumn column, final JdbcProductType type)
        {
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
            if (column.meta().adql().type() == AdqlColumn.Type.DATETIME)
                {
                builder.append(
                    "CONVERT(varchar, 126, "
                    );
                builder.append(
                    column.root().name()
                    );
                builder.append(
                    ")"
                    );
                }
            else if (column.meta().adql().type() == AdqlColumn.Type.TIME)
                {
                builder.append(
                    "CONVERT(varchar, 114, "
                    );
                builder.append(
                    column.root().name()
                    );
                builder.append(
                    ")"
                    );
                }
            else if (column.meta().adql().type() == AdqlColumn.Type.DATE)
                {
                builder.append(
                    "CONVERT(varchar, 102, "
                    );
                builder.append(
                    column.root().name()
                    );
                builder.append(
                    ")"
                    );
                }
            else {
                builder.append(
                    column.root().name()
                    );
                }
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
     * VOTable GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=TEXT_XML_MIME)
    public void votable(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident,
        final HttpServletResponse response
        ) throws NotFoundException, IOException {

        response.setContentType(
            TEXT_XML_MIME
            );
        response.setCharacterEncoding(
            "UTF-8"
            );

        final PrintWriter writer = response.getWriter();
        
        final AdqlQuery query = factories().adql().queries().select(
            factories().adql().queries().idents().ident(
                ident
                )
            );

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
            writer.append(JSON_CONTENT);
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
        
                for (final AdqlColumn column : table.columns().select())
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
        
                    writer.append("<DATA>");
                    writer.append("<TABLEDATA>");

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
                        ResultSet results = statement.executeQuery(
                            select(
                                table,
                                type
                                )
                            );

                        final ResultSetMetaData colmeta = results.getMetaData();
                        final int colcount = colmeta.getColumnCount();                         
                        
                        while (results.next())
                            {
                            writer.append("<TR>");
                            for (int colnum = 1 ; colnum <= colcount ; colnum++)
                                {
                                Object object = null ;
                                switch(colmeta.getColumnType(colnum))
                                    {
                                    default :
                                        object = results.getObject(colnum);
                                        break ;
                                    }
                                if (object != null)
                                    {
                                    writer.append("<TD>");
                                    writer.append(object.toString());
                                    writer.append("</TD>");
                                    }
                                else {
                                    writer.append("<TD/>");
                                    }
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
    }
