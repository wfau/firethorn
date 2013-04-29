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

import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
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
@RequestMapping(VOTableLinkFactory.QUERY_PATH)
public class VOTableController
    extends AbstractController
    {

    @Override
    public Path path()
        {
        return path(
            VOTableLinkFactory.QUERY_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public VOTableController()
        {
        super();
        }

    /**
     * Get the target entity based on the ident in the path.
     * @throws NotFoundException
     *
     */
    public AdqlQuery query(
        final String ident
        ) throws NotFoundException {
        return factories().adql().queries().select(
            factories().adql().queries().idents().ident(
                ident
                )
            );
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
    @RequestMapping(method=RequestMethod.GET, produces=VOTABLE_MIME)
    public void select(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident,
        final HttpServletResponse response
        ) throws NotFoundException {

        AdqlQuery query = query(ident);

        try {

// AdqlTable
// JdbcTable
// JdbcResource
// JdbcConnection

            response.setContentType(
                VOTABLE_MIME
                );

            log.debug("Query [{}]", query.ident());
        
            StringBuilder builder = new StringBuilder();
            builder.append(
                "SELECT "
                );
    
            List<AdqlColumn> columns = new ArrayList<AdqlColumn>();
            for (AdqlColumn column : query.results().adql().columns().select())
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
                builder.append(
                    column.root().name()
                    );
                builder.append(
                    " AS "
                    );
                builder.append(
                    column.alias()
                    );
                }
            builder.append(
                " FROM "
                );
            builder.append(
                query.results().jdbc().fullname()
                );
            String sql = builder.toString();
            log.debug("Statement [{}]", sql);

            JdbcResource resource = query.results().jdbc().resource();
            Connection connection = resource.connection().open();
            Statement  statement  = connection.createStatement();            
            ResultSet  results    = statement.executeQuery(
                sql
                ); 
            
            StarResultSet starset = new StarResultSet(
                results
                );
            
            StarTable startab = new SequentialResultSetStarTable(
                starset
                );

            for (int i = 0 ; i < startab.getColumnCount() ; i++)
                {
                ColumnInfo info = startab.getColumnInfo(i);            
                AdqlColumn adql = columns.get(i);

                log.debug("Info [{}]", info.getName());
                log.debug("Adql [{}]", adql.name());
                info.setName(
                    adql.name()
                    );
                info.setUCD(
                    adql.meta().adql().ucd()
                    );
                info.setUnitString(
                    adql.meta().adql().unit()
                    );
                }
            
            VOTableWriter writer = new VOTableWriter();
            writer.writeStarTable(
                startab,
                response.getOutputStream()
                );        
            }
        catch (SQLException ouch)
            {
            log.error("Exception reading SQL results [{}]", ouch.getMessage());
            }
        catch (TableFormatException ouch)
            {
            log.error("Exception writing VOTable output [{}]", ouch.getMessage());
            }
        catch (IOException ouch)
            {
            log.error("Exception writing VOTable output [{}]", ouch.getMessage());
            }
        }
    }
