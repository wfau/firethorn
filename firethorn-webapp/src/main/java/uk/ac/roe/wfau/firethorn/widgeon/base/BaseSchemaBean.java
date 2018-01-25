/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.widgeon.base;

import java.net.URI;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;

/**
 *
 *
 */
public class BaseSchemaBean<SchemaType extends BaseSchema<?,?>>
extends NamedEntityBeanImpl<SchemaType>
    {
    public BaseSchemaBean(final URI type, final SchemaType schema)
        {
        super(
            type,
            schema
            );
        }

    @Deprecated
    public String getParent()
    throws ProtectionException
        {
        return entity().resource().link();
        }

    public String getResource()
    throws ProtectionException
        {
        return entity().resource().link();
        }

    public String getBase()
    throws ProtectionException
        {
        return entity().base().link();
        }

    public String getRoot()
    throws ProtectionException
        {
        return entity().root().link();
        }

    public String getFullname()
    throws ProtectionException
        {
        return entity().fullname();
        }

    public String getDepth()
    throws ProtectionException
        {
        return entity().depth().toString();
        }

    public String getTables()
        {
        // TODO This should be in a model class. 
        return entity().link().concat("/tables/select");
        }
    }
