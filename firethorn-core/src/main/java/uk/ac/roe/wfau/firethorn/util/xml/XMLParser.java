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
     * Peek at the next event from an XMLEventReader to see if it matches this
     * reader. This may skip processing instructions, comments and whitespace to
     * find the next element event.
     *
     * @returns true if the next event matches.
     *
     */
    public boolean match(final XMLEventReader reader)
    throws XMLParserException;

    /**
     * Check if an event matches this reader.
     *
     * @returns true if the event matches this reader.
     *
     */
    public boolean match(final XMLEvent event)
    throws XMLParserException;

    /**
     * Check if the next event is an END_ELEMENT that matches our QName and
     * remove it from the stream. This method will skip processing instructions,
     * comments and whitespace to locate the next element event.
     *
     * @throws XMLParserException
     *             if the event is an not an END_ELEMENT event or does not match
     *             our QName.
     * @todo Refactor this as done(true)
     *
     */
    public void done(final XMLEventReader reader)
    throws XMLParserException;

    /**
     * Check if an event is an END_ELEMENT event that matches our QName.
     *
     * @returns true of the event is a END_ELEMENT and it matches our QName,
     *          false if it is not an END_ELEMENT.
     * @throws XMLParserException
     *             if the event is and END_ELEMENT event but it does not match
     *             our QName.
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
     * Skip processing instructions, comments and whitespace to peek at the next
     * element event. This method will step the XMLEventReader forwards,
     * skipping DTD, PROCESSING_INSTRUCTION, START_DOCUMENT, COMMENT and
     * whitespace events.
     *
     * @return The next element event in the stream or null if the next event is
     *         not an element.
     *
     */
    public XMLEvent peek(final XMLEventReader reader)
    throws XMLParserException;

    /**
     * Check if a StartElement matches this reader.
     *
     * @returns true if the element matches this reader.
     *
     */
    public boolean match(final StartElement element)
    throws XMLParserException;

    /**
     * Get the next XML start element. In order to find the next XML element
     * this method will skip DTD, PROCESSING_INSTRUCTION, START_DOCUMENT COMMENT
     * and whitespace events.
     *
     * @return The start of the next XML element, or null if a END_ELEMENT event
     *         is encountered.
     * @throws XMLParserException
     *             If an unexpected event is encountered, or the stream is
     *             empty.
     *
     */
    public StartElement start(final XMLEventReader reader)
    throws XMLParserException;

    }
