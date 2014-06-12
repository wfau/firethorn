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

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 */
@Slf4j
public class XMLStringValueReader
extends XMLObjectReaderImpl<String>
implements XMLObjectReader<String>
    {

    /**
     * Public constructor.
     * @param qname The XML element {@link QName}.
     *
     */
    public XMLStringValueReader(final QName qname)
        {
        this(
            qname,
            true
            );
        }

    /**
     * Public constructor.
     * @param qname The XML element {@link QName}.
     * @param required True if this element is required.
     *
     */
    public XMLStringValueReader(final QName qname, final boolean required)
        {
        super(
            qname
            );
        this.required = required ;
        }

    /**
     * Public constructor.
     * @param name The XML element name.
     * @param namespave The XML element namespace URI.
     *
     */
    public XMLStringValueReader(final String namespace, final String name)
        {
        this(
            namespace,
            name,
            true
            );
        }

    /**
     * Public constructor.
     * @param name The XML element name.
     * @param namespave The XML element namespace URI.
     * @param required True if this element is required.
     *
     */
    public XMLStringValueReader(final String namespace, final String name, final boolean required)
        {
        super(
            namespace,
            name
            );
        this.required = required ;
        }

    private boolean trim = true ;
    private boolean required ;

    @Override
    public String read(final XMLEventReader reader)
    throws XMLParserException, XMLReaderException
        {
        log.debug("read(XMLEventReader)");
        log.debug("  QName [{}]", this.qname());
        //
        // If we we match the next element.
        if ((required) || (parser.match(reader)))
            {
            start(
                reader
                );
            return content(
                reader
                );
            }
        //
        // Otherwise, just return null.
        else {
            return null ;
            }
        }

    /**
     * Process our element content.
     *
     */
    protected String content(final XMLEventReader reader)
    throws XMLParserException, XMLReaderException
        {
        log.debug("content(XMLEventReader)");
        log.debug("  QName [{}]", this.qname());
        //
        // Start with a null String.
        String result = null ;
        //
        // If there are more events.
        if (reader.hasNext())
            {
            //
            // Check the next element type.
            try {
                switch (reader.peek().getEventType())
                    {
                    //
                    // Raw text or CDATA elements.
                    case XMLStreamConstants.CDATA:
                    case XMLStreamConstants.CHARACTERS:
                        result = reader.getElementText();
                        break ;
                    //
                    // (optional) end element is expected.
                    case XMLStreamConstants.END_ELEMENT:
                        reader.next();
                        break ;
                    //
                    // Anything else is unexpected.
                    default:
                        log.debug("Unexpected element type : expected CDATA or CHARACTERS");
                        throw new XMLReaderException(
                            "Expected CDATA or CHARACTERS"
                            );
                    }
                }
            catch (final XMLStreamException ouch)
                {
                log.debug("XMLStreamException while reading string [{}]", ouch.getMessage());
                throw new XMLReaderException(
                    "XMLStreamException while reading string",
                    ouch
                    );
                }
            }
        if ((result != null) & trim)
            {
            return result.trim();
            }
        else {
            return result ;
            }
        }
    }
