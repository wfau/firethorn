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
package uk.ac.roe.wfau.firethorn.adql.query;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParser;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaResource;

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
    name = AdqlQueryEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "AdqlQuery-select-resource",
            query = "FROM AdqlQueryEntity WHERE resource = :resource ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "AdqlQuery-select-resource.name",
            query = "FROM AdqlQueryEntity WHERE ((resource = :resource) AND (name = :name)) ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "AdqlQuery-search-resource.text",
            query = "FROM AdqlQueryEntity WHERE ((resource = :resource) AND (name LIKE :text)) ORDER BY name asc, ident desc"
            )
        }
     )       
public class AdqlQueryEntity
extends AbstractEntity
implements AdqlQuery, AdqlParserQuery
    {
    /**
     * Hibernate table mapping.
     * 
     */
    protected static final String DB_TABLE_NAME = "AdqlQueryEntity";
    /**
     * Hibernate column mapping.
     * 
     */
    protected static final String DB_MODE_COL     = "mode";
    protected static final String DB_QUERY_COL    = "query";
    protected static final Integer DB_QUERY_LEN   = new Integer(1024);
    protected static final String DB_STATUS_COL   = "status";
    protected static final String DB_RESOURCE_COL = "resource";

    /**
     * Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<AdqlQuery>
    implements AdqlQuery.Factory
        {
        @Override
        public Class<?> etype()
            {
            return AdqlQueryEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public AdqlQuery create(final AdqlResource resource, final String query)
            {
            return this.insert(
                new AdqlQueryEntity(
                    resource,
                    this.names.name(),
                    query
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlQuery create(final AdqlResource resource, final String name, final String query)
            {
            return this.insert(
                new AdqlQueryEntity(
                    resource,
                    name,
                    query
                    )
                );
            }

        @Autowired
        private AdqlQuery.NameFactory names;
        @Override
        public AdqlQuery.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private AdqlQuery.LinkFactory links;
        @Override
        public AdqlQuery.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private AdqlQuery.IdentFactory idents;
        @Override
        public AdqlQuery.IdentFactory idents()
            {
            return this.idents;
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlQuery> select(AdqlResource resource)
            {
            return super.list(
                super.query(
                    "AdqlQuery-select-resource"
                    ).setEntity(
                        "resource",
                        resource
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlQuery> search(AdqlResource resource, String text)
            {
            return super.iterable(
                super.query(
                    "AdqlQuery-search-resource.text"
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

    protected AdqlQueryEntity()
        {
        }

    protected AdqlQueryEntity(final AdqlResource resource, final String name, final String query)
    throws NameFormatException
        {
        super(
            name
            );
        this.resource = resource;
        this.query(
            query
            );
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByResource"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = AdqlResourceEntity.class
        )
    @JoinColumn(
        name = DB_RESOURCE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private AdqlResource resource;
    @Override
    public AdqlResource resource()
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
    => query varchar(1000)

    @org.hibernate.annotations.Type(
        type="org.hibernate.type.TextType"
        )        
    => query longvarchar
    
    @Lob
    => query clob

     *
     */
    @Type(
        type="org.hibernate.type.TextType"
        )        
    @Column(
        name = DB_QUERY_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String query;
    @Override
    public String query()
        {
        return this.query;
        }
    @Override
    public void query(final String next)
        {
        final String prev = this.query ; 
        this.query = next;
        //
        // Parse the query if it has changed.
        if (next != null)
            {
            if ((prev == null) || (prev.compareTo(next) > 0))
                {
                parse();
                }
            }
       }

    /**
     * Parse the query and update our properties.
     *
    @Override
     */
    public void parse()
        {
        //
        // Reset everything.
        reset();
        //
        // Create the two query parsers.
        // TODO -- This should be part of the workspace.
        final AdqlParser direct = this.factories().adql().parsers().create(
            Mode.DIRECT,
            this.resource
            );
        final AdqlParser distrib = this.factories().adql().parsers().create(
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
    private final Set<AdqlColumn> columns = new HashSet<AdqlColumn>();
    @Override
    public Iterable<AdqlColumn> columns()
        {
        return this.columns;
        }

    @Transient
    private final Set<AdqlTable> tables = new HashSet<AdqlTable>();
    @Override
    public Iterable<AdqlTable> tables()
        {
        return this.tables;
        }

    @Transient
    private final Set<OgsaResource<?>> resources = new HashSet<OgsaResource<?>>();
    @Override
    public Iterable<OgsaResource<?>> resources()
        {
        return this.resources;
        }

    @Override
    public String link()
        {
        return factories().adql().queries().links().link(
            this
            );
        }


//    
// AdqlParserQuery methods ..
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
    public void add(final AdqlColumn column)
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
    public void add(final AdqlTable table)
        {
        this.tables.add(
            table
            );
        this.resources.add(
            table.ogsa().resource()
            );
        }
    }
