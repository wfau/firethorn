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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.entity.DateNameFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlResourceLinkFactory;

/**
 * Spring MVC controller to handle {@link AdqlResource} entities.
 * <br/>Controller path : [{@value AdqlResourceLinkFactory#ENTITY_PATH}]
 *
 */
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
     * Our{@link BlueQuery.NameFactory} implementation.
     *
     */
    @Component
    public static class NameFactory
    extends DateNameFactory<AdqlResource>
    implements AdqlResource.NameFactory
        {
        @Override
        public String name()
            {
            return datename();
            }
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
     * VOSI URL path, [{@value}].
     * @todo Move to AdqlResourceModel
     *
     */
    public static final String VOSI_PATH = "vosi" ;

    /**
     * The VOSI JSP page name, [{@value}].
     * @todo Move to AdqlResourceModel
     *
     */
    public static final String VOSI_VIEW = "adql/vosi-xml" ;

    /**
     * The VOSI content type, [{@value}].
     * @todo Move to AdqlResourceModel
     *
     */
    public static final String VOSI_MIME = "application/x-vosi+xml" ;

    /**
     * MVC property for the {@link AdqlResource}, [{@value}].
     * @todo Move to AdqlResourceModel
     *
     */
    public static final String TARGET_ENTITY = "urn:adql.resource.entity" ;

    /**
     * MVC property for the {@link AdqlResource} name, [{@value}].
     * @todo Move to AdqlResourceModel
     *
     */
    public static final String RESOURCE_NAME_PARAM = "adql.resource.update.name" ;

    /**
     * MVC property for the {@link AdqlResource} {@link BaseComponent.Status}, [{@value}].
     * @todo Move to AdqlResourceModel
     *
     */
    public static final String RESOURCE_STATUS_PARAM = "adql.resource.update.status" ;

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
     * Get the target {@link AdqlResource} based on the {@Identifier} in the request path.
     * @param ident The {@link AdqlResource} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The target {@link AdqlResource}.
     * @throws IdentifierNotFoundException If the {@link AdqlResource} could not be found.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public AdqlResource entity(
        @PathVariable("ident")
        final String ident
        )
    throws IdentifierNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().adql().resources().entities().select(
            factories().adql().resources().idents().ident(
                ident
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to select a specific {@link AdqlResource}.
     * <br/>Request path : [{@value AdqlResourceLinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param resource The {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @return The selected {@link AdqlResource} wrapped in a {@link AdqlResourceBean}.
     * 
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<AdqlResourceBean> select(
        @ModelAttribute(TARGET_ENTITY)
        final AdqlResource resource
        ){
        return selected(
            resource
            );
        }

    /**
     * {@link RequestMethod#POST} request to update an {@link AdqlResource}.
     * <br/>Request path : [{@value AdqlResourceLinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param resource The {@link AdqlResource} selected using the {@Identifier} in the request path.
     * <br/>Optional {@link AdqlResource} params :
     * @param name   The {@link AdqlResource} name, [{@value #RESOURCE_NAME_PARAM}].
     * @param status The {@link AdqlResource} {@link BaseComponent.Status}, [{@value #RESOURCE_STATUS_PARAM}].
     * @return The updated {@link AdqlResource} wrapped in a {@link AdqlResourceBean}.
     * @throws ProtectionException 
     * @throws NameFormatException 
     * 
     */
    @ResponseBody
    @UpdateAtomicMethod
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<AdqlResourceBean> update(
        @ModelAttribute(TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=RESOURCE_NAME_PARAM, required=false)
        final String name,
        @RequestParam(value=RESOURCE_STATUS_PARAM, required=false)
        final String status
        )
    throws NameFormatException, ProtectionException
        {
        if (name != null)
            {
            if (name.length() > 0)
                {
                resource.name(
                    name
                    );
                }
            }

        if (status != null)
            {
            if (status.length() > 0)
                {
                resource.status(
                    BaseComponent.Status.valueOf(
                        status
                        )
                    );
                }
            }

        return selected(
            resource
            );
        }

    /**
     * {@link RequestMethod#GET} request for a VOSI view of an {@link AdqlResource}.
     * <br/>Request path : [{@value #VOSI_PATH}]
     * <br/>Content type : [{@value #VOSI_MIME}]
     * @param resource The {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @return The name of the VOSI JSP page, [{@value #VOSI_VIEW}].
     * 
     */
    @RequestMapping(value=VOSI_PATH, method=RequestMethod.GET, produces=VOSI_MIME)
    public String vosi(
        @ModelAttribute(TARGET_ENTITY)
        final AdqlResource resource
        ){
        return VOSI_VIEW ;
        }
    }
