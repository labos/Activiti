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
package org.activiti.explorer.ui.budget.program;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.budget.BudgetService;
import org.activiti.engine.budget.Program;
import org.activiti.explorer.data.AbstractLazyLoadingQuery;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;


/**
 * @author Lab Open Source
 */
public class ProgramListQuery extends AbstractLazyLoadingQuery {
  
  /**
	 * 
 */
private static final long serialVersionUID = 1L;
protected transient BudgetService budgetService;
  
  public ProgramListQuery() {
    this.budgetService = ProcessEngines.getDefaultProcessEngine().getBudgetService();
  }

  public int size() {
    return (int) budgetService.createProgramQuery().count();
  }

  public List<Item> loadItems(int start, int count) {
    List<Program> programs = budgetService.createProgramQuery().listPage(start, count);
    
    List<Item> programListItems = new ArrayList<Item>();
    for (Program program : programs) {
    	programListItems.add(new ProgramListItem(program));
    }
    return programListItems;
  }

  public Item loadSingleResult(String id) {
    return new ProgramListItem(budgetService.createProgramQuery().programId(id).singleResult());
  }

  public void setSorting(Object[] propertyIds, boolean[] ascending) {
    throw new UnsupportedOperationException();
  }
  
  class ProgramListItem extends PropertysetItem implements Comparable<ProgramListItem> {
    
    private static final long serialVersionUID = 1L;
    
    public ProgramListItem(Program program) {
      addItemProperty("id", new ObjectProperty<String>(program.getId(), String.class));
      if (program.getName() != null) {
        addItemProperty("name", new ObjectProperty<String>(program.getName()
                + " (" + program.getTotal() + ")", String.class));
      } else {
        addItemProperty("name", new ObjectProperty<String>("(" + program.getId() + ")", String.class));
      }
    }

    public int compareTo(ProgramListItem other) {
      String id = (String) getItemProperty("id").getValue();
      String otherId = (String) other.getItemProperty("id").getValue();
      return id.compareTo(otherId);
    }
    
  }

}
