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

import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaSchema;
import uk.ac.roe.wfau.firethorn.meta.vosi.VosiTableSetReader;
import uk.ac.roe.wfau.firethorn.util.xml.XMLParserException;
import uk.ac.roe.wfau.firethorn.util.xml.XMLReaderException;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.IvoaResourceLinkFactory;

/**
 * Spring MVC controller for <code>IvoaResource</code>.
 *
 */
@Controller
@RequestMapping(IvoaResourceLinkFactory.VOSI_IMPORT_PATH)
public class IvoaResourceImportController
    extends AbstractEntityController<IvoaSchema, IvoaSchemaBean>
    {

    @Override
    public Path path()
        {
        return path(
            IvoaResourceLinkFactory.VOSI_IMPORT_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public IvoaResourceImportController()
        {
        super();
        }

    /**
     * MVC property for the target resource.
     *
     */
    public static final String TARGET_ENTITY = "urn:ivoa.resource.entity" ;

    @Override
    public IvoaSchemaBean bean(final IvoaSchema entity)
        {
        return new IvoaSchemaBean(
            entity
            );
        }

    @Override
    public Iterable<IvoaSchemaBean> bean(final Iterable<IvoaSchema> iter)
        {
        return new IvoaSchemaBean.Iter(
            iter
            );
        }

    /**
     * Get the target resource based on the identifier in the request.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public IvoaResource entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws EntityNotFoundException, IdentifierFormatException, ProtectionException
        {
        final IvoaResource entity = factories().ivoa().resources().entities().select(
            factories().ivoa().resources().idents().ident(
                ident
                )
            );
        return entity ;
        }

    /**
     * MVC property for the VOSI file.
     * 
     */
    protected static final String VOSI_IMPORT_FILE = "vosi.tableset" ;
    
    /**
     * Import a VOSI tableset document.
     * @throws IOException
     * @throws XMLReaderException
     * @throws XMLParserException
     * @throws EntityNotFoundException 
     * @throws IdentifierFormatException 
     * @throws DuplicateEntityException 
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<Iterable<IvoaSchemaBean>> inport(
        @ModelAttribute(TARGET_ENTITY)
        final IvoaResource resource,
        @RequestPart(value=VOSI_IMPORT_FILE, required=true)
        final MultipartFile vosidata
        )
    throws XMLParserException, XMLReaderException, IOException, IdentifierFormatException, EntityNotFoundException, DuplicateEntityException, ProtectionException
        {

        // TODO Support flexible namespaces
        // TODO Support update (duplicate => update)
        
        VosiTableSetReader reader = new VosiTableSetReader();

        reader.inport(
            new InputStreamReader(
                vosidata.getInputStream()
                ),
            resource
            );

        return selected(
            resource.schemas().select()
            );
        }
    }
