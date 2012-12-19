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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.RedirectHeader;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>AdqlResources</code>.
 * TODO better exception handling.
 *
 */
@Controller
@RequestMapping(AdqlResourceLinkFactory.SERVICE_PATH)
public class AdqlResourcesController
extends AbstractController
    {

    @Override
    public Path path()
        {
        return new PathImpl(
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
     * URL path for the create method.
     *
     */
    public static final String CREATE_PATH = "create" ;

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
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "adql.resource.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "adql.resource.search.result" ;

    /**
     * MVC property for the create name.
     *
     */
    public static final String CREATE_NAME = "adql.resource.create.name" ;

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public AdqlResourceBean.Iter jsonSelect(
        final ModelAndView model
        ){
        return new AdqlResourceBean.Iter(
            factories().adql().resources().select()
            );
        }

    /**
     * JSON GET or POST request to search by text.
     *
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public AdqlResourceBean.Iter jsonSearch(
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
        ){
        return new AdqlResourceBean.Iter(
            factories().adql().resources().search(
                text
                )
            );
        }

    /**
     * JSON POST request to create a new resource.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<AdqlResourceBean> jsonCreate(
        @RequestParam(value=CREATE_NAME, required=true)
        final String name,
        final ModelAndView model
        ){
        final AdqlResourceBean bean = new AdqlResourceBean(
            factories().adql().resources().create(
                name
                )
            );
        return new ResponseEntity<AdqlResourceBean>(
            bean,
            new RedirectHeader(
                bean
                ),
            HttpStatus.CREATED
            );
        }
    }
