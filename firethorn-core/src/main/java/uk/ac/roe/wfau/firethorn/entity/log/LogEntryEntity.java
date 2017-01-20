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

package uk.ac.roe.wfau.firethorn.entity.log;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
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
@Table(
    name = LogEntryEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList = LogEntryEntity.DB_SUBJECT_COL
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "LogEntryEntity-select-subject",
            query = "FROM LogEntryEntity WHERE subject = :subject ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "LogEntryEntity-select-subject.level",
            query = "FROM LogEntryEntity WHERE subject = :subject AND level = :level ORDER BY ident desc"
            )
        }
    )
public class LogEntryEntity
    extends AbstractEntity
    implements LogEntry
    {

    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "LogEntryEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_SUBJECT_COL = "subject";

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
    extends AbstractEntityFactory<LogEntry>
    implements LogEntry.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return LogEntryEntity.class ;
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
        public LogEntry create(final Entity subject, final Level level, final String message)
            {
            String source = Thread.currentThread().getStackTrace()[1].getClassName();
            return create(
                source,
                subject,
                level,
                message
                );
            }

        @Override
        @CreateAtomicMethod
        public LogEntry create(final Object source, final Entity subject, final Level level, final String message)
            {
            return create(
                identify(
                    source
                    ),
                subject,
                level,
                message
                );
            }

        @CreateAtomicMethod
        public LogEntry create(final String source, final Entity subject, final Level level, final String message)
            {
            return this.insert(
                new LogEntryEntity(
                    source,
                    subject,
                    level,
                    message
                    )
                );
            }

        @Override
        @SelectMethod
        public Iterable<LogEntry> select(final Entity subject)
            {
            return super.list(
                super.query(
                    "LogEntryEntity-select-subject"
                    ).setParameter(
                        "subject",
                        subject
                        )
                );
            }

        @Override
        public Iterable<LogEntry> select(final Entity subject, final Integer limit)
            {
            return super.list(
                super.query(
                    "LogEntryEntity-select-subject"
                    ).setParameter(
                        "subject",
                        subject
                        )
                );
            }

        @Override
        @SelectMethod
        public Iterable<LogEntry> select(final Entity subject, final Level level)
            {
            return super.list(
                super.query(
                    "LogEntryEntity-select-subject.level"
                    ).setParameter(
                        "subject",
                        subject
                        ).setParameter(
                            "level",
                            level.name()
                            )
                );
            }

        @Override
        public Iterable<LogEntry> select(final Entity subject, final Integer limit, final Level level)
            {
            return super.list(
                super.query(
                    "LogEntryEntity-select-subject.level"
                    ).setParameter(
                        "subject",
                        subject
                        ).setParameter(
                            "level",
                            level.name()
                            )
                );
            }
        }

    /**
     * {@link LogEntry.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements LogEntry.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static LogEntryEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static LogEntryEntity.EntityServices instance()
            {
            return LogEntryEntity.EntityServices.instance ;
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
            if (LogEntryEntity.EntityServices.instance == null)
                {
                LogEntryEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private LogEntry.IdentFactory idents;
        @Override
        public LogEntry.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private LogEntry.LinkFactory links;
        @Override
        public LogEntry.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private LogEntry.EntityFactory entities;
        @Override
        public LogEntry.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected LogEntry.EntityFactory factory()
        {
        return LogEntryEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected LogEntry.EntityServices services()
        {
        return LogEntryEntity.EntityServices.instance() ; 
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
    protected LogEntryEntity()
        {
        }

    /**
     * Protected constructor.
     * 
     */
    protected LogEntryEntity(final String source, final Entity subject, final Level level, final String message)
        {
        super();
        this.level = level ;
        this.source = source;
        this.subject = subject;
        this.message = message;

        log.debug("LogEntryEntity()");
        log.debug("  source  [{}]", source);
        log.debug("  level   [{}]", level);
        log.debug("  subject [{}]", subject);
        log.debug("  message [{}]", message);

        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AbstractEntity.class
        )
    @JoinColumn(
        name = DB_SUBJECT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Entity subject;
    @Override
    public Entity subject()
        {
        return this.subject;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_SOURCE_COL,
        unique = false,
        nullable = false,
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
