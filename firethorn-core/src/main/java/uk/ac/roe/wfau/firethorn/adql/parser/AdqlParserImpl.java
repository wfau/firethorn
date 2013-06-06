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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserTable.AdqlDBColumn;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import adql.db.DBChecker;
import adql.db.DBColumn;
import adql.db.DBTable;
import adql.parser.ADQLParser;
import adql.parser.ParseException;
import adql.query.ADQLObject;
import adql.query.ADQLQuery;
import adql.query.ClauseSelect;
import adql.query.SelectAllColumns;
import adql.query.SelectItem;
import adql.query.from.ADQLTable;
import adql.query.from.FromContent;
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
                AdqlQuery.Syntax.State.VALID
                );
            }
        catch (final ParseException ouch)
            {
            subject.syntax(
                AdqlQuery.Syntax.State.PARSE_ERROR,
                ouch.getMessage()
                );
            log.warn("Error parsing query [{}]", ouch.getMessage());
            }
        catch (final TranslationException ouch)
            {
            subject.syntax(
                AdqlQuery.Syntax.State.TRANS_ERROR,
                ouch.getMessage()
                );
            log.warn("Error translating query [{}]", ouch.getMessage());
            }
        }

    /**
     * Helper class to get the metadata for a query SELECT item.
     *
     */
    public static class ColumnMetaImpl implements AdqlQuery.SelectField
        {

        /**
         * Private constructor.
         *
         */
        private ColumnMetaImpl(final String name)
            {
            this(
                name,
                name,
                (AdqlColumn.Metadata)null
                );
            }

        /**
         * Private constructor.
         *
         */
        private ColumnMetaImpl(final String name, final AdqlQuery.SelectField meta)
            {
            this(
                name,
                null,
                meta.info()
                );
            }

        /**
         * Private constructor.
         *
         */
        private ColumnMetaImpl(final String name, final String alias, final AdqlQuery.SelectField meta)
            {
            this(
                name,
                alias,
                meta.info()
                );
            }

        /**
         * Private constructor.
         *
         */
        private ColumnMetaImpl(final String name, final AdqlColumn.Metadata info)
            {
            this(
                name,
                null,
                info
                );
            }

        /**
         * Private constructor.
         *
         */
        private ColumnMetaImpl(final String name, final String alias, final AdqlColumn.Metadata info)
            {
            if (alias != null)
                {
                this.name = alias ;
                }
            else {
                this.name = name ;
                }
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
        private final AdqlColumn.Metadata info ;

        @Override
        public AdqlColumn.Metadata info()
            {
            return this.info;
            }

        @Override
        public int length()
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
        public static int size(final AdqlColumn.Metadata info)
            {
            if (info == null)
                {
                return 0 ;
                }
            else {
                if (info.adql().size() == null)
                    {
                    return 0 ;
                    }
                else {
                    return info.adql().size().intValue();
                    }
                }
            }

        /**
         * Get the type from an AdqlColumn Info.
         *
         */
        public static AdqlColumn.Type type(final AdqlColumn.Metadata info)
            {
            if (info == null)
                {
                return AdqlColumn.Type.UNKNOWN;
                }
            else {
                return info.adql().type();
                }
            }

        /**
         * Evaluate a SelectItem.
         *
         */
        public static AdqlQuery.SelectField eval(final SelectItem item)
            {
            log.debug("eval(SelectItem)");
            log.debug("  alias [{}]", item.getAlias());
            log.debug("  name  [{}]", item.getName());
            log.debug("  class [{}]", item.getClass().getName());
            return new ColumnMetaImpl(
                item.getName(),
                item.getAlias(),
                eval(
                    item.getOperand()
                    )
                );
            }

        /**
         * Evaluate an ADQLOperand.
         *
         */
        public static AdqlQuery.SelectField eval(final ADQLOperand oper)
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
         * Evaluate an ADQLColumn.
         *
         */
        public static AdqlQuery.SelectField eval(final ADQLColumn column)
            {
            log.debug("eval(ADQLColumn)");
            if ((column).getDBLink() instanceof AdqlDBColumn)
                {
                return ColumnMetaImpl.eval(
                    ((AdqlDBColumn) (column).getDBLink()).column()
                    );
                }
            else {
                return new ColumnMetaImpl(
                    "unknown-column"
                    );
                }
            }

        /**
         * Evaluate an AdqlColumn.
         *
         */
        public static AdqlQuery.SelectField eval(final AdqlColumn column)
            {
            log.debug("eval(AdqlColumn)");
            log.debug("  adql [{}]", column.fullname());
            log.debug("  base [{}]", column.base().fullname());
            log.debug("  root [{}]", column.root().fullname());
            return new ColumnMetaImpl(
                column.name(),
                column.root().meta()
                );
            }
        
        /**
         * Evaluate an Operation.
         * **This is proof of concept only, it just picks the largest size param.
         *
         */
        public static AdqlQuery.SelectField eval(final Operation oper)
            {
            log.debug("eval(Operation)");
            log.debug("  name   [{}]", oper.getName());
            log.debug("  number [{}]", oper.isNumeric());
            log.debug("  string [{}]", oper.isString());

            final AdqlColumn.Metadata left = eval(
                oper.getLeftOperand()
                ).info();

            final AdqlColumn.Metadata right = eval(
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
         * Evaluate an ADQLFunction.
         * **This is proof of concept only, it just picks the largest size param.
         *
         */
        public static AdqlQuery.SelectField eval(final ADQLFunction funct)
            {
            log.debug("eval(ADQLFunction)");
            log.debug("  name   [{}]", funct.getName());
            log.debug("  number [{}]", funct.isNumeric());
            log.debug("  string [{}]", funct.isString());

            AdqlColumn.Metadata info = null ;

            for (final ADQLOperand param : funct.getParameters())
                {
                final AdqlColumn.Metadata temp = eval(
                    param
                    ).info();
                if (info == null)
                    {
                    info = temp;
                    }
                else {
                    if (temp.adql().size().intValue() > info.adql().size().intValue())
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
     * Wrap an ADQLObject as an Iterable.
     *
     */
    protected Iterable<ADQLObject> iter(final ADQLObject object)
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
     * Process an ADQL query.
     *
     */
    protected void process(final AdqlParserQuery subject, final ADQLQuery object)
        {
        process(
            "..",
            subject,
            object,
            iter(
                object
                )
            );
        }

    protected void process(final String prefix, final AdqlParserQuery subject, final ADQLQuery aquery, final Iterable<ADQLObject> iter)
        {
        for (final ADQLObject object: iter)
            {
            log.debug("{} ----", prefix);
            log.debug("{} ADQLObject [{}]", prefix, object.getClass().getName());

            if (object instanceof ClauseSelect)
                {
                log.debug("{} ----", prefix);
                log.debug("{} ClauseSelect", prefix);
                for (final SelectItem item : ((ClauseSelect) object))
                    {
                    log.debug("{} Select item ----", prefix);
                    log.debug("{}  alias [{}]", prefix, item.getAlias());
                    log.debug("{}  class [{}]", prefix, item.getClass().getName());

                    if (item instanceof SelectAllColumns)
                        {
                        log.debug("{} Select all >>>>", prefix);
                        SelectAllColumns all = (SelectAllColumns) item;
                        log.debug("{}  All table [{}]", prefix, all.getAdqlTable());
                        log.debug("{}  All query [{}]", prefix, all.getQuery());
                        if (all.getQuery() != null)
                            {
                            fields(
                                prefix,
                                subject,
                                all.getQuery()
                                );                            
                            }
                        else if (all.getAdqlTable() != null)
                            {
                            fields(
                                prefix,
                                subject,
                                aquery,
                                all.getAdqlTable()
                                );                            
                            }
                        else {
                            // TODO error
                            // Neither query nor table ..
                            log.warn("SelectAllColumns with null query and table");
                            }
                        log.debug("{} Select all <<<<", prefix);
                        }
                    else {
                        final AdqlQuery.SelectField meta = ColumnMetaImpl.eval(
                            item
                            );
                        log.debug("{}  name [{}]", prefix, meta.name());
                        log.debug("{}  type [{}]", prefix, meta.type());
                        log.debug("{}  size [{}]", prefix, meta.length());
                        subject.add(
                            meta
                            );
                        }
                    }
                }

            else if (object instanceof ADQLColumn)
                {
                log.debug("{} ----", prefix);
                log.debug("{} ADQLColumn [{}]", prefix, ((ADQLColumn) object).getName());
                if (((ADQLColumn) object).getDBLink() instanceof AdqlDBColumn)
                    {
                    final AdqlColumn adql = ((AdqlDBColumn) ((ADQLColumn) object).getDBLink()).column();
                    log.debug("{}  ----", prefix);
                    log.debug("{}  AdqlColumn [{}]", prefix, adql.fullname());
                    log.debug("{}  BaseColumn [{}]", prefix, adql.base().fullname());
                    log.debug("{}  RootColumn [{}]", prefix, adql.root().fullname());
                    subject.add(
                        adql
                        );
                    }
                }

            else if (object instanceof ADQLTable)
                {
                log.debug("{} ----", prefix);
                log.debug("{} ADQLTable [{}]", prefix, ((ADQLTable) object).getName());
                if (((ADQLTable) object).getDBLink() instanceof AdqlParserTable)
                    {
                    final AdqlTable adql = ((AdqlParserTable) ((ADQLTable) object).getDBLink()).table();
                    log.debug("{}   ----", prefix);
                    log.debug("{}   AdqlTable [{}]", prefix, adql.fullname());
                    log.debug("{}   BaseTable [{}]", prefix, adql.base().fullname());
                    log.debug("{}   RootTable [{}]", prefix, adql.root().fullname());
                    subject.add(
                        adql
                        );
                    }
                }

            else {
                process(
                    prefix.concat(" .."),
                    subject,
                    aquery,
                    iter(
                        object
                        )
                    );
                }
            }
        }
    
    protected void fields(final String prefix, final AdqlParserQuery subject, final ADQLQuery query, final ADQLTable table)
        {
        log.debug("{} fields(AdqlParserQuery, ADQLQuery, ADQLTable)", prefix);
        log.debug("{} ADQLTable [{}][{}]", prefix, table.getName(), table.getClass().getName());
        if (table.getDBLink() == null)
            {
            log.debug("{} ADQLTable getDBLink() is NULL", prefix);
            fields(
                prefix,
                subject,
                query,
                table,
                query.getFrom()
                );
            }
        else if (table.getDBLink() instanceof AdqlParserTable)
            {
            log.debug("{} ADQLTable getDBLink() is AdqlParserTable", prefix);
            fields(
                prefix,
                subject,
                (AdqlParserTable) table.getDBLink()
                );
            }
        else {
            log.warn("{} ADQLTable getDBLink() is unexpected class [{}]", prefix, table.getDBLink().getClass().getName());
            }
        }
 
    protected void fields(final String prefix, final AdqlParserQuery subject, final ADQLQuery query, ADQLTable table, FromContent from)
        {
        log.debug("{} fields(AdqlParserQuery, ADQLQuery, ADQLTable, FromContent)", prefix);
        log.debug("{}  table [{}][{}]", prefix, table.getName(), table.getAlias());
        for (ADQLTable temp : from.getTables())
            {
            log.debug("{}  temp  [{}][{}]", prefix, temp.getName(), temp.getAlias());
            if (table.hasAlias())
                {
                if (temp.hasAlias())
                    {
                    if (table.getAlias() != null)
                        {
                        if (table.getAlias().equalsIgnoreCase(temp.getAlias()))
                            {
                            log.debug("{} Found match on alias [{}]", prefix, table.getAlias());
                            fields(
                                prefix,
                                subject,
                                query,
                                temp
                                );
                            return ;
                            }
                        }
                    }
                }
            else {
                if (table.getName() != null)
                    {
                    if (table.getName().equalsIgnoreCase(temp.getName()))
                        {
                        log.debug("{} Found match on name [{}]", prefix, table.getName());
                        fields(
                            prefix,
                            subject,
                            query,
                            temp
                            );
                        return ;
                        }
                    }
                }
            }
        log.warn("Unable to find matching table [{}][{}]", table.getName(), table.getAlias());
        }

    protected void fields(final String prefix, final AdqlParserQuery subject, final ADQLQuery query)
        {
        log.debug("{} fields(AdqlParserQuery, ADQLQuery)", prefix);
        log.debug("{}  ADQLQuery [{}]", prefix, query.getName());
        for (DBColumn column : query.getResultingColumns())
            {
            log.debug("{}   DBColumn [{}][{}]", prefix, column.getADQLName(), column.getClass().getName());
            if (column instanceof AdqlDBColumn)
                {
                subject.add(
                    ColumnMetaImpl.eval(
                        ((AdqlDBColumn) column).column()
                        )
                    );
                }
            }
        }
    
    protected void fields(final String prefix, final AdqlParserQuery subject, AdqlParserTable table)
        {
        log.debug("{} fields(AdqlParserQuery, AdqlParserTable)", prefix);
        fields(
            prefix,
            subject,
            table.table()
            );
        }

    protected void fields(final String prefix, final AdqlParserQuery subject, AdqlTable table)
        {
        log.debug("{} fields(AdqlParserQuery, AdqlTable)", prefix);
        log.debug("{}  AdqlTable [{}]", prefix, table.fullname());
        log.debug("{}  BaseTable [{}]", prefix, table.base().fullname());
        log.debug("{}  RootTable [{}]", prefix, table.root().fullname());
        for (AdqlColumn column : table.columns().select())
            {
            subject.add(
                ColumnMetaImpl.eval(
                    column
                    )
                );
            }
        }
    }
