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
 *
 *
 */
public abstract class TuesdayBaseTableEntity
    implements TuesdayBaseTable
    {
    @Override
    public String name()
        {
        return "name";
        }
    @Override
    public void name(String name)
        {
        }
    @Override
    public String text()
        {
        return "text";
        }
    @Override
    public void text(String text)
        {
        }
    @Override
    public String type()
        {
        return "type";
        }
    @Override
    public void type(String type)
        {
        }
    @Override
    public Integer size()
        {
        return new Integer(21);
        }
    @Override
    public void size(Integer size)
        {
        }
    @Override
    public String ucd()
        {
        return "ucd";
        }
    @Override
    public void ucd(String ucd)
        {
        }

    @Override
    public abstract TuesdayAdqlTable adql();
    @Override
    public abstract TuesdayOgsaTable<?> ogsa();
    @Override
    public abstract TuesdayBaseSchema schema();
    @Override
    public abstract TuesdayBaseResource resource();
    }
