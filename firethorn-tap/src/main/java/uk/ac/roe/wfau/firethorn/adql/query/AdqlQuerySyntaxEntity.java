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
package uk.ac.roe.wfau.firethorn.adql.query;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 *
 */
@Embeddable
@Access(
    AccessType.FIELD
    )
public class AdqlQuerySyntaxEntity
implements AdqlQuerySyntax
    {
    /**
     * Hibernate column mapping.
     * 
     */
    protected static final String DB_CODE_COL = "syntaxcode";
    protected static final String DB_TEXT_COL = "syntaxtext";

    /**
     * Protected constructor, used by Hibernate.
     *
     */
    protected AdqlQuerySyntaxEntity()
        {
        this(
            Status.UNKNOWN
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlQuerySyntaxEntity(Status status)
        {
        this(
            status,
            null
            );
        }

    /**
     * Protected constructor.
     *
     */
    protected AdqlQuerySyntaxEntity(Status status, String message)
        {
        this.status = status  ;
        this.error  = message ;
        }
    
    @Column(
        name = DB_CODE_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Status status = Status.UNKNOWN ;
    @Override
    public Status status()
        {
        return this.status ;
        }

    @Column(
        name = DB_TEXT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String error;
    @Override
    public String error()
        {
        return this.error;
        }

    @Override
    public String friendly()
        {
        return this.error;
        }
    }
