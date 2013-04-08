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

    
    protected static class EvalResult
        {
        public EvalResult(String name)
            {
            this.name = name ;
            }
        public EvalResult(String name, EvalResult eval)
            {
            this.name = name ;
            if (eval != null)
                {
                this.info = eval.info();
                }
            }
        public EvalResult(String name, BaseColumn.Info info)
            {
            this.name = name ;
            this.info = info ;
            }
        private String name;
        public  String name()
            {
            return this.name;
            }
        private BaseColumn.Info info ;
        public  BaseColumn.Info info()
            {
            return this.info;
            }
        }
    
    /**
     * Evaluate the type of a select item.
     * 
     */
    protected EvalResult eval(SelectItem item)
        {
        log.debug("eval(SelectItem)");
        log.debug("  alias [{}]", item.getAlias());
        log.debug("  name  [{}]", item.getName());
        log.debug("  class [{}]", item.getClass().getName());
        return new EvalResult(
            item.getAlias(),
            eval(
                item.getOperand()
                )
            );
        }
        
    protected EvalResult eval(ADQLOperand oper)
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
            return new EvalResult(
                "oops"
                );
            }
        }
    
    /**
     * Simple eval for a column.
     * This is proof of concept only.
     *
     */
    protected EvalResult eval(ADQLColumn column)
        {
        log.debug("eval(ADQLColumn)");
        if (((ADQLColumn) column).getDBLink() instanceof AdqlDBColumn)
            {
            final AdqlColumn adql = ((AdqlDBColumn) ((ADQLColumn) column).getDBLink()).column();
            log.debug("  ----");
            log.debug("  adql [{}]", adql.fullname());
            log.debug("  base [{}]", adql.base().fullname());
            return new EvalResult(
                adql.name(),
                adql.base().info()
                );
            }
        else {
            return new EvalResult(
                "oops"
                );
            }
        }

    public int size(BaseColumn.Info info)
        {
        if (info == null)
            {
            return 0 ;
            }
        else {
            if (info.adql() == null)
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
        }

    public AdqlColumnType type(BaseColumn.Info info)
        {
        if (info == null)
            {
            return AdqlColumnType.UNKNOWN;
            }
        else {
            if (info.adql() == null)
                {
                return AdqlColumnType.UNKNOWN;
                }
            else {
                return info.adql().type();
                }
            }
        }

    /**
     * Simple eval for an operation, picks the largest param.
     * This is proof of concept only.
     * Needs a lot more work to get it right.
     *
     */
    protected EvalResult eval(Operation oper)
        {
        log.debug("eval(Operation)");
        log.debug("  name   [{}]", oper.getName());
        log.debug("  number [{}]", oper.isNumeric());
        log.debug("  string [{}]", oper.isString());

        BaseColumn.Info left = eval(
            oper.getLeftOperand()
            ).info(); 

        BaseColumn.Info right = eval(
            oper.getRightOperand()
            ).info(); 

        if (left == null)
            {
            return new EvalResult(
                oper.getName(),
                right
                );
            }
        else if (right == null)
            {
            return new EvalResult(
                oper.getName(),
                left
                );
            }
        else {
            if (size(left) > size(right))
                {
                return new EvalResult(
                    oper.getName(),
                    left
                    );
                }
            else {
                return new EvalResult(
                    oper.getName(),
                    right
                    );
                }
            }
        }

    /**
     * Simple eval for a function, picks the largest param.
     * This is proof of concept only.
     * Needs a lot more work to get it right.
     *
     */
    protected EvalResult eval(ADQLFunction funct)
        {
        log.debug("eval(ADQLFunction)");
        log.debug("  name   [{}]", funct.getName());
        log.debug("  number [{}]", funct.isNumeric());
        log.debug("  string [{}]", funct.isString());

        BaseColumn.Info info = null ;
        
        for (ADQLOperand param : funct.getParameters())
            {
            BaseColumn.Info temp = eval(
                param
                ).info(); 
            if (info == null)
                {
                info = temp;
                }
            else {
                if (temp.adql().size() > info.adql().size())
                    {
                    info = temp ;
                    }
                }
            }
        return new EvalResult(
            funct.getName(),
            info
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

            if (object instanceof ClauseSelect)
                {
                log.debug("  ----");
                log.debug("  ClauseSelect");
                for (SelectItem item : ((ClauseSelect) object))
                    {
                    log.debug("-- Select item ----");
                    log.debug(" alias  [{}]", item.getAlias());
                    EvalResult eval = eval(
                        item
                        );
                    log.debug(" name [{}]", eval.name());
                    log.debug(" type [{}]", type(eval.info()));
                    log.debug(" size [{}]", size(eval.info()));
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
