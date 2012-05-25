package uws.job;

public class SimpleJobOwner
implements JobOwner
    {

    public SimpleJobOwner(String ident)
        {
        this.ident = ident ;
        }

	private String ident;

	public String getID()
	    {
	    return ident ;
	    }

	public String getPseudo()
	    {
	    return ident ;
	    }
    }
