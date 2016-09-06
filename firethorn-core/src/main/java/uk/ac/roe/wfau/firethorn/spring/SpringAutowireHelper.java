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
package uk.ac.roe.wfau.firethorn.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.extern.slf4j.Slf4j;

/**
 * Helper class which is able to autowire a specified class.
 * http://guylabs.ch/2014/02/22/autowiring-pring-beans-in-hibernate-jpa-entity-listeners/
 *
 */
@Slf4j
public class SpringAutowireHelper
    implements ApplicationContextAware
    {
    /**
     * Private constructor.
     *
     */
    private SpringAutowireHelper()
        {
        }

    /**
     * Reference to our singleton instance.
     * 
     */
    private static final SpringAutowireHelper instance = new SpringAutowireHelper();

    /**
     * Access to our singleton instance.
     */
    public static SpringAutowireHelper instance()
        {
        return SpringAutowireHelper.instance;
        }    

    /**
     * Reference to our Spring {@link ApplicationContext}.
     * 
     */
    private static ApplicationContext context;
 
    @Override
    public void setApplicationContext(final ApplicationContext context)
        {
        SpringAutowireHelper.context = context;
        }
    
    /**
     * Tries to autowire the specified instance of the class if one of the specified beans which need to be autowired are null.
     *
     * @param target the instance of the class which holds @Autowire annotations
     * @param beans the beans which have the @Autowire annotation in the specified {#target}
     */
    public static void autowire(final Object target, final Object... beans)
        {
        log.debug("autowire(Object, Object... ");
        log.debug("   target [{}]", target);
        for (Object bean : beans)
            {
            log.debug("   bean [{}]", bean);
            if (bean == null)
                {
                SpringAutowireHelper.context.getAutowireCapableBeanFactory().autowireBean(
                    target
                    );
                }
            }
        }
     }
