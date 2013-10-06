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

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.budget.BudgetService;
import org.activiti.engine.budget.Project;
import org.activiti.engine.identity.User;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.data.LazyLoadingQuery;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.custom.DetailPanel;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
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
public class ProjectDetailPanel extends DetailPanel {

	private static final long serialVersionUID = 1L;

	protected transient IdentityService identityService;
	protected transient BudgetService budgetService;
	protected I18nManager i18nManager;

	protected ProjectPage projectPage;
	protected Project project;
	protected VerticalLayout panelLayout;

	protected boolean editingDetails;
	protected HorizontalLayout detailLayout;
	protected GridLayout detailsGrid;
	protected TextField nameTextField;
	protected HorizontalLayout sourcesLayout;
	protected Table sourcesTable;
	protected Label noSourcesTable;

	public ProjectDetailPanel(ProjectPage projectPage, String projectId) {
		this.projectPage = projectPage;
		this.budgetService = ProcessEngines.getDefaultProcessEngine()
				.getBudgetService();
		this.project = budgetService.createProjectQuery().projectId(projectId).singleResult();
		this.i18nManager = ExplorerApp.get().getI18nManager();

		init();
	}

	protected void init() {
		setSizeFull();
		addStyleName(Reindeer.PANEL_LIGHT);

		initPageTitle();
		initProjectDetails();
		initProjectSources();

	}

	protected void initPageTitle() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100, UNITS_PERCENTAGE);
		layout.addStyleName(ExplorerLayout.STYLE_TITLE_BLOCK);
		layout.setSpacing(true);
		layout.setMargin(false, false, true, false);
		addDetailComponent(layout);

		Embedded projectImage = new Embedded(null, Images.GROUP_50);
		layout.addComponent(projectImage);

		Label projectName = new Label(getProjectName(project));
		projectName.setSizeUndefined();
		projectName.addStyleName(Reindeer.LABEL_H2);
		layout.addComponent(projectName);
		layout.setComponentAlignment(projectName, Alignment.MIDDLE_LEFT);
		layout.setExpandRatio(projectName, 1.0f);
	}

	protected String getProjectName(Project theProject) {
		if (theProject.getName() == null) {
			return theProject.getId();
		}
		return theProject.getName();
	}

	protected void initProjectDetails() {
		Label projectDetailsHeader = new Label(
				i18nManager.getMessage(Messages.GROUP_HEADER_DETAILS));
		projectDetailsHeader.addStyleName(ExplorerLayout.STYLE_H3);
		projectDetailsHeader.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);

		addDetailComponent(projectDetailsHeader);

		detailLayout = new HorizontalLayout();
		detailLayout.setSpacing(true);
		detailLayout.setMargin(true, false, true, false);
		addDetailComponent(detailLayout);

		populateProjectDetails();
	}

	protected void populateProjectDetails() {
		initProjectProperties();

	}

	protected void initProjectProperties() {
		detailsGrid = new GridLayout(2, 3);
		detailsGrid.setSpacing(true);
		detailLayout.setMargin(true, true, true, false);
		detailLayout.addComponent(detailsGrid);

		// id
		Label idLabel = new Label(i18nManager.getMessage(Messages.GROUP_ID)
				+ ": ");
		idLabel.addStyleName(ExplorerLayout.STYLE_LABEL_BOLD);
		detailsGrid.addComponent(idLabel);
		Label idValueLabel = new Label(project.getId());
		detailsGrid.addComponent(idValueLabel);

		// name
		Label nameLabel = new Label(i18nManager.getMessage(Messages.GROUP_NAME)
				+ ": ");
		nameLabel.addStyleName(ExplorerLayout.STYLE_LABEL_BOLD);
		detailsGrid.addComponent(nameLabel);
		if (!editingDetails) {
			Label nameValueLabel = new Label(project.getName());
			detailsGrid.addComponent(nameValueLabel);
		} else {
			nameTextField = new TextField(null, project.getName());
			detailsGrid.addComponent(nameTextField);
		}

		// Total
		// Label totalLabel = new
		// Label(i18nManager.getMessage(Messages.GROUP_TYPE) + ": ");
		Label totalLabel = new Label("Total" + ": ");
		totalLabel.addStyleName(ExplorerLayout.STYLE_LABEL_BOLD);
		detailsGrid.addComponent(totalLabel);
		Label typeValueLabel = new Label(project.getTotal().toString());
		detailsGrid.addComponent(typeValueLabel);

	}

	protected void initProjectSources() {
		HorizontalLayout sourcesHeader = new HorizontalLayout();
		sourcesHeader.setSpacing(true);
		sourcesHeader.setWidth(100, UNITS_PERCENTAGE);
		sourcesHeader.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
		addDetailComponent(sourcesHeader);

		initSourcesTitle(sourcesHeader);

		sourcesLayout = new HorizontalLayout();
		sourcesLayout.setWidth(100, UNITS_PERCENTAGE);
		addDetailComponent(sourcesLayout);
		initProjectSourceTable();
	}

	protected void initSourcesTitle(HorizontalLayout sourcesHeader) {
		//Label usersHeader = new Label(i18nManager.getMessage(Messages.GROUP_HEADER_USERS));
		Label label = new Label("Fonti");
		label.addStyleName(ExplorerLayout.STYLE_H3);
		sourcesHeader.addComponent(label);
	}

	protected void initProjectSourceTable() {
		LazyLoadingQuery query = new ProjectSourceQuery(project.getId());
		if (query.size() > 0) {
			sourcesTable = new Table();
			sourcesTable.setWidth(100, UNITS_PERCENTAGE);
			sourcesTable.setHeight(400, UNITS_PIXELS);

			sourcesTable.setEditable(false);
			sourcesTable.setSelectable(false);
			sourcesTable.setSortDisabled(true);

			LazyLoadingContainer container = new LazyLoadingContainer(query, 30);
			sourcesTable.setContainerDataSource(container);

			sourcesTable.addContainerProperty("id", String.class, null);
			sourcesTable.addContainerProperty("idProject", String.class, null);
			sourcesTable.addContainerProperty("idSource", Button.class, null);
			sourcesTable.addContainerProperty("total", Double.class, null);
			sourcesTable.addContainerProperty("actual", Double.class, null);
			
			sourcesLayout.addComponent(sourcesTable);
		} else {
			//noSourcesTable = new Label(i18nManager.getMessage(Messages.GROUP_NO_MEMBERS));
			noSourcesTable = new Label("Nessuna fonte per il progetto selezionato" );
			sourcesLayout.addComponent(noSourcesTable);
		}
	}

}
