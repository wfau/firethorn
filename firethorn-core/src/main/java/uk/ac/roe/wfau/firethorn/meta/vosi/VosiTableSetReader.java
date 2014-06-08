/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.vosi;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaColumn;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaSchema;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;
import uk.ac.roe.wfau.firethorn.util.xml.XMLParserException;
import uk.ac.roe.wfau.firethorn.util.xml.XMLReader;
import uk.ac.roe.wfau.firethorn.util.xml.XMLReaderImpl;
import uk.ac.roe.wfau.firethorn.util.xml.XMLReaderException;
import uk.ac.roe.wfau.firethorn.util.xml.XMLStringValueReader;

/**
 *
 *
 */
@Slf4j
public class VosiTableSetReader
    extends XMLReaderImpl
    implements XMLReader
    {
    public static final String NAMESPACE_URI = "http://www.ivoa.net/xml/VODataService/v1.1";

    public VosiTableSetReader()
        {
        super(
            new QName(
                NAMESPACE_URI,
                "tableset"
                )
            );
        }

    private static final VosiSchemaReader schemareader = new VosiSchemaReader();  
    
    public void inport(final IvoaResource resource, final Reader reader)
    throws XMLParserException, XMLReaderException, NameNotFoundException, DuplicateEntityException
        {
        this.inport(
            resource,
            wrap(
                reader
                )
            );
        }

    public void inport(final IvoaResource resource, final XMLEventReader events)
    throws XMLParserException, XMLReaderException, DuplicateEntityException
        {
        this.start(
            events
            );

        IvoaSchema.Builder schemas = resource.schemas().builder(); 

        while (schemareader.match(events))
            {
            schemareader.inport(
                resource,
                schemas,
                events
                );
            }
        
        schemas.finish();

        this.done(
            events
            );
        }

    public static class VosiSchemaReader
    extends XMLReaderImpl
    implements XMLReader
        {
        public VosiSchemaReader()
            {
            super(
                NAMESPACE_URI,
                "schema"
                );
            }
        
        private static final XMLStringValueReader namereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "name",
            true
            );
        private static final XMLStringValueReader titlereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "title",
            false
            );
        private static final XMLStringValueReader textreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "description",
            false
            );
        private static final XMLStringValueReader utypereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "utype",
            false
            );

        private static final VosiTableReader tablereader = new VosiTableReader();

        public void inport(final IvoaResource resource, final IvoaSchema.Builder schemas, final XMLEventReader events)
        throws XMLParserException, XMLReaderException, DuplicateEntityException
            {
            this.start(
                events
                );

            final String name  = namereader.read(events); 
            final String title = titlereader.read(events);
            final String text  = textreader.read(events); 
            final String utype = utypereader.read(events); 

            log.debug("Schema [{}]", name);
            log.debug("    title [{}]", title);
            log.debug("    text  [{}]", text);
            log.debug("    utype [{}]", utype);

            IvoaSchema schema = schemas.select(
                name,
                new IvoaSchema.Builder.Worker()
                    {
                    @Override
                    public IvoaSchema create(final String name)
                    throws DuplicateEntityException
                        {
                        final IvoaSchema schema = resource.schemas().create(
                            name
                            );
                        update(schema);
                        return schema ;
                        }
                    @Override
                    public void update(final IvoaSchema schema)
                        {
                        schema.text(text);
                        }
                    }
                );

            IvoaTable.Builder tables = schema.tables().builder();            
            while (tablereader.match(events))
                {
                tablereader.inport(
                    schema,
                    tables,
                    events
                    );
                }

            schemas.finish();

            this.done(
                events
                );
            }
        }
    
    public static class VosiTableReader
    extends XMLReaderImpl
    implements XMLReader
        {
        public VosiTableReader()
            {
            super(
                NAMESPACE_URI,
                "table"
                );
            }

        private static final XMLStringValueReader namereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "name",
            true
            );
        private static final XMLStringValueReader titlereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "title",
            false
            );
        private static final XMLStringValueReader textreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "description",
            false
            );
        private static final XMLStringValueReader utypereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "utype",
            false
            );

        private static final VosiColumnReader columnreader = new VosiColumnReader();
        private static final VosiForeignKeyReader keys = new VosiForeignKeyReader();
        
        public void inport(final IvoaSchema schema, final IvoaTable.Builder tables, final XMLEventReader events)
        throws XMLParserException, XMLReaderException, DuplicateEntityException
            {
            this.start(
                events
                );

            String name  = namereader.read(events); 
            final String title = titlereader.read(events);
            final String text  = textreader.read(events); 
            final String utype = utypereader.read(events); 

            log.debug("Table [{}]", name);
            log.debug("    title [{}]", title);
            log.debug("    text  [{}]", text);
            log.debug("    utype [{}]", utype);

            String prefix =  schema.name() + "." ;
            if (name.startsWith(prefix))
                {
                name = name.substring(
                    prefix.length()
                    );
                }

            IvoaTable table = tables.select(
                name,
                new IvoaTable.Builder.Worker()
                    {
                    @Override
                    public IvoaTable create(String name)
                        throws DuplicateEntityException
                        {
                        final IvoaTable table = schema.tables().create(
                            name
                            );
                        update(table);
                        return table;
                        }

                    @Override
                    public void update(IvoaTable table)
                        {
                        table.text(text);
                        }
                    }
                );

            IvoaColumn.Builder columns = table.columns().builder();

            while (columnreader.match(events))
                {
                columnreader.inport(
                    table,
                    columns,
                    events
                    );
                }

            columns.finish();

            while (keys.match(events))
                {
                keys.inport(
                    events
                    );
                }

            this.done(
                events
                );
            }
        }

    public static class VosiColumnReader
    extends XMLReaderImpl
    implements XMLReader
        {
        public VosiColumnReader()
            {
            super(
                NAMESPACE_URI,
                "column"
                );
            }

        private static final XMLStringValueReader namereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "name",
            true
            );
        private static final XMLStringValueReader titlereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "title",
            false
            );
        private static final XMLStringValueReader textreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "description",
            false
            );
        private static final XMLStringValueReader unitreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "unit",
            false
            );
        private static final XMLStringValueReader ucdreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "ucd",
            false
            );
        private static final XMLStringValueReader utypereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "utype",
            false
            );

        private static final XMLStringValueReader dtypereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "dataType",
            false
            );
        private static final XMLStringValueReader flagreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "flag",
            false
            );
        
        public void inport(final IvoaTable table, final IvoaColumn.Builder columns, final XMLEventReader events)
        throws XMLParserException, XMLReaderException, DuplicateEntityException
            {
            StartElement start = this.start(
                events
                );

            Boolean std = new Boolean(
                attrib(
                    start,
                    "std"
                    )
                );
                
            final String name  = namereader.read(events); 
            final String title = titlereader.read(events);
            final String text  = textreader.read(events); 
            final String unit  = unitreader.read(events); 
            final String ucd   = ucdreader.read(events); 
            final String utype = utypereader.read(events); 
            final String dtype = dtypereader.read(events); 
            
            List<String> flags = new ArrayList<String>();
            while(flagreader.match(events))
                {
                flags.add(
                    flagreader.read(events)
                    );
                }

            log.debug("Column [{}]", name);
            log.debug("    std   [{}]", std);
            log.debug("    title [{}]", title);
            log.debug("    text  [{}]", text);
            log.debug("    unit  [{}]", unit);
            log.debug("    ucd   [{}]", ucd);
            log.debug("    utype [{}]", utype);
            log.debug("    dtype [{}]", dtype);
            for (String flag : flags)
                {
                log.debug("    flag  [{}]", flag);
                }

            columns.select(
                name,
                new IvoaColumn.Builder.Worker()
                    {
                    @Override
                    public IvoaColumn create(String name)
                        throws DuplicateEntityException
                        {
                        final IvoaColumn column = table.columns().create(
                            name
                            );
                        update(column);
                        return column;
                        }

                    @Override
                    public void update(final IvoaColumn column)
                        {
                        column.text(text);
                        column.meta().adql().ucd(ucd);
                        column.meta().adql().units(unit);
                        column.meta().adql().utype(utype);
                        column.meta().adql().dtype(dtype);
                        }
                    }
                );
            this.done(
                events
                );
            }
        }

    public static class VosiForeignKeyReader
    extends XMLReaderImpl
    implements XMLReader
        {
        public VosiForeignKeyReader()
            {
            super(
                NAMESPACE_URI,
                "foreignKey"
                );
            }

        private static final XMLStringValueReader targetreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "targetTable",
            true
            );

        private static final VosiForeignKeyPairReader columnreader = new VosiForeignKeyPairReader();

        private static final XMLStringValueReader textreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "description",
            false
            );
        private static final XMLStringValueReader utypereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "utype",
            false
            );
        
        public void inport(final XMLEventReader events)
        throws XMLParserException, XMLReaderException
            {
            log.debug("inport(XMLEventReader)");
            this.start(
                events
                );
                
            String target = targetreader.read(events); 
            
            do { 
                columnreader.inport(events);
                }
            while (columnreader.match(events));

            String text   = textreader.read(events); 
            String utype  = utypereader.read(events); 
            
            log.debug("ForeignKey");
            log.debug("    target [{}]", target);

            log.debug("    text  [{}]", text);
            log.debug("    utype [{}]", utype);
            
            this.done(
                events
                );
            }
        }

    public static class VosiForeignKeyPairReader
    extends XMLReaderImpl
    implements XMLReader
        {

        public VosiForeignKeyPairReader()
            {
            super(
                NAMESPACE_URI,
                "fkColumn"
                );
            }

        private static final XMLStringValueReader localreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "fromColumn",
            true
            );

        private static final XMLStringValueReader remotereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "targetColumn",
            true
            );

        public void inport(final XMLEventReader events)
        throws XMLParserException, XMLReaderException
            {
            log.debug("inport(XMLEventReader, IvoaResource)");
            this.start(
                events
                );

            String local  = localreader.read(events);
            String remote = remotereader.read(events);
            
            this.done(
                events
                );
            }
        }    
    
    /**
     * Helper method to check for an attribute.
     * This should probably be in th XMLReader class.
     * 
     * @param element The XML StartElement event.
     * @param name The XML attribute name.
     * @return The String value of the XML attribute, or null if XML attribute is not present.
     *
     */
    protected static String attrib(final StartElement element, final String name)
        {
        return attrib(
            element,
            new QName(name)
            );
        }

    /**
     * Helper method to check for an attribute.
     * This should probably be in th XMLReader class.
     * 
     * @param element The XML StartElement event.
     * @param name The XML attribute name.
     * @return The String value of the XML attribute, or null if XML attribute is not present.
     *
     */
    protected static String attrib(final StartElement element, final QName name)
        {
        Attribute attribute = element.getAttributeByName(
            name
            );
        if (attribute != null)
            {
            return attribute.getValue();
            }
        else {
            return null ;
            }
        }
    }
