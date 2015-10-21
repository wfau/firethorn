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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.webapp.blue.BlueQueryController;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlResourceLinkFactory;

/**
 * Spring MVC controller to handle {@link AdqlResource} entities.
 * <br/>Controller path : [{@value AdqlResourceLinkFactory#SERVICE_PATH}]
 * @todo Better exception handling.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlResourceLinkFactory.SERVICE_PATH)
public class AdqlResourcesController
extends AbstractEntityController<AdqlResource, AdqlResourceBean>
    {

    @Override
    public Path path()
        {
        return path(
            AdqlResourceLinkFactory.SERVICE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlResourcesController()
        {
        super();
        }

    /**
     * MVC property for the {@link AdqlResource} name, [{@value}].
     *
     */
    public static final String CREATE_NAME = "adql.resource.create.name" ;

    @Override
    public AdqlResourceBean bean(final AdqlResource entity)
        {
        return new AdqlResourceBean(
            entity
            );
        }

    @Override
    public Iterable<AdqlResourceBean> bean(final Iterable<AdqlResource> iter)
        {
        return new AdqlResourceBean.Iter(
            iter
            );
        }

    /**
     * {@link RequestMethod#GET} request to select all the available {@link AdqlResource}.
     * <br/>Request path : [{@value #SELECT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @return An {@Iterable} set of {@link AdqlResourceBean}.
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public Iterable<AdqlResourceBean> select(
        ){
        return bean(
            factories().adql().resources().entities().select()
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link AdqlResource}.
     * <br/>Request path : [{@value #CREATE_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @return A new {@link AdqlResource} wrapped in an {@link AdqlResourceBean}.
     * 
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, params={}, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<AdqlResourceBean> create()
        {
        log.debug("create()");
        return created(
            factories().adql().resources().entities().create()
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link AdqlResource}.
     * <br/>Request path : [{@value #CREATE_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param name The {@link AdqlResource} name, [{@value #CREATE_NAME}]
     * @return A new {@link AdqlResource} wrapped in an {@link AdqlResourceBean}.
     * 
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, params={CREATE_NAME}, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<AdqlResourceBean> create(
        @RequestParam(value=CREATE_NAME, required=true)
        final String name
        ){
        log.debug("create(String)");
        return created(
            factories().adql().resources().entities().create(
                name
                )
            );
        }
    }
