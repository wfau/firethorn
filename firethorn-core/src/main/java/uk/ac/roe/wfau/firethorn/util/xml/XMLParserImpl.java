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
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 */
@Slf4j
public class XMLParserImpl
    implements XMLParser
    {

    /**
     * Public constructor.
     *
     */
    public XMLParserImpl()
        {
        this(null);
        }

    /**
     * Public constructor.
     *
     */
    public XMLParserImpl(final String namespace, final String name)
        {
        this(
            new QName(
                namespace,
                name
                )
            );
        }

    /**
     * Public constructor.
     *
     */
    public XMLParserImpl(final QName qname)
        {
        log.debug("XMLReaderImpl(QName)");
        log.debug("  QName [{}]", qname);
        this.qname = qname;
        }

    /**
     * The XML element QName this reader handles.
     *
     */
    private final QName qname;

    /**
     * The XML element QName this reader handles.
     *
     */
    @Override
    public QName qname()
        {
        return this.qname;
        }

    /**
     * Peek at the next event from an XMLEventReader to see if it matches this reader.
     * This may skip processing instructions, comments and whitespace to find the next element.
     *
     * @returns true if the next event matches.
     *
     */
    @Override
    public boolean match(final XMLEventReader reader)
    throws XMLParserException
        {
        log.debug("match(XMLEventReader)");
        log.debug("  QName [{}]", this.qname());
        return this.match(
            this.peek(
                reader
                )
            );
        }

    /**
     * Check if an XMLEvent matches this reader.
     *
     * @returns true if the event matches this reader.
     *
     */
    @Override
    public boolean match(final XMLEvent event)
    throws XMLParserException
        {
        log.debug("match(XMLEvent)");
        log.debug("  QName [{}]", this.qname());
        log.debug("  Event [{}]", event);
        if (null != event)
            {
            if (event.isStartElement())
                {
                return this.match(
                    event.asStartElement()
                    );
                }
            }
        return false;
        }

    /**
     * Check if a StartElement matches this reader.
     *
     * @returns true if the element matches this reader.
     *
     */
    @Override
    public boolean match(final StartElement event)
    throws XMLParserException
        {
        log.debug("match(StartElement)");
        log.debug("  QName [{}]", this.qname());
        log.debug("  Event [{}]", event);
        //
        // If we have a parser QName.
        if (null != this.qname())
            {
            log.debug("  Parser QName not null");
            //
            // If the element QName matches ours.
            if (this.qname().equals(event.getName()))
                {
                log.debug("  Element QName matches");
                return true;
                }
            //
            // If the element QName does not match ours.
            else
                {
                log.debug("  Element QName does not match");
                return false;
                }
            }
        //
        // If we don't have a parser QName.
        else
            {
            // if (log.isDebugEnabled())
            // log.debug("XMLReaderImpl.match() Parser  QName is null");
            // Assume we accept any QName.
            return true;
            }
        }

    /**
     * Check if the next event is an END_ELEMENT that matches our QName and remove it from the stream.
     * This method will skip processing instructions, comments and whitespace to locate the next END_ELEMENT.
     *
     * @throws XMLReaderException if the event is an not an END_ELEMENT event or does not match our QName.
     *
     */
    @Override
    public void done(final XMLEventReader reader)
    throws XMLParserException
        {
        log.debug("close(XMLEventReader)");
       log.debug("  QName [{}]", this.qname());
        //
        // Peek at the next element and check we are done.
        final XMLEvent event = this.peek(reader);
        if (this.done(event))
            {
            this.next(reader);
            }
        else
            {
            log.debug("End element expected [{}][{}]", this.qname(), event);
            throw new XMLParserException(
                "End element expected",
                this.qname(),
                event
                );
            }
        }

    /**
     * Check if an event is an END_ELEMENT event that matches our QName.
     *
     * @returns true if the event is a END_ELEMENT and it matches our QName, false if it is not an END_ELEMENT.
     * @throws XMLReaderException if the event is and END_ELEMENT event but it does not match our QName.
     *
     */
    @Override
    public boolean done(final XMLEvent event)
    throws XMLParserException
        {
        log.debug("done(XMLEvent)");
        log.debug("  QName [{}]", this.qname());
        log.debug("  Event [{}]", event);
        if (event.isEndElement())
            {
            return this.done(
                event.asEndElement()
                );
            }
        else
            {
            return false;
            }
        }

    /**
     * Check if an END_ELEMENT event matches our QName.
     *
     * @throws XMLReaderException if the END_ELEMENT Qname does not match.
     *
     */
    public boolean done(final EndElement event)
    throws XMLParserException
        {
        log.debug("done(EndElement)");
        log.debug("  QName [{}]", this.qname());
        log.debug("  Event [{}]", event);

        if (this.qname().equals(event.getName()))
            {
            return true;
            }
        else
            {
            log.debug("EndElement QName does not match [{}][{}]", this.qname(), event.getName());
            throw new XMLParserException(
                "EndElement QName does not match",
                this.qname(),
                event
                );
            }
        }

    /**
     * Read the next event from an XMLEventReader.
     *
     */
    @Override
    public XMLEvent next(final XMLEventReader reader)
    throws XMLParserException
        {
        log.debug("next(XMLEventReader)");
        log.debug("  QName [{}]", this.qname());
        try {
            return reader.nextEvent();
            }
        catch (final XMLStreamException ouch)
            {
            log.debug("XMLStreamException while reading XML reader [{}]", ouch.getMessage());
            throw new XMLParserException(
                "XMLStreamException while reading XML stream",
                ouch
                );
            }
        }

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
    @Override
    public XMLEvent peek(final XMLEventReader reader)
    throws XMLParserException
        {
        log.debug("peek(XMLEventReader)");
        log.debug("  QName [{}]", this.qname());
        try
            {
            //
            // Check if there is another event.
            if (reader.hasNext())
                {
                //
                // Peek at the next event.
                final XMLEvent event = reader.peek();
                log.debug("  Event [{}][{}]", typename(event), event);
                switch (event.getEventType())
                    {
                    //
                    // Start or end element are valid.
                    case XMLStreamConstants.START_ELEMENT:
                    case XMLStreamConstants.END_ELEMENT:
                        return event;
                        //
                        // End document is valid.
                    case XMLStreamConstants.END_DOCUMENT:
                        return event;
                        //
                        // Skip processing events.
                    case XMLStreamConstants.DTD:
                    case XMLStreamConstants.PROCESSING_INSTRUCTION:
                    case XMLStreamConstants.START_DOCUMENT:
                        //
                        // Move to the next event and process that.
                        reader.nextEvent();
                        return this.peek(reader);
                        //
                        // Skip comments.
                    case XMLStreamConstants.COMMENT:
                        //
                        // Move to the next event and process that.
                        reader.nextEvent();
                        return this.peek(reader);
                        //
                        // Skip whitespace.
                    case XMLStreamConstants.SPACE:
                        //
                        // Move to the next event and process that.
                        reader.nextEvent();
                        return this.peek(reader);
                        //
                        // Skip character events if they are whitespace.
                    case XMLStreamConstants.CHARACTERS:
                        if (event.asCharacters().isWhiteSpace())
                            {
                            //
                            // Move to the next event and process that.
                            reader.nextEvent();
                            return peek(reader);
                            }
                        else
                            {
                            return event;
                            }
                        //
                        // Return null for other event types.
                    case XMLStreamConstants.ATTRIBUTE:
                    case XMLStreamConstants.CDATA:
                    case XMLStreamConstants.ENTITY_DECLARATION:
                    case XMLStreamConstants.ENTITY_REFERENCE:
                    case XMLStreamConstants.NAMESPACE:
                    case XMLStreamConstants.NOTATION_DECLARATION:
                    default:
                        return null;
                    }
                }
            //
            // If the stream is empty.
            else
                {
                return null;
                }
            }
        catch (final XMLStreamException ouch)
            {
            log.debug("XMLStreamException while reading XML stream [{}]", ouch.getMessage());
            throw new XMLParserException(
                "XMLStreamException while reading XML stream",
                ouch
                );
            }
        }

    /**
     * Get the start of next XML element. In order to find the next XML element
     * this method will skip DTD, PROCESSING_INSTRUCTION, START_DOCUMENT COMMENT
     * and whitespace events.
     *
     * @return The start of the next XML element, or null if a END_ELEMENT event is encountered.
     * @throws XMLReaderException
     *             If an unexpected event is encountered, or the stream is empty.
     *
     */
    @Override
    public StartElement start(final XMLEventReader reader)
    throws XMLParserException
        {
        log.debug("start(XMLEventReader)");
        log.debug("  QName [{}]", this.qname());
        //
        // Find the next element event.
        XMLEvent event = this.peek(reader);
        //
        // If we found an element event.
        if (null != event)
            {
            //
            // Read the element from the reader.
            try {
                event = reader.nextEvent();
                }
            catch (final XMLStreamException ouch)
                {
                log.debug("XMLStreamException while reading XML stream [{}]", ouch.getMessage());
                throw new XMLParserException(
                    "XMLStreamException while reading XML stream",
                    ouch
                    );
                }
            log.debug("  Next [{}][{}]", typename(event), event);
            switch (event.getEventType())
                {
                //
                // Start element is what we are looking for.
                case XMLStreamConstants.START_ELEMENT:
                    return event.asStartElement();
                    //
                    // Throw exception for other event types.
                default:
                    log.debug("XML start element expected [{}][{}]", typename(event), event);
                    throw new XMLParserException(
                        "XML start element expected",
                        this.qname(),
                        event
                        );
                }
            }
        //
        // If we didn't find an element.
        else
            {
            log.debug("XML element expected [null]");
            throw new XMLParserException(
                "XML start element expected",
                this.qname(),
                event
                );
            }
        }

    /**
     * Decode an XML event type into a human readable name.
     *
     */
    public static String typename(final XMLEvent event)
        {
        return typename(event.getEventType());
        }

    /**
     * Decode an XML event type into a human readable name.
     *
     */
    public static String typename(final int type)
        {
        switch (type)
            {
            case XMLStreamConstants.ATTRIBUTE:
                return "ATTRIBUTE";
            case XMLStreamConstants.CDATA:
                return "CDATA";
            case XMLStreamConstants.CHARACTERS:
                return "CHARACTERS";
            case XMLStreamConstants.COMMENT:
                return "COMMENT";
            case XMLStreamConstants.DTD:
                return "DTD";
            case XMLStreamConstants.END_DOCUMENT:
                return "END_DOCUMENT";
            case XMLStreamConstants.END_ELEMENT:
                return "END_ELEMENT";
            case XMLStreamConstants.ENTITY_DECLARATION:
                return "ENTITY_DECLARATION";
            case XMLStreamConstants.ENTITY_REFERENCE:
                return "ENTITY_REFERENCE";
            case XMLStreamConstants.NAMESPACE:
                return "NAMESPACE";
            case XMLStreamConstants.NOTATION_DECLARATION:
                return "NOTATION_DECLARATION";
            case XMLStreamConstants.PROCESSING_INSTRUCTION:
                return "PROCESSING_INSTRUCTION";
            case XMLStreamConstants.SPACE:
                return "SPACE";
            case XMLStreamConstants.START_DOCUMENT:
                return "START_DOCUMENT";
            case XMLStreamConstants.START_ELEMENT:
                return "START_ELEMENT";
            default:
                return "unknown";
            }
        }
    }
