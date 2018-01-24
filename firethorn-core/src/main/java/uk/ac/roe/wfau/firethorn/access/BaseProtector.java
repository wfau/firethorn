/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.access;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.AbstractComponent;
import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;

/**
 * An abstract base class for Protector implementations.
 *
 */
@Slf4j
public abstract class BaseProtector
implements Protector
    {
    @Component
    public static class EntityServices
    extends AbstractComponent
        {
        /**
         * Our singleton instance.
         * 
         */
        private static EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return BaseProtector.EntityServices.instance ;
            }

        /**
         * Protected constructor.
         * 
         */
        protected EntityServices()
            {
            }
        
        /**
         * Protected initialiser.
         * 
         */
        @PostConstruct
        protected void init()
            {
            log.debug("init()");
            if (BaseProtector.EntityServices.instance == null)
                {
                BaseProtector.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        protected Operation.EntityFactory operations;
        public Operation.EntityFactory operations()
            {
            return operations;
            }

        public Operation operation()
            {
            return operations.current();
            }
        
        @Autowired
        protected Identity.EntityFactory identities;
        public Identity.EntityFactory identities()
            {
            return identities;
            }

        public Identity admin()
        throws ProtectionException
            {
            return identities.admin();
            }
        }

    /**
     * Protected {@link Constructor#}
     * 
     */
    protected BaseProtector(final Protected target)
        {
        super();
        this.target = target;
        }

    protected Protected target;
    protected Protected target()
        {
        return this.target;
        }
    
    protected EntityServices services()
        {
        return EntityServices.instance() ;
        }

    /**
     * Check if an {@link Identity} is allowed to perform an {@link Action}.
     * @param identity The {@link Identity} to check.
     * @param action The {@link Action} to check.
     * @return true if the {@link Identity} is allowed to perform the {@link Action}.
     * 
     */
    @Override
    abstract public boolean check(final Identity identity, final Action action);

    @Override
    public boolean check(final Action action)
        {
        final Operation operation = services().operation(); 
        if (null != operation)
            {
            return check(
                operation.authentications().select(),
                action
                );
            }
        return false ;
        }

    @Override
    public boolean check(final Authentication authentication, final Action action)
        {
        return check(
            authentication.identity(),
            action
            );
        }

    @Override
    public boolean check(final Operation.Authentications authentications, final Action action)
        {
        return check(
            authentications.select(),
            action
            );
        }

    @Override
    public boolean check(final Iterable<Authentication> authentications, final Action action)
        {
        for (final Authentication authentication: authentications)
            {
            if (check(authentication, action))
                {
                return true;
                }
            }
        return false ;
        }

    @Override
    public Protector accept(final Action action)
    throws ProtectionException
        {
        final Operation operation = services().operation(); 
        if (null != operation)
            {
            return accept(
                operation.authentications(),
                action
                );
            }
        throw new ProtectionException(
            this,
            action
            );
        }
    
    @Override
    public Protector accept(final Identity identity, final Action action)
    throws ProtectionException
        {
        if (check(identity, action))
            {
            return this;
            }
        throw new ProtectionException(
            this,
            identity,
            action
            );
        }
    
    @Override
    public Protector accept(final Authentication authentication, final Action action)
    throws ProtectionException
        {
        return accept(
            authentication.identity(),
            action
            );
        }

    @Override
    public Protector accept(final Operation.Authentications authentications, final Action action)
    throws ProtectionException
        {
        return accept(
            authentications.select(),
            action
            );
        }

    @Override
    public Protector accept(final Iterable<Authentication> authentications, final Action action)
    throws ProtectionException
        {
        for (final Authentication authentication: authentications)
            {
            if (check(authentication, action))
                {
                return this;
                }
            }
        throw new ProtectionException(
            this,
            action
            );
        }
    
    /**
     * Check if an {@link Identity} matches the admin account.
     * 
     */
    public boolean isAdmin(final Identity identity)
        {
        try {
            return services().admin().ident().equals(identity.ident());
            }
        catch (final ProtectionException ouch)
            {
            log.error("ProtectionException checking admin account");
            return false ;
            }
        }
    }
