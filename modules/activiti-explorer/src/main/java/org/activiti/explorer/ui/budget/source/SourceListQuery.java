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
package org.activiti.explorer.ui.budget.source;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.budget.BudgetService;
import org.activiti.engine.budget.Source;
import org.activiti.explorer.data.AbstractLazyLoadingQuery;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;


/**
 * @author Joram Barrez
 */
public class SourceListQuery extends AbstractLazyLoadingQuery {
  
  /**
	 * 
 */
private static final long serialVersionUID = 1L;
protected transient BudgetService budgetService;
  
  public SourceListQuery() {
    this.budgetService = ProcessEngines.getDefaultProcessEngine().getBudgetService();
  }

  public int size() {
    return (int) budgetService.createSourceQuery().count();
  }

  public List<Item> loadItems(int start, int count) {
    List<Source> sources = budgetService.createSourceQuery()
      .listPage(start, count);
    
    List<Item> sourceListItems = new ArrayList<Item>();
    for (Source source : sources) {
    	sourceListItems.add(new SourceListItem(source));
    }
    return sourceListItems;
  }

  public Item loadSingleResult(String id) {
    return new SourceListItem(budgetService.createSourceQuery().sourceId(id).singleResult());
  }

  public void setSorting(Object[] propertyIds, boolean[] ascending) {
    throw new UnsupportedOperationException();
  }
  
  class SourceListItem extends PropertysetItem implements Comparable<SourceListItem> {
    
    private static final long serialVersionUID = 1L;
    
    public SourceListItem(Source source) {
      addItemProperty("id", new ObjectProperty<String>(source.getId(), String.class));
      if (source.getName() != null) {
        addItemProperty("name", new ObjectProperty<String>(source.getName()
                + " (" + source.getTotal() + ")", String.class));
      } else {
        addItemProperty("name", new ObjectProperty<String>("(" + source.getId() + ")", String.class));
      }
    }

    public int compareTo(SourceListItem other) {
      String id = (String) getItemProperty("id").getValue();
      String otherId = (String) other.getItemProperty("id").getValue();
      return id.compareTo(otherId);
    }
    
  }

}
