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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Mode;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.SelectField;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InternalServerErrorException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InvalidRequestException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.TreeComponent;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;


/**
 *
 *
 */
@Slf4j
@Ignore
public class AbstractQueryTestBase
extends TestPropertiesBase
    {

    /**
     * Our JDBC resources.
     * 
     */
    private Map<String, JdbcResource> jdbc = new HashMap<String, JdbcResource>();  
    
    /**
     * Our IVOA resources.
     * 
     */
    private Map<String, IvoaResource> ivoa = new HashMap<String, IvoaResource>();  

    /**
     * Our ADQL resources.
     * 
     */
    private Map<String, AdqlResource> adql = new HashMap<String, AdqlResource>();  

    /**
     * Load a JDBC resource.
     * 
     */
    protected JdbcResource jdbcResource(final String tag)
        {
        return jdbc.get(tag);
        }

    /**
     * Load a {@link JdbcResource}.
     * @throws IdentifierNotFoundException 
     * 
     */
    protected JdbcResource jdbcResource(final String tag, final String catalog, final String name, final String url, final String user, final String pass, final String driver)
    throws IdentifierNotFoundException
        {
        JdbcResource found = jdbc.get(
            tag
            );
        if (found == null)
            {
            final String ident = testprops().getProperty(tag);
            log.debug("Loading JDBC resource [{}][{}]", tag, ident);
            if (ident != null)
                {
                found = factories().jdbc().resources().entities().search(
                    factories().jdbc().resources().idents().ident(
                        ident
                        )
                    );
                }
            if (found == null)
                {
                log.debug("Creating new JDBC resource [{}]", tag);
                found = factories().jdbc().resources().entities().create(
                    catalog,
                    name,
                    url,
                    user,
                    pass,
                    driver
                    );
                }
            jdbc.put(
                tag,
                found
                );
            }
        return found ;
        }
    
    /**
     * Load an {@link IvoaResource}.
     * 
     */
    protected IvoaResource ivoaResource(final String tag)
        {
        return ivoa.get(tag);
        }

    /**
     * Load an {@link IvoaResource}.
     * 
     */
    protected IvoaResource ivoaResource(final String tag, final String name, final String endpoint)
        {
        IvoaResource found = ivoa.get(
            tag
            );
        if (found == null)
            {
            final String ident = testprops().getProperty(tag);
            log.debug("Loading IVOA resource [{}][{}]", tag, ident);
            if (ident != null)
                {
                found = factories().ivoa().resources().entities().search(
                    factories().ivoa().resources().idents().ident(
                        ident
                        )
                    );
                }
            if (found == null)
                {
                log.debug("Creating new IVOA resource [{}]", tag);
                found = factories().ivoa().resources().entities().create(
                    name,
                    endpoint
                    );
                }
            ivoa.put(
                tag,
                found
                );
            }
        return found ;
        }

    /**
     * Save an {@link IvoaResource}.
     * 
     */
    protected void ivoaResource(final String tag, final IvoaResource resource)
        {
        ivoa.put(
            tag,
            resource
            );
        }
    

    /**
     * Load an {@link AdqlResource}.
     * 
     */
    protected AdqlResource adqlResource(final String tag)
        {
        return adql.get(tag);
        }

    /**
     * Load an {@link AdqlResource}.
     * 
     */
    protected AdqlResource adqlResource(final String tag, final String name)
    throws Exception
        {
        AdqlResource found = adql.get(
            tag
            );
        if (found == null)
            {
            final String ident = testprops().getProperty(tag);
            log.debug("Loading ADQL resource [{}][{}]", tag, ident);
            if (ident != null)
                {
                found = factories().adql().resources().entities().search(
                    factories().adql().resources().idents().ident(
                        ident
                        )
                    );
                }
            if (found == null)
                {
                log.debug("Creating new ADQL resource [{}]", tag);
                found = factories().adql().resources().entities().create(
                    name
                    );
                }
            adql.put(
                tag,
                found
                );
            }
        return found ;
        }
    
    /**
     * Save our test resources.
     *
     */
    @After
    public void saveResources()
        {
        for (Map.Entry<String, JdbcResource> entry : jdbc.entrySet())
            {
            testprops().setProperty(
                entry.getKey(), 
                entry.getValue().ident().toString()
                );
            }

        for (Map.Entry<String, IvoaResource> entry : ivoa.entrySet())
            {
            testprops().setProperty(
                entry.getKey(), 
                entry.getValue().ident().toString()
                );
            }
        
        for (Map.Entry<String, AdqlResource> entry : adql.entrySet())
            {
            testprops().setProperty(
                entry.getKey(), 
                entry.getValue().ident().toString()
                );
            }
        }
    
    /**
     * Our target query resource.
     * 
     */
    private AdqlResource testspace ;

    /**
     * Our target query resource.
     * 
     */
    public AdqlResource testspace()
        {
        return this.testspace;
        }

    /**
     * Our target query schema.
     * 
     */
    private AdqlSchema testschema;

    /**
     * Our target query schema.
     * 
     */
    public AdqlSchema testschema()
        {
        return this.testschema;
        }
    
    /**
     * Load our test resources.
     * 
     */
    @Before
    public void loadResources()
    throws Exception
        {
        log.debug("loadResources()");
        if (testspace == null)
            {
            testspace = adqlResource(
                "test.adql.space"
                );
            }
        if (testspace == null)
            {
            testspace = adqlResource(
                "test.adql.space",
                "test.adql.space"
                );
            }

        if (testschema == null)
            {
            testschema = testspace.schemas().search(
                "testschema"
                );  
            }
        if (testschema == null)
            {
            testschema = testspace.schemas().create(
                "testschema"
                );  
            }
        }

    /**
     * Load a {@link JdbcSchema} into an {@link AdqlResource}.
     * 
     * @param parent The AdqlResource to load the AdqlSchema from.
     * @param source The JdbcResource to load the JdbcSchema from.
     * @param name The name for both the AdqlSchema and JdbcSchema catalog.
     * @return The AdqlSchema.
     * @throws NameNotFoundException
     *
    protected AdqlSchema testSchema(AdqlResource parent, JdbcResource source, String name)
    throws NameNotFoundException
        {
        return testSchema(
            parent, 
            source,
            name,
            name,
            DEFAULT_JDBC_SCHEMA
            );
        }
     */

    /**
     * Load a {@link IvoaSchema} into an {@link AdqlResource}.
     * 
     * @param parent The AdqlResource to load the AdqlSchema from.
     * @param source The IvoaResource to load the IvoaSchema from.
     * @param name The name for both the AdqlSchema and IvoaSchema.
     * @return The AdqlSchema.
     * @throws NameNotFoundException
     *
    protected AdqlSchema testSchema(AdqlResource parent, IvoaResource source, String name)
    throws NameNotFoundException
        {
        return testSchema(
            parent, 
            source,
            name,
            name,
            DEFAULT_IVOA_SCHEMA
            );
        }
     */
    
    /**
     * Load a {@link JdbcSchema} into an {@link AdqlResource}.
     * 
     * @param parent The AdqlResource to load the AdqlSchema from.
     * @param source The JdbcResource to load the JdbcSchema from.
     * @param adql The name for the AdqlSchema.
     * @param catalog The JdbcSchema catalog name.
     * @param schema  The JdbcSchema schema name.
     * @return The AdqlSchema.
     * @throws NameNotFoundException
     *
     */
    protected AdqlSchema testSchema(final AdqlResource parent, final JdbcResource source, final String adql, final String catalog, final String schema)
    throws NameNotFoundException
        {
        AdqlSchema found = testspace().schemas().search(adql); 
        if (found == null)
            {
            found = testspace().schemas().create(
                TreeComponent.CopyDepth.THIN,        
                adql,
                adqlSchema(
                    parent,
                    source,
                    adql,
                    catalog,
                    schema
                    )
                );
            }
        return found ;
        }

    /**
     * Load a {@link IvoaSchema} into an {@link AdqlResource}.
     * 
     * @param parent The AdqlResource to load the AdqlSchema from.
     * @param source The IvoaResource to load the IvoaSchema from.
     * @param adql The name for the AdqlSchema.
     * @param schema  The IvoaSchema schema name.
     * @return The AdqlSchema.
     * @throws NameNotFoundException
     *
     */
    protected AdqlSchema testSchema(final AdqlResource parent, final IvoaResource source, final String adql, final String schema)
    throws NameNotFoundException
        {
        AdqlSchema found = testspace().schemas().search(adql); 
        if (found == null)
            {
            found = testspace().schemas().create(
                TreeComponent.CopyDepth.THIN,        
                adql,
                adqlSchema(
                    parent,
                    source,
                    adql,
                    schema
                    )
                );
            }
        return found ;
        }

    /**
     * Load a {@link JdbcSchema} into an {@link AdqlResource}.
     * 
     * @param parent The AdqlResource to load the AdqlSchema into.
     * @param source The JdbcResource to load the JdbcSchema from.
     * @param adql The AdqlSchema name.
     * @param catalog The JdbcSchema catalog name.
     * @param schema  The JdbcSchema schema name.
     * @return The AdqlSchema.
     * @throws NameNotFoundException
     *
     */
    private AdqlSchema adqlSchema(final AdqlResource parent, final JdbcResource source, final String adql, final String catalog, final String schema)
    throws NameNotFoundException
        {
        AdqlSchema found = parent.schemas().search(
            adql
            ); 
        if (found == null)
            {
// Changed to use 'catalog.schema' rather than 'catalog' and 'schema'.
// Still not ..
            found = parent.schemas().create(
                TreeComponent.CopyDepth.THIN,        
                adql,
                source.schemas().select(
                    catalog,
                    schema
                    )
                );
            }
        return found ;
        }

    /**
     * Load a {@link IvoaSchema} into an {@link AdqlResource}.
     * 
     * @param parent The AdqlResource to load the AdqlSchema into.
     * @param source The IvoaResource to load the IvoaSchema from.
     * @param adql The name for the AdqlSchema.
     * @param schema  The IvoaSchema schema name.
     * @return The AdqlSchema.
     * @throws NameNotFoundException
     *
     */
    private AdqlSchema adqlSchema(final AdqlResource parent, final IvoaResource source, final String adql, final String schema)
    throws NameNotFoundException
        {
        AdqlSchema found = parent.schemas().search(
            adql
            ); 
        if (found == null)
            {
            found = parent.schemas().create(
                TreeComponent.CopyDepth.THIN,        
                adql,
                source.schemas().select(
                    schema
                    )
                );
            }
        return found ;
        }
    
    
    /**
     * Check the expected state.
     *
     */
    public void validate(final BlueQuery query, final AdqlQueryBase.Syntax.State status)
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
    implements BlueQuery.SelectField
        {
        public ExpectedField(final String name, final AdqlColumn.AdqlType type, final Integer size)
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
        private final AdqlColumn.AdqlType type;
        @Override
        public  AdqlColumn.AdqlType type()
            {
            return this.type;
            }
        private final String name ;
        @Override
        public String name()
            {
            return this.name;
            }
        void validate(final BlueQuery.SelectField field)
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

    public void validate(final BlueQuery query, final ExpectedField[] fields)
        {
        final Iterator<BlueQuery.SelectField> iter = query.fields().select().iterator();
        int i = 0 ;
        while (iter.hasNext())
            {
            final BlueQuery.SelectField field = iter.next();
            log.debug("Field [{}][{}][{}]", field.name(), field.type(), field.arraysize());
            if ((i+1)<=fields.length){
	            fields[i++].validate(
	                field
	                );
	            }
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
        final String s9 = s8.replaceAll("\\s+","");
        return s9;
        }

    private Map<String, String> replacements = new HashMap<String, String>(); 

    protected void replace(final String match, final String value)
        {
        replacements.put(
            match,
            value
            );
        }

    protected String expand(final String input)
        {
        String result = input ;
        for (Map.Entry<String, String> entry : replacements.entrySet())
            {
            result = result.replace(
                entry.getKey(),
                entry.getValue()
                );
            }
        return result ;
        }
    
    public void validate(final BlueQuery query, final String input)
        {
        log.debug("---- SQL ----");
        log.debug(query.osql());
        log.debug("---- SQL ----");

        assertEquals(
            clean(
                expand(
                    input
                    )
                ),
            clean(
                query.osql()
                )
            );
        }

    public BlueQuery validate(final BlueQuery.Mode mode, final Level level, final AdqlQueryBase.Syntax.State state, final String adql, final String sql, final ExpectedField[] fields)
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        final BlueQuery query = testspace().blues().create(
            adql,
            mode,
            level,
            null,
            null,
            null,
            null
            );
        if (state != null)
            {
            validate(query, state);
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

    public BlueQuery validate(final Level level, final AdqlQueryBase.Syntax.State state, final String adql, final String sql, final ExpectedField[] fields)
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        return validate(
            Mode.AUTO,
            level,
            state,
            adql,
            sql,
            fields
            );
        }

    public BlueQuery validate(final String adql, final String sql, final ExpectedField[] fields)
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        return validate(
            Mode.AUTO,
            Level.LEGACY,
            State.VALID,
            adql,
            sql,
            fields
            );
        }
    
    public BlueQuery validate(final Level level, final AdqlQueryBase.Syntax.State status, final String adql)
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        return validate(
            Mode.AUTO,
            level,
            status,
            adql,
            null,
            null
            );
        }
    
    /**
     * Debug display of a query.
     *
     */
    public void debug(final BlueQuery query)
        {
        log.debug("Query -- ");
        log.debug("Mode   [{}]", query.mode());
        log.debug("State  [{}]", query.state());
        log.debug("ADQL   [{}]", query.adql());
        log.debug("OSQL   [{}]", query.osql());
        log.debug("Target [{}]", (query.resources().primary() != null) ? query.resources().primary().ident() : null);
        log.debug("Resources -- ");
        for (final BaseResource<?> target : query.resources().select())
            {
            log.debug("Resource [{}]", target.namebuilder());
            }
        log.debug("Tables -- ");
        for (final AdqlTable table : query.tables().select())
            {
            log.debug("Table [{}]", table.namebuilder());
            }
        log.debug("Columns -- ");
        for (final AdqlColumn column : query.columns().select())
            {
            log.debug("Column [{}]", column.namebuilder());
            }
        log.debug("Fields -- ");
        for (final SelectField field : query.fields().select())
            {
            log.debug("Field [{}][{}][{}]", field.name(), field.type(), field.arraysize());
            }
        }
    }

