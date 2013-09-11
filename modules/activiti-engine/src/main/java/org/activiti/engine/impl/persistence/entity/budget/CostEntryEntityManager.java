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

import org.activiti.engine.budget.CostEntry;
import org.activiti.engine.budget.CostEntryQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.budget.CostEntryQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;

/**
 * Lab Open Source
 */
public class CostEntryEntityManager extends AbstractManager {

	public CostEntry createNewCostEntry(String costEntryId) {
		return new CostEntryEntity(costEntryId);
	}

	public void insertCostEntry(CostEntry costEntry) {
		getDbSqlSession().insert((PersistentObject) costEntry);
	}

	public void updateCostEntry(CostEntryEntity updatedCostEntry) {
		CommandContext commandContext = Context.getCommandContext();
		DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
		dbSqlSession.update(updatedCostEntry);
	}

	public CostEntryEntity findCostEntryById(String costEntryId) {
		return (CostEntryEntity) getDbSqlSession().selectOne("selectCostEntryById",
				costEntryId);
	}

	@SuppressWarnings("unchecked")
	public List<CostEntry> findCostEntryByQueryCriteria(CostEntryQueryImpl query,
			Page page) {
		return getDbSqlSession().selectList("selectCostEntryByQueryCriteria",
				query, page);
	}

	public long findCostEntryCountByQueryCriteria(CostEntryQueryImpl query) {
		return (Long) getDbSqlSession().selectOne(
				"selectCostEntryCountByQueryCriteria", query);
	}

	public CostEntryQuery createNewCostEntryQuery() {
		return new CostEntryQueryImpl(Context.getProcessEngineConfiguration()
				.getCommandExecutorTxRequired());
	}

}
