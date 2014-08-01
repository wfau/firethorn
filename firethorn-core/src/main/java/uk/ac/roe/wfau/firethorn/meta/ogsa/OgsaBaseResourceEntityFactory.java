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

import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity.IdentFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity.LinkFactory;

/**
 * {@link OgsaBaseResource.EntityFactory} implementation.
 *
 */
@Repository
public class OgsaBaseResourceEntityFactory
extends AbstractEntityFactory<OgsaBaseResource>
implements OgsaBaseResource.EntityFactory
    {

    @Override
    public Class<?> etype()
        {
        return OgsaBaseResourceEntity.class;
        }

    @Override
    public Iterable<OgsaBaseResource> select()
        {
        return super.iterable(
            super.query(
                "OgsaBaseResource-select-all"
                )
            );
        }
    
    @Override
    public Iterable<OgsaBaseResource> select(final OgsaService service)
        {
        return super.iterable(
            super.query(
                "OgsaBaseResource-select-service"
                )
            );
        }

    @Override
    public IdentFactory<OgsaBaseResource> idents()
        {
        // TODO Auto-generated method stub
        return null;
        }

    @Override
    public LinkFactory<OgsaBaseResource> links()
        {
        // TODO Auto-generated method stub
        return null;
        }
    }
