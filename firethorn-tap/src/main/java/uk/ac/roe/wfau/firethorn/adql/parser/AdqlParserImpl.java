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
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumnInfo;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumnType;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import adql.db.DBChecker;
import adql.db.DBTable;
import adql.parser.ADQLParser;
import adql.parser.ParseException;
import adql.query.ADQLObject;
import adql.query.ADQLQuery;
import adql.query.ClauseSelect;
import adql.query.SelectItem;
import adql.query.from.ADQLTable;
import adql.query.operand.ADQLColumn;
import adql.query.operand.ADQLOperand;
import adql.query.operand.Operation;
import adql.query.operand.function.ADQLFunction;
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
        public AdqlParser create(final AdqlQuery.Mode mode, final AdqlSchema schema)
            {
            return new AdqlParserImpl(
                this.tables,
                mode,
                schema
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
    protected AdqlParserImpl(final AdqlParserTable.Factory factory, final AdqlQuery.Mode mode, final AdqlSchema schema)
        {
        this.mode = mode ;

        final AdqlResource workspace = schema.resource();
        
        final Set<DBTable> tables = new HashSet<DBTable>();
        for (final AdqlSchema temp : workspace.schemas().select())
            {
            for (final AdqlTable table : temp.tables().select())
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
        this.parser.disable_tracing();
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
            // ** PATCH FIX FOR CROSS JOIN BUG **
            final ADQLTranslator translator = new PostgreSQLTranslator(false);
            subject.osql(
                translator.translate(
                    object
                    ).replace(
                        "CROSS JOIN",
                        ","
                        )
                );
            //
            // If we got this far, then the query is valid.
            subject.syntax(
                AdqlQuery.Syntax.Status.VALID
                );
            }
        catch (final ParseException ouch)
            {
            subject.syntax(
                AdqlQuery.Syntax.Status.PARSE_ERROR,
                ouch.getMessage()
                );
            log.warn("Error parsing query [{}]", ouch.getMessage());
            }
        catch (final TranslationException ouch)
            {
            subject.syntax(
                AdqlQuery.Syntax.Status.TRANS_ERROR,
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
     * Helper class to get the metadata for a query SELECT item.
     *
     */
    public static class ColumnMetaImpl implements AdqlQuery.ColumnMeta
        {

        /**
         * Private constructor.
         *
         */
        private ColumnMetaImpl(final String name)
            {
            this.name = name ;
            }

        /**
         * Private constructor.
         *
         */
        private ColumnMetaImpl(final String name, final AdqlQuery.ColumnMeta eval)
            {
            this.name = name ;
            if (eval != null)
                {
                this.info = eval.info();
                }
            }

        /**
         * Private constructor.
         *
         */
        private ColumnMetaImpl(final String name, final AdqlColumn.Info info)
            {
            this.name = name ;
            this.info = info ;
            }

        /**
         * The item name or alias.
         *
         */
        private String name;

        @Override
        public String name()
            {
            return this.name;
            }

        /**
         * The item metadata.
         *
         */
        private AdqlColumn.Info info ;

        @Override
        public AdqlColumn.Info info()
            {
            return this.info;
            }

        @Override
        public int size()
            {
            return size(
                this.info
                );
            }

        @Override
        public AdqlColumn.Type type()
            {
            return type(
                this.info
                );
            }

        /**
         * Get the size from an AdqlColumn Info.
         *
         */
        public static int size(final AdqlColumn.Info info)
            {
            if (info == null)
                {
                return 0 ;
                }
            else {
                if (info.size() == null)
                    {
                    return 0 ;
                    }
                else {
                    return info.size().intValue();
                    }
                }
            }

        /**
         * Get the type from an AdqlColumn Info.
         *
         */
        public static AdqlColumn.Type type(final AdqlColumn.Info info)
            {
            if (info == null)
                {
                return AdqlColumn.Type.UNKNOWN;
                }
            else {
                return info.type();
                }
            }

        /**
         * Evaluate the column metadata for a SelectItem.
         * 
         */
        public static AdqlQuery.ColumnMeta eval(final SelectItem item)
            {
            log.debug("eval(SelectItem)");
            log.debug("  alias [{}]", item.getAlias());
            log.debug("  name  [{}]", item.getName());
            log.debug("  class [{}]", item.getClass().getName());
            return new ColumnMetaImpl(
                item.getAlias(),
                eval(
                    item.getOperand()
                    )
                );
            }
            
        /**
         * Evaluate the column metadata for an ADQLOperand.
         *
         */
        public static AdqlQuery.ColumnMeta eval(final ADQLOperand oper)
            {
            log.debug("eval(ADQLOperand)");
            log.debug("  name   [{}]", oper.getName());
            log.debug("  class  [{}]", oper.getClass().getName());
            log.debug("  number [{}]", oper.isNumeric());
            log.debug("  string [{}]", oper.isString());
            if (oper instanceof ADQLColumn)
                {
                return eval(
                    (ADQLColumn) oper
                    );
                }
            else if (oper instanceof ADQLFunction)
                {
                return eval(
                    (ADQLFunction) oper
                    );
                }
            else if (oper instanceof Operation)
                {
                return eval(
                    (Operation) oper
                    );
                }
            else {
                return new ColumnMetaImpl(
                    "unknown-oper"
                    );
                }
            }
    
        /**
         * Evaluate the column metadata for an ADQLColumn.
         *
         */
        public static AdqlQuery.ColumnMeta eval(final ADQLColumn column)
            {
            log.debug("eval(ADQLColumn)");
            if (((ADQLColumn) column).getDBLink() instanceof AdqlDBColumn)
                {
                final AdqlColumn adql = ((AdqlDBColumn) ((ADQLColumn) column).getDBLink()).column();
                log.debug("  ----");
                log.debug("  adql [{}]", adql.fullname());
                log.debug("  base [{}]", adql.base().fullname());
                return new ColumnMetaImpl(
                    adql.name(),
                    adql.base().info().adql()
                    );
                }
            else {
                return new ColumnMetaImpl(
                    "unknown-column"
                    );
                }
            }

        /**
         * Evaluate the column metadata for an Operation.
         * This is proof of concept only, it just picks the largest size param.
         *
         */
        public static AdqlQuery.ColumnMeta eval(final Operation oper)
            {
            log.debug("eval(Operation)");
            log.debug("  name   [{}]", oper.getName());
            log.debug("  number [{}]", oper.isNumeric());
            log.debug("  string [{}]", oper.isString());
    
            AdqlColumn.Info left = eval(
                oper.getLeftOperand()
                ).info(); 
    
            AdqlColumn.Info right = eval(
                oper.getRightOperand()
                ).info(); 
    
            if (left == null)
                {
                return new ColumnMetaImpl(
                    oper.getName(),
                    right
                    );
                }
            else if (right == null)
                {
                return new ColumnMetaImpl(
                    oper.getName(),
                    left
                    );
                }
            else {
                if (size(left) > size(right))
                    {
                    return new ColumnMetaImpl(
                        oper.getName(),
                        left
                        );
                    }
                else {
                    return new ColumnMetaImpl(
                        oper.getName(),
                        right
                        );
                    }
                }
            }

        /**
         * Evaluate the column metadata for an ADQLFunction.
         * This is proof of concept only, it just picks the largest size param.
         *
         */
        public static AdqlQuery.ColumnMeta eval(final ADQLFunction funct)
            {
            log.debug("eval(ADQLFunction)");
            log.debug("  name   [{}]", funct.getName());
            log.debug("  number [{}]", funct.isNumeric());
            log.debug("  string [{}]", funct.isString());
    
            AdqlColumn.Info info = null ;
            
            for (ADQLOperand param : funct.getParameters())
                {
                AdqlColumn.Info temp = eval(
                    param
                    ).info(); 
                if (info == null)
                    {
                    info = temp;
                    }
                else {
                    if (temp.size() > info.size())
                        {
                        info = temp ;
                        }
                    }
                }
            return new ColumnMetaImpl(
                funct.getName(),
                info
                );
            }

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

            if (object instanceof ClauseSelect)
                {
                log.debug("  ----");
                log.debug("  ClauseSelect");
                for (SelectItem item : ((ClauseSelect) object))
                    {
                    log.debug("-- Select item ----");
                    log.debug(" alias  [{}]", item.getAlias());
                    AdqlQuery.ColumnMeta meta = ColumnMetaImpl.eval(
                        item
                        );
                    log.debug(" name [{}]", meta.name());
                    log.debug(" type [{}]", meta.type());
                    log.debug(" size [{}]", meta.size());
                    subject.add(
                        meta
                        );
                    }
                }
            
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
