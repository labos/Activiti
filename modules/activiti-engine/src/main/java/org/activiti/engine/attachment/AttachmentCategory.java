package org.activiti.engine.attachment;

import java.io.Serializable;

public interface AttachmentCategory extends Serializable {
	String getId();
	void setId(String Id);
		
	String getName();
	void setName(String name);	

}