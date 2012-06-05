/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

import java.util.Date;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

/**
 * Generic interface for a persisten Entity.
 *
 */
public interface GenericEntity
    {

    public interface Factory
        {

        /**
         * Create an Identifier from a String.
         *
         */
        public Identifier ident(String string);

        }

    public Identifier ident();

    public String name();
    public void name(String name);

    public Date created();

    public Date modified();

    }

