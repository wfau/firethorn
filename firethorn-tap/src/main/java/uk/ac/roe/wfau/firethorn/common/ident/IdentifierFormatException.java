/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.ident ;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IdentFormatException
extends RuntimeException
    {

    public IdentFormatException()
        {
        this(
            null,
            null
            );
        }

    public IdentFormatException(String string)
        {
        this(
            string,
            null
            );
        }

    public IdentFormatException(Exception cause)
        {
        this(
            null,
            cause
            );
        }

    public IdentFormatException(String string, Exception cause)
        {
        super(
            cause
            );
        this.string = string ;
        }

    private String string;

    public String string()
        {
        return this.string;
        }
    }

