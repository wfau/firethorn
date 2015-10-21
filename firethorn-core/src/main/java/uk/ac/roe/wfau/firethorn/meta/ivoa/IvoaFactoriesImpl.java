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
package uk.ac.roe.wfau.firethorn.meta.ivoa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 *
 */
@Component
public class IvoaFactoriesImpl
    implements IvoaFactories
    {

    @Autowired
    private IvoaResource.EntityServices resources;
    @Override
    public IvoaResource.EntityServices resources()
        {
        return this.resources;
        }

    @Autowired
    private IvoaSchema.EntityServices schemas;
    @Override
    public IvoaSchema.EntityServices schemas()
        {
        return this.schemas;
        }

    @Autowired
    private IvoaTable.EntityServices tables;
    @Override
    public IvoaTable.EntityServices tables()
        {
        return this.tables;
        }

    @Autowired
    private IvoaColumn.EntityServices columns;
    @Override
    public IvoaColumn.EntityServices columns()
        {
        return this.columns;
        }
    }
