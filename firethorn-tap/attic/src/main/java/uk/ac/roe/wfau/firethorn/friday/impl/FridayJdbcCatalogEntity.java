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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcCatalog;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcResource;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcSchema;
import uk.ac.roe.wfau.firethorn.friday.api.FridayOgsaResource;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceEntity;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = "FridayJdbcCatalog"
    )
public class FridayJdbcCatalogEntity
extends FridayAbstractEntity
implements FridayJdbcCatalog
    {

    @Index(
        name = "parent"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = FridayJdbcResourceEntity.class
        )
    @JoinColumn(
        unique = false,
        nullable = false,
        updatable = false
        )
    private FridayJdbcResource resource ;

    @Override
    public FridayJdbcResource resource()
        {
        return this.resource  ;
        }

    @Override
    public Schemas schemas()
        {
        return new Schemas()
            {
            @Override
            public Iterable<FridayJdbcSchema> select()
                {
                return null;
                }

            @Override
            public FridayJdbcSchema select(String name)
                {
                return null;
                }
            };
        }
    }
