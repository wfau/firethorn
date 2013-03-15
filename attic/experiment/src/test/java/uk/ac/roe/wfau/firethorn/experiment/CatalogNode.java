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

import org.metagrid.gatekeeper.node.Node;

/**
 *
 */
public interface CatalogNode
extends Node
    {

    /**
     * The Node type URI for a Catalog.
     * 
     */
    public static final URI NODE_TYPE_URI = URI.create(
        "urn:uk.ac.roe.wfau.firethorn.catalog"
        );

    /**
     * Access to our Catalog.
     * 
     */
    public WidgeonBase.Catalog catalog();

    }

