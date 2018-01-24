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
package uk.ac.roe.wfau.firethorn.webapp.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
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

    static final String DEFAULT_IDENTITY  = "anon.identity"  ;
    static final String DEFAULT_COMMUNITY = "s" ;

    static final String DEFAULT_IDENTITY_ATTRIB   = "firethorn.auth.identity"  ;
    static final String DEFAULT_COMMUNITY_ATTRIB  = "firethorn.auth.community" ;

    private String method = DEFAULT_METHOD ;
    public String getMethod()
        {
        return method;
        }
    public void setMethod(final String value)
        {
        method = value;
        }

    private String defaultCommunity ;
    public String getDefaultCommunity()
        {
        return defaultCommunity;
        }
    public void setDefaultCommunity(final String name)
        {
        defaultCommunity = name ;
        }

    private String defaultIdentity ;
    public String getDefaultIdentity()
        {
        return defaultIdentity ;
        }
    public void setDefaultIdentity(final String name)
        {
        defaultIdentity = name ;
        }

    private String identityAttrib = DEFAULT_IDENTITY_ATTRIB ;
    public String getIdentityAttrib()
        {
        return identityAttrib;
        }
    public void setIdentityAttrib(final String attrib)
        {
        identityAttrib = attrib ;
        }

    private String communityAttrib = DEFAULT_COMMUNITY_ATTRIB ;
    public String getCommunityAttrib()
        {
        return communityAttrib;
        }
    public void setCommunityAttrib(final String attrib)
        {
        communityAttrib = attrib ;
        }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
    throws ProtectionException
        {
        log.debug("preHandle()");

        String iname = request.getHeader(identityAttrib);
        String cname = request.getHeader(communityAttrib);

        if (iname == null)
            {
            iname = defaultIdentity ;
            }

        if (cname == null)
            {
            cname = defaultCommunity;
            }

        final Operation operation = factories.operations().entities().current();

        log.debug("Identity  [{}]", iname);
        log.debug("Community [{}]", cname);
        log.debug("Operation [{}]", operation);

        if ((operation != null) && (cname != null) && (iname != null))
            {
            try {
                operation.authentications().create(
                    factories.communities().entities().create(
                        cname
                        ).members().create(
                            iname
                            ),
                    method
                    );
                }
            catch (DuplicateEntityException ouch)
                {
                log.debug("Duplicate exception ", ouch);
                }
            }

        log.debug("Operation [{}]", operation);
        log.debug("Primary   [{}]", operation.authentications().primary());

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
