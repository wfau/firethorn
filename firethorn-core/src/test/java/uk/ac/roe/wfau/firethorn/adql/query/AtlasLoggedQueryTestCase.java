/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.query;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 */
@Slf4j
public class AtlasLoggedQueryTestCase
    extends AtlasQueryTestBase
    {

    @Test
    public void test000()
    throws Exception
        {
        final BufferedReader reader = new BufferedReader(
            new FileReader(
                "src/test/data/adql/atlas-logged-queries-000.txt"
                )
            );

        final String linestr = reader.readLine();
        for (int linenum = 1; (linestr != null) ; linenum++)
            {
            inner(linenum, linestr);
            }
        }

    @Transactional(
        readOnly=false,
        propagation=Propagation.REQUIRES_NEW
        )

    public void inner(final int linenum, final String linestr)
    throws Exception
        {
        log.debug("--------");
        log.debug("ADQL [{}][{}]", linenum, linestr);

        final AdqlQuery query = this.queryschema.queries().create(
            linestr
            );

        final String s1 = clean(linestr);
        final String s2 = clean(query.adql());
        final String s3 = clean(query.osql());

        log.debug("LINE [{}][{}]", linenum, s1);
        log.debug("ADQL [{}][{}]", linenum, s2);
        log.debug("OSQL [{}][{}]", linenum, s3);

        final String diff = StringUtils.difference(
            s1,
            s3
            );

        log.debug("DELTA [{}]", diff);
        factories().hibernate().flush();
        //factories().hibernate().clear();

        }
    }
