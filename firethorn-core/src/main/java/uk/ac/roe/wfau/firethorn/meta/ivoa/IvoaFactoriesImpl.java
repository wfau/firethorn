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
    private IvoaResource.EntityFactory resources;
    @Override
    public IvoaResource.EntityFactory resources()
        {
        return this.resources;
        }

    @Autowired
    private IvoaSchema.EntityFactory schemas;
    @Override
    public IvoaSchema.EntityFactory schemas()
        {
        return this.schemas;
        }

    @Autowired
    private IvoaTable.EntityFactory tables;
    @Override
    public IvoaTable.EntityFactory tables()
        {
        return this.tables;
        }

    @Autowired
    private IvoaColumn.EntityFactory columns;
    @Override
    public IvoaColumn.EntityFactory columns()
        {
        return this.columns;
        }
    }
