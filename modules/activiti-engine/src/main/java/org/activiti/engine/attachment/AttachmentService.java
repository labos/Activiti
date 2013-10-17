package org.activiti.engine.attachment;



public interface AttachmentService {
	
	AttachmentCategory newAttachmentCategory(String attachmentCategoryId);
	
	void saveAttachmentCategory(AttachmentCategory attachmentCategory);
	
	AttachmentCategoryQuery createAttachmentCategoryQuery();

}
