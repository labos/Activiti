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
package org.activiti.engine.impl.budget;

import org.activiti.engine.budget.BudgetService;
import org.activiti.engine.budget.SourceQuery;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.cmd.budget.CreateSourceQueryCmd;


/**
 * @author Lab Open Source
 */
public class BudgetServiceImpl extends ServiceImpl implements BudgetService {
  

  public SourceQuery createSourceQuery(){
	  return commandExecutor.execute(new CreateSourceQueryCmd());
  }

  
  
}
