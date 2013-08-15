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

import java.util.UUID;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.config.ConfigProperty;
import uk.ac.roe.wfau.firethorn.entity.AbstractIdentFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractLinkFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Community;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.test.TestJob;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseNameFactory;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaColumn;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaSchema;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

@Component
public class TestFactories
    {

    @Component
    public static class JobFactories
        {
        @Component
        public static class BaseFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements Job.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class TestJobFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements TestJob.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
            extends AbstractIdentFactory
            implements Identity.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class CommunityFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements Community.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class OperationFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements Operation.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class AuthenticationFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements Authentication.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
            extends AbstractIdentFactory
            implements ConfigProperty.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }
        }

    @Component
    public static class BaseFactories
        {

        @Component
        public static class TableFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements BaseTable.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends AbstractLinkFactory<BaseTable<?,?>>
            implements BaseTable.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/base/table"
                        );
                    }
                }
            }

        @Component
        public static class SchemaFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements BaseSchema.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends AbstractLinkFactory<BaseSchema<?,?>>
            implements BaseSchema.LinkFactory
                {
                public LinkFactory()
                    {
                    super(
                        "/base/schema"
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
            public static class NameFactory
            extends BaseNameFactory<AdqlQuery>
            implements AdqlQuery.NameFactory
                {
                public NameFactory()
                    {
                    }
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
            extends AbstractIdentFactory
            implements AdqlQuery.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class ColumnFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements AdqlColumn.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class TableFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements AdqlTable.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class SchemaFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements AdqlSchema.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class ResourceFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements AdqlResource.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
            extends AbstractIdentFactory
            implements JdbcColumn.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class TableFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements JdbcTable.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class SchemaFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements JdbcSchema.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class ResourceFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements JdbcResource.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
            extends AbstractIdentFactory
            implements IvoaColumn.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class TableFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements IvoaTable.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class SchemaFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements IvoaSchema.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }

        @Component
        public static class ResourceFactories
            {
            @Component
            public static class IdentFactory
            extends AbstractIdentFactory
            implements IvoaResource.IdentFactory
                {
                public IdentFactory()
                    {
                    }
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
                }
            }
        }
    }

