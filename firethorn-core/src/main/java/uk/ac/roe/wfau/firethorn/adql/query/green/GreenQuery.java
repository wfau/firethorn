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
package uk.ac.roe.wfau.firethorn.adql.query.green;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.adql.query.QueryProcessingException;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Limits;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Mode;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.SelectField;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Limits.Factory;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.Level;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 *
 *
 */
@Deprecated
public interface GreenQuery
extends AdqlQueryBase, NamedEntity, Job
    {

    /**
     * {@link NamedEntity.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<GreenQuery>
        {
        }

    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<GreenQuery>
        {
        }

    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<GreenQuery>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends Job.EntityFactory<GreenQuery>
        {
        /**
         * Create a new query.
         *
         */
        public GreenQuery create(final AdqlSchema schema, final QueryParam params, final String input)
        throws QueryProcessingException;

        /**
         * Create a new query.
         * @throws QueryProcessingException 
         *
         */
        public GreenQuery create(final AdqlSchema schema, final QueryParam params, final String input, final String name)
        throws QueryProcessingException;

        /**
         * Select all the queries from a resource.
         *
         */
        public Iterable<GreenQuery> select(final AdqlSchema schema);

        /**
         * Text search for queries (name starts with).
         *
         */
        public Iterable<GreenQuery> search(final AdqlSchema schema, final String text);
        
        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<GreenQuery>
        {
        /**
         * Our {@link GreenQuery.EntityFactory} instance.
         *
         */
        public GreenQuery.EntityFactory entities();

        /**
         * Our {@link Job.Executor} instance.
         *
         */
        public Job.Executor executor();

        /**
         * Our OGSA-DAI param factory instance.
         *
         */
        public ParamFactory params();

        /**
         * Our query limits factory.
         *
         */
        public Limits.Factory limits();
        
        }
    
    /**
     * QueryTable builder interface.
     *
     */
    @Deprecated
    public static interface Builder
        {
        /**
         * Build a physical table.
         *
         */
        public JdbcTable create(final JdbcSchema store, final GreenQuery query);

        }

    /**
     * Get the original input text.
     *
     */
    public String input();

    /**
     * Set the input text.
     *
     */
    public void input(final String input);
        
    /**
     * Get the processed input text.
     *
     */
    public String cleaned();

    /**
     * The ADQL schema this query applies to.
     *
     */
    public AdqlSchema schema();

    /**
     * The processed ADQL query.
     *
     */
    public String adql();

    /**
     * The processed SQL query.
     *
     */
    public String osql();

    /**
     * A list of the AdqlColumns used by the query.
     *
     */
    public Iterable<AdqlColumn> columns();

    /**
     * A list of the AdqlTables used by the query.
     * The list is only generated in response to a POST request that updates the ADQL query.
     * The list is generated when an input query is parsed and is not saved in the database.
     * On subsequent GET requests the list will be empty.
     *
     */
    public Iterable<AdqlTable> tables();

    /**
     * A list of the resources used by the query.
     *
     */
    public Iterable<BaseResource<?>> resources();

    /**
     * Select the primary resource used by this query.
     * @todo rename to resource()
     *
     */
    public BaseResource<?> primary();

    /**
     * A list of the SELECT fields used in this query.
     * ** The list is only generated when an input query is parsed.
     * ** The list is NOT saved in the database.
     *
     */
    public Iterable<SelectField> fields();

    /**
     * Our result tables.
     *
     */
    public interface Results
        {
        /**
         * The physical JDBC database table.
         *
         */
        public JdbcTable jdbc();

        /**
         * The physical base table.
         *
         */
        public BaseTable<?,?> base();

        /**
         * The abstract ADQL table.
         *
         */
        public AdqlTable adql();
        }

    /**
     * Our result tables.
     *
     */
    public Results results();

    /**
     * Public interface for OGSA-DAI query params.
     * @todo This should become the basis for an OgsaDaiService entity ?
     *
     */
    public interface QueryParam
        {
        /**
         * The service endpoint URL.
         *
         */
        public String endpoint();

        /**
         * The DQP processor name.
         *
         */
        public String dqp();

        /**
         * The DQP processor mode.
         *
         */
        public Mode mode();

        /**
         * The ADQL parser level.
         *
         */
        public GreenQuery.Syntax.Level level();
        
        }

    /**
     * OGSA-DAI param factory interface.
     *
     */
    public static interface ParamFactory
        {
        /**
         * Create a new set of params using the environment settings.
         *
         */
        public QueryParam create();

        /**
         * Create a new set of params, with a specific mode.
         * @param level The @{link AdqlQuery.Syntax.Level}.
         * @param mode  The @{link AdqlQuery.Mode}.
         *
         */
        public QueryParam create(final GreenQuery.Syntax.Level level, final GreenQuery.Mode mode);

        }

    /**
     * The OGSA-DAI query params.
     *
     */
    public QueryParam params();

    }
