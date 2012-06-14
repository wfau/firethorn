/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Version;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.UniqueConstraint;
import javax.persistence.MappedSuperclass;

import javax.persistence.Access;
import javax.persistence.AccessType;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.GenericGenerator;

import uk.ac.roe.wfau.firethorn.common.womble.Womble;
import uk.ac.roe.wfau.firethorn.common.womble.WombleImpl;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;
import uk.ac.roe.wfau.firethorn.common.ident.LongIdent;
import uk.ac.roe.wfau.firethorn.common.ident.AbstractIdent;

/**
 * Generic base class for a persistent Entity.
 *
 * Problems with AccessType.FIELD and getter/setter methods
 * mean we have to have get/set methods on fields we want to modify.
 * If we don't have the get/set methods then Hibernate ignores the changes
 * and doesn't commit them to the database. 
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
    public static final String DB_GEN_NAME   = "entity-ident" ;
    public static final String DB_GEN_METHOD = "identity" ;

    public static final String DB_NAME_COL   = "name"  ;
    public static final String DB_IDENT_COL  = "ident" ;

    public static final String DB_CREATED_COL  = "created"  ;
    public static final String DB_MODIFIED_COL = "modified" ;

    /**
     * Access to our Womble instance - naff, but works.
     *
     */
    protected Womble womble()
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
     * @todo Better default for the name.
     *
     */
    protected AbstractEntity(final String name)
        {
        super();
        this.name    = name ;
        this.created = new Date();
        if (this.name == null)
            {
            this.name = "no name" ;
            }
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
            return new LongIdent(
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
        this.name = name ;
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
        builder.append("[");
        builder.append(
            this.getClass().getName()
            );
        builder.append("][");
        builder.append(
            this.ident()
            );
        builder.append("][");
        builder.append(
            this.hashCode()
            );
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
     */
    @Override
    public void update()
        {
        womble().hibernate().update(
            this
            );
        }

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

