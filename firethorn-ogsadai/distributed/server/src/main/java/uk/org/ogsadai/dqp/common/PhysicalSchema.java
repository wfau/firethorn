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


package uk.org.ogsadai.dqp.common;

/**
 * A container class for physical schema information.
 * 
 * TODO: doc
 * TODO: are names needed?
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface PhysicalSchema
{
    public long getCardinality();
    
    public long getAverageRowLength();
    
    public long getDataLength();
    
    public String getTableName();
    
    public String getDatabaseName();
}
