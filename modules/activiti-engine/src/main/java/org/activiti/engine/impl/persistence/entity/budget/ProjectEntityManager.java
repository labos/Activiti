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

package org.activiti.engine.impl.persistence.entity.budget;

import java.util.List;

import org.activiti.engine.budget.Project;
import org.activiti.engine.budget.ProjectQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.budget.ProjectQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;

/**
  * @author Lab Open Source
 */
public class ProjectEntityManager extends AbstractManager {

	public Project createNewProject(String projectId) {
		return new ProjectEntity(projectId);
	}

	public void insertProject(Project project) {
		getDbSqlSession().insert((PersistentObject) project);
	}

	public void updateProject(ProjectEntity updatedProject) {
		CommandContext commandContext = Context.getCommandContext();
		DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
		dbSqlSession.update(updatedProject);
	}

	public ProjectEntity findProjectById(String projectId) {
		return (ProjectEntity) getDbSqlSession().selectOne("selectProjectById",	projectId);
	}

	@SuppressWarnings("unchecked")
	public List<Project> findProjectByQueryCriteria(ProjectQueryImpl query,Page page) {
		return getDbSqlSession().selectList("selectProjectByQueryCriteria",	query, page);
	}

	public long findProjectCountByQueryCriteria(ProjectQueryImpl query) {
		return (Long) getDbSqlSession().selectOne(
				"selectProjectCountByQueryCriteria", query);
	}

	public ProjectQuery createNewProjectQuery() {
		return new ProjectQueryImpl(Context.getProcessEngineConfiguration()
				.getCommandExecutorTxRequired());
	}

}
