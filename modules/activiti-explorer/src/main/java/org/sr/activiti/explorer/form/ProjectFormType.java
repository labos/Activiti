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
import org.activiti.engine.form.AbstractFormType;


/**
 * @author Lab Open Source
 */
public class ProjectFormType extends AbstractFormType {

  public static final String TYPE_NAME = "project";
  
  public String getName() {
    return TYPE_NAME;
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    if(propertyValue != null) {
      BudgetRow budgetRow = new BudgetRow();
     
      budgetRow.setId(propertyValue);
      budgetRow.setProjectName(propertyValue);
      budgetRow.setProjectAmount(new Double(100)); 
        
      return budgetRow;
    }
    return null;
  }

  @Override
  public String convertModelValueToFormValue(Object modelValue) {
    if (modelValue == null) {
      return null;
    }
    if (!(modelValue instanceof BudgetRow)) {
      throw new ActivitiIllegalArgumentException("This form type only support budget rows, but is " + modelValue.getClass());
    }
    return ((BudgetRow) modelValue).getId();
  }
}
