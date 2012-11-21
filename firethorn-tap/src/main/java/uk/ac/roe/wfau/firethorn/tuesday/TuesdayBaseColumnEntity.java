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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;

/**
 *
 *
 */
@MappedSuperclass
@Access(
    AccessType.FIELD
    )
public abstract class TuesdayBaseColumnEntity
extends TuesdayBaseNameEntity
    implements TuesdayBaseColumn
    {
    protected static final String DB_TYPE_COL = "type";
    protected static final String DB_SIZE_COL = "size";
    protected static final String DB_UCD_COL  = "ucd";

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String type ;
    @Override
    public String type()
        {
        return this.type;
        }
    @Override
    public void type(String type)
        {
        this.type = type;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer size ;
    @Override
    public Integer size()
        {
        return this.size;
        }

    @Override
    public void size(Integer size)
        {
        this.size = size;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_UCD_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String ucd;
    @Override
    public String ucd()
        {
        return this.ucd;
        }

    @Override
    public void ucd(String ucd)
        {
        this.ucd = ucd;
        }
    
    @Override
    public abstract TuesdayAdqlColumn adql();

    @Override
    public abstract TuesdayOgsaColumn ogsa();

    @Override
    public abstract TuesdayBaseTable table();

    @Override
    public abstract TuesdayBaseSchema schema();

    @Override
    public abstract TuesdayBaseResource resource();

    }
