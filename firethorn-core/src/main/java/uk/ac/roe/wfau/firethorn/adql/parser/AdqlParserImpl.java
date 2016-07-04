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

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserQuery.DuplicateFieldException;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserTable.AdqlDBColumn;
import uk.ac.roe.wfau.firethorn.adql.parser.green.MyQueryChecker;
import uk.ac.roe.wfau.firethorn.adql.parser.green.MySearchTableList;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Mode;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import adql.db.DBColumn;
import adql.parser.ADQLParser;
import adql.parser.ADQLQueryFactory;
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
import adql.query.operand.NegativeOperand;
import adql.query.operand.NumericConstant;
import adql.query.operand.Operation;
import adql.query.operand.OperationType;
import adql.query.operand.StringConstant;
import adql.query.operand.WrappedOperand;
import adql.query.operand.function.ADQLFunction;
import adql.query.operand.function.CastFunction;
import adql.query.operand.function.MathFunction;
import adql.query.operand.function.SQLFunction;
import adql.query.operand.function.UserDefinedFunction;
import adql.query.operand.function.geometry.GeometryFunction;
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
        public AdqlParser create(final AdqlQuery.Mode mode, final AdqlResource resource)
            {
            return new AdqlParserImpl(
                this.tables,
                mode,
                resource
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
    protected AdqlParserImpl(final AdqlParserTable.Factory factory, final AdqlQuery.Mode mode, final AdqlResource resource)
        {
        this.mode = mode ;
        //
        // Create our ADQL parser.
        this.parser = new ADQLParser(
            new MyQueryChecker(
                new MySearchTableList(
                    resource,
                    factory,
                    mode
                    )
                ),
            new AdqlQueryFactoryImpl()
            );

        //this.parser.disable_tracing();

        }

    /**
     *  Factory for building an ADQL query representation.
     *
     */
    public static class AdqlQueryFactoryImpl
    extends ADQLQueryFactory
        {
        /**
         * Public constructor.
         *
         */
        public AdqlQueryFactoryImpl()
            {
            super();
            }

        /**
         * Public constructor.
         *
         */
        public AdqlQueryFactoryImpl(final boolean unknowns)
            {
            super();
            }

        /**
         * Create a SelectItem.
         *
         */
        public SelectItem createSelectItem(final ADQLOperand operand, final String alias, final ADQLQuery parent)
        throws Exception
            {
            log.debug("createSelectItem(ADQLOperand, String)");
            log.debug("  Oper [{}][{}]", operand.getName(), operand.getClass());
            return super.createSelectItem(
                operand,
                alias
                );
            }

        /**
         * Create a UserDefinedFunction.
         *
         */
        @Override
        public UserDefinedFunction createUserDefinedFunction(final String name, final ADQLOperand[] params)
        throws Exception
            {
            log.debug("createUserDefinedFunction(String, ADQLOperand[])");
            log.debug("  Name [{}]", name);

            //
            // SIGN()
            if ("sign".equalsIgnoreCase(name))
                {
                //ZRQ-UDF
                return null ;
                }
            //
            // SQUARE()
            if ("square".equalsIgnoreCase(name))
                {
                //ZRQ-UDF
                return null ;
                }
            //
            // Default
            else {
                // Need to check the function resource (catalog).
                // Some functions are schema specific.
                return super.createUserDefinedFunction(
                    name,
                    params
                    );
                }
            }

        /**
         * Class to represent a function defined within a schema.
         *
         */
        protected static class SchemaDefinedFunction
        extends UserDefinedFunction
            {

            @Override
            public boolean isNumeric()
                {
                // TODO Auto-generated method stub
                return false;
                }

            @Override
            public boolean isString()
                {
                // TODO Auto-generated method stub
                return false;
                }

            @Override
            public String getName()
                {
                // TODO Auto-generated method stub
                return null;
                }

            @Override
            public ADQLObject getCopy() throws Exception
                {
                // TODO Auto-generated method stub
                return null;
                }

            @Override
            public int getNbParameters()
                {
                // TODO Auto-generated method stub
                return 0;
                }

            @Override
            public ADQLOperand[] getParameters()
                {
                // TODO Auto-generated method stub
                return null;
                }

            @Override
            public ADQLOperand getParameter(final int index)
                throws ArrayIndexOutOfBoundsException
                {
                // TODO Auto-generated method stub
                return null;
                }

            @Override
            public ADQLOperand setParameter(final int index, final ADQLOperand replacer)
                throws ArrayIndexOutOfBoundsException, NullPointerException, Exception
                {
                // TODO Auto-generated method stub
                return null;
                }

			@Override
			public boolean isGeometry() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public String translate(ADQLTranslator caller) throws TranslationException {
				// TODO Auto-generated method stub
				return null;
			}

            }

        /**
         * Create a NumericConstant.
         *
         */
        @Override
        public NumericConstant createNumericConstant(final String value)
        throws NumberFormatException
            {
            log.debug("createNumericConstant(String)");
            log.debug("  value [{}]", value);
            // Only if in LEGACY or FUTURE mode.
            if (value.startsWith(HexConstant.PREFIX))
                {
                return new HexConstant(
                    value,
                    true
                    );
                }
            else {
                return new NumericConstant(
                    value,
                    true
                    );
                }
            }

        /**
         * A hexadecimal numeric.
         *
         */
        public static class HexConstant
        extends NumericConstant
        implements ADQLOperand
            {
            /**
             * Hexadecimal prefix.
             *
             */
            protected static final String PREFIX = "0x";

            /**
             * Create a hex value from a long.
             *
             */
            public HexConstant(final long value)
                {
                super(
                    value
                    );
                }

            /**
             * Create a hex value from a String.
             *
             */
            public HexConstant(final String value, final boolean check)
            throws NumberFormatException
                {
                super(
                    value,
                    check
                    );
                }

            /**
             * Copy a hex value.
             *
             */
            public HexConstant(final HexConstant origin)
                {
                super(
                    origin.getValue(),
                    false
                    );
                }

            /**
             * Warning - there may be rounding errors.
             *
             */
            @Override
            public final void setValue(final double value)
                {
                log.debug("setValue(double)");
                log.debug("  value [{}]", value);
                this.setValue(
                    (long) value
                    );
                }

            @Override
            public final void setValue(final long value)
                {
                log.debug("setValue(long)");
                log.debug("  value [{}]", value);
                this.value = PREFIX + Long.toHexString(value);
                }

            @Override
            public void setValue(final String value, final boolean check)
            throws NumberFormatException
                {
                log.debug("setValue(String, boolean)");
                log.debug("  value [{}]", value);
                log.debug("  check [{}]", check);
                if (check)
                    {
                    this.getDoubleValue(
                        value
                        );
                    }
                this.value = value;
                }

            /**
             * Warning - there may be rounding errors.
             *
             */
            public double getDoubleValue()
            throws NumberFormatException
                {
                return this.getDoubleValue(
                    this.value
                    );
                }

            /**
             * Warning - there may be rounding errors.
             *
             */
            public double getDoubleValue(final String value)
            throws NumberFormatException
                {
                log.debug("getDoubleValue()");
                log.debug("  value [{}]", value);
                String temp = value ;
                if (temp.startsWith(PREFIX))
                    {
                    temp = temp.substring(2);
                    }
                return new Double(
                    Long.parseLong(
                        temp,
                        16
                        )
                    );
                }

            public double getNumericValue()
                {
                log.debug("getNumericValue()");
                log.debug("  value [{}]", value);
                try {
                    return this.getDoubleValue();
                    }
                catch(final NumberFormatException ouch)
                    {
                    return Double.NaN;
                    }
                }

            @Override
            public ADQLObject getCopy()
                {
                log.debug("getCopy()");
                log.debug("  value [{}]", value);
                return new HexConstant(
                    this
                    );
                }
            }
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
                subject.cleaned()
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
            //log.warn("Error parsing query [{}]", ouch.getMessage());
            log.warn("Error parsing query [{}]", ouch);
            }
        catch (final AdqlParserException ouch)
            {
            subject.syntax(
                AdqlQuery.Syntax.State.PARSE_ERROR,
                ouch.getMessage()
                );
            //log.warn("Error parsing query [{}]", ouch.getMessage());
            log.warn("Error parsing query [{}]", ouch);
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
     * @throws AdqlParserException
     *
     */
    protected void process(final AdqlParserQuery subject, final ADQLQuery object)
    throws AdqlParserException
        {
        log.debug("process(AdqlParserQuery, ADQLQuery, ADQLQuery)");
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
                ),
            true
            );
        }

    /**
     * Recursively process a tree of ADQLObject(s).
     * @throws AdqlParserException
     *
     */
    protected void process(final AdqlParserQuery subject, final ADQLQuery query, final Iterable<ADQLObject> iter, Boolean additem)
    throws AdqlParserException
        {
        log.debug("process(AdqlParserQuery, ADQLQuery, Iterable<ADQLObject>)");
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
                    ((ClauseSelect) clause),
                    additem
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
            // Check for LEGACY operations.
            else if (clause instanceof Operation)
                {
                log.debug(" ----");
                log.debug(" Operation [{}]", ((Operation) clause).getName());
                process(
                    subject,
                    query,
                    ((Operation) clause)
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
                        ),
                    false
                    );
                }
            }
        }

    protected void legacy(final Level level, final Operation oper)
    throws AdqlParserException
        {
        if (level == Level.STRICT)
            {
            final OperationType type = oper.getOperation();
            if (type == OperationType.MOD)
                {
                throw new AdqlParserException(
                    "Modulo '%' operator is not supported in ADQL"
                    );
                }

            else if (type == OperationType.BIT_OR)
                {
                throw new AdqlParserException(
                    "Binary OR operator '|' is not supported in ADQL"
                    );
                }

            else if (type == OperationType.BIT_AND)
                {
                throw new AdqlParserException(
                    "Binary AND operator '&' is not supported in ADQL"
                    );
                }

            else if (type == OperationType.BIT_XOR)
                {
                throw new AdqlParserException(
                    "Binary XOR operator '^' is not supported in ADQL"
                    );
                }
            }
        }

    /**
     * Process an Operation.
     * @throws AdqlParserException
     *
     */
    protected void process(final AdqlParserQuery subject, final ADQLQuery query, final Operation oper)
    throws AdqlParserException
        {
        log.debug("process(AdqlParserQuery, ADQLQuery, Operation)");
        log.debug(" Operation ----");
        log.debug("  name [{}]", oper.getName());
        log.debug("  type [{}]", oper.getOperation());
        //
        // Check for LEGACY operations.
        legacy(
            subject.syntax().level(),
            oper
            );
        //
        // Process the rest of the chain
        process(
            subject,
            query,
            iter(
                oper
                ),
            true
            );
        }

    /**
     * Process the SELECT part of the query.
     *
     */
    protected void process(final AdqlParserQuery subject, final ADQLQuery query, final ClauseSelect clause, Boolean additem)
    throws AdqlParserException
        {
        log.debug("process(AdqlParserQuery, ADQLQuery, ClauseSelect)");
        for (final SelectItem item : clause)
            {
            log.debug(" Select item ----");
            log.debug("  name  [{}]", item.getName());
            log.debug("  alias [{}]", item.getAlias());
            log.debug("  class [{}]", item.getClass().getName());
            //
            // Specific case of SelectAll.
            if (item instanceof SelectAllColumns)
                {
            	if (additem){
	                process(
	                    subject,
	                    query,
	                    (SelectAllColumns) item
	                    );
	                }
                }
            //
            // Everything else ....
            else {
            	if (additem){
	                additem(
	                    subject,
	                    wrap(
	                        item
	                        )
	                    );
            		}
                }
            }
        }

    /**
     * Process a 'SELECT *' construct.
     * TODO Replace the SelectAllColumns with a list of columns generated from the AdqlTable(s).
     *
     */
    protected void process(final AdqlParserQuery subject, final ADQLQuery query, final SelectAllColumns selectall)
    throws DuplicateFieldException
        {
        log.debug("process(AdqlParserQuery, ADQLQuery, SelectAllColumns)");
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
        log.debug("process(AdqlParserQuery, ADQLQuery, ADQLColumn)");
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
        log.debug("process(AdqlParserQuery, ADQLQuery, ADQLTable)");
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
     * Add a SELECT field to a query.
     * @Deprecated - only used by SelectAll
     *
     */
    @Deprecated
    public void additem(final AdqlParserQuery subject,final MySelectField field)
    throws DuplicateFieldException
        {
        subject.add(
            field
            );
        }

    /**
     * Add a SELECT item to a query.
     * TODO Try renaming the item ?
     *
     */
    public void additem(final AdqlParserQuery subject,final MySelectItem item)
    throws DuplicateFieldException
        {
        // Check the name is valid.
        // Adds generated alias if needed.
		//if (item.item().isMain()){
			subject.add(
				item
				);
    	//	}
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
    throws DuplicateFieldException
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
     * TODO Searches the query FromContent for a matching table.
     * TODO Needs to replace the ADQLQuery with our own list of columns.
     *
     */
    protected void fields(final AdqlParserQuery subject, final ADQLQuery query, final ADQLTable table, final FromContent from)
    throws DuplicateFieldException
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
     * TODO Needs to replace the ADQLQuery with our own list of columns.
     *
     */
    protected void fields(final AdqlParserQuery subject, final ADQLQuery query)
    throws DuplicateFieldException
        {
        log.debug("fields(AdqlParserQuery, ADQLQuery)");
        log.debug("  ADQLQuery [{}]", query.getName());
        for (final DBColumn column : query.getResultingColumns())
            {
            log.debug("    DBColumn [{}][{}]", column.getADQLName(), column.getClass().getName());
            if (column instanceof AdqlDBColumn)
                {
                additem(
                    subject,
                    wrap(
                        ((AdqlDBColumn) column).column()
                        )
                    );
                }
            }
        }

    /**
     * Add all the fields from an AdqlParserTable.
     * TODO This is deceptive
     * The Firethorn query contains the list of columns from the Firethorn table.
     * The CDQ query still contains the list of columns from the root database table.
     *
     */
    protected void fields(final AdqlParserQuery subject, final AdqlParserTable table)
    throws DuplicateFieldException
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
    throws DuplicateFieldException
        {
        log.debug("fields(AdqlParserQuery, AdqlTable)");
        log.debug("  AdqlTable [{}]", table.namebuilder());
        log.debug("  BaseTable [{}]", table.base().namebuilder());
        log.debug("  RootTable [{}]", table.root().namebuilder());
        for (final AdqlColumn column : table.columns().select())
            {
            additem(
                subject,
                wrap(
                    column
                    )
                );
            }
        }

    public static final MySelectField UNKNOWN_FIELD = new MySelectFieldImpl(
        "unknown",
        AdqlColumn.AdqlType.UNKNOWN
        );

    /**
     * Local copy of the AdqlQuery interface.
     *
     */
    public static interface MySelectField
    extends AdqlQuery.SelectField
        {
        }

    /**
     * Extends the SelectField interface to include a SelectItem.
     *
     */
    public static interface MySelectItem
    extends MySelectField
        {
        public String alias();
        public void alias(final String alias);

        public SelectItem item();

        }

    /**
     * Inner class to represent a static SelectField.
     *
     */
    public static class MySelectFieldImpl
    implements MySelectField
        {

        private MySelectFieldImpl(final String name, final AdqlColumn.AdqlType type)
            {
            this(
                name,
                type,
                AdqlColumn.NON_ARRAY_SIZE
                );
            }

        private MySelectFieldImpl(final String name, final AdqlColumn.AdqlType type, final Integer size)
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

        private final Integer size;
        @Override
        public Integer arraysize()
            {
            return this.size;
            }

        private final AdqlColumn.AdqlType type;
        @Override
        public AdqlColumn.AdqlType type()
            {
            return this.type;
            }
        }

    /**
     * Inner class to wrap a SelectField with an optional change of name and type.
     *
     */
    public static class MySelectFieldWrapper
    implements MySelectField
        {
        private MySelectFieldWrapper(final MySelectField field)
            {
            this(
                field.name(),
                field.type(),
                field
                );
            }

        private MySelectFieldWrapper(final String name, final MySelectField field)
        	{
            this(
	            name,
                field.type(),
	            field
	            );
        	}

        private MySelectFieldWrapper(final String name, final AdqlColumn.AdqlType type, final MySelectField field)
            {
            this.name  = name  ;
            this.type  = type  ;
            this.field = field ;
            }

        private final String name;
        @Override
        public String name()
            {
        	if (this.name != null)
        		{
        		return this.name;
        		}
        	else {
        		return field().name();
        		}
            }

        private final AdqlColumn.AdqlType type;
        @Override
        public AdqlColumn.AdqlType type()
            {
        	if (this.type != null)
        		{
        		return this.type;
        		}
        	else {
        		return field().type();
        		}
            }

        private final MySelectField field;
        public MySelectField field()
            {
            return this.field;
            }

        @Override
        public Integer arraysize()
            {
            return this.field.arraysize();
            }
        }

    /**
     * Inner class to wrap a SelectItem.
     *
     */
    public static class MySelectItemWrapper
    implements MySelectItem
        {
        protected MySelectItemWrapper(final SelectItem item)
        throws AdqlParserException
            {
            this.item  = item;
            this.field = wrap(
                item.getOperand()
                );
            }

        private final SelectItem item ;
        @Override
        public SelectItem item()
            {
            return this.item;
            }
        @Override
        public String alias()
            {
            return item.getAlias();
            }
        @Override
        public void alias(final String alias)
            {
            item.setAlias(
                alias
                );
            }
        @Override
        public String name()
            {
            return ((item.hasAlias()) ? item.getAlias() : item.getName());
            }

        private final MySelectField field;
        public MySelectField field()
            {
            return this.field ;
            }
        @Override
        public Integer arraysize()
            {
            return this.field.arraysize();
            }
        @Override
        public AdqlColumn.AdqlType type()
            {
            return this.field.type();
            }
        }

    /**
     * Inner class to wrap an ADQLOperand, adding the operand type.
     *
     */
    public static class MySelectOperWrapper
    extends MySelectFieldImpl
    implements MySelectField
        {
        protected MySelectOperWrapper(final ADQLOperand oper, final AdqlColumn.AdqlType type)
            {
            this(
                oper,
                type,
                AdqlColumn.NON_ARRAY_SIZE
                );
            }

        protected MySelectOperWrapper(final ADQLOperand oper, final AdqlColumn.AdqlType type, final Integer arraysize)
            {
            super(
                oper.getName(),
                type,
                arraysize
                );
            this.oper = oper;
            }

        private final ADQLOperand oper ;
        public ADQLOperand oper()
            {
            return this.oper;
            }
        }

    /**
     * Inner class to wrap an AdqlColumn, with an optional name change.
     *
     */
    public static class MyAdqlColumnWrapper
    implements MySelectField
        {

        private MyAdqlColumnWrapper(final AdqlColumn adql)
            {
            this(
                adql.name(),
                adql
                );
            }

        private MyAdqlColumnWrapper(final String name, final AdqlColumn adql)
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
        public Integer arraysize()
            {
            return this.adql.meta().adql().arraysize();
            }
        @Override
        public AdqlColumn.AdqlType type()
            {
            return this.adql.meta().adql().type();
            }
        }

    /**
     * Wrap a SelectItem.
     *
     */
    public static MySelectItem wrap(final SelectItem item)
    throws AdqlParserException
        {
        log.trace("wrap(SelectItem)");
        log.trace("  alias [{}]", item.getAlias());
        log.trace("  name  [{}]", item.getName());
        log.trace("  class [{}]", item.getClass().getName());
        return new MySelectItemWrapper(
            item
            );
        }

    /**
     * Get the type for an ADQLOperand.
     *
     */
    protected static AdqlColumn.AdqlType type(final ADQLOperand operand)
    throws AdqlParserException
        {
        log.debug("type(ADQLOperand)");
        log.debug("  operand [{}]", operand);

        if (operand == null)
            {
            return null;
            }
        else if (operand instanceof WrappedOperand)
	        {
	        return type(
        		((WrappedOperand) operand).getOperand()
        		);
	        }
        else if (operand instanceof StringConstant)
            {
            return AdqlColumn.AdqlType.CHAR;
            }
        else if (operand instanceof NumericConstant)
            {
            return type((NumericConstant) operand);
            }
        else if (operand instanceof NegativeOperand)
            {
            return type((NegativeOperand) operand);
            }
        else if (operand instanceof ADQLColumn)
            {
            return type((ADQLColumn) operand);
            }
        else if (operand instanceof ADQLFunction)
            {
            return type((ADQLFunction) operand);
            }
        else if (operand instanceof Operation)
            {
            return type((Operation) operand);
            }
        else {
            throw new AdqlParserException(
                "Unknown operand type [" + operand.getName() + "][" + operand.getClass().getName() + "]"
                );
            }
        }

    /**
     * Wrap an ADQLOperand.
     *
     */
    public static MySelectField wrap(final ADQLOperand oper)
    throws AdqlParserException
        {
        log.trace("wrap(ADQLOperand)");
        log.trace("  name   [{}]", oper.getName());
        log.trace("  class  [{}]", oper.getClass().getName());
        log.trace("  number [{}]", oper.isNumeric());
        log.trace("  string [{}]", oper.isString());

        if (oper instanceof WrappedOperand)
	        {
            return new MySelectOperWrapper(
                oper,
                type(
                    (WrappedOperand)oper
                    )
                );
	        }
        else if (oper instanceof StringConstant)
            {
            return new MySelectOperWrapper(
                oper,
                AdqlColumn.AdqlType.CHAR,
                ((StringConstant) oper).getValue().length()
                );
            }
        else if (oper instanceof NumericConstant)
            {
            return new MySelectOperWrapper(
                oper,
                type(
                    (NumericConstant)oper
                    )
                );
            }
        else if (oper instanceof NegativeOperand)
            {
            return wrap(
                (NegativeOperand) oper
                );
            }
        else if (oper instanceof ADQLColumn)
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
            throw new AdqlParserException(
                "Unknown ADQLOperand class [" + oper.getClass().getName() + "]"
                );
            }
        }

    /**
     * Get the type of an ADQLColumn.
     *
     */
    protected static AdqlColumn.AdqlType type(final ADQLColumn column)
    throws AdqlParserException
        {
        log.debug("type(ADQLColumn)");
        log.debug("  column [{}]", column);

        if (column == null)
            {
            return null;
            }
        else if (column.getDBLink() instanceof AdqlDBColumn)
            {
            return ((AdqlDBColumn)column.getDBLink()).column().meta().adql().type();
            }
        else {
            throw new AdqlParserException(
                "Unknown column type [" + column.getName() + "][" + column.getClass().getName() + "]"
                );
            }
        }

    /**
     * Wrap an ADQLColumn.
     *
     */
    public static MySelectField wrap(final ADQLColumn column)
    throws AdqlParserException
        {
        log.trace("wrap(ADQLColumn)");
        log.trace("  name   [{}]", column.getName());
        log.trace("  class  [{}]", column.getClass().getName());
        if (column.getDBLink() == null)
            {
            throw new AdqlParserException(
                "ADQLColumn with null DBLink [" + column.getName() + "]"
                );
            }
        else if (column.getDBLink() instanceof AdqlDBColumn)
            {
            return wrap(
                ((AdqlDBColumn) (column.getDBLink())).column()
                );
            }
        else {
            throw new AdqlParserException(
                "ADQLColumn with unknown DBLink class [" + column.getDBLink().getClass().getName() + "]"
                );
            }
        }

    /**
     * Wrap an AdqlColumn.
     *
     */
    public static MySelectField wrap(final AdqlColumn column)
        {
        log.trace("wrap(AdqlColumn)");
        log.trace("  adql [{}]", column.namebuilder());
        log.trace("  base [{}]", column.base().namebuilder());
        log.trace("  root [{}]", column.root().namebuilder());
        log.trace("  type [{}]", column.meta().adql().type());
        return new MyAdqlColumnWrapper(
            column
            );
        }

    /**
     * Wrap a Negative Operand.
     *
     */
    public static MySelectField wrap(final NegativeOperand oper)
    throws AdqlParserException
        {
        log.trace("wrap(NegativeOperand)");
        log.trace("  name   [{}]", oper.getName());
        log.trace("  number [{}]", oper.isNumeric());
        log.trace("  string [{}]", oper.isString());
        return new MySelectFieldImpl(
    		oper.getName(),
    		type(
    		    oper.getOperand()
    		    )
            );
        }

    /**
     * Get the smallest type for a numeric value.
     *
     */
    protected static AdqlColumn.AdqlType type(final NumericConstant number)
        {
        log.debug("type(NumericConstant)");
        log.debug("  number [{}]", number);
        //
        // Check for a floating point number.
        if (number.getValue().contains("."))
            {
            double value = number.getNumericValue();
            if (value < 0)
                {
                value *= -1.0 ;
                }
            if ((value >= Float.MIN_VALUE) && (value <= Float.MAX_VALUE))
                {
                return AdqlColumn.AdqlType.FLOAT;
                }
            else {
                return AdqlColumn.AdqlType.DOUBLE;
                }
            }
        //
        // Not a floating point number.
        else {
            final long value = (long) number.getNumericValue();
            if ((value >= Integer.MIN_VALUE) && (value <= Integer.MAX_VALUE))
                {
                return AdqlColumn.AdqlType.INTEGER;
                }
            else {
                return AdqlColumn.AdqlType.LONG;
                }
            }
        }

    /**
     * Get the type of an Operation.
     *
     */
    public static AdqlColumn.AdqlType type(final Operation oper)
    throws AdqlParserException
        {
        log.debug("type(Operation)");
        log.debug("  name   [{}]", oper.getName());
        log.debug("  number [{}]", oper.isNumeric());
        log.debug("  string [{}]", oper.isString());

        final AdqlColumn.AdqlType t1 = type(oper.getLeftOperand());
        final AdqlColumn.AdqlType t2 = type(oper.getRightOperand());

        if ((t1 == AdqlColumn.AdqlType.DOUBLECOMPLEX) || (t2 == AdqlColumn.AdqlType.DOUBLECOMPLEX))
            {
            return AdqlColumn.AdqlType.DOUBLECOMPLEX;
            }

        else if ((t1 == AdqlColumn.AdqlType.FLOATCOMPLEX) || (t2 == AdqlColumn.AdqlType.FLOATCOMPLEX))
            {
            return AdqlColumn.AdqlType.FLOATCOMPLEX;
            }

        else if ((t1 == AdqlColumn.AdqlType.DOUBLE) || (t2 == AdqlColumn.AdqlType.DOUBLE))
            {
            return AdqlColumn.AdqlType.DOUBLE;
            }

        else if ((t1 == AdqlColumn.AdqlType.FLOAT) || (t2 == AdqlColumn.AdqlType.FLOAT))
            {
            return AdqlColumn.AdqlType.FLOAT;
            }

        else if ((t1 == AdqlColumn.AdqlType.LONG) || (t2 == AdqlColumn.AdqlType.LONG))
            {
            return AdqlColumn.AdqlType.LONG;
            }

        else if ((t1 == AdqlColumn.AdqlType.INTEGER) || (t2 == AdqlColumn.AdqlType.INTEGER))
            {
            return AdqlColumn.AdqlType.INTEGER;
            }

        else if ((t1 == AdqlColumn.AdqlType.SHORT) || (t2 == AdqlColumn.AdqlType.SHORT))
            {
            return AdqlColumn.AdqlType.SHORT;
            }

        else if ((t1 == AdqlColumn.AdqlType.BYTE) || (t2 == AdqlColumn.AdqlType.BYTE))
            {
            return AdqlColumn.AdqlType.BYTE;
            }

        else if ((t1 == AdqlColumn.AdqlType.BIT) || (t2 == AdqlColumn.AdqlType.BIT))
            {
            return AdqlColumn.AdqlType.BIT;
            }

        // Probably wrong BOOLEAN + BIT = .... ?
        else if ((t1 == AdqlColumn.AdqlType.BOOLEAN) || (t2 == AdqlColumn.AdqlType.BOOLEAN))
            {
            return AdqlColumn.AdqlType.BOOLEAN;
            }

        // UNICODE plus anything is probably wrong.
        // Check UNICODE + BYTE = ... ?
        else if ((t1 == AdqlColumn.AdqlType.UNICODE) || (t2 == AdqlColumn.AdqlType.UNICODE))
            {
            return AdqlColumn.AdqlType.UNICODE;
            }

        // Check CHAR + BYTE = ... ?
        else if ((t1 == AdqlColumn.AdqlType.CHAR) || (t2 == AdqlColumn.AdqlType.CHAR))
            {
            return AdqlColumn.AdqlType.CHAR;
            }

        else {
            throw new AdqlParserException(
                "Unknown type for operation params [" + t1 + "][" + t2 + "]"
                );
            }
        }

    /**
     * Wrap an Operation.
     *
     */
    public static MySelectField wrap(final Operation oper)
    throws AdqlParserException
        {
        log.trace("wrap(Operation)");
        log.trace("  name   [{}]", oper.getName());
        log.trace("  number [{}]", oper.isNumeric());
        log.trace("  string [{}]", oper.isString());
        return new MySelectFieldImpl(
            "operation",
            type(
                oper
                )
            );
        }

    /**
     * Get the type of an ADQLFunction.
     *
     */
    public static AdqlColumn.AdqlType type(final ADQLFunction funct)
    throws AdqlParserException
        {
        log.debug("type(ADQLFunction)");
        if (funct instanceof SQLFunction)
            {
            return type((SQLFunction) funct);
            }
        else if (funct instanceof MathFunction)
            {
            return type((MathFunction) funct);
            }
        else if (funct instanceof CastFunction)
	        {
	        return type((CastFunction) funct);
	        }
        else if (funct instanceof UserDefinedFunction)
            {
            return type((UserDefinedFunction) funct);
            }
        else {
            throw new AdqlParserException(
                "Unknown ADQLFunction class [" + funct.getName() + "][" + funct.getClass().getName() + "]"
                );
            }
        }

    /**
     * Wrap an ADQLFunction.
     *
     */
    public static MySelectField wrap(final ADQLFunction funct)
    throws ArrayIndexOutOfBoundsException, AdqlParserException
        {
        log.trace("wrap(ADQLFunction)");
        log.trace("  name   [{}]", funct.getName());
        log.trace("  number [{}]", funct.isNumeric());
        log.trace("  string [{}]", funct.isString());
        if (funct instanceof SQLFunction)
            {
            return wrap((SQLFunction) funct);
            }
        else if (funct instanceof MathFunction)
            {
            return wrap((MathFunction) funct);
            }
        else if (funct instanceof CastFunction)
	        {
	        return wrap((CastFunction) funct);
	        }
        else if (funct instanceof UserDefinedFunction)
            {
            return wrap((UserDefinedFunction) funct);
            }
        else if (funct instanceof GeometryFunction)
        	{
        	return wrap((GeometryFunction) funct);
        	}
        
        else {
            throw new AdqlParserException(
                "Unknown ADQLFunction class [" + funct.getName() + "][" + funct.getClass().getName() + "]"
                );
            }
        }

    /**
     * Get the type of a SQLFunction.
     *
     */
    public static AdqlColumn.AdqlType type(final SQLFunction funct)
    throws AdqlParserException
        {
        log.debug("type(SQLFunction)");
        switch (funct.getType())
            {
            case COUNT :
            case COUNT_ALL :
                return AdqlColumn.AdqlType.LONG;

            case AVG:
            case MAX:
            case MIN:
            case SUM:
                return type(
                    funct.getParameter(0)
                    );

            default :
                throw new AdqlParserException(
                    "Unknown ADQLFunction type [" + funct.getName() + "][" + funct.getType() + "]"
                    );
            }
        }

    /**
     * Wrap a SQLFunction.
     *
     */
    public static MySelectField wrap(final SQLFunction funct)
    throws ArrayIndexOutOfBoundsException, AdqlParserException
        {
        log.trace("wrap(SQLFunction)");
        log.trace("  name   [{}]", funct.getName());
        log.trace("  number [{}]", funct.isNumeric());
        log.trace("  string [{}]", funct.isString());
        switch (funct.getType())
            {
            case COUNT :
            case COUNT_ALL :
                return new MySelectFieldImpl(
                    funct.getName(),
                    AdqlColumn.AdqlType.LONG
                    );

            case AVG:
            case MAX:
            case MIN:
            case SUM:
                return new MySelectFieldWrapper(
                    funct.getName(),
                    wrap(
                        funct.getParameter(0)
                        )
                    );

            default :
                throw new AdqlParserException(
                    "Unknown ADQLFunction type [" + funct.getName() + "][" + funct.getType() + "]"
                    );
            }
        }

    /**
     * Get the floating point type of an ADQLOperand.
     *
     */
    public static AdqlColumn.AdqlType ftype(final ADQLOperand oper)
    throws AdqlParserException
        {
        log.debug("ftype(ADQLOperand)");
        switch (type(oper))
            {
            case BIT:
            case BYTE:
            case SHORT:
            case INTEGER:
            case FLOAT:
                return AdqlColumn.AdqlType.FLOAT;

            case LONG:
            case DOUBLE:
                return AdqlColumn.AdqlType.DOUBLE;

            default :
                throw new AdqlParserException(
                    "Invalid floating point type [" + oper.getName() + "]"
                    );
            }
        }

    /**
     * Get the type of a MathFunction.
     *
     */
    public static AdqlColumn.AdqlType type(final MathFunction funct)
    throws AdqlParserException
        {
        log.debug("type(MathFunction)");
        switch (funct.getType())
            {
            case ABS:
            case MOD:
            case ROUND:
            case TRUNCATE:
                return ftype(
                    funct.getParameter(0)
                    );

            case FLOOR:
            case CEILING:
                return ftype(
                    funct.getParameter(0)
                    );

            case LOG:
            case LOG10:
            case POWER:
            case SQUARE:
            case DEGREES:
            case RADIANS:
            case RAND:
            case ACOS:
            case ASIN:
            case ATAN:
            case ATAN2:
            case COS:
            case COT:
            case SIN:
            case TAN:
            case PI:
            case SQRT:
            case EXP:
                return AdqlColumn.AdqlType.DOUBLE;
            case SIGN:
            	 return AdqlColumn.AdqlType.INTEGER;
            default :
                throw new AdqlParserException(
                    "Unknown MathFunction type [" + funct.getName() + "][" + funct.getType() + "]"
                    );
            }
        }

    /**
     * Get the type of a CastFunction.
     *
     */
    public static AdqlColumn.AdqlType type(final CastFunction funct)
    throws AdqlParserException
        {
        log.debug("type(CastFunction)");
        switch(funct.type())
        	{
        	case SHORT:
        	case SMALLINT:
        		return AdqlColumn.AdqlType.SHORT;
        	
        	case INT :
        	case INTEGER :
        		return AdqlColumn.AdqlType.INTEGER;

        	case LONG:
        	case BIGINT:
        		return AdqlColumn.AdqlType.LONG;

        	case FLOAT:
        		return AdqlColumn.AdqlType.FLOAT;

        	case DOUBLE:
        		return AdqlColumn.AdqlType.DOUBLE;

        	default :
                throw new AdqlParserException(
                    "Unknown CastFunction type [" + funct.type() + "]"
                    );
        	}
        }

    /**
     * Wrap a MathFunction.
     *
     */
    public static MySelectField wrap(final MathFunction funct)
    throws AdqlParserException
        {
        log.trace("wrap(MathFunction)");
        log.trace("  name   [{}]", funct.getName());
        log.trace("  number [{}]", funct.isNumeric());
        log.trace("  string [{}]", funct.isString());

        switch (funct.getType())
            {

            case ABS:
            case MOD:
            case ROUND:
            case TRUNCATE:
                return new MySelectFieldWrapper(
                    funct.getName(),
                    wrap(
                        funct.getParameter(0)
                        )
                    );

            case FLOOR:
            case CEILING:
                return new MySelectFieldWrapper(
                    funct.getName(),
                    wrap(
                        funct.getParameter(0)
                        )
                    );

            case LOG:
            case LOG10:
            case POWER:
            case SQUARE:
            case DEGREES:
            case RADIANS:
            case RAND:
            case ACOS:
            case ASIN:
            case ATAN:
            case ATAN2:
            case COS:
            case COT:
            case SIN:
            case TAN:
            case PI:
            case SQRT:
            case EXP:
                return new MySelectFieldImpl(
                    funct.getName(),
                    AdqlColumn.AdqlType.DOUBLE
                    );
            case SIGN:
          	  return new MySelectFieldImpl(
                        funct.getName(),
                        AdqlColumn.AdqlType.INTEGER
                        );
            default :
                throw new AdqlParserException(
                    "Unknown MathFunction type [" + funct.getName() + "][" + funct.getType() + "]"
                    );
            }
        }

    /**
     * Wrap a CastFunction.
     *
     */
    public static MySelectField wrap(final CastFunction funct)
    throws AdqlParserException
        {
        log.trace("wrap(castFunction)");
        log.trace("  name   [{}]", funct.type());

        final ADQLOperand inner = funct.oper(); 
        if (inner instanceof ADQLColumn)
        	{
            return new MySelectFieldWrapper(
                inner.getName(),
                type(
            		funct
            		),
                wrap(
                    funct.oper()
                    )
                );
        	}
        else {
            return new MySelectFieldWrapper(
                "CASTED",
                type(
            		funct
            		),
                wrap(
                    funct.oper()
                    )
                );
        	}
        
        }
    
    /**
     * Hard coded set of UserDefinedFunctions for the OSA Altas catalog.
     *
     */
    public static MySelectField wrap(final UserDefinedFunction funct)
        {
	        log.trace("wrap(UserDefinedFunction)");
	        log.trace("  name   [{}]", funct.getName());
	        log.trace("  number [{}]", funct.isNumeric());
	        log.trace("  string [{}]", funct.isString());
	        final String name = funct.getName();
// TODO
// STRING == WON'T WORK
	        if ((name=="fDMS") ||
	        	(name=="fDMSbase") ||
	        	(name=="fHMS") ||
	        	(name=="fHMSbase")) {

	        	   return new MySelectFieldImpl(
				           name,
				           AdqlColumn.AdqlType.CHAR,
				           new Integer(32)
				           );

	        } else if (name=="fGreatCircleDist") {
				   return new MySelectFieldImpl(
						   name,
				           AdqlColumn.AdqlType.DOUBLE
				           );

		    } else {
		    	 return new MySelectFieldImpl(
		    			  name,
				           AdqlColumn.AdqlType.CHAR,
				           new Integer(32)
				           );
	        }
        }
    
    /**
     * Wrap a SQLFunction.
     *
     */
    public static MySelectField wrap(final GeometryFunction funct)
    throws ArrayIndexOutOfBoundsException, AdqlParserException
        {
        log.trace("wrap(GeometryFunction)");
        log.trace("  name   [{}]", funct.getName());
        log.trace("  number [{}]", funct.isNumeric());
        log.trace("  string [{}]", funct.isString());
      
        	return new MySelectFieldImpl(
        			funct.getName(),
		           AdqlColumn.AdqlType.INTEGER
		           );

         
        }
    }

