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
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;

/**
 *
 *
 */
@Slf4j
public class AdqlColumnImporter
    {
    /**
     * Public constructor.
     * @throws ProtectionException 
     * 
     */
    public AdqlColumnImporter(final AdqlTable table)
    throws ProtectionException
        {
        this.table = table ;
        //
        // Cache our base columns.
        log.debug("Caching base columns for [{}]", table.fullname());
        for (BaseColumn<?> column : table.base().columns().select())
            {
            log.debug("Caching base column [{}]", column.name());
            bases.put(
                column.name(),
                column
                );
            }
        //
        // Cache our existing columns.
        log.debug("Caching existing columns for [{}]", table.fullname());
        for (AdqlColumn column : table.columns().select())
            {
            log.debug("Caching existing column [{}]", column.name());
            existing.put(
                column.name(),
                column
                );
        	}
        }

    /**
     * Our parent table.
     * 
     */
    private AdqlTable table;

    /**
     * Our parent table.
     * 
     */
    public AdqlTable table()
        {
        return this.table;
        }

    /**
     * Our cache of base columns.
     * 
     */
    private Map<String, BaseColumn<?>> bases = new HashMap<String, BaseColumn<?>>();

    /**
     * Our cache of existing columns.
     * 
     */
    private Map<String, AdqlColumn> existing = new HashMap<String, AdqlColumn>();

    /**
     * Our cache of matching columns.
     * 
     */
    private Map<String, AdqlColumn> matching = new HashMap<String, AdqlColumn>();
    
    public AdqlColumn inport(final String name)
    throws ProtectionException, NameNotFoundException
        {
        log.debug("Importing column [{}][{}]", table.base().fullname(), name);
        //
        // Check for duplicate.
        AdqlColumn match = matching.get(
            name
            );
        if (match != null)
            {
            log.warn("Duplicate column found in import [{}][{}]", match.base().fullname(), match.fullname());
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
                // Find the corresponding base column.
                final BaseColumn<?> base = bases.get(
                    name
                    );
                //
                // If we didn't find the corresponding base column.
                if (base == null)
                    {
                    log.warn("Unable to locate base column [{}][{}]", table.base().fullname(), name);
                    throw new NameNotFoundException(
                        "Unable to locate base column [" + table.base().fullname() + "][" + name + "]"
                        );
                    }
                //
                // If we found the corresponding base column.
                else {
                    match = table.columns().create(
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
