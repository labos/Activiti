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
package org.activiti.engine.impl.cmd.budget.projectCostItem;

import java.io.Serializable;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.budget.ProjectCostItemEntity;
import org.activiti.engine.impl.persistence.entity.budget.SourceEntity;


/**
 * @author Lab Open Source
 */
public class SaveProjectCostItemCmd implements Command<Void>, Serializable {
  
  private static final long serialVersionUID = 1L;
  protected ProjectCostItemEntity projectCostItem;
  
  public SaveProjectCostItemCmd(ProjectCostItemEntity projectCostItem) {
    this.projectCostItem = projectCostItem;
  }
  
  public Void execute(CommandContext commandContext) {
    if(projectCostItem == null) {
      throw new ActivitiIllegalArgumentException("projectCostItem is null");
    }
    if (projectCostItem.getRevision()==0) {
      commandContext
        .getProjectCostItemEntityManager()
        .insertProjectCostItem(projectCostItem);
    } else {
      commandContext
        .getProjectCostItemEntityManager()
        .updateProjectCostItem(projectCostItem);
    }
    
    return null;
  }

}
