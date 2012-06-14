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
public class IdentifierFormatException
extends RuntimeException
    {

    public IdentifierFormatException()
        {
        this(
            null,
            null
            );
        }

    public IdentifierFormatException(String string)
        {
        this(
            string,
            null
            );
        }

    public IdentifierFormatException(Exception cause)
        {
        this(
            null,
            cause
            );
        }

    public IdentifierFormatException(String string, Exception cause)
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

