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

import org.activiti.engine.budget.AttachmentCategory;
import org.activiti.engine.budget.AttachmentCategoryQuery;
import org.activiti.engine.budget.Project;
import org.activiti.engine.budget.ProjectQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.budget.AttachmentCategoryQueryImpl;
import org.activiti.engine.impl.budget.ProjectQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;

/**
  * @author Lab Open Source
 */
public class AttachmentCategoryEntityManager extends AbstractManager {

	public AttachmentCategory createNewAttachmentCategory(String attachmentCategoryId) {
		return new AttachmentCategoryEntity(attachmentCategoryId);
	}

	public void insertAttachmentCategory(AttachmentCategory attachmentCategory) {
		getDbSqlSession().insert((PersistentObject) attachmentCategory);
	}

	public void updateAttachmentCategory(AttachmentCategoryEntity updatedAttachmentCategory) {
		CommandContext commandContext = Context.getCommandContext();
		DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
		dbSqlSession.update(updatedAttachmentCategory);
	}

	public AttachmentCategory findAttachmentCategoryById(String AttachmentCategoryId) {
		AttachmentCategory ret = null;
		
		ret = (AttachmentCategory) getDbSqlSession().selectOne("selectAttachmentCategoryById",	AttachmentCategoryId);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<AttachmentCategory> findAttachmentCategoryByQueryCriteria(AttachmentCategoryQueryImpl query,Page page) {
		List<AttachmentCategory> ret = null;
		
		ret = getDbSqlSession().selectList("selectAttachmentCategoryByQueryCriteria",	query, page);
		return ret;
	}

	public long findAttachmentCategoryCountByQueryCriteria(AttachmentCategoryQueryImpl query) {
		return (Long) getDbSqlSession().selectOne("selectAttachmentCategoryCountByQueryCriteria", query);
	}

	public AttachmentCategoryQuery createNewAttachmentCategoryQuery() {
		return null;
		//return new AttachmentCategoryQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
	}

}
