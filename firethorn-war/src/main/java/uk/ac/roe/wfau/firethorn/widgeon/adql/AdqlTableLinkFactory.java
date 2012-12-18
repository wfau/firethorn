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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlTable;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>AdqlTable</code>.
 * TODO
 *
 */
@Component
public class AdqlTableLinkFactory
extends WebappLinkFactory<TuesdayAdqlTable>
implements TuesdayAdqlTable.LinkFactory
    {
    /**
     * The URI path for individual tables.
     *
     */
    public static final String TABLE_PATH = "/adql/table/" + IDENT_TOKEN ;

    /**
     * The URI path for table columns.
     *
     */
    public static final String TABLE_COLUMN_PATH = TABLE_PATH + "/columns" ;

    @Override
    public String link(final TuesdayAdqlTable entity)
        {
        return this.link(
            TABLE_PATH,
            entity
            );
        }

    @Override
    public Identifier parse(String string)
        {
        return parse(
            pattern,
            string
            );
        }

    /**
     * Our local Identifier factory.
     * 
     */
    @Autowired
    private TuesdayAdqlTable.IdentFactory idents ;

    /**
     * Our link regular expression pattern.
     * 
     */
    protected static final Pattern pattern = Pattern.compile(
        ".*/adql/table/(\\p{Alnum}+).*"
        );

    protected Identifier parse(Pattern pattern, String string)
        {
        Matcher matcher = pattern.matcher(
            string
            );
        if (matcher.matches())
            {
            return this.idents.ident(
                matcher.group(1)
                );
            }
        else {
            throw new IdentifierFormatException(
                string
                );
            }
        }
    }
