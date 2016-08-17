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
import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;


/**
 *
 *
 */
@Slf4j
public class QuerySelectFieldTestBase
extends TwomassQueryTestBase
    {

    /**
     * An expected result.
     *
     */
    public static class ExpectedField
    implements GreenQuery.SelectField
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
        void validate(final GreenQuery.SelectField field)
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

    public void validate(final GreenQuery query, final ExpectedField[] results)
    throws Exception
        {
        final Iterator<GreenQuery.SelectField> iter = query.fields().iterator();
        int i = 0 ;
        while (iter.hasNext())
            {
            final GreenQuery.SelectField field = iter.next();
            log.debug("Field [{}][{}][{}]", field.name(), field.type(), field.arraysize());
            results[i++].validate(
                field
                );
            }
        assertEquals(
            results.length,
            i
            );
        }
    }

