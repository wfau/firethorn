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
package uk.ac.roe.wfau.firethorn.tuesday;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.adql.AdqlDBParser;

/**
 *
 *
 */
@Component
public class AdqlFactoriesImpl
    implements AdqlFactories
    {

    @Autowired
    private AdqlResource.Factory resources;
    @Override
    public AdqlResource.Factory resources()
        {
        return this.resources;
        }

    @Autowired
    private TuesdayAdqlSchema.Factory schemas;
    @Override
    public TuesdayAdqlSchema.Factory schemas()
        {
        return this.schemas;
        }

    @Autowired
    private TuesdayAdqlTable.Factory tables;
    @Override
    public TuesdayAdqlTable.Factory tables()
        {
        return this.tables;
        }

    @Autowired
    private AdqlColumn.Factory columns;
    @Override
    public AdqlColumn.Factory columns()
        {
        return this.columns;
        }

    @Autowired
    private AdqlQuery.Factory queries;
    @Override
    public AdqlQuery.Factory queries()
        {
        return this.queries;
        }

    @Autowired
    private AdqlDBParser.Factory parsers;
    @Override
    public AdqlDBParser.Factory parsers()
        {
        return this.parsers;
        }
    }
