/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.ivoa.reg;

/**
 *
 *
 */
public interface IvoaRegistration
    {
    public interface VOElement
        {
        }
    
    public interface IdentifierURI
        {
        public String string();
        }

    /*
    public interface ResourceName
        {
        public IdentifierURI ident();
        public String name();
        }
     */

    /*
    public interface UTCDateTime
        {
        public String string();
        }
     */

    /*
    public interface UTCTimestamp
        {
        public String string();
        }
     */

    /**
     * 
     * http://www.ivoa.net/documents/REC/ReR/VOResource-20080222.html
     * http://www.ivoa.net/documents/VODataService/20100412/PR-VODataService-1.1-20100412.html
     * 
     * http://www.ivoa.net/documents/VOSI/20110531/REC-VOSI-1.0-20110531.html
     * 
     * http://dc.zah.uni-heidelberg.de/getRR/__system__/tap/run
     * http://dc.zah.uni-heidelberg.de/__system__/tap/run/capabilities
     *
     */
    public interface VOResource
    extends VOElement
        {
        public String created();
        public String updated();
        public String status();
        
        public String title();
        public String shortName();
        public IdentifierURI ident();
        
        public interface ValidationLevel
        extends VOElement
            {
            public Integer integer();
            }
        public Iterable<ValidationLevel> validationLevels();
        
        }

    public interface VOService
    extends VOResource, VOElement
        {

        public interface Capability
        extends VOElement
            {
            public String standardId();
            public String description();

            public Iterable<ValidationLevel> validationLevels();
            
            public interface Interface
            extends VOElement
                {
                public String role(); 
                public String version(); 

                public interface AccessURL
                extends VOElement
                    {
                    public String url();
                    public String use();
                    }
                public Iterable<AccessURL> accessURLs(); 

                public interface SecurityMethod
                extends VOElement
                    {
                    }
                public Iterable<SecurityMethod> securityMethods();
                }
            public Iterable<Interface> interfaces();

            }
        public Iterable<Capability> capabilities();
        
        }
    }
