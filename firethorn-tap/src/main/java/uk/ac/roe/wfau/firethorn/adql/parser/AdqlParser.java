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
package uk.ac.roe.wfau.firethorn.adql.parser;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;

/**
 *
 *
 */
public interface AdqlParser
    {
    /**
     * Factory interface.
     *
     */
    public static interface Factory
        {
        /**
         * Create a parser for a workspace.
         *
         */
        public AdqlParser create(final AdqlQuery.Mode mode, final AdqlResource workspace);

        }

    /**
     * Process an ADQL query and populate its components.
     *
     */
    public void process(final AdqlParserQuery query);

    }
