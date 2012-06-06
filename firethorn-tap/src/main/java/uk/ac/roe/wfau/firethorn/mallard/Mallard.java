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
        {
        public Mallard create(String name);
        public Mallard create(String name, URI uri);
        }

    /**
     * A service component containing a resource.
     *
     */
    public interface Component
    extends Entity
        {
        public Mallard parent();
        public Widgeon resource();
        }

    /**
     * The collection of resources provided by this service.
     *
     */
    public Components components();
    public interface Components
        {
        public Component insert(String name, Widgeon resource);
        public Component insert(String name, URI uri);
        public Component select(URI uri);
        }
    }

