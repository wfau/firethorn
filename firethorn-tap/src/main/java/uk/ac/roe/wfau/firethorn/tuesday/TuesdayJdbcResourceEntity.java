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
    name = TuesdayJdbcResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        }
    )
public class TuesdayJdbcResourceEntity
    extends TuesdayBaseNameEntity
    implements TuesdayJdbcResource
    {
    protected static final String DB_TABLE_NAME = "TuesdayJdbcResourceEntity";
    
    protected static final String DB_URI_COL  = "dburi"; 
    protected static final String DB_USER_COL = "dbuser"; 
    protected static final String DB_PASS_COL = "dbpass"; 

    protected TuesdayJdbcResourceEntity()
        {
        super();
        }

    protected TuesdayJdbcResourceEntity(String name)
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
        name = DB_USER_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String user;
    @Override
    public String user()
        {
        return this.user;
        }
    @Override
    public void user(String user)
        {
        this.user = user;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_PASS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String pass;
    @Override
    public String pass()
        {
        return this.pass;
        }
    @Override
    public void pass(String pass)
        {
        this.pass = pass;
        }

    @Override
    public Catalogs catalogs()
        {
        return new Catalogs(){
            @Override
            public Iterable<TuesdayJdbcCatalog> select()
                {
                return null;
                }
            @Override
            public TuesdayJdbcCatalog select(String name)
                {
                return null;
                }
            };
        }
    }
