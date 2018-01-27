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
package uk.ac.roe.wfau.firethorn.meta.ogsa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 *
 *
 */
@Component
public class OgsaTableResolverImpl
    implements OgsaTableResolver
    {
    
    /**
     * Our {@link AdqlTable.AliasFactory} implementation.
     * 
     */
    @Autowired
    private AdqlTable.AliasFactory adql ; 

    /**
     * Our {@link IvoaTable.AliasFactory} implementation.
     * 
     */
    @Autowired
    private IvoaTable.AliasFactory ivoa ; 

    /**
     * Our {@link JdbcTable.AliasFactory} implementation.
     * 
     */
    @Autowired
    private JdbcTable.AliasFactory jdbc ; 
    
    @Override
    public BaseTable<?, ?> resolve(String alias)
    throws ProtectionException, EntityNotFoundException
        {
        if (adql.matches(alias))
            {
            return adql.resolve(alias);
            }
        else if (ivoa.matches(alias))
            {
            return ivoa.resolve(alias);
            }
        else if (jdbc.matches(alias))
            {
            return jdbc.resolve(alias);
            }
        else {
            throw new EntityNotFoundException(
                alias
                );
            }
        }
    }
