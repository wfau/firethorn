/*
 *  Copyright (C) 2016 Royal Observatory, University of Edinburgh, UK
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

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InternalServerErrorException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InvalidRequestException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.blue.BlueQueryBean;
import uk.ac.roe.wfau.firethorn.webapp.blue.BlueQueryModel;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlSchemaLinkFactory;

/**
 * Spring MVC controller to handle {@link BlueQuery} for an {@link AdqlSchema}.
 * <br/>Controller path : [{@value AdqlSchemaLinkFactory.SCHEMA_BLUE_PATH}]
 *
 */
@Slf4j
@Controller
@Deprecated
@RequestMapping(AdqlSchemaLinkFactory.SCHEMA_BLUE_PATH)
public class AdqlSchemaBlueController
extends AbstractEntityController<BlueQuery, BlueQueryBean>
    {
    @Override
    public Path path()
        {
        return path(
            AdqlSchemaLinkFactory.SCHEMA_BLUE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlSchemaBlueController()
        {
        super();
        }

    @Override
    public BlueQueryBean bean(final BlueQuery entity)
        {
        return new BlueQueryBean(
            entity
            );
        }

    @Override
    public Iterable<BlueQueryBean> bean(final Iterable<BlueQuery> iter)
        {
        return new BlueQueryBean.Iter(
            iter
            );
        }

    /**
     * Get the target {@link AdqlSchema} based on the {@Identifier} in the request path.
     * @param ident The {@link AdqlSchema} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The parent {@link AdqlSchema}.
     * @throws IdentifierNotFoundException If the {@link AdqlSchema} could not be found.
     *
     */
    @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
    public AdqlSchema parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws IdentifierNotFoundException {
        log.debug("parent() [{}]", ident);
        return factories().adql().schemas().entities().select(
            factories().adql().schemas().idents().ident(
                ident
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link BlueQuery}.
     * <br/>Request path : [{@value #CREATE_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param schema The target {@link AdqlSchema} selected using the {@Identifier} in the request path.
     * 
     * 
     * @return A {@link BlueQueryBean} wrapping the new {@link BlueQuery}.
     * @throws InternalServerErrorException 
     * @throws InvalidRequestException 
     * @deprecated replaced by AdqlResource.blues().create(...) 
     *  
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<BlueQueryBean> create(
        @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
        final AdqlSchema schema,

        @RequestParam(value=BlueQueryModel.QUERY_INPUT_PARAM, required=false)
        final String input,
        @RequestParam(value=BlueQueryModel.NEXT_STATUS_PARAM, required=false)
        final TaskState next,
        @RequestParam(value=BlueQueryModel.REQUEST_WAIT_PARAM, required=false)
        final Long wait,

        @RequestParam(value=BlueQueryModel.QUERY_LIMT_CELLS, required=false)
        final Long cells,
        @RequestParam(value=BlueQueryModel.QUERY_LIMT_ROWS, required=false)
        final Long rows,
        @RequestParam(value=BlueQueryModel.QUERY_LIMT_TIME, required=false)
        final Long time,

        @RequestParam(value=BlueQueryModel.QUERY_DELAY_FIRST, required=false)
        final Integer first,
        @RequestParam(value=BlueQueryModel.QUERY_DELAY_EVERY, required=false)
        final Integer every,
        @RequestParam(value=BlueQueryModel.QUERY_DELAY_LAST, required=false)
        final Integer last,
        
        @RequestParam(value=BlueQueryModel.QUERY_MODE, required=false)
        final AdqlQueryBase.Mode mode,
        @RequestParam(value=BlueQueryModel.QUERY_SYNTAX, required=false)
        final AdqlQueryBase.Syntax.Level syntax
        ) throws
        InvalidRequestException,
        InternalServerErrorException 
        {
        log.warn("Call to deprecated schema/queries method");
        return created(
            schema.resource().blues().create(
                input,
                factories().blues().limits().create(
                    rows,
                    cells,
                    time
                    ),
                factories().blues().delays().create(
                    first,
                    every,
                    last
                    ),
                next,
                wait,
                mode,
                syntax
                )
            );
        }
    }
