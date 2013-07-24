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

package org.activiti.explorer.form;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.form.AbstractFormType;


public class TextAreaFormType extends AbstractFormType {

  public static final String TYPE_NAME = "textarea";
  
  public String getName() {
    return TYPE_NAME;
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    return propertyValue;
  }

  @Override
  public String convertModelValueToFormValue(Object modelValue) {
    return (String) modelValue;
  }
}