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
package uk.ac.roe.wfau.firethorn.adql.parser;

import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;

import lombok.extern.slf4j.Slf4j;

import adql.db.DBColumn;
import adql.query.ADQLQuery;
import adql.query.ClauseSelect;
import adql.query.IdentifierField;
import adql.query.SelectAllColumns;
import adql.query.from.ADQLTable;
import adql.translator.ADQLTranslator;
import adql.translator.PostgreSQLTranslator;
import adql.translator.TranslationException;

/**
 * OGSA-DAI DQP ADQL Translator.
 * This needs to generate DQP compatible ADQL that can be passed to OGSA-DAI DQP.   
 * @todo Remove dependency on PostgreSQLTranslator
 *
 */
@Slf4j
public class OgsaDQPTranslator
    extends PostgreSQLTranslator
    implements ADQLTranslator
    {

    /**
     *
     *
     */
    public OgsaDQPTranslator()
        {
        super(
            false
            );
        }

    /**
     *
     *
     */
    public OgsaDQPTranslator(boolean column)
        {
        super(
            column
            );
        }

    /**
     *
     *
     */
    public OgsaDQPTranslator(boolean catalog, boolean schema, boolean table, boolean column)
        {
        super(
            catalog,
            schema,
            table,
            column
            );
        }

    /**
     * Copy of the PostgreSQLTranslator method ... 
     *
     */
    public String translate(final ADQLQuery query)
        throws TranslationException
        {
        log.debug("translate(ADQLQuery)");

        StringBuilder builder = new StringBuilder();
        builder.append(
            translate(
                query.getSelect()
                )
            );

        builder.append("\nFROM ").append(
            translate(
                query.getFrom()
                )
            );

        if (!query.getWhere().isEmpty())
            {
            builder.append('\n').append(
                translate(
                    query.getWhere()
                    )
                );
            }

        if (!query.getGroupBy().isEmpty())
            {
            builder.append('\n').append(
                translate(
                    query.getGroupBy()
                    )
                );
            }

        if (!query.getHaving().isEmpty())
            {
            builder.append('\n').append(
                translate(
                    query.getHaving()
                    )
                );
            }

        if (!query.getOrderBy().isEmpty())
            {
            builder.append('\n').append(
                translate(
                    query.getOrderBy()
                    )
                );
            }
        return builder.toString();
        }

    
    /**
     * Copy of the PostgreSQLTranslator method ... 
     *
     */
    public String translate(final ClauseSelect clause)
    throws TranslationException
        {
        log.debug("translate(ClauseSelect)");

        StringBuilder builder = new StringBuilder();
        for(int i=0; (i < clause.size()); i++)
            {
            if (i == 0)
                {
                builder.append(
                    clause.getName()
                    );
                if (clause.hasLimit())
                    {
                    builder.append(" TOP ").append(
                        clause.getLimit()
                        );
                    }
                if (clause.distinctColumns())
                    {
                    builder.append(
                        " DISTINCT "
                        );
                    }
                }
            else {
                builder.append(" ").append(
                    clause.getSeparator(i)
                    );
                }
            builder.append(" ").append(
                translate(clause.get(i)
                    )
                );
            }
        return builder.toString();
        }
    
    /**
     * Copy of the PostgreSQLTranslator method ...
     *  
     *
     */
    public String translate(final SelectAllColumns all)
    throws TranslationException
        {
        log.debug("translate(SelectAllColumns)");

// TODO Replace this with code that uses the Firethorn metadata.
// Need to make sure this ends up with the same list of fields as the JDBC table in user data. 
        
// Check if ADQLTable is a AdqlParserTable
// cast into AdqlParserTable and call table.table() to get a AdqlTable 
// process the column list from the AdqlTable. 
        
        HashMap<String, String> mapAlias = new HashMap<String, String>();
        
        // Fetch the full list of columns to display:
        Iterable<DBColumn> dbCols = null;
        if (all.getAdqlTable() != null)
            {
            ADQLTable table = all.getAdqlTable();
            log.debug("Table [{}][{}]", table.getName(), table.getClass().getName());
            if (table.getDBLink() != null)
                {
                dbCols = table.getDBLink();
                if (table.hasAlias())
                    {
                    String key = appendFullDBName(new StringBuffer(), table.getDBLink()).toString();
                    mapAlias.put(key, table.isCaseSensitive(IdentifierField.ALIAS) ? ("\""+table.getAlias()+"\"") : table.getAlias());
                    }
                }
            else {
// getDBLink is null - need to fix this
// Need to find where the ADQLTable is created
// and add a reference to the BaseTable that this refers to.
                log.warn("ADQLTable with no link to the BaseTable [{}]", table.getName());
                }
            }
        else if (all.getQuery() != null)
            {
            dbCols = all.getQuery().getFrom().getDBColumns();
            ArrayList<ADQLTable> tables = all.getQuery().getFrom().getTables();
            for(ADQLTable table : tables)
                {
                if (table.hasAlias())
                    {
                    String key = appendFullDBName(new StringBuffer(), table.getDBLink()).toString();
                    mapAlias.put(key, table.isCaseSensitive(IdentifierField.ALIAS) ? ("\""+table.getAlias()+"\"") : table.getAlias());
                    }
                }
            }

        // Write the DB name of all these columns:
        if (dbCols != null)
            {
            StringBuffer cols = new StringBuffer();
            for(DBColumn col : dbCols)
                {
                if (cols.length() > 0)
                    cols.append(',');
                if (col.getTable() != null)
                    {
                    String fullDbName = appendFullDBName(new StringBuffer(), col.getTable()).toString();
                    if (mapAlias.containsKey(fullDbName))
                        appendIdentifier(cols, mapAlias.get(fullDbName), false).append('.');
                    else
                        cols.append(fullDbName).append('.');
                    }
                appendIdentifier(cols, col.getDBName(), IdentifierField.COLUMN);
                cols.append(" AS ").append(col.getADQLName());
                }
            return cols.toString();
            }
        else{
            return all.toADQL();
            }
        }
    }
