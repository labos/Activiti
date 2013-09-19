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
package org.sr.activiti.explorer.ui.form;

import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.budget.ProjectSourceItem;
import org.activiti.engine.form.FormProperty;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;
import org.sr.activiti.explorer.form.ProjectSourceItemFormType;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

/**
 * @author Lab Open Source
 */
public class ProjectSourceItemFormPropertyRenderer extends AbstractFormPropertyRenderer {
  
  private static final long serialVersionUID = 1L;

  public ProjectSourceItemFormPropertyRenderer() {
    super(ProjectSourceItemFormType.class);
  }

  public Field getPropertyField(FormProperty formProperty) {
    ComboBox comboBox = new ComboBox(getPropertyLabel(formProperty));
    comboBox.setRequired(formProperty.isRequired());
    comboBox.setRequiredError(getMessage(Messages.FORM_FIELD_REQUIRED, getPropertyLabel(formProperty)));
    comboBox.setEnabled(formProperty.isWritable());
    
    List<ProjectSourceItem> projectSourceItems = ProcessEngines.getDefaultProcessEngine()
    		.getBudgetService()
    		.createProjectSourceItemQuery()
    		.list();
    		
    for(ProjectSourceItem projectSourceItem: projectSourceItems){
    	comboBox.addItem(projectSourceItem.getId());
    	String name = projectSourceItem.getIdProject() + " " +  projectSourceItem.getIdSource() + " (Residuo: " + projectSourceItem.getActual() + ")";
    	comboBox.setItemCaption(projectSourceItem.getId(), name);
    }
    		
    // Select first
    if (projectSourceItems.size() > 0) {
      comboBox.setNullSelectionAllowed(false);
      comboBox.select(projectSourceItems.get(0).getId());
    }
    
    return comboBox;
  }

}
