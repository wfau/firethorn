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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import lombok.extern.slf4j.Slf4j;

/**
 * Simple test for OGSA-DAI queries.
 *
 *
 */
@Slf4j
public class SimpleQueryTestBase
    {

    /**
     * Simple test for OGSA-DAI queries.
     *
     */
    public void execute(final String endpoint, final String dataset, final String query)
    throws Exception
        {
        //
        // Create our server client.
        final SimpleClient client = new SimpleClient(
            endpoint
            );
        //
        // Get ResultSet.
        final ResultSet results = client.execute(
            dataset,
            query
            );
        if (results != null)
            {
            // Get the result set metadata.
            final ResultSetMetaData md = results.getMetaData();
            // Get column names and initial column widths.
            final int numColumns = md.getColumnCount();
            log.info("");
            // Get ResultSet rows.
            while (results.next())
                {
                final StringBuilder builder = new StringBuilder();
                for (int i = 0; i < numColumns; i++)
                    {
                    final Object field = results.getObject(i + 1);
                    if (field == null)
                        {
                        builder.append(
                            pad(
                                "null"
                                )
                            );
                        }
                    else {
                        builder.append(
                            pad(
                                field.toString()
                                )
                            );
                        }

                    builder.append(" ");
                    }
                log.info(
                    builder.toString()
                    );
                }
            results.close();
            }
        }

    /**
     * Pad a string out to a given width with space characters.
     *
     */
    public static String pad(final String base)
        {
        return pad(
            base,
            22
            );
        }

    /**
     * Pad a string out to a given width with space characters.
     *
     */
    public static String pad(final String base, final int width)
        {
        final int count = width - base.length();
        if (count == 0)
            {
            return base ;
            }
        else if (count < 0)
            {
            return base.substring(
                0,
                width
                );
            }
        else {
            final StringBuffer buffer = new StringBuffer(
                base
                );
            for (int i = 0; i < count; i++)
                {
                buffer.append(" ");
                }
            return buffer.toString();
            }
        }
    }

