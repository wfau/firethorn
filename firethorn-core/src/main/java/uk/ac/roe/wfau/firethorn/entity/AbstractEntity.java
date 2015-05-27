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

import java.util.Random;

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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.entity.access.EntityProtector;
import uk.ac.roe.wfau.firethorn.entity.access.SimpleEntityProtector;
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
    /**
     * Shared random number generator, used to create the random part of the Entity UID.
     * This does not need to be secure, it just needs to be random enough to prevent collisions.
     * 
     */
    protected static final Random random = new Random(
        System.currentTimeMillis()
        );
    
    /**
     * Hibernate table name prefix.
     *
     */
    protected static final String DB_TABLE_PREFIX = "FT011315";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_PKEY_COL   = "ident" ;
    protected static final String DB_UID_LO_COL = "uidlo" ;
    protected static final String DB_UID_HI_COL = "uidhi" ;

    protected static final String DB_OWNER_COL  = "owner" ;

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

    protected void factories(final ComponentFactories factories)
        {
        this.factories = factories ; 
        }
    
    /**
     * Access to our ComponentFactories singleton instance.
     * Has to use dynamic initialisation.
     * http://stackoverflow.com/questions/9104221/hibernate-buildsessionfactory-exception
     * TODO Improve this
     * 
     * TODO This might help
     * http://guylabs.ch/2014/02/22/autowiring-pring-beans-in-hibernate-jpa-entity-listeners/
     * TODO Looks similar
     * http://stackoverflow.com/a/4144102
     * 
     * TODO Another way
     * http://stackoverflow.com/a/9011451
     * 
     * TODO Looks complicated
     * http://www.javacodegeeks.com/2013/10/spring-injected-beans-in-jpa-entitylisteners.html
     * https://code.google.com/p/invariant-properties-blog/source/browse/spring-entity-listener/src/main/java/com/invariantproperties/sandbox/springentitylistener/annotation/SpringEntityListeners.java
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
        //log.debug("AbstractEntity(boolean)");
        //log.debug("  init [{}]", init);
        
        if (init)
            {
            this.uidlo = random.nextLong();
            this.uidhi = System.currentTimeMillis();
            
            this.owner = factories().contexts().current().identity();
            this.created = new DateTime();
            }
        else {
            throw new IllegalStateException(
                "AbstractEntity constructor called with invalid param [false]"
                );
            }

        log.debug("  created  [{}]", created);

        /*
        *
        * [UnresolvedEntityInsertActions] HHH000437: Attempting to save one or more entities that have a non-nullable association with an unsaved transient entity. The unsaved transient entity must be saved in an operation prior to saving these dependent entities.
        * Unsaved transient entity: ([uk.ac.roe.wfau.firethorn.identity.IdentityEntity#<null>])
        * Dependent entities: ([[uk.ac.roe.wfau.firethorn.identity.IdentityEntity#<null>]])
        * Non-nullable association(s): ([uk.ac.roe.wfau.firethorn.identity.IdentityEntity.owner])
        * Error executing Hibernate query [org.hibernate.TransientPropertyValueException][Not-null property references a transient value - transient instance must be saved before current operation: uk.ac.roe.wfau.firethorn.identity.IdentityEntity.owner -> uk.ac.roe.wfau.firethorn.identity.IdentityEntity]
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
     * The database primary key.
     * Note - unique=false because @Id already adds a unique primary key.
     * https://hibernate.onjira.com/browse/HHH-5376
     * http://sourceforge.net/projects/hsqldb/forums/forum/73674/topic/4537620
     *
     */
    @Id
    @Column(
        name = DB_PKEY_COL,
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
     * The lower part of the Entity UID.
     *  
     */
    @Column(
        name = DB_UID_LO_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Long uidlo ;
    protected Long uidlo()
        {
        return this.uidlo;
        }

    /**
     * The higher part of the Entity UID.
     *  
     */
    @Column(
        name = DB_UID_HI_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Long uidhi ;
    protected Long uidhi()
        {
        return this.uidhi;
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

    @Override
    public boolean equals(final Object that)
        {
        if (that == null)
            {
            return false;
            }
        if (this == that)
            {
            return true ;
            }
        if (that instanceof Entity)
            {
            final EqualsBuilder builder = new EqualsBuilder();
            builder.appendSuper(
                super.equals(
                    that
                    )
                );
            if (that instanceof AbstractEntity)
                {
                builder.append(
                    ((AbstractEntity)that).uidlo(),
                    this.uidlo()
                    );
                builder.append(
                    ((AbstractEntity)that).uidhi(),
                    this.uidhi()
                    );
                }
           
            else {
                builder.append(
                    ((Entity)that).ident(),
                    this.ident()
                    );
                }
            return builder.isEquals();
            }
        else {
            return false ;
            }
        }

    /**
     * A non-zero, odd number used as the initial hash code value.  
     * @see HashCodeBuilder#HashCodeBuilder(int, int) 
     * 
     */
    protected static final int HASHCODE_INITIAL = 51 ; 

    /**
     * A non-zero, odd number used as the hash code multiplier.  
     * @see HashCodeBuilder#HashCodeBuilder(int, int) 
     * 
     */
    protected static final int HASHCODE_MULTIPLIER = 97 ; 

    @Override
    public int hashCode()
        {
        return this.hashCode(
            HASHCODE_INITIAL,
            HASHCODE_MULTIPLIER
            );
        }

    /**
     * Two randomly chosen, non-zero, odd numbers must be passed in.
     * Ideally these should be different for each class, however this is not vital.
     * Prime numbers are preferred, especially for the multiplier. 
     * 
     * @param initial A non-zero, odd number used as the initial value 
     * @param multiplier A a non-zero, odd number used as the multiplier 
     * @return The computed hashCode.
     * @see HashCodeBuilder#HashCodeBuilder(int, int) 
     *
     */
    protected int hashCode(int initial, int multiplier)
        {
        final HashCodeBuilder builder = new HashCodeBuilder(
            initial,
            multiplier
            ); 
        builder.append(
            this.uidlo()
            );
        builder.append(
            this.uidhi()
            );
        return builder.toHashCode();
        }

    @Override
    public EntityProtector protector()
        {
        return new SimpleEntityProtector(
            this
            );
        }
    }
