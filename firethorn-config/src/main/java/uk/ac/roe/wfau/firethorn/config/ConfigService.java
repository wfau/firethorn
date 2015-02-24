/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.config;

/**
 * Configuration service interface.
 *
 */
public interface ConfigService
    {

    /**
     * Access to the configuration data as key:value {@link String} properties.
     *
     */
    public static interface Properties
        {
        /**
         * Get a configuration property.
         * @return The property value, or null if no match.
         * 
         */
        public String getProperty(final String key)
        throws ConfigException;

        /**
         * Get a configuration property, fallback value for not found.
         * @return The property value, or the fallback value if no match.
         * 
         */
        public String getProperty(final String key, final String fallback)
        throws ConfigException;
        
        /**
         * Set a configuration property.
         * 
         */
        public void setProperty(final String key, final String value)
        throws ConfigException;
        
        }

    /**
     * Access to the configuration data as key:value {@link String} properties.
     *
     */
    public Properties properties();

    /**
     * A {@link ConfigurationService} factory. 
     *
     */
    public static interface Factory
        {
        /**
         * Load the default configuration.  
         *
         */
        public ConfigService load()
        throws ConfigException;

        /**
         * Load a specific configuration. 
         *
         */
        public ConfigService load(final String source)
        throws ConfigException;
        }
 
    /**
     * Configuration Exception. 
     *
     */
    public static class ConfigException
    extends Exception
        {
        private static final long serialVersionUID = -1955739261325914288L;

        public ConfigException(final String message)
            {
            super(message);
            }
        public ConfigException(final Throwable cause)
            {
            super(cause);
            }
        public ConfigException(final String message, final Throwable cause)
            {
            super(message, cause);
            }
        }
    }
