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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.blue.BlueQueryBean;
import uk.ac.roe.wfau.firethorn.blue.BlueQueryController;
import uk.ac.roe.wfau.firethorn.blue.InvalidStateTransitionException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.hibernate.HibernateConvertException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller to handle the {@link AdqlSchema} in an {@link AdqlResource}.
 * <br/>Controller path : [{@value AdqlResourceLinkFactory#RESOURCE_SCHEMA_PATH}]
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlResourceLinkFactory.RESOURCE_BLUE_PATH)
public class AdqlResourceBlueQueryController
extends AbstractEntityController<BlueQuery, BlueQueryBean>
    {
    @Override
    public Path path()
        {
        return path(
            AdqlResourceLinkFactory.RESOURCE_BLUE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlResourceBlueQueryController()
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
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public Iterable<BlueQueryBean> select(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws IdentifierNotFoundException {
        log.debug("select(String)");
        log.debug("  ident [{}]", ident);
        return bean(
            factories().adql().resources().select(
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
     * @param input The {@link BlueQuery} input, [{@value BlueQueryController#INPUT_PARAM_NAME}].
     * @return A new {@link BlueQuery} wrapped in an {@link BlueQueryBean}.
     * @throws InvalidStateTransitionException 
     * @throws IdentifierFormatException 
     * @throws HibernateConvertException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<BlueQueryBean> create(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident,
        @RequestParam(value=BlueQueryController.INPUT_PARAM_NAME, required=false)
        final String input
        ) throws
            IdentifierNotFoundException,
            IdentifierFormatException,
            InvalidStateTransitionException,
            HibernateConvertException
            {
        log.debug("create(String, String)");
        log.debug("  ident [{}]", ident);
        log.debug("  input [{}]", input);
        return created(
            factories().adql().resources().select(
                factories().adql().resources().idents().ident(
                    ident
                    )
                ).blues().create(
                    input
                    )
            );
        }
    }
