package org.activiti.engine.impl.attachment;

import org.activiti.engine.attachment.AttachmentCategory;
import org.activiti.engine.attachment.AttachmentCategoryQuery;
import org.activiti.engine.attachment.AttachmentService;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.cmd.attachment.attachmentCategory.CreateAttachmentCategoryCmd;
import org.activiti.engine.impl.cmd.attachment.attachmentCategory.CreateAttachmentCategoryQueryCmd;
import org.activiti.engine.impl.cmd.attachment.attachmentCategory.SaveAttachmentCategoryCmd;
import org.activiti.engine.impl.persistence.entity.attachment.AttachmentCategoryEntity;

public class AttachmentServiceImpl extends ServiceImpl implements AttachmentService {

	public AttachmentCategory newAttachmentCategory(String attachmentCategoryId) {
		return commandExecutor.execute(new CreateAttachmentCategoryCmd(attachmentCategoryId));
	}

	public void saveAttachmentCategory(AttachmentCategory attachmentCategory) {
		commandExecutor.execute(new SaveAttachmentCategoryCmd((AttachmentCategoryEntity) attachmentCategory));

	}

	public AttachmentCategoryQuery createAttachmentCategoryQuery() {
		return commandExecutor.execute(new CreateAttachmentCategoryQueryCmd());
	}

}
