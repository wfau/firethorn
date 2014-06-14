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
package uk.ac.roe.wfau.firethorn.widgeon.ivoa;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>IvoaTables</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(IvoaTableLinkFactory.TABLE_PATH)
public class IvoaTableController
    extends AbstractEntityController<IvoaTable, IvoaTableBean>
    {

    @Override
    public Path path()
        {
        return path(
            IvoaTableLinkFactory.TABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public IvoaTableController()
        {
        super();
        }

    /**
     * MVC property for the target entity.
     *
     */
    public static final String TARGET_ENTITY = "urn:ivoa.table.entity" ;

    /**
     * POST param for the table name.
     *
     */
    public static final String TABLE_NAME_PARAM = "urn:ivoa.table.name" ;

    /**
     * POST param for the JDBC status.
     *
     */
    public static final String JDBC_STATUS_PARAM = "urn:ivoa.table.jdbc.status" ;

    /**
     * POST param for the ADQL status.
     *
     */
    public static final String ADQL_STATUS_PARAM = "urn:ivoa.table.adql.status" ;


    @Override
    public Iterable<IvoaTableBean> bean(final Iterable<IvoaTable> iter)
        {
        return new IvoaTableBean.Iter(
            iter
            );
        }

    @Override
    public IvoaTableBean bean(final IvoaTable entity)
        {
        return new IvoaTableBean(
            entity
            );
        }

    /**
     * Get the target table based on the identifier in the request.
     * @throws EntityNotFoundException
     *
     */
    @ModelAttribute(IvoaTableController.TARGET_ENTITY)
    public IvoaTable entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws EntityNotFoundException {
        log.debug("table() [{}]", ident);
        return factories().ivoa().tables().select(
            factories().ivoa().tables().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public IvoaTableBean select(
        @ModelAttribute(TARGET_ENTITY)
        final IvoaTable entity
        ){
        log.debug("select()");
        return bean(
            entity
            );
        }

    /**
     * POST update name request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.POST, params={TABLE_NAME_PARAM}, produces=JSON_MIME)
    public IvoaTableBean update(
        @ModelAttribute(TARGET_ENTITY)
        final IvoaTable entity,
        @RequestParam(value=TABLE_NAME_PARAM, required=true)
        final String name
        ){
        log.debug("update(String)");
        log.debug(" name [{}]", name);
        //
        // Needs a transaction ..
        if (null != name)
            {
            entity.name(
                name
                );
            }
        return bean(
            entity
            );
        }

    /**
     * POST update request.
     *
    @ResponseBody
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MIME)
    public IvoaTableBean update(
        @ModelAttribute(TARGET_ENTITY)
        final IvoaTable entity,
        @RequestParam(value=JDBC_STATUS_PARAM, required=false)
        final IvoaTable.TableStatus jdbcstatus,
        @RequestParam(value=ADQL_STATUS_PARAM, required=false)
        final AdqlTable.TableStatus adqlstatus
        ){
        log.debug("update(IvoaTable.IvoaStatus)");
        log.debug(" jdbcstatus [{}]", jdbcstatus);
        log.debug(" adqlstatus [{}]", adqlstatus);

        factories().spring().transactor().update(
            new Runnable()
                {
                @Override
                public void run()
                    {
                    if (null != jdbcstatus)
                        {
                        entity.meta().jdbc().status(
                            jdbcstatus
                            );
                        }
                    if (null != adqlstatus)
                        {
                        entity.meta().adql().status(
                            adqlstatus
                            );
                        }
                    }
                }
            );

        return bean(
            entity
            );
        }
     */
    }