/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.xml;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import lombok.extern.slf4j.Slf4j;

import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn.UCD;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent.CopyDepth;
import uk.ac.roe.wfau.firethorn.util.xml.XMLStringValueReader;
import uk.ac.roe.wfau.firethorn.util.xml.XMLParserException;
import uk.ac.roe.wfau.firethorn.util.xml.XMLReader;
import uk.ac.roe.wfau.firethorn.util.xml.XMLReaderException;
import uk.ac.roe.wfau.firethorn.util.xml.XMLReaderImpl;

/**
 *
 *
 */
@Slf4j
public class MetaDocReader
extends XMLReaderImpl
implements XMLReader
    {
    
    public static final String NAMESPACE_OLD = "urn:astrogrid:schema:dsa:TableMetaDoc:v1.1";
    public static final String NAMESPACE_URI = "urn:astrogrid:schema:TableMetaDoc:v1.1";
    
    public MetaDocReader()
        {
        super(
            new QName(
                NAMESPACE_URI,
                "DatasetDescription"
                )
            );
        }

    private static SchemaReader schemareader = new SchemaReader();

    public Iterable<AdqlSchema> inport(final Reader source, final BaseSchema<?,?> base, final AdqlResource workspace)
    throws XMLParserException, XMLReaderException, NameNotFoundException
        {
        return inport(
            wrap(
                source
                ),
            base,
            workspace
            );
        }

    public Iterable<AdqlSchema> inport(final XMLEventReader source, final BaseSchema<?,?> base, final AdqlResource workspace)
        throws XMLParserException, XMLReaderException, NameNotFoundException
        {
        log.debug("inport(XMLEventReader, BaseSchema, AdqlResource)");

        parser.start(
            source
            );

        List<AdqlSchema> list = new ArrayList<AdqlSchema>();
        
        while (schemareader.match(source))
            {
            list.add(
                schemareader.inport(
                    source,
                    base,
                    workspace
                    )
                );
            }
        
        parser.done(
            source
            );

        return list;

        }

    public static class SchemaReader
    extends XMLReaderImpl
    implements XMLReader
        {
        public SchemaReader()
            {
            super(
                NAMESPACE_URI,
                "Catalog"
                );
            }

        private static XMLStringValueReader namereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "Name",
            true
            );
        private static XMLStringValueReader textreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "Description",
            false
            );

        private static TableReader tablereader = new TableReader();
        
        public AdqlSchema inport(final XMLEventReader source, final BaseSchema<?,?> base, final AdqlResource workspace)
        throws XMLParserException, XMLReaderException, NameNotFoundException
            {
            log.debug("inport(XMLEventReader, BaseSchema, AdqlResource)");
            parser.start(
                source
                );

            AdqlSchema schema = workspace.schemas().inport(
                namereader.read(
                    source
                    ),
                base
                );

            schema.text(
                textreader.read(
                    source
                    )
                );

            while (tablereader.match(source))
                {
                tablereader.inport(
                    source,
                    schema
                    );
                }

            parser.done(
                source
                );

            return schema;
            }
        }

    public static class TableReader
    extends XMLReaderImpl
    implements XMLReader
        {
        
        public TableReader()
            {
            super(
                NAMESPACE_URI,
                "Table"
                );
            }

        private static XMLStringValueReader namereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "Name",
            true
            );

        public void inport(final XMLEventReader source, final AdqlSchema schema)
        throws XMLParserException, XMLReaderException, NameNotFoundException
            {
            log.debug("inport(XMLEventReader, AdqlSchema)");
            parser.start(
                source
                );

            config(
                schema.tables().inport(
                    namereader.read(
                        source
                        )
                    ),
                source
                ); 

            parser.done(
                source
                );
            }

        public static class ConeSettingsReader
        extends XMLReaderImpl
        implements XMLReader
            {
            public ConeSettingsReader()
                {
                super(
                    NAMESPACE_URI,
                    "ConeSettings"
                    );
                }
            private static XMLStringValueReader racol = new XMLStringValueReader(
                NAMESPACE_URI,
                "RAColName",
                true
                );
            private static XMLStringValueReader decol = new XMLStringValueReader(
                NAMESPACE_URI,
                "DecColName",
                true
                );
    
            public void read(XMLEventReader reader)
                throws XMLParserException, XMLReaderException
                {
                if (this.match(reader))
                    {
                    parser.start(
                        reader
                        );
                    String ra = racol.read(
                        reader
                        );
                    String dec = decol.read(
                        reader
                        );
                    log.debug(" columns [{}][{}]", ra, dec);
                    parser.done(
                        reader
                        );
                    }
                }
            }

        private static ConeSettingsReader conesettings = new ConeSettingsReader();

        private static ColumnReader columnreader = new ColumnReader();

        private static XMLStringValueReader textreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "Description",
            false
            );

        public void config(final AdqlTable table, final XMLEventReader source)
        throws XMLParserException, XMLReaderException, NameNotFoundException
            {
            table.text(
                textreader.read(
                    source
                    )
                );
            conesettings.read(
                source
                );
            while (columnreader.match(source))
                {
                columnreader.inport(
                    source,
                    table
                    );
                }
            }
        }
    
    public static class ColumnReader
    extends XMLReaderImpl
    implements XMLReader
        {
        public ColumnReader()
            {
            super(
                NAMESPACE_URI,
                "Column"
                );
            }

        private static XMLStringValueReader namereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "Name",
            true
            );

        public void inport(final XMLEventReader source, final AdqlTable table)
        throws XMLParserException, XMLReaderException, NameNotFoundException 
            {
            log.debug("inport(XMLEventReader, AdqlTable)");
            parser.start(
                source
                );
            config(
                table.columns().inport(
                    namereader.read(
                        source
                        )
                    ),
                source
                );
            parser.done(
                source
                );
            }

        private static XMLStringValueReader typereader = new XMLStringValueReader(
            NAMESPACE_URI,
            "Datatype",
            false
            );
        private static XMLStringValueReader textreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "Description",
            false
            );
        private static XMLStringValueReader unitreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "Units",
            false
            );
        private static XMLStringValueReader errreader = new XMLStringValueReader(
            NAMESPACE_URI,
            "ErrorColumn",
            false
            );

        public static class UCDReader
        extends XMLStringValueReader 
            {
            public UCDReader()
                {
                super(
                    NAMESPACE_URI,
                    "UCD",
                    false
                    );
                }

            public void read(final AdqlColumn column, final XMLEventReader reader)
            throws XMLParserException, XMLReaderException
                {
                log.debug("read(AdqlColumn, XMLEventReader)");
                
                if (match(reader))
                    {
                    final StartElement element = start(
                        reader
                        );

                    UCD.Type type = UCD.Type.ONE;
                    final Attribute attrib = element.getAttributeByName(
                        new QName(
                            "version"
                            )
                        );
                    if (attrib != null)
                        {
                        if ("1+".equals(attrib.getValue()))
                            {
                            type = UCD.Type.ONEPLUS;
                            }
                        }
    
                    column.meta().adql().ucd(
                        type,
                        content(
                            reader
                            )
                        );
                    }
                }
            }

        private static UCDReader ucdreader = new UCDReader();
        
        public void config(final AdqlColumn column, final XMLEventReader source)
        throws XMLParserException, XMLReaderException
            {
            /*
             * Ignore the type from the metadoc.
            try {
                column.meta().adql().type(
                    AdqlColumn.Type.type(
                        typereader.read(
                            source
                            )
                        )
                    );
                }
            catch (Exception ouch)
                {
                log.warn(
                    "Unable to process column type",
                    ouch.getMessage()
                    );
                }
             */
            //
            // Skip the column type.
            typereader.read(
                source
                );
            //
            // Read the column description.
            column.text(
                textreader.read(
                    source
                    )
                );
            //
            // Read the column units.
            column.meta().adql().units(
                unitreader.read(
                    source
                    )
                );
            //
            // Read the column UCD.
            ucdreader.read(
                column,
                source
                );
            //
            // Skip the error column.
            errreader.read(
                source
                );
            }
        }
    }
