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
package uk.ac.roe.wfau.firethorn.config;

import java.net.URI;
import java.net.URISyntaxException;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;

/**
 * ConfigProperty implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = ConfigPropertyEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "config-select-all",
            query = "FROM ConfigPropertyEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "config-select-key",
            query = "FROM ConfigPropertyEntity WHERE key = :key ORDER BY ident desc"
            ),
        }
    )
public class ConfigPropertyEntity
extends AbstractNamedEntity
implements ConfigProperty
    {

    /**
     * Our database table name.
     *
     */
    public static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "ConfigProperty" ;

    /*
     * Our database mapping values.
     *
     */
    public static final String DB_KEY_COL   = "propkey" ;
    public static final String DB_TYPE_COL  = "type"    ;
    public static final String DB_VALUE_COL = "value"   ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends AbstractEntityFactory<ConfigProperty>
    implements ConfigProperty.EntityFactory
        {

        @Override
        public Class<ConfigPropertyEntity> etype()
            {
            return ConfigPropertyEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public ConfigProperty select(final URI key)
            {
            return super.first(
                super.query(
                    "config-select-key"
                    ).setParameter(
                        "key",
                        key
                        )
                );
            }

        @Override
        @CreateEntityMethod
        public ConfigProperty create(final URI key, final String name, final String value)
            {
            final ConfigProperty property = this.select(
                key
                );
            if (property != null)
                {
                return property ;
                }
            else {
                return super.insert(
                    new ConfigPropertyEntity(
                        key,
                        name,
                        value
                        )
                    );
                }
            }

        @Autowired
        protected ConfigProperty.IdentFactory idents;
        @Override
        public ConfigProperty.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected ConfigProperty.LinkFactory links;
        @Override
        public ConfigProperty.LinkFactory links()
            {
            return this.links;
            }
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected ConfigPropertyEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     *
     */
    protected ConfigPropertyEntity(final URI key, final String name, final String value)
        {
        super(name);
        this.key   = key   ;
        this.value = value ;
        }

    /**
     * The property key.
     *
     */
    @Column(
        name = DB_KEY_COL,
        unique = true,
        nullable = false,
        updatable = false
        )
    private URI key;

    @Override
    public  URI key()
        {
        return this.key;
        }

    /**
     * The property value.
     *
     */
    @Column(
        name = DB_VALUE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String value;

    @Override
    public String toString()
        {
        return this.value;
        }

    @Override
    public URI toUri()
        {
        try {
            return new URI(
                this.value
                );
            }
        catch (final URISyntaxException ouch)
            {
            log.error("Failed to convert config property to URI [{}][{}]", this.value, ouch);
            return null ;
            }
        }

    @Override
    public String link()
        {
        return null ;
        }
    }

