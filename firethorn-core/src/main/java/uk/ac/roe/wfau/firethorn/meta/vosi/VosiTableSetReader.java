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
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn.Type;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable.TableStatus;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaColumn;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaSchema;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;
import uk.ac.roe.wfau.firethorn.util.xml.XMLParserException;
import uk.ac.roe.wfau.firethorn.util.xml.XMLReader;
import uk.ac.roe.wfau.firethorn.util.xml.XMLReaderException;
import uk.ac.roe.wfau.firethorn.util.xml.XMLReaderImpl;
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

    /**
     * The VODataService namespace URI, [{@value}].
     * 
     */
    protected static final String NAMESPACE_URI = "http://www.ivoa.net/xml/VODataService/v1.1";

    /**
     * Read data from a {@link Reader} to update an {@link IvoaResource}. 
     *
     */
    public void inport(final Reader reader, final IvoaResource resource)
    throws XMLParserException, XMLReaderException, NameNotFoundException, DuplicateEntityException
        {
        this.inport(
            wrap(
                reader
                ),
            resource
            );
        }

    /**
     * Read data from an {@link XMLEventReader} and update an {@link IvoaResource}. 
     *
     */
    public void inport(final XMLEventReader events, final IvoaResource resource)
    throws XMLParserException, XMLReaderException, DuplicateEntityException
        {
        if (left.match(events))
            {
            left.inport(
                events,
                resource
                );
            }
        if (right.match(events))
            {
            right.inport(
                events,
                resource
                );
            }
        else {
            throw new XMLParserException(
                "Expected [" + TableSetReader.ELEMENT_NAME + "] element"
                );
            }
        }

    /**
     * A {@link TableSetReader} with no namespace URI.
     * 
     */
    private TableSetReader left  = new TableSetReader(
        null
        );

    /**
     * A {@link TableSetReader} with the default namespace.
     * 
     */
    private TableSetReader right = new TableSetReader(
        NAMESPACE_URI
        );

    /**
     * Remove any prefixes from a name.
     *
     */
    private String clean(final String name)
        {
        if (name.contains("."))
            {
            return name.substring(
                name.lastIndexOf('.')
                );
            }
        else {
            return name ;
            }
        }

    /**
     * An {@link XMLReader} for [{@value TableSetReader#ELEMENT_NAME}] elements.
     *
     */
    public class TableSetReader
        extends XMLReaderImpl
        implements XMLReader
        {
        /**
         * The standard element name, [{@value}].
         *
         */
        protected static final String ELEMENT_NAME = "tableset" ;

        /**
         * Public constructor, using standard element name and namespace.
         *
         */
        public TableSetReader()
            {
            this(
                NAMESPACE_URI,
                ELEMENT_NAME
                );
            }
        
        /**
         * Public constructor, using standard element name and a specific namespace.
         * @param space The XML namespace.
         *
         */
        public TableSetReader(final String namespace)
            {
            this(
                namespace,
                ELEMENT_NAME
                );
            }
        
        /**
         * Public constructor, using specific element name and namespace.
         * @param space The XML namespace.
         * @param nam   The XML element name.
         *
         */
        public TableSetReader(final String namespace, final String name)
            {
            super(
                new QName(
                    namespace,
                    name
                    )
                );
            this.schemareader = new SchemaReader(
                namespace
                );
            }

        /**
         * Our inner {@link SchemaReader}.
         * 
         */
        private SchemaReader schemareader ;  

        /**
         * Read data from an {@link XMLEventReader} and update an {@link IvoaResource}. 
         *
         */
        public void inport(final XMLEventReader events, final IvoaResource resource)
        throws XMLParserException, XMLReaderException, DuplicateEntityException
            {
            this.start(
                events
                );
    
            IvoaSchema.Builder schemas = resource.schemas().builder(); 
    
            while (schemareader.match(events))
                {
                schemareader.inport(
                    events,
                    schemas
                    );
                }
            
            schemas.finish();
    
            this.done(
                events
                );
            }
        }

    /**
     * An {@link XMLReader} for [{@value SchemaReader#ELEMENT_NAME}] elements.
     *
     *
     */
    public class SchemaReader
    extends XMLReaderImpl
    implements XMLReader
        {
        /**
         * The standard element name, [{@value}].
         *
         */
        protected static final String ELEMENT_NAME = "schema" ;
        
        /**
         * Public constructor, using standard element name and namespace.
         *
         */
        public SchemaReader()
            {
            this(
                NAMESPACE_URI,
                ELEMENT_NAME
                );
            }

        /**
         * Public constructor, using standard element name and a specific namespace.
         * @param space The XML namespace.
         *
         */
        public SchemaReader(final String namespace)
            {
            this(
                namespace,
                ELEMENT_NAME
                );
            }
        
        /**
         * Public constructor, using specific element name and namespace.
         * @param space The XML namespace.
         * @param nam   The XML element name.
         *
         */
        public SchemaReader(final String namespace, final String name)
            {
            super(
                new QName(
                    namespace,
                    name
                    )
                );
            namereader = new XMLStringValueReader(
                namespace,
                "name",
                true
                );
            titlereader = new XMLStringValueReader(
                namespace,
                "title",
                false
                );
            textreader = new XMLStringValueReader(
                namespace,
                "description",
                false
                );
            utypereader = new XMLStringValueReader(
                namespace,
                "utype",
                false
                );
            tablereader = new TableReader(
                namespace
                );
            }

        /**
         * An {@link XMLStringValueReader} to read the schema name.
         * 
         */
        private final XMLStringValueReader namereader ;

        /**
         * An {@link XMLStringValueReader} to read the schema title.
         * 
         */
        private final XMLStringValueReader titlereader ;

        /**
         * An {@link XMLStringValueReader} to read the schema description.
         * 
         */
        private final XMLStringValueReader textreader ;

        /**
         * An {@link XMLStringValueReader} to read the schema uType.
         * 
         */
        private final XMLStringValueReader utypereader ;

        /**
         * An {@link TableReader} to read the schema tables.
         * 
         */
        private final TableReader tablereader ;

        /**
         * Read data from an {@link XMLEventReader} and update an {@link IvoaSchema.Builder}. 
         *
         */
        public void inport(final XMLEventReader events, final IvoaSchema.Builder schemas)
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

            IvoaSchema schema = schemas.build(
                new IvoaSchema.Metadata()
                    {
                    @Override
                    public Ivoa ivoa()
                        {
                        return new Ivoa()
                            {
                            @Override
                            public String name()
                                {
                                return name ;
                                }

                            @Override
                            public String title()
                                {
                                return title;
                                }

                            @Override
                            public String text()
                                {
                                return text;
                                }

                            @Override
                            public String utype()
                                {
                                return utype;
                                }
                            };
                        }
                    @Override
                    public Adql adql()
                        {
                        return null ;
                        }
                    }
                );

            IvoaTable.Builder tables = schema.tables().builder();            
            while (tablereader.match(events))
                {
                tablereader.inport(
                    events,
                    tables
                    );
                }

            schemas.finish();

            this.done(
                events
                );
            }
        }
    
    /**
     * An {@link XMLReader} for [{@value TableReader#ELEMENT_NAME}] elements.
     *
     *
     */
    public class TableReader
    extends XMLReaderImpl
    implements XMLReader
        {
        /**
         * The standard element name, [{@value}].
         *
         */
        protected static final String ELEMENT_NAME = "table" ;

        /**
         * Public constructor, using standard element name and namespace.
         *
         */
        public TableReader()
            {
            this(
                NAMESPACE_URI,
                ELEMENT_NAME
                );
            }

        /**
         * Public constructor, using standard element name and a specific namespace.
         * @param space The XML namespace.
         *
         */
        public TableReader(final String namespace)
            {
            this(
                namespace,
                ELEMENT_NAME
                );
            }

        /**
         * Public constructor, using specific element name and namespace.
         * @param space The XML namespace.
         * @param nam   The XML element name.
         *
         */
        public TableReader(final String namespace, String name)
            {
            super(
                new QName(
                    namespace,
                    name
                    )
                );
            namereader = new XMLStringValueReader(
                namespace,
                "name",
                true
                );
            titlereader = new XMLStringValueReader(
                namespace,
                "title",
                false
                );
            textreader = new XMLStringValueReader(
                namespace,
                "description",
                false
                );
            utypereader = new XMLStringValueReader(
                namespace,
                "utype",
                false
                );
            columnreader = new ColumnReader(
                namespace
                );
            keyreader = new ForeignKeyReader(
                namespace
                );
            }
        

        /**
         * An {@link XMLStringValueReader} to read the table name.
         * 
         */
        private final XMLStringValueReader namereader ;

        /**
         * An {@link XMLStringValueReader} to read the table title.
         * 
         */
        private final XMLStringValueReader titlereader ;

        /**
         * An {@link XMLStringValueReader} to read the table description.
         * 
         */
        private final XMLStringValueReader textreader ;

        /**
         * An {@link XMLStringValueReader} to read the table uType.
         * 
         */
        private final XMLStringValueReader utypereader ;

        /**
         * An {@link XMLReader} to read the table columns.
         * 
         */
        private final ColumnReader columnreader ;

        /**
         * An {@link XMLReader} to read the table keys.
         * 
         */
        private final ForeignKeyReader keyreader;

        /**
         * Read data from an {@link XMLEventReader} and update an {@link IvoaTable.Builder}. 
         *
         */
        public void inport(final XMLEventReader events, final IvoaTable.Builder tables)
        throws XMLParserException, XMLReaderException, DuplicateEntityException
            {
            this.start(
                events
                );

            final String name  = clean(namereader.read(events)); 
            final String title = titlereader.read(events);
            final String text  = textreader.read(events); 
            final String utype = utypereader.read(events); 

            log.debug("Table [{}]", name);
            log.debug("    title [{}]", title);
            log.debug("    text  [{}]", text);
            log.debug("    utype [{}]", utype);

            IvoaTable table = tables.build(
                new IvoaTable.Metadata()
                    {
                    @Override
                    public Adql adql()
                        {
                        return null ;
                        }
                    @Override
                    public Ivoa ivoa()
                        {
                        return new Ivoa()
                            {
                            @Override
                            public String name()
                                {
                                return name ;
                                }
                            @Override
                            public String title()
                                {
                                return title;
                                }
                            @Override
                            public String text()
                                {
                                return text;
                                }
                            @Override
                            public String utype()
                                {
                                return utype;
                                }
                            };
                        }
                    }
                );

            IvoaColumn.Builder columns = table.columns().builder();

            while (columnreader.match(events))
                {
                columnreader.inport(
                    events,
                    columns
                    );
                }

            columns.finish();

            while (keyreader.match(events))
                {
                keyreader.inport(
                    events
                    );
                }

            this.done(
                events
                );
            }
        }
    
    /**
     * An {@link XMLReader} for [{@value ColumnReader#ELEMENT_NAME}] elements.
     *
     *
     */
    public class ColumnReader
    extends XMLReaderImpl
    implements XMLReader
        {
        /**
         * The standard element name, [{@value}].
         *
         */
        protected static final String ELEMENT_NAME = "column" ;

        /**
         * Public constructor, using standard element name and namespace.
         *
         */
        public ColumnReader()
            {
            this(
                NAMESPACE_URI,
                ELEMENT_NAME
                );
            }

        /**
         * Public constructor, using standard element name and a specific namespace.
         * @param space The XML namespace.
         *
         */
        public ColumnReader(final String namespace)
            {
            this(
                namespace,
                ELEMENT_NAME
                );
            }

        /**
         * Public constructor, using specific element name and namespace.
         * @param space The XML namespace.
         * @param nam   The XML element name.
         *
         */
        public ColumnReader(final String namespace, final String name)
            {
            super(
                namespace,
                "column"
                );

            namereader = new XMLStringValueReader(
                namespace,
                "name",
                true
                );
            titlereader = new XMLStringValueReader(
                namespace,
                "title",
                false
                );
            textreader = new XMLStringValueReader(
                namespace,
                "description",
                false
                );
            unitreader = new XMLStringValueReader(
                namespace,
                "unit",
                false
                );
            ucdreader = new XMLStringValueReader(
                namespace,
                "ucd",
                false
                );
            utypereader = new XMLStringValueReader(
                namespace,
                "utype",
                false
                );
            dtypereader = new XMLStringValueReader(
                namespace,
                "dataType",
                false
                );
            flagreader = new XMLStringValueReader(
                namespace,
                "flag",
                false
                );
            }

        /**
         * An {@link XMLStringValueReader} to read the column name.
         * 
         */
        private final XMLStringValueReader namereader ;

        /**
         * An {@link XMLStringValueReader} to read the column title.
         * 
         */
        private final XMLStringValueReader titlereader ;

        /**
         * An {@link XMLStringValueReader} to read the column description.
         * 
         */
        private final XMLStringValueReader textreader ;

        /**
         * An {@link XMLStringValueReader} to read the column units.
         * 
         */
        private final XMLStringValueReader unitreader ;

        /**
         * An {@link XMLStringValueReader} to read the column UCD.
         * 
         */
        private final XMLStringValueReader ucdreader ;

        /**
         * An {@link XMLStringValueReader} to read the column uType.
         * 
         */
        private final XMLStringValueReader utypereader ;

        /**
         * An {@link XMLStringValueReader} to read the column dType.
         * 
         */
        private final XMLStringValueReader dtypereader ;

        /**
         * An {@link XMLStringValueReader} to read the column flags.
         * 
         */
        private final XMLStringValueReader flagreader ;
        
        /**
         * Read data from an {@link XMLEventReader} and update an {@link IvoaColumn.Builder}. 
         *
         */
        public void inport(final XMLEventReader events, final IvoaColumn.Builder columns)
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
                
            final String name  = clean(namereader.read(events)); 
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

            columns.build(
                new IvoaColumn.Metadata()
                    {
                    @Override
                    public Adql adql()
                        {
                        return null;
                        }
                    @Override
                    public Ivoa ivoa()
                        {
                        return new Ivoa()
                            {
                            @Override
                            public String name()
                                {
                                return name;
                                }

                            @Override
                            public String title()
                                {
                                return title;
                                }

                            @Override
                            public String text()
                                {
                                return text;
                                }

                            @Override
                            public String utype()
                                {
                                return utype;
                                }

                            @Override
                            public String dtype()
                                {
                                return dtype;
                                }

                            @Override
                            public String unit()
                                {
                                return unit;
                                }

                            @Override
                            public Integer arraysize()
                                {
                                return null;
                                }

                            @Override
                            public String ucd()
                                {
                                return ucd;
                                }
                            };
                        }
                    }
                );

            this.done(
                events
                );
            }
        }

    /**
     * An {@link XMLReader} for [{@value ForeignKeyReader#ELEMENT_NAME}] elements.
     *
     *
     */
    public static class ForeignKeyReader
    extends XMLReaderImpl
    implements XMLReader
        {
        /**
         * The standard element name, [{@value}].
         *
         */
        protected static final String ELEMENT_NAME = "foreignKey" ;

        /**
         * Public constructor, using standard element name and namespace.
         *
         */
        public ForeignKeyReader()
            {
            this(
                NAMESPACE_URI,
                ELEMENT_NAME
                );
            }

        /**
         * Public constructor, using standard element name and a specific namespace.
         * @param space The XML namespace.
         *
         */
        public ForeignKeyReader(final String namespace)
            {
            this(
                namespace,
                ELEMENT_NAME
                );
            }

        /**
         * Public constructor, using specific element name and namespace.
         * @param space The XML namespace.
         * @param nam   The XML element name.
         *
         */
        public ForeignKeyReader(final String namespace, final String name)
            {
            super(
                new QName(
                    namespace,
                    name
                    )
                );
            targetreader = new XMLStringValueReader(
                NAMESPACE_URI,
                "targetTable",
                true
                );
            textreader = new XMLStringValueReader(
                NAMESPACE_URI,
                "description",
                false
                );
            utypereader = new XMLStringValueReader(
                NAMESPACE_URI,
                "utype",
                false
                );
            columnreader = new ForeignPairReader();
            }

        /**
         * An {@link XMLStringValueReader} to read the key target.
         * 
         */
        private final XMLStringValueReader targetreader ;

        /**
         * An {@link XMLStringValueReader} to read the key description.
         * 
         */
        private final XMLStringValueReader textreader ;

        /**
         * An {@link XMLStringValueReader} to read the key uType.
         * 
         */
        private final XMLStringValueReader utypereader ;

        /**
         * An {@link XMLStringValueReader} to read the foreign columns.
         * 
         */
        private final ForeignPairReader columnreader ;

        /**
         * Read data from an {@link XMLEventReader} and .... 
         *
         */
        public void inport(final XMLEventReader events)
        throws XMLParserException, XMLReaderException
            {
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

    /**
     * An {@link XMLReader} for [{@value ForeignPairReader#ELEMENT_NAME}] elements.
     *
     *
     */
    public static class ForeignPairReader
    extends XMLReaderImpl
    implements XMLReader
        {
        /**
         * The standard element name, [{@value}].
         *
         */
        protected static final String ELEMENT_NAME = "fkColumn" ;


        /**
         * Public constructor, using standard element name and namespace.
         *
         */
        public ForeignPairReader()
            {
            this(
                NAMESPACE_URI,
                ELEMENT_NAME
                );
            }

        /**
         * Public constructor, using standard element name and a specific namespace.
         * @param space The XML namespace.
         *
         */
        public ForeignPairReader(final String namespace)
            {
            this(
                namespace,
                ELEMENT_NAME
                );
            }

        /**
         * Public constructor, using specific element name and namespace.
         * @param space The XML namespace.
         * @param nam   The XML element name.
         *
         */
        public ForeignPairReader(final String namespace, final String name)
            {
            super(
                new QName(
                    namespace,
                    name
                    )
                );
            localreader = new XMLStringValueReader(
                namespace,
                "fromColumn",
                true
                );

            remotereader = new XMLStringValueReader(
                namespace,
                "targetColumn",
                true
                );
            }

        /**
         * An {@link XMLStringValueReader} to read the local column name.
         * 
         */
        private final XMLStringValueReader localreader ;

        /**
         * An {@link XMLStringValueReader} to read the remote column name.
         * 
         */
        private final XMLStringValueReader remotereader ;

        /**
         * Read data from an {@link XMLEventReader} and .... 
         *
         */
        public void inport(final XMLEventReader events)
        throws XMLParserException, XMLReaderException
            {
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
            new QName(
                name
                )
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
