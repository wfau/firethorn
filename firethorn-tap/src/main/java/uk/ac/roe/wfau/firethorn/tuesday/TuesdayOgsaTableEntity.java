package uk.ac.roe.wfau.firethorn.tuesday;

public abstract class TuesdayOgsaTableEntity<ColumnType extends TuesdayOgsaColumn>
extends TuesdayBaseTableEntity
implements TuesdayOgsaTable<ColumnType>
    {
    @Override
    public String alias()
        {
        return "alias";
        }
    @Override
    public String fullname()
        {
        return "fullname";
        }
    
    public TuesdayBaseTable base()
        {
        return this ;
        }
    @Override
    public TuesdayOgsaTable<ColumnType> ogsa()
        {
        return this ;
        }

    // Assert adql.base = this
    private TuesdayAdqlTableEntity adql;
    @Override
    public TuesdayAdqlTable adql()
        {
        return adql ;
        }
    }
