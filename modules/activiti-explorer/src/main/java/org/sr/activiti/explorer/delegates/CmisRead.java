
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

import java.io.FileOutputStream;
import java.io.InputStream;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.explorer.ExplorerApp;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.sr.activiti.explorer.delegates.cmis.CmisUtil;


/**
 * @author LabOpenSource
 */
public class CmisRead implements JavaDelegate {
  
 
  private Expression minAttachmentsNum;
  private static final String ALFRESCO_CMIS_URL =
		  "http://alfrescotest.consorzio21.it:8080/alfresco/service/cmis";

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		retrieveFolder();
		
	}

	  public void retrieveFolder() throws Exception {
			try {
		  Session session = CmisUtil.createCmisSession(
		  "admin", "tubonero.99", ALFRESCO_CMIS_URL);
		  Folder folder = CmisUtil.getFolder(
		  session, "Processi");
		 // assertNotNull(folder);
		 //assertEquals(1, folder.getChildren().getTotalNumItems());
		  CmisObject cmisObject = folder.getChildren()
		  .iterator().next();
		  //assertTrue(cmisObject instanceof Document);
		  Document document = (Document) cmisObject;
		  System.out.println("document name " +
		  document.getName());
		  System.out.println("document type " +
		  document.getType().getDisplayName());
		  System.out.println("created by " +
		  document.getCreatedBy());
		  System.out.println("created date " +
		  document.getCreationDate().getTime());
		  FileOutputStream output = new FileOutputStream(
		  document.getName());
		  InputStream repoDocument = document.getContentStream().getStream();
		  byte[] buffer = new byte[1024];
		  while(repoDocument.read(buffer) != -1) {
		  output.write(buffer);
		  }
		  output.close();
		  repoDocument.close();
			}
			catch(Exception e){
			    ExplorerApp.get().getNotificationManager().showErrorNotification(
			            "Problema lettura con il documentale Alfresco", 
			            "Riprova o contatta l'amministratore");
			}

  }

}
