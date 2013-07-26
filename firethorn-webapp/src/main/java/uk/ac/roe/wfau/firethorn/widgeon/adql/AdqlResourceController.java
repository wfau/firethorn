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

import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>AdqlResource</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlResourceLinkFactory.RESOURCE_PATH)
public class AdqlResourceController
    extends AbstractEntityController<AdqlResource, AdqlResourceBean>
    {

    @Override
    public Path path()
        {
        return path(
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
     * VOSI URL path.
     *
     */
    public static final String VOSI_PATH = "vosi" ;

    /**
     * The VOSI view generator.
     *
     */
    public static final String VOSI_XML_VIEW = "adql/vosi-xml" ;

    /**
     * MVC property for the target entity.
     *
     */
    public static final String TARGET_ENTITY = "urn:adql.resource.entity" ;

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

    @Override
    public Iterable<AdqlResourceBean> bean(final Iterable<AdqlResource> iter)
        {
        return new AdqlResourceBean.Iter(
            iter
            );
        }

    @Override
    public AdqlResourceBean bean(final AdqlResource entity)
        {
        return new AdqlResourceBean(
            entity
            );
        }

    /**
     * Get the target entity based on the ident in the path.
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public AdqlResource entity(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException  {
        log.debug("entity() [{}]", ident);
        return factories().adql().resources().select(
            factories().adql().resources().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public AdqlResourceBean select(
        @ModelAttribute(TARGET_ENTITY)
        final AdqlResource entity
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
    public AdqlResourceBean update(
        @RequestParam(value=UPDATE_NAME, required=false) final
        String name,
        @RequestParam(value=UPDATE_STATUS, required=false) final
        String status,
        @ModelAttribute(TARGET_ENTITY)
        final AdqlResource entity
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
                    BaseComponent.Status.valueOf(
                        status
                        )
                    );
                }
            }

        return bean(
            entity
            );
        }

    /**
     * VOSI GET request.
     *
     */
    @RequestMapping(value=VOSI_PATH, method=RequestMethod.GET)
    public String vosi(
        @ModelAttribute(TARGET_ENTITY)
        final AdqlResource entity
        ){
        return VOSI_XML_VIEW ;
        }

    }
