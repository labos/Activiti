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
package org.activiti.engine.impl.budget;

import org.activiti.engine.budget.BudgetService;
import org.activiti.engine.budget.CostEntry;
import org.activiti.engine.budget.CostEntryQuery;
import org.activiti.engine.budget.Program;
import org.activiti.engine.budget.ProgramQuery;
import org.activiti.engine.budget.Project;
import org.activiti.engine.budget.ProjectCostItem;
import org.activiti.engine.budget.ProjectCostItemQuery;
import org.activiti.engine.budget.ProjectQuery;
import org.activiti.engine.budget.Source;
import org.activiti.engine.budget.SourceQuery;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.cmd.budget.costEntry.CreateCostEntryCmd;
import org.activiti.engine.impl.cmd.budget.costEntry.CreateCostEntryQueryCmd;
import org.activiti.engine.impl.cmd.budget.costEntry.SaveCostEntryCmd;
import org.activiti.engine.impl.cmd.budget.program.CreateProgramCmd;
import org.activiti.engine.impl.cmd.budget.program.CreateProgramQueryCmd;
import org.activiti.engine.impl.cmd.budget.program.SaveProgramCmd;
import org.activiti.engine.impl.cmd.budget.project.CreateProjectCmd;
import org.activiti.engine.impl.cmd.budget.project.CreateProjectQueryCmd;
import org.activiti.engine.impl.cmd.budget.project.SaveProjectCmd;
import org.activiti.engine.impl.cmd.budget.projectCostItem.CreateProjectCostItemCmd;
import org.activiti.engine.impl.cmd.budget.projectCostItem.CreateProjectCostItemQueryCmd;
import org.activiti.engine.impl.cmd.budget.projectCostItem.SaveProjectCostItemCmd;
import org.activiti.engine.impl.cmd.budget.source.CreateSourceCmd;
import org.activiti.engine.impl.cmd.budget.source.CreateSourceQueryCmd;
import org.activiti.engine.impl.cmd.budget.source.SaveSourceCmd;
import org.activiti.engine.impl.persistence.entity.budget.CostEntryEntity;
import org.activiti.engine.impl.persistence.entity.budget.ProgramEntity;
import org.activiti.engine.impl.persistence.entity.budget.ProjectCostItemEntity;
import org.activiti.engine.impl.persistence.entity.budget.ProjectEntity;
import org.activiti.engine.impl.persistence.entity.budget.SourceEntity;

/**
 * @author Lab Open Source
 */
public class BudgetServiceImpl extends ServiceImpl implements BudgetService {

	public Source newSource(String sourceId) {
		return commandExecutor.execute(new CreateSourceCmd(sourceId));
	}

	public void saveSource(Source source) {
		commandExecutor.execute(new SaveSourceCmd((SourceEntity) source));
	}

	public SourceQuery createSourceQuery() {
		return commandExecutor.execute(new CreateSourceQueryCmd());
	}
	
	public Program newProgram(String programId) {
		return commandExecutor.execute(new CreateProgramCmd(programId));
	}

	public void saveProgram(Program program) {
		commandExecutor.execute(new SaveProgramCmd((ProgramEntity) program));
	}

	public ProgramQuery createProgramQuery() {
		return commandExecutor.execute(new CreateProgramQueryCmd());
	}
	
	public Project newProject(String projectId) {
		return commandExecutor.execute(new CreateProjectCmd(projectId));
	}

	public void saveProject(Project project) {
		commandExecutor.execute(new SaveProjectCmd((ProjectEntity) project));
	}

	public ProjectQuery createProjectQuery() {
		return commandExecutor.execute(new CreateProjectQueryCmd());
	}
	
	public CostEntry newCostEntry(String costEntryId) {
		return commandExecutor.execute(new CreateCostEntryCmd(costEntryId));
	}

	public void saveCostEntry(CostEntry costEntry) {
		commandExecutor.execute(new SaveCostEntryCmd((CostEntryEntity) costEntry));
	}

	public CostEntryQuery createCostEntryQuery() {
		return commandExecutor.execute(new CreateCostEntryQueryCmd());
	}

	@Override
	public ProjectCostItem newProjectCostItem(String id) {
		return commandExecutor.execute(new CreateProjectCostItemCmd(id));
	}

	@Override
	public void saveProjectCostItem(ProjectCostItem projectCostItem) {
		commandExecutor.execute(new SaveProjectCostItemCmd((ProjectCostItemEntity) projectCostItem));
		
	}

	@Override
	public ProjectCostItemQuery createProjectCostItemQuery() {
		return commandExecutor.execute(new CreateProjectCostItemQueryCmd());
	}
	
	
	

}
