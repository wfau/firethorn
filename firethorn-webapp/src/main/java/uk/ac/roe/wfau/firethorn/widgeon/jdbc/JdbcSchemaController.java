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
package uk.ac.roe.wfau.firethorn.widgeon.jdbc;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.JdbcSchemaLinkFactory;

/**
 * Spring MVC controller for <code>JdbcSchema</code>.
 *
 */
@Controller
@RequestMapping(JdbcSchemaLinkFactory.SCHEMA_PATH)
public class JdbcSchemaController
extends AbstractEntityController<JdbcSchema, JdbcSchemaBean>
implements JdbcSchemaModel
    {

    @Override
    public Path path()
        {
        return path(
            JdbcSchemaLinkFactory.SCHEMA_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcSchemaController()
        {
        super();
        }


    @Override
    public Iterable<JdbcSchemaBean> bean(final Iterable<JdbcSchema> iter)
        {
        return new JdbcSchemaBean.Iter(
            iter
            );
        }

    @Override
    public JdbcSchemaBean bean(final JdbcSchema entity)
        {
        return new JdbcSchemaBean(
            entity
            );
        }

    /**
     * Get the target schema based on the identifier in the request.
     * @throws EntityNotFoundException
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(JdbcSchemaModel.TARGET_ENTITY)
    public JdbcSchema entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws EntityNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().jdbc().schemas().entities().select(
            factories().jdbc().schemas().idents().ident(
                ident
                )
            );
        }

    /**
     * GET request to select a {@link JdbcSchema}.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<JdbcSchemaBean> select(
        @ModelAttribute(JdbcSchemaModel.TARGET_ENTITY)
        final JdbcSchema entity
        ){
        return selected(
            entity
            );
        }
    }
