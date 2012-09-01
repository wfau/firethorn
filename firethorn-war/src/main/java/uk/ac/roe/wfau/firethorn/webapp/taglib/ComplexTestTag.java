/**
 *
 *
 */
package uk.ac.roe.wfau.firethorn.webapp.taglib;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *
 */
@Slf4j
public class ComplexTestTag
extends TagSupport
    {

    private static final long serialVersionUID = 4467879046665729363L;

    public ComplexTestTag()
        {
        super();
        log.debug("ComplexTestTag()");
        }

    @Override
    public int doStartTag()
    throws JspException
        {
        log.debug("ComplexTestTag.doStartTag()");
/*
        ControllerData data = (ControllerData) this.pageContext.getAttribute(
            ControllerData.MODEL_PROPERTY,
            PageContext.REQUEST_SCOPE
            );
 */
        final JspWriter out = this.pageContext.getOut();
        try {
            out.print("[" + this.name + "][" + this.size + "]");
            }
        catch (final IOException ouch)
            {
            log.error("Exception writing tag body [{}]", ouch);
            }

        return Tag.SKIP_BODY;

        }

    @Override
    public int doEndTag()
    throws JspException
        {
        log.debug("ComplexTestTag.doEndTag()");
        return Tag.EVAL_PAGE;
        }

    private String name ;
    public void setName(final String name)
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
    public void setSize(final Long size)
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

