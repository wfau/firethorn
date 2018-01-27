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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlSchemaLinkFactory;

/**
 * Spring MVC controller to handle {@link AdqlSchema} entities.
 * <br/>Controller path : [{@value AdqlSchemaLinkFactory#ENTITY_PATH}]
 *
 */
@Controller
@RequestMapping(AdqlSchemaLinkFactory.SCHEMA_PATH)
public class AdqlSchemaController
    extends AbstractEntityController<AdqlSchema, AdqlSchemaBean>
    {

    @Override
    public Path path()
        {
        return path(
            AdqlSchemaLinkFactory.SCHEMA_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlSchemaController()
        {
        super();
        }

    /**
     * MVC property for the {@link AdqlSchema}, [{@value}].
     *
     */
    public static final String TARGET_ENTITY = "urn:adql.schema.entity" ;

    /**
     * MVC property for the {@link AdqlSchema} name, [{@value}].
     * @todo Merge create, select and update.
     *
     */
    public static final String SCHEMA_NAME_PARAM = "adql.schema.update.name" ;

    @Override
    public Iterable<AdqlSchemaBean> bean(final Iterable<AdqlSchema> iter)
        {
        return new AdqlSchemaBean.Iter(
            iter
            );
        }

    @Override
    public AdqlSchemaBean bean(final AdqlSchema entity)
        {
        return new AdqlSchemaBean(
            entity
            );
        }

    /**
     * Get the target {@link AdqlSchema} based on the {@Identifier} in the request path.
     * @param ident The {@link AdqlSchema} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The target {@link AdqlSchema}.
     * @throws IdentifierNotFoundException If the {@link AdqlSchema} could not be found.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public AdqlSchema entity(
        @PathVariable("ident")
        final String ident
        )
    throws IdentifierNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().adql().schemas().entities().select(
            factories().adql().schemas().idents().ident(
                ident
                )
            );
        }

    /**
     * {@link RequestMethod#GET} request to select a specific {@link AdqlSchema}.
     * <br/>Request path : [{@value AdqlSchemaLinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param entity The {@link AdqlSchema} selected using the {@Identifier} in the request path.
     * @return The selected {@link AdqlSchema} wrapped in a {@link AdqlSchemaBean}.
     * 
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<AdqlSchemaBean> select(
        @ModelAttribute(TARGET_ENTITY)
        final AdqlSchema entity
        ){
        return selected(
            entity
            );
        }

    /**
     * {@link RequestMethod#POST} request to update a specific {@link AdqlSchema}.
     * <br/>Request path : [{@value AdqlSchemaLinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param entity The {@link AdqlSchema} selected using the {@Identifier} in the request path.
     * <br/>Optional {@link AdqlSchema} params :
     * @param name   The {@link AdqlSchema} name, [{@value #SCHEMA_NAME_PARAM}].
     * @return The updated {@link AdqlSchema} wrapped in a {@link AdqlSchemaBean}.
     * @throws ProtectionException 
     * @throws NameFormatException 
     * 
     */
    @ResponseBody
    @UpdateAtomicMethod
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<AdqlSchemaBean> update(
        @ModelAttribute(TARGET_ENTITY)
        final AdqlSchema entity,
        @RequestParam(value=SCHEMA_NAME_PARAM, required=false)
        final String name
        )
    throws NameFormatException, ProtectionException
        {
        if (name != null)
            {
            if (name.length() > 0)
                {
                entity.name(
                    name
                    );
                }
            }
        return selected(
            entity
            );
        }
    }
