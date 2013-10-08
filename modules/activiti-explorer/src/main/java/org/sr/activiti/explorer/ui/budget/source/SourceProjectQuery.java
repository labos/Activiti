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
     
        
    List<ProjectSourceItem> projectSourceItems = query.listPage(start, count);
    
    List<Item> items = new ArrayList<Item>();
    for (ProjectSourceItem projectSourceItem : projectSourceItems) {
      items.add(new ProjectSourceItemItem(projectSourceItem));
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
  
  class ProjectSourceItemItem extends PropertysetItem {
    
    private static final long serialVersionUID = 1L;

    public ProjectSourceItemItem(final ProjectSourceItem projectSourceItem) {
    	
      // id
      addItemProperty("id", new ObjectProperty<String>(projectSourceItem.getId(), String.class));
      
      // idProject
      if (projectSourceItem.getIdProject() != null) {
    	  Button idProjectButton = new Button(projectSourceItem.getIdProject());
    	  idProjectButton.addStyleName(Reindeer.BUTTON_LINK);
    	  idProjectButton.addListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
              ExplorerApp.get().getViewManager().showProjectPage(projectSourceItem.getIdProject());
            }
          });
        addItemProperty("idProject", new ObjectProperty<Button>(idProjectButton, Button.class));  
      }
      // idSource
      if (projectSourceItem.getIdSource() != null) {
    	addItemProperty("idSource", new ObjectProperty<String>(projectSourceItem.getIdSource(), String.class));  
      }
      
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
