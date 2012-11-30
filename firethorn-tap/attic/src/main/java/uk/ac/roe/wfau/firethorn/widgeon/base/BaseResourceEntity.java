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
package uk.ac.roe.wfau.firethorn.widgeon.base ;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponentImpl;

/**
 * Hibernate implementation of <code>BaseResource</code>.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Inheritance(
    strategy=InheritanceType.SINGLE_TABLE
    )
@DiscriminatorColumn(
    name=BaseResourceEntity.DB_CLASS_NAME,
    discriminatorType=DiscriminatorType.STRING
    )
@DiscriminatorValue(
    value=BaseResourceEntity.DB_CLASS_TYPE
    )
@Table(
    name = BaseResourceEntity.DB_TABLE_NAME
/*
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL
                }
            )
        }
 */
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "base.resource-select-all",
            query = "FROM BaseResourceEntity ORDER BY ident desc"
            ),
            @NamedQuery(
                name  = "base.resource-select-name",
                query = "FROM BaseResourceEntity WHERE (name = :name) ORDER BY ident desc"
                ),
        @NamedQuery(
            name  = "base.resource-search-text",
            query = "FROM BaseResourceEntity WHERE (name LIKE :text) ORDER BY ident desc"
            )
        }
    )
public abstract class BaseResourceEntity
extends DataComponentImpl
implements BaseResource
    {

    /**
     * Our persistence table name.
     *
     */
    public static final String DB_TABLE_NAME = "base_resource" ;

    /**
     * Our persistence type column.
     *
     */
    public static final String DB_CLASS_NAME = "resource_type" ;

    /**
     * Our persistence type value.
     *
     */
    public static final String DB_CLASS_TYPE = "BASE" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<BaseResource>
    implements BaseResource.Factory<BaseResource>
        {

        @Override
        public Class<?> etype()
            {
            return BaseResourceEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<BaseResource> select()
            {
            return super.iterable(
                super.query(
                    "base.resource-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<BaseResource> select(final String name)
            {
            return super.iterable(
                super.query(
                    "base.resource-select-name"
                    ).setString(
                        "name",
                        name
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<BaseResource> search(final String text)
            {
            final String match = new StringBuilder(text).append("%").toString();
            return super.iterable(
                super.query(
                    "base.resource-search-text"
                    ).setString(
                        "text",
                        match
                        )
                );
            }

        /*
        @Override
        @CreateEntityMethod
        public BaseResource create(final String name)
            {
            return super.insert(
                new BaseResourceEntity(
                    name
                    )
                );
            }
        */

        @Autowired
        protected AdqlResource.Factory views ;

        @Override
        public AdqlResource.Factory views()
            {
            return this.views ;
            }

        @Autowired
        protected BaseResource.IdentFactory identifiers ;

        @Override
        public BaseResource.IdentFactory identifiers()
            {
            return this.identifiers;
            }
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected BaseResourceEntity()
        {
        super();
        }

    /**
     * Create a new resource.
     *
     */
    protected BaseResourceEntity(final String name)
        {
        super(name);
        log.debug("BaseResourceEntity([{}]", name);
        }

    @Override
    public void name(final String name)
    throws NameFormatException
        {
        if ((name != null) && (name.trim().length() > 0))
            {
            this.name = name.trim() ;
            }
        else {
            throw new NameFormatException(
                name
                );
            }
        }

    @Override
    public String link()
        {
        return null;
        }
    }

