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

import org.activiti.engine.budget.ProjectSourceItem;
import org.activiti.engine.budget.ProjectSourceItemQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.budget.ProjectSourceItemQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;


/**
 * Lab Open Source
 */
public class ProjectSourceItemEntityManager extends AbstractManager {

	public ProjectSourceItem createNewProjectSourceItem(String id) {
		return new ProjectSourceItemEntity(id);
	}

	public void insertProjectSourceItem(ProjectSourceItem projectSourceItem) {
		getDbSqlSession().insert((PersistentObject) projectSourceItem);
	}

	public void updateProjectSourceItem(ProjectSourceItemEntity updatedProjectSourceItem) {
		CommandContext commandContext = Context.getCommandContext();
		DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
		dbSqlSession.update(updatedProjectSourceItem);
	}

	public ProjectSourceItemEntity findProjectSourceItemById(String id) {
		return (ProjectSourceItemEntity) getDbSqlSession().selectOne("selectProjectSourceItemById",
				id);
	}

	@SuppressWarnings("unchecked")
	public List<ProjectSourceItem> findProjectSourceItemByQueryCriteria(ProjectSourceItemQueryImpl query,
			Page page) {
		return getDbSqlSession().selectList("selectProjectSourceItemByQueryCriteria",
				query, page);
	}

	public long findProjectSourceItemCountByQueryCriteria(ProjectSourceItemQueryImpl query) {
		return (Long) getDbSqlSession().selectOne(
				"selectProjectSourceItemCountByQueryCriteria", query);
	}

	public ProjectSourceItemQuery createNewProjectSourceItemQuery() {
		return new ProjectSourceItemQueryImpl(Context.getProcessEngineConfiguration()
				.getCommandExecutorTxRequired());
	}

}
