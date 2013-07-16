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
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Mode;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
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
import adql.query.operand.function.MathFunction;
import adql.query.operand.function.SQLFunction;
import adql.query.operand.function.UserDefinedFunction;
import adql.translator.ADQLTranslator;
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
        //
        // Create a full set of all the available tables.
        log.debug("Initalising tables");
        final Set<DBTable> tables = new HashSet<DBTable>();
        for (final AdqlSchema temp : workspace.schemas().select())
            {
            log.debug("  schema [{}]", temp.name());
            for (final AdqlTable table : temp.tables().select())
                {
                log.debug("  table  [{}][{}]", temp.name(), table.name());
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
            log.debug("  AdqlColumn [{}]", adql.namebuilder());
            log.debug("  BaseColumn [{}]", adql.base().namebuilder());
            log.debug("  RootColumn [{}]", adql.root().namebuilder());
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
            log.debug("   AdqlTable [{}]", adql.namebuilder());
            log.debug("   BaseTable [{}]", adql.base().namebuilder());
            log.debug("   RootTable [{}]", adql.root().namebuilder());
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
    protected void fields(final AdqlParserQuery subject, final ADQLQuery query, final ADQLTable table, final FromContent from)
        {
        log.debug("fields(AdqlParserQuery, ADQLQuery, ADQLTable, FromContent)");
        log.debug("  table [{}][{}]", table.getName(), table.getAlias());
        for (final ADQLTable temp : from.getTables())
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
        for (final DBColumn column : query.getResultingColumns())
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
    protected void fields(final AdqlParserQuery subject, final AdqlParserTable table)
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
    protected void fields(final AdqlParserQuery subject, final AdqlTable table)
        {
        log.debug("fields(AdqlParserQuery, AdqlTable)");
        log.debug("  AdqlTable [{}]", table.namebuilder());
        log.debug("  BaseTable [{}]", table.base().namebuilder());
        log.debug("  RootTable [{}]", table.root().namebuilder());
        for (final AdqlColumn column : table.columns().select())
            {
            subject.add(
                wrap(
                    column
                    )
                );
            }
        }

    public static class SelectFieldImpl
    implements AdqlQuery.SelectField
        {

        private SelectFieldImpl(final String name, final Integer size, final AdqlColumn.Type type)
            {
            this.name  = name ;
            this.size  = size ;
            this.type  = type ;
            }

        private final String name;
        @Override
        public String name()
            {
            return this.name;
            }

        @Override
        public AdqlColumn adql()
            {
            return null;
            }

        @Override
        public JdbcColumn jdbc()
            {
            return null ;
            }

        private final Integer size;
        @Override
        public Integer arraysize()
            {
            return this.size;
            }

        private final AdqlColumn.Type type;
        @Override
        public AdqlColumn.Type type()
            {
            return this.type;
            }
        }

    public static class SelectFieldWrapper
    implements AdqlQuery.SelectField
        {

        private SelectFieldWrapper(final String name, final AdqlQuery.SelectField field)
            {
            this.name  = name  ;
            this.field = field ;
            }

        private final String name;
        @Override
        public String name()
            {
            return this.name;
            }

        private final AdqlQuery.SelectField field;
        public AdqlQuery.SelectField field()
            {
            return this.field;
            }

        @Override
        public AdqlColumn adql()
            {
            return this.field.adql();
            }

        @Override
        public JdbcColumn jdbc()
            {
            return this.field.jdbc();
            }

        @Override
        public Integer arraysize()
            {
            return this.field.arraysize();
            }

        @Override
        public AdqlColumn.Type type()
            {
            return this.field.type();
            }
        }

    public static class AdqlColumnWrapper
    implements AdqlQuery.SelectField
        {

        private AdqlColumnWrapper(final AdqlColumn adql)
            {
            this(
                adql.name(),
                adql
                );
            }

        private AdqlColumnWrapper(final String name, final AdqlColumn adql)
            {
            this.name  = name ;
            this.adql  = adql ;
            }

        private final String name;
        @Override
        public String name()
            {
            return this.name;
            }

        private final AdqlColumn adql;

        @Override
        public AdqlColumn adql()
            {
            return this.adql;
            }

        @Override
        public JdbcColumn jdbc()
            {
            if (this.adql != null)
                {
                if (this.adql.base() instanceof JdbcColumn)
                    {
                    return ((JdbcColumn) this.adql.base());
                    }
                }
            return null ;
            }

        @Override
        public Integer arraysize()
            {
            return this.adql.meta().adql().arraysize();
            }

        @Override
        public AdqlColumn.Type type()
            {
            return this.adql.meta().adql().type();
            }
        }

    /**
     * Wrap a SelectItem.
     *
     */
    public static AdqlQuery.SelectField wrap(final SelectItem item)
        {
        log.debug("wrap(SelectItem)");
        log.debug("  alias [{}]", item.getAlias());
        log.debug("  name  [{}]", item.getName());
        log.debug("  class [{}]", item.getClass().getName());
        return new SelectFieldWrapper(
            ((item.getAlias() != null) ? item.getAlias() : item.getName()),
            wrap(
                item.getOperand()
                )
            );
        }

    /**
     * Wrap an ADQLOperand.
     *
     */
    public static AdqlQuery.SelectField wrap(final ADQLOperand oper)
        {
        log.debug("wrap(ADQLOperand)");
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
            return new SelectFieldImpl(
                "unknown",
                new Integer(0),
                AdqlColumn.Type.UNKNOWN
                );
            }
        }

    /**
     * Wrap an ADQLColumn.
     *
     */
    public static AdqlQuery.SelectField wrap(final ADQLColumn column)
        {
        log.debug("wrap(ADQLColumn)");
        log.debug("  name   [{}]", column.getName());
        log.debug("  class  [{}]", column.getClass().getName());
        if (column.getDBLink() == null)
            {
            log.warn("column.getDBLink() == null");
            return new SelectFieldImpl(
                "unknown",
                new Integer(0),
                AdqlColumn.Type.UNKNOWN
                );
            }
        else if (column.getDBLink() instanceof AdqlDBColumn)
            {
            return wrap(
                ((AdqlDBColumn) (column.getDBLink())).column()
                );
            }
        else {
            log.warn("Unknown column.getDBLink() class [{}]", column.getDBLink().getClass().getName());
            return new SelectFieldImpl(
                "unknown",
                new Integer(0),
                AdqlColumn.Type.UNKNOWN
                );
            }
        }

    /**
     * Wrap an AdqlColumn.
     * @todo Catch DATE_TIME and convert into char[10]
     *
     */
    public static AdqlQuery.SelectField wrap(final AdqlColumn column)
        {
        log.debug("wrap(AdqlColumn)");
        log.debug("  adql [{}]", column.namebuilder());
        log.debug("  base [{}]", column.base().namebuilder());
        log.debug("  root [{}]", column.root().namebuilder());
        return new AdqlColumnWrapper(
            column
            );
        }

    /**
     * Wrap an Operation.
     * **This is proof of concept only, it just picks the largest size param.
     *
     */
    public static AdqlQuery.SelectField wrap(final Operation oper)
        {
        log.debug("wrap(Operation)");
        log.debug("  name   [{}]", oper.getName());
        log.debug("  number [{}]", oper.isNumeric());
        log.debug("  string [{}]", oper.isString());
        //
        // Temp fix ... array() is null
        return new SelectFieldWrapper(
            oper.getName(),
            wrap(
                oper.getLeftOperand()
                )
            );
        }

    /**
     * Function handler.
     *
    public enum FunctionHandler
        {
        AVG()
            {
            @Override
            AdqlQuery.SelectField handle(ADQLOperand[] params)
                {
                return new SelectFieldWrapper(
                    this.name(),
                    wrap(
                        params[0]
                        )
                    );
                }
            },

        SUM()
            {
            @Override
            AdqlQuery.SelectField handle(ADQLOperand[] params)
                {
                return new SelectFieldWrapper(
                    this.name(),
                    wrap(
                        params[0]
                        )
                    );
                }
            },

        MIN()
            {
            @Override
            AdqlQuery.SelectField handle(ADQLOperand[] params)
                {
                return new SelectFieldWrapper(
                    this.name(),
                    wrap(
                        params[0]
                        )
                    );
                }
            },

        MAX()
            {
            @Override
            AdqlQuery.SelectField handle(ADQLOperand[] params)
                {
                return new SelectFieldWrapper(
                    this.name(),
                    wrap(
                        params[0]
                        )
                    );
                }
            };

        abstract AdqlQuery.SelectField handle(ADQLOperand[] param);

        }
     */

    /**
     * Wrap an ADQLFunction.
     *
     */
    public static AdqlQuery.SelectField wrap(final ADQLFunction funct)
        {
        log.debug("wrap(ADQLFunction)");
        log.debug("  name   [{}]", funct.getName());
        log.debug("  number [{}]", funct.isNumeric());
        log.debug("  string [{}]", funct.isString());

        if (funct instanceof SQLFunction)
            {
            return wrap((SQLFunction) funct);
            }
        else if (funct instanceof MathFunction)
            {
            return wrap((MathFunction) funct);
            }
        else if (funct instanceof UserDefinedFunction)
            {
            return wrap((UserDefinedFunction) funct);
            }
        else {
            log.error("Unexpected function type [{}][{}]", funct.getName(), funct.getClass().getName());
            return null ;
            }
        }

    /**
     * Wrap an ADQLFunction.
     *
     */
    public static AdqlQuery.SelectField wrap(final SQLFunction funct)
        {
        log.debug("wrap(SQLFunction)");
        log.debug("  name   [{}]", funct.getName());
        log.debug("  number [{}]", funct.isNumeric());
        log.debug("  string [{}]", funct.isString());

        switch (funct.getType())
            {
            case COUNT :
            case COUNT_ALL :
                return new SelectFieldImpl(
                    funct.getName(),
                    new Integer(0),
                    AdqlColumn.Type.LONG
                    );

            case AVG:
            case MAX:
            case MIN:
            case SUM:
                return new SelectFieldWrapper(
                    funct.getName(),
                    wrap(
                        funct.getParameter(0)
                        )
                    );

            default :
                log.error("Unexpected function type [{}][{}]", funct.getName(), funct.getType());
                return null ;
            }
        }

    /**
     * Wrap a MathFunction.
     *
     */
    public static AdqlQuery.SelectField wrap(final MathFunction funct)
        {
        log.debug("wrap(MathFunction)");
        log.debug("  name   [{}]", funct.getName());
        log.debug("  number [{}]", funct.isNumeric());
        log.debug("  string [{}]", funct.isString());
        return null ;
        }

    /**
     * Wrap a UserDefinedFunction.
     *
     */
    public static AdqlQuery.SelectField wrap(final UserDefinedFunction funct)
        {
        log.debug("wrap(UserDefinedFunction)");
        log.debug("  name   [{}]", funct.getName());
        log.debug("  number [{}]", funct.isNumeric());
        log.debug("  string [{}]", funct.isString());
        return null ;
        }

/*
 *
        AdqlQuery.SelectField field = null ;

        for (final ADQLOperand param : funct.getParameters())
            {
            final AdqlQuery.SelectField temp = wrap(
                param
                );
            if (field == null)
                {
                field = temp;
                }
            else {
                if (temp.arraysize().intValue() > field.arraysize().intValue())
                    {
                    field = temp ;
                    }
                }
            }
        return new SelectFieldWrapper(
            funct.getName(),
            field
            );
*
*/
    }
