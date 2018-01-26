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
package uk.ac.roe.wfau.firethorn.webapp.control;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.webapp.auth.SimpleHeaderAuthenticator;

/**
 * Abstract base class for Spring MVC controllers.
 *
 */
@Slf4j
@Controller
public abstract class AbstractEntityController<EntityType extends Entity, BeanType extends EntityBean<EntityType>>
extends AbstractController
    {

    /**
     * Protected constructor.
     *
     */
    protected AbstractEntityController()
        {
        super();
        }

    /**
     * Wrap an entity as a bean.
     *
     */
    public abstract BeanType bean(final EntityType entity);

    /**
     * Wrap a set of entities as beans.
     *
     */
    public abstract Iterable<BeanType> bean(final Iterable<EntityType> iter);

    /**
     * Generate a 'created' HTTP response.
     *
     */
    public ResponseEntity<BeanType> created(final EntityType entity)
        {
        final BeanType bean = bean(entity);
        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
            bean.getSelf()
            );
        annotate(
            headers
            );
        return new ResponseEntity<BeanType>(
            bean,
            headers,
            HttpStatus.CREATED
            );
        }

    /**
     * Generate a 'OK' HTTP response.
     *
     */
    public ResponseEntity<BeanType> selected(final EntityType entity)
        {
        final BeanType bean = bean(entity);
        final HttpHeaders headers = new HttpHeaders();
        annotate(
            headers
            );
        return new ResponseEntity<BeanType>(
            bean,
            headers,
            HttpStatus.OK
            );
        }

    /**
     * Generate a 'OK' HTTP response.
     *
     */
    public ResponseEntity<Iterable<BeanType>> selected(final Iterable<EntityType> iterable)
        {
        final Iterable<BeanType> bean = bean(iterable);
        final HttpHeaders headers = new HttpHeaders();
        annotate(
            headers
            );
        return new ResponseEntity<Iterable<BeanType>>(
            bean,
            headers,
            HttpStatus.OK
            );
        }
    
    /**
     * Annotate response headers with the primary {@link Identity}.
     * 
     */
    protected void annotate(final HttpHeaders headers)
        {
        final Operation operation = factories().operations().entities().current();
        if (operation != null)
            {
            log.debug("Operation [{}]", operation.ident());
            final Authentication authentication = operation.authentications().primary();
            if (authentication != null)
                {
                log.debug("Authentication [{}]", authentication);
                final Identity identity = authentication.identity();
                if (identity != null)
                    {
                    log.debug("Identity  [{}][{}]", identity.ident(), identity.name());
                    headers.add(SimpleHeaderAuthenticator.USERNAME_ATTRIB, identity.name());

                    final Community community = identity.community();
                    log.debug("Community [{}][{}]", community.ident(), community.name());

                    if (community != null)
                        {
                        headers.add(SimpleHeaderAuthenticator.COMMUNITY_ATTRIB, community.name());
                        }
                    }
                }
            }
        }
    }

