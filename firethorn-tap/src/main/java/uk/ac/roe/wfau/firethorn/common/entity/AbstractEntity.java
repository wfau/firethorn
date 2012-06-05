/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Version;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.UniqueConstraint;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.GenericGenerator;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;
import uk.ac.roe.wfau.firethorn.common.ident.LongIdent;
import uk.ac.roe.wfau.firethorn.common.ident.AbstractIdent;

/**
 * Generic base class for a persistent Entity.
 *
 */
@MappedSuperclass
public abstract class AbstractEntity
implements GenericEntity
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        AbstractEntity.class
        );

    /**
     * Our database mapping values.
     * 
     */
    public static final String DB_NAME_COL   = "name"  ;
    public static final String DB_IDENT_COL  = "ident" ;

    public static final String DB_GEN_NAME   = "abstract-ident" ;
    public static final String DB_GEN_METHOD = "hilo" ;

    public static final String DB_CREATED_COL  = "created"  ;
    public static final String DB_MODIFIED_COL = "modified" ;

    /**
     * Access to our Womble instance - naff.
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
        logger.debug("AbstractEntity()");
        }

    /**
     * Protected constructor, sets the create date.
     *
     */
    protected AbstractEntity(final String name)
        {
        super();
        logger.debug("AbstractEntity(String)");
        logger.debug("  Name [{}]", name);
        this.name    = name ;
        this.created = new Date();
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
    protected Long ident ;

    @Override
    public Identifier ident()
        {
        return new AbstractIdent()
            {
            public Serializable value()
                {
                return ident ;
                }
            };
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
     * Generic toString() method.
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
            this.ident
            );
        builder.append("]");
        return builder.toString();
        }

    }

