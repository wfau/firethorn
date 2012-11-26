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
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

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
        @NamedQuery(
            name  = "TuesdayJdbcResource-select-all",
            query = "FROM TuesdayJdbcResourceEntity ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayJdbcResource-search-text",
            query = "FROM TuesdayJdbcResourceEntity WHERE (name LIKE :text) ORDER BY ident desc"
            )
        }
    )
public class TuesdayJdbcResourceEntity
    extends TuesdayBaseResourceEntity<TuesdayJdbcSchema>
    implements TuesdayJdbcResource
    {
    protected static final String DB_TABLE_NAME = "TuesdayJdbcResourceEntity";
    
    protected static final String DB_URI_COL  = "dburi"; 
    protected static final String DB_USER_COL = "dbuser"; 
    protected static final String DB_PASS_COL = "dbpass"; 
    
    /**
     * Resource factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayJdbcResource>
    implements TuesdayJdbcResource.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayJdbcResourceEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayJdbcResource> select()
            {
            return super.iterable(
                super.query(
                    "TuesdayJdbcResource-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayJdbcResource> search(final String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayJdbcResource-search-text"
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }

        @Override
        @CreateEntityMethod
        public TuesdayJdbcResource  create(final String name)
            {
            return super.insert(
                new TuesdayJdbcResourceEntity(
                    name
                    )
                );
            }

        @Autowired
        protected TuesdayJdbcSchema.Factory schemas;

        @Override
        public TuesdayJdbcSchema.Factory schemas()
            {
            return this.schemas;
            }

        @Autowired
        protected TuesdayJdbcResource.IdentFactory idents ;

        @Override
        public TuesdayJdbcResource.IdentFactory identifiers()
            {
            return this.idents ;
            }
        }
    
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
    public TuesdayJdbcResource.Schemas schemas()
        {
        return new TuesdayJdbcResource.Schemas(){
            @Override
            public Iterable<TuesdayJdbcSchema> select()
                {
                // TODO Auto-generated method stub
                return null;
                }
            @Override
            public TuesdayJdbcSchema select(String name)
                {
                // TODO Auto-generated method stub
                return null;
                }
            };
        }
    }
