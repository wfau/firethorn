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
package uk.ac.roe.wfau.firethorn.widgeon.ogsa;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.entity.DateNameFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaIvoaResource;

/**
 * {@link Entity.NameFactory} for {@link OgsaIvoaResource}s.
 *
 */
@Component
public class OgsaIvoaResourceNameFactory
extends DateNameFactory<OgsaIvoaResource>
implements OgsaIvoaResource.NameFactory
     {
     /**
      *  Public constructor.
      *  
      */
    public OgsaIvoaResourceNameFactory()
        {
        super();
        }
    
    @Override
    public String name()
        {
        return datename(
            "OGSA_IVOA"
            );
        }
    }
