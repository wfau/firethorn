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

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity.IdentFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity.LinkFactory;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 *
 *
 */
public class OgsaJdbcResourceEntityFactory
    extends AbstractEntityFactory<OgsaJdbcResource>
    implements OgsaJdbcResource.EntityFactory
    {

    @Override
    public Class<?> etype()
        {
        return OgsaJdbcResourceEntity.class;
        }

    @Autowired
    private IdentFactory<OgsaJdbcResource> idents;
    @Override
    public IdentFactory<OgsaJdbcResource> idents()
        {
        return this.idents;
        }

    private LinkFactory<OgsaJdbcResource> links;
    @Override
    public LinkFactory<OgsaJdbcResource> links()
        {
        return this.links;
        }
    
    @Override
    public Iterable<OgsaJdbcResource> select()
        {
        return super.iterable(
            super.query(
                "OgsaJdbcResource-select-all"
                )
            );
        }
    
    @Override
    public Iterable<OgsaJdbcResource> select(final OgsaService service)
        {
        return super.iterable(
            super.query(
                "OgsaJdbcResource-select-service"
                )
            );
        }

    @Override
    public Iterable<OgsaJdbcResource> select(final OgsaService service, final JdbcResource source)
        {
        return super.iterable(
            super.query(
                "OgsaJdbcResource-select-service-source"
                )
            );
        }

    @Override
    public OgsaJdbcResource create(final OgsaService service, final JdbcResource source)
        {
        return super.insert(
            new OgsaJdbcResourceEntity(
                service,
                source
                )
            );
        }
    @Override
    public OgsaJdbcResource create(final OgsaService service, final JdbcResource source, final String name)
        {
        return super.insert(
            new OgsaJdbcResourceEntity(
                service,
                source,
                name
                )
            );
        }
    }
