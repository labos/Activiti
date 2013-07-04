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
package org.activiti.explorer.demo;

import java.util.List;
import java.util.ListIterator;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;

/**
 * @author Lab Open Source
 */
public class InitiatorJobTitle implements JavaDelegate {
  
  private Expression idInitiator;
  
  public void execute(DelegateExecution execution) {
	  String initiatorJobTitle = execution.getEngineServices().getIdentityService().getUserInfo((String)idInitiator.getValue(execution), "jobTitle");
	  execution.setVariable("initiatorJobTitle",initiatorJobTitle);
	  
	  
	  List<Group> groups = execution.getEngineServices().getIdentityService().createGroupQuery().groupMember((String)idInitiator.getValue(execution)).list();
	 
	  ListIterator<Group> i = groups.listIterator();
	  while (i.hasNext()) {
		  if (i.next().getId().equals("user")) {
			  i.remove();
		  }
	  }
	  
	  execution.setVariable("initiatorGroupHead","kermit");
	  for (Group group : groups) {
		  System.out.println(group.getId());
		  for (User user : execution.getEngineServices().getIdentityService().createUserQuery().memberOfGroup(group.getId()).list()) {
			  System.out.println(user.getId());
			  String userJobTitle = execution.getEngineServices().getIdentityService().getUserInfo(user.getId(), "jobTitle");
			  System.out.println(userJobTitle);
			  if (userJobTitle != null && userJobTitle.equals("Responsabile")) {
				  execution.setVariable("initiatorGroupHead",user.getId());
			}
		}
	}
  }
}