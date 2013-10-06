package org.sr.activiti.explorer.ui.budget.program;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.data.LazyLoadingQuery;
import org.activiti.explorer.navigation.ProcessNavigator;
import org.activiti.explorer.navigation.UriFragment;
import org.sr.activiti.explorer.navigation.budget.ProgramNavigator;
import org.activiti.explorer.ui.Images;
import org.sr.activiti.explorer.ui.budget.BudgetPage;
import org.activiti.explorer.ui.process.ProcessDefinitionDetailPanel;
import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Table;

public class ProgramPage extends BudgetPage{
	private static final long serialVersionUID = 1L;
	
	
	  protected String programId;
	  protected Table programTable;
	  protected LazyLoadingQuery programListQuery;
	  protected LazyLoadingContainer programListContainer;
	  
	  public ProgramPage() {
	    ExplorerApp.get().setCurrentUriFragment(
	            new UriFragment(ProgramNavigator.PROGRAM_URI_PART));
	  }
	  
	  public ProgramPage(String programId) {
	    this.programId = programId;
	  }
	  
	  @Override
	  protected void initUi() {
	    super.initUi();
	    
	    if (programId == null) {
	      selectElement(0);
	    } else {
	      selectElement(programListContainer.getIndexForObjectId(programId));
	    }
	  }

	  protected Table createList() {
	    programTable = new Table();
	    
	    programTable.setEditable(false);
	    programTable.setImmediate(true);
	    programTable.setSelectable(true);
	    programTable.setNullSelectionAllowed(false);
	    programTable.setSortDisabled(true);
	    programTable.setSizeFull();
	    
	    programListQuery = new ProgramListQuery();
	    programListContainer = new LazyLoadingContainer(programListQuery, 30);
	    programTable.setContainerDataSource(programListContainer);
	    
	    // Column headers
	    programTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.GROUP_22));
	    programTable.setColumnWidth("icon", 22);
	    programTable.addContainerProperty("name", String.class, null);
	    programTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
	            
	    // Listener to change right panel when clicked on a user
	    programTable.addListener(new Property.ValueChangeListener() {
	      private static final long serialVersionUID = 1L;
	      public void valueChange(ValueChangeEvent event) {
	        Item item = programTable.getItem(event.getProperty().getValue()); // the value of the property is the itemId of the table entry
	        if(item != null) {
	          String programId = (String) item.getItemProperty("id").getValue();
	          setDetailComponent(new ProgramDetailPanel(ProgramPage.this, programId));          
	          
	          // Update URL
	          ExplorerApp.get().setCurrentUriFragment(new UriFragment(ProgramNavigator.PROGRAM_URI_PART, programId));
	        } else {
	          // Nothing is selected
	          setDetailComponent(null);
	          ExplorerApp.get().setCurrentUriFragment(new UriFragment(ProgramNavigator.PROGRAM_URI_PART, programId));
	        }
	      }
	    });
	    
	    return programTable;
	  }
	  
	 
}
