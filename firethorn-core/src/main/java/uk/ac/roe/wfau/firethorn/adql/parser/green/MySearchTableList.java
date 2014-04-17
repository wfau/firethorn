/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.parser.green;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserTable;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;

import lombok.extern.slf4j.Slf4j;
import adql.db.DBTable;
import adql.db.SearchTableApi;
import adql.db.SearchTableList;
import adql.query.from.ADQLTable;

/**
 *
 *
 */
@Slf4j
public class MySearchTableList
implements SearchTableApi
    {

    /**
     * Wrapped implementation.
     * 
     */
    protected SearchTableList inner ;

    /**
     * Our AdqlResource.
     * 
     */
    private AdqlResource resource ;

    /**
     * Our AdqlResource.
     * 
     */
    public AdqlResource resource()
        {
        return this.resource;
        }

    /**
     * Our AdqlParserTable Factory.
     * 
     */
    private AdqlParserTable.Factory factory ;

    /**
     * Our AdqlParserTable Factory .
     * 
     */
    public AdqlParserTable.Factory factory()
        {
        return this.factory;
        }

    /**
     * Our AdqlQuery Mode.
     * 
     */
    private AdqlQuery.Mode mode ;

    /**
     * Our AdqlQuery Mode.
     * 
     */
    public AdqlQuery.Mode mode()
        {
        return this.mode;
        }
    
    /**
     * Public constructor.
     * 
    public MySearchTableList(final Collection<DBTable> collection)
        {
        log.debug("MySearchTableList(Collection<DBTable>)");
        inner = new SearchTableList(
            collection
            );
        }
     */

    /**
     * Public constructor.
     * 
     */
    public MySearchTableList(final AdqlResource resource, final AdqlParserTable.Factory factory, final AdqlQuery.Mode mode)
        {
        log.debug("MySearchTableList(AdqlResource, AdqlParserTable.Factory, AdqlQuery.Mode)");
        this.resource = resource;
        this.factory = factory;
        this.mode = mode ;
        //
        // Stuff to replace ...
/*
        final Set<DBTable> tables = new HashSet<DBTable>();
        for (final AdqlSchema temp : resource.schemas().select())
            {
            log.debug("  schema [{}]", temp.name());
            for (final AdqlTable table : temp.tables().select())
                {
                log.debug("  table  [{}][{}]", temp.name(), table.name());
                tables.add(
                    factory.create(
                        mode,
                        table
                        )
                    );
                }
            }
        inner = new SearchTableList(
            tables
            );
*/            
        }
    
    
    //@Override
    public List<DBTable> search(String table)
        {
        log.debug("search(String)");
        log.debug("  table   [{}]", table);
        //return inner.search(table);
        throw new UnsupportedOperationException(
            "search(String) not implemented"
            );
        }

    //@Override
    public List<DBTable> search(String catalog, String schema, String table)
        {
        log.debug("search(String, String, String, byte)");
        log.debug("  catalog [{}]", catalog);
        log.debug("  schema  [{}]", schema);
        log.debug("  table   [{}]", table);
        //return inner.search(catalog, schema, table);
        throw new UnsupportedOperationException(
            "search(String, String, String) not implemented"
            );
        }

    @Override
    public List<DBTable> search(ADQLTable target)
        {
        log.debug("search(ADQLTable)");
        log.debug("  target [{}][{}][{}]", target.getTableName(), target.getSchemaName(), target.getCatalogName());

        List<DBTable> tables = new ArrayList<DBTable>();
        for (AdqlSchema schema : resource.schemas().select())
            {
            AdqlTable found = schema.tables().search(
                target.getTableName()
                ); 
            if (found != null)
                {
                log.debug("  found [{}]", found.namebuilder().toString());
                tables.add(
                    factory.create(
                        mode,
                        found
                        )
                    );
                }
            }
        return tables ;
        }

/*    
    @Override
    public List<DBTable> search(String catalog, String schema, String table, byte caseSensitivity)
        {
        log.debug("search(String, String, String, byte)");
        log.debug("  catalog [{}]", catalog);
        log.debug("  schema  [{}]", schema);
        log.debug("  table   [{}]", table);
        log.debug("  case    [{}]", caseSensitivity);
        //return inner.search(catalog, schema, table, caseSensitivity);
        throw new UnsupportedOperationException(
            "search(String, String, String, byte) not implemented"
            );
        }
*/
    }
