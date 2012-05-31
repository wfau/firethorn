/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.ident ;

import java.io.Serializable;

/**
 *
 */
public class LongIdent
extends SimpleIdent<Long>
    {

    public LongIdent(String string)
        {
        this(
            parse(
                string
                )
            );
        }

    public LongIdent(Long value)
        {
        super(value) ;
        }

    /**
     * Parse a string.
     *
     */
    public static Long parse(String string)
        {
        try {
            return Long.valueOf(
                string
                );
            }
        catch (NumberFormatException ouch)
            {
            throw new IdentFormatException(
                string,
                ouch
                );
            }
        }

    /**
     * Create an Identifier from a string.
     *
     */
    public static Identifier create(String string)
        {
        try {
            return new LongIdent(
                LongIdent.parse(
                    string
                    )
                );
            }
        catch (NumberFormatException ouch)
            {
            throw new IdentFormatException(
                string,
                ouch
                );
            }
        }

    }

