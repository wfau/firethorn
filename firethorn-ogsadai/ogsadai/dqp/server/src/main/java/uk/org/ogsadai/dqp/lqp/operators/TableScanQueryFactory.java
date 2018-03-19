// Copyright (c) The University of Edinburgh, 2009.
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

package uk.org.ogsadai.dqp.lqp.operators;

/**
 * Interface for factory that creates <code>TableScanQuery</code> objects.
 *
 * @author The OGSA-DAI Project Team
 */
public interface TableScanQueryFactory
{
    /**
     * Creates a new instance of a <code>TableScanQuery</code> object.  The
     * concrete class returned may implement additional interfaces or provide
     * additional methods used by a plug-in optimiser.  In such cases the
     * plug-in optimsier will attempt to cast the <code>TableScanQuery</code>
     * object created here to the interface it expects to receive.
     * 
     * @return a new instance of a <code>TableScanQuery</code> object.
     */
    TableScanQuery createTableScanQuery();
}
