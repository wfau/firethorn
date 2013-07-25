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
package uk.ac.roe.wfau.firethorn.util.xml;

import java.io.Reader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 */
@Slf4j
public abstract class XMLObjectReaderImpl<ObjectType>
extends XMLReaderImpl
implements XMLObjectReader<ObjectType>
    {

    /**
     * Public constructor.
     * 
     */
    public XMLObjectReaderImpl()
        {
        this(
            null
            );
        }

    /**
     * Public constructor.
     * 
     */
    public XMLObjectReaderImpl(final String namespace, final String name)
        {
        super(
            namespace,
            name
            );
        }

    /**
     * Public constructor.
     * 
     */
    public XMLObjectReaderImpl(final QName qname)
        {
        super(
            qname
            );
        }

    @Override
    public ObjectType read(final Reader reader)
    throws XMLReaderException, XMLParserException
        {
        return this.read(
            wrap(
                reader
                )
            );
        }
    }
