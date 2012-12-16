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
package uk.ac.roe.wfau.firethorn.adql;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.AdqlDBTable.AdqlDBColumn;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlColumn;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlQuery;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlSchema;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlTable;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayOgsaResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlQuery.Status;

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
public class AdqlDBParserImpl
implements AdqlDBParser
    {

    /**
     * Factory implementation.
     * 
     */
    @Repository
    public static class Factory
    implements AdqlDBParser.Factory
        {
        @Override
        public AdqlDBParser create(final TuesdayAdqlQuery.Mode mode, final TuesdayAdqlResource workspace)
            {
            return new AdqlDBParserImpl(
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
        private AdqlDBTable.Factory tables;

        }

    /**
     * Protected constructor.
     *
     */
    protected AdqlDBParserImpl(final AdqlDBTable.Factory factory, final TuesdayAdqlQuery.Mode mode, final TuesdayAdqlResource workspace)
        {
        this.mode = mode ;
        
        Set<DBTable> tables = new HashSet<DBTable>();
        for (TuesdayAdqlSchema schema : workspace.schemas().select())
            {
            for (TuesdayAdqlTable table : schema.tables().select())
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

    protected TuesdayAdqlQuery.Mode mode ;

    protected ADQLParser parser ;

    @Override
    public void process(final AdqlDBQuery subject)
        {
        //
        // Parse the query.
        try {
            final ADQLQuery object = this.parser.parseQuery(
                subject.input()
                );
            //
            // Update the query mode.
            subject.mode(
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
            ADQLTranslator translator = new PostgreSQLTranslator(false);
            subject.ogsa(
                translator.translate(
                    object
                    )
                );
            }
        catch (final ParseException ouch)
            {
            subject.status(Status.ERROR);
            log.warn("Error parsing query [{}]", ouch.getMessage());
            }
        catch (final TranslationException ouch)
            {
            subject.status(Status.ERROR);
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
    public void process(final AdqlDBQuery subject, final ADQLObject object)
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
    public void process(final AdqlDBQuery subject, final Iterable<ADQLObject> objects)
        {
        for (ADQLObject object: objects)
            {
            log.debug("----");
            log.debug("ADQLObject [{}]", object.getClass().getName());

            if (object instanceof ADQLColumn)
                {
                log.debug("  ----");
                log.debug("  ADQLColumn [{}]", ((ADQLColumn) object).getName());
                if (((ADQLColumn) object).getDBLink() instanceof AdqlDBColumn)
                    {
                    TuesdayAdqlColumn adql = ((AdqlDBColumn) ((ADQLColumn) object).getDBLink()).column();
                    log.debug("  ----");
                    log.debug("  TuesdayAdqlColumn [{}]", adql.fullname());
                    log.debug("  TuesdayBaseColumn [{}]", adql.base().fullname());
                    subject.add(
                        adql
                        );                    
                    }
                }

            else if (object instanceof ADQLTable)
                {
                log.debug("  ----");
                log.debug("  ADQLTable [{}]", ((ADQLTable) object).getName());
                if (((ADQLTable) object).getDBLink() instanceof AdqlDBTable)
                    {
                    TuesdayAdqlTable adql = ((AdqlDBTable) ((ADQLTable) object).getDBLink()).table();
                    log.debug("  ----");
                    log.debug("  TuesdayAdqlTable [{}]", adql.fullname());
                    log.debug("  TuesdayBaseTable [{}]", adql.base().fullname());
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
