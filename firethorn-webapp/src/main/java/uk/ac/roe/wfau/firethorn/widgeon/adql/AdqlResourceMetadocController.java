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

import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.xml.MetaDocReader;
import uk.ac.roe.wfau.firethorn.util.xml.XMLParserException;
import uk.ac.roe.wfau.firethorn.util.xml.XMLReaderException;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlResourceLinkFactory;

/**
 *
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlResourceLinkFactory.RESOURCE_METADOC_PATH)
public class AdqlResourceMetadocController
extends AbstractEntityController<AdqlSchema, AdqlSchemaBean>
    {
    @Override
    public Path path()
        {
        return path(
            AdqlResourceLinkFactory.RESOURCE_METADOC_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlResourceMetadocController()
        {
        super();
        }

    /**
     * URL path for the metadoc import method.
     *
     */
    public static final String METADOC_IMPORT_PATH = "import" ;

    /**
     * MVC property for the import metadoc file.
     *
     */
    public static final String METADOC_IMPORT_FILE = "metadoc.file" ;

    /**
     * MVC property for the import metadoc base.
     *
     */
    public static final String METADOC_IMPORT_BASE = "metadoc.base" ;

    // TODO Move these to a bean factory ?
    @Override
    public AdqlSchemaBean bean(final AdqlSchema entity)
        {
        return new AdqlSchemaBean(
            entity
            );
        }

    // TODO Move these to a bean factory ?
    @Override
    public Iterable<AdqlSchemaBean > bean(final Iterable<AdqlSchema> iter)
        {
        return new AdqlSchemaBean.Iter(
            iter
            );
        }

    /**
     * Get the parent {@link AdqlResource} based on the ident in the request.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     * @throws EntityNotFoundException
     *
     */
    @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
    public AdqlResource parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
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
     * Our XML metadoc reader.
     *
     */
    protected MetaDocReader reader = new MetaDocReader();

    /**
     * Import tables from a base schema using metadata from a metadoc.
     * @throws IOException
     * @throws XMLReaderException
     * @throws XMLParserException
     * @throws EntityNotFoundException 
     * @throws IdentifierFormatException 
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=METADOC_IMPORT_PATH, method=RequestMethod.POST, produces=JSON_MIME)
    public Iterable<AdqlSchemaBean> inport(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=METADOC_IMPORT_BASE, required=true)
        final String base,
        @RequestPart(value=METADOC_IMPORT_FILE, required=true)
        final MultipartFile metadoc
        )
    throws XMLParserException, XMLReaderException, IOException, IdentifierFormatException, EntityNotFoundException, ProtectionException
        {
        return bean(
            reader.inport(
                new InputStreamReader(
                    metadoc.getInputStream()
                    ),
                factories().base().schema().resolve(
                    base
                    ),
                resource
                )
            );
        }
    }
