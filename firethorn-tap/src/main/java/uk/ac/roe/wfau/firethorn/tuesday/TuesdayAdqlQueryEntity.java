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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.type.TextType;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.AdqlDBParser;
import uk.ac.roe.wfau.firethorn.adql.AdqlDBQuery;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameFormatException;

/**
 *
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TuesdayAdqlQueryEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TuesdayAdqlQuery-select-resource",
            query = "FROM TuesdayAdqlQueryEntity WHERE resource = :resource ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayAdqlQuery-select-resource.name",
            query = "FROM TuesdayAdqlQueryEntity WHERE ((resource = :resource) AND (name = :name)) ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayAdqlQuery-search-resource.text",
            query = "FROM TuesdayAdqlQueryEntity WHERE ((resource = :resource) AND (name LIKE :text)) ORDER BY name asc, ident desc"
            )
        }
     )       
public class TuesdayAdqlQueryEntity
extends AbstractEntity
implements TuesdayAdqlQuery, AdqlDBQuery
    {
    /**
     * Hibernate table mapping.
     * 
     */
    protected static final String DB_TABLE_NAME = "TuesdayAdqlQueryEntity";
    /**
     * Hibernate column mapping.
     * 
     */
    protected static final String DB_MODE_COL   = "mode";
    protected static final String DB_INPUT_COL  = "input";
    protected static final Integer DB_INPUT_LEN  = new Integer(1024);
    protected static final String DB_STATUS_COL = "status";
    protected static final String DB_RESOURCE_COL = "resource";

    /**
     * Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayAdqlQuery>
    implements TuesdayAdqlQuery.Factory
        {
        @Override
        public Class<?> etype()
            {
            return TuesdayAdqlQueryEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public TuesdayAdqlQuery create(final TuesdayAdqlResource resource, final String input)
            {
            return this.insert(
                new TuesdayAdqlQueryEntity(
                    resource,
                    this.names.name(),
                    input
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public TuesdayAdqlQuery create(final TuesdayAdqlResource resource, final String name, final String input)
            {
            return this.insert(
                new TuesdayAdqlQueryEntity(
                    resource,
                    name,
                    input
                    )
                );
            }

        @Autowired
        private TuesdayAdqlQuery.NameFactory names;
        @Override
        public TuesdayAdqlQuery.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private TuesdayAdqlQuery.LinkFactory links;
        @Override
        public TuesdayAdqlQuery.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private TuesdayAdqlQuery.IdentFactory idents;
        @Override
        public TuesdayAdqlQuery.IdentFactory idents()
            {
            return this.idents;
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayAdqlQuery> select(TuesdayAdqlResource resource)
            {
            return super.list(
                super.query(
                    "TuesdayAdqlQuery-select-resource"
                    ).setEntity(
                        "resource",
                        resource
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayAdqlQuery> search(TuesdayAdqlResource resource, String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayAdqlQuery-search-resource.text"
                    ).setEntity(
                        "resource",
                        resource
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }
        }

    protected TuesdayAdqlQueryEntity()
        {
        }

    protected TuesdayAdqlQueryEntity(final TuesdayAdqlResource resource, final String name, final String input)
    throws NameFormatException
        {
        super(
            name
            );
        this.input = input;
        this.resource = resource;
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByResource"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayAdqlResourceEntity.class
        )
    @JoinColumn(
        name = DB_RESOURCE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayAdqlResource resource;
    @Override
    public TuesdayAdqlResource resource()
        {
        return this.resource;
        }

    /**
     * The component status.
     *
     */
    @Column(
        name = DB_STATUS_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Status status = Status.EDITING;
    @Override
    public Status status()
        {
        return this.status;
        }

    @Column(
        name = DB_MODE_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Mode mode = Mode.DIRECT ;
    @Override
    public Mode mode()
        {
        return this.mode;
        }

    /*
     *
    org.hsqldb.HsqlException: data exception: string data, right truncation

    length=DB_INPUT_LEN,
    => input varchar(1000)

    @org.hibernate.annotations.Type(
        type="org.hibernate.type.TextType"
        )        
    => input longvarchar
    
    @Lob
    => input clob

     *
     */
    @Type(
        type="org.hibernate.type.TextType"
        )        
    @Column(
        name = DB_INPUT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String input;
    @Override
    public String input()
        {
        return this.input;
        }
    @Override
    public void input(final String input)
        {
        this.input = input;
        }

    /**
     * Parse the query and update our properties.
     *
     */
    @Override
    public void parse()
        {
        //
        // Reset everything.
        reset();
        //
        // Create the two query parsers.
        // TODO -- This should be part of the workspace.
        final AdqlDBParser direct = this.factories().adql().parsers().create(
            Mode.DIRECT,
            this.resource
            );
        final AdqlDBParser distrib = this.factories().adql().parsers().create(
            Mode.DISTRIBUTED,
            this.resource
            );
        //
        // Process as a direct query to start with.
        direct.process(
            this
            );
        //
        // If we have multiple resources, re-process as a distributed query.
        // TODO - In theory, we could re-use the same parsers by using a ThreadLocal for the mode ...
        if (this.resources.size() > 1)
            {
            log.debug("Query uses multiple resources");
            distrib.process(
                this
                );
            }
        }

    /**
     * Reset our internal data.
     *
     */
    protected void reset()
        {
        this.adql = null ;
        this.ogsa = null ;
        this.mode = Mode.DIRECT ;

        this.columns.clear();
        this.tables.clear();
        this.resources.clear();
        }

//
// These are all transient for now,
// will make them persistent later ...
    
    @Transient
    private String adql;
    @Override
    public String adql()
        {
        return this.adql;
        }

    @Transient
    private String ogsa;
    @Override
    public String ogsa()
        {
        return this.ogsa;
        }

    @Transient
    private final Set<TuesdayAdqlColumn> columns = new HashSet<TuesdayAdqlColumn>();
    @Override
    public Iterable<TuesdayAdqlColumn> columns()
        {
        return this.columns;
        }

    @Transient
    private final Set<TuesdayAdqlTable> tables = new HashSet<TuesdayAdqlTable>();
    @Override
    public Iterable<TuesdayAdqlTable> tables()
        {
        return this.tables;
        }

    @Transient
    private final Set<TuesdayOgsaResource<?>> resources = new HashSet<TuesdayOgsaResource<?>>();
    @Override
    public Iterable<TuesdayOgsaResource<?>> resources()
        {
        return this.resources;
        }


    @Override
    public String link()
        {
        // TODO Auto-generated method stub
        return null;
        }

//    
// AdqlDBQuery methods ..
//
    @Override
    public void mode(final Mode mode)
        {
        this.mode = mode;
        }

    @Override
    public void status(final Status status)
        {
        this.status = status;
        }

    @Override
    public void adql(final String adql)
        {
        this.adql = adql;
        }

    @Override
    public void ogsa(final String ogsa)
        {
        this.ogsa = ogsa;
        }

    @Override
    public void add(final TuesdayAdqlColumn column)
        {
        this.columns.add(
            column
            );
        this.tables.add(
            column.table()
            );
        this.resources.add(
            column.ogsa().resource()
            );
        }

    @Override
    public void add(final TuesdayAdqlTable table)
        {
        this.tables.add(
            table
            );
        this.resources.add(
            table.ogsa().resource()
            );
        }
    }
