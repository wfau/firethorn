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

import java.net.URI;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.UriBuilder;
import uk.ac.roe.wfau.firethorn.webapp.widgeon.JdbcResourcesController;

/**
 * EntityBean implementation.
 *
 */
@Slf4j
public class AbstractEntityBean<EntityType extends Entity>
implements EntityBean<EntityType>
    {
    /**
     * Our date time formatter.
     * @todo Move this much further back.
     * 
     */
    private DateTimeFormatter formatter = ISODateTimeFormat.dateHourMinuteSecondFraction().withZoneUTC()  ; 

    private UriBuilder builder ;
    private EntityType entity ;
    private URI type ;

    /**
     * Public constructor.
     * @param builder
     * @param entity
     *
     */
    public AbstractEntityBean(URI type , UriBuilder builder, EntityType entity)
        {
        log.debug("AbstractEntityBean [{}][{}]", type, entity.ident().value());
        this.type = type ;
        this.entity = entity ;
        this.builder = builder ; 
        }

    @Override
    public EntityType entity()
        {
        return this.entity;
        }

    @Override
    public String id()
        {
        return entity.ident().toString();
        }

    @Override
    public URI uri()
        {
        return builder.uri(
            entity
            );
        }

    @Override
    public URI getIdent()
        {
        return builder.uri(
            entity
            );
        }

    public URI getType()
        {
        return this.type;
        }

    public String getName()
        {
        return entity.name();
        }

    public String getCreated()
        {
        return formatter.print(
            entity.created()
            );
        }
    
    public String getModified()
        {
        return formatter.print(
            entity.modified()
            );
        }
    }
