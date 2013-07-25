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
import javax.xml.stream.events.XMLEvent;

/**
 *
 *
 */
public interface XMLReader
    {
    /**
     * The XML element QName this reader handles.
     * 
     */
    public QName qname();

    /**
     * Peek at the next event from an XMLEventReader to see if it matches this reader.
     * This may skip processing instructions, comments and whitespace to find the next element.
     * 
     * @returns true if the next event matches.
     * 
     */
    public boolean match(final XMLEventReader reader)
    throws XMLParserException;

    /**
     * Check if an event matches this reader.
     * @returns true if the event matches this reader.
     * 
     */
    public boolean match(final XMLEvent event)
    throws XMLParserException;

    }
