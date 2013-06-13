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
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Mode;
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
import adql.query.ADQLOrder;
import adql.query.ADQLQuery;
import adql.query.ClauseADQL;
import adql.query.ClauseConstraints;
import adql.query.ClauseSelect;
import adql.query.ColumnReference;
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

            //
            // Translate the query into SQL.
            final ADQLTranslator translator ;
            if (this.mode == Mode.DIRECT)
                {
                translator = new SQLServerTranslator();
                }
            else {
                translator = new OgsaDQPTranslator();
                }
            
            // TODO ** PATCH FIX FOR CROSS JOIN BUG **
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
                if (info.adql().arraysize() == null)
                    {
                    return 0 ;
                    }
                else {
                    return info.adql().arraysize().intValue();
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
        log.debug("process(final AdqlParserQuery, ADQLQuery, ADQLQuery");
        /*
         * Handle each part separately ?
         * 
        ClauseSelect select = object.getSelect();
        FromContent  from = object.getFrom();        
        ClauseConstraints where = object.getWhere();
        ClauseADQL<ADQLOrder> orderrby =object.getOrderBy();
        ClauseADQL<ColumnReference> groupby = object.getGroupBy();
        ClauseConstraints having = object.getHaving();
         *  
         */
        
        /*
         * Recursively process the tree ...
         *
         */
        process(
            subject,
            object,
            iter(
                object
                )
            );
        }

    /**
     * Recursively process a tree of ADQLObject(s).
     * 
     */
    protected void process(final AdqlParserQuery subject, final ADQLQuery query, final Iterable<ADQLObject> iter)
        {
        log.debug("process(final AdqlParserQuery, ADQLQuery, Iterable<ADQLObject>");
        for (final ADQLObject clause: iter)
            {
            log.debug(" ----");
            log.debug(" ADQLObject [{}]", clause.getClass().getName());
            //
            // The query select clause.
            if (clause instanceof ClauseSelect)
                {
                log.debug(" ----");
                log.debug(" ClauseSelect");
                process(
                    subject,
                    query,
                    ((ClauseSelect) clause)
                    );
                }
            //
            // A column reference outside the select clause.
            else if (clause instanceof ADQLColumn)
                {
                log.debug(" ----");
                log.debug(" ADQLColumn [{}]", ((ADQLColumn) clause).getName());
                process(
                    subject,
                    query,
                    ((ADQLColumn) clause)
                    );
                }
            //
            // A table reference outside the select clause.
            else if (clause instanceof ADQLTable)
                {
                log.debug(" ----");
                log.debug(" ADQLTable [{}]", ((ADQLTable) clause).getName());
                process(
                    subject,
                    query,
                    ((ADQLTable) clause)
                    );
                }
            //
            // Process the child nodes.
            else {
                process(
                    subject,
                    query,
                    iter(
                        clause
                        )
                    );
                }
            }
        }

    /**
     * Process the SELECT part of the query.
     * 
     */
    protected void process(final AdqlParserQuery subject, final ADQLQuery query, final ClauseSelect select)
        {
        log.debug("process(final AdqlParserQuery, ADQLQuery, ClauseSelect");
        for (final SelectItem item : select)
            {
            log.debug(" Select item ----");
            log.debug("  name  [{}]", item.getName());
            log.debug("  alias [{}]", item.getAlias());
            log.debug("  class [{}]", item.getClass().getName());
            //
            // Specific case of SelectAll.
            if (item instanceof SelectAllColumns)
                {
                process(
                    subject,
                    query,
                    (SelectAllColumns) item
                    );
                }
            //
            // Everything else ....
            else {
                subject.add(
                    wrap(
                        item
                        )
                    );
                }
            }
        }

    /**
     * Process a 'SELECT *' construct.
     * 
     */
    protected void process(final AdqlParserQuery subject, final ADQLQuery query, final SelectAllColumns selectall)
        {
        log.debug("process(final AdqlParserQuery, ADQLQuery, SelectAllColumns");
        //
        // Generic 'SELECT *' from all the tables.
        if (selectall.getQuery() != null)
            {
            fields(
                subject,
                selectall.getQuery()
                );                            
            }
        //
        // Specific 'SELECT table.*' from a single table. 
        else if (selectall.getAdqlTable() != null)
            {
            fields(
                subject,
                query,
                selectall.getAdqlTable()
                );                            
            }
        //
        // Shouldn't get here, but check anyway.
        else {
            // TODO error
            // Neither query nor table ..
            log.warn("SelectAllColumns with null query and table");
            }
        }

    /**
     * Process a column reference outside the SELECT clause.
     * Adds the column to the list of columns used by the query.  
     *
     */
    protected void process(final AdqlParserQuery subject, final ADQLQuery query, final ADQLColumn column)
        {
        log.debug("process(final AdqlParserQuery, ADQLQuery, ADQLColumn");
        if (column.getDBLink() == null)
            {
            log.warn("ADQLColumn getDBLink() is NULL");
            }
        else if (column.getDBLink() instanceof AdqlDBColumn)
            {
            final AdqlColumn adql = ((AdqlDBColumn) column.getDBLink()).column();
            log.debug("  ----");
            log.debug("  AdqlColumn [{}]", adql.fullname());
            log.debug("  BaseColumn [{}]", adql.base().fullname());
            log.debug("  RootColumn [{}]", adql.root().fullname());
            subject.add(
                adql
                );
            }
        else {
            log.warn("ADQLColumn getDBLink() is unexpected class [{}]", column.getDBLink().getClass().getName());
            }
        }
    
    /**
     * Process a table reference outside the SELECT clause.
     * Adds the table to the list of tables used by the query.  
     *
     */
    protected void process(final AdqlParserQuery subject, final ADQLQuery query, final ADQLTable table)
        {
        log.debug("process(final AdqlParserQuery, ADQLQuery, ADQLTable");
        if (table.getDBLink() == null)
            {
            log.warn("ADQLTable getDBLink() is NULL");
            }
        else if (table.getDBLink() instanceof AdqlParserTable)
            {
            final AdqlTable adql = ((AdqlParserTable) table.getDBLink()).table();
            log.debug("   ----");
            log.debug("   AdqlTable [{}]", adql.fullname());
            log.debug("   BaseTable [{}]", adql.base().fullname());
            log.debug("   RootTable [{}]", adql.root().fullname());
            subject.add(
                adql
                );
            }
        else {
            log.warn("ADQLTable getDBLink() is unexpected class [{}]", table.getDBLink().getClass().getName());
            }
        }
    
    /**
     * Add all the fields from a table, called by 'SELECT table.*'.
     * 
     * TODO
     * This has to handle ADQLTable with getDBLink() == null.
     * Figure out where this comes from and fix it.
     * 
     */
    protected void fields(final AdqlParserQuery subject, final ADQLQuery query, final ADQLTable table)
        {
        log.debug("fields(AdqlParserQuery, ADQLQuery, ADQLTable)");
        log.debug("ADQLTable [{}][{}]", table.getName(), table.getClass().getName());
        if (table.getDBLink() == null)
            {
            log.warn("ADQLTable getDBLink() is NULL");
            fields(
                subject,
                query,
                table,
                query.getFrom()
                );
            }
        else if (table.getDBLink() instanceof AdqlParserTable)
            {
            log.debug("ADQLTable getDBLink() is AdqlParserTable");
            fields(
                subject,
                (AdqlParserTable) table.getDBLink()
                );
            }
        else {
            log.warn("ADQLTable getDBLink() is unexpected class [{}]", table.getDBLink().getClass().getName());
            }
        }
 
    /**
     * Add all the fields from a table, called by 'SELECT table.*'.
     * 
     * TODO
     * Searches the query FromContent for a matching table.
     * Need to replace this with code that uses the firethorn metadata.
     * 
     */
    protected void fields(final AdqlParserQuery subject, final ADQLQuery query, ADQLTable table, FromContent from)
        {
        log.debug("fields(AdqlParserQuery, ADQLQuery, ADQLTable, FromContent)");
        log.debug("  table [{}][{}]", table.getName(), table.getAlias());
        for (ADQLTable temp : from.getTables())
            {
            log.debug("    temp  [{}][{}]", temp.getName(), temp.getAlias());
            if (table.hasAlias())
                {
                if (temp.hasAlias())
                    {
                    if (table.getAlias() != null)
                        {
                        if (table.getAlias().equalsIgnoreCase(temp.getAlias()))
                            {
                            log.debug("Found match on alias [{}]", table.getAlias());
                            fields(
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
                        log.debug("Found match on name [{}]", table.getName());
                        fields(
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

    /**
     * Add all the fields from a query, called by 'SELECT *'.
     *
     */
    protected void fields(final AdqlParserQuery subject, final ADQLQuery query)
        {
        log.debug("fields(AdqlParserQuery, ADQLQuery)");
        log.debug("  ADQLQuery [{}]", query.getName());
        for (DBColumn column : query.getResultingColumns())
            {
            log.debug("    DBColumn [{}][{}]", column.getADQLName(), column.getClass().getName());
            if (column instanceof AdqlDBColumn)
                {
                subject.add(
                    wrap(
                        ((AdqlDBColumn) column).column()
                        )
                    );
                }
            }
        }
    
    /**
     * Add all the fields from an AdqlParserTable.
     *
     */
    protected void fields(final AdqlParserQuery subject, AdqlParserTable table)
        {
        log.debug("fields(AdqlParserQuery, AdqlParserTable)");
        fields(
            subject,
            table.table()
            );
        }

    /**
     * Add all the fields from an AdqlTable.
     *
     */
    protected void fields(final AdqlParserQuery subject, AdqlTable table)
        {
        log.debug("fields(AdqlParserQuery, AdqlTable)");
        log.debug("  AdqlTable [{}]", table.fullname());
        log.debug("  BaseTable [{}]", table.base().fullname());
        log.debug("  RootTable [{}]", table.root().fullname());
        for (AdqlColumn column : table.columns().select())
            {
            subject.add(
                wrap(
                    column
                    )
                );
            }
        }

    /**
     * Wrap a SelectItem.
     *
     */
    public AdqlQuery.SelectField wrap(final SelectItem item)
        {
        log.debug("eval(SelectItem)");
        log.debug("  alias [{}]", item.getAlias());
        log.debug("  name  [{}]", item.getName());
        log.debug("  class [{}]", item.getClass().getName());
        return new ColumnMetaImpl(
            item.getName(),
            item.getAlias(),
            wrap(
                item.getOperand()
                )
            );
        }

    /**
     * Wrap an ADQLOperand.
     *
     */
    public AdqlQuery.SelectField wrap(final ADQLOperand oper)
        {
        log.debug("eval(ADQLOperand)");
        log.debug("  name   [{}]", oper.getName());
        log.debug("  class  [{}]", oper.getClass().getName());
        log.debug("  number [{}]", oper.isNumeric());
        log.debug("  string [{}]", oper.isString());
        if (oper instanceof ADQLColumn)
            {
            return wrap(
                (ADQLColumn) oper
                );
            }
        else if (oper instanceof ADQLFunction)
            {
            return wrap(
                (ADQLFunction) oper
                );
            }
        else if (oper instanceof Operation)
            {
            return wrap(
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
     * Wrap an ADQLColumn.
     *
     */
    public AdqlQuery.SelectField wrap(final ADQLColumn column)
        {
        log.debug("wrap(ADQLColumn)");
        log.debug("  name   [{}]", column.getName());
        log.debug("  class  [{}]", column.getClass().getName());
        if (column.getDBLink() == null)
            {
            log.warn("column.getDBLink() == null");
            return new ColumnMetaImpl(
                "unknown-column"
                );
            }
        else if (column.getDBLink() instanceof AdqlDBColumn)
            {
            return wrap(
                ((AdqlDBColumn) (column.getDBLink())).column()
                );
            }
        else {
            log.warn("Unknown column.getDBLink() class [{}]", column.getDBLink(),getClass().getName());
            return new ColumnMetaImpl(
                "unknown-column"
                );
            }
        }

    /**
     * Wrap an AdqlColumn.
     *
     */
    public AdqlQuery.SelectField wrap(final AdqlColumn column)
        {
        log.debug("wrap(AdqlColumn)");
        log.debug("  adql [{}]", column.fullname());
        log.debug("  base [{}]", column.base().fullname());
        log.debug("  root [{}]", column.root().fullname());
        return new ColumnMetaImpl(
            column.name(),
            column.root().meta()
            );
        }

    /**
     * Wrap an Operation.
     * **This is proof of concept only, it just picks the largest size param.
     *
     */
    public AdqlQuery.SelectField wrap(final Operation oper)
        {
        log.debug("wrap(Operation)");
        log.debug("  name   [{}]", oper.getName());
        log.debug("  number [{}]", oper.isNumeric());
        log.debug("  string [{}]", oper.isString());

        final AdqlColumn.Metadata left = wrap(
            oper.getLeftOperand()
            ).info();

        final AdqlColumn.Metadata right = wrap(
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
/*
            if (left.adql().array() > right.adql().array())
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
 */
            //
            // Temp fix ... array() is null
            return new ColumnMetaImpl(
                oper.getName(),
                left
                );
            }
        }

    /**
     * Wrap an ADQLFunction.
     * **This is proof of concept only, it just picks the largest size param.
     *
     */
    public AdqlQuery.SelectField wrap(final ADQLFunction funct)
        {
        log.debug("wrap(ADQLFunction)");
        log.debug("  name   [{}]", funct.getName());
        log.debug("  number [{}]", funct.isNumeric());
        log.debug("  string [{}]", funct.isString());

        AdqlColumn.Metadata info = null ;

        for (final ADQLOperand param : funct.getParameters())
            {
            final AdqlColumn.Metadata temp = wrap(
                param
                ).info();
            if (info == null)
                {
                info = temp;
                }
            else {
                if (temp.adql().arraysize().intValue() > info.adql().arraysize().intValue())
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
