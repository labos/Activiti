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
package org.activiti.explorer.ui.budget;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.budget.BudgetService;
import org.activiti.engine.budget.Source;
import org.activiti.engine.identity.Group;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.custom.DetailPanel;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * @author Lab Open Source
 */
//public class SourceDetailPanel extends DetailPanel implements MemberShipChangeListener {
public class SourceDetailPanel extends DetailPanel {

  private static final long serialVersionUID = 1L;
  
  protected transient IdentityService identityService;
  protected transient BudgetService budgetService;
  protected I18nManager i18nManager;

  protected SourcePage sourcePage;
  protected Source source;
  protected VerticalLayout panelLayout;
  
  protected boolean editingDetails;
  protected HorizontalLayout detailLayout;
  protected GridLayout detailsGrid;
  protected TextField nameTextField;
  protected ComboBox typeCombobox;
  protected HorizontalLayout membersLayout;
  protected Table membersTable;
  protected Label noMembersTable;
  
  public SourceDetailPanel(SourcePage sourcePage, String sourceId) {
	    this.sourcePage = sourcePage;
	    this.budgetService = ProcessEngines.getDefaultProcessEngine().getBudgetService();
	    this.source = budgetService.createSourceQuery().sourceId(sourceId).singleResult();
	    this.i18nManager = ExplorerApp.get().getI18nManager();
	    
	    init();
  }
  
  protected void init() {
    setSizeFull();
    addStyleName(Reindeer.PANEL_LIGHT);
    
    initPageTitle();
    initSourceDetails();
   // initMembers();
    
   
  }
  
  

  protected void initPageTitle() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setWidth(100, UNITS_PERCENTAGE);
    layout.addStyleName(ExplorerLayout.STYLE_TITLE_BLOCK);
    layout.setSpacing(true);
    layout.setMargin(false, false, true, false);
    addDetailComponent(layout);
    
    Embedded sourceImage = new Embedded(null, Images.GROUP_50);
    layout.addComponent(sourceImage);
    
    Label sourceName = new Label(getSourceName(source));
    sourceName.setSizeUndefined();
    sourceName.addStyleName(Reindeer.LABEL_H2);
    layout.addComponent(sourceName);
    layout.setComponentAlignment(sourceName, Alignment.MIDDLE_LEFT);
    layout.setExpandRatio(sourceName, 1.0f);
  }
  
  protected String getSourceName(Source theSource) {
    if(theSource.getName() == null) {
      return theSource.getId();
    }
    return theSource.getName();
  }

  protected void initSourceDetails() {
    Label sourceDetailsHeader = new Label(i18nManager.getMessage(Messages.GROUP_HEADER_DETAILS));
    sourceDetailsHeader.addStyleName(ExplorerLayout.STYLE_H3);
    sourceDetailsHeader.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
    
    addDetailComponent(sourceDetailsHeader);
    
    detailLayout = new HorizontalLayout();
    detailLayout.setSpacing(true);
    detailLayout.setMargin(true, false, true, false);
    addDetailComponent(detailLayout);
    
    populateSourceDetails();
  }
  
  protected void populateSourceDetails() {
    initSourceProperties();
   
  }
  
  protected void initSourceProperties() {
    detailsGrid = new GridLayout(2, 3);
    detailsGrid.setSpacing(true);
    detailLayout.setMargin(true, true, true, false);
    detailLayout.addComponent(detailsGrid);
    
    // id
    Label idLabel = new Label(i18nManager.getMessage(Messages.GROUP_ID) + ": ");
    idLabel.addStyleName(ExplorerLayout.STYLE_LABEL_BOLD);
    detailsGrid.addComponent(idLabel);
    Label idValueLabel = new Label(source.getId());
    detailsGrid.addComponent(idValueLabel);
    
    // name
    Label nameLabel = new Label(i18nManager.getMessage(Messages.GROUP_NAME) + ": ");
    nameLabel.addStyleName(ExplorerLayout.STYLE_LABEL_BOLD);
    detailsGrid.addComponent(nameLabel);
    if (!editingDetails) {
      Label nameValueLabel = new Label(source.getName());
      detailsGrid.addComponent(nameValueLabel);
    } else {
      nameTextField = new TextField(null, source.getName());
      detailsGrid.addComponent(nameTextField);
    }
    
    // Total
   // Label totalLabel = new Label(i18nManager.getMessage(Messages.GROUP_TYPE) + ": ");
    Label totalLabel = new Label("Total" + ": ");
    totalLabel.addStyleName(ExplorerLayout.STYLE_LABEL_BOLD);
    detailsGrid.addComponent(totalLabel);
    Label typeValueLabel = new Label(source.getTotal().toString());
    detailsGrid.addComponent(typeValueLabel);
    
  }
   
  /*
  protected void initMembers() {
    HorizontalLayout membersHeader = new HorizontalLayout();
    membersHeader.setSpacing(true);
    membersHeader.setWidth(100, UNITS_PERCENTAGE);
    membersHeader.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
    addDetailComponent(membersHeader);
    
    initMembersTitle(membersHeader);
    
    
    membersLayout = new HorizontalLayout();
    membersLayout.setWidth(100, UNITS_PERCENTAGE);
    addDetailComponent(membersLayout);
    initMembersTable();
  }
  
  protected void initMembersTitle(HorizontalLayout membersHeader) {
    Label usersHeader = new Label(i18nManager.getMessage(Messages.GROUP_HEADER_USERS));
    usersHeader.addStyleName(ExplorerLayout.STYLE_H3);
    membersHeader.addComponent(usersHeader);
  }
  
    
  // Hacky - must be put in custom service
  protected List<String> getCurrentMembers() {
    List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();
    List<String> userIds = new ArrayList<String>();
    for (User user : users) {
      userIds.add(user.getId());
    }
    return userIds;
  }
  
  protected void initMembersTable() {
    LazyLoadingQuery query = new GroupMembersQuery(group.getId(), this);
    if (query.size() > 0) {
      membersTable = new Table();
      membersTable.setWidth(100, UNITS_PERCENTAGE);
      membersTable.setHeight(400, UNITS_PIXELS);
      
      membersTable.setEditable(false);
      membersTable.setSelectable(false);
      membersTable.setSortDisabled(false);
      
      LazyLoadingContainer container = new LazyLoadingContainer(query, 30);
      membersTable.setContainerDataSource(container);
      
      membersTable.addContainerProperty("id", Button.class, null);
      membersTable.addContainerProperty("firstName", String.class, null);
      membersTable.addContainerProperty("lastName", String.class, null);
      membersTable.addContainerProperty("email", String.class, null);
      membersTable.addContainerProperty("actions", Component.class, null);
      
      membersLayout.addComponent(membersTable);
    } else {
      noMembersTable = new Label(i18nManager.getMessage(Messages.GROUP_NO_MEMBERS));
      membersLayout.addComponent(noMembersTable);
    }
  }
  */
  
  /*
  public void notifyMembershipChanged() {
    membersLayout.removeAllComponents();
    initMembersTable();
    
  
  }
  */

}
