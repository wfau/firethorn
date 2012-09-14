/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.votable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.votable.TableElement;
import uk.ac.starlink.votable.VOElement;
import uk.ac.starlink.votable.VOElementFactory;
import uk.ac.starlink.votable.VOStarTable;


/**
 * StarTableParser for VOTable files.
 *
 */
@Slf4j
public class VOTableStarTableParser
implements Iterator<StarTable>
    {

    /**
     * Wrap an InputStream as a Iterable<StarTable>.
     *
     */
    public static Iterable<StarTable> iterable(final InputStream stream)
        {
        return new Iterable<StarTable>()
            {
            @Override
            public Iterator<StarTable> iterator()
                {
                try {
                    return new VOTableStarTableParser(
                        stream
                        );
                    }
                catch (final Exception ouch)
                    {
                    throw new RuntimeException(
                        "Unable to create Iterable<StarTable> from an InputStream",
                        ouch
                        ) ;
                    }
                }
            };
        }

    /**
     * Public constructor from an InputStream.
     *
     */
    public VOTableStarTableParser(final InputStream stream)
        {
        super();
        this.init(stream);
        }

    /**
     * List of RESOURCE elements.
     *
     */
    private NodeList resourceList ;
    int resourceCount = 0 ;
    int resourceIndex = 0 ;

    /**
     * Array of TABLE elements.
     *
     */
    private VOElement[] tableArray ;
    int tableCount = 0 ;
    int tableIndex = 0 ;

    @Override
    public void remove()
        {
        throw new UnsupportedOperationException(
            "Iterator.remove() not supported"
            );
        }

    @Override
    public boolean hasNext()
        {
        if (tableIndex < tableCount)
            {
            return true;
            }
        else if (resourceIndex < resourceCount)
            {
            return true;
            }
        else {
            return false;
            }
        }

    /**
     * Initialise our VOTable parser from an InputStream.
     *
     */
    protected void init(final InputStream stream)
        {
        try {
            this.init(
                new VOElementFactory().makeVOElement(
                    stream,
                    null
                    )
                );
            }
        catch (final SAXException ouch)
            {
            log.error("SAXException while processing VOTable [" + ouch.getMessage() + "]");
            }
        catch (final IOException ouch)
            {
            log.error("IOException while processing VOTable [" + ouch.getMessage() + "]");
            }
        }

    /**
     * Initialise our VOTable parser from top level VOElement.
     *
     */
    protected void init(final VOElement top)
    throws IOException
        {
        //
        // Find the RESOURCE elements using standard DOM method.
        resourceList = top.getElementsByTagName(
            "RESOURCE"
            );
        resourceCount = resourceList.getLength() ;
        resourceIndex = 0 ;
        }

    protected void nextResource()
        {
        log.debug("VOTableParser.nextResource()");
        log.debug("Resources [" + resourceIndex + "][" + resourceCount + "]");
        if (resourceIndex < resourceCount)
            {
            //
            // Get the TABLE elements using VOElement method.
            final VOElement resource = (VOElement) resourceList.item(
                resourceIndex++
                );
            tableArray = resource.getChildrenByName("TABLE");
            tableCount = tableArray.length ;
            tableIndex = 0 ;
            }
        else {
            tableCount = 0 ;
            tableIndex = 0 ;
            }
        }

    protected TableElement nextTable()
        {
        log.debug("VOTableParser.nextTable()");
        log.debug("Tables [" + tableIndex + "][" + tableCount + "]");
        if (tableIndex >= tableCount)
            {
            nextResource();
            }
        if (tableIndex < tableCount)
            {
            return (TableElement) tableArray[tableIndex++] ;
            }
        else {
            throw new IllegalStateException(
                "Unable to process next TableElement - no data"
                );
            }
        }

    @Override
    public StarTable next()
        {
        log.debug("VOTableParser.next()");
        try {
            return new VOStarTable(
                nextTable()
                );
            }
        catch (final IOException ouch)
            {
            log.error("IOException while processing VOTable [" + ouch.getMessage() + "]");
            throw new IllegalStateException(
                "IOException thrown inside Iterator.next()",
                ouch
                );
            }
        }
    }

