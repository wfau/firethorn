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
package uk.ac.roe.wfau.firethorn.common.entity ;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Version;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.UniqueConstraint;
import javax.persistence.MappedSuperclass;

import javax.persistence.ManyToOne;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

import javax.persistence.Access;
import javax.persistence.AccessType;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.GenericGenerator;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.IdentityEntity;

import uk.ac.roe.wfau.firethorn.common.womble.Womble;
import uk.ac.roe.wfau.firethorn.common.womble.WombleImpl;

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
    public static final String DB_GEN_NAME    = "entity-ident" ;
    public static final String DB_GEN_METHOD  = "identity" ;

    public static final String DB_NAME_COL    = "name"  ;
    public static final String DB_IDENT_COL   = "ident" ;
    public static final String DB_OWNER_COL   = "owner" ;

    public static final String DB_CREATED_COL  = "created"  ;
    public static final String DB_MODIFIED_COL = "modified" ;

    /**
     * Check an Entity name, returns the create date if the given name is null or empty.
     * @todo Delegate this to a naming factory.
     *
     */
    public static String name(final String name, final Entity entity)
        {
        if ((name == null) || (name.trim().length() == 0))
            {
            return entity.created().toString();
            }
        else {
            return name.trim();
            }
        }

    /**
     * Access to our Womble instance - naff, but works for now.
     * @todo Replace this with something, anything, else.
     *
     */
    protected static Womble womble()
        {
        return WombleImpl.womble();
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
     * The owner defaults to the current actor.
     *
     */
    protected AbstractEntity(final String name)
        {
        this(
            womble().actor(),
            name
            );
        }

    /**
     * Protected constructor, sets the owner, name and create date.
     * @todo Better default for the name.
     *
     */
    protected AbstractEntity(final Identity owner)
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
        {
        super();
        this.owner = owner;
        this.created = new Date();

        //
        // We need to set a name because name can't be null.
        // Name can't be null because it is used in unique constraints.
        // We can't use ident because it probably won't have been set yet.
        this.name = name(
            name,
            this
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
    @Column(
        name = DB_NAME_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    protected String getName()
        {
        return this.name ;
        }
    protected void setName(final String name)
        {
        this.name = name ;
        }
    private String name ;

    @Override
    public String name()
        {
        return this.name ;
        }

    @Override
    public void name(final String name)
        {
        if ((name != null) && (name.trim().length() > 0))
            {
            this.name = name ;
            }
        else {
//throw a name error.
            }
        }

    /**
     * The Entity owner.
     *
     */
    @ManyToOne(
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
    @Temporal(
        TemporalType.TIMESTAMP
        )
    private Date created ;

    @Override
    public Date created()
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
    @Temporal(
        TemporalType.TIMESTAMP
        )
    private Date modified ;

    @Override
    public Date modified()
        {
        return this.modified ;
        }

    /**
     * Object toString() method.
     *
     */
    public String toString()
        {
        StringBuilder builder = new StringBuilder();
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
     * Object equals(Object) method, based on ident only.
     *
     */
    @Override
    public boolean equals(Object that)
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
     * Object hashCode() method.
     *
    @Override
    public int hashCode()
        {
logger.debug("hashCode()");
        if (ident != null)
            {
            return ident.hashCode() ;
            }
        else {
            return -1 ;
            }        
        }
     */

    /**
     * Update (store) this Entity in the database.
     *
    @Override
    public void update()
        {
        womble().hibernate().update(
            this
            );
        }
     */

    /**
     * Delete this Entity from the database.
     *
     */
    @Override
    public void delete()
        {
        womble().hibernate().delete(
            this
            );
        }

    }

