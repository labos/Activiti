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
package org.activiti.explorer.ui.form;


import java.util.ArrayList;
import java.util.Random;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;
import org.activiti.explorer.form.LinksFormType;
import org.activiti.explorer.form.TextAreaFormType;


import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Field;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;

public class LinksFormPropertyRenderer extends AbstractFormPropertyRenderer {


  public LinksFormPropertyRenderer() {
		super(LinksFormType.class);
		// TODO Auto-generated constructor stub
	}

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

@Override
  public Field getPropertyField(FormProperty formProperty) {
	Random rand = new Random(); 
	String[] listOfLinks = formProperty.getValue().split("###");
	Table table = new Table("Lista Documenti della Procedura");
	table.addContainerProperty("Nome", String.class,  null);
	table.addContainerProperty("Link",  Link.class,  null);
	table.setHeight("100px");
	int i = 1;
	for( String link: listOfLinks){
		Link linkHtml =  new Link(link, new ExternalResource(link));

		table.addItem(new Object[] {"Documento " + i++ ,linkHtml}, rand.nextInt());
	}
	
    return table;
  }

}

