// Copyright (c) The University of Edinburgh, 2011-2012.
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
 * Basic implementation of a Box.  This will later be changed to work with
 * the astronomical interpretation.
 * 
 * @author The OGSA-DAI Project Team
 */
public class Box implements Shape
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011-2012";

    private double x;
    private double y;
    private double width;
    private double height;

    /**
     * Constructor. The box is defined by (x,y) as the centre point and its
     * width and height, that is the box has opposite corners 
     * (x-width/2, y-width/2) and (x+width/2, y+width/2) inclusive.
     * 
     * @param x
     *            centre of box x
     * @param y
     *            centre of box y
     * @param width
     *            width of box
     * @param height
     *            height of box
     */
    public Box(double x, double y, double width, double height)
    {
        // calculate the opposite corners of the box 
        this.x = x - width/2;
        this.y = y - height/2;
        this.width = width;
        this.height = height;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean contains(Point p)
    {
        return p.getX() >= x && p.getX() <= x+width &&
               p.getY() >= y && p.getY() <= y+height;
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
     * Gets width.
     * 
     * @return width
     */
    public double getWidth()
    {
        return width;
    }

    /**
     * Gets height.
     * 
     * @return height
     */
    public double getHeight()
    {
        return height;
    }
}
