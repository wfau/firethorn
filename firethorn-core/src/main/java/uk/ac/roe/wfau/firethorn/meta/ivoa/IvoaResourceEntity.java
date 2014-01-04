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
package uk.ac.roe.wfau.firethorn.meta.ivoa;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;

/**
 *
 *
 */
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = IvoaResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "IvoaResource-select-all",
            query = "FROM IvoaResourceEntity ORDER BY name asc, ident desc"
            )
        }
    )
public class IvoaResourceEntity
    extends BaseResourceEntity<IvoaSchema>
    implements IvoaResource
    {
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "IvoaResourceEntity";

    protected static final String DB_URI_COL  = "uri";
    protected static final String DB_URL_COL  = "url";

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends AbstractEntityFactory<IvoaResource>
    implements IvoaResource.EntityFactory
        {

        @Override
        public Class<?> etype()
            {
            return IvoaResourceEntity.class ;
            }

        @Override
        @SelectMethod
        public Iterable<IvoaResource> select()
            {
            return super.iterable(
                super.query(
                    "IvoaResource-select-all"
                    )
                );
            }

        @Override
        @CreateMethod
        public IvoaResource create(final String name)
            {
            return super.insert(
                new IvoaResourceEntity(
                    name
                    )
                );
            }

        @Autowired
        protected IvoaSchema.EntityFactory schemas;
        @Override
        public IvoaSchema.EntityFactory schemas()
            {
            return this.schemas;
            }

        @Autowired
        protected IvoaResource.IdentFactory idents;
        @Override
        public IvoaResource.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected IvoaResource.LinkFactory links;
        @Override
        public IvoaResource.LinkFactory links()
            {
            return this.links;
            }
        }

    protected IvoaResourceEntity()
        {
        super();
        }

    protected IvoaResourceEntity(final String name)
        {
        super(name);
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_URI_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String uri;
    @Override
    public String uri()
        {
        return this.uri;
        }
    @Override
    public void uri(final String uri)
        {
        this.uri = uri;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_URL_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String url;
    @Override
    public String url()
        {
        return this.url;
        }
    @Override
    public void url(final String url)
        {
        this.url = url;
        }

    @Override
    public IvoaResource.Schemas schemas()
        {
        return new IvoaResource.Schemas()
            {
            @Override
            public Iterable<IvoaSchema> select()
                {
                return factories().ivoa().schemas().select(
                    IvoaResourceEntity.this
                    );
                }
            @Override
            public IvoaSchema select(final String name)
            throws NameNotFoundException
                {
                return factories().ivoa().schemas().select(
                    IvoaResourceEntity.this,
                    name
                    );
                }
            @Override
            public IvoaSchema search(final String name)
                {
                return factories().ivoa().schemas().search(
                    IvoaResourceEntity.this,
                    name
                    );
                }
            };
        }

	/**
	 * The the OGSA-DAI resource ID.
	 * @todo Move to a common base class.
	 *
	 */
    protected static final String DB_OGSA_ID_COL = "ogsa_id";
    @Column(
        name = DB_OGSA_ID_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
	private String ogsaid;
	@Override
	public String ogsaid()
		{
		return this.ogsaid;
		}
	@Override
	public void ogsaid(final String ogsaid)
		{
		this.ogsaid = ogsaid;
		}

    @Override
    public String link()
        {
        return factories().ivoa().resources().links().link(
            this
            );
        }

    @Override
    protected void scanimpl()
        {
        // TODO Auto-generated method stub

        }
    }
