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
package uk.ac.roe.wfau.firethorn.test;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.green.GreenJob;
import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;
import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.config.ConfigProperty;
import uk.ac.roe.wfau.firethorn.entity.AbstractIdentFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractLinkFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNameFactory;
import uk.ac.roe.wfau.firethorn.entity.DateNameFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaColumn;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaSchema;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaExecResource;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaIvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaJdbcResource;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaService;

@Component
public class TestFactories
    {
    public static class MockNameFactory<EntityType extends NamedEntity>
    extends AbstractNameFactory<EntityType>
        {
		@Override
		public String name()
			{
			return "mock-name";
			}
		@Override
		public String name(String name)
			{
			return name;
			}
        }

    public static class MockLinkFactory<EntityType extends Entity>
    extends AbstractLinkFactory<EntityType>
        {
        public MockLinkFactory(final String path)
            {
            super(path);
            }
        @Override
        public EntityType resolve(String link)
            throws IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException
            {
            // TODO Auto-generated method stub
            return null;
            }
        }
    
    
    @Component
    public static class JobFactories
        {
        @Component
        public static class BaseJobFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<GreenJob>
            implements GreenJob.IdentFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<GreenJob>
            implements GreenJob.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/job/base"
                        );
                    }
                }
            }
        }

    @Component
    public static class AuthFactories
        {
        @Component
        public static class IdentityFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<Identity>
            implements Identity.IdentFactory
                {
                }
            @Component
            public static class NameFactory
            extends MockNameFactory<Identity>
            implements Identity.NameFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<Identity>
            implements Identity.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/auth/identity"
                        );
                    }
                }
            }

        @Component
        public static class CommunityFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<Community>
            implements Community.IdentFactory
                {
                }
            @Component
            public static class NameFactory
            extends MockNameFactory<Community>
            implements Community.NameFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<Community>
            implements Community.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/auth/community"
                        );
                    }
                }
            }

        @Component
        public static class OperationFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<Operation>
            implements Operation.IdentFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<Operation>
            implements Operation.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/operation"
                        );
                    }
                }
            }

        @Component
        public static class AuthenticationFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<Authentication>
            implements Authentication.IdentFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<Authentication>
            implements Authentication.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/authentication"
                        );
                    }
                }
            }
        }

    @Component
    public static class ConfigFactories
        {
        @Component
        public static class PropertyFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<ConfigProperty>
            implements ConfigProperty.IdentFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<ConfigProperty>
            implements ConfigProperty.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/config/property"
                        );
                    }
                }
            }
        }

    @Component
    public static class AdqlFactories
        {

        @Component
        public static class QueryFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<GreenQuery>
            implements GreenQuery.IdentFactory
                {
                }
            @Component
            public static class NameFactory
            extends DateNameFactory<GreenQuery>
            implements GreenQuery.NameFactory
                {

				@Override
				public String name() {
					return datename("TEST_QUERY");
				}
            	
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<GreenQuery>
            implements GreenQuery.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/adql/query"
                        );
                    }
                }
            }

        @Component
        public static class ColumnFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<AdqlColumn>
            implements AdqlColumn.IdentFactory
                {
                }
            @Component
            public static class NameFactory
            extends MockNameFactory<AdqlColumn>
            implements AdqlColumn.NameFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<AdqlColumn>
            implements AdqlColumn.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/adql/column"
                        );
                    }
                }
            }

        @Component
        public static class TableFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<AdqlTable>
            implements AdqlTable.IdentFactory
                {
                }
/*
 * 
            @Component
            public static class NameFactory
            extends MockNameFactory<AdqlTable>
            implements AdqlTable.NameFactory
                {
                }
 * 
 */
            @Component
            public static class LinkFactory
            extends MockLinkFactory<AdqlTable>
            implements AdqlTable.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/adql/table"
                        );
                    }
                }
            }

        @Component
        public static class SchemaFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<AdqlSchema>
            implements AdqlSchema.IdentFactory
                {
                }
            @Component
            public static class NameFactory
            extends MockNameFactory<AdqlSchema>
            implements AdqlSchema.NameFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<AdqlSchema>
            implements AdqlSchema.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/adql/schema"
                        );
                    }
                }
            }

        @Component
        public static class ResourceFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<AdqlResource>
            implements AdqlResource.IdentFactory
                {
                }
            @Component
            public static class NameFactory
            extends MockNameFactory<AdqlResource>
            implements AdqlResource.NameFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<AdqlResource>
            implements AdqlResource.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/adql/resource"
                        );
                    }
                }
            }
        }

    @Component
    public static class JdbcFactories
        {

        @Component
        public static class ColumnFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<JdbcColumn>
            implements JdbcColumn.IdentFactory
                {
                }
            @Component
            public static class NameFactory
            extends MockNameFactory<JdbcColumn>
            implements JdbcColumn.NameFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<JdbcColumn>
            implements JdbcColumn.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/jdbc/column"
                        );
                    }
                }
            }

        @Component
        public static class TableFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<JdbcTable>
            implements JdbcTable.IdentFactory
                {
                }
/*
 * 
            @Component
            public static class NameFactory
            extends MockNameFactory<JdbcTable>
            implements JdbcTable.NameFactory
                {
                }
 * 
 */

            @Component
            public static class LinkFactory
            extends MockLinkFactory<JdbcTable>
            implements JdbcTable.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/jdbc/table"
                        );
                    }
                }
            }

        @Component
        public static class SchemaFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<JdbcSchema>
            implements JdbcSchema.IdentFactory
                {
                }
