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

import uk.ac.roe.wfau.firethorn.common.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayBaseComponent;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>AdqlResource</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlResourceLinkFactory.RESOURCE_PATH)
public class AdqlResourceController
    extends AbstractController
    {

    @Override
    public Path path()
        {
        return new PathImpl(
            AdqlResourceLinkFactory.RESOURCE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlResourceController()
        {
        super();
        }

    /**
     * MVC property for the target entity.
     *
     */
    public static final String RESOURCE_ENTITY = "urn:adql.resource.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String UPDATE_NAME = "adql.resource.update.name" ;

    /**
     * MVC property for updating the status.
     *
     */
    public static final String UPDATE_STATUS = "adql.resource.update.status" ;

    /**
     * Wrap an entity as a bean.
     *
     */
    public AdqlResourceBean bean(
        final TuesdayAdqlResource entity
        ){
        log.debug("bean() [{}]", entity);
        return new AdqlResourceBean(
            entity
            );
        }

    /**
     * Get the target entity based on the ident in the path.
     *
     */
    @ModelAttribute(RESOURCE_ENTITY)
    public TuesdayAdqlResource entity(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException  {
        log.debug("entity(}");
        log.debug("ident [{}]", ident);
        final TuesdayAdqlResource entity = factories().adql().resources().select(
            factories().adql().resources().idents().ident(
                ident
                )
            );
        return entity ;
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public AdqlResourceBean jsonSelect(
        @ModelAttribute(RESOURCE_ENTITY)
        final TuesdayAdqlResource entity
        ){
        return bean(
            entity
            );
        }

    /**
     * JSON POST update.
     *
     */
    @ResponseBody
    @UpdateAtomicMethod
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MAPPING)
    public AdqlResourceBean jsonUpdate(
        @RequestParam(value=UPDATE_NAME, required=false) final
        String name,
        @RequestParam(value=UPDATE_STATUS, required=false) final
        String status,
        @ModelAttribute(RESOURCE_ENTITY)
        final TuesdayAdqlResource entity
        ){

        if (name != null)
            {
            if (name.length() > 0)
                {
                entity.name(
                    name
                    );
                }
            }

        if (status != null)
            {
            if (status.length() > 0)
                {
                entity.status(
                    TuesdayBaseComponent.Status.valueOf(
                        status
                        )
                    );
                }
            }

        return bean(
            entity
            );
        }
    }
