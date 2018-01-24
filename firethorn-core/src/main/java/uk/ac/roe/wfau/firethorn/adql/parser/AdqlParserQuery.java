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
package uk.ac.roe.wfau.firethorn.adql.parser;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;

/**
 * Internal interface used by the AdqlParser during processing.
 *
 */
public interface AdqlParserQuery
    {

    /**
     * Get the cleaned text.
     *
     */
    public String cleaned();

    /**
     * Reset the query state.
     *
     */
    public void reset(final AdqlQueryBase.Mode mode);

    /**
     * Set the processed ADQL query.
     *
     */
    public void adql(final String adql);

    /**
     * Set the processed SQL query.
     *
     */
    public void osql(final String ogsa);

    /**
     * Add an AdqlColumn reference.
     * @throws ProtectionException 
     *
     */
    public void add(final AdqlColumn column)
    throws ProtectionException;

    /**
     * Add an AdqlTable reference.
     * @throws ProtectionException 
     *
     */
    public void add(final AdqlTable table)
    throws ProtectionException;

    /**
     * An Exception thrown when a duplicate select field is added.
     *
     *
     */
    public static class DuplicateFieldException
    extends AdqlParserException
        {
        private static final long serialVersionUID = -7933054733777146904L;

        private final AdqlQueryBase.SelectField field ;
        public AdqlQueryBase.SelectField field()
            {
            return this.field;
            }

        public DuplicateFieldException(final AdqlQueryBase.SelectField field)
            {
            super(
                "Duplicate SELECT field [" + field.name() + "]"
                );
            this.field = field;
            }
        }

    /**
     * Add the metadata for a SELECT field.
     * @throws ProtectionException 
     *
     */
    public void add(final AdqlQueryBase.SelectField field)
    throws DuplicateFieldException, ProtectionException;

    /**
     * Get the ADQL parser syntax.
     *
     */
    @Deprecated
    public AdqlQueryBase.Syntax syntax();

    /**
     * Set the ADQL syntax status.
     *
     */
    public void syntax(final AdqlQueryBase.Syntax.State status);

    /**
     * Set the ADQL syntax status.
     *
     */
    public void syntax(final AdqlQueryBase.Syntax.State status, final String message);

    /**
     * The BaseTranslator for the primary resource.
     *
     */
    public BaseTranslator translator();
    
    }
