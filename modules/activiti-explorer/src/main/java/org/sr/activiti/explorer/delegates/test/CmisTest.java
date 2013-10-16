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
				"workspace://SpacesStore/ca1571e7-edee-4ad6-824d-da065c693f60");
		tagsMap.put("contratto",
				"workspace://SpacesStore/ddc47ca2-29ac-41a8-825d-7429192b968c");
		tagsMap.put("letterainvito",
				"workspace://SpacesStore/34dc803d-b51c-46ab-8e5d-279cf1490412");
		tagsMap.put("fattura",
				"workspace://SpacesStore/78312eab-8034-4454-b153-b157ed672ccb");
		tagsMap.put("generico",
				"workspace://SpacesStore/8cdde542-5078-4305-b5a6-76576ba7b6e6");
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
