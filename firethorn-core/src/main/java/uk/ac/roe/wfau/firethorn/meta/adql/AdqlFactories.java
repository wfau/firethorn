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
package uk.ac.roe.wfau.firethorn.meta.adql;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParser;
import uk.ac.roe.wfau.firethorn.adql.query.GreenQuery;
import uk.ac.roe.wfau.firethorn.blue.BlueQuery;

/**
 * Our ADQL component factories
 *
 */
public interface AdqlFactories
    {
    /**
     * Our {@link AdqlResource.EntityServices} instance.
     *
     */
    public AdqlResource.EntityServices resources();

    /**
     * Our {@link AdqlSchema.EntityServices} instance.
     *
     */
    public AdqlSchema.EntityServices schemas();

    /**
     * Our {@link AdqlTable.EntityServices} instance.
     *
     */
    public AdqlTable.EntityServices tables();

    /**
     * Our {@link AdqlColumn.EntityServices} instance.
     *
     */
    public AdqlColumn.EntityServices columns();

    /**
     * Our {@link GreenQuery.EntityServices} instance.
     *
     */
    public GreenQuery.EntityServices greens();

    /**
     * Our {@link BlueQuery.EntityServices} instance.
     *
     */
    public BlueQuery.EntityServices blues();
    
    /**
     * The local ADQL parser factory.
     *
     */
    public AdqlParser.Factory parsers();

    }
