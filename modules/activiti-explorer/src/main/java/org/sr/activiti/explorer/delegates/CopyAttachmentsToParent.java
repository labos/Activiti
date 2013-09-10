package org.sr.activiti.explorer.delegates;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.task.Attachment;

public class CopyAttachmentsToParent implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		 List<Attachment> attachmentList = new ArrayList<Attachment>();
		   CommandContext commandContext = Context.getCommandContext();
		   attachmentList = execution.getEngineServices().getTaskService().getProcessInstanceAttachments(execution.getParentId());
		 for(Attachment anAttachment : attachmentList){
			 //update each anAttachment
			   /* AttachmentEntity updateAttachment = commandContext
			    	      .getDbSqlSession()
			    	      .selectById(AttachmentEntity.class, anAttachment.getId());
			    	    
			    	    updateAttachment.setName(anAttachment.getName());
			    	    updateAttachment.setDescription(anAttachment.getDescription());
			    	    */
		 }
		
	}

}
