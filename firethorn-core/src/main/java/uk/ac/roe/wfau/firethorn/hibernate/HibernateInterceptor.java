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
package uk.ac.roe.wfau.firethorn.hibernate;

import java.io.Serializable;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 * Hibernate {@link Interceptor} implementation.
 *
 */
@Slf4j
public class HibernateInterceptor
    extends EmptyInterceptor
    implements Interceptor
    {

    /**
     * Generated serial version UID.
     *
     */
    private static final long serialVersionUID = 419993145346031077L;

    /**
     * Public constructor.
     *
     *
     */
    public HibernateInterceptor()
        {
        }
    
    @Override
    public boolean onLoad(final Object object, final Serializable ident, final Object[] state, final String[] names, final Type[] types)
        {
        log.debug("onLoad(....)");
        log.debug("  factories [{}]", factories);
        if (object instanceof AbstractEntity)
            {
            AbstractEntity entity = (AbstractEntity) object ;
            log.debug("  entity [{}]", entity.getClass().getName());
            }
        else {
            log.debug("  object [{}]", object.getClass().getName());
            }
        return false ;
        }
    
    @Autowired
    private ComponentFactories factories ;

    }
