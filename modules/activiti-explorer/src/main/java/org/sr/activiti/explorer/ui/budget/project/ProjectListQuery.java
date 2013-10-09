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
package org.sr.activiti.explorer.ui.budget.project;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.budget.BudgetService;
import org.activiti.engine.budget.Project;
import org.activiti.explorer.data.AbstractLazyLoadingQuery;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

/**
 * @author Lab Open Source
 */
public class ProjectListQuery extends AbstractLazyLoadingQuery {
  
	private static final long serialVersionUID = 1L;
	protected transient BudgetService budgetService;
  
  public ProjectListQuery() {
    this.budgetService = ProcessEngines.getDefaultProcessEngine().getBudgetService();
  }

  public int size() {
    return (int) budgetService.createProjectQuery().count();
  }

  public List<Item> loadItems(int start, int count) {
    List<Project> projects = budgetService.createProjectQuery().listPage(start, count);
    
    List<Item> projectListItems = new ArrayList<Item>();
    for (Project project : projects) {
    	projectListItems.add(new ProjectListItem(project));
    }
    return projectListItems;
  }

  public Item loadSingleResult(String id) {
    return new ProjectListItem(budgetService.createProjectQuery().projectId(id).singleResult());
  }

  public void setSorting(Object[] propertyIds, boolean[] ascending) {
    throw new UnsupportedOperationException();
  }
  
  class ProjectListItem extends PropertysetItem implements Comparable<ProjectListItem> {
    
    private static final long serialVersionUID = 1L;
    
    public ProjectListItem(Project project) {
      addItemProperty("id", new ObjectProperty<String>(project.getId(), String.class));
      if (project.getName() != null) {
        addItemProperty("name", new ObjectProperty<String>(project.getName(), String.class));
      } else {
        addItemProperty("name", new ObjectProperty<String>(project.getId(), String.class));
      }
    }

    public int compareTo(ProjectListItem other) {
      String id = (String) getItemProperty("id").getValue();
      String otherId = (String) other.getItemProperty("id").getValue();
      return id.compareTo(otherId);
    }
    
  }

}
