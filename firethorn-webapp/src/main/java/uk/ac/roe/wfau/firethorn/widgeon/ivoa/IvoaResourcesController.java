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

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
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
implements IvoaResourceModel
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
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<Iterable<IvoaResourceBean>> select(
        final ModelAndView model
        )
    throws ProtectionException
        {
        return selected(
            factories().ivoa().resources().entities().select()
            );
        }

    /**
     * POST request to select an {@link AdqlResource} by {@link Identifier}.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=RESOURCE_IDENT_PARAM, produces=JSON_MIME)
    public ResponseEntity<IvoaResourceBean> select_by_ident(
        @RequestParam(RESOURCE_IDENT_PARAM)
        final String ident
        )
    throws IdentifierNotFoundException, ProtectionException
        {
        return selected(
            factories().ivoa().resources().entities().select(
                factories().ivoa().resources().idents().ident(
                    ident
                    )
                )
            );
        }
    
    /**
     * POST request to select an {@link AdqlResource} by name.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=RESOURCE_NAME_PARAM, produces=JSON_MIME)
    public ResponseEntity<IvoaResourceBean> select_by_name(
        @RequestParam(RESOURCE_NAME_PARAM)
        final String name
        )
    throws NameNotFoundException, ProtectionException
        {
        return selected(
            factories().ivoa().resources().entities().select(
                name
                )
            );
        }

    /**
     * JSON POST request to create a new resource.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<IvoaResourceBean> create(
        @RequestParam(value=RESOURCE_NAME_PARAM, required=true)
        final String name,
        @RequestParam(value=RESOURCE_ENDPOINT_PARAM, required=false)
        final String endpoint
        )
    throws ProtectionException
        {
        return created(
            factories().ivoa().resources().entities().create(
                name,
                endpoint
                )
            );
        }
    }
