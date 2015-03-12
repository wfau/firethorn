/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data;

import uk.org.ogsadai.resource.request.RequestExecutionStatus;

/**
 *
 * @deprecated Use {@link SimpleWorkflowResult}
 *
 */
public class SimplePipelineResult
    implements PipelineResult
    {
    public SimplePipelineResult(final RequestExecutionStatus status)
        {
        this.cause   = null ;
        this.message = null ;
        if (RequestExecutionStatus.COMPLETED.equals(status))
            {
            this.result = PipelineResult.Result.COMPLETED;
            }
        else if (RequestExecutionStatus.COMPLETED_WITH_ERROR.equals(status))
            {
            this.result = PipelineResult.Result.FAILED;
            }
        else if (RequestExecutionStatus.TERMINATED.equals(status))
            {
            this.result = PipelineResult.Result.CANCELLED;
            }
        else if (RequestExecutionStatus.ERROR.equals(status))
            {
            this.result = PipelineResult.Result.FAILED;
            }
        else {
            this.result = PipelineResult.Result.FAILED;
            this.message = "Unknown RequestExecutionStatus [" + status + "]";
            }
        }
    
    public SimplePipelineResult(final Throwable cause)
        {
        this.cause   = cause   ;
        this.result  = PipelineResult.Result.FAILED;
        this.message = cause.getMessage() ;
        }

    public SimplePipelineResult(final String message)
        {
        this.cause   = null ;
        this.result  = PipelineResult.Result.FAILED;
        this.message = message ;
        }

    private String message;
    @Override
    public String message()
        {
        return this.message;
        }

    private final Throwable cause;
    @Override
    public Throwable cause()
        {
        return this.cause;
        }

    private final Result result;
    @Override
    public Result result()
        {
        return this.result;
        }

    }
