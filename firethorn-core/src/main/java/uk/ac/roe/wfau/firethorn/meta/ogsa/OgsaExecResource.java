/**
 * 
 */
package uk.ac.roe.wfau.firethorn.meta.ogsa;

import uk.ac.roe.wfau.firethorn.entity.Entity;

/**
 *
 *
 */
public interface OgsaExecResource
extends OgsaBaseResource
	{
    /**
     * Services interface.
     * 
     */
    public static interface Services
        {
        /**
         * Our {@link OgsaExecResource.IdentFactory}.
         *
         */
        public OgsaExecResource.IdentFactory idents();

        /**
         * Our {@link OgsaExecResource.LinkFactory}.
         *
         */
        public OgsaExecResource.LinkFactory links();

        /**
         * Our {@link OgsaExecResource.EntityFactory}.
         *
         */
        public OgsaExecResource.EntityFactory entities();
        
        }
        
    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<OgsaExecResource>
        {
        }

    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<OgsaExecResource>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends OgsaBaseResource.EntityFactory<OgsaExecResource>
        {
        /**
         * Select all the {@link OgsaExecResource}(s).
         * @return An {@link Iterable} set of {@link OgsaExecResource}(s). 
         *
         */
        public Iterable<OgsaJdbcResource> select();

        /**
         * Select all the {@link OgsaExecResource}(s) for a {@link OgsaService}.
         * @param service The {@link OgsaService} service.
         * @return An {@link Iterable} list of {@link OgsaExecResource}(s).
         *
         */
        public Iterable<OgsaExecResource> select(final OgsaService service);

        /**
         * Create a new {@link OgsaExecResource} for an {@link OgsaService}.
         * @return A new {@link OgsaExecResource}.
         *
         */
        public OgsaExecResource create(final OgsaService service);

        /**
         * Select the primary {@link OgsaJdbcResource} for an {@link OgsaService}.
         * @param service The {@link OgsaService}.
         * @return The primary {@link OgsaJdbcResource}.
         *
         */
        public OgsaExecResource primary(final OgsaService service);
        
        }

	}
