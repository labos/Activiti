/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.budget;

import org.activiti.engine.query.Query;


/**
 * Allows programmatic querying of ProjectSourceItem
 * 
 * @author Lab Open Source
 */
public interface ProjectSourceItemQuery extends Query<ProjectSourceItemQuery, ProjectSourceItem> {
  
  /** Only select ProjectSourceItems with the given id/ */
  ProjectSourceItemQuery projectSourceItemId(String id);
  
  /** Only select ProjectSourceItems with the given idProject/ */
  ProjectSourceItemQuery idProject(String idProject);
  
}
