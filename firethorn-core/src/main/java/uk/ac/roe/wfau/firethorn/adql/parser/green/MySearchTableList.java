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
import java.util.List;

import adql.db.DBTable;
import adql.db.SearchTableApi;
import adql.query.from.ADQLTable;
import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionError;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserTable;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Mode;
import uk.ac.roe.wfau.firethorn.exception.NotImplementedException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;

/**
 *
 *
 */
@Slf4j
public class MySearchTableList
implements SearchTableApi
    {

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
    private Mode mode ;

    /**
     * Our AdqlQuery Mode.
     * 
     */
    public Mode mode()
        {
        return this.mode;
        }
    
    /**
     * Public constructor.
     * 
     */
    public MySearchTableList(final AdqlResource resource, final AdqlParserTable.Factory factory, final Mode mode)
        {
        log.debug("MySearchTableList(AdqlResource, AdqlParserTable.Factory, AdqlQuery.Mode)");
        this.resource = resource;
        this.factory = factory;
        this.mode = mode ;
        }

    @Override
    public List<DBTable> search(ADQLTable target)
        {
        log.debug("search(ADQLTable)");
        log.debug("  target [{}][{}][{}]", target.getCatalogName(), target.getSchemaName(), target.getTableName());

        try {
            List<DBTable> tables = new ArrayList<DBTable>();
    
            if (target.getCatalogName() != null)
                {
                log.debug("search catalog [{}]", target.getCatalogName());
                throw new NotImplementedException();
                // Check the resource name ?
                }
                
            if (target.getSchemaName() != null)
                {
                log.debug("search schema [{}]", target.getSchemaName());
                AdqlSchema schema = resource.schemas().search(
                    target.getSchemaName()
                    ) ;
                if (schema != null)
                    {
                    log.debug("  schema [{}]", schema.namebuilder().toString());
                    AdqlTable table = schema.tables().search(
                        target.getTableName()
                        ); 
                    if (table != null)
                        {
                        log.debug("  table [{}]", table.namebuilder().toString());
                        tables.add(
                            factory.create(
                                mode,
                                table
                                )
                            );
                        }
                    }
                }
            else {
        		log.debug("null search schema");
                for (AdqlSchema schema : resource.schemas().select())
                    {
                	log.debug("schema [{}]", schema.name());
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
                }
            return tables ;
            }
        catch (final ProtectionException ouch)
            {
            throw new ProtectionError(
                ouch
                );
            }
        }
    }
