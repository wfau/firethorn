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
package uk.ac.roe.wfau.firethorn.tuesday;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TuesdayIvoaResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TuesdayIvoaResource-select-all",
            query = "FROM TuesdayIvoaResourceEntity ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayIvoaResource-search-text",
            query = "FROM TuesdayIvoaResourceEntity WHERE (name LIKE :text) ORDER BY ident desc"
            )
        }
    )
public class TuesdayIvoaResourceEntity
    extends TuesdayBaseResourceEntity<TuesdayIvoaSchema>
    implements TuesdayIvoaResource
    {
    protected static final String DB_TABLE_NAME = "TuesdayIvoaResourceEntity";

    protected static final String DB_URI_COL  = "uri"; 
    protected static final String DB_URL_COL  = "url"; 

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayIvoaResource>
    implements TuesdayIvoaResource.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayIvoaResourceEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayIvoaResource> select()
            {
            return super.iterable(
                super.query(
                    "TuesdayIvoaResource-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayIvoaResource> search(final String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayIvoaResource-search-text"
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }

        @Override
        @CreateEntityMethod
        public TuesdayIvoaResource  create(final String name)
            {
            return super.insert(
                new TuesdayIvoaResourceEntity(
                    name
                    )
                );
            }

        @Autowired
        protected TuesdayIvoaSchema.Factory schemas;
        @Override
        public TuesdayIvoaSchema.Factory schemas()
            {
            return this.schemas;
            }

        @Autowired
        protected TuesdayIvoaResource.IdentFactory idents;
        @Override
        public TuesdayIvoaResource.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected TuesdayIvoaResource.LinkFactory links;
        @Override
        public TuesdayIvoaResource.LinkFactory links()
            {
            return this.links;
            }
        }
    
    protected TuesdayIvoaResourceEntity()
        {
        super();
        }

    protected TuesdayIvoaResourceEntity(String name)
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
    public void uri(String uri)
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
    public void url(String url)
        {
        this.url = url;
        }

    @Override
    public TuesdayIvoaResource.Schemas schemas()
        {
        return new TuesdayIvoaResource.Schemas()
            {
            @Override
            public Iterable<TuesdayIvoaSchema> select()
                {
                return factories().ivoa().schemas().select(
                    TuesdayIvoaResourceEntity.this
                    );
                }
            @Override
            public TuesdayIvoaSchema select(String name)
                {
                return factories().ivoa().schemas().select(
                    TuesdayIvoaResourceEntity.this,
                    name
                    );
                }
            @Override
            public Iterable<TuesdayIvoaSchema> search(String text)
                {
                return factories().ivoa().schemas().search(
                    TuesdayIvoaResourceEntity.this,
                    text
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
	public void ogsaid(String ogsaid)
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
    }
