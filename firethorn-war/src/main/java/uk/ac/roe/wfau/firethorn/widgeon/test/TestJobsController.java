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
package uk.ac.roe.wfau.firethorn.widgeon.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.RedirectHeader;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>JdbcResource</code>.
 * TODO better exception handling.
 * http://blog.newsplore.com/2010/08/04/restful-error-handling-with-tomcat-springmvc
 * http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/util/AntPathMatcher.html
 *
 */
@Controller
@RequestMapping(TestJobLinkFactory.SERVICE_PATH)
public class TestJobsController
extends AbstractController
    {

    @Override
    public Path path()
        {
        return path(
            TestJobLinkFactory.SERVICE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public TestJobsController()
        {
        super();
        }

    /**
     * URL path for the select method.
     * @todo These are fairly standard - should be in a shared base class.  
     *
     */
    public static final String SELECT_PATH = "select" ;

    /**
     * URL path for the search method.
     * @todo These are fairly standard - should be in a shared base class.  
     *
     */
    public static final String SEARCH_PATH = "search" ;

    /**
     * URL path for the create method.
     * @todo These are fairly standard - should be in a shared base class.  
     *
     */
    public static final String CREATE_PATH = "create" ;

    /**
     * MVC property for the initial name.
     * @todo These are fairly standard - created from a base in a shared base class.  
     *
     */
    public static final String CREATE_NAME = "test.job.create.name" ;

    /**
     * MVC property for the pause.
     *
     */
    public static final String CREATE_PAUSE = "test.job.create.pause" ;
    
    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public Iterable<TestJobController.Bean> select(
        ){
        return TestJobController.bean(
            factories().tests().factory().select()
            );
        }

    /**
     * JSON POST request to create a new job.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<TestJobController.Bean> create(
        @RequestParam(value=CREATE_NAME, required=true)
        final String name,
        @RequestParam(value=CREATE_PAUSE, required=false)
        final Integer pause
        ){
        final TestJobController.Bean bean = TestJobController.bean(
            factories().tests().factory().create(
                name,
                pause
                )
            );
        return new ResponseEntity<TestJobController.Bean>(
            bean,
            new RedirectHeader(
                bean
                ),
            HttpStatus.CREATED
            );
        }
    }
