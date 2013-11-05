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

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.sr.activiti.bean.ApplicationConf;
import org.sr.activiti.explorer.delegates.cmis.CmisUtil;
import org.activiti.engine.impl.util.IoUtil;
import org.alfresco.cmis.client.AlfrescoDocument;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.junit.Test;

public class CmisTestWrite {
	private AnnotationConfigApplicationContext context =
    	     new AnnotationConfigApplicationContext(ApplicationConf.class);
	@Test
	public void writeFile() throws Exception {
		Session session = CmisUtil.createCmisSession(getParameter("alfresco.user"), getParameter("alfresco.password"),
				getParameter("alfresco.cmis.url"));
		Folder folder = CmisUtil.getFolder(session, "Procedure");
		assertNotNull(folder);
		InputStream aStream = new ByteArrayInputStream("blablabla".getBytes());
		
		
		Document aDocument =	CmisUtil.createDocument(session, folder, "mi5onome" , "octet/stream", IoUtil
				.readInputStream(aStream, "stream attachment"));
	
		assertNotNull(aDocument);


	}
	private String getParameter(String key){
		
       	return context.getEnvironment().getProperty(key);
	}

}
