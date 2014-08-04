package uk.ac.roe.wfau.firethorn.ogsadai.activity.jdbc;

/**
 * Public interface for the Activity parameters.
 * @todo Move this to the common package.
 *
 */
public interface Param
    {
    /**
     * The database URL, as a String.
     *
     */
    public String url();

    /**
     * The database username, as a String.
     *
     */
    public String username();

    /**
     * The database password, as a String.
     *
     */
    public String password();
    }