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
public abstract class TuesdayOgsaTableEntity<ColumnType extends TuesdayOgsaColumn>
extends TuesdayBaseTableEntity
implements TuesdayOgsaTable<ColumnType>
    {
    protected static final String DB_ADQL_COL  = "adql";
    protected static final String DB_ALIAS_COL = "alias";

    protected TuesdayOgsaTableEntity()
        {
        super();
        }

    protected TuesdayOgsaTableEntity(String name)
        {
        super(name);
        }

    @Override
    public TuesdayOgsaTable<ColumnType> ogsa()
        {
        return this ;
        }

    public TuesdayBaseTable base()
        {
        return this ;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_ALIAS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String alias;
    @Override
    public String alias()
        {
        return this.alias;
        }
    @Override
    public void alias(String alias)
        {
        this.alias= alias;
        }
    }
