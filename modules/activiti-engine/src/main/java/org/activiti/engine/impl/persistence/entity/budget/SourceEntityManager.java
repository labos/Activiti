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

import org.activiti.engine.budget.Source;
import org.activiti.engine.budget.SourceQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.budget.SourceQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.AbstractManager;


/**
 * Lab Open Source
 */
public class SourceEntityManager extends AbstractManager {

    
  public Source createNewSource(String sourceId){
	  return new SourceEntity(sourceId);
  }

  
  public SourceEntity findSourceById(String sourceId) {
    return (SourceEntity) getDbSqlSession().selectOne("selectSourceById", sourceId);
  }  
  
  @SuppressWarnings("unchecked")
  public List<Source> findSourceByQueryCriteria(SourceQueryImpl query, Page page) {
    return getDbSqlSession().selectList("selectSourceByQueryCriteria", query, page);
  }
  
  public long findSourceCountByQueryCriteria(SourceQueryImpl query) {
    return (Long) getDbSqlSession().selectOne("selectSourceCountByQueryCriteria", query);
  }
  
  
  public SourceQuery createNewSourceQuery() {
    return new SourceQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
  } 
  
}
