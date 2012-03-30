package uk.ac.roe.wfau.firethorn.tap;

import java.sql.ResultSet;


public class FireThornTapResult
    {

    protected ResultSet results ;

    public FireThornTapResult(ResultSet results)
        {
        this.results = results ;
        }

    public ResultSet results()
        {
        return this.results ;
        }

    @Override
    public String toString()
        {
        return "firethorn TAP result" ;
        }

    }
