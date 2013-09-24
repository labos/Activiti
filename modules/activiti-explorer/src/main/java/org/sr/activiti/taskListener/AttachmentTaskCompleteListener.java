package org.sr.activiti.taskListener;

import java.util.ArrayList;
import java.util.List;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.Attachment;
import org.activiti.explorer.ExplorerApp;

/**
 * @author LabOpenSource
 */
public class AttachmentTaskCompleteListener implements TaskListener {
	public static final String IS_ATTACHED = "isAttached";
	private static final long serialVersionUID = 1L;
	private Expression minAttachmentsNum;
	List<Attachment> attachmentList = new ArrayList<Attachment>();

	public void notify(DelegateTask delegateTask) {
		Integer numAttachments = 1;
		Boolean isAttached = true;
		if (minAttachmentsNum.getValue(delegateTask.getExecution()) != null) {
			numAttachments = Integer.parseInt((String) minAttachmentsNum
					.getValue(delegateTask.getExecution()));
		}
		attachmentList = delegateTask.getExecution().getEngineServices()
				.getTaskService().getTaskAttachments(delegateTask.getId());

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
		delegateTask.setVariable(IS_ATTACHED, isAttached);
	}

}
