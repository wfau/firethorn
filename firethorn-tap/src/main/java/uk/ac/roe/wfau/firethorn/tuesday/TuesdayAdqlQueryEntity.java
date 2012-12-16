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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import adql.db.DBChecker;
import adql.db.DBTable;
import adql.parser.ADQLParser;
import adql.parser.ParseException;
import adql.query.ADQLQuery;

import uk.ac.roe.wfau.firethorn.adql.AdqlDBParser;
import uk.ac.roe.wfau.firethorn.adql.AdqlDBQuery;
import uk.ac.roe.wfau.firethorn.adql.AdqlDBTable;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlQuery.Status;

/**
 *
 *
 */
@Slf4j
public class TuesdayAdqlQueryEntity
extends AbstractEntity
implements TuesdayAdqlQuery, AdqlDBQuery
    {
    
    /**
     * Factory implementation.
     *
     */
    @Repository
    public static class Factory
    implements TuesdayAdqlQuery.Factory
        {

        @Override
        @CreateEntityMethod
        public TuesdayAdqlQuery create(final TuesdayAdqlResource workspace, final String input)
            {
            return create(
                workspace,
                "query",
                input
                );
            }

        @Override
        @CreateEntityMethod
        public TuesdayAdqlQuery create(final TuesdayAdqlResource workspace, final String name, final String input)
            {
            return new TuesdayAdqlQueryEntity(
                workspace,
                name,
                input
                );
            }

        }
    
    protected TuesdayAdqlQueryEntity()
        {
        }

    protected TuesdayAdqlQueryEntity(final TuesdayAdqlResource workspace, final String name, final String input)
    throws NameFormatException
        {
        super(
            name
            );
        this.input = input;
        this.workspace = workspace;
        }

    private TuesdayAdqlResource workspace;
    @Override
    public TuesdayAdqlResource workspace()
        {
        return this.workspace;
        }
 
    private Status status;
    @Override
    public Status status()
        {
        return this.status;
        }
    
    private Mode mode = Mode.DIRECT ;
    @Override
    public Mode mode()
        {
        return this.mode;
        }

    private String input;
    @Override
    public String input()
        {
        return this.input;
        }
    @Override
    public void input(String input)
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
        AdqlDBParser direct = this.factories().adql().parsers().create(
            Mode.DIRECT,
            this.workspace
            );        
        AdqlDBParser distrib = this.factories().adql().parsers().create(
            Mode.DISTRIBUTED,
            this.workspace
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
    private Set<TuesdayAdqlColumn> columns = new HashSet<TuesdayAdqlColumn>();
    @Override
    public Iterable<TuesdayAdqlColumn> columns()
        {
        return this.columns;
        }

    @Transient
    private Set<TuesdayAdqlTable> tables = new HashSet<TuesdayAdqlTable>();
    @Override
    public Iterable<TuesdayAdqlTable> tables()
        {
        return this.tables;
        }

    @Transient
    private Set<TuesdayOgsaResource<?>> resources = new HashSet<TuesdayOgsaResource<?>>();
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
    public void mode(Mode mode)
        {
        this.mode = mode;
        }
    
    @Override
    public void status(Status status)
        {
        this.status = status;
        }

    @Override
    public void adql(String adql)
        {
        this.adql = adql;
        }

    @Override
    public void ogsa(String ogsa)
        {
        this.ogsa = ogsa;
        }

    @Override
    public void add(TuesdayAdqlColumn column)
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
    public void add(TuesdayAdqlTable table)
        {
        this.tables.add(
            table
            );
        this.resources.add(
            table.ogsa().resource()
            );
        }
    }
