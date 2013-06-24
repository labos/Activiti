
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
package org.activiti.explorer.demo;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.cmd.GetAttachmentContentCmd;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.Messages;
import org.activiti.explorer.demo.cmis.CmisUtil;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.DocumentType;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.FolderType;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.BindingType;



/**
 * @author LabOpenSource
 */
public class CmisArchive implements JavaDelegate {
  
 
  private Expression suffixFolder;
  private Session session;
  private static final String ALFRESCO_CMIS_URL ="http://alfrescotest.consorzio21.it:8080/alfresco/service/cmis";
  private static final String ALFRESCO_ADMIN_PASSWORD = "tubonero.99";

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Boolean isArchived = false;
		String suffix = "";
		Folder archiveFolder;

		  //create a new CMIS session
		  this.createCmisSession();
		  //set a new folder name for current process instance
		  suffix = (String)suffixFolder.getValue(execution);
		  archiveFolder = this.getFolder(suffix);
		  //get all attachments
		  List<Attachment> attachmentList =  execution.getEngineServices().getTaskService().getProcessInstanceAttachments( execution.getProcessInstanceId());
		  //store each file in the new folder
		  for (Attachment attachment : attachmentList) {
			  InputStream aStream = execution.getEngineServices().getTaskService().getAttachmentContent(attachment.getId());
			  Document aDocument = this.saveDocumentToFolder(IoUtil.readInputStream(aStream, "stream attachment"), archiveFolder.getId(), "determinazione", suffix, "application/pdf");
			 if(aDocument == null){
				 isArchived = false;
				 break;
			 }
		}
		  // set a boolean process variable to check for successful archive submission
		  execution.setVariable("isArchived",isArchived);
		
		
	}
	
	  public void createCmisSession() {
		   session = CmisUtil.createCmisSession("admin", ALFRESCO_ADMIN_PASSWORD, ALFRESCO_CMIS_URL);
		  }



		  
		  private Folder getFolder(String folderName) {
			Folder parentFolder = CmisUtil.getFolder(session, "Processi");
		    Folder folder = containsFolderWithName(folderName, parentFolder);
		    if(folder == null) {
		    folder = CmisUtil.createFolder(session, parentFolder, folderName);
		   }
		    return folder;
		  }
		  
		  public Document saveDocumentToFolder(byte[] documentStreamByteArray, String folderId, String name,
				  String fileSuffix, String mimeType) {
			  try {
				  byte[] content = documentStreamByteArray;
			Folder folder = (Folder) session.getObject(folderId);
			return CmisUtil.createDocument(session, folder, name +
		 "-" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + fileSuffix,
		mimeType, content);
		    } catch(Exception e) {
		   throw new ActivitiException("Error storing document in CMIS repository", e);
		    } finally {
		  try {
			  //documentStreamByteArray.close();
		   } catch(Exception e) {}
		    }
		  }
		  
		  public void attachDocumentToProcess(String processInstanceId, Document document, String fileSuffix, String fileDescription) {
		  ProcessEngine processEngine = ProcessEngines.getProcessEngines().get(ProcessEngines.NAME_DEFAULT);
		    processEngine.getTaskService().createAttachment(fileSuffix, null, processInstanceId,
		    document.getName().substring(0, document.getName().lastIndexOf(".")),
		     fileDescription, document.getContentStream().getStream());
		  }
		
 private Folder containsFolderWithName(String name, Folder parentFolder) {
		  Folder found = null;
		  for(CmisObject cmisObject : parentFolder.getChildren()) {
		 System.out.println("name " + name + " cmis " + cmisObject.getName());
		 if(cmisObject.getProperty(PropertyIds.OBJECT_TYPE_ID).getValueAsString().equals(ObjectType.FOLDER_BASETYPE_ID) &&
		 name.equals(cmisObject.getName())) {
		
	  found = (Folder) cmisObject;
		break;
	}
		}
  return found;
		 }

}
