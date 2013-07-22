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

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.activiti.explorer.ExplorerApp;

/**
 * @author Lab Open Source
 */
public class AttachmentCheck implements JavaDelegate {
  
 
  private Expression minAttachmentsNum;
  
  public void execute(DelegateExecution execution) {
	  Boolean isAttached = false;
	  Integer numAttachments = 1;
	  List<Attachment> attachmentList = new ArrayList<Attachment>();
	  if( minAttachmentsNum.getValue(execution)!= null	){
		  numAttachments = Integer.parseInt((String)minAttachmentsNum.getValue(execution));
	  }
  
	  
	
	  List<HistoricTaskInstance> taskList =	  execution.getEngineServices().getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(execution.getProcessInstanceId()).orderByHistoricTaskInstanceStartTime().asc().list();
	  System.out.println("Dimensione tasks: " +taskList.size());
	  
	  if( taskList != null && taskList.size() > 0 ){
		  HistoricTaskInstance lastTask = taskList.get(taskList.size() -1);
		  List<HistoricTaskInstance> taskListRelated =  execution.getEngineServices().getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(execution.getProcessInstanceId()).taskDefinitionKey(lastTask.getTaskDefinitionKey()).list();
		  for(HistoricTaskInstance aTask: taskListRelated){
			  attachmentList.addAll(execution.getEngineServices().getTaskService().getTaskAttachments(aTask.getId()));		 
		  }
		  System.out.println("ID del task: " + lastTask.getId() + " chiave task: " + lastTask.getTaskDefinitionKey() + " Dimensione attachmentlist: " + attachmentList.size() + " -- "  + numAttachments.intValue() + " Dimensione taskListRelated:" + taskListRelated.size());

	  if(attachmentList.size() >= numAttachments.intValue()){
			  isAttached = true;
		  } 
	  }
	 
 
		  

    execution.setVariable("isAttached",isAttached);
    
	  if(!isAttached){
		    ExplorerApp.get().getNotificationManager().showErrorNotification(
		            "attachment.required", 
		            "Devi Allegare " + numAttachments.toString() + " documento/i  in questo task");
	  }
    
  }
  
}
