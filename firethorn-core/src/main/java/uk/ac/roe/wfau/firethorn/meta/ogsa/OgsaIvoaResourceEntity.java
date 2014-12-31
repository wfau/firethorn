/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.ogsa;

import java.net.MalformedURLException;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResourceEntity;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.CreateResourceResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ivoa.IvoaCreateResourceWorkflow;

/**
 *
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = OgsaIvoaResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "OgsaIvoaResource-select-all",
            query = "FROM OgsaIvoaResourceEntity ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "OgsaIvoaResource-select-service",
            query = "FROM OgsaIvoaResourceEntity WHERE service = :service ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "OgsaIvoaResource-select-source",
            query = "FROM OgsaIvoaResourceEntity WHERE source = :source ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "OgsaIvoaResource-select-service-source",
            query = "FROM OgsaIvoaResourceEntity WHERE service = :service AND source = :source ORDER BY name asc, ident desc"
            ),
        }
    )
public class OgsaIvoaResourceEntity
    extends OgsaBaseResourceEntity
    implements OgsaIvoaResource
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "OgsaIvoaResource";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESOURCE_SOURCE_COL = "source";
    
    /**
     * Protected constructor. 
     *
     */
    protected OgsaIvoaResourceEntity()
        {
        super();
        }

   /**
     *
     * Public constructor.
     * @param service The parent {@link OgsaService}
     * @param source  The source {@link IvoaResource}
     *
     */
    public OgsaIvoaResourceEntity(final OgsaService service, final IvoaResource source)
        {
        super(
            service
            );
        this.source = source  ;
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = IvoaResourceEntity.class
        )
    @JoinColumn(
        name = DB_RESOURCE_SOURCE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private IvoaResource source;
    @Override
    public IvoaResource source()
        {
        return this.source;
        }

    @Override
    public String link()
        {
        return factories().ogsa().factories().ivoa().links().link(
            this
            );
        }

    @Override
    public Status create()
        {
        //
        // If we already have an ODSA-DAI resource ID.
        if (ogsaid() != null)
            {
            return status() ;
            }
        //
        // If we don't have an ODSA-DAI resource ID.
        else {
            IvoaCreateResourceWorkflow workflow = null;
            try {
                workflow = new IvoaCreateResourceWorkflow(
                    service().endpoint()
                    );
                }
            catch (MalformedURLException ouch)
                {
                return status(
                    Status.ERROR
                    );
                }

            final CreateResourceResult response = workflow.execute(
                new IvoaCreateResourceWorkflow.Param()
                    {
                    @Override
                    public String endpoint()
                        {
                        // Just use the first endpoint.
                        return source().endpoints().select().iterator().next().endpoint();
                        }

                    @Override
                    public Boolean quickstart()
                        {
                        return Boolean.FALSE;
                        }

                    @Override
                    public Integer interval()
                        {
                        return new Integer(10);
                        }

                    @Override
                    public Integer timeout()
                        {
                        return new Integer(300);
                        }
                    }
                );

            log.debug("Status  [{}]", response.status());
            log.debug("Created [{}]", response.resource());
    
            if (response.status() == WorkflowResult.Status.COMPLETED)
                {
                ogsaid(
                    response.resource().toString()
                    );
                return status(
                    Status.ACTIVE
                    );
                }
    
            else {
                return status(
                    Status.ERROR
                    );
                }
            }
        }

    @Override
    public Status release()
        {
        throw new UnsupportedOperationException(
            "Release not implemented yet"
            );
        }
    }
