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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.config.ConfigProperty;
import uk.ac.roe.wfau.firethorn.entity.AbstractIdentFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractLinkFactory;
import uk.ac.roe.wfau.firethorn.entity.DateNameFactory;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.test.TestJob;
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
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaIvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaJdbcResource;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaService;

@Component
public class TestFactories
    {

    @Component
    public static class JobFactories
        {
        @Component
        public static class BaseJobFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<Job>
            implements Job.IdentFactory
                {
                }
            @Component
            public static class LinkFactory
            extends AbstractLinkFactory<Job>
            implements Job.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/job/base"
                        );
                    }
                @Override
                public Job resolve(String link)
                throws IdentifierFormatException,IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
                    }
                }
            }
        @Component
        public static class TestJobFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<TestJob>
            implements TestJob.IdentFactory
                {
                }

            @Component
            public static class LinkFactory
            extends AbstractLinkFactory<TestJob>
            implements TestJob.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/job/test"
                        );
                    }

                @Override
                public TestJob resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            public static class LinkFactory
            extends AbstractLinkFactory<Identity>
            implements Identity.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/auth/identity"
                        );
                    }

                @Override
                public Identity resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            public static class LinkFactory
            extends AbstractLinkFactory<Community>
            implements Community.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/auth/community"
                        );
                    }

                @Override
                public Community resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            extends AbstractLinkFactory<Operation>
            implements Operation.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/operation"
                        );
                    }

                @Override
                public Operation resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            extends AbstractLinkFactory<Authentication>
            implements Authentication.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/authentication"
                        );
                    }

                @Override
                public Authentication resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            extends AbstractLinkFactory<ConfigProperty>
            implements ConfigProperty.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/config/property"
                        );
                    }

                @Override
                public ConfigProperty resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            public static class NameFactory
            extends DateNameFactory<AdqlQuery>
            implements AdqlQuery.NameFactory
                {
                @Override
                public String name()
                    {
                    return datename(
                        "QUERY"
                        );
                    }
                }

            @Component
            public static class IdentFactory
            extends AbstractIdentFactory<AdqlQuery>
            implements AdqlQuery.IdentFactory
                {
                }

            @Component
            public static class LinkFactory
            extends AbstractLinkFactory<AdqlQuery>
            implements AdqlQuery.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/adql/query"
                        );
                    }

                @Override
                public AdqlQuery resolve(String link)
                    throws IdentifierFormatException,  IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            public static class LinkFactory
            extends AbstractLinkFactory<AdqlColumn>
            implements AdqlColumn.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/adql/column"
                        );
                    }

                @Override
                public AdqlColumn resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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

            @Component
            public static class LinkFactory
            extends AbstractLinkFactory<AdqlTable>
            implements AdqlTable.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/adql/table"
                        );
                    }

                @Override
                public AdqlTable resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            public static class LinkFactory
            extends AbstractLinkFactory<AdqlSchema>
            implements AdqlSchema.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/adql/schema"
                        );
                    }

                @Override
                public AdqlSchema resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            public static class LinkFactory
            extends AbstractLinkFactory<AdqlResource>
            implements AdqlResource.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/adql/resource"
                        );
                    }

                @Override
                public AdqlResource resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            public static class LinkFactory
            extends AbstractLinkFactory<JdbcColumn>
            implements JdbcColumn.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/jdbc/column"
                        );
                    }

                @Override
                public JdbcColumn resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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

            @Component
            public static class LinkFactory
            extends AbstractLinkFactory<JdbcTable>
            implements JdbcTable.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/jdbc/table"
                        );
                    }

                @Override
                public JdbcTable resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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

            @Component
            public static class LinkFactory
            extends AbstractLinkFactory<JdbcSchema>
            implements JdbcSchema.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/jdbc/schema"
                        );
                    }

                @Override
                public JdbcSchema resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            public static class LinkFactory
            extends AbstractLinkFactory<JdbcResource>
            implements JdbcResource.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/jdbc/resource"
                        );
                    }

                @Override
                public JdbcResource resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            public static class LinkFactory
            extends AbstractLinkFactory<IvoaColumn>
            implements IvoaColumn.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/ivoa/column"
                        );
                    }

                @Override
                public IvoaColumn resolve(String link)
                    throws IdentifierFormatException,IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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

            @Component
            public static class LinkFactory
            extends AbstractLinkFactory<IvoaTable>
            implements IvoaTable.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/ivoa/table"
                        );
                    }

                @Override
                public IvoaTable resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            public static class LinkFactory
            extends AbstractLinkFactory<IvoaSchema>
            implements IvoaSchema.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/ivoa/schema"
                        );
                    }

                @Override
                public IvoaSchema resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            public static class LinkFactory
            extends AbstractLinkFactory<IvoaResource>
            implements IvoaResource.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/ivoa/resource"
                        );
                    }

                @Override
                public IvoaResource resolve(String link)
                    throws IdentifierFormatException, IdentifierNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            extends AbstractLinkFactory<OgsaService>
            implements OgsaService.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/ogsa/service"
                        );
                    }

                @Override
                public OgsaService resolve(String link)
                throws IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException
                    {
                    // TODO Auto-generated method stub
                    return null;
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
            public static class JdbcResourceFactories
                {
                @Component
                public static class IdentFactory
                extends AbstractIdentFactory<OgsaJdbcResource>
                implements OgsaJdbcResource.IdentFactory
                    {
                    }
                @Component
                public static class LinkFactory
                extends AbstractLinkFactory<OgsaJdbcResource>
                implements OgsaJdbcResource.LinkFactory
                    {
                    public LinkFactory()
                        {
                        super(
                            "/ogsa/jdbc"
                            );
                        }

                    @Override
                    public OgsaJdbcResource resolve(String link)
                        throws IdentifierFormatException,
                        IdentifierNotFoundException, EntityNotFoundException
                        {
                        // TODO Auto-generated method stub
                        return null;
                        }
                    }
                @Component
                public static class NameFactory
                extends DateNameFactory<OgsaJdbcResource>
                implements OgsaJdbcResource.NameFactory
                    {
                    @Override
                    public String name()
                        {
                        return datename(
                            "OGSA_JDBC_RESOURCE"
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
                extends AbstractLinkFactory<OgsaIvoaResource>
                implements OgsaIvoaResource.LinkFactory
                    {
                    public LinkFactory()
                        {
                        super(
                            "/ogsa/ivoa"
                            );
                        }

                    @Override
                    public OgsaIvoaResource resolve(String link)
                        throws IdentifierFormatException,
                        IdentifierNotFoundException, EntityNotFoundException
                        {
                        // TODO Auto-generated method stub
                        return null;
                        }
                    }
                @Component
                public static class NameFactory
                extends DateNameFactory<OgsaIvoaResource>
                implements OgsaIvoaResource.NameFactory
                    {
                    @Override
                    public String name()
                        {
                        return datename(
                            "OGSA_IVOA_RESOURCE"
                            );
                        }
                    }
                }
            }
        }
    }

