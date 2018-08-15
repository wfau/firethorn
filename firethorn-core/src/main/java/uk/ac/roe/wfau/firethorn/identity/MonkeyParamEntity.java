/*
 *  Copyright (C) 2018 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.identity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;

import org.hibernate.annotations.Parent;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResourceEntity;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.chaos.MonkeyParam;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.chaos.MonkeyParamBase;

/**
 * 
 * 
 */
@Slf4j
@Embeddable
@Access(
    AccessType.FIELD
    )
public class MonkeyParamEntity
extends MonkeyParamBase
implements MonkeyParam
    {
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_MONKEY_NAME_COL = "chaosmonkeyname";
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_MONKEY_DATA_COL = "chaosmonkeydata";

    /**
     * Public constructor.
     * 
     */
    public MonkeyParamEntity(final OperationEntity operation, final String name, final Object data)
        {
        super(
            name,
            data
            );
        this.operation = operation;
        }

    /**
     * Public constructor.
     * 
     */
    public MonkeyParamEntity(final OperationEntity operation, final MonkeyParam param)
        {
        super(
            param
            );
        this.operation = operation;
        }
    
    /**
     * Protected constructor.
     * 
     */
    protected MonkeyParamEntity()
        {
        super();
        }
    
    @Parent
    protected OperationEntity operation ;
    protected OperationEntity operation()
        {
        return this.operation;
        }

    /**
     * Get/Set methods for Hibernate.
     * 
     */
    protected OperationEntity getOperation()
        {
        return this.operation;
        }
    protected void setOperation(final OperationEntity operation)
        {
        this.operation = operation;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_MONKEY_NAME_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String name ;
    @Override
    public String name()
        {
        return this.name;
        }
    @Override
    public void name(final Object name)
        {
        if (null != name)
            {
            this.name = name.toString();
            }
        else {
            this.name = null ;
            }
        }
    
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_MONKEY_DATA_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String data ;
    @Override
    public Object data()
        {
        return this.data;
        }
    @Override
    public void data(final Object data)
        {
        if (null != data)
            {
            this.data = data.toString() ;
            }
        else {
            this.data = null ;
            }
        }
    }
