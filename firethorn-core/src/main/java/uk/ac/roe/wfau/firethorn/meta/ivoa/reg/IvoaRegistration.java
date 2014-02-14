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
    public interface Base
        {
        public boolean understood();
        }

    public interface IdentifierURI
        {
        public String string();
        }

    public interface ResourceName
        {
        public IdentifierURI ident();
        public String name();
        }
    
    public interface UTCDateTime
        {
        public String string();
        }

    public interface UTCTimestamp
        {
        public String string();
        }
    
    public interface VOResource
    extends Base
        {
        public String created();
        public String updated();
        public String status();
        
        public String title();
        public String shortName();
        public IdentifierURI ident();
        
        public interface ValidationLevel
        extends Base
            {
            public Integer integer();
            }
        public Iterable<ValidationLevel> validationLevels();
        
        }

    public interface VOService
    extends VOResource, Base
        {

        public interface Capability
        extends Base
            {
            public String standardId();
            public String description();

            public Iterable<ValidationLevel> validationLevels();
            
            public interface Interface
            extends Base
                {
                public String role(); 
                public String version(); 

                public interface AccessURL
                extends Base
                    {
                    public String url();
                    public String use();
                    }
                public Iterable<AccessURL> accessURLs(); 

                public interface SecurityMethod
                extends Base
                    {
                    }
                public Iterable<SecurityMethod> securityMethods();
                }
            public Iterable<Interface> interfaces();

            }
        public Iterable<Capability> capabilities();
        
        }
    }
