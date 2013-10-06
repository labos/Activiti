package org.sr.activiti.explorer.ui.budget.project;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.data.LazyLoadingQuery;
import org.activiti.explorer.navigation.UriFragment;
import org.sr.activiti.explorer.navigation.budget.ProjectNavigator;
import org.activiti.explorer.ui.Images;
import org.sr.activiti.explorer.ui.budget.BudgetPage;
import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Table;

public class ProjectPage extends BudgetPage{
	private static final long serialVersionUID = 1L;
	
	
	  protected String projectId;
	  protected Table projectTable;
	  protected LazyLoadingQuery projectListQuery;
	  protected LazyLoadingContainer projectListContainer;
	  
	  public ProjectPage() {
	    ExplorerApp.get().setCurrentUriFragment(
	            new UriFragment(ProjectNavigator.PROJECT_URI_PART));
	  }
	  
	  public ProjectPage(String projectId) {
	    this.projectId = projectId;
	  }
	  
	  @Override
	  protected void initUi() {
	    super.initUi();
	    
	    if (projectId == null) {
	      selectElement(0);
	    } else {
	      selectElement(projectListContainer.getIndexForObjectId(projectId));
	    }
	  }

	  protected Table createList() {
	    projectTable = new Table();
	    
	    projectTable.setEditable(false);
	    projectTable.setImmediate(true);
	    projectTable.setSelectable(true);
	    projectTable.setNullSelectionAllowed(false);
	    projectTable.setSortDisabled(true);
	    projectTable.setSizeFull();
	    
	    projectListQuery = new ProjectListQuery();
	    projectListContainer = new LazyLoadingContainer(projectListQuery, 30);
	    projectTable.setContainerDataSource(projectListContainer);
	    
	    // Column headers
	    projectTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.GROUP_22));
	    projectTable.setColumnWidth("icon", 22);
	    projectTable.addContainerProperty("name", String.class, null);
	    projectTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
	            
	    // Listener to change right panel when clicked on a user
	    projectTable.addListener(new Property.ValueChangeListener() {
	      private static final long serialVersionUID = 1L;
	      public void valueChange(ValueChangeEvent event) {
	        Item item = projectTable.getItem(event.getProperty().getValue()); // the value of the property is the itemId of the table entry
	        if(item != null) {
	          String projectId = (String) item.getItemProperty("id").getValue();
	          setDetailComponent(new ProjectDetailPanel(ProjectPage.this, projectId));
	          
	          
	          // Update URL
	          ExplorerApp.get().setCurrentUriFragment(
	            new UriFragment(ProjectNavigator.PROJECT_URI_PART, projectId));
	        } else {
	          // Nothing is selected
	          setDetailComponent(null);
	          ExplorerApp.get().setCurrentUriFragment(new UriFragment(ProjectNavigator.PROJECT_URI_PART, projectId));
	        }
	      }
	    });
	    
	    return projectTable;
	  }
	  
	
}
