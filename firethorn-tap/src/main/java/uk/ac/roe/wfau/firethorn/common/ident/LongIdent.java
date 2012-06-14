/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.ident ;

/**
 *
 */
public class LongIdent
extends AbstractIdent<Long>
    {

    public LongIdent(String string)
        {
        this(
            parse(
                string
                )
            );
        }

    public LongIdent(int value)
        {
        super(
            new Long(
                value
                )
            ) ;
        }

    public LongIdent(long value)
        {
        super(
            new Long(
                value
                )
            ) ;
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

