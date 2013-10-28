package org.sr.activiti.explorer.delegates.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sr.activiti.explorer.delegates.cmis.CmisUtil;
import org.activiti.engine.impl.util.IoUtil;
import org.alfresco.cmis.client.AlfrescoDocument;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.junit.Test;

public class CmisTestWrite {

	private static final String ALFRESCO_CMIS_URL = "http://alfrescotest.consorzio21.it:8080/alfresco/service/cmis";

	@Test
	public void writeFile() throws Exception {
		Session session = CmisUtil.createCmisSession("admin", "tubonero.99",
				ALFRESCO_CMIS_URL);
		Folder folder = CmisUtil.getFolder(session, "Procedure");
		assertNotNull(folder);
		InputStream aStream = new ByteArrayInputStream("blablabla".getBytes());
		
		
		Document aDocument =	CmisUtil.createDocument(session, folder, "mionome" , null, IoUtil
				.readInputStream(aStream, "stream attachment"));
	
		assertNotNull(aDocument);


	}

}
