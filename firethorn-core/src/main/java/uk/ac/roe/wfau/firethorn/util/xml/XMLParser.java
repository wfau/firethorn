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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 *
 */
public interface XMLParser
    {
    /**
     * The XML element QName this reader handles.
     *
     */
    public QName qname();

    /**
     * Peek at the next event from an {@link XMLEventReader} to see if it matches this parser.
     * This may skip processing instructions, comments and whitespace to find the next element.
     * @returns true if the next {@link XMLEvent} matches.
     *
     */
    public boolean match(final XMLEventReader reader)
    throws XMLParserException;

    /**
     * Check if an {@link XMLEvent} matches this reader.
     * @param event The {@link XMLEvent} to check.
     * @returns true if the {@link XMLEvent} matches.
     *
     */
    public boolean match(final XMLEvent event)
    throws XMLParserException;

    /**
     * Check if the next event is a {@link XMLStreamConstants#END_ELEMENT} that matches our {@link QName} and remove it from the stream.
     * This method will skip processing instructions, comments and whitespace to locate the next END_ELEMENT.
     * @throws {@link XMLReaderException} if the event does not match.
     *
     */
    public void done(final XMLEventReader reader)
    throws XMLParserException;

    /**
     * Check if an event is a {@link XMLStreamConstants#END_ELEMENT} that matches our {@link QName}.
     * @returns true if the event matches.
     * @throws {@link XMLReaderException} if the event does not match.
     *
     */
    public boolean done(final XMLEvent event)
    throws XMLParserException;

    /**
     * Read the next event from an XMLEventReader.
     *
     */
    public XMLEvent next(final XMLEventReader reader)
    throws XMLParserException;

    /**
     * Skip processing instructions, comments and whitespace to peek at the next element.
     * This will skip {@link XMLStreamConstants#DTD}, {@link XMLStreamConstants#PROCESSING_INSTRUCTION},
     * {@link XMLStreamConstants#START_DOCUMENT}, {@link XMLStreamConstants#COMMENT} and
     * {@link XMLStreamConstants#SPACE whitespace} events.
     *
     * @return The next element {@link XMLEvent} in the stream, or null if the next {@link XMLEvent} is not an element.
     *
     */
    public XMLEvent peek(final XMLEventReader reader)
    throws XMLParserException;

    /**
     * Check if a {@link StartElement} matches this reader.
     * @param event The {@link StartElement} to check.
     * @returns true if the {@link StartElement} matches.
     *
     */
    public boolean match(final StartElement element)
    throws XMLParserException;

    /**
     * Get the next {@link StartElement}, skipping {@link XMLStreamConstants#DTD},
     * {@link XMLStreamConstants#PROCESSING_INSTRUCTION}, {@link XMLStreamConstants#START_DOCUMENT},
     * {@link XMLStreamConstants#COMMENT} and {@link XMLStreamConstants#SPACE whitespace} events.
     *
     * @return The next {@link StartElement}, or null if an {@link XMLStreamConstants#END_ELEMENT} is encountered.
     * @throws XMLReaderException If an unexpected {@link XMLEvent} is encountered, or the stream is empty.
     *
     */
    public StartElement start(final XMLEventReader reader)
    throws XMLParserException;

    }
