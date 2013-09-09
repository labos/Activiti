
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
package org.sr.activiti.explorer.delegates;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.DelegateExecution;

/**
 * @author Lab Open Source
 */
public class EmailInitiator implements JavaDelegate {
  
  private Expression idInitiator;
  
  public void execute(DelegateExecution execution) {
	  String emailInitiator = execution.getEngineServices().getIdentityService().getUserEmail((String)idInitiator.getValue(execution));
    execution.setVariable("emailInitiator",emailInitiator);
  }
  
}