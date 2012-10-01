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
import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;

@Slf4j
/**
 * Path implementation.
 *
 */
public class PathImpl
implements Path
    {

    public static final String DELIMITER = "/";
    public static final String IDENT_TOKEN = "{ident}" ;
    public static final String IDENT_REGEX = "\\{ident\\}" ;

    private String base ;

    public PathImpl(StringBuilder builder)
        {
        this(
            builder.toString()
            );
        }

    public PathImpl(String string)
        {
        log.debug("PathImpl()");
        log.debug(" base [{}]", string);
        this.base = string;
        }
    
    @Override
    public Path append(String string)
        {
        log.debug("append() [{}][{}]", base, string);
        if (string != null)
            {
            String trimmed = string.trim();
            if (trimmed.length() > 0)
                {
                StringBuilder builder = new StringBuilder(
                    base
                    );
                if (!trimmed.startsWith(DELIMITER))
                    {
                    if (!base.endsWith(DELIMITER))
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
    public Path append(String... strings)
        {
        Path path = this ;
        for (String string : strings)
            {
            path = path.append(
                string
                );
            }
        return path;
        }

    @Override
    public String resolve(Entity entity)
        {
        return resolve(
            entity.ident()
            );
        }

    @Override
    public String resolve(Identifier ident)
        {
        log.debug("resolve() [{}][{}]", base, ident.value());
        return base.replaceFirst(
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