package org.sr.activiti.explorer.delegates.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sr.activiti.explorer.delegates.cmis.CmisUtil;
import org.alfresco.cmis.client.AlfrescoDocument;
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
		Folder folder = CmisUtil.getFolder(session, "Procedure");
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

		Map<String, String> tagsMap = new HashMap<String, String>();
		tagsMap.put("determinazione",
				"workspace://SpacesStore/de905af4-3ae6-415a-ab54-45de7393571b");
		tagsMap.put("contratto",
				"workspace://SpacesStore/cde09f2a-cac7-4082-a878-3534b22c6e50");
		tagsMap.put("letterainvito",
				"workspace://SpacesStore/bb695981-b9e1-4b3d-878c-1f7ae78a24ff");
		tagsMap.put("fattura",
				"workspace://SpacesStore/361b6d8d-b8ce-4c1e-a234-80cbc3e47c26");
		AlfrescoDocument alfDoc = (AlfrescoDocument) document;

		if (!alfDoc.hasAspect("P:cm:taggable")) {
			alfDoc.addAspect("P:cm:taggable");
		}
		assertTrue(alfDoc.hasAspect("P:cm:taggable"));
		if (alfDoc.hasAspect("P:cm:taggable")) {
			System.out.println("It's a taggable document ");
			List<String> tags = new ArrayList<String>();
			tags.add(tagsMap.get("contratto"));

			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("cm:taggable", tags);
			alfDoc.addAspect("P:cm:taggable", properties);
		}

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
