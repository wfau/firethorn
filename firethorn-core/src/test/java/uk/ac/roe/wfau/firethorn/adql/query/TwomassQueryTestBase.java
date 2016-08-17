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

import org.junit.Before;
import org.junit.Ignore;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.SelectField;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.test.TestBase;


/**
 * Base class for tests using the TWOMAss dataset.
 * @todo refactor all the tests to use test data databases.
 *
 */
@Slf4j
@Ignore
public class TwomassQueryTestBase
extends TestBase
    {

    protected JdbcResource twomass ;
    protected AdqlSchema   schema  ;
    protected AdqlResource workspace ;

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
        this.twomass = factories().jdbc().resources().entities().create(
            "TWOMASS",
            "twomass",
            config().property(
                "firethorn.twomass.url"
                ),
            config().property(
                "firethorn.twomass.user"
                ),
            config().property(
                "firethorn.twomass.pass"
                ),
            config().property(
                "firethorn.twomass.driver"
                )
            );
        //
        // Create our ADQL workspace.
        this.workspace = factories().adql().resources().entities().create(
            "adql-workspace"
            );
        //
        // Import the JDBC tables into our workspace.
        this.schema = this.workspace.schemas().create(
            "adql_twomass"
            );
        this.schema.tables().create(
            this.twomass.schemas().select(
                "TWOMASS",
                "dbo"
                ).tables().select(
                    "twomass_psc"
                    )
            );
        }

    /**
     * Debug display of a query.
     *
     */
    public void debug(final GreenQuery query)
        {
        log.debug("Query -- ");
        log.debug("Mode   [{}]", query.mode());
        log.debug("Status [{}]", query.status());
        log.debug("ADQL   [{}]", query.adql());
        log.debug("OSQL   [{}]", query.osql());
        log.debug("Target [{}]", query.primary().ident());
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

