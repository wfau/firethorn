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
package uk.ac.roe.wfau.firethorn.common.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.roe.wfau.firethorn.common.entity.exception.IdentifierFormatException;

/**
 *
 *
 */
public abstract class EntityLinkFactory<EntityType extends Entity>
implements Entity.LinkFactory<EntityType>
    {

    protected EntityLinkFactory(String path)
        {
        this.pattern = Pattern.compile(
            ".*/" + path + "/(\\p{Alnum}+).*"
            );
        }
    
    protected final Entity.IdentFactory idents = new EntityIdentFactory();
    protected final Pattern pattern ;

    @Override
    public Identifier parse(String string)
        {
        Matcher matcher = this.pattern.matcher(
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
