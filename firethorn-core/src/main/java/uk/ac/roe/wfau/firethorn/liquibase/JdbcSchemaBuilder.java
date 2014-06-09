/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.liquibase;

import liquibase.changelog.ChangeSet;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

/**
 *
 *
 */
@Slf4j
@Component
public class JdbcSchemaBuilder
extends JdbcBuilderBase
implements JdbcSchema.OldBuilder
    {

    @Override
    public JdbcSchema create(final JdbcSchema schema)
        {
        log.debug("create(JdbcSchema)");
        log.debug("  Schema [{}][{}]", schema.ident(), schema.name());

        log.debug("--- 000 ");
        final ChangeSet changeset = changeset();

        log.debug("--- 001 ");
        changeset.addChange(
            new CreateJdbcSchemaChange(
                schema
                )
            );

        log.debug("--- 002 ");
        execute(
            schema.resource(),
            changeset
            );

        log.debug("--- 003 ");
        return schema;

        }

    @Override
    public void delete(final JdbcSchema schema)
        {
        // TODO Auto-generated method stub
        }
    }
