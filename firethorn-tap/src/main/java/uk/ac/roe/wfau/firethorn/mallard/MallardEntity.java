/*
 *
 *
 */
package uk.ac.roe.wfau.firethorn.mallard ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

import uk.ac.roe.wfau.firethorn.common.womble.Womble;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;

/**
 * Core Widgeon implementations.
 *
 */
@Entity
@Table(
    name = MallardEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "mallard-select",
            query = "FROM MallardEntity"
            ),
        @NamedQuery(
            name  = "mallard-select-name",
            query = "FROM MallardEntity WHERE name = :name"
            )
        }
    )
public class MallardEntity
extends AbstractEntity
implements Mallard
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        MallardEntity.class
        );

    /**
     * Our database table name.
     * 
     */
    public static final String DB_TABLE_NAME = "mallard_entity" ;

    /**
     * Mallard factory.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Mallard>
    implements Mallard.Factory
        {

        @Override
        public Class etype()
            {
            return MallardEntity.class ;
            }

        @SelectEntityMethod
        public Iterable<Mallard> select()
            {
            return super.iterable(
                super.query(
                    "mallard-select"
                    )
                );
            }

        /*
        @Override
        @SelectEntityMethod
        public Mallard select(final Identifier ident)
            {
            return super.select(
                ident
                );
            }
        */

        @Override
        @CreateEntityMethod
        public Mallard create(String name)
            {
            return super.insert(
                new MallardEntity(
                    name
                    )
                );
            }

        /**
         * Our Autowired Job factory.
         * 
         */
        //@Autowired
        protected Job.Factory jobs ;

        /**
         * Access to our Job factory.
         * 
         */
        @Override
        public Job.Factory jobs()
            {
            return this.jobs ;
            }
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected MallardEntity()
        {
        super();
        }

    /**
     * Create a new MallardEntity.
     *
     */
    private MallardEntity(String name)
        {
        super(name);
        }

    @Override
    public Jobs jobs()
        {
        return new Jobs()
            {
            @Override
            public Job create(String adql)
                {
                return womble().mallards().jobs().create(
                    MallardEntity.this,
                    adql
                    ) ;
                }

            @Override
            public Iterable<Job> select()
                {
                return womble().mallards().jobs().select(
                    MallardEntity.this
                    ) ;
                }
            };
        }

    /**
     * The collection of resources used by this service.
     *
     */
    @Override
    public Widgeons widgeons()
        {
        return null ;
        }
/*
    public interface Widgeons
    extends Entity.Factory<Widgeon>
        {
        public void insert(Widgeon widgeon);
        }
*/

    }

