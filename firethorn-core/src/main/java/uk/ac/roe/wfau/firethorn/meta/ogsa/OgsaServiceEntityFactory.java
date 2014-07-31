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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity.IdentFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity.LinkFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;

/**
 * {@link OgsaService.EntityFactory} implementation.
 *
 */
@Repository
public class OgsaServiceEntityFactory
extends AbstractEntityFactory<OgsaService>
implements OgsaService.EntityFactory
    {
    @Override
    public Class<?> etype()
        {
        return OgsaServiceEntity.class ;
        }

    @Autowired
    private IdentFactory<OgsaService> idents;
    @Override
    public IdentFactory<OgsaService> idents()
        {
        return idents;
        }

    @Autowired
    private LinkFactory<OgsaService> links;
    @Override
    public LinkFactory<OgsaService> links()
        {
        return links;
        }


    @Override
    @SelectMethod
    public Iterable<OgsaService> select()
        {
        return super.iterable(
            super.query(
                "OgsaService-select-all"
                )
            );
        }

    @Override
    public OgsaService create(final String endpoint)
        {
        return super.insert(
            new OgsaServiceEntity(
                endpoint
                )
            );
        }


    @Override
    public OgsaService create(final String name, final String endpoint)
        {
        return super.insert(
            new OgsaServiceEntity(
                name,
                endpoint
                )
            );
        }
    }
