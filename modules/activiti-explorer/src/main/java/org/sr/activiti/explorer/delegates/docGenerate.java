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
package org.sr.activiti.explorer.delegates;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.explorer.ExplorerApp;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.sr.activiti.bean.ApplicationConf;
import org.sr.activiti.explorer.delegates.cmis.CmisUtil;

/**
 * @author Lab Open Source
 */
@Configuration
@PropertySource("delegates.properties")
public class docGenerate implements JavaDelegate {
	
	@Autowired
    private Environment env;
  
	private static final String ALFRESCO_CMIS_URL =
				"http://alfrescotest.consorzio21.it:8080/alfresco/service/cmis";
	private static final String ALFRESCO_ADMIN_USER = "admin";
	private static final String ALFRESCO_ADMIN_PASSWORD = "tubonero.99";
	private static final String ALFRESCO_TEMPLATE_DETERMINAZIONI_PATH = 
				"/Sites/sardegna-ricerche/documentLibrary/Modelli/Determinazioni/template_determinazione-DG.docx";	
	
	private Expression data_determinazione;
	private Expression numero_determinazione;
	private Expression oggetto_determinazione;
	private Expression nome_rup;
//	private AnnotationConfigApplicationContext context =
//    	     new AnnotationConfigApplicationContext(ApplicationConf.class);
//	
	
	public void execute(DelegateExecution execution) throws IOException, JAXBException {
	  
		Long numDet = (Long) numero_determinazione.getValue(execution);
		Date dataDet = (Date) data_determinazione.getValue(execution);
		String oggettoDet = (String) oggetto_determinazione.getValue(execution);
		String rup = (String) nome_rup.getValue(execution);
	  
		Session session = null;
		CmisObject object = null;
		InputStream stream = null;
		
		try{
		session = CmisUtil.createCmisSession(ALFRESCO_ADMIN_USER, ALFRESCO_ADMIN_PASSWORD, ALFRESCO_CMIS_URL);
		object = session.getObjectByPath(ALFRESCO_TEMPLATE_DETERMINAZIONI_PATH);
		}
		catch(CmisConnectionException ex){
			ExplorerApp.get().getNotificationManager().showErrorNotification(
					"Problema di connessione con il documentale Alfresco", 
			        "Riprova o contatta l'amministratore");
		}catch (CmisObjectNotFoundException e) {
			ExplorerApp.get().getNotificationManager().showErrorNotification(
		            "Template non trovato nel documentale Alfresco", 
		            "Riprova o contatta l'amministratore");
		}
		
		try {
			org.apache.chemistry.opencmis.client.api.Document document = (org.apache.chemistry.opencmis.client.api.Document)object;
			stream = (InputStream) document.getContentStream().getStream(); 
		
			String outputFilePath = env.getProperty("docx4j.save.path");
			File outputFile = new java.io.File(outputFilePath);
	  
			WordprocessingMLPackage template = WordprocessingMLPackage.load(stream);
			MainDocumentPart documentPart = template.getMainDocumentPart();
			
			HashMap<String, String> mappings = new HashMap<String, String>();
			mappings.put("numero_determinazione", numDet.toString());
			mappings.put("data_determinazione", dataDet.toString());
			mappings.put("oggetto_determinazione", oggettoDet);
			mappings.put("nome_rup", rup);
			
			//documentPart.variableReplace(mappings);   from v3.0.0
			
			// unmarshallFromTemplate requires string input
			String xml = XmlUtils.marshaltoString(documentPart.getJaxbElement(), true);
			// Do it...
			Object obj = XmlUtils.unmarshallFromTemplate(xml, mappings);
			// Inject result into docx
			documentPart.setJaxbElement((Document) obj);
			
			template.save(outputFile);
		
			execution.getEngineServices().getTaskService().createAttachment("application/vnd.openxmlformats-officedocument.wordprocessingml.document;docx", null, execution.getProcessInstanceId(), outputFile.getName(), null, new FileInputStream(outputFile), "determinazione");
		}
		catch (IOException ex){
			ExplorerApp.get().getNotificationManager().showErrorNotification(
					"document.save.error",
					"Problema nel salvare il documento generato, riprova o contatta l'amministratore");
		}
		catch (Docx4JException ex){
			ExplorerApp.get().getNotificationManager().showErrorNotification(
					"document.generate.error",
					"Problema nel generare il documento, riprova o contatta l'amministratore");
		}
		finally{
			if (stream != null) {
				stream.close();
			}
		}
	}
	
//	private String getParameter(String key){
//		
//       	return context.getEnvironment().getProperty(key);
//	}
}