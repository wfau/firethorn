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
package uk.ac.roe.wfau.firethorn.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;

/**
 *
 *
 */
@Slf4j
public abstract class AbstractLinkFactory<EntityType extends Entity>
implements Entity.LinkFactory<EntityType>
    {
    public static final String DELIM = "/";
    public static final String REGEX = "(\\p{Alnum}+).*";

    protected AbstractLinkFactory(final String path)
        {
        this(
            DELIM,
            path
            );
        }

    protected AbstractLinkFactory(final String delim, final String path)
        {
        this.path  = path  ;
        this.delim = delim ;
        this.pattern = Pattern.compile(
            ".*" + this.path + this.delim + REGEX
            );
        }

    protected final String path  ;
    protected final String delim ;
    protected final Pattern pattern ;
    protected final Entity.IdentFactory idents = new AbstractIdentFactory();

    @Override
    public String link(final EntityType entity)
        {
        final StringBuilder builder = new StringBuilder(
            this.path
            );
        builder.append(
            DELIM
            );
        builder.append(
            entity.ident().toString()
            );
        return builder.toString();
        }

    @Override
    public Identifier ident(final String string)
        {
        log.debug("parse(String)");
        log.debug("  string  [{}]", string);
        log.debug("  pattern [{}]", this.pattern.pattern());

        final Matcher matcher = this.pattern.matcher(
            string
            );
        if (matcher.matches())
            {
            log.debug("PASS");
            final String temp = matcher.group(1);
            log.debug("  temp [{}]", temp);
            return this.idents.ident(
                temp
                );
            }
        else {
            log.debug("FAIL");
            throw new IdentifierFormatException(
                string
                );
            }
        }
    }
