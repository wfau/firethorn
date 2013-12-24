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
package uk.ac.roe.wfau.firethorn.entity;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateMethod;
import uk.ac.roe.wfau.firethorn.spring.SpringThings;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 */
@Slf4j
@Component
public class TransactionWrapper
    implements SpringThings.TransactionWrapper
    {
    @Override
    @SelectMethod
    public void select(Runnable runnable)
        {
        log.debug("select(Runnable)");
        runnable.run();
        }

    @Override
    @UpdateMethod
    public void update(Runnable runnable)
        {
        log.debug("update(Runnable)");
        runnable.run();
        }
    }
