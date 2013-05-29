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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.IdentityEntity;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactoriesImpl;

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
public abstract class AbstractEntity
implements Entity
    {

    /*
     * Our database mapping values.
     *
     */
    protected static final String DB_GEN_NAME    = "entity-ident" ;
    protected static final String DB_GEN_METHOD  = "identity" ;

    public    static final String DB_NAME_COL    = "name"  ;
    protected static final String DB_TEXT_COL    = "text";
    protected static final String DB_IDENT_COL   = "ident" ;
    protected static final String DB_OWNER_COL   = "owner" ;

    protected static final String DB_CREATED_COL  = "created"  ;
    protected static final String DB_MODIFIED_COL = "modified" ;

    /**
     * Access to our ComponentFactories singleton instance.
     * @todo Replace this with something, anything, else.
     * @todo re-enable compiler warnings for indirect access to static members
     *
     */
    public ComponentFactories factories()
        {
        return ComponentFactoriesImpl.instance();
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected AbstractEntity()
        {
        super();
        }

    /**
     * Protected constructor, sets the name and create date.
     * @todo default owner ?
     *
     */
    protected AbstractEntity(final String name)
    throws NameFormatException
        {
        this(
            //womble().context().identity(),
            null,
            name
            );
        }

    /**
     * Protected constructor, sets the owner, name and create date.
     * @todo Better default for the name.
     *
     */
    protected AbstractEntity(final Identity owner)
    throws NameFormatException
        {
        this(
            owner,
            null
            );
        }

    /**
     * Protected constructor, sets the owner, name and create date.
     * @todo Better default for the name.
     *
     */
    protected AbstractEntity(final Identity owner, final String name)
    throws NameFormatException
        {
        super();
        this.owner = owner;
        this.created = new DateTime();
        this.name(
            name
            );

        /*
         *
         * [UnresolvedEntityInsertActions] HHH000437: Attempting to save one or more entities that have a non-nullable association with an unsaved transient entity. The unsaved transient entity must be saved in an operation prior to saving these dependent entities.
         * Unsaved transient entity: ([uk.ac.roe.wfau.firethorn.identity.IdentityEntity#<null>])
         * Dependent entities: ([[uk.ac.roe.wfau.firethorn.identity.IdentityEntity#<null>]])
         * Non-nullable association(s): ([uk.ac.roe.wfau.firethorn.identity.IdentityEntity.owner])
         * [WombleImpl] Error executing Hibernate query [org.hibernate.TransientPropertyValueException][Not-null property references a transient value - transient instance must be saved before current operation: uk.ac.roe.wfau.firethorn.identity.IdentityEntity.owner -> uk.ac.roe.wfau.firethorn.identity.IdentityEntity]
         *
        if (this.owner == null)
            {
            if (this instanceof Identity)
                {
                this.owner = (Identity) this ;
                }
            }
         *
         */

        }

    /**
     * The Entity Identifier.
     * Note - unique=false because @Id already adds a unique primary key.
     * https://hibernate.onjira.com/browse/HHH-5376
     * http://sourceforge.net/projects/hsqldb/forums/forum/73674/topic/4537620
     *
     */
    @Id
    @Column(
        name = DB_IDENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    @GeneratedValue(
        generator=DB_GEN_NAME
        )
    @GenericGenerator(
        name=DB_GEN_NAME,
        strategy=DB_GEN_METHOD
        )
    private Long ident ;

    @Override
    public Identifier ident()
        {
        if (this.ident != null)
            {
            return new LongIdentifier(
                this.ident
                );
            }
        else {
            return null ;
            }
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
        this.name = name ;
        }

    /**
     * The Entity owner.
     *
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = IdentityEntity.class
        )
    @JoinColumn(
        name = DB_OWNER_COL,
        unique = false,
        nullable = true,
        updatable = false
        )
    private Identity owner ;
    @Override
    public Identity owner()
        {
        return this.owner ;
        }
    protected void owner(Identity owner)
        {
        this.owner = owner;
        }

    /**
     * The date/time this Entity was created.
     *
     */
    @Column(
        name = DB_CREATED_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private DateTime created ;
    @Override
    public DateTime created()
        {
        return this.created ;
        }

    /**
     * The date/time this Entity was modified.
     *
     */
    @Version
    @Column(
        name = DB_MODIFIED_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private DateTime modified ;
    @Override
    public DateTime modified()
        {
        return this.modified ;
        }

    /**
     * The Entity description.
     *
     */
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
    public void text(final String text)
        {
        this.text = text;
        }

    /**
     * Object toString() method.
     *
     */
    @Override
    public String toString()
        {
        final StringBuilder builder = new StringBuilder();
        builder.append("entity[");

            builder.append("class[");
            builder.append(
                this.getClass().getName()
                );
            builder.append("]");

            builder.append(" ident[");
            builder.append(
                this.ident()
                );
            builder.append("]");

            builder.append(" name[");
            builder.append(
                this.name()
                );
            builder.append("]");

        builder.append("]");
        return builder.toString();
        }

    /**
     * Object equals(Object) method (based on ident only).
     *
     */
    @Override
    public boolean equals(final Object that)
        {
        if (that != null)
            {
            if (this == that)
                {
                return true ;
                }
            if (that instanceof Entity)
                {
                return this.ident().equals(
                    ((Entity)that).ident()
                    );
                }
            }
        return false ;
        }

    /**
     * Object hashCode() method (based on ident only).
     *
     */
    @Override
    public int hashCode()
        {
        if (this.ident != null)
            {
            return this.ident.hashCode() ;
            }
        else {
            return -1 ;
            }
        }

    /**
     * Refresh (fetch) this Entity from the database.
     *
     */
    @Override
    public void refresh()
        {
        log.debug("---- ---- ---- ----");
        log.debug("refresh()");
        factories().hibernate().refresh(
            this
            );
        log.debug("---- ----");
        }

    /**
     * Delete this Entity from the database.
     *
     */
    @Override
    public void delete()
        {
        factories().hibernate().delete(
            this
            );
        }
    }

