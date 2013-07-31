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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent.CopyDepth;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.xml.MetaDocReader;
import uk.ac.roe.wfau.firethorn.util.xml.XMLParserException;
import uk.ac.roe.wfau.firethorn.util.xml.XMLReaderException;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

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
    public static final String METADOC_IMPORT_FILE = "urn:schema.metadoc.file" ;

    /**
     * MVC property for the import metadoc base.
     *
     */
    public static final String METADOC_IMPORT_BASE = "urn:schema.metadoc.base" ;

    @Override
    public AdqlSchemaBean bean(final AdqlSchema entity)
        {
        return new AdqlSchemaBean(
            entity
            );
        }

    @Override
    public Iterable<AdqlSchemaBean > bean(final Iterable<AdqlSchema> iter)
        {
        return new AdqlSchemaBean.Iter(
            iter
            );
        }

    /**
     * Get the parent entity based on the request ident.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
    public AdqlResource parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws IdentifierNotFoundException {
        log.debug("parent() [{}]", ident);
        return factories().adql().resources().select(
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
     * Import schema from using metadata from a metadoc.
     * @throws IOException 
     * @throws XMLReaderException 
     * @throws XMLParserException 
     * @throws NameNotFoundException 
     * @throws IdentifierNotFoundException 
     *
     */
    @ResponseBody
    @RequestMapping(value=METADOC_IMPORT_PATH, method=RequestMethod.POST, produces=JSON_MAPPING)
    public Iterable<AdqlSchemaBean> inport(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=ADQL_COPY_DEPTH_URN, required=false)
        final CopyDepth depth,
        @RequestParam(value=METADOC_IMPORT_BASE, required=true)
        final String base,
        @RequestPart(value=METADOC_IMPORT_FILE, required=true)
        final MultipartFile metadoc
        ) throws XMLParserException, XMLReaderException, IOException, IdentifierNotFoundException, NameNotFoundException {
        log.debug("inport(CopyDepth, BaseSchema, File) [{}][{}]", depth, base);
        return bean(
            reader.inport(
                new InputStreamReader(
                    metadoc.getInputStream()
                    ),
                factories().base().schema().select(
                    factories().base().schema().links().ident(
                        base
                        )
                    ),
                resource
                )
            );
        }
    }
