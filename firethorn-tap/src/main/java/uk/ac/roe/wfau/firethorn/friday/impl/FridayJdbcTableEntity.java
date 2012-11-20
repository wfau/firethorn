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
import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.friday.api.FridayAdqlTable;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcCatalog;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcColumn;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcResource;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcSchema;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcTable;
import uk.ac.roe.wfau.firethorn.friday.api.FridayOgsaResource;
import uk.ac.roe.wfau.firethorn.friday.api.FridayOgsaTable;
import uk.ac.roe.wfau.firethorn.identity.Identity;
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
    name = "FridayJdbcTable"
    )
public class FridayJdbcTableEntity
extends FridayAbstractEntity
implements FridayJdbcTable
    {

    @Index(
        name = "parent"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = FridayJdbcSchemaEntity.class
        )
    @JoinColumn(
        unique = false,
        nullable = false,
        updatable = false
        )
    private FridayJdbcSchema schema;

    @Override
    public FridayJdbcSchema schema()
        {
        return this.schema;
        }

    @Override
    public FridayJdbcResource resource()
        {
        return this.schema.resource();
        }

    @Override
    public Columns columns()
        {
        return new Columns()
            {
            @Override
            public Iterable<FridayJdbcColumn> select()
                {
                return null;
                }
            @Override
            public FridayJdbcColumn select(String name)
                {
                return null;
                }
            };
        }

    @Override
    public FridayAdqlTable adql()
        {
        return null;
        }

    @Embedded
    private FridayOgsaTableEntity ogsa ;

    @Override
    public FridayOgsaTable ogsa()
        {
        return new FridayOgsaTable()
            {
            private FridayJdbcTableEntity entity()
                {
                return FridayJdbcTableEntity.this;
                }
            @Override
            public String name()
                {
                return entity().name();
                }

            @Override
            public String link()
                {
                return ogsa.link();
                }

            @Override
            public String alias()
                {
                return ogsa.alias();
                }

            @Override
            public FridayOgsaResource resource()
                {
                return entity().resource().ogsa();
                }

            @Override
            public FridayAdqlTable adql()
                {
                return entity().adql();
                }
            };
        }
    }
