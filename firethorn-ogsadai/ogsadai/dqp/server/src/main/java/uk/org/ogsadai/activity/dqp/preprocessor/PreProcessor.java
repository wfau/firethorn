// Copyright (c) The University of Edinburgh, 2007 - 2008.
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


package uk.org.ogsadai.activity.dqp.preprocessor;

import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Preprocessor interface for DQP SQL Query activity.
 *
 * The preprocessor is to allow queries of any format to be input to 
 * the DQP SQL Query activity and transformed to a valid SQL Query for
 * DQP. 
 * 
 * An example of this could be a preprocessor which took the simple language
 * query "How many people have blue eyes in Perth?" and using information 
 * from its configuration, i.e. knowledge of resources, transform and parsing,
 * output the query "SELECT count(personID) FROM eyecolours JOIN locations ON 
 * eyecolours.personID=locations.personID WHERE eyecolours.colour='blue'".
 *  
 * The preprocessor can make use of information it can retrieve from its
 * own configuration, the resourceID and/or the securitycontext it is provided.
 * 
 * If an error should occur in the preprocessing method 
 * then a PreProcessException should be thrown.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface PreProcessor
{

    /**
     * Preprocess query from format X to DQP-Compliant SQL.
     * 
     * This method can use an information obtained from the ProProcessor config 
     * and the input arguments.
     * 
     * The output of the method must be a valid SQL string for DQP to process.
     * 
     * @param inputQuery the input query.
     * @param resourceID the target resource id.
     * @param securityContext the security context.
     * 
     * @return the string represented a DQP-Compliant SQL Query.
     * 
     * @throws PreProcessException when an error occurs in the PreProcessor.
     */
    public String preprocessQuery(String inputQuery, ResourceID resourceID,
            SecurityContext securityContext) throws PreProcessException;

}
