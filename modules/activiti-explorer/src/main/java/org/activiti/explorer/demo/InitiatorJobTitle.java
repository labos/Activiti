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
	  
	  //gets all groups initiator belongs to
	  List<Group> groups = execution.getEngineServices().getIdentityService().createGroupQuery().groupMember((String)idInitiator.getValue(execution)).list();
	 
	  //removes "user" from the group list
	  ListIterator<Group> i = groups.listIterator();
	  while (i.hasNext()) {
		  if (i.next().getId().equals("user")) {
			  i.remove();
		  }
	  }
	  
	  //finds the user of the initiator groups with the role "responsabile" (must be just one even if there is more than one group)
	  execution.setVariable("initiatorGroupHead","kermit");
	  for (Group group : groups) {
		  System.out.println("Gruppo initiator:" + group.getId() + group.getName());
		  for (User user : execution.getEngineServices().getIdentityService().createUserQuery().memberOfGroup(group.getId()).list()) {
			  String userJobTitle = execution.getEngineServices().getIdentityService().getUserInfo(user.getId(), "jobTitle");
			  if (userJobTitle != null && userJobTitle.contains("Responsabile")) {
				  execution.setVariable("initiatorGroupHead",user.getId());
			  }
		  }
	  }
	  
	  //finds the head of the area initiator belongs to. Since the director doesn't
	  for (Group group : groups) {
		  if (group.getId().equals("ric") || group.getId().equals("cds") || group.getId().equals("pst")) {
			  execution.setVariable("initiatorAreaHead","vsongini");
		  } else if (group.getId().equals("agi") || group.getId().equals("sir") || 
				  	  group.getId().equals("sag") || group.getId().equals("spf") || group.getId().equals("app")) {
			  execution.setVariable("initiatorAreaHead","emulas");
		  } else {
			  execution.setVariable("initiatorAreaHead","gpisanu");
		  }
	  }
  }
}