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
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;

/**
 * Our ADQL component factories
 *
 */
public interface AdqlFactories
    {
    /**
     * Our resource factory.
     *
     */
    public AdqlResource.EntityFactory resources();

    /**
     * Our schema factory.
     *
     */
    public AdqlSchema.EntityFactory schemas();

    /**
     * Our table factory.
     *
     */
    public AdqlTable.EntityFactory tables();

    /**
     * Our column factory.
     *
     */
    public AdqlColumn.EntityFactory columns();

    /**
     * Our query factory.
     *
     */
    public AdqlQuery.EntityFactory queries();

    /**
     * The local ADQL parser factory.
     *
     */
    public AdqlParser.Factory parsers();

    }
