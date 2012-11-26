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
package uk.ac.roe.wfau.firethorn.tuesday;

/**
 * Our IVOA component factories
 *
 */
public interface TuesdayIvoaFactory
    {
    /**
     * Our resource factory.
     *
     */
    public TuesdayIvoaResource.Factory resources();

    /**
     * Our schema factory.
     *
     */
    public TuesdayIvoaSchema.Factory schemas();

    /**
     * Our table factory.
     *
     */
    public TuesdayIvoaTable.Factory tables();

    /**
     * Our column factory.
     *
     */
    public TuesdayIvoaColumn.Factory  columns();

    }
