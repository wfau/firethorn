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
package uk.ac.roe.wfau.firethorn.adql.query.atlas ;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.TestPropertiesBase;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.SelectField;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;


/**
 *
 *
 */
@Slf4j
@Ignore
public class AtlasQueryTestBase
extends TestPropertiesBase
    {

    protected static final String ATLAS_VERSION = "ATLASDR1" ;
  //protected static final String ATLAS_VERSION = "ATLASv20131127" ;
  //protected static final String ATLAS_VERSION = "ATLASv20131029" ;
  //protected static final String ATLAS_VERSION = "ATLASv20130426" ;
  //protected static final String ATLAS_VERSION = "ATLASv20130304" ;

    protected JdbcResource resource ;
    protected AdqlResource workspace ;
    protected AdqlResource testspace ;

    protected AdqlSchema queryspace ;

    public void schema()
        {

        }

    public void loadCatalog(final String name)
        {
        loadCatalog(
            name,
            name
            );
        }

    public void loadCatalog(final String name, final String alias)
        {
        try {
            this.workspace.schemas().create(
                BaseComponent.CopyDepth.THIN,
                alias,
                resource.schemas().select(
                    name,
                    "dbo"
                    )
                );
            }
        catch (final EntityNotFoundException ouch)
            {
            log.warn("Unable to load catalog [{}]", name);
            }
        }


    public void loadSchema(final String name)
        {
        loadSchema(
            name,
            name
            );
        }

    public void loadSchema(final String name, final String alias)
        {
        try {
            this.testspace.schemas().create(
                BaseComponent.CopyDepth.THIN,
                alias,
                this.workspace.schemas().select(
                    name
                    )
                );
            }
        catch (final EntityNotFoundException ouch)
            {
            log.warn("Unable to load schema [{}]", name);
            }
        }

    /**
     * Load our test resources.
     *
     */
    @Before
    public void loadResources()
    throws EntityNotFoundException
        {
        //
        // Create our JDBC resource.
        if (resource == null)
            {
            log.debug("Loading test resource");
            final String prop = testprops().getProperty("jdbc.space");
            if (prop != null)
                {
                try {
                    resource = factories().jdbc().resources().select(
                        factories().jdbc().resources().idents().ident(
                            prop
                            )
                        );
                    }
                catch (final IdentifierNotFoundException ouch)
                    {
                    log.debug("Unable to load JDBC resource from test config [{}]", ouch.getMessage());
                    }
                }
            if (resource == null)
                {
                log.debug("Null JDBC resource, creating new one");
                resource = factories().jdbc().resources().create(
                    "atlas",
                    "*",
                    "atlas",
                    "spring:RoeATLAS"
                    );
                }
            }

        //
        // Create our ADQL workspace.
        if (this.workspace == null)
            {
            log.debug("Loading test workspace");
            final String prop = testprops().getProperty("adql.space");
            if (prop != null)
                {
                try {
                    this.workspace = factories().adql().resources().select(
                        factories().adql().resources().idents().ident(
                            prop
                            )
                        );
                    }
                catch (final IdentifierNotFoundException ouch)
                    {
                    log.debug("Unable to load ADQL resource from test config [{}]", ouch.getMessage());
                    }
                }
            if (this.workspace == null)
                {
                log.debug("Null ADQL resource, creating new one");
                this.workspace = factories().adql().resources().create(
                    "workspace"
                    );

                loadCatalog(
                    ATLAS_VERSION
                    );

                loadCatalog(
                    "ROSAT"
                    );

                loadCatalog(
                    "BestDR8"
                    );

                loadCatalog(
                    "BestDR9"
                    );

                loadCatalog(
                    "TWOMASS"
                    );
                }
            }

        //
        // Create our ADQL workspace.
        if (this.testspace == null)
            {
            log.debug("Loading test schema");
            final String prop = testprops().getProperty("test.space");
            if (prop != null)
                {
                try {
                    this.testspace = factories().adql().resources().select(
                        factories().adql().schemas().idents().ident(
                            prop
                            )
                        );
                    }
                catch (final IdentifierNotFoundException ouch)
                    {
                    log.debug("Unable to load ADQL resource from test config [{}]", ouch.getMessage());
                    }
                }
            if (this.testspace == null)
                {
                this.testspace = factories().adql().resources().create(
                    "testspace"
                    );

                loadSchema(
                    ATLAS_VERSION
                    );

                loadSchema(
                    "ROSAT"
                    );

                loadSchema(
                    "BestDR8"
                    );

                loadSchema(
                    "BestDR9"
                    );

                loadSchema(
                    "TWOMASS"
                    );
                }
            }

        //
        // Create our ADQL query space.
        if (this.queryspace == null)
            {
            log.debug("Loading test schema");
            this.queryspace = this.testspace.schemas().search(
                "queryspace"
                );
            if (this.queryspace == null)
                {
                this.queryspace = this.testspace.schemas().create(
                    "queryspace"
                    );
                }
            }
        }

    /**
     * Save our test resources.
     *
     */
    @After
    public void saveResources()
        {
        log.debug("Saving test resources");
        if (this.resource != null)
            {
            testprops().setProperty("jdbc.space", this.resource.ident().toString());
            }
        if (this.workspace != null)
            {
            testprops().setProperty("adql.space", this.workspace.ident().toString());
            }
        if (this.testspace != null)
            {
            testprops().setProperty("test.space", this.testspace.ident().toString());
            }
        }

    public void validate(final AdqlQuery query, final AdqlQuery.Syntax.State status)
        {
        assertEquals(
            status,
            query.syntax().state()
            );
        }

    /**
     * An expected result.
     *
     */
    public static class ExpectedField
    implements AdqlQuery.SelectField
        {
        public ExpectedField(final String name, final AdqlColumn.Type type, final Integer size)
            {
            this.name = name ;
            this.type = type ;
            this.size = size ;
            }
        private final Integer size ;
        @Override
        public Integer arraysize()
            {
            return this.size;
            }
        private final AdqlColumn.Type type;
        @Override
        public  AdqlColumn.Type type()
            {
            return this.type;
            }
        private final String name ;
        @Override
        public String name()
            {
            return this.name;
            }
        void validate(final AdqlQuery.SelectField field)
            {
            log.debug("validate(SelectField)");
            log.debug("  name [{}][{}]", this.name, field.name());
            log.debug("  size [{}][{}]", this.size, field.arraysize());
            log.debug("  type [{}][{}]", this.type, field.type());
            assertEquals(
                this.name,
                field.name()
                ) ;
            assertEquals(
                this.size,
                field.arraysize()
                ) ;
            assertEquals(
                this.type,
                field.type()
                ) ;
            }
        }

    public void validate(final AdqlQuery query, final ExpectedField[] fields)
        {
        final Iterator<AdqlQuery.SelectField> iter = query.fields().iterator();
        int i = 0 ;
        while (iter.hasNext())
            {
            final AdqlQuery.SelectField field = iter.next();
            log.debug("Field [{}][{}][{}]", field.name(), field.type(), field.arraysize());
            fields[i++].validate(
                field
                );
            }
        assertEquals(
            i,
            fields.length
            );
        }

    /**
     * Clean a query string.
     *
     */
    protected String clean(final String s1)
        {
        if (s1 == null)
            {
            return  null ;
            }

        final String s2 = StringUtils.trim(s1);
        final String s3 = s2.toLowerCase();
        final String s4 = s3.replaceAll("[\n\r]+", " ");
        final String s5 = s4.replaceAll("\\p{Space}+", " ");
        final String s6 = s5.replaceAll(" *, *", ",");
        final String s7 = s6.replaceAll(" *\\( *", "(");
        final String s8 = s7.replaceAll(" *\\) *", ")");

        return s8;
        }

    public void validate(final AdqlQuery query, final String sql)
        {
        assertEquals(
            clean(
                sql.replace("{ATLAS_VERSION}", ATLAS_VERSION)
                ),
            clean(
                query.osql()
                )
            );
        }

/*
    public void validate(final AdqlQuery query, final AdqlQuery.Syntax.State status, final ExpectedField[] fields, final String sql)
        {
        validate(query, status);
        validate(query, fields);
        validate(query, sql);
        }
 */

    public AdqlQuery validate(final Level level, final AdqlQuery.Syntax.State status, final String adql, final String sql, final ExpectedField[] fields)
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                level
                ),
            adql
            );
        if (status != null)
            {
            validate(query, status);
            }
        if (fields != null)
            {
            validate(query, fields);
            }
        if (sql != null)
            {
            validate(query, sql);
            }
        return query;
        }

    public AdqlQuery validate(final AdqlQuery.Syntax.State status, final String adql, final String sql, final ExpectedField[] fields)
        {
        return validate(Level.STRICT, status, adql, sql, fields);
        }

    public AdqlQuery validate(final String adql, final String sql, final ExpectedField[] fields)
        {
        return validate(Level.STRICT, AdqlQuery.Syntax.State.VALID, adql, sql, fields);
        }

    public AdqlQuery validate(final String adql, final String sql)
        {
        return validate(Level.STRICT, AdqlQuery.Syntax.State.VALID, adql, sql, null);
        }

    public AdqlQuery validate(final Level level, final AdqlQuery.Syntax.State status, final String adql, final String sql)
        {
        return validate(level, status, adql, sql, null);
        }

    public AdqlQuery validate(final Level level, final AdqlQuery.Syntax.State status, final String adql)
        {
        return validate(level, status, adql, null, null);
        }

    /**
     * Dummy test for Eclipse.
     *
     */
    @Test
    public void notest()
    throws Exception
        {
        log.debug("---- No test ----");
        }

    /**
     * Debug display of a query.
     *
     */
    public void debug(final AdqlQuery query)
        {
        log.debug("Query -- ");
        log.debug("Mode   [{}]", query.mode());
        log.debug("Status [{}]", query.status());
        log.debug("ADQL   [{}]", query.adql());
        log.debug("OSQL   [{}]", query.osql());
        log.debug("Target [{}]", (query.primary() != null) ? query.primary().ident() : null);
        log.debug("Resources -- ");
        for (final BaseResource<?> target : query.resources())
            {
            log.debug("Resource [{}]", target.namebuilder());
            }
        log.debug("Tables -- ");
        for (final AdqlTable table : query.tables())
            {
            log.debug("Table [{}]", table.namebuilder());
            }
        log.debug("Columns -- ");
        for (final AdqlColumn column : query.columns())
            {
            log.debug("Column [{}]", column.namebuilder());
            }
        log.debug("Fields -- ");
        for (final SelectField field : query.fields())
            {
            log.debug("Field [{}][{}][{}]", field.name(), field.type(), field.arraysize());
            }
        }

    }

