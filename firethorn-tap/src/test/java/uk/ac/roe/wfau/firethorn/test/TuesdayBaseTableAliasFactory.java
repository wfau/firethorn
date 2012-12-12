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
package uk.ac.roe.wfau.firethorn.test;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractIdentFactory;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.LongIdentifier;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayBaseTable;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayBaseTable.AliasFactory;

/**
 * JUnit test implementation.
 *
 */
@Slf4j
@Component
public class TuesdayBaseTableAliasFactory
//extends AbstractIdentFactory<TuesdayBaseTable<?,?>>
//implements AliasFactory
    {
/*
    protected static final String PREFIX = "TABLE_" ;
    
    //@Override
    public String alias(final TuesdayBaseTable<?,?> table)
        {
        return PREFIX + table.ident().toString();
        }

    //@Override
    public Identifier ident(final String alias)
        {
        return super.ident(
            alias.substring(
                PREFIX.length()
                )
            );
        }

    //@Override
    public String link(TuesdayBaseTable<?,?> entity)
        {
        return null;
        }

    //@Override
    public TuesdayBaseTable<?, ?> select(String alias) throws NotFoundException
        {
        // TODO Auto-generated method stub
        return null;
        }
 */        
    }
