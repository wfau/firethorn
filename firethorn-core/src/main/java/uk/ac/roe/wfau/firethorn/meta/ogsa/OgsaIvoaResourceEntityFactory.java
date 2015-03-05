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
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;

/**
 * {@link OgsaJdbcResource.EntityFactory} implementation.
 *
 */
@Repository
public class OgsaIvoaResourceEntityFactory
extends AbstractEntityFactory<OgsaIvoaResource>
implements OgsaIvoaResource.EntityFactory
    {

    @Override
    public Class<?> etype()
        {
        return OgsaIvoaResourceEntity.class;
        }

    @Autowired
    private OgsaIvoaResource.IdentFactory idents;
    @Override
    public OgsaIvoaResource.IdentFactory idents()
        {
        return this.idents;
        }

    private OgsaIvoaResource.LinkFactory links;
    @Override
    public OgsaIvoaResource.LinkFactory links()
        {
        return this.links;
        }
    
    @Override
    @SelectMethod
    public Iterable<OgsaIvoaResource> select()
        {
        return super.iterable(
            super.query(
                "OgsaIvoaResource-select-all"
                )
            );
        }
    
    @Override
    @SelectMethod
    public Iterable<OgsaIvoaResource> select(final OgsaService service)
        {
        return super.iterable(
            super.query(
                "OgsaIvoaResource-select-service"
                ).setEntity(
                    "service",
                    service
                    )
            );
        }

    @Override
    public Iterable<OgsaIvoaResource> select(IvoaResource source)
        {
        return super.iterable(
            super.query(
                "OgsaIvoaResource-select-source"
                ).setEntity(
                    "source",
                    source
                )
            );
        }

    @Override
    @SelectMethod
    public Iterable<OgsaIvoaResource> select(final OgsaService service, final IvoaResource source)
        {
        return super.iterable(
            super.query(
                "OgsaIvoaResource-select-service-source"
                ).setEntity(
                    "service",
                    service
                ).setEntity(
                    "source",
                    source
                )
            );
        }

    @Override
    @CreateMethod
    public OgsaIvoaResource create(final OgsaService service, final IvoaResource source)
        {
        return super.insert(
            new OgsaIvoaResourceEntity(
                service,
                source
                )
            );
        }

    @Override
    @CreateMethod
    public OgsaIvoaResource primary(IvoaResource source)
        {
        return this.primary(
            factories().ogsa().services().primary(),
            source
            );
        }

    @Override
    @CreateMethod
    public OgsaIvoaResource primary(OgsaService service, IvoaResource source)
        {
        // Really really simple - just get the first. 
        OgsaIvoaResource found = super.first(
            super.query(
                "OgsaIvoaResource-select-service-source"
                ).setEntity(
                    "service",
                    service
                ).setEntity(
                    "source",
                    source
                )
            );
        // If we don't have one, create one.
        if (found == null)
            {
            found = create(
                service,
                source
                );
            }
        return found ;
        }
    }
