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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractComponent;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>AdqlQuery</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlQueryLinkFactory.QUERY_PATH)
public class AdqlQueryController
extends AbstractEntityController<AdqlQuery, AdqlQueryBean>
    {

    @Override
    public Path path()
        {
        return path(
            AdqlQueryLinkFactory.QUERY_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlQueryController()
        {
        super();
        }

    /**
     * MVC property for the target entity.
     *
     */
    public static final String TARGET_ENTITY = "urn:adql.query.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String UPDATE_NAME = "adql.query.update.name" ;

    /**
     * MVC property for updating the query.
     *
     */
    public static final String UPDATE_QUERY = "adql.query.update.query" ;

    /**
     * MVC property for updating the status.
     *
     */
    public static final String UPDATE_STATUS = "adql.query.update.status" ;

    /**
     * MVC property for the timelimit.
     *
     */
    public static final String UPDATE_TIMEOUT = "adql.query.update.timeout" ;

    @Override
    public AdqlQueryBean bean(final AdqlQuery entity)
        {
        return new AdqlQueryBean(
            entity
            );
        }

    @Override
    public Iterable<AdqlQueryBean> bean(final Iterable<AdqlQuery> iter)
        {
        return new AdqlQueryBean.Iter(
            iter
            );
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_CONTENT)
    public AdqlQueryBean select(
        @PathVariable("ident")
        final String ident
        ) throws IdentifierNotFoundException {
        return bean(
            factories().adql().queries().select(
                factories().adql().queries().idents().ident(
                    ident
                    )
                )
            );
        }

    /**
     * JSON POST update.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.POST, produces=JSON_CONTENT)
    public AdqlQueryBean update(
        @RequestParam(value=UPDATE_NAME, required=false)
        final String name,
        @RequestParam(value=UPDATE_QUERY, required=false)
        final String input,
        @RequestParam(value=UPDATE_STATUS, required=false)
        final Status status,
        @RequestParam(value=UPDATE_TIMEOUT, required=false)
        final Integer timeout,
        @PathVariable("ident")
        final String ident
        ) throws IdentifierNotFoundException {
log.info("TIMESTAMP starting query update");
log.debug("  name   [{}]", name);
log.debug("  input  [{}]", input);
log.debug("  status [{}]", status);

        final AdqlQuery query = factories().queries().resolver().select(
            factories().queries().idents().ident(
                ident
                )
            );

        if ((name != null) || (input != null))
            {
            this.helper.update(
                query,
                name,
                input
                );
            }

        if (status != null)
            {
            factories().queries().executor().update(
                query.ident(),
                status,
                timeout
                );
            }
log.info("TIMESTAMP finishing query update");

        return bean(
            query
            ) ;
        }

    /**
     * Transactional helper.
     *
     */
    public static interface Helper
        {

        /**
         * Update the properties.
         *
         */
        public void update(final AdqlQuery query, final String  name, final String input);

        }

    /**
     * Transactional helper.
     *
     */
    @Autowired
    private Helper helper ;

    /**
     * Transactional helper.
     *
     */
    @Slf4j
    @Component
    public static class HelperImpl
    extends AbstractComponent
    implements Helper
        {
        @Override
        @UpdateAtomicMethod
        public void update(
            final AdqlQuery query,
            final String  name,
            final String input
            ) {
            log.debug("---- ---- ---- ----");
            log.debug("update(AdqlQuery, String, String)");
            log.debug("  Name  [{}]", name);
            log.debug("  Input [{}]", input);
            if (name != null)
                {
                log.debug("Setting name");
                if (name.length() > 0)
                    {
                    query.name(
                        name
                        );
                    }
                }
            if (input != null)
                {
                log.debug("Setting input");
                query.input(
                    input
                    );
                }
            log.debug("---- ----");
            }
        }
    }
