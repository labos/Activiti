package org.activiti.engine.attachment;

import org.activiti.engine.query.Query;

/**
 * Allows programmatic querying of AttachmentCategory
 * 
 * @author Lab Open Source
 */
public interface AttachmentCategoryQuery extends Query<AttachmentCategoryQuery, AttachmentCategory> {
  
  /** Only select Projects with the given id/ */
	AttachmentCategoryQuery attachmentCategoryId(String id);
  
}