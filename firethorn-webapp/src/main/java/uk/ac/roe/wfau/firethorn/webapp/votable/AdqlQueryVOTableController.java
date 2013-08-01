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
import java.sql.Connection;
import java.sql.ResultSet;
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
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
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
        ) throws NotFoundException {

        final AdqlQuery query = factories().adql().queries().select(
            factories().adql().queries().idents().ident(
                ident
                )
            );

        try {

            response.setContentType(
                TEXT_XML_MIME
                );

            log.debug("Query [{}]", query.ident());

            //
            // Run the SQL query.
            // http://stackoverflow.com/questions/858836/does-a-resultset-load-all-data-into-memory-or-only-when-requested
            final JdbcResource resource = query.results().jdbc().resource();
            final Connection connection = resource.connection().open();
            final Statement statement = connection.createStatement(
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY
                );

/*
 * ResultSet.CLOSE_CURSORS_AT_COMMIT
 * Exception reading SQL results [The CLOSE_CURSORS_AT_COMMIT option is not currently supported by the setHoldability method.]
 *
 */

            //
            // Create an SQL query to get the columns in a specific order.
            final StringBuilder builder = new StringBuilder();
            builder.append(
                "SELECT "
                );
            //
            // Add the columns from our ADQL metadata, using the ADQL aliases.
            final List<AdqlColumn> columns = new ArrayList<AdqlColumn>();
            for (final AdqlColumn column : query.results().adql().columns().select())
                {
                log.debug("Column [{}][{}]", column.ident(), column.name());
                columns.add(
                    column
                    );
                if (columns.size() > 1)
                    {
                    builder.append(
                        ", "
                        );
                    }

                //
                // Postgresql dialect
                if (resource.connection().type() == JdbcProductType.PGSQL)
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
                else if (resource.connection().type() == JdbcProductType.MSSQL)
                    {
                    builder.append(
                        column.root().name()
                        );
                    /*
                    if (column.meta().adql().type() == Type.TIMESTAMP)
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
                    else if (column.meta().adql().type() == Type.TIME)
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
                    else if (column.meta().adql().type() == Type.DATE)
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
                     */
                    }

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
            builder.append(
                " FROM "
                );
            builder.append(
                query.results().jdbc().namebuilder()
                );
            log.debug("SQL [{}]", builder.toString());


            statement.setFetchSize(
                1000
                );
            try {
                final ResultSet results = statement.executeQuery(
                    builder.toString()
                    );
                final StarResultSet starset = new StarResultSet(
                    results
                    );
                final StarTable startab = new SequentialResultSetStarTable(
                    starset
                    );
                //
                // Update the StarTable column metadata to match the ADQL column names.
                for (int i = 0 ; i < startab.getColumnCount() ; i++)
                    {
                    final ColumnInfo info = startab.getColumnInfo(i);
                    final AdqlColumn adql = columns.get(i);

                    log.debug("Info name [{}]", info.getName());
                    log.debug("Adql name [{}]", adql.name());
                    log.debug("Adql type [{}]", adql.meta().adql().type());
                    log.debug("Adql size [{}]", adql.meta().adql().arraysize());

                    info.setName(
                        adql.name()
                        );

                    if (adql.meta().adql().ucd() != null)
                        {
                        info.setUCD(
                            adql.meta().adql().ucd().value()
                            );
                        }

                    if (adql.meta().adql().units() != null)
                        {
                        info.setUnitString(
                            adql.meta().adql().units()
                            );
                        }
                    }

                final VOTableWriter writer = new VOTableWriter();
                writer.writeStarTable(
                    startab,
                    response.getOutputStream()
                    );
                }
            finally {
                resource.connection().close();
                }
            }
        catch (final SQLException ouch)
            {
            log.error("Exception reading SQL results [{}]", ouch.getMessage());
            }
        catch (final TableFormatException ouch)
            {
            log.error("Exception writing VOTable output [{}]", ouch.getMessage());
            }
        catch (final IOException ouch)
            {
            log.error("Exception writing VOTable output [{}]", ouch.getMessage());
            }
        }
    }
