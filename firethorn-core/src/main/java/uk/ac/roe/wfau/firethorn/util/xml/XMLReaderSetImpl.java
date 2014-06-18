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

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 */
@Slf4j
public class XMLReaderSetImpl<ObjectType extends Object>
implements XMLReaderSet<ObjectType>
    {
    /**
     * Public constructor, with no {@link QName}. 
     * 
     */
    public XMLReaderSetImpl()
        {
        this(
            null
            );
        }

    /**
     * Public constructor, with a specific {@link QName} for the set. 
     * 
     */
    public XMLReaderSetImpl(final QName qname)
        {
        this.qname = qname ;
        }

    @Override
    public QName qname()
        {
        return this.qname;
        }
    
    /**
     * Our {@link QName}
     * 
     */
    private QName qname ;

    
    @Override
    public boolean match(XMLEventReader events) throws XMLParserException
        {
        for (XMLReader reader : readers)
            {
            if (reader.match(events))
                {
                return true;
                }
            }
        return false;
        }

    @Override
    public boolean match(XMLEvent event) throws XMLParserException
        {
        log.debug("match(XMLEvent)");
        log.debug("  event [{}]", event.toString());
        for (XMLReader reader : readers)
            {
            if (reader.match(event))
                {
                return true;
                }
            }
        return false;
        }

    /**
     * Our set of {@link XMLReader}s.
     * 
     */
    private Set<XMLObjectReader<ObjectType>> readers = new HashSet<XMLObjectReader<ObjectType>>();
    
    @Override
    public void add(final XMLObjectReader<ObjectType> reader)
        {
        readers.add(
            reader
            );
        }

    /**
     * Flag to indicate if this set is required. 
     * 
     */
    private boolean required = false ;

    @Override
    public ObjectType read(final XMLEventReader events)
    throws XMLParserException, XMLReaderException 
        {
        for (XMLObjectReader<ObjectType> reader : readers)
            {
            if (reader.match(events))
                {
                return reader.read(
                    events
                    );
                }
            }
        if (required)
            {
            throw new XMLReaderException(
                "No matching reader found"
                );
            }
        else {
            return null ;
            }
        }
    }
