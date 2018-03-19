// Copyright (c) The University of Edinburgh, 2008.
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


package uk.org.ogsadai.expression.arithmetic.visitors;

import java.util.ArrayList;
import java.util.List;

import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.TableColumn;

/**
 * Function extracting arithmetic expression visitor.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class FuncExtrArithmeticExprVisitor extends
    AttrArithmeticExprVisitorBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Extracted functions. */
    List<Function> mFunctions = new ArrayList<Function>();
    
    /**
     * {@inheritDoc}
     */
    public void visitFunction(ExecutableFunctionExpression function)
    {
        mFunctions.add(function.getExecutable());
    }

    /**
     * {@inheritDoc}
     */
    public void visitTableColumn(TableColumn tableColumn)
    {
        // NOOP
    }
    
    /**
     * Gets a list of extracted functions.
     * 
     * @return extracted functions list
     */
    public List<Function> getFunctions()
    {
        return mFunctions;
    }
}
