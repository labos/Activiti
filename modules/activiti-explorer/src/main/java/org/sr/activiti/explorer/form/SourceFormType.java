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

package org.sr.activiti.explorer.form;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.budget.Source;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.impl.persistence.entity.budget.SourceEntity;


/**
 * @author Lab Open Source
 */
public class SourceFormType extends AbstractFormType {

  public static final String TYPE_NAME = "source";
  
  public String getName() {
    return TYPE_NAME;
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    if(propertyValue != null) {
      Source source = ProcessEngines.getDefaultProcessEngine()
    		  .getBudgetService()
    		  .createSourceQuery()
    		  .sourceId(propertyValue)
    		  .singleResult();
      
      if(source == null) {
        throw new ActivitiObjectNotFoundException("Source with id " + propertyValue + " does not exist", SourceEntity.class);
      }
      
      return source;
    }
    return null;
  }

  @Override
  public String convertModelValueToFormValue(Object modelValue) {
    if (modelValue == null) {
      return null;
    }
    if (!(modelValue instanceof Source)) {
      throw new ActivitiIllegalArgumentException("This form type only support sources, but is " + modelValue.getClass());
    }
    return ((Source) modelValue).getId();
  }
}
