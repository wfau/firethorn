/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.adql.query;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Limits;
import uk.ac.roe.wfau.firethorn.exception.NotImplementedException;

/**
 * Base class for implementations of the AdqlQuery.Limits interface.
 * @todo combine BaseQueryLimits, AdqlQueryLimits and SimpleQueryLimits
 *
 */
abstract public class BaseQueryLimits
implements AdqlQuery.Limits
    {

    @Override
    public Limits lowest(final Limits that)
        {
        return lowest(
            this,
            that
            );
        }

    @Override
    public Limits combine(final Limits that)
        {
        return combine(
            this,
            that
            );
        }

    protected static final Long ZERO = new Long(0L);

    /**
     * Create a new Limits containing the lowest set of values from two Limits.
     * If the value from both Limits are not null and not zero, then the lowest (non-zero) value is chosen.
     * If the value from one of the Limits is null or zero and the other is not null or zero, then the non-null non-zero value is chosen.
     * If the values from both of the Limits are null or zero, then the result is zero.
     * @param left  One of the Limits to  compare.
     * @param right One of the Limits to compare.
     * @return A new Limits containing a combination of the lowest values from the two Limits.
     * 
     */
    public static AdqlQuery.Limits lowest(final AdqlQuery.Limits left, final AdqlQuery.Limits right)
        {
        return new BaseQueryLimits()
            {
            @Override
            public Long rows()
                {
                if ((left != null) && (left.rows() != null) && (left.rows() != 0L))
                    {
                    if ((right != null) && (right.rows() != null) && (right.rows() != 0L))
                        {
                        if (left.rows() <=  right.rows())
                            {
                            return left.rows();
                            }
                        else {
                            return right.rows();
                            }
                        }
                    else {
                        return left.rows();
                        }
                    }
                else {
                    if ((right != null) && (right.rows() != null))
                        {
                        return right.rows();
                        }
                    else {
                        return ZERO ;
                        }
                    }
                }

            @Override
            public Long cells()
                {
                if ((left != null) && (left.cells() != null) && (left.cells() != 0L))
                    {
                    if ((right != null) && (right.cells() != null) && (right.cells() != 0L))
                        {
                        if (left.cells() <=  right.cells())
                            {
                            return left.cells();
                            }
                        else {
                            return right.cells();
                            }
                        }
                    else {
                        return left.cells();
                        }
                    }
                else {
                    if ((right != null) && (right.cells() != null))
                        {
                        return right.cells();
                        }
                    else {
                        return ZERO ;
                        }
                    }
                }

            @Override
            public Long time()
                {
                if ((left != null) && (left.time() != null) && (left.time() != 0L))
                    {
                    if ((right != null) && (right.time() != null) && (right.time() != 0L))
                        {
                        if (left.time() <=  right.time())
                            {
                            return left.time();
                            }
                        else {
                            return right.time();
                            }
                        }
                    else {
                        return left.time();
                        }
                    }
                else {
                    if ((right != null) && (right.time() != null))
                        {
                        return right.time();
                        }
                    else {
                        return ZERO ;
                        }
                    }
                }
            };
        }

    /**
     * Create a new Limits containing a combination of values from two Limits.
     * If the value from the first Limits is not null and not zero, then the value from the first Limits is used.
     * If the value from the first Limits is null or zero, and the value from the second Limits is not zero, then the value from the second Limits is used.
     * If the values from both Limits are null or zero, then the value is zero.
     * @param left  The first Limits to  compare.
     * @param right The second Limits to compare.
     * @return A new Limits containing a combination of the values from the two Limits.
     * 
     */
    public static AdqlQuery.Limits combine(final AdqlQuery.Limits left, final AdqlQuery.Limits right)
        {
        return new BaseQueryLimits()
            {
            @Override
            public Long rows()
                {
                if ((left != null) && (left.rows() != null) && (left.rows() != 0L))
                    {
                    return left.rows();
                    }
                else if ((right != null) && (right.rows() != null))
                    {
                    return right.rows();
                    }
                else {
                    return ZERO ;
                    }
                }

            @Override
            public Long cells()
                {
                if ((left != null) && (left.cells() != null) && (left.cells() != 0L))
                    {
                    return left.cells();
                    }
                else if ((right != null) && (right.cells() != null))
                    {
                    return right.cells();
                    }
                else {
                    return ZERO ;
                    }
                }

            @Override
            public Long time()
                {
                if ((left != null) && (left.time() != null) && (left.time() != 0L))
                    {
                    return left.time();
                    }
                else if ((right != null) && (right.time() != null))
                    {
                    return right.time();
                    }
                else {
                    return ZERO ;
                    }
                }
            };
        }

    @Override
    public void rows(final Long value)
    	{
    	throw new NotImplementedException(); 
    	}

    @Override
    public void cells(final Long value)
    	{
    	throw new NotImplementedException(); 
    	}
    
    @Override
    public void time(final Long value)
    	{
    	throw new NotImplementedException(); 
    	}
    }
