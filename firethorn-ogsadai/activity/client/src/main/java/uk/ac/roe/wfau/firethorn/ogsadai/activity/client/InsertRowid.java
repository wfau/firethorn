/**
 * Copyright (c) 2013, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.StringData;

/**
 * Client proxy for InsertRowidActivity.
 *
 */
public class InsertRowid
extends BaseActivity implements Activity
    {

    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        InsertRowid.class
        );

    /*
     * Our default activity name.
     *
     */
    private final static ActivityName DEFAULT_ACTIVITY_NAME =
        new ActivityName(
            "uk.ac.roe.wfau.firethorn.InsertRowid"
            );

    /**
     * Activity input - column name.
     *
     */
    private final ActivityInput colname;

    /**
     * Activity input - tuples.
     *
     */
    private final ActivityInput tuples;

    /**
     * Activity output - tuples.
     *
     */
    private final ActivityOutput output;

    /**
     * Public constructor.
     *
     */
    public InsertRowid()
        {
        super(
            DEFAULT_ACTIVITY_NAME
            );
        colname = new SimpleActivityInput(
            "colname"
            );
        tuples = new SimpleActivityInput(
            "tuples"
            );
        output = new SimpleActivityOutput(
            "result"
            );
        }

    /**
     * Set the rowid column name.
     *
     */
    public void setColname(final String name)
        {
        colname.add(
            new StringData(
                name
                )
            );
        }

    /**
     * Add the tuples input.
     *
     */
    public void connectDataInput(final SingleActivityOutput source)
        {
        tuples.connect(
            source
            );
        }

    /**
     * Get the tuples output.
     *
     */
    public SingleActivityOutput getResultOutput()
        {
        return output.getSingleActivityOutputs()[0];
        }

    /**
     * Get the activity inputs.
     *
     */
    @Override
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]{
            colname,
            tuples
            };
        }

    /**
     * Get the activity outputs.
     *
     */
    @Override
    protected ActivityOutput[] getOutputs()
        {
        return new ActivityOutput[]{
            output
            };
        }

    /**
     * Validate the inputs and outputs.
     *
     */
    @Override
    protected void validateIOState()
    throws ActivityIOIllegalStateException
        {
        }
    }


