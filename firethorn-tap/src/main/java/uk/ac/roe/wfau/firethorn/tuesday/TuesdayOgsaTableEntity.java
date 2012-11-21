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
import javax.persistence.Transient;

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
    protected static final String DB_ALIAS_COL = "alias";

    protected static final String ALIAS_IDENT = "{ident}";
    protected static final String ALIAS_TEMPLATE = "ogsa_table_" + ALIAS_IDENT;

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

    protected String init()
        {
        return ALIAS_TEMPLATE.replace(
            ALIAS_IDENT,
            "xxxx"
            );
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_ALIAS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String alias;
    @Transient
    private String temp;
    //@Override
    public String alias()
        {
        if (this.temp == null)
            {
            if (this.alias == null)
                {
                this.temp = this.init();
                }
            else {
                this.temp = this.alias;
                }
            }
        return this.temp;
        }
    @Override
    public void alias(String alias)
        {
        this.alias= alias;
        }
    }
