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
package uk.ac.roe.wfau.firethorn.meta.base;

/**
 *
 *
 */
public interface BaseValue<ValueType>
    {
    /**
     * Get the current value.
     *
     */
    public ValueType value();

    /**
     * Get the user value.
     *
     */
    public ValueType user();

    /**
     * Set the user value.
     *
     */
    public void user(final ValueType value);

    /**
     * Get the base value.
     *
     */
    public ValueType base();

    /**
     * Set the base value.
     *
     */
    public void base(final ValueType value);

    }
