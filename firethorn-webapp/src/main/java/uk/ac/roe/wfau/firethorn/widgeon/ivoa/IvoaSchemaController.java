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
package uk.ac.roe.wfau.firethorn.widgeon.ivoa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.IvoaSchemaLinkFactory;

/**
 * Spring MVC controller for <code>IvoaSchema</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(IvoaSchemaLinkFactory.SCHEMA_PATH)
public class IvoaSchemaController
    extends AbstractEntityController<IvoaSchema, IvoaSchemaBean>
    {

    @Override
    public Path path()
        {
        return path(
            IvoaSchemaLinkFactory.SCHEMA_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public IvoaSchemaController()
        {
        super();
        }

    /**
     * MVC property for the target entity.
     *
     */
    public static final String TARGET_ENTITY = "urn:ivoa.schema.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String SCHEMA_NAME_PARAM = "ivoa.schema.name" ;

    @Override
    public Iterable<IvoaSchemaBean> bean(final Iterable<IvoaSchema> iter)
        {
        return new IvoaSchemaBean.Iter(
            iter
            );
        }

    @Override
    public IvoaSchemaBean bean(final IvoaSchema entity)
        {
        return new IvoaSchemaBean(
            entity
            );
        }

    /**
     * Get the target schema based on the identifier in the request.
     * @throws EntityNotFoundException
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public IvoaSchema entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws EntityNotFoundException {
        log.debug("schema() [{}]", ident);
        return factories().ivoa().schemas().entities().select(
            factories().ivoa().schemas().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public IvoaSchemaBean select(
        @ModelAttribute(TARGET_ENTITY)
        final IvoaSchema entity
        ){
        log.debug("select()");
        return bean(
            entity
            );
        }

    }
