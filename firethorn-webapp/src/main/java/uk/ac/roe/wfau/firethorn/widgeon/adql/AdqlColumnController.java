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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.GreenQuery;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlColumnLinkFactory;

/**
 * Spring MVC controller to handle {@link AdqlColumn} entities.
 * <br/>Controller path : [{@value AdqlColumnLinkFactory#ENTITY_PATH}]
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlColumnLinkFactory.ENTITY_PATH)
public class AdqlColumnController
extends AbstractEntityController<AdqlColumn, AdqlColumnBean>
    {

    @Override
    public Path path()
        {
        return path(
            AdqlColumnLinkFactory.ENTITY_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlColumnController()
        {
        super();
        }

    /**
     * MVC property for the {@link AdqlColumn}, [{@value}].
     *
     */
    public static final String TARGET_ENTITY = "urn:adql.column.entity" ;

    /**
     * MVC property for the {@link AdqlColumn} name, [{@value}].
     *
     */
    public static final String UPDATE_NAME = "adql.column.update.name" ;

    @Override
    public AdqlColumnBean bean(final AdqlColumn entity)
        {
        return new AdqlColumnBean(
            entity
            );
        }

    @Override
    public Iterable<AdqlColumnBean> bean(final Iterable<AdqlColumn> iter)
        {
        return new AdqlColumnBean.Iter(
            iter
            );
        }

    /**
     * Get the target {@link AdqlColumn} based on the {@Identifier} in the request path.
     * @param ident The {@link AdqlColumn} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The target {@link AdqlColumn}.
     * @throws IdentifierNotFoundException If the {@link AdqlColumn} could not be found.
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public AdqlColumn entity(
        @PathVariable("ident")
        final String ident
        ) throws IdentifierNotFoundException {
        log.debug("entity() [{}]", ident);
        return factories().adql().columns().entities().select(
            factories().adql().columns().idents().ident(
                ident
                )
            );
        }

    /**
     * {@link RequestMethod#GET} request to select a specific {@link AdqlColumn}.
     * <br/>Request path : [{@value AdqlColumnLinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param column The {@link AdqlColumn} selected using the {@Identifier} in the request path.
     * @return The selected {@link AdqlColumn} wrapped in a {@link AdqlColumnBean}.
     * 
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public AdqlColumnBean select(
        @ModelAttribute(TARGET_ENTITY)
        final AdqlColumn column
        ){
        return bean(
            column
            );
        }

    /**
     * {@link RequestMethod#POST} request to update a specific {@link AdqlColumn}.
     * <br/>Request path : [{@value AdqlColumnLinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param column The {@link AdqlColumn} selected using the {@Identifier} in the request path.
     * <br/>Optional {@link AdqlColumn} params :
     * @param name  The {@link AdqlColumn} name, [{@value #UPDATE_NAME}].
     * @return The updated {@link AdqlColumn} wrapped in a {@link AdqlColumnBean}.
     * 
     */
    @ResponseBody
    @UpdateAtomicMethod
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MIME)
    public AdqlColumnBean update(
        @RequestParam(value=UPDATE_NAME, required=false)
        final String name,
        @ModelAttribute(TARGET_ENTITY)
        final AdqlColumn column
        ){
        if (name != null)
            {
            if (name.length() > 0)
                {
                column.name(
                    name
                    );
                }
            }
        return bean(
            column
            );
        }
    }
