package org.sr.activiti.explorer.delegates;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.cmd.CreateAttachmentCmd;
import org.activiti.engine.impl.cmd.sr.CopyAttachmentCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.task.Attachment;

public class CopyAttachmentsToParent implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<Attachment> attachmentList = new ArrayList<Attachment>();
		CommandExecutor commandExecutor = Context
				.getProcessEngineConfiguration()
				.getCommandExecutorTxRequiresNew();
		attachmentList = execution
				.getEngineServices()
				.getTaskService()
				.getProcessInstanceAttachments(execution.getProcessInstanceId());
		for (Attachment anAttachment : attachmentList) {
			commandExecutor.execute(new CopyAttachmentCmd(anAttachment,
					execution.getParentId()));
		}

	}

}
