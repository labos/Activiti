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

import org.activiti.engine.budget.CostEntry;
import org.activiti.engine.impl.db.HasRevision;
import org.activiti.engine.impl.db.PersistentObject;

/**
 * @author Lab Open Source
 */
public class CostEntryEntity implements CostEntry, Serializable, PersistentObject, HasRevision {

	private static final long serialVersionUID = 1L;

	protected String id;
	protected int revision;
	protected String name;

	public CostEntryEntity() {
	}

	public CostEntryEntity(String id) {
		this.id = id;
	}

	public Object getPersistentState() {
		Map<String, Object> persistentState = new HashMap<String, Object>();
		persistentState.put("name", name);
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
	public String getName() {
		return name;
	}
	public void setName(String firstName) {
		this.name = firstName;
	}
	@Override
	public void setRevision(int revision) {
		this.revision = revision;
	}
	@Override
	public int getRevision() {
		return this.revision;
	}

}
