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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.budget.ProjectCostItem;
import org.activiti.engine.budget.ProjectSourceItem;
import org.activiti.engine.budget.Source;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.HasRevision;
import org.activiti.engine.impl.db.PersistentObject;

/**
 * @author Lab Open Source
 */
public class ProjectSourceItemEntity implements ProjectSourceItem, Serializable, PersistentObject, HasRevision {

	private static final long serialVersionUID = 1L;

	protected String id;
	protected String idProject;
	protected String idSource;
	protected int revision;
	protected Double total;
	protected Double actual;

	public ProjectSourceItemEntity() {
	}

	public ProjectSourceItemEntity(String id) {
		this.id = id;
	}

	public Object getPersistentState() {
		Map<String, Object> persistentState = new HashMap<String, Object>();
		persistentState.put("idProject", idProject);
		persistentState.put("idSource", idSource);
		persistentState.put("total", total);
		persistentState.put("actual", actual);
		return persistentState;
	}

	public int getRevisionNext() {
		return revision + 1;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getIdProject() {
		return idProject;
	}

	public void setIdProject(String idProject) {
		this.idProject = idProject;
	}

	
	public String getIdSource() {
		return idSource;
	}

	public void setIdSource(String idSource) {
		this.idSource = idSource;
	}

	@Override
	public void setRevision(int revision) {
		this.revision = revision;
	}

	@Override
	public int getRevision() {
		return this.revision;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getActual() {
		return actual;
	}

	public void setActual(Double actual) {
		this.actual = actual;
	}	
	

}
