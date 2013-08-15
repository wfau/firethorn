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
public abstract class XMLReaderImpl
implements XMLReader
    {

    /**
     * Our XML XMLEventReader factory.
     *
     */
    private static final XMLInputFactory xmlreaderfactory = XMLInputFactory.newInstance();

    /**
     * Create an XMLEventReader for a Java IO Reader.
     * @todo Move this to new XMLReader
     *
     */
    public static XMLEventReader wrap(final Reader reader)
    throws XMLReaderException
        {
        try {
            return xmlreaderfactory.createXMLEventReader(reader);
            }
        catch (final XMLStreamException ouch)
            {
            log.error("Failed to open XMLEventReader [{}]", ouch.getMessage());
            throw new XMLReaderException(
                "Failed to open XMLEventReader",
                ouch
                );
            }
        }

    /**
     * Public constructor.
     *
     */
    public XMLReaderImpl()
        {
        this(
            null
            );
        }

    /**
     * Public constructor.
     *
     */
    public XMLReaderImpl(final String namespace, final String name)
        {
        log.debug("XMLObjectReaderImpl(QString, String)");
        log.debug("  Space [{}]", namespace);
        log.debug("  Name  [{}]", name);
        this.parser = new XMLParserImpl(
            namespace,
            name
            );
        }

    /**
     * Public constructor.
     *
     */
    public XMLReaderImpl(final QName qname)
        {
        log.debug("XMLObjectReaderImpl(QName)");
        log.debug("  QName [{}]", qname);
        this.parser = new XMLParserImpl(
            qname
            );
        }

    /**
     * Our XMLParser.
     *
     */
    protected XMLParser parser;

    /**
     * The XML element QName this reader handles.
     *
     */
    @Override
    public QName qname()
        {
        return parser.qname();
        }

    /**
     * Peek at the next XMLEvent from an XMLEventReader to see if it matches
     * this reader. This may skip processing instructions, comments and
     * whitespace to find the next element event.
     * @returns true if the next event matches.
     *
     */
    @Override
    public boolean match(final XMLEventReader reader)
    throws XMLParserException
        {
        return parser.match(
            reader
            );
        }

    /**
     * Check if an XMLEvent matches this reader.
     * @returns true if the event matches this reader.
     *
     */
    @Override
    public boolean match(final XMLEvent event)
    throws XMLParserException
        {
        log.debug("match(XMLEvent)");
        log.debug("  Event [{}]", event);
        return parser.match(
            event
            );
        }

    /**
     * Check if a StartElement matches this reader.
     * @returns true if the element matches this reader.
     *
     */
    public boolean match(final StartElement element)
    throws XMLParserException
        {
        log.debug("match(StartElement)");
        log.debug("  Element [{}]", element);
        return parser.match(
            element
            );
        }

    /**
     * Validate our start element.
     *
     * @param reader The XMLEventReader to read from.
     * @return Our start element.
     * @throws XMLReaderException If we didn't find what we expected.
     *
     */
    public StartElement start(final XMLEventReader reader)
    throws XMLParserException
        {
        log.debug("start(StartElement)");
        log.debug("  QName [{}]", this.qname());
        return parser.start(
            reader
            );
        }
    }
