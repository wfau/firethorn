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
package uk.ac.roe.wfau.firethorn.meta.ivoa.vosi.tables;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaColumn;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
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
public class VosiTablesetReader
    extends XMLReaderImpl
    implements XMLReader
    {
    public static final String NAMESPACE_URI = "http://www.ivoa.net/xml/VODataService/v1.1";

    public VosiTablesetReader()
        {
        super(
            new QName(
                NAMESPACE_URI,
                "tableset"
                )
            );
        }

    private static final VosiSchemaReader schemareader = new VosiSchemaReader();  
    
    public void inport(final Reader reader, final IvoaResource resource)
    throws XMLParserException, XMLReaderException, NameNotFoundException
        {
        log.debug("inport(Reader, IvoaResource)");
        this.inport(
            wrap(
                reader
                ),
            resource
            );
        }

    public void inport(final XMLEventReader events, final IvoaResource resource)
    throws XMLParserException, XMLReaderException
        {
        log.debug("inport(XMLEventReader, IvoaResource)");
        this.start(
            events
            );
        while (schemareader.match(events))
            {
            schemareader.inport(
                events,
                resource
                );
            }
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

        public void inport(final XMLEventReader events, final IvoaResource resource)
        throws XMLParserException, XMLReaderException
            {
            log.debug("inport(XMLEventReader, IvoaResource)");
            this.start(
                events
                );

            String name  = namereader.read(events); 
            String title = titlereader.read(events);
            String text  = textreader.read(events); 
            String utype = utypereader.read(events); 

            log.debug("Schema [{}]", name);
            log.debug("    title [{}]", title);
            log.debug("    text  [{}]", text);
            log.debug("    utype [{}]", utype);

            /*
             * 
            IvoaSchema schema = resource.schemas().search(
                name
                );
             * 
             */
            IvoaSchema schema = null;
            
            while (tablereader.match(events))
                {
                tablereader.inport(
                    events,
                    schema
                    );
                }

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

        private static final VosiColumnReader columns = new VosiColumnReader();
        private static final VosiForeignKeyReader keys = new VosiForeignKeyReader();
        
        public void inport(final XMLEventReader events, final IvoaSchema schema)
        throws XMLParserException, XMLReaderException
            {
            log.debug("inport(XMLEventReader, IvoaResource)");
            this.start(
                events
                );

            String name  = namereader.read(events); 
            String title = titlereader.read(events);
            String text  = textreader.read(events); 
            String utype = utypereader.read(events); 

            log.debug("Table [{}]", name);
            log.debug("    title [{}]", title);
            log.debug("    text  [{}]", text);
            log.debug("    utype [{}]", utype);

            /*
             * 
            IvoaTable table = schema.tables().search(
                name
                );
             * 
             */
            IvoaTable table = null ;

            while (columns.match(events))
                {
                columns.inport(
                    events,
                    table
                    );
                }

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
        
        public void inport(final XMLEventReader events, final IvoaTable table)
        throws XMLParserException, XMLReaderException
            {
            log.debug("inport(XMLEventReader, IvoaResource)");
            StartElement start = this.start(
                events
                );

            Boolean std = new Boolean(
                attrib(
                    start,
                    "std"
                    )
                );
                
            String name  = namereader.read(events); 
            String title = titlereader.read(events);
            String text  = textreader.read(events); 
            String unit  = unitreader.read(events); 
            String ucd   = ucdreader.read(events); 
            String utype = utypereader.read(events); 
            String dtype = dtypereader.read(events); 
            
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

            /*
             * 
            IvoaColumn column = table.columns().search(name);
             * 
             */
            IvoaColumn column = null;
            
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
