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

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.mallard.AdqlService;
import uk.ac.roe.wfau.firethorn.mallard.AdqlServiceEntity;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponent.Status;

/**
 * Spring MVC controller for <code>AdqlService</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlServiceIdentFactory.SERVICE_PATH)
public class AdqlServiceController
extends AbstractController
    {

    @Override
    public Path path()
        {
        return new PathImpl(
            AdqlServiceIdentFactory.SERVICE_PATH 
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlServiceController()
        {
        super();
        }

    /**
     * MVC property for the target AdqlService entity.
     *
     */
    public static final String SERVICE_ENTITY = "urn:adql.service.entity" ;

    /**
     * MVC property for the target AdqlResource entity.
     *
     */
    public static final String RESOURCE_ENTITY = "urn:adql.resource.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String UPDATE_NAME = "adql.service.update.name" ;

    /**
     * MVC property for updating the status.
     *
     */
    public static final String UPDATE_STATUS = "adql.service.update.status" ;
    
    /**
     * Wrap an entity as a bean.
     *
     */
    public AdqlServiceBean bean(
        final AdqlService entity
        ){
        log.debug("bean() [{}]", entity);
        return new AdqlServiceBean(
            entity
            );
        }

    /**
     * Get the target entity based on the ident in the path.
     *
     */
    @ModelAttribute(SERVICE_ENTITY)
    public AdqlService entity(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException  {
        log.debug("entity(}");
        log.debug("ident [{}]", ident);
        AdqlService entity = womble().adql().services().select(
            womble().adql().services().ident(
                ident
                )
            );
        return entity ;
        }

    /**
     * HTML GET request for a service.
     *
     */
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView htmlSelect(
        final ModelAndView model
	    ){
	    model.setViewName(
	        "adql/services/display"
	        );
        return model ;
        }

	/**
     * JSON GET request for a service.
     *
     */
	@ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public AdqlServiceBean jsonSelect(
        @ModelAttribute(SERVICE_ENTITY)
        final AdqlServiceEntity entity
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
    public AdqlServiceBean jsonUpdate(
        @RequestParam(value=UPDATE_NAME, required=false)
        String name,
        @RequestParam(value=UPDATE_STATUS, required=false)
        String status,
        @ModelAttribute(RESOURCE_ENTITY)
        final AdqlService entity
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

        /*
        if (status != null)
            {
            if (status.length() > 0)
                {
                entity.status(
                    Status.valueOf(
                        status
                        )
                    );
                }
            }
        */

        return bean(
            entity
            );
        }
    }

