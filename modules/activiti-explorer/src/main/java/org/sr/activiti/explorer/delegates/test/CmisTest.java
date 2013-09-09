package org.sr.activiti.explorer.delegates.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.sr.activiti.explorer.delegates.cmis.CmisUtil;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;

import org.junit.Test;

public class CmisTest {

	private static final String ALFRESCO_CMIS_URL = "http://alfrescotest.consorzio21.it:8080/alfresco/service/cmis";

	@Test
	public void retrieveFolder() throws Exception {
		Session session = CmisUtil.createCmisSession("admin", "tubonero.99",
				ALFRESCO_CMIS_URL);
		Folder folder = CmisUtil.getFolder(session, "myExpenses");
		assertNotNull(folder);
		assertEquals(1, folder.getChildren().getTotalNumItems());
		CmisObject cmisObject = folder.getChildren().iterator().next();
		assertTrue(cmisObject instanceof Document);
		Document document = (Document) cmisObject;
		System.out.println("document name " + document.getName());
		System.out.println("document type "
				+ document.getType().getDisplayName());
		System.out.println("created by " + document.getCreatedBy());
		System.out.println("created date "
				+ document.getCreationDate().getTime());
		FileOutputStream output = new FileOutputStream(document.getName());
		InputStream repoDocument = document.getContentStream().getStream();
		byte[] buffer = new byte[1024];
		while (repoDocument.read(buffer) != -1) {
			output.write(buffer);
		}
		output.close();
		repoDocument.close();
	}

}
