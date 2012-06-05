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
    extends IdentSelector<Widgeon>
        {
        public Widgeon create(String name, URI uri);
        public Widgeon create(String name, URL url);
        public Widgeon create(String name, DataSource src);

        /**
         * Our Schema factory.
         * 
         */
        public Widgeon.Schema.Factory schemas();

        }

    public interface Schemas
    extends NameSelector<Widgeon.Schema>
        {
        public Widgeon.Schema create(String name);
        }
    public Schemas schemas();

    public interface Schema
    extends GenericEntity
        {
        public Widgeon parent();
        public NameSelector<Catalog> catalogs();

        public interface Catalog
        extends GenericEntity
            {
            public Schema parent();
            public NameSelector<Table> tables();

            public interface Table
            extends GenericEntity
                {
                public Catalog parent();
                public NameSelector<Column> columns();

                public interface Column
                extends GenericEntity
                    {
                    public Table parent();
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

