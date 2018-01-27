/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.access;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Method;

/**
 *
 *
 */
@Embeddable
@Access(
    AccessType.FIELD
    )
public class AccessMapField
    {
    /**
     * Hibernate column mapping.
     * 
     */
    public static final String DB_ENTITY_COL   = "entity"   ;
    public static final String DB_ACTION_COL   = "action"   ;
    public static final String DB_METHOD_COL   = "method"   ;
    public static final String DB_IDENTITY_COL = "identity" ;

    /**
     * Public constructor.
     *
     */
    public AccessMapField(final Entity entity, final Action action, final Identity identity, final Method method)
        {
        this.entity = entity;
        this.action = action;
        this.identity = identity;
        }

    @Column(
        name = DB_ENTITY_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Entity entity;
    public Entity entity()
        {
        return entity;
        }

    @Column(
        name = DB_ACTION_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Action action;
    public Action action()
        {
        return action;
        }

    @Column(
        name = DB_IDENTITY_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Identity identity;
    public Identity identity()
        {
        return identity ;
        }

    @Column(
        name = DB_METHOD_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private String method;
    public Method method()
        {
        return new Method()
            {
            @Override
            public String urn()
                {
                return AccessMapField.this.method;
                }
            };
        }

    
    public Authentication authentication()
        {
        return new Authentication()
            {
            @Override
            public Method method()
                {
                return AccessMapField.this.method();
                }

            @Override
            public Identity identity()
                {
                return AccessMapField.this.identity();
                }
            };
        }
    }
