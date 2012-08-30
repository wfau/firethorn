/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.webapp.taglib;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import uk.ac.roe.wfau.firethorn.webapp.control.ControllerData;

/**
 *
 * http://docs.oracle.com/javaee/5/tutorial/doc/bnann.html#bnanw
 */
@Slf4j
public class SimpleTestTag
extends SimpleTagSupport
    {

    public SimpleTestTag()
        {
        super();
        log.debug("SimpleTestTag()");
        }

    public void doTag()
    throws JspException, IOException
        {
        log.debug("SimpleTestTag.doTag()");

        ControllerData data = (ControllerData) this.getJspContext().getAttribute(
            ControllerData.MODEL_PROPERTY,
            PageContext.REQUEST_SCOPE
            );

        JspWriter out = this.getJspContext().getOut();
        out.print("[" + this.name + "][" + this.size + "]");
        }

    private String name ;
    public void setName(String name)
        {
        log.debug("SimpleTestTag.setName() [{}]:[{}]", this.name, name);
        this.name = name ;
        }
    public String getName()
        {
        log.debug("SimpleTestTag.getName() [{}]", this.name);
        return this.name ;
        }

    private Long size ;
    public void setSize(Long size)
        {
        log.debug("SimpleTestTag.setSize() [{}]:[{}]", this.size, size);
        this.size = size ;
        }
    public Long getSize()
        {
        log.debug("SimpleTestTag.getSize() [{}]", this.size);
        return this.size ;
        }

    }

