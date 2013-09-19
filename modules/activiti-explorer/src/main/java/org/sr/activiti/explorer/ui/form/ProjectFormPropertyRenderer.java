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

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.budget.Project;
import org.activiti.engine.form.FormProperty;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;
import org.sr.activiti.explorer.form.BudgetRow;
import org.sr.activiti.explorer.form.ProjectFormType;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;


/**
 * @author Joram Barrez
 */
public class ProjectFormPropertyRenderer extends AbstractFormPropertyRenderer {
  
  private static final long serialVersionUID = 1L;

  public ProjectFormPropertyRenderer() {
    super(ProjectFormType.class);
  }

  /*
  public Field getPropertyField(FormProperty formProperty) {
    ComboBox comboBox = new ComboBox(getPropertyLabel(formProperty));
    comboBox.setRequired(formProperty.isRequired());
    comboBox.setRequiredError(getMessage(Messages.FORM_FIELD_REQUIRED, getPropertyLabel(formProperty)));
    comboBox.setEnabled(formProperty.isWritable());
    
    List<BudgetRow> budgetRows = new ArrayList<BudgetRow>();
    BudgetRow aBudgetRow = new BudgetRow();
    aBudgetRow.setId("progetto 1");
    aBudgetRow.setProjectName("progetto 1");
    budgetRows.add(aBudgetRow);
    
    aBudgetRow = new BudgetRow();
    aBudgetRow.setId("progetto 2");
    aBudgetRow.setProjectName("progetto 2");
    budgetRows.add(aBudgetRow);
    
    aBudgetRow = new BudgetRow();
    aBudgetRow.setId("progetto 3");
    aBudgetRow.setProjectName("progetto 3");
    budgetRows.add(aBudgetRow);
    
    aBudgetRow = new BudgetRow();
    aBudgetRow.setId("progetto 4");
    aBudgetRow.setProjectName("progetto 4");
    budgetRows.add(aBudgetRow);
    
    aBudgetRow = new BudgetRow();
    aBudgetRow.setId("progetto 5");
    aBudgetRow.setProjectName("progetto 5");
    budgetRows.add(aBudgetRow);
    
    aBudgetRow.setId("progetto 6");
    aBudgetRow.setProjectName("progetto 6");
    budgetRows.add(aBudgetRow);
    
    aBudgetRow = new BudgetRow();
    aBudgetRow.setId("progetto 7");
    aBudgetRow.setProjectName("progetto 7");
    budgetRows.add(aBudgetRow);
    
       
    for(BudgetRow each: budgetRows){
    	comboBox.addItem(each.getId());
        String name = each.getProjectName() + " (" + 100 + ")";
        comboBox.setItemCaption(each.getId(), name);
    	
    }
    
    // Select first
    if(budgetRows.size() > 0){
    	comboBox.setNullSelectionAllowed(false);
        comboBox.select(budgetRows.get(0).getId());    	
    }
    
    return comboBox;
  }*/
  
  public Field getPropertyField(FormProperty formProperty) {
	    ComboBox comboBox = new ComboBox(getPropertyLabel(formProperty));
	    comboBox.setRequired(formProperty.isRequired());
	    comboBox.setRequiredError(getMessage(Messages.FORM_FIELD_REQUIRED, getPropertyLabel(formProperty)));
	    comboBox.setEnabled(formProperty.isWritable());
	    
	    List<Project> projects = ProcessEngines.getDefaultProcessEngine()
	    		.getBudgetService()
	    		.createProjectQuery()
	    		.list();
	    		
	    for(Project project: projects){
	    	comboBox.addItem(project.getId());
	    	String name = project.getName();
	    	comboBox.setItemCaption(project.getId(), name);
	    }
	    		
	    // Select first
	    if (projects.size() > 0) {
	      comboBox.setNullSelectionAllowed(false);
	      comboBox.select(projects.get(0).getId());
	    }
	    
	    return comboBox;
	  }
  

}
