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
package uk.ac.roe.wfau.firethorn.adql.query ;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Ignore;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.SelectField;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.test.TestBase;


/**
 *
 *
 */
@Slf4j
@Ignore
public class AtlasQueryTestBase
extends TestBase
    {

    protected static JdbcResource resource  ;
    protected AdqlResource workspace ;
    protected AdqlSchema   schema ;

    /**
     * Create our resources.
     *
     */
    @Before
    public void init()
    throws Exception
        {
        //
        // Create our JDBC resources.
        if (resource == null)
            {
            resource = factories().jdbc().resources().create(
                "atlas",
                "ATLASv20130426",
                "atlas",
                "spring:RoeATLAS"
                );
            }
        //
        // Create our ADQL workspace.
        this.workspace = factories().adql().resources().create(
            "workspace"
            );
        //
        // Import the tables into our workspace.
        this.schema = this.workspace.schemas().create(
            resource.schemas().select(
                "ATLASv20130426",
                "dbo"
                )
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
        @Override
        public AdqlColumn adql()
            {
            return null;
            }
        @Override
        public JdbcColumn jdbc()
            {
            return null;
            }
        }

    public void validate(final AdqlQuery query, final ExpectedField[] results)
    throws Exception
        {
        final Iterator<AdqlQuery.SelectField> iter = query.fields().iterator();
        int i = 0 ;
        while (iter.hasNext())
            {
            final AdqlQuery.SelectField field = iter.next();
            log.debug("Field [{}][{}][{}]", field.name(), field.type(), field.arraysize());
            results[i++].validate(
                field
                );
            }
        assertEquals(
            i,
            results.length
            );
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

    /**
     * Clean a query string.
     * 
     */
    protected String clean(String s1)
        {
        if (s1 == null)
            {
            return  null ;
            }

        final String s2 = s1.replaceAll("\\p{Space}+", " ");
        return s2;
        }

    /**
     * Compare an ADQL query and the resulting SQL output.
     * 
     */
    public void compare(final String adql, final String osql)
    throws Exception
        {
        final AdqlQuery query = this.schema.queries().create(
            adql
            );
        debug(
            query
            );
        assertEquals(
            clean(
                osql
                ),
            clean(
                query.osql()
                )
            );
        }
    }

