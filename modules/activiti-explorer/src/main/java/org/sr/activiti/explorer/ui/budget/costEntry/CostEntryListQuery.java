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
package org.sr.activiti.explorer.ui.budget.costEntry;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.budget.BudgetService;
import org.activiti.engine.budget.CostEntry;
import org.activiti.explorer.data.AbstractLazyLoadingQuery;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

/**
 * @author Lab Open Source
 */
public class CostEntryListQuery extends AbstractLazyLoadingQuery {
  
  /**
	 * 
 */
private static final long serialVersionUID = 1L;
protected transient BudgetService budgetService;
  
  public CostEntryListQuery() {
    this.budgetService = ProcessEngines.getDefaultProcessEngine().getBudgetService();
  }

  public int size() {
    return (int) budgetService.createCostEntryQuery().count();
  }

  public List<Item> loadItems(int start, int count) {
    List<CostEntry> costEntrys = budgetService.createCostEntryQuery().listPage(start, count);
    
    List<Item> costEntryListItems = new ArrayList<Item>();
    for (CostEntry costEntry : costEntrys) {
    	costEntryListItems.add(new CostEntryListItem(costEntry));
    }
    return costEntryListItems;
  }

  public Item loadSingleResult(String id) {
    return new CostEntryListItem(budgetService.createCostEntryQuery().costEntryId(id).singleResult());
  }

  public void setSorting(Object[] propertyIds, boolean[] ascending) {
    throw new UnsupportedOperationException();
  }
  
  class CostEntryListItem extends PropertysetItem implements Comparable<CostEntryListItem> {
    
    private static final long serialVersionUID = 1L;
    
    public CostEntryListItem(CostEntry costEntry) {
      addItemProperty("id", new ObjectProperty<String>(costEntry.getId(), String.class));
      if (costEntry.getName() != null) {
        addItemProperty("name", new ObjectProperty<String>(costEntry.getName(), String.class));
      } else {
        addItemProperty("name", new ObjectProperty<String>("(" + costEntry.getId() + ")", String.class));
      }
    }

    public int compareTo(CostEntryListItem other) {
      String id = (String) getItemProperty("id").getValue();
      String otherId = (String) other.getItemProperty("id").getValue();
      return id.compareTo(otherId);
    }
    
  }

}
