/**
 *
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

        ControllerData<Long> data = (ControllerData<Long>) this.getJspContext().getAttribute(
            ControllerData.MODEL_ATTRIB,
            PageContext.REQUEST_SCOPE
            );

        JspWriter out = this.getJspContext().getOut();
        out.print("vdthfvdrhdfsg [" + this.name + "][" + this.size + "] dfw45tdaesfA");
        out.print("vdthfvdrhdfsg [" + data.target() + "] dfw45tdaesfA");
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

