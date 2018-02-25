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
import java.net.URISyntaxException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.Entity;

/**
 *
 *
 */
@Slf4j
public class AbstractEntityBeanImpl<EntityType extends Entity>
implements EntityBean<EntityType>
    {
    /**
     * Our date time formatter.
     * @todo Move this much further back.
     *
     */
    private static final DateTimeFormatter dateformat = ISODateTimeFormat.dateHourMinuteSecondFraction().withZoneUTC()  ;
    protected DateTimeFormatter dateformat()
        {
        return dateformat ;
        }
    protected String dateformat(final DateTime datetime)
        {
        return dateformat.print(datetime) ;
        }


    private final EntityType entity ;
    private final URI type ;

    /**
     *
     *
     */
    public AbstractEntityBeanImpl(final URI type, final EntityType entity)
        {
        log.debug("AbstractEntityBeanImpl [{}][{}]", type, entity.ident());
        this.type = type ;
        this.entity = entity ;
        }

    @Override
    public EntityType entity()
        {
        return this.entity;
        }

    @Override
    public String getIdent()
        {
        return this.entity.ident().toString();
        }
    
    @Override
    public URI getUrl()
        {
        try {
            return new URI(
                this.entity.link()
                );
            }
        catch (final URISyntaxException ouch)
            {
            throw new RuntimeException(
                ouch
                );
            }
        }

    @Override
    public URI getSelf()
        {
        return this.getUrl();
        }
    
    @Override
    public URI getType()
        {
        return this.type;
        }

    @Override
    public URI getOwner()
        {
        if (this.entity.owner() != null)
            {
            try {
                return new URI(
                    this.entity.owner().link()
                    );
                }
            catch (final URISyntaxException ouch)
                {
                throw new RuntimeException(
                    ouch
                    );
                }
            }
        else {
            return null ;
            }
        }

    @Override
    public String getCreated()
        {
        return dateformat(
            this.entity.created()
            );
        }

    @Override
    public String getModified()
        {
        return dateformat(
            this.entity.modified()
            );
        }
    }
