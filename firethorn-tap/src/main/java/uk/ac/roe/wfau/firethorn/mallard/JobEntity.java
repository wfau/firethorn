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
package uk.ac.roe.wfau.firethorn.mallard ;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Access;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.common.womble.Womble;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonEntity;

/**
 * Mallard.Job implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = JobEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "mallard.job-select",
            query = "FROM JobEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "mallard.job-select-mallard",
            query = "FROM JobEntity WHERE mallard = :mallard ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "mallard.job-select-status",
            query = "FROM JobEntity WHERE status = :status ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "mallard.job-select-mallard.status",
            query = "FROM JobEntity WHERE mallard = :mallard AND status = :status ORDER BY ident desc"
            )
        }
    )
public class JobEntity
extends AbstractEntity
implements Mallard.Job
    {

    /**
     * Our database table name.
     * 
     */
    public static final String DB_TABLE_NAME = "job_entity" ;

    /*
     * Our database mapping values.
     * 
     */
    public static final String DB_ADQL_COL    = "adql"   ;
    public static final String DB_STATUS_COL  = "status" ;
    public static final String DB_MALLARD_COL = "mallard" ;

    /**
     * Mallard.Job factory.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Mallard.Job>
    implements Mallard.Job.Factory
        {
        @Override
        public Class etype()
            {
            return JobEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<Mallard.Job> select()
            {
            return super.iterable(
                super.query(
                    "mallard.job-select"
                    )
                );
            }

        @SelectEntityMethod
        public Iterable<Mallard.Job> select(Mallard mallard)
            {
            return super.iterable(
                super.query(
                    "mallard.job-select-mallard"
                    ).setEntity(
                        "mallard",
                        mallard
                        )
                );
            }

        @Override
        @CreateEntityMethod
        public Mallard.Job create(Mallard mallard, String name, String adql)
            {
            return super.insert(
                new JobEntity(
                    mallard,
                    name,
                    adql
                    )
                );
            }
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected JobEntity()
        {
        super();
        }

    /**
     * Create a new JobEntity.
     *
     */
    protected JobEntity(Mallard mallard, String name, String adql)
        {
        super(name);
        this.adql = adql ;
        this.mallard = mallard ;
        }

    /**
     * Our parent Mallard.
     *
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = MallardEntity.class
        )
    @JoinColumn(
        name = DB_MALLARD_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Mallard mallard ;
    @Override
    public Mallard mallard()
        {
        return this.mallard ;
        }

    /**
     * The Job status.
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
    private Mallard.Job.Status status = Mallard.Job.Status.EDITING ;
    @Override
    public  Mallard.Job.Status status()
        {
        return this.status;
        }

    /**
     * The ADQL query.
     *
     */
    @Column(
        name = DB_ADQL_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String adql;
    @Override
    public  String adql()
        {
        return this.adql;
        }
    }

