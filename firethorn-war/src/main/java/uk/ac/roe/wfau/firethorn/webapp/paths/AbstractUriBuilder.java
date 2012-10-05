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

import java.net.URI;

import lombok.extern.slf4j.Slf4j;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;

/**
 *
 */
@Deprecated
@Slf4j
public class AbstractUriBuilder
implements UriBuilder
    {

    /**
     * The base URI for this builder.
     *
     */
    protected URI  base ;

    /**
     * The base URI for this builder.
     * @return
     *      The base URI.
     *
     */
    public URI base()
        {
        return this.base ;
        }

    /**
     * The webapp Path for this builder.
     *
     */
    protected Path path ;

    /**
     * The webapp Path for this builder.
     * @return
     *      The webapp Path.
     *
     */
    public Path path()
        {
        return this.path ;
        }

    /**
     * Public constructor.
     * @param base
     *      The base URI for this builder.
     * @param path
     *      The webapp Path for this builder.
     *
     */
    public AbstractUriBuilder(final URI base, final Path path)
        {
        log.debug("AbstractUriBuilder() [{}][{}]", base, path);
        this.base = base ;
        this.path = path ;
        }

    @Override
    public URI uri(final Entity entity)
        {
        return this.uri(
            entity.ident()
            );
        }

    @Override
    public URI uri(final Identifier ident)
        {
        return base.resolve(
            path.resolve(
                ident
                )
            );
        }

    @Override
    public String str(final Entity entity)
        {
        return str(
            entity.ident()
            );
        }

    @Override
    public String str(final Identifier ident)
        {
        return path.resolve(
            ident
            );
        }
    }