/*
 * 
            @Component
            public static class NameFactory
            extends MockNameFactory<JdbcSchema>
            implements JdbcSchema.NameFactory
                {
				@Override
				public String fullname(String catalog, String schema)
					{
					return catalog + "." + schema ;
					}
                }
 * 
 */

            @Component
            public static class LinkFactory
            extends MockLinkFactory<JdbcSchema>
            implements JdbcSchema.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/jdbc/schema"
                        );
                    }
                }
            }

        @Component
        public static class ResourceFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<JdbcResource>
            implements JdbcResource.IdentFactory
                {
                }
            @Component
            public static class NameFactory
            extends MockNameFactory<JdbcResource>
            implements JdbcResource.NameFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<JdbcResource>
            implements JdbcResource.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/jdbc/resource"
                        );
                    }
                }
            }
        }

    @Component
    public static class IvoaFactories
        {

        @Component
        public static class ColumnFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<IvoaColumn>
            implements IvoaColumn.IdentFactory
                {
                }
            @Component
            public static class NameFactory
            extends MockNameFactory<IvoaColumn>
            implements IvoaColumn.NameFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<IvoaColumn>
            implements IvoaColumn.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/ivoa/column"
                        );
                    }
                }
            }

        @Component
        public static class TableFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<IvoaTable>
            implements IvoaTable.IdentFactory
                {
                }
/*
 * 
            @Component
            public static class NameFactory
            extends MockNameFactory<IvoaTable>
            implements IvoaTable.NameFactory
                {
                }
 * 
 */

            @Component
            public static class LinkFactory
            extends MockLinkFactory<IvoaTable>
            implements IvoaTable.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/ivoa/table"
                        );
                    }
                }
            }

        @Component
        public static class SchemaFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<IvoaSchema>
            implements IvoaSchema.IdentFactory
                {
                }
            @Component
            public static class NameFactory
            extends MockNameFactory<IvoaSchema>
            implements IvoaSchema.NameFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<IvoaSchema>
            implements IvoaSchema.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/ivoa/schema"
                        );
                    }
                }
            }

        @Component
        public static class ResourceFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<IvoaResource>
            implements IvoaResource.IdentFactory
                {
                }
            @Component
            public static class NameFactory
            extends MockNameFactory<IvoaResource>
            implements IvoaResource.NameFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<IvoaResource>
            implements IvoaResource.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/ivoa/resource"
                        );
                    }
                }
            }
        }

    @Component
    public static class OgsaFactories
        {

        @Component
        public static class ServiceFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<OgsaService>
            implements OgsaService.IdentFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<OgsaService>
            implements OgsaService.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/ogsa/service"
                        );
                    }
                }
            @Component
            public static class NameFactory
            extends DateNameFactory<OgsaService>
            implements OgsaService.NameFactory
                {
                @Override
                public String name()
                    {
                    return datename(
                        "OGSA_SERVICE"
                        );
                    }
                }
            }

        @Component
        public static class ResourceFactories
            {

            @Component
            public static class ExecResourceFactories
                {
                @Component
                public static class IdentFactory
                extends AbstractIdentFactory<OgsaExecResource>
                implements OgsaExecResource.IdentFactory
                    {
                    }
                @Component
                public static class LinkFactory
                extends MockLinkFactory<OgsaExecResource>
                implements OgsaExecResource.LinkFactory
                    {
                    public LinkFactory()
                        {
                        super(
                            "/ogsa/exec"
                            );
                        }
                    }
                }
            
            @Component
            public static class JdbcResourceFactories
                {
                @Component
                public static class IdentFactory
                extends AbstractIdentFactory<OgsaJdbcResource>
                implements OgsaJdbcResource.IdentFactory
                    {
                    }
                @Component
                public static class NameFactory
                extends MockNameFactory<OgsaJdbcResource>
                implements OgsaJdbcResource.NameFactory
                    {
                    }
                @Component
                public static class LinkFactory
                extends MockLinkFactory<OgsaJdbcResource>
                implements OgsaJdbcResource.LinkFactory
                    {
                    public LinkFactory()
                        {
                        super(
                            "/ogsa/jdbc"
                            );
                        }
                    }
                }

            @Component
            public static class IvoaResourceFactories
                {
                @Component
                public static class IdentFactory
                extends AbstractIdentFactory<OgsaIvoaResource>
                implements OgsaIvoaResource.IdentFactory
                    {
                    }
                @Component
                public static class LinkFactory
                extends MockLinkFactory<OgsaIvoaResource>
                implements OgsaIvoaResource.LinkFactory
                    {
                    public LinkFactory()
                        {
                        super(
                            "/ogsa/ivoa"
                            );
                        }
                    }
                }
            }
        }
    
    @Component
    public static class BlueFactories
        {
        @Component
        public static class QueryFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<BlueQuery>
            implements BlueQuery.IdentFactory
                {
                }
            @Component
            public static class NameFactory
            extends MockNameFactory<BlueQuery>
            implements BlueQuery.NameFactory
                {
                }
            @Component
            public static class LinkFactory
            extends MockLinkFactory<BlueQuery>
            implements BlueQuery.LinkFactory
                {
                public LinkFactory()
                	{
                	super(
            			"/blue/query"
            			);
                	}
                @Override
                public String callback(BlueQuery query)
                    {
                    return "callback:" + query.ident();
                    }
                }
            }
        }
    }

