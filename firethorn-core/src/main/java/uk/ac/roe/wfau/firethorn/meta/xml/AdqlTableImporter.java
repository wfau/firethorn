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
package uk.ac.roe.wfau.firethorn.meta.xml;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.base.TreeComponent;

/**
 *
 *
 */
@Slf4j
public class AdqlTableImporter
    {

    /**
     * Public constructor.
     *
     */
    public AdqlTableImporter(final AdqlSchema schema)
        {
        this.schema = schema;
        this.tables = schema.tables();
        //
        // Cache our base tables.
        log.debug("Caching base tables for [{}]", schema.fullname());
        for (BaseTable<?,?> table : schema.base().tables().select())
            {
            log.debug("Caching base table [{}]", table.name());
            bases.put(
                table.name(),
                table
                );
            }
        //
        // Cache our existing tables.
        log.debug("Caching existing tables for [{}]", schema.fullname());
        for (AdqlTable table : schema.tables().select())
            {
            log.debug("Caching existing table [{}]", table.name());
            existing.put(
                table.name(),
                table
                );
            }
        }

    /**
     * Our parent schema.
     *
     */
    private AdqlSchema schema ;

    /**
     * Our parent schema.
     *
     */
    public AdqlSchema schema()
        {
        return this.schema;
        }

    /**
     * Our parent tables.
     *
     */
    private AdqlSchema.Tables tables ;

    /**
     * Our parent tables.
     *
     */
    public AdqlSchema.Tables tables()
        {
        return tables;
        }
    
    /**
     * Our cache of base tables.
     * 
     */
    private Map<String, BaseTable<?,?>> bases = new HashMap<String, BaseTable<?,?>>();

    /**
     * Our cache of existing tables.
     * 
     */
    private Map<String, AdqlTable> existing = new HashMap<String, AdqlTable>();

    /**
     * Our cache of matching tables.
     * 
     */
    private Map<String, AdqlTable> matching = new HashMap<String, AdqlTable>();
    
    /**
     * Import a table.
     *
     */
    public AdqlTable inport(final String name)
    throws NameNotFoundException
        {
        log.debug("Importing table [{}][{}]", schema.base().fullname(), name);
        //
        // Check for duplicate.
        AdqlTable match = matching.get(
            name
            );
        if (match != null)
            {
            log.warn("Duplicate table  found in import [{}][{}]", match.base().fullname(), match.fullname());
            }
        else {
            //
            // Check for existing.
            match = existing.get(
                name
                );
            //
            // If we found existing.
            if (match != null)
                {
                existing.remove(
                    name
                    );
                matching.put(
                    name,
                    match
                    );
                }
            //
            // If we didn't find existing.
            else {
                //
                // Find the corresponding base table.
                final BaseTable<?,?> base = bases.get(
                    name
                    );
                //
                // If we didn't find the corresponding base table.
                if (base == null)
                    {
                    log.warn("Unable to locate base table [{}][{}]", schema.base().fullname(), name);
                    throw new NameNotFoundException(
                        "Unable to locate base table [" + schema.base().fullname() + "][" + name + "]"
                        );
                    }
                //
                // If we found the corresponding base table.
                else {
                    match = schema.tables().create(
                        TreeComponent.CopyDepth.PARTIAL,
                        base,
                        name
                        );
                    matching.put(
                        name,
                        match
                        );
                    }
                }
            }
        return match ;
        }
    }
