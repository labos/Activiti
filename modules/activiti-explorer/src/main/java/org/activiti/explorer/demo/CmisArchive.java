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

import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.task.Attachment;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.demo.cmis.CmisUtil;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.bindings.spi.atompub.AbstractAtomPubService;
import org.apache.chemistry.opencmis.client.bindings.spi.atompub.AtomPubParser;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.commons.lang.StringUtils;

/**
 * @author LabOpenSource
 */
public class CmisArchive implements JavaDelegate {
	
	private Expression parentFolder;
	private Expression suffixId;
	private Expression suffixDate;	
	private Session session;
	private String parentFolderName;
	private static final String ALFRESCO_CMIS_URL = "http://alfrescotest.consorzio21.it:8080/alfresco/service/cmis";
	private static final String ALFRESCO_ADMIN_PASSWORD = "tubonero.99";

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Boolean isArchived = false;
		Integer indexAttachment = 1;
		String suffixIdString = "";
		String suffixDateString = "";
		Folder archiveFolder;
		ArrayList<String> alfrescoPageLinks = new ArrayList<String>();


		try {
			// create a new CMIS session
			this.createCmisSession();
			// set parent folder name
			this.parentFolderName = (String)parentFolder.getValue(execution);
			// set a new folder name for current process instance
			suffixIdString = Long.toString((Long) suffixId.getValue(execution));
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
			
			suffixDateString =  dt.format((Date) suffixDate.getValue(execution));
			archiveFolder = CmisUtil.getFolder(session, this.parentFolderName );
			
			// get all attachments
			List<Attachment> attachmentList = execution
					.getEngineServices()
					.getTaskService()
					.getProcessInstanceAttachments(execution.getProcessInstanceId());
			
			System.out.println("*** cartella parent:" + this.parentFolderName);
			System.out.println("*** suffisso:" + suffixIdString + "_" + suffixDateString);
			
			
			// store each file in the new folder
			for (Attachment attachment : attachmentList) {
				// set content type
				String[] contentTypeValues =  attachment.getType().split(";");
				
				InputStream aStream = execution.getEngineServices()
						.getTaskService().getAttachmentContent(attachment.getId());
				System.out.println("*** tipo di content:" + contentTypeValues[0]);
				Document aDocument = this.saveDocumentToFolder(
						IoUtil.readInputStream(aStream, "stream attachment"),
						archiveFolder.getId(), "determinazioneDG" + indexAttachment.toString(), suffixIdString + "_" + suffixDateString + "." + contentTypeValues[1],
						contentTypeValues[0]);
				indexAttachment++;
				if (aDocument == null) {
					isArchived = false;
					break;
				}
				isArchived = true;
				alfrescoPageLinks.add(CmisUtil.getDocumentURL(aDocument, session));
				System.out.println("link alfresco:" + CmisUtil.getDocumentURL(aDocument, session));
			}
			
		} catch (Exception e) {
			
		
			System.out.println("*** Problema accesso Alfresco: " + e.getMessage());
			
		}
		

		// set a boolean process variable to check for successful archive
		// submission
		execution.setVariable("isArchived", isArchived);
		execution.setVariable("alfrescoPageLinks", StringUtils.join(alfrescoPageLinks, "###"));

	}



	public void createCmisSession() {
		session = CmisUtil.createCmisSession("admin", ALFRESCO_ADMIN_PASSWORD,
				ALFRESCO_CMIS_URL);
	}



	public Document saveDocumentToFolder(byte[] documentStreamByteArray,
			String folderId, String name, String fileSuffix, String mimeType) {
		try {
			byte[] content = documentStreamByteArray;
			Folder folder = (Folder) session.getObject(folderId);
			return CmisUtil.createDocument(session, folder, name + "_"
					+ fileSuffix, mimeType, content);
		} catch (Exception e) {
			throw new ActivitiException(
					"Error storing document in CMIS repository", e);
		} finally {
			try {
				// documentStreamByteArray.close();
			} catch (Exception e) {
			}
		}
	}





}
