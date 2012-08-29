/**
 *
 *
 */
package uk.ac.roe.wfau.firethorn.webapp.taglib;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import uk.ac.roe.wfau.firethorn.webapp.control.ControllerData;

/**
 *
 *
 */
@Slf4j
public class ComplexTestTag
extends TagSupport
    {

    public ComplexTestTag()
        {
        super();
        log.debug("ComplexTestTag()");
        }

    public int doStartTag()
    throws JspException
        {
        log.debug("ComplexTestTag.doStartTag()");

        ControllerData<Long> data = (ControllerData<Long>) this.pageContext.getAttribute(
            ControllerData.MODEL_ATTRIB,
            PageContext.REQUEST_SCOPE
            );

        JspWriter out = this.pageContext.getOut();
        try {
            out.print("vdthfvdrhdfsg [" + this.name + "][" + this.size + "] dfw45tdaesfA");
            out.print("vdthfvdrhdfsg [" + data.target() + "] dfw45tdaesfA");
            }
        catch (IOException ouch)
            {
            log.error("Exception writing tag body [{}]", ouch);
            }

        return Tag.SKIP_BODY;

        }

    public int doEndTag()
    throws JspException
        {
        log.debug("ComplexTestTag.doEndTag()");
        return Tag.EVAL_PAGE;
        }

    private String name ;
    public void setName(String name)
        {
        log.debug("ComplexTestTag.setName() [{}]:[{}]", this.name, name);
        this.name = name ;
        }
    public String getName()
        {
        log.debug("ComplexTestTag.getName() [{}]", this.name);
        return this.name ;
        }

    private Long size ;
    public void setSize(Long size)
        {
        log.debug("ComplexTestTag.setSize() [{}]:[{}]", this.size, size);
        this.size = size ;
        }
    public Long getSize()
        {
        log.debug("ComplexTestTag.getSize() [{}]", this.size);
        return this.size ;
        }

    }

