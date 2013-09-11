package org.sr.activiti.explorer.ui.budget.costEntry;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.data.LazyLoadingQuery;
import org.activiti.explorer.navigation.UriFragment;
import org.sr.activiti.explorer.navigation.budget.CostEntryNavigator;
import org.activiti.explorer.ui.Images;
import org.sr.activiti.explorer.ui.budget.BudgetPage;
import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Table;

public class CostEntryPage extends BudgetPage{
	private static final long serialVersionUID = 1L;
	
	
	  protected String costEntryId;
	  protected Table costEntryTable;
	  protected LazyLoadingQuery costEntryListQuery;
	  protected LazyLoadingContainer costEntryListContainer;
	  
	  public CostEntryPage() {
	    ExplorerApp.get().setCurrentUriFragment(
	            new UriFragment(CostEntryNavigator.COSTENTRY_URI_PART));
	  }
	  
	  public CostEntryPage(String costEntryId) {
	    this.costEntryId = costEntryId;
	  }
	  
	  @Override
	  protected void initUi() {
	    super.initUi();
	    
	    if (costEntryId == null) {
	      selectElement(0);
	    } else {
	      selectElement(costEntryListContainer.getIndexForObjectId(costEntryId));
	    }
	  }

	  protected Table createList() {
	    costEntryTable = new Table();
	    
	    costEntryTable.setEditable(false);
	    costEntryTable.setImmediate(true);
	    costEntryTable.setSelectable(true);
	    costEntryTable.setNullSelectionAllowed(false);
	    costEntryTable.setSortDisabled(true);
	    costEntryTable.setSizeFull();
	    
	    costEntryListQuery = new CostEntryListQuery();
	    costEntryListContainer = new LazyLoadingContainer(costEntryListQuery, 30);
	    costEntryTable.setContainerDataSource(costEntryListContainer);
	    
	    // Column headers
	    costEntryTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.GROUP_22));
	    costEntryTable.setColumnWidth("icon", 22);
	    costEntryTable.addContainerProperty("name", String.class, null);
	    costEntryTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
	            
	    // Listener to change right panel when clicked on a user
	    costEntryTable.addListener(new Property.ValueChangeListener() {
	      private static final long serialVersionUID = 1L;
	      public void valueChange(ValueChangeEvent event) {
	        Item item = costEntryTable.getItem(event.getProperty().getValue()); // the value of the property is the itemId of the table entry
	        if(item != null) {
	          String costEntryId = (String) item.getItemProperty("id").getValue();
	          setDetailComponent(new CostEntryDetailPanel(CostEntryPage.this, costEntryId));
	          
	          
	          // Update URL
	          ExplorerApp.get().setCurrentUriFragment(
	            new UriFragment(CostEntryNavigator.COSTENTRY_URI_PART, costEntryId));
	        } else {
	          // Nothing is selected
	          setDetailComponent(null);
	          ExplorerApp.get().setCurrentUriFragment(new UriFragment(CostEntryNavigator.COSTENTRY_URI_PART, costEntryId));
	        }
	      }
	    });
	    
	    return costEntryTable;
	  }
	  
	  public void notifyGroupChanged(String costEntryId) {
	    // Clear cache
	    costEntryTable.removeAllItems();
	    costEntryListContainer.removeAllItems();
	    
	    // select changed group
	    costEntryTable.select(costEntryListContainer.getIndexForObjectId(costEntryId));
	  }

}
