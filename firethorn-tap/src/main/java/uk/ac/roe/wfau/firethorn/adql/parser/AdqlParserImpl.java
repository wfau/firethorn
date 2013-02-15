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
package uk.ac.roe.wfau.firethorn.adql.parser;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserTable.AdqlDBColumn;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuerySyntax;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import adql.db.DBChecker;
import adql.db.DBTable;
import adql.parser.ADQLParser;
import adql.parser.ParseException;
import adql.query.ADQLObject;
import adql.query.ADQLQuery;
import adql.query.from.ADQLTable;
import adql.query.operand.ADQLColumn;
import adql.translator.ADQLTranslator;
import adql.translator.PostgreSQLTranslator;
import adql.translator.TranslationException;

/**
 * ADQL parser implementation.
 *
 */
@Slf4j
public class AdqlParserImpl
implements AdqlParser
    {

    /**
     * Factory implementation.
     *
     */
    @Repository
    public static class Factory
    implements AdqlParser.Factory
        {
        @Override
        public AdqlParser create(final AdqlQuery.Mode mode, final AdqlResource workspace)
            {
            return new AdqlParserImpl(
                this.tables,
                mode,
                workspace
                );
            }

        /**
         * Autowired reference to the local table factory.
         *
         */
        @Autowired
        private AdqlParserTable.Factory tables;

        }

    /**
     * Protected constructor.
     *
     */
    protected AdqlParserImpl(final AdqlParserTable.Factory factory, final AdqlQuery.Mode mode, final AdqlResource workspace)
        {
        this.mode = mode ;

        final Set<DBTable> tables = new HashSet<DBTable>();
        for (final AdqlSchema schema : workspace.schemas().select())
            {
            for (final AdqlTable table : schema.tables().select())
                {
                tables.add(
                    factory.create(
                        this.mode,
                        table
                        )
                    );
                }
            }
        //
        // Create our ADQL parser:
        this.parser = new ADQLParser();
        //
        // Add a DBChecker using the DBTables.
        this.parser.setQueryChecker(
            new DBChecker(
                tables
                )
            );
        }

    protected AdqlQuery.Mode mode ;

    protected ADQLParser parser ;

    @Override
    public void process(final AdqlParserQuery subject)
        {
        //
        // Parse the query.
        try {
            final ADQLQuery object = this.parser.parseQuery(
                subject.input()
                );
            //
            // Reset the query state.
            subject.reset(
                this.mode
                );
            //
            // Update the processed ADQL.
            subject.adql(
                object.toADQL()
                );
            //
            // Process the query components.
            process(
                subject,
                object
                );

            // IF we are single resource .. then translate into the resource dialect ?

            //
            // Translate the query into SQL.
            final ADQLTranslator translator = new PostgreSQLTranslator(false);
            subject.osql(
                translator.translate(
                    object
                    )
                );
            //
            // If we got this far, then the query is valid.
            subject.syntax(
                AdqlQuerySyntax.Status.VALID
                );
            }
        catch (final ParseException ouch)
            {
            subject.syntax(
                AdqlQuerySyntax.Status.PARSE_ERROR,
                ouch.getMessage()
                );
            log.warn("Error parsing query [{}]", ouch.getMessage());
            }
        catch (final TranslationException ouch)
            {
            subject.syntax(
                AdqlQuerySyntax.Status.TRANS_ERROR,
                ouch.getMessage()
                );
            log.warn("Error translating query [{}]", ouch.getMessage());
            }
        }

    /**
     * Wrap an ADQLObject as an Iterable.
     *
     */
    public Iterable<ADQLObject> iter(final ADQLObject object)
        {
        return new Iterable<ADQLObject>()
            {
            @Override
            public Iterator<ADQLObject> iterator()
                {
                return object.adqlIterator();
                }
            };
        }

    /**
     * Process an ADQL object.
     *
     */
    public void process(final AdqlParserQuery subject, final ADQLObject object)
        {
        process(
            subject,
            iter(
                object
                )
            );
        }

    /**
     * Process a set of ADQL objects.
     *
     */
    public void process(final AdqlParserQuery subject, final Iterable<ADQLObject> objects)
        {
        for (final ADQLObject object: objects)
            {
            log.debug("----");
            log.debug("ADQLObject [{}]", object.getClass().getName());

            if (object instanceof ADQLColumn)
                {
                log.debug("  ----");
                log.debug("  ADQLColumn [{}]", ((ADQLColumn) object).getName());
                if (((ADQLColumn) object).getDBLink() instanceof AdqlDBColumn)
                    {
                    final AdqlColumn adql = ((AdqlDBColumn) ((ADQLColumn) object).getDBLink()).column();
                    log.debug("  ----");
                    log.debug("  AdqlColumn [{}]", adql.fullname());
                    log.debug("  BaseColumn [{}]", adql.base().fullname());
                    subject.add(
                        adql
                        );
                    }
                }

            else if (object instanceof ADQLTable)
                {
                log.debug("  ----");
                log.debug("  ADQLTable [{}]", ((ADQLTable) object).getName());
                if (((ADQLTable) object).getDBLink() instanceof AdqlParserTable)
                    {
                    final AdqlTable adql = ((AdqlParserTable) ((ADQLTable) object).getDBLink()).table();
                    log.debug("  ----");
                    log.debug("  AdqlTable [{}]", adql.fullname());
                    log.debug("  BaseTable [{}]", adql.base().fullname());
                    subject.add(
                        adql
                        );
                    }
                }

            else {
                process(
                    subject,
                    object
                    );
                }
            }
        }
    }
