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

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;

import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>AdqlResources</code>.
 * TODO better exception handling.
 *
 */
@Controller
@RequestMapping(AdqlResourceLinkFactory.SERVICE_PATH)
public class AdqlResourcesController
extends AbstractEntityController<AdqlResource>
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
     * MVC property for the select name.
     *
     */
    public static final String SELECT_NAME = "adql.resource.select.name" ;

    /**
     * MVC property for the select results.
     *
     */
    public static final String SELECT_RESULT = "adql.resource.select.result" ;

    /**
     * MVC property for the create name.
     *
     */
    public static final String CREATE_NAME = "adql.resource.create.name" ;

    @Override
    public EntityBean<AdqlResource> bean(final AdqlResource entity)
        {
        return new AdqlResourceBean(
            entity
            );
        }

    @Override
    public Iterable<EntityBean<AdqlResource>> bean(final Iterable<AdqlResource> iter)
        {
        return new AdqlResourceBean.Iter(
            iter
            );
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public Iterable<EntityBean<AdqlResource>> select(
        ){
        return bean(
            factories().adql().resources().select()
            );
        }

    /**
     * JSON POST request to create a new resource.
     *
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<EntityBean<AdqlResource>> create(
        @RequestParam(value=CREATE_NAME, required=true)
        final String name
        ){
        return created(
            factories().adql().resources().create(
                name
                )
            );
        }
    }
