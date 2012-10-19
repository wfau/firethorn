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
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcCatalogBean;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcCatalogBeanIter;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceController;

/**
 * Spring MVC controller for <code>AdqlService</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlServiceIdentFactory.RESOURCES_PATH)
public class AdqlServiceResourcesController
extends AbstractController
    {

    @Override
    public Path path()
        {
        return new PathImpl(
            AdqlServiceIdentFactory.RESOURCES_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlServiceResourcesController()
        {
        super();
        }

    /**
     * URL path for the select method.
     *
     */
    public static final String SELECT_PATH = "select" ;

    /**
     * URL path for the search method.
     *
     */
    public static final String SEARCH_PATH = "search" ;

    /**
     * URL path for the insert method.
     *
     */
    public static final String INSERT_PATH = "insert" ;

    /**
     * MVC property for the select name.
     *
     */
    public static final String SELECT_NAME = "adql.service.resources.select.name" ;

    /**
     * MVC property for the select results.
     *
     */
    public static final String SELECT_RESULT = "adql.service.resources.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "adql.service.resources.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "adql.service.resources.search.result" ;

    /**
     * MVC property for the insert name.
     *
     */
    public static final String INSERT_NAME = "adql.service.resources.insert.name" ;
    
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
     * Get the target AdqlService  based on the ident in the path.
     * @todo inherit from AdqlServiceController ?
     *
     */
    @ModelAttribute(AdqlServiceController.SERVICE_ENTITY)
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
     * Select all.
     *
     */
    public AdqlResourceBeanIter select(
        @ModelAttribute(AdqlServiceController.SERVICE_ENTITY)
        final AdqlService service
        ){
        log.debug("select()");
        return new AdqlResourceBeanIter(
            service.resources().select()
            );
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public AdqlResourceBeanIter jsonSelect(
        @ModelAttribute(AdqlServiceController.SERVICE_ENTITY)
        final AdqlService service
        ){
        log.debug("jsonSelect()");
        return select(
            service
            );
        }

    /**
     * Select by name.
     *
     */
    public AdqlResourceBean select(
        @ModelAttribute(AdqlServiceController.SERVICE_ENTITY)
        final AdqlService service,
        final String name
        ){
        log.debug("select(String) [{}]", name);
        return new AdqlResourceBean(
            service.resources().select(
                name
                )
            );
        }
    
    }

