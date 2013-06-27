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
package uk.ac.roe.wfau.firethorn.webapp.identity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 *
 *
 */
@Slf4j
public class HttpHeaderAuthenticator
implements HandlerInterceptor
    {
    /**
     * Autowired system services.
     *
     */
    @Autowired
    private ComponentFactories factories;

    static final String DEFAULT_METHOD = "http:header" ;

    static final String DEFAULT_IDENTITY  = "guest.identity"  ;
    static final String DEFAULT_COMMUNITY = "guest.community" ;

    static final String DEFAULT_IDENTITY_ATTRIB   = "firethorn.auth.identity"  ;
    static final String DEFAULT_COMMUNITY_ATTRIB  = "firethorn.auth.community" ;

    private String method = DEFAULT_METHOD ;
    public String getMethod()
        {
        return method;
        }
    public void setMethod(String value)
        {
        method = value;
        }
    
    private String defaultCommunity ;
    public String getDefaultCommunity()
        {
        return defaultCommunity;
        }
    public void setDefaultCommunity(String name)
        {
        defaultCommunity = name ;
        }

    private String defaultIdentity ;
    public String getDefaultIdentity()
        {
        return defaultIdentity ;
        }
    public void setDefaultIdentity(String name)
        {
        defaultIdentity = name ;
        }

    private String identityAttrib = DEFAULT_IDENTITY_ATTRIB ;
    public String getIdentityAttrib()
        {
        return identityAttrib;
        }
    public void setIdentityAttrib(String attrib)
        {
        identityAttrib = attrib ;
        }
    
    private String communityAttrib = DEFAULT_COMMUNITY_ATTRIB ;
    public String getCommunityAttrib()
        {
        return communityAttrib;
        }
    public void setCommunityAttrib(String attrib)
        {
        communityAttrib = attrib ;
        }
    
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
        {
        log.debug("preHandle()");
        
        String identity  = (String) request.getHeader(identityAttrib);
        String community = (String) request.getHeader(communityAttrib);

        if (identity == null)
            {
            identity = defaultIdentity ; 
            }

        if (community == null)
            {
            community = defaultCommunity; 
            }

        Operation operation = factories.operations().current();

        log.debug("Identity  [{}]", identity);
        log.debug("Community [{}]", community);
        log.debug("Operation [{}]", operation);

        if ((operation != null) && (community != null) && (identity != null))
            {
            log.debug("creating ....");
            operation.authentications().create(
                factories.identities().create(
                    factories.communities().create(
                        community,
                        community
                        ),
                    identity
                    ),
                method
                );
            }

        log.debug("Operation [{}]", operation);
        log.debug("Primary   [{}]", operation.authentications().primary());
        log.debug("Identity  [{}]", operation.authentications().primary().identity());
        log.debug("Community [{}]", operation.authentications().primary().identity().community());
        
        return true ;
        }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView model)
        {
        log.debug("postHandle()");
        }
    
    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ouch)
        {
        log.debug("afterCompletion()");
        }
    
    }
