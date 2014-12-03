// Copyright (c) The University of Edinburgh, 2011.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END

package uk.org.ogsadai.dqp.spatial;

/**
 * Basic implementation of a Circle.  This will later be changed to work with
 * the astronomical interpretation.
 * 
 * @author The OGSA-DAI Project Team
 */
public class Circle implements Shape
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011";

    private double x;
    private double y;
    private double r;
    private double rr;

    /**
     * Constructor. The circle is defined by centre (x,y) and radius r.
     * 
     * @param x      x
     * @param y      y
     * @param r      radius
     */
    public Circle(double x, double y, double r)
    {
        this.x = x;
        this.y = y;
        this.r = r;
        this.rr = r*r;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean contains(Point p)
    {
        double dx = p.getX() - x;
        double dy = p.getY() - y;
        return (dx*dx) + (dy*dy) <= rr;
    }

    /**
     * Gets x.
     * 
     * @return x
     */
    public double getX()
    {
        return x;
    }

    /**
     * Gets y.
     * 
     * @return y
     */
    public double getY()
    {
        return y;
    }

    /**
     * Gets radius.
     * 
     * @return radius
     */
    public double getRadius()
    {
        return r;
    }
}
