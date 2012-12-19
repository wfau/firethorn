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
package uk.ac.roe.wfau.firethorn.webapp.paths;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
public class PathImplTestCase
extends TestBase
    {

    public static class AppendTest
        {
        public AppendTest(final String base, final String string, final String result)
            {
            this.base = base ;
            this.string = string ;
            this.result = result ;
            }
        public String base ;
        public String string ;
        public String result ;
        }

    public AppendTest[] appends = {

        new AppendTest("", "", ""),

        new AppendTest("here",   "", "here"),
        new AppendTest("here/",  "", "here/"),

        new AppendTest("/here",  "", "/here"),
        new AppendTest("/here/", "", "/here/"),



        new AppendTest("here",   "/", "here/"),
        new AppendTest("here/",  "/", "here//"),

        new AppendTest("/here",  "/", "/here/"),
        new AppendTest("/here/", "/", "/here//"),


        new AppendTest("here",   "there", "here/there"),
        new AppendTest("here/",  "there", "here/there"),

        new AppendTest("/here",  "there", "/here/there"),
        new AppendTest("/here/", "there", "/here/there"),


        new AppendTest("here",   "/there", "here/there"),
        new AppendTest("here/",  "/there", "here//there"),

        new AppendTest("/here",  "/there", "/here/there"),
        new AppendTest("/here/", "/there", "/here//there"),


        new AppendTest("here",   "there/", "here/there/"),
        new AppendTest("here/",  "there/", "here/there/"),

        new AppendTest("/here",  "there/", "/here/there/"),
        new AppendTest("/here/", "there/", "/here/there/"),


        new AppendTest("here",   "/there/", "here/there/"),
        new AppendTest("here/",  "/there/", "here//there/"),

        new AppendTest("/here",  "/there/", "/here/there/"),
        new AppendTest("/here/", "/there/", "/here//there/"),

        };

    @Test
    public void testAppend()
    throws Exception
        {
        for (final AppendTest test : appends)
            {
            assertEquals(
                test.result,
                new PathImpl(
                    test.base
                    ).append(
                        test.string
                        ).toString()
                );
            }
        }

    public static class TestIdent
    implements Identifier
        {
        public TestIdent(final String value)
            {
            this.value = value ;
            }
        private final String value ;
        @Override
        public Serializable value()
            {
            return value ;
            }
        }

    public static class ResolveTest
        {
        public ResolveTest(final String base, final String ident, final String result)
            {
            this(
                base,
                new TestIdent(
                    ident
                    ),
                result
                );
            }
        public ResolveTest(final String base, final Identifier ident, final String result)
            {
            this.base = base ;
            this.ident=  ident ;
            this.result = result ;
            }

        public String base ;
        public Identifier ident ;
        public String result ;
        }

    public ResolveTest[] resolves = {

        new ResolveTest("", "", ""),

        new ResolveTest("here", "00000", "here"),

        new ResolveTest("ident",   "00000", "ident"),
        new ResolveTest("{ident}", "00000", "00000"),

        new ResolveTest("here/ident",   "00000", "here/ident"),
        new ResolveTest("here/{ident}", "00000", "here/00000"),

        new ResolveTest("here/ident/there",   "00000", "here/ident/there"),
        new ResolveTest("here/{ident}/there", "00000", "here/00000/there")

        };

    @Test
    public void testResolve()
    throws Exception
        {
        for (final ResolveTest test : resolves)
            {
            assertEquals(
                test.result,
                new PathImpl(
                    test.base
                    ).resolve(
                        test.ident
                        )
                );
            }
        }
    }
