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
package org.sr.activiti.explorer.ui.budget.source;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.budget.BudgetService;
import org.activiti.engine.budget.ProjectSourceItem;
import org.activiti.engine.budget.ProjectSourceItemQuery;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.data.AbstractLazyLoadingQuery;
import org.activiti.explorer.data.LazyLoadingQuery;
import org.activiti.explorer.ui.management.identity.GroupDetailPanel;


import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;


/**
 * Lab Open Source
 */
public class SourceProjectQuery extends AbstractLazyLoadingQuery {
  
  protected transient BudgetService budgetService;

  protected String idSource;
  protected String sortby;
  protected boolean ascending;
  
  public SourceProjectQuery(String idSource) {
    this.idSource = idSource;
    this.budgetService = ProcessEngines.getDefaultProcessEngine().getBudgetService();
  }

  public int size() {
     return (int)budgetService.createProjectSourceItemQuery().idSource(idSource).count();
  }

  
	public List<Item> loadItems(int start, int count) {
		ProjectSourceItemQuery query = budgetService.createProjectSourceItemQuery().idSource(idSource);

		List<ProjectSourceItem> projectSourceItems = query.listPage(start,count);

		List<Item> items = new ArrayList<Item>();
		for (ProjectSourceItem projectSourceItem : projectSourceItems) {
			ProjectSourceItemItem projectSourceItemItem = new ProjectSourceItemItem(projectSourceItem);

			// Source
			String sourceName = this.getSourceName(projectSourceItem);
			projectSourceItemItem.addItemProperty("source", new ObjectProperty<String>(sourceName, String.class));

			// Project
			String projectName = this.getProjectName(projectSourceItem);
			Button projectButton = new Button(projectName);
			projectButton.addStyleName(Reindeer.BUTTON_LINK);
			final String idProject = projectSourceItem.getIdProject();
			projectButton.addListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					ExplorerApp.get().getViewManager().showProjectPage(idProject);
				}
			});
			projectSourceItemItem.addItemProperty("project",	new ObjectProperty<Button>(projectButton, Button.class));

			items.add(projectSourceItemItem);
		}
		return items;
	}

  public Item loadSingleResult(String id) {
    throw new UnsupportedOperationException();
  }

  public void setSorting(Object[] propertyIds, boolean[] ascending) {
    if (propertyIds.length > 0) {
      this.sortby = propertyIds[0].toString();
      this.ascending = ascending[0];
    }
  }
  
  private String getProjectName(ProjectSourceItem projectSourceItem){
	  String ret = null;	  
	  
	  ret = this.budgetService
			  .createProjectQuery()
			  .projectId(projectSourceItem.getIdProject())
			  .singleResult()
			  .getName();
	  return ret;
	  
  }
  
  private String getSourceName(ProjectSourceItem projectSourceItem){
	  String ret = null;	  
	  
	  ret = this.budgetService
			  .createSourceQuery()
			  .sourceId(projectSourceItem.getIdSource())
			  .singleResult()
			  .getName();
	  return ret;
	  
  }
  
  class ProjectSourceItemItem extends PropertysetItem {
    
    private static final long serialVersionUID = 1L;

    public ProjectSourceItemItem(final ProjectSourceItem projectSourceItem) {
    	
      // id
      addItemProperty("id", new ObjectProperty<String>(projectSourceItem.getId(), String.class));
      
         
      // total
      if (projectSourceItem.getTotal() != null) {
        addItemProperty("total", new ObjectProperty<Double>(projectSourceItem.getTotal(), Double.class));
      } 
      
      // actual
      if (projectSourceItem.getActual() != null) {
        addItemProperty("actual", new ObjectProperty<Double>(projectSourceItem.getActual(), Double.class));
      } 
      
    }
    
  }

}
