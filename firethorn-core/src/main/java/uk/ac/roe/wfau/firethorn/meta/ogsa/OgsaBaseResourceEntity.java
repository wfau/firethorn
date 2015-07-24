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

import java.io.IOException;
import java.net.URISyntaxException;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.meta.base.BaseComponentEntity;

/**
 *
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Inheritance(
    strategy = InheritanceType.TABLE_PER_CLASS
    )
public abstract class OgsaBaseResourceEntity<ResourceType extends OgsaBaseResource>
extends BaseComponentEntity<ResourceType>
implements OgsaBaseResource
    {
    /**
     * {@link OgsaBaseResource.EntityFactory} implementation.
     *
     */
    @Slf4j
    @Repository
    public static abstract class EntityFactory<ComponentType extends OgsaBaseResource>
    extends BaseComponentEntity.EntityFactory<ComponentType>
    implements OgsaBaseResource.EntityFactory<ComponentType>
        {
        @Override
        @Value("${firethorn.ogsa.resource.scan:PT11M}")
        public void scanperiod(final String value)
            {
            log.debug("scanperiod(String)");
            log.debug("  value      [{}]", value);
            super.scanperiod(
                value
                );
            }
        }
    
	/**
	 * Default component name.
	 * 
	 */
	protected static final String DEFAULT_NAME = "OGSA-DAI resource" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_OGSA_RESOURCE_OGSAID_COL = "ogsaid";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_OGSA_RESOURCE_STATUS_COL = "ogstatus";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_OGSA_RESOURCE_SERVICE_COL = "service";

    /**
     * Protected constructor.
     *
     */
    public OgsaBaseResourceEntity()
        {
        super();
        }

    /**
    *
    * Protected constructor.
    * @param service The parent {@link OgsaService}.
    *
    */
   protected OgsaBaseResourceEntity(final OgsaService service)
       {
       super(
           DEFAULT_NAME
           );
       this.ogstatus  = OgStatus.CREATED ;
       this.ogservice = service ;
       }

   @ManyToOne(
       fetch = FetchType.LAZY,
       targetEntity = OgsaServiceEntity.class
       )
   @JoinColumn(
       name = DB_OGSA_RESOURCE_SERVICE_COL,
       unique = false,
       nullable = false,
       updatable = false
       )
   private OgsaService ogservice;
   @Override
   public OgsaService service()
       {
       return this.ogservice;
       }

   @Basic(
       fetch = FetchType.EAGER
       )
   @Column(
       name = DB_OGSA_RESOURCE_OGSAID_COL,
       unique = false,
       nullable = true,
       updatable = true
       )
   protected String ogsaid;
   @Override
   public String ogsaid()
       {
       return this.ogsaid;
       }
   @Override
   public OgStatus ogsaid(final OgStatus status, final String ogsaid)
       {
       log.debug("ogsaid(status, ogsaid) [{}][{}]", status, ogsaid);
       factories().spring().transactor().update(
           new Runnable()
               {
                @Override
                public void run()
                    {
                    OgsaBaseResourceEntity.this.ogstatus = status ;
                    OgsaBaseResourceEntity.this.ogsaid = ogsaid ;
                    }
                }
           );
       return this.ogstatus;
       }

   @Column(
       name = DB_OGSA_RESOURCE_STATUS_COL,
       unique = false,
       nullable = false,
       updatable = true
       )
   @Enumerated(
       EnumType.STRING
       )
   protected OgStatus ogstatus = OgStatus.UNKNOWN ;
   @Override
   public OgStatus ogStatus()
       {
       return this.ogstatus;
       }
   @Override
   public OgStatus ogStatus(final OgStatus status)
       {
       log.debug("status(status) [{}]", status);
       factories().spring().transactor().update(
           new Runnable()
               {
                @Override
                public void run()
                    {
                    OgsaBaseResourceEntity.this.ogstatus = status ;
                    }
                }
           );
       return this.ogstatus;
       }

   protected OgStatus init()
       {
       log.debug("init()");
       return this.ogstatus;
       }

   @Override
   protected void scanimpl()
       {
       log.debug("Scanning OgsaBaseResource [{}][{}]", this.name(), this.ogsaid);

       if (this.ogstatus.active() && (this.ogsaid != null))
           {
           final HttpStatus http = ping(); 
           switch(http)
               {
               case OK :
                   ogStatus(
                       OgStatus.ACTIVE
                       );
                   break ;

               case NOT_FOUND:
                   this.init();
                   break ;

               default :
                   ogStatus(
                       OgStatus.ERROR
                       );
                   break ;
               }
           }
       }

   /**
    *  Our local HTTP request factory.
    *
    */
   private static final ClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
   
   /**
    *  Check our OGSA-DAI resource.
    *
    */
   public HttpStatus ping()
       {
       try {
           ClientHttpRequest request = factory.createRequest(
               service().baseuri().resolve(
                   "dataResources/" + this.ogsaid
                   ),
               HttpMethod.GET
               );

           log.debug("Service request [{}][{}]", this.ident(), request.getURI());
           ClientHttpResponse response = request.execute();

           HttpStatus http = response.getStatusCode();
           log.debug("Service response [{}][{}]", this.ident(), response.getStatusText());

           return http ;

           }
       catch (URISyntaxException ouch)
           {
           log.warn("Problem occured parsing service endpoint [{}][{}]", this.ident(), ouch.getReason());
           return HttpStatus.BAD_REQUEST;
           }
       catch (IOException ouch)
           {
           log.error("Problem occured sending service request [{}][{}]", this.ident(), ouch.getMessage());
           return HttpStatus.BAD_REQUEST;
           }
       }

   /**
    * System start time.
    * 
    */
   protected static final DateTime START_TIME = new DateTime(); 
   
   /**
    * Check to see if we should run a scan.
    * OGSA resources always scan first time after a restart.
    *
    */
   protected boolean scantest()
       {
       log.debug("scantest for [{}][{}]", this.ident(), this.name());

       DateTime prev = scandate(); 
       log.debug("prevscan   [{}]", prev);
       log.debug("start time [{}]", START_TIME );

       if (START_TIME.isAfter(prev))
           {
           log.debug("prev scan is before startup - scanning");
           return true ;
           }
       else {
           return super.scantest();
           }
       }
    }
