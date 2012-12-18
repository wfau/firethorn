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
package uk.ac.roe.wfau.firethorn.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlTable;

/**
 * JUnit test implementation.
 *
 */
@Component
public class TuesdayAdqlTableLinkFactory
implements TuesdayAdqlTable.LinkFactory
    {
    /**
     * Our local Identifier factory.
     * 
     */
    @Autowired
    private TuesdayAdqlTable.IdentFactory idents ;

    protected static final Pattern pattern = Pattern.compile(
        ".*/adql/table/(\\p{Alnum}+).*"
        );

    @Override
    public Identifier parse(String string)
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

    @Override
    public String link(TuesdayAdqlTable entity)
        {
        return null;
        }
    }
