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
package uk.ac.roe.wfau.firethorn.entity;


/**
 * A simple {@link Entity.AliasFactory} implementation, using a prefix plus the identifier.
 *
 */
public abstract class SimpleAliasFactory<EntityType extends Entity>
    implements Entity.AliasFactory<EntityType>
    {
    /**
     * Our alias prefix.
     * 
     */
    private String prefix ; 

    /**
     * Our alias prefix.
     * 
     */
    public String prefix()
        {
        return this.prefix;
        } 

    /**
     * Public constructor.
     * 
     */
    public SimpleAliasFactory(final String prefix)
        {
        this.prefix = prefix;
        }

    @Override
    public String alias(final EntityType entity)
        {
        return this.prefix .concat(
            entity.ident().toString()
            );
        }

    @Override
    public boolean matches(String alias)
        {
        return alias.startsWith(
            this.prefix
            );
        }

/*    
    @Override
    public EntityType resolve(String alias)
        throws EntityNotFoundException
        {
        return entities.select(
            idents.ident(
                alias.substring(
                    PREFIX.length()
                    )
                )
            );
 */
    }
