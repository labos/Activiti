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
package org.activiti.engine.budget;

import org.activiti.engine.budget.SourceQuery;

/**
 * Service to manage Budget Sources, Programs and Projects
 * 
 * @author Lab Open Source
 */
public interface BudgetService {

	/**
	 * Creates a new source. The source is transient and must be saved using
	 * {@link #saveSource(Source)}.
	 * 
	 * @param sourceId
	 *            id for the new source, cannot be null.
	 */
	Source newSource(String sourceId);

	/**
	 * Saves the source. If the source already exists, the source is updated.
	 * 
	 * @param source
	 *            source to save, cannot be null.
	 * @throws RuntimeException
	 *             when a source with the same name already exists.
	 */
	void saveSource(Source source);

	SourceQuery createSourceQuery();

}
