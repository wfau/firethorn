package uk.ac.roe.wfau.firethorn.widgeon ;

import java.net.URI;
import java.net.URL;

import javax.sql.DataSource;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

import uk.ac.roe.wfau.firethorn.common.entity.GenericEntity;
import uk.ac.roe.wfau.firethorn.common.entity.NameSelector;
import uk.ac.roe.wfau.firethorn.common.entity.IdentSelector;

/**
 * Metadata structure describing a database resource.
 *
 */
public interface Widgeon
extends GenericEntity
    {

    /**
     * Widgeon factory interface.
     *
     */
    public static interface Factory
        {
        public Widgeon create(String name, URI uri);
        public Widgeon create(String name, URL url);
        public Widgeon create(String name, DataSource src);

        public Iterable<Widgeon> select();
        public Widgeon select(Identifier ident);

        /**
         * Our Schema factory.
         * 
         */
        public Widgeon.Schema.Factory schemas();

        }

    public Schemas schemas();
    public interface Schemas
        {
        public Widgeon.Schema create(String name);
        public Widgeon.Schema select(String name);
        public Iterable<Widgeon.Schema> select();
        }

    public interface Schema
    extends GenericEntity
        {
        public Widgeon parent();

        public interface Factory
            {
            public Schema select(Identifier ident);
            public Schema create(Widgeon parent, String name);
            public Schema select(Widgeon parent, String name);
            public Iterable<Schema> select();
            public Iterable<Schema> select(Widgeon parent);
            }

        public Catalogs catalogs();
        public interface Catalogs
            {
            public Schema.Catalog create(String name);
            public Schema.Catalog select(String name);
            public Iterable<Schema.Catalog> select();
            }

        public interface Catalog
        extends GenericEntity
            {
            public Schema parent();

            public interface Factory
                {
                }

            public Tables tables();
            public interface Tables
                {
                public Catalog.Table create(String name);
                public Catalog.Table select(String name);
                public Iterable<Catalog.Table> select();
                }

            public interface Table
            extends GenericEntity
                {
                public Catalog parent();

                public interface Factory
                    {
                    }

                public Columns tables();
                public interface Columns
                    {
                    public Table.Column create(String name);
                    public Table.Column select(String name);
                    public Iterable<Table.Column> select();
                    }

                public interface Column
                extends GenericEntity
                    {
                    public Table parent();

                    public interface Factory
                        {
                        }
                    }
                }
            }
        }

    /**
     * A core resource built from what IS.
     *
     */
    public interface Core
    extends Widgeon
        {
        /**
         * The views avaiable for this resource.
         *
         */
        public NameSelector<Teal> views();
        }

    /**
     * A customised view of a resource.
     *
     */
    public interface Teal
    extends Widgeon
        {
        /**
         * The parent resource.
         *
         */
        public Widgeon parent();
        }
    }

