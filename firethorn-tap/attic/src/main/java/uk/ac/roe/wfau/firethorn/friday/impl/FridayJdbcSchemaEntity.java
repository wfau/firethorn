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
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcTable;
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
    name = "FridayJdbcSchema"
    )
public class FridayJdbcSchemaEntity
extends FridayAbstractEntity
implements FridayJdbcSchema
    {

    @Index(
        name = "parent"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = FridayJdbcCatalogEntity.class
        )
    @JoinColumn(
        unique = false,
        nullable = false,
        updatable = false
        )
    private FridayJdbcCatalog catalog ;

    @Override
    public FridayJdbcCatalog catalog()
        {
        return this.catalog;
        }

    @Override
    public FridayJdbcResource resource()
        {
        return catalog.resource();
        }

    @Override
    public Tables tables()
        {
        return new Tables()
            {
            @Override
            public Iterable<FridayJdbcTable> select()
                {
                return null;
                }
            @Override
            public FridayJdbcTable select(String name)
                {
                return null;
                }
            };
        }
    }
