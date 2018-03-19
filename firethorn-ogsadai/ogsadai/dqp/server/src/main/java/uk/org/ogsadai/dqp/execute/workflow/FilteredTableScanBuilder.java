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


package uk.org.ogsadai.dqp.execute.workflow;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.SimpleRenameMap;
import uk.org.ogsadai.dqp.lqp.operators.FilteredTableScanOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionUtils;

/**
 * Builds activities for TABLE SCAN operator.
 *
 * @author The OGSA-DAI Project Team.
 */
public class FilteredTableScanBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Annotation key for filtered stats input annotation. */
    public static final String SQL_FILTERED_QUERY_INPUT_KEY = 
        "FilteredSQLQuery.input.filterStats";

    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(
            Operator op, 
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder)
    {
        FilteredTableScanOperator operator = (FilteredTableScanOperator)op;
        
        // Filtered SQL Query
        GenericActivity filteredQuery =
            new GenericActivity("uk.org.ogsadai.FilteredSQLQuery");
        filteredQuery.createInput("filterPredicates");
        filteredQuery.createInput("expression");
        filteredQuery.createInput("filterStats");
        filteredQuery.createOutput("result");
        
        // find query apply operator
        Operator current = operator;
        while (current.getID() != OperatorID.QUERY_APPLY)
        {
            current = current.getParent();
        }
        builder.addInput(
            current, operator, filteredQuery.getInput("filterStats"));
        
        filteredQuery.setResourceID(operator.getDataNode().getResourceID());
        String expression = operator.getPhysicalDatabaseQuery();
        filteredQuery.addInput("expression", expression);
        
        RenameMap renameMap = 
            getAttributeRenameMap(operator.getPredicate().getExpression(), operator);
        String predicate = 
            getRenamedPredicate(operator.getPredicate(), renameMap);
        filteredQuery.addInput("filterPredicates", new StringData(predicate));
        builder.add(filteredQuery);
        
        // SQL Query
        GenericActivity query = new GenericActivity("uk.org.ogsadai.SQLQuery");
        query.createInput("expression");
        query.createOutput("data");
        
        query.setResourceID(operator.getDataNode().getResourceID());
        query.connectInput("expression", filteredQuery.getOutput("result"));
        builder.add(query);
        
        // Rename the table names
        GenericActivity rename =
            new GenericActivity("uk.org.ogsadai.MetadataRename");
        rename.createInput("data");
        rename.createInput("resultColumnNames");
        rename.createOutput("result", GenericActivity.LIMITED_VALIDATION);

        List<Attribute> attributes = operator.getHeading().getAttributes();
        String[] newNames = new String[attributes.size()];
        for (int i=0; i<newNames.length; i++)
        {
            newNames[i] = attributes.get(i).toString(); 
        }
        rename.addInputList("resultColumnNames", newNames);
        rename.connectInput("data", query.getOutput("data"));
        builder.add(rename);
        return rename.getOutput("result");
    }

    /**
     * Creates an attribute rename map that maps an attribute in the header
     * to the name of the attribute in the physical database query.
     * 
     * @param expression 
     *    expression contain attributes for which to build the map
     *    
     * @param tableScanOperator
     *    table scan operator that will execute the query
     *    
     * @return
     *    attribute rename map that maps attributes in the given expression to
     *    physical database attributes
     */
    private RenameMap getAttributeRenameMap(
        Expression expression, 
        TableScanOperator tableScanOperator)
    {
        Set<Attribute> attributes = 
            ExpressionUtils.getUsedAttributes(expression);
        
        List<Attribute> origAttr = new LinkedList<Attribute>();
        List<Attribute> renmAttr = new LinkedList<Attribute>();
        
        for (Attribute attribute : attributes)
        {
            Attribute attr = tableScanOperator.getPhysicalAttribute(attribute);
            if (attr == null)
            {
                attr = attribute;
            }
            origAttr.add(attribute);
            renmAttr.add(attr);
        }
        return new SimpleRenameMap(origAttr, renmAttr);
    }
    
    /**
     * Builds the SQL for a given predicate using the given attribute map
     * to rename each of the attributes in the predicate.
     * 
     * @param predicate
     *   predicate for which to generate the SQL.
     * 
     * @param attributeRenameMap
     *   attribute rename map.
     * 
     * @return SQL for the predicate with each attribute renamed according to
     *         the rename map.
     */
    private String getRenamedPredicate(
        Predicate predicate,  
        RenameMap attributeRenameMap)
    {
        Expression expression = 
            ExpressionUtils.getClone(predicate.getExpression());
        ExpressionUtils.renameUsedAttributes(expression, attributeRenameMap);
        return ExpressionUtils.generateSQL(expression);
    }
}
