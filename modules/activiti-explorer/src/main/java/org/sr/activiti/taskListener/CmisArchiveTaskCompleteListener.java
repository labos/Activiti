package org.sr.activiti.taskListener;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.task.Attachment;
import org.activiti.explorer.ExplorerApp;
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
	private Expression documentCategory;
	private Expression minAttachmentsNum;
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
		String documentCategoryName = "documento";
		Folder archiveFolder;

		ArrayList<String> alfrescoPageLinks = new ArrayList<String>();
		attachmentList = delegateTask.getExecution().getEngineServices()
				.getTaskService().getTaskAttachments(delegateTask.getId());

		if (minAttachmentsNum.getValue(delegateTask.getExecution()) != null) {
			numAttachments = Integer.parseInt((String) minAttachmentsNum
					.getValue(delegateTask.getExecution()));
		}

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
				// set parent folder name
				this.parentFolderName = (String) parentFolder
						.getValue(delegateTask.getExecution());
				// set a new folder name for current process instance
				suffixName = (String) suffix.getValue(delegateTask
						.getExecution());

				archiveFolder = CmisUtil.getFolderAndCreate(session,
						this.parentFolderName, "Procedure");

				if (documentCategory != null
						&& documentCategory.getValue(delegateTask
								.getExecution()) != null) {
					documentCategoryName = (String) documentCategory
							.getValue(delegateTask.getExecution());
				}

				// get all attachments
				List<Attachment> attachmentList = delegateTask
						.getExecution()
						.getEngineServices()
						.getTaskService()
						.getProcessInstanceAttachments(
								delegateTask.getExecution()
										.getProcessInstanceId());

				System.out.println("*** cartella parent:"
						+ this.parentFolderName);
				System.out.println("*** suffisso:" + suffixName);

				// store each file in the new folder
				for (Attachment attachment : attachmentList) {
					// set content type
					String[] contentTypeValues = attachment.getType()
							.split(";");

					InputStream aStream = delegateTask.getExecution()
							.getEngineServices().getTaskService()
							.getAttachmentContent(attachment.getId());
					System.out.println("*** tipo di content:"
							+ contentTypeValues[0]);
					Document aDocument = this.saveDocumentToFolder(IoUtil
							.readInputStream(aStream, "stream attachment"),
							archiveFolder.getId(),
							suffixName + indexAttachment.toString(),
							contentTypeValues[1], contentTypeValues[0]);
					indexAttachment++;
					if (aDocument == null) {
						isArchived = false;
						break;
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
