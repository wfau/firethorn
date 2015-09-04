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

/**
 * Our IVOA component factories
 *
 */
public interface IvoaFactories
    {
    /**
     * Our {@link IvoaResource.EntityServices} instance.
     *
     */
    public IvoaResource.EntityServices resources();

    /**
     * Our {@link IvoaSchema.EntityServices} instance.
     *
     */
    public IvoaSchema.EntityServices schemas();

    /**
     * Our {@link IvoaTable.EntityServices} instance.
     *
     */
    public IvoaTable.EntityServices tables();

    /**
     * Our {@link IvoaColumn.EntityServices} instance.
     *
     */
    public IvoaColumn.EntityServices columns();

    }
