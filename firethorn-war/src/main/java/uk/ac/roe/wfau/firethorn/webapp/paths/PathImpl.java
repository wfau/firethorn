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
package uk.ac.roe.wfau.firethorn.webapp.paths;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;

/**
 * Path implementation.
 *
 */
@Slf4j
public class PathImpl
implements Path
    {

    public static final String DELIMITER = "/";
    public static final String IDENT_TOKEN = "{ident}" ;
    public static final String IDENT_REGEX = "\\{ident\\}" ;

    private final String base ;

    public PathImpl(final StringBuilder builder)
        {
        this(
            builder.toString()
            );
        }

    public PathImpl(final String string)
        {
        log.debug("PathImpl()");
        log.debug(" base [{}]", string);
        this.base = string;
        }

    @Override
    public Path append(final String string)
        {
        log.debug("append() [{}][{}]", this.base, string);
        if (string != null)
            {
            final String trimmed = string.trim();
            if (trimmed.length() > 0)
                {
                final StringBuilder builder = new StringBuilder(
                    this.base
                    );
                if (!trimmed.startsWith(DELIMITER))
                    {
                    if (!this.base.endsWith(DELIMITER))
                        {
                        builder.append(
                            DELIMITER
                            );
                        }
                    }
                builder.append(
                    trimmed
                    );
                return new PathImpl(
                    builder
                    );
                }
            }
        return this ;
        }

    @Override
    public Path append(final String... strings)
        {
        Path path = this ;
        for (final String string : strings)
            {
            path = path.append(
                string
                );
            }
        return path;
        }

    @Override
    public String resolve(final Entity entity)
        {
        return resolve(
            entity.ident()
            );
        }

    @Override
    public String resolve(final Identifier ident)
        {
        log.debug("resolve() [{}][{}]", this.base, ident.value());
        return this.base.replaceFirst(
            IDENT_REGEX,
            ident.value().toString()
            );
        }
    @Override
    public String toString()
        {
        return this.base ;
        }

    }