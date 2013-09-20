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
package org.sr.activiti.explorer.ui.form;

import org.activiti.engine.form.FormProperty;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;
import org.sr.activiti.explorer.form.TextMessageFormType;

import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

public class TextMessageFormPropertyRenderer extends
		AbstractFormPropertyRenderer {

	public TextMessageFormPropertyRenderer() {
		super(TextMessageFormType.class);
	}

	@Override
	public Field getPropertyField(FormProperty formProperty) {
		TextField textField = new TextField(getPropertyLabel(formProperty));
		textField.setValue(formProperty.getValue());
		textField.setReadOnly(true);
		return textField;
	}

}