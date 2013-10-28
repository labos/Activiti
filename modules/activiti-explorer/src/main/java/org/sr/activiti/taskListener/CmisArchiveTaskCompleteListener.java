package org.sr.activiti.taskListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.task.Attachment;
import org.activiti.explorer.ExplorerApp;
import org.alfresco.cmis.client.AlfrescoDocument;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.commons.lang.StringUtils;
import org.sr.activiti.explorer.delegates.cmis.CmisUtil;

/**
 * @author LabOpenSource
 */
public class CmisArchiveTaskCompleteListener implements TaskListener {
	private Expression parentFolder;
	private Expression suffix;
	private Expression minAttachmentsNum;
	private Expression documentCategory;
	private Session session;
	private String parentFolderName;
	private static final long serialVersionUID = 1L;
	public static final String IS_ARCHIVED = "isArchived";
	public static final String IS_ATTACHED = "isAttached";
	public static final String ALFRESCO_PAGE_LINKS = "alfrescoPageLinks";
	private static final String ALFRESCO_CMIS_URL = "http://alfrescotest.consorzio21.it:8080/alfresco/service/cmis";
	private static final String ALFRESCO_ADMIN_PASSWORD = "tubonero.99";
	List<Attachment> attachmentList = new ArrayList<Attachment>();

	public void notify(DelegateTask delegateTask) {
		Boolean isAttached = true;
		Boolean isArchived = false;
		Integer numAttachments = 1;
		Integer indexAttachment = 1;
		String suffixName = "";
		Folder archiveFolder;
		// set a map mimetype-activiti->mimetype-alfresco to match mime-types used in alfresco
		Map<String, String> mimeTypesMap = new HashMap<String, String>();
		// set a map key->tagName to tag document in Alfresco
		Map<String, String> tagsMap = new HashMap<String, String>();
		mimeTypesMap.put("application/vnd.ms-word", "application/msword");
		mimeTypesMap.put("application/x-zip-compressed", "application/zip");
		tagsMap.put("determinazione",
				"workspace://SpacesStore/ca1571e7-edee-4ad6-824d-da065c693f60");
		tagsMap.put("contratto",
				"workspace://SpacesStore/ddc47ca2-29ac-41a8-825d-7429192b968c");
		tagsMap.put("letterainvito",
				"workspace://SpacesStore/34dc803d-b51c-46ab-8e5d-279cf1490412");
		tagsMap.put("fattura",
				"workspace://SpacesStore/78312eab-8034-4454-b153-b157ed672ccb");
		tagsMap.put("generico",
				"workspace://SpacesStore/8cdde542-5078-4305-b5a6-76576ba7b6e6");
		
		
		ArrayList<String> alfrescoPageLinks = new ArrayList<String>();
		attachmentList = delegateTask.getExecution().getEngineServices()
				.getTaskService().getTaskAttachments(delegateTask.getId());
		// set minAttachments to archive
		if (minAttachmentsNum.getValue(delegateTask.getExecution()) != null) {
			numAttachments = Integer.parseInt((String) minAttachmentsNum
					.getValue(delegateTask.getExecution()));
		}
		
		// check required attachments
		if (attachmentList.size() < numAttachments.intValue()) {
			isAttached = false;
			Integer numRequiredAttachments = numAttachments.intValue()
					- attachmentList.size();
			ExplorerApp
					.get()
					.getNotificationManager()
					.showErrorNotification(
							"attachment.required",
							"Devi Allegare "
									+ numRequiredAttachments.toString()
									+ " documento/i  in questo task, di "
									+ numAttachments.toString()
									+ " richiesti. ");

		}

		try {
			if (isAttached) {

				// create a new CMIS session
				this.createCmisSession();
				// set parent folder name (in this case processInstanceId of the root process)
				this.parentFolderName = (String) parentFolder
						.getValue(delegateTask.getExecution());
				// set a new folder name for current process instance
				suffixName = (String) suffix.getValue(delegateTask
						.getExecution());
				// get root folder for procedure
				archiveFolder = CmisUtil.getFolderAndCreate(session,
						this.parentFolderName, "Procedure");

				// get all attachments
				List<Attachment> attachmentList = delegateTask
						.getExecution()
						.getEngineServices()
						.getTaskService()
						.getProcessInstanceAttachments(
								delegateTask.getExecution()
										.getProcessInstanceId());

				System.out.println("*** parent folder:"
						+ this.parentFolderName);
				System.out.println("*** using suffix:" + suffixName);

				// store each file in the new parent folder
				for (Attachment attachment : attachmentList) {
					// set content type
					String[] contentTypeValues = attachment.getType()
							.split(";");
					String mimeType = mimeTypesMap.containsKey(contentTypeValues[0])?  mimeTypesMap.get(contentTypeValues[0]) :contentTypeValues[0]; 

					InputStream aStream = delegateTask.getExecution()
							.getEngineServices().getTaskService()
							.getAttachmentContent(attachment.getId());
					
					System.out.println("*** type of content in attachement:"
							+ contentTypeValues[0]);
					Document aDocument = this.saveDocumentToFolder(IoUtil
							.readInputStream(aStream, "stream attachment"),
							archiveFolder.getId(),
							suffixName + "_" +indexAttachment.toString(),
							contentTypeValues[1], mimeType);
					indexAttachment++;
					if (aDocument == null) {
						isArchived = false;
						break;
					}
					
					// set tag to saved document
					AlfrescoDocument alfDocument = (AlfrescoDocument) aDocument;
					// check for taggable aspect in document
					if (!alfDocument.hasAspect("P:cm:taggable")) {
						alfDocument.addAspect("P:cm:taggable");
					}

					if (alfDocument.hasAspect("P:cm:taggable")) {
						System.out.println("It's a taggable document ");
						List<String> tags = new ArrayList<String>();
						tags.add(tagsMap.get(attachment.getCategoryId()));

						Map<String, Object> properties = new HashMap<String, Object>();
						properties.put("cm:taggable", tags);
						alfDocument.addAspect("P:cm:taggable", properties);
					}

					isArchived = true;
					alfrescoPageLinks.add(CmisUtil.getDocumentURL(aDocument,
							session));
					System.out.println("link alfresco:"
							+ CmisUtil.getDocumentURL(aDocument, session));

				}
			}

		} catch (Exception e) {

			System.out.println("*** Problema accesso Alfresco: "
					+ e.getMessage());

		}

		// set process variables to check for attachments and successful archive
		// submission
		delegateTask.setVariable(IS_ATTACHED, isAttached);
		delegateTask.setVariable(IS_ARCHIVED, isArchived);
		delegateTask.setVariable(ALFRESCO_PAGE_LINKS,
				StringUtils.join(alfrescoPageLinks, "###"));
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
			return CmisUtil.createDocument(session, folder, name + "."
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
