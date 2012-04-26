/**
 * Copyright (c) 2012, ROE (http://www.roe.ac.uk/)
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.ResourceActivity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.BaseResourceActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;

import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.StringData;

/**
 * <p>
 * Client proxy for
 * <code>uk.ac.roe.wfau.firethorn.ogsadai.activity.CreateTableActivity</code>.
 * </p>
 *
 */
public class CreateTable
extends BaseResourceActivity 
implements ResourceActivity
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        CreateTable.class
        );

    /*
     * Our default activity name.
     *
     */
    private final static ActivityName DEFAULT_ACTIVITY_NAME = 
        new ActivityName(
            "uk.ac.roe.wfau.firethorn.ogsadai.activity.CreateTable"
            );

    /**
     * Activity input - table name.
     *
     */
    private ActivityInput table;

    /**
     * Activity input - tuples.
     *
     */
    private ActivityInput tuples;

    /**
     * Activity output - tuples.
     *
     */
    private ActivityOutput output;

    /**
     * Public constructor.
     *
     */
    public CreateTable()
        {
        super(
            DEFAULT_ACTIVITY_NAME
            );
        table = new SimpleActivityInput(
            "table"
            );
        tuples = new SimpleActivityInput(
            "tuples"
            );
        output = new SimpleActivityOutput(
            "result"
            );
        }


    /**
     * Add the table name.
     * @param value The table name.
     * 
     */
    public void setTableName(String value)
        {
        table.add(
            new StringData(
                value
                )
            );
        }

    /**
     * Add the tuples input.
     * @param output The tuples source.
     *
     */
    public void connectTuples(SingleActivityOutput source)
        {
        tuples.connect(
            source
            );
        }

    /**
     * Get the tuples output.
     * 
     */
    public SingleActivityOutput getDataOutput()
        {
        return output.getSingleActivityOutputs()[0];
        }

    /**
     * Gets the activity inputs.
     *
     */
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]{
            table,
            tuples
            };
        }

    /**
     * Gets the activity outputs.
     *
     */
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
    protected void validateIOState()
    throws ActivityIOIllegalStateException
        {
        }

    }


