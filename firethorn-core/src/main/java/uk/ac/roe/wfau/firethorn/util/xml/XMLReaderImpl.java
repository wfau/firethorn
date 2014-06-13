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
     * Our {@link XMLEventReader} factory.
     *
     */
    private static final XMLInputFactory xmlreaderfactory = XMLInputFactory.newInstance();

    /**
     * Create an {@link XMLEventReader} for a Java IO Reader.
     *
     */
    public static XMLEventReader xmlreader(final Reader reader)
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

    @Override
    public QName qname()
        {
        return parser.qname();
        }

    @Override
    public boolean match(final XMLEventReader reader)
    throws XMLParserException
        {
        return parser.match(
            reader
            );
        }

    @Override
    public boolean match(final XMLEvent event)
    throws XMLParserException
        {
        return parser.match(
            event
            );
        }

    /**
     * Check if a {@link StartElement} matches this reader.
     * @returns true if the {@link StartElement} matches.
     *
     */
    protected boolean match(final StartElement element)
    throws XMLParserException
        {
        log.debug("match(StartElement)");
        log.debug("  Element [{}]", element);
        return parser.match(
            element
            );
        }

    /**
     * Read a {@link StartElement} from a {@link XMLEventReader}
     * @param reader The {@link XMLEventReader} to read from.
     * @return A matching {@link StartElement}.
     * @throws XMLReaderException If the next {@link XMLEvent} from the {@link XMLEventReader} wasn't a matching {@link StartElement}..
     *
     */
    protected StartElement start(final XMLEventReader reader)
    throws XMLParserException
        {
        log.debug("start(XMLEventReader)");
        log.debug("  QName [{}]", this.qname());
        return parser.start(
            reader
            );
        }

    /**
     * Read an {@link EndElement} from a {@link XMLEventReader}
     * @param reader The {@link XMLEventReader} to read from.
     * @throws XMLReaderException If the next {@link XMLEvent} from the {@link XMLEventReader} wasn't a matching {@link EndElement}..
     *
     */
    protected void done(final XMLEventReader reader)
    throws XMLParserException
        {
        log.debug("done(XMLEventReader)");
        log.debug("  QName [{}]", this.qname());
        parser.done(
            reader
            );
        }
    }
