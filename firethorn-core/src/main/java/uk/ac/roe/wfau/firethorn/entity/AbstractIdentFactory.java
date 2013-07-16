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
public class AbstractIdentFactory
implements Entity.IdentFactory
    {

    private static final Pattern p1 = Pattern.compile("^[0-9]+$") ;
    private static final Pattern p2 = Pattern.compile("^\\(([0-9]+):([0-9]+)\\)$") ;
    private static final Pattern p3 = Pattern.compile("^\\((\\([0-9]+:[0-9]+\\)):(\\([0-9]+:[0-9]+\\))\\)$") ;

    @Override
    public Identifier ident(final String string)
        {
        log.debug("ident(String) [{}]", string);
        Matcher m1 = p1.matcher(string);
        if (m1.matches())
            {
            log.debug("m1 matches");
            return new LongIdentifier(
                string
                );
            }
        else {
            Matcher m2 = p2.matcher(string);
            if (m2.matches())
                {
                log.debug("m2 matches");
                return new ProxyIdentifier(
                    ident(
                        m2.group(1)
                        ),
                    ident(
                        m2.group(2)
                        )
                    );
                }
            else {
                Matcher m3 = p3.matcher(string);
                if (m3.matches())
                    {
                    log.debug("m3 matches");
                    return new ProxyIdentifier(
                        ident(
                            m3.group(1)
                            ),
                        ident(
                            m3.group(2)
                            )
                        );
                    }
                else {
                    throw new IdentifierFormatException(
                        string
                        ); 
                    }
                }
            }
        }
    }
