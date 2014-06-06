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
package uk.ac.roe.wfau.firethorn.util.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 *
 */
public class UnexpectedXMLElementException
extends XMLParserException
    {

    /**
     * 
     *
     */
    private static final long serialVersionUID = 4587018356048220644L;

    /**
     * Build an Exception message.
     * 
     */
    public static String message(final QName qname, final StartElement element)
        {
        StringBuilder builder = new StringBuilder();
        builder.append("Unexpected XML element ");

        builder.append("expected");
        builder.append("[");
        builder.append(qname.getNamespaceURI());
        builder.append("][");
        builder.append(qname.getLocalPart());
        builder.append("]");

        builder.append("found ");
        builder.append("[");
        builder.append(element.getName().getNamespaceURI());
        builder.append("][");
        builder.append(element.getName().getLocalPart());
        builder.append("]");
        
        return builder.toString();
        }

    /**
     *
     *
     */
    public UnexpectedXMLElementException(final QName qname, final StartElement element)
        {
        super(
            message(
                qname,
                element
                ),
            qname,
            element
            );
        }
    }
