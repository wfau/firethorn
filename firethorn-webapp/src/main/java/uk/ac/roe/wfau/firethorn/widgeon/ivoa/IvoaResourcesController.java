/*
 *  Copyright (C) 2016 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.widgeon.ivoa;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.IvoaResourceLinkFactory;

/**
 *
 * 
 */
@Controller
@RequestMapping(IvoaResourceLinkFactory.SERVICE_PATH)
public class IvoaResourcesController
extends AbstractEntityController<IvoaResource, IvoaResourceBean>
    {

    @Override
    public Path path()
        {
        return path(
            IvoaResourceLinkFactory.SERVICE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public IvoaResourcesController()
        {
        super();
        }

    /**
     * MVC property for the resource name.
     *
     */
    public static final String RESOURCE_NAME_PARAM = "ivoa.resource.name" ;

    /**
     * MVC property for the resource endpoint.
     *
     */
    public static final String RESOURCE_ENDPOINT_PARAM = "ivoa.resource.endpoint" ;

    @Override
    public IvoaResourceBean bean(final IvoaResource entity)
        {
        return new IvoaResourceBean(
            entity
            );
        }

    @Override
    public Iterable<IvoaResourceBean> bean(final Iterable<IvoaResource> iter)
        {
        return new IvoaResourceBean.Iter(
            iter
            );
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public Iterable<IvoaResourceBean> select(
        final ModelAndView model
        ){
        return bean(
            factories().ivoa().resources().entities().select()
            );
        }

    /**
     * JSON POST request to create a new resource.
     *
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<IvoaResourceBean> create(
        @RequestParam(value=RESOURCE_NAME_PARAM, required=true)
        final String name,
        @RequestParam(value=RESOURCE_ENDPOINT_PARAM, required=false)
        final String endpoint
        ){
        return created(
            factories().ivoa().resources().entities().create(
                name,
                endpoint
                )
            );
        }
    }
