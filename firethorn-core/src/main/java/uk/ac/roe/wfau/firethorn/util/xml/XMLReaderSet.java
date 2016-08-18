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

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;

/**
 *
 *
 */
public interface XMLReaderSet<ObjectType extends Object>
extends XMLReader
    {
    /**
     * Add an {@link XMLObjectReader}.
     * 
     */
    public void add(final XMLObjectReader<ObjectType> reader);
    
    /**
     * Process {@link XMLEvent}s from a {@link XMLEventReader},
     * passing them to a matching {@link XMLObjectReader} in our set.
     * @returns The processing result, or null if none of the {@link XMLObjectReader}s in our set matched the {@link XMLEvent}s from the {@link XMLEventReader}.
     *  
     * @throws XMLReaderException If the selected {@link XMLObjectReader} encounters a problem.
     * @throws XMLParserException If the selected {@link XMLObjectReader} encounters a problem.
     * 
     */
    public ObjectType read(final XMLEventReader events)
    throws XMLParserException, XMLReaderException;
    
    }
