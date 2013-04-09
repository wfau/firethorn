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

import uk.ac.roe.wfau.firethorn.config.ConfigProperty;
import uk.ac.roe.wfau.firethorn.entity.EntityIdentFactory;
import uk.ac.roe.wfau.firethorn.entity.EntityLinkFactory;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.test.TestJob;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
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
            extends EntityIdentFactory
            implements Job.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<Job>
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
            extends EntityIdentFactory
            implements TestJob.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<TestJob>
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
            extends EntityIdentFactory
            implements Identity.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<Identity>
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
        }

    @Component
    public static class ConfigFactories
        {
        @Component
        public static class PropertyFactories
            {
            @Component
            public static class IdentFactory
            extends EntityIdentFactory
            implements ConfigProperty.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<ConfigProperty>
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
            extends EntityIdentFactory
            implements BaseTable.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<BaseTable<?,?>>
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
            extends EntityIdentFactory
            implements BaseSchema.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<BaseSchema<?,?>>
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
            implements AdqlQuery.NameFactory
                {
                public NameFactory()
                    {
                    }
                @Override
                public String name()
                    {
                    return name("name");
                    }

                @Override
                public String name(final String name)
                    {
                    return "test-".concat(name);
                    }
                }

            @Component
            public static class IdentFactory
            extends EntityIdentFactory
            implements AdqlQuery.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<AdqlQuery>
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
            extends EntityIdentFactory
            implements AdqlColumn.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<AdqlColumn>
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
            extends EntityIdentFactory
            implements AdqlTable.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<AdqlTable>
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
            extends EntityIdentFactory
            implements AdqlSchema.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<AdqlSchema>
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
            extends EntityIdentFactory
            implements AdqlResource.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<AdqlResource>
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
            extends EntityIdentFactory
            implements JdbcColumn.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<JdbcColumn>
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
            extends EntityIdentFactory
            implements JdbcTable.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<JdbcTable>
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
            extends EntityIdentFactory
            implements JdbcSchema.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<JdbcSchema>
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
            extends EntityIdentFactory
            implements JdbcResource.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<JdbcResource>
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
            extends EntityIdentFactory
            implements IvoaColumn.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<IvoaColumn>
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
            extends EntityIdentFactory
            implements IvoaTable.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<IvoaTable>
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
            extends EntityIdentFactory
            implements IvoaSchema.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<IvoaSchema>
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
            extends EntityIdentFactory
            implements IvoaResource.IdentFactory
                {
                public IdentFactory()
                    {
                    }
                }

            @Component
            public static class LinkFactory
            extends EntityLinkFactory<IvoaResource>
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

