package uk.ac.roe.wfau.firethorn.mallard ;

import java.net.URL;
import java.net.URI;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 * A TAP service instance.
 *
 */
public interface Mallard
extends Entity
    {

    /**
     * A service factory (TAP factory).
     *
     */
    public static interface Factory
    extends Entity.Factory<Mallard>
        {
        public Mallard create(String name);

        public void join(Mallard mallard, Widgeon widgeon);

        /**
         * Access to our Job factory.
         * 
         */
        public Job.Factory jobs();
        }

    /**
     * The collection of resources used by this service.
     *
     */
    public Widgeons widgeons();
    public interface Widgeons
        {
        public void insert(Widgeon widgeon);
        public Iterable<Widgeon> select();
        }

    /**
     * An ADQL query.
     *
     */
    public interface Job
    extends Entity
        {

        public interface Factory
        extends Entity.Factory<Job>
            {

            public Job create(Mallard parent, String adql);
            public Iterable<Job> select(Mallard parent);

            }        

        public Status status();
        public enum Status
            {
            PENDING(),
            RUNNING(),
            COMPLETED(),
            FAILED();
            }; 

        public String adql();

        // Results

        }

    /**
     * Job list for this TAP service.
     *
     */
    public Jobs jobs();
    public interface Jobs
        {
        public Job create(String adql);
        public Iterable<Job> select();
        }

    }

