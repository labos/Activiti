package org.sr.activiti.explorer.delegates.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

public class AttachmentsTest extends PluggableActivitiTestCase {

	@Deployment
	public void testAttachmentsById() throws Exception {
		List<Attachment> attachmentList = new ArrayList<Attachment>();
		System.out.println("Attachment: ");
		attachmentList = taskService.getTaskAttachments("332");
		assertEquals(1, attachmentList.size());
		for (Attachment attachment : attachmentList) {
			
			System.out.println("Attachment: "  + attachment.getName());
		}
	}
}

