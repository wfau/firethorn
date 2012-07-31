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
package uk.ac.roe.wfau.firethorn.experiment ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon ;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonBase ;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.exception.*;

import org.metagrid.gatekeeper.test.object.node.ObjectNode;

/**
 *
 */
public interface WidgeonNode
extends ObjectNode<Widgeon>
    {

    /**
     * The Node type URI for a Widgeon.
     * 
     */
    public static final URI NODE_TYPE_URI = URI.create(
        "urn:uk.ac.roe.wfau.firethorn.widgeon"
        );

    /**
     * The Widgeon name property URI.
     * 
     */
    public static final URI NAME_PROPERTY_URI = URI.create(
        "urn:uk.ac.roe.wfau.firethorn.name"
        );

    /**
     * The Widgeon created property URI.
     * 
     */
    public static final URI CREATED_PROPERTY_URI = URI.create(
        "urn:uk.ac.roe.wfau.firethorn.created"
        );

    /**
     * The Widgeon modified property URI.
     * 
     */
    public static final URI MODIFIED_PROPERTY_URI = URI.create(
        "urn:uk.ac.roe.wfau.firethorn.modified"
        );

    /**
     * Access to our Widgeon.
     * 
     */
    public Widgeon widgeon();

    }

