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
package uk.ac.roe.wfau.firethorn.entity ;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Type;

import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;

/**
 * Generic base class for a persistent Entity.
 *
 * Problems with AccessType.FIELD means we still have to have get/set methods on fields we want to modify.
 * If we don't include get/set methods, then Hibernate doesn't commit changes to the database.
 *   https://forum.hibernate.org/viewtopic.php?f=1&t=1012254
 *   https://hibernate.onjira.com/browse/HHH-6581
 *   http://javaprogrammingtips4u.blogspot.co.uk/2010/04/field-versus-property-access-in.html
 *
 */
@Slf4j
@MappedSuperclass
@Access(
    AccessType.FIELD
    )
public abstract class AbstractNamedEntity
extends AbstractEntity
implements Entity, NamedEntity
    {

    /**
     * Hibernate column mapping.
     *
     */
    public static final String DB_NAME_COL    = "name";
    public static final String DB_TEXT_COL    = "text";

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected AbstractNamedEntity()
        {
        super();
        }

    /**
     * Protected constructor, sets the owner and create date.
     * @param init A flag to distinguish this from the default constructor.
     *
    @Deprecated
    protected AbstractNamedEntity(final boolean init)
    throws NameFormatException
        {
        super(init);
        }
     */

    /**
     * Protected constructor, sets the owner, name and create date.
     *
     */
    protected AbstractNamedEntity(final String name)
    throws NameFormatException
        {
    	super(true);
    	//log.debug("AbstractNamedEntity(String)");
        //log.debug("  Name  [{}]", name);
    	this.name(
            name
            );
        }

    /**
     * The Entity name.
     *
     */
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_NAME_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String name ;

    @Override
    public String name()
        {
        return this.name ;
        }

    @Override
    public void name(final String name)
        {
        if (name != null)
            {
            final String temp = name.trim();
            if (temp.length() > 0)
                {
                this.name = temp;
                }
            }
        }

    /**
     * The Entity description.
     *
     */
    @Type(
        type="org.hibernate.type.TextType"
        )
    @Basic(
        fetch = FetchType.LAZY
        )
    @Column(
        name = DB_TEXT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String text ;
    @Override
    public String text()
        {
        return this.text;
        }
    @Override
    public void text(final String value)
        {
        this.text = emptystr(
            value
            );
        }
    }

