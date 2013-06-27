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

import org.activiti.engine.budget.Program;
import org.activiti.engine.budget.ProgramQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.budget.ProgramQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;


/**
 * Lab Open Source
 */
public class ProgramEntityManager extends AbstractManager {

	public Program createNewProgram(String programId) {
		return new ProgramEntity(programId);
	}

	public void insertProgram(Program program) {
		getDbSqlSession().insert((PersistentObject) program);
	}

	public void updateProgram(ProgramEntity updatedProgram) {
		CommandContext commandContext = Context.getCommandContext();
		DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
		dbSqlSession.update(updatedProgram);
	}

	public ProgramEntity findProgramById(String programId) {
		return (ProgramEntity) getDbSqlSession().selectOne("selectProgramById",	programId);
	}

	@SuppressWarnings("unchecked")
	public List<Program> findProgramByQueryCriteria(ProgramQueryImpl query,Page page) {
		return getDbSqlSession().selectList("selectProgramByQueryCriteria",	query, page);
	}

	public long findProgramCountByQueryCriteria(ProgramQueryImpl query) {
		return (Long) getDbSqlSession().selectOne(
				"selectProgramCountByQueryCriteria", query);
	}

	public ProgramQuery createNewProgramQuery() {
		return new ProgramQueryImpl(Context.getProcessEngineConfiguration()
				.getCommandExecutorTxRequired());
	}

}
