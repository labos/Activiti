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

import org.activiti.engine.budget.ProjectCostItem;
import org.activiti.engine.budget.ProjectCostItemQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.budget.ProjectCostItemQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;


/**
 * Lab Open Source
 */
public class ProjectCostItemEntityManager extends AbstractManager {

	public ProjectCostItem createNewProjectCostItem(String id) {
		return new ProjectCostItemEntity(id);
	}

	public void insertProjectCostItem(ProjectCostItem projectCostItem) {
		getDbSqlSession().insert((PersistentObject) projectCostItem);
	}

	public void updateProjectCostItem(ProjectCostItemEntity updatedProjectCostItem) {
		CommandContext commandContext = Context.getCommandContext();
		DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
		dbSqlSession.update(updatedProjectCostItem);
	}

	public ProjectCostItemEntity findProjectCostItemById(String id) {
		return (ProjectCostItemEntity) getDbSqlSession().selectOne("selectProjectCostItemById",
				id);
	}

	@SuppressWarnings("unchecked")
	public List<ProjectCostItem> findProjectCostItemByQueryCriteria(ProjectCostItemQueryImpl query,
			Page page) {
		return getDbSqlSession().selectList("selectProjectCostItemByQueryCriteria",
				query, page);
	}

	public long findProjectCostItemCountByQueryCriteria(ProjectCostItemQueryImpl query) {
		return (Long) getDbSqlSession().selectOne(
				"selectProjectCostItemCountByQueryCriteria", query);
	}

	public ProjectCostItemQuery createNewProjectCostItemQuery() {
		return new ProjectCostItemQueryImpl(Context.getProcessEngineConfiguration()
				.getCommandExecutorTxRequired());
	}

}
