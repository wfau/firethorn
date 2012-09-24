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

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

/**
 * AdqlService.Job implementation.
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
            name  = "service.job-select-all",
            query = "FROM JobEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "service.job-select-service",
            query = "FROM JobEntity WHERE service = :service ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "service.job-select-status",
            query = "FROM JobEntity WHERE status = :status ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "service.job-select-service.status",
            query = "FROM JobEntity WHERE service = :service AND status = :status ORDER BY ident desc"
            )
        }
    )
public class JobEntity
extends AbstractEntity
implements AdqlService.Job
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
    public static final String DB_MALLARD_COL = "service" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<AdqlService.Job>
    implements AdqlService.Job.Factory
        {
        @Override
        public Class<?> etype()
            {
            return JobEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlService.Job> select(final AdqlService service)
            {
            return super.iterable(
                super.query(
                    "service.job-select-service"
                    ).setEntity(
                        "service",
                        service
                        )
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlService.Job create(final AdqlService service, final String name, final String adql)
            {
            return super.insert(
                new JobEntity(
                    service,
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
    protected JobEntity(final AdqlService service, final String name, final String adql)
        {
        super(name);
        this.adql = adql ;
        this.service = service ;
        }

    /**
     * Our parent AdqlService.
     *
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlServiceEntity.class
        )
    @JoinColumn(
        name = DB_MALLARD_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private AdqlService service ;
    @Override
    public AdqlService service()
        {
        return this.service ;
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
    private final AdqlService.Job.Status status = AdqlService.Job.Status.EDITING ;
    @Override
    public  AdqlService.Job.Status status()
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

