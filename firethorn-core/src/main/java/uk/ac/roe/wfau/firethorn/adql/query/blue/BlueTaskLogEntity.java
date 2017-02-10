/*
 *  Copyright (C) 2017 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.adql.query.blue;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;

/**
 * 
 * 
 */
@Slf4j
@javax.persistence.Entity
@Access(
    AccessType.FIELD
    )
@Inheritance(
    strategy = InheritanceType.TABLE_PER_CLASS
    )
@Table(
    name = BlueTaskLogEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList = BlueTaskLogEntity.DB_TASK_COL
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "BlueTaskLogEntity-select-task",
            query = "FROM BlueTaskLogEntity WHERE task = :task ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "BlueTaskLogEntity-select-task.level",
            query = "FROM BlueTaskLogEntity WHERE task = :task AND level = :level ORDER BY ident desc"
            )
        }
    )
public class BlueTaskLogEntity
    extends AbstractEntity
    implements BlueTaskLogEntry
    {

    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "BlueTaskLogEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_TASK_COL = "task";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_SOURCE_COL = "source";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_LEVEL_COL = "level";
    
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_MESSAGE_COL = "message";
    
    /**
     * {@link Entity.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends AbstractEntityFactory<BlueTaskLogEntry>
    implements BlueTaskLogEntry.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return BlueTaskLogEntity.class ;
            }

        /**
         * Get a name for a source {@link Object}.
         * Initially this just returns the {@link Object} class name.
         * 
         */
        protected String identify(final Object source)
            {
            if (null != source)
                {
                return source.getClass().getName();
                }
            else {
                return null ;
                }
            }

        @Override
        @CreateAtomicMethod
        public BlueTaskLogEntry create(final BlueTask<?> task, final Level level, final String message)
            {
            String source = Thread.currentThread().getStackTrace()[1].getClassName();
            return create(
                source,
                task,
                level,
                message
                );
            }

        @Override
        @CreateAtomicMethod
        public BlueTaskLogEntry create(final Object source, final BlueTask<?> task, final Level level, final String message)
            {
            return create(
                identify(
                    source
                    ),
                task,
                level,
                message
                );
            }

        @CreateAtomicMethod
        public BlueTaskLogEntry create(final String source, final BlueTask<?> task, final Level level, final String message)
            {
            return this.insert(
                new BlueTaskLogEntity(
                    source,
                    task,
                    level,
                    message
                    )
                );
            }

        @Override
        @SelectMethod
        public Iterable<BlueTaskLogEntry> select(final BlueTask<?> task)
            {
            return super.list(
                super.query(
                    "BlueTaskLogEntity-select-task"
                    ).setParameter(
                        "task",
                        task.ident()
                        )
                );
            }

        @Override
        public Iterable<BlueTaskLogEntry> select(final BlueTask<?> task, final Integer limit)
            {
            return super.list(
                super.query(
                    "BlueTaskLogEntity-select-task"
                    ).setParameter(
                        "task",
                        task
                        )
                );
            }

        @Override
        @SelectMethod
        public Iterable<BlueTaskLogEntry> select(final BlueTask<?> task, final Level level)
            {
            return super.list(
                super.query(
                    "BlueTaskLogEntity-select-task.level"
                    ).setParameter(
                        "task",
                        task
                        ).setParameter(
                            "level",
                            level.name()
                            )
                );
            }

        @Override
        public Iterable<BlueTaskLogEntry> select(final BlueTask<?> task, final Integer limit, final Level level)
            {
            return super.list(
                super.query(
                    "LogEntryEntity-select-task.level"
                    ).setParameter(
                        "task",
                        task.ident()
                        ).setParameter(
                            "level",
                            level.name()
                            )
                );
            }
        }

    /**
     * {@link BlueTaskLogEntry.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements BlueTaskLogEntry.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static BlueTaskLogEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static BlueTaskLogEntity.EntityServices instance()
            {
            return BlueTaskLogEntity.EntityServices.instance ;
            }

        /**
         * Protected constructor.
         * 
         */
        protected EntityServices()
            {
            }
        
        /**
         * Protected initialiser.
         * 
         */
        @PostConstruct
        protected void init()
            {
            log.debug("init()");
            if (BlueTaskLogEntity.EntityServices.instance == null)
                {
                BlueTaskLogEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private BlueTaskLogEntry.IdentFactory idents;
        @Override
        public BlueTaskLogEntry.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private BlueTaskLogEntry.LinkFactory links;
        @Override
        public BlueTaskLogEntry.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private BlueTaskLogEntry.EntityFactory entities;
        @Override
        public BlueTaskLogEntry.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected BlueTaskLogEntry.EntityFactory factory()
        {
        return BlueTaskLogEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected BlueTaskLogEntry.EntityServices services()
        {
        return BlueTaskLogEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }
    
    /**
     * Protected constructor.
     * 
     */
    protected BlueTaskLogEntity()
        {
        }

    /**
     * Protected constructor.
     * 
     */
    protected BlueTaskLogEntity(final String source, final BlueTask<?> task, final Level level, final String message)
        {
        super(true);
        this.task = task;
        this.source = source;
        this.message = message;
        if (level != null)
            {
            this.level = level;
            }
        else {
            this.level = Level.ERROR;
            }

        log.debug("LogEntryEntity()");
        log.debug("  task    [{}]", task);
        log.debug("  level   [{}]", level);
        log.debug("  source  [{}]", source);
        log.debug("  message [{}]", message);

        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = BlueTaskEntity.class
        )
    @JoinColumn(
        name = DB_TASK_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    protected BlueTask<?> task;
    public BlueTask<?> task()
        {
        return this.task;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_SOURCE_COL,
        unique = false,
        nullable = true,
        updatable = false
        )
    private String source;
    @Override
    public String source()
        {
        return this.source;
        }
    
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_LEVEL_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    @Enumerated(
        EnumType.STRING
        )
    private Level level;
    @Override
    public Level level()
        {
        return this.level;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_MESSAGE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private String message;
    @Override
    public String message()
        {
        return this.message;
        }
    }
