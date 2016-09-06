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
    public static final String REGEX = "([\\p{Alnum}():]+).*?";

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
            ".*?" + this.path + this.delim + REGEX
            );
        }

    protected final String path  ;
    protected final String delim ;
    protected final Pattern pattern ;
    //TODO
    protected final Entity.IdentFactory<EntityType> idents = new AbstractIdentFactory<EntityType>();

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

    protected Matcher matcher(String link)
        {
        log.debug("matcher(String)");
        log.debug("  link    [{}]", link);
        log.debug("  pattern [{}]", this.pattern.pattern());
        return this.pattern.matcher(
            link
            );
        }

    @Override
    public boolean matches(String link)
        {
        log.debug("matches(String)");
        log.debug("  link    [{}]", link);
        log.debug("  pattern [{}]", this.pattern.pattern());
        final Matcher matcher = this.matcher(
            link
            );
        return matcher.matches();
        }

    @Override
    public Identifier ident(final String link)
        {
        log.debug("ident(String)");
        log.debug("  link    [{}]", link);
        log.debug("  pattern [{}]", this.pattern.pattern());

        final Matcher matcher = matcher(
            link
            );
        if (matcher.matches())
            {
            log.debug("  group[0][{}]", matcher.group(0));
            log.debug("  group[1][{}]", matcher.group(1));
            return this.idents.ident(
                matcher.group(1)
                );
            }
        else {
            throw new IdentifierFormatException(
                link
                );
            }
        }
    }
