package org.activiti.explorer.ui.budget;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.data.LazyLoadingQuery;
import org.activiti.explorer.navigation.GroupNavigator;
import org.activiti.explorer.navigation.UriFragment;
import org.activiti.explorer.navigation.budget.SourceNavigator;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.management.identity.GroupDetailPanel;
import org.activiti.explorer.ui.management.identity.GroupListQuery;
import org.activiti.explorer.ui.management.identity.GroupPage;
import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Table;

public class SourcePage extends BudgetPage{
	private static final long serialVersionUID = 1L;
	
	
	  protected String sourceId;
	  protected Table sourceTable;
	  protected LazyLoadingQuery sourceListQuery;
	  protected LazyLoadingContainer sourceListContainer;
	  
	  public SourcePage() {
	    ExplorerApp.get().setCurrentUriFragment(
	            new UriFragment(SourceNavigator.SOURCE_URI_PART));
	  }
	  
	  public SourcePage(String sourceId) {
	    this.sourceId = sourceId;
	  }
	  
	  @Override
	  protected void initUi() {
	    super.initUi();
	    
	    if (sourceId == null) {
	      selectElement(0);
	    } else {
	      selectElement(sourceListContainer.getIndexForObjectId(sourceId));
	    }
	  }

	  protected Table createList() {
	    sourceTable = new Table();
	    
	    sourceTable.setEditable(false);
	    sourceTable.setImmediate(true);
	    sourceTable.setSelectable(true);
	    sourceTable.setNullSelectionAllowed(false);
	    sourceTable.setSortDisabled(true);
	    sourceTable.setSizeFull();
	    
	    sourceListQuery = new SourceListQuery();
	    sourceListContainer = new LazyLoadingContainer(sourceListQuery, 30);
	    sourceTable.setContainerDataSource(sourceListContainer);
	    
	    // Column headers
	    sourceTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.GROUP_22));
	    sourceTable.setColumnWidth("icon", 22);
	    sourceTable.addContainerProperty("name", String.class, null);
	    sourceTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
	            
	    // Listener to change right panel when clicked on a user
	    sourceTable.addListener(new Property.ValueChangeListener() {
	      private static final long serialVersionUID = 1L;
	      public void valueChange(ValueChangeEvent event) {
	        Item item = sourceTable.getItem(event.getProperty().getValue()); // the value of the property is the itemId of the table entry
	        if(item != null) {
	          String sourceId = (String) item.getItemProperty("id").getValue();
	         // setDetailComponent(new SourceDetailPanel(SourcePage.this, sourceId));
	          setDetailComponent(null); //Da cancellare
	          
	          // Update URL
	          ExplorerApp.get().setCurrentUriFragment(
	            new UriFragment(SourceNavigator.SOURCE_URI_PART, sourceId));
	        } else {
	          // Nothing is selected
	          setDetailComponent(null);
	          ExplorerApp.get().setCurrentUriFragment(new UriFragment(SourceNavigator.SOURCE_URI_PART, sourceId));
	        }
	      }
	    });
	    
	    return sourceTable;
	  }
	  
	  public void notifyGroupChanged(String sourceId) {
	    // Clear cache
	    sourceTable.removeAllItems();
	    sourceListContainer.removeAllItems();
	    
	    // select changed group
	    sourceTable.select(sourceListContainer.getIndexForObjectId(sourceId));
	  }

}
