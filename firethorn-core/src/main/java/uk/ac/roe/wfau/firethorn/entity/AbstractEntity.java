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
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.CommunityMemberEntity;
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

    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_PREFIX = "FT0109";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_IDENT_COL = "ident" ;
    protected static final String DB_UUID_COL  = "uuid"  ;
    protected static final String DB_OWNER_COL = "owner" ;

    protected static final String DB_CREATED_COL  = "created"  ;
    protected static final String DB_MODIFIED_COL = "modified" ;
    protected static final String DB_ACCESSED_COL = "accessed" ;

    /**
     * Access to our ComponentFactories singleton instance.
     * Can't use static initialisation.
     * http://stackoverflow.com/questions/9104221/hibernate-buildsessionfactory-exception
     *
     */
    @Transient
    protected ComponentFactories factories ;

    /**
     * Access to our ComponentFactories singleton instance.
     * Has to use dynamic initialisation.
     * http://stackoverflow.com/questions/9104221/hibernate-buildsessionfactory-exception
     * TODO Improve this
     *
     */
    protected ComponentFactories factories()
    	{
    	if (this.factories == null)
    		{
    		this.factories = ComponentFactoriesImpl.instance();
    		}
    	return this.factories;
    	}

    /**
     * Helper method to check for empty or blank strings.
     *
     * @param string The String to check
     * @return The trimmed String, or null if the String was blank.
     *
     */
    public static String emptystr(final String string)
        {
        if (string != null)
            {
            final String temp = string.trim();
            if (temp.length() > 0)
                {
                return temp;
                }
            }
        return null ;
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
     * Protected constructor, sets the owner and create date.
     * @param init A flag to distinguish this from the default constructor.
     *
     */
    protected AbstractEntity(final boolean init)
        {
        super();
        if (init)
            {
            //this.uuid = factories().uuids().uuid();
            this.owner = factories().identities().current();
            this.created = new DateTime();
            }

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
     *  strategy="org.hibernate.id.MultipleHiLoPerTableGenerator"
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
        generator="ident-generator"
        )
    @GenericGenerator(
        name="ident-generator",
        strategy="hilo"
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
     * The Entity owner.
     *
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = CommunityMemberEntity.class
        )
    @JoinColumn(
        name = DB_OWNER_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Identity owner ;
    @Override
    public Identity owner()
        {
        return this.owner ;
        }
    protected void owner(final Identity owner)
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
     * @TODO This is probably wrong ... should it be based on all the fields rather than just the ident ?
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
    @Override
    public void delete()
        {
        factories().hibernate().delete(
            this
            );
        }
     */

    @Override
    public EntityProtector protector()
        {
        // TODO Auto-generated method stub
        return null ;
        }
    }

