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
package uk.ac.roe.wfau.firethorn.friday.impl;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcCatalog;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcResource;
import uk.ac.roe.wfau.firethorn.friday.api.FridayOgsaResource;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = "FridayJdbcResource"
    )
public class FridayJdbcResourceEntity
extends FridayAbstractEntity
implements FridayJdbcResource
    {

    @Column(
        unique = false,
        nullable = true,
        updatable = true
        )
    private String dburl ;

    @Override
    public String url()
        {
        return this.dburl ;
        }

    @Column(
        unique = false,
        nullable = true,
        updatable = true
        )
    private String dbuser;

    @Override
    public String user()
        {
        return this.dbuser;
        }

    @Column(
        unique = false,
        nullable = true,
        updatable = true
        )
    private String dbpass ;
    
    @Override
    public String pass()
        {
        return this.dbpass;
        }

    @Override
    public Catalogs catalogs()
        {
        return new Catalogs()
            {
            @Override
            public Iterable<FridayJdbcCatalog> select()
                {
                return null;
                }

            @Override
            public FridayJdbcCatalog select(String name)
                {
                return null;
                }
            };
        }

    //@Embedded
    //private FridayOgsaResourceEntity ogsa ;

    @Column(
        unique = false,
        nullable = true,
        updatable = true
        )
    private String ogsa_id;

    @Override
    public FridayOgsaResource ogsa()
        {
        return new FridayOgsaResource()
            {
            @Override
            public String id()
                {
                return ogsa_id;
                }

            @Override
            public String name()
                {
                return FridayJdbcResourceEntity.this.name();
                }

            @Override
            public String text()
                {
                return FridayJdbcResourceEntity.this.text();
                }

            @Override
            public String link()
                {
                return null;
                }
            };
        }
    }
