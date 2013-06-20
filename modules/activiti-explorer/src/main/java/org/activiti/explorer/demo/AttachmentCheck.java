
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

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.task.Attachment;



/**
 * @author Joram Barrez
 */
public class AttachmentCheck implements JavaDelegate {
  
 
  private Expression minAttachmentsNum;
  
  public void execute(DelegateExecution execution) {
	  Boolean isAttached = false;
	  Integer numAttachments = 1;
	  if( minAttachmentsNum.getValue(execution)!= null	){
		  numAttachments = Integer.parseInt((String)minAttachmentsNum.getValue(execution));
	  }
	  List<Attachment> attachmentList =  execution.getEngineServices().getTaskService().getProcessInstanceAttachments( execution.getProcessInstanceId());
	  if(attachmentList.size() >= numAttachments.intValue()){
		  isAttached = true;
	  }
    execution.setVariable("isAttached",isAttached);
  }
  
}
