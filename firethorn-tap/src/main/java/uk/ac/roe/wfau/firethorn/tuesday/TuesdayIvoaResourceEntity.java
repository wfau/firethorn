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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TuesdayIvoaResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        }
    )
public class TuesdayIvoaResourceEntity
    extends TuesdayBaseResourceEntity
    implements TuesdayIvoaResource
    {
    protected static final String DB_TABLE_NAME = "TuesdayIvoaResourceEntity";

    protected static final String DB_URI_COL  = "uri"; 
    protected static final String DB_URL_COL  = "url"; 

    protected TuesdayIvoaResourceEntity()
        {
        super();
        }

    protected TuesdayIvoaResourceEntity(String name)
        {
        super(name);
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_URI_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String uri;
    @Override
    public String uri()
        {
        return this.uri;
        }
    @Override
    public void uri(String uri)
        {
        this.uri = uri;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_URL_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String url;
    @Override
    public String url()
        {
        return this.url;
        }
    @Override
    public void url(String url)
        {
        this.url = url;
        }

    @Override
    public Catalogs catalogs()
        {
        return new Catalogs()
            {
            @Override
            public Iterable<TuesdayIvoaCatalog> select()
                {
                // TODO Auto-generated method stub
                return null;
                }
            @Override
            public TuesdayIvoaCatalog select(String name)
                {
                // TODO Auto-generated method stub
                return null;
                }
            };
        }
    }
