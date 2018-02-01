/*
 *  Copyright (C) 2016 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InternalServerErrorException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InvalidRequestException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InvalidStateTransitionException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.hibernate.HibernateConvertException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.webapp.blue.BlueQueryBean;
import uk.ac.roe.wfau.firethorn.webapp.blue.BlueQueryController;
import uk.ac.roe.wfau.firethorn.webapp.blue.BlueQueryModel;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlResourceLinkFactory;

/**
 * Spring MVC controller to handle the {@link BlueQuery}s linked to an {@link AdqlResource}.
 * <br/>Controller path : [{@value AdqlResourceLinkFactory#RESOURCE_QUERIES_PATH}]
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlResourceLinkFactory.RESOURCE_QUERIES_PATH)
public class AdqlResourceBlueController
extends AbstractEntityController<BlueQuery, BlueQueryBean>
    {
    @Override
    public Path path()
        {
        return path(
            AdqlResourceLinkFactory.RESOURCE_QUERIES_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlResourceBlueController()
        {
        super();
        }

    @Override
    public BlueQueryBean bean(final BlueQuery entity)
        {
        return new BlueQueryBean(
            entity
            );
        }

    @Override
    public Iterable<BlueQueryBean > bean(final Iterable<BlueQuery> iter)
        {
        return new BlueQueryBean.Iter(
            iter
            );
        }

    /**
     * {@link RequestMethod#GET} request to select all the {@link BlueQuery}s for this {@link AdqlResource}.
     * <br/>Request path : [{@value #SELECT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param ident The parent {@link AdqlResource} identifier in the request path.
     * @return An {@Iterable} of {@link BlueQueryBean}.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<Iterable<BlueQueryBean>> select(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws IdentifierNotFoundException, IdentifierFormatException, ProtectionException
        {
        return selected( 
            factories().adql().resources().entities().select(
                factories().adql().resources().idents().ident(
                    ident
                    )
                ).blues().select()
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link BlueQuery}.
     * <br/>Request path : [{@value #CREATE_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param ident The parent {@link AdqlResource} identifier in the request path.
     * @param input The {@link BlueQuery} input, [{@value BlueQueryController#QUERY_INPUT_PARAM}].
     * 
     * @return A new {@link BlueQuery} wrapped in an {@link BlueQueryBean}.
     * @throws InvalidStateTransitionException 
     * @throws IdentifierFormatException 
     * @throws ProtectionException 
     * @throws HibernateConvertException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<BlueQueryBean> create(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident,

        @RequestParam(value=BlueQueryModel.QUERY_ADQL_SCHEMA, required=false)
        final String adqlschemaident,
        @RequestParam(value=BlueQueryModel.QUERY_JDBC_SCHEMA, required=false)
        final String jdbcschemaident,
        
        @RequestParam(value=BlueQueryModel.QUERY_INPUT_PARAM, required=false)
        final String input,

        @RequestParam(value=BlueQueryModel.QUERY_MODE, required=false)
        final AdqlQueryBase.Mode mode,
        @RequestParam(value=BlueQueryModel.QUERY_SYNTAX, required=false)
        final AdqlQueryBase.Syntax.Level syntax,
        
        @RequestParam(value=BlueQueryModel.QUERY_LIMT_CELLS, required=false)
        final Long cells,
        @RequestParam(value=BlueQueryModel.QUERY_LIMT_ROWS, required=false)
        final Long rows,
        @RequestParam(value=BlueQueryModel.QUERY_LIMT_TIME, required=false)
        final Long time,

        @RequestParam(value=BlueQueryModel.QUERY_DELAY_FIRST, required=false)
        final Integer first,
        @RequestParam(value=BlueQueryModel.QUERY_DELAY_EVERY, required=false)
        final Integer every,
        @RequestParam(value=BlueQueryModel.QUERY_DELAY_LAST, required=false)
        final Integer last,

        @RequestParam(value=BlueQueryModel.STATUS_NEXT_PARAM, required=false)
        final TaskState next,
        @RequestParam(value=BlueQueryModel.STATUS_WAIT_PARAM, required=false)
        final Long wait
        )
    throws IdentifierNotFoundException, IdentifierFormatException, InvalidRequestException, InternalServerErrorException, ProtectionException
        {
        log.debug("create(String, String)");
        log.debug("  ident [{}]", ident);
        log.debug("  jdbc  [{}]", jdbcschemaident);
        log.debug("  adql  [{}]", adqlschemaident);
        log.debug("  input [{}]", input);
        log.debug("  next  [{}]", next);
        log.debug("  wait  [{}]", wait);

        JdbcSchema jdbcschema = null ;
        if (null != jdbcschemaident)
            {
            jdbcschema = factories().jdbc().schemas().entities().select(
                factories().jdbc().schemas().idents().ident(
                    jdbcschemaident
                    )
                );
            }

        AdqlSchema adqlschema = null ;
        if (null != adqlschemaident)
            {
            adqlschema = factories().adql().schemas().entities().select(
                factories().adql().schemas().idents().ident(
                    adqlschemaident
                    )
                );
            }
        
        return created(
            factories().adql().resources().entities().select(
                factories().adql().resources().idents().ident(
                    ident
                    )
                ).blues().create(
                    jdbcschema,
                    adqlschema,
                    input,
                    mode,
                    syntax,
                    factories().blues().limits().create(
                        rows,
                        cells,
                        time
                        ),
                    factories().blues().delays().create(
                        first,
                        every,
                        last
                        ),
                    next,
                    wait
                    )
            );
        }
    }
