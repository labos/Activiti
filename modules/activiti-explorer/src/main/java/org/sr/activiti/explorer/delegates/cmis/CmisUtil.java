package org.sr.activiti.explorer.delegates.cmis;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.DocumentType;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.FolderType;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.bindings.spi.atompub.AbstractAtomPubService;
import org.apache.chemistry.opencmis.client.bindings.spi.atompub.AtomPubParser;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.BindingType;


public class CmisUtil {

  public static Session createCmisSession(String user, String password, String url) {
    SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
    Map<String, String> parameter = new HashMap<String, String>();
    parameter.put(SessionParameter.USER, user);
    parameter.put(SessionParameter.PASSWORD, password);
    parameter.put(SessionParameter.ATOMPUB_URL, url);
    parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());


 // Set the alfresco object factory
 parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

    Repository repository = sessionFactory.getRepositories(parameter).get(0);
    return repository.createSession();
  }
 
  public static Folder getFolder(Session session, String folderName) {
    ObjectType type = session.getTypeDefinition("cmis:folder");
    PropertyDefinition<?> objectIdPropDef = type.getPropertyDefinitions().get(PropertyIds.OBJECT_ID);
    String objectIdQueryName = objectIdPropDef.getQueryName();
   
    ItemIterable<QueryResult> results = session.query("SELECT * FROM cmis:folder WHERE cmis:name='" + folderName + "'", false);
    for (QueryResult qResult : results) {
        String objectId = qResult.getPropertyValueByQueryName(objectIdQueryName);
        return (Folder) session.getObject(session.createObjectId(objectId));
    }
    return null;
  }

	public static Folder getFolderAndCreate(Session session,String folderName, String parentFolderName) {
		Folder parentFolder = CmisUtil.getFolder(session, parentFolderName );
		Folder folder = CmisUtil.containsFolderWithName(folderName, parentFolder);
		if (folder == null) {
			folder = createFolder(session, parentFolder, folderName);
		}
		return folder;
	}
  
  public static Folder createFolder(Session session, Folder parentFolder, String folderName) {
    Map<String, Object> folderProps = new HashMap<String, Object>();
    folderProps.put(PropertyIds.NAME, folderName);
    folderProps.put(PropertyIds.OBJECT_TYPE_ID, FolderType.FOLDER_BASETYPE_ID);

    ObjectId folderObjectId = session.createFolder(folderProps, parentFolder, null, null, null);
    return (Folder) session.getObject(folderObjectId);
  }
 
  public static Document createDocument(Session session, Folder folder, String fileName, String mimetype, byte[] content) throws Exception {
    Map<String, Object> docProps = new HashMap<String, Object>();
    docProps.put(PropertyIds.NAME, fileName);
    docProps.put(PropertyIds.OBJECT_TYPE_ID, DocumentType.DOCUMENT_BASETYPE_ID);
   
    ByteArrayInputStream in = new ByteArrayInputStream(content);
    ContentStream contentStream = session.getObjectFactory().createContentStream(fileName, content.length, mimetype, in);
   
    ObjectId documentId = session.createDocument(docProps, session.createObjectId((String) folder.getPropertyValue(PropertyIds.OBJECT_ID)), contentStream, null, null, null, null);
    Document document = (Document) session.getObject(documentId);
    return document;
  }
	public static final String getDocumentURL(final Document document,
			final Session session) {

		String link = null;

		try {

			Method loadLink = AbstractAtomPubService.class.getDeclaredMethod(
					"loadLink",

					new Class[] { String.class, String.class, String.class,
							String.class });

			loadLink.setAccessible(true);

			link = (String) loadLink.invoke(session.getBinding()
					.getObjectService(), session.getRepositoryInfo().getId(),

			document.getId(), AtomPubParser.LINK_REL_CONTENT, null);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return link;

	}
	public static Folder containsFolderWithName(String name, Folder parentFolder) {
		Folder found = null;
		for (CmisObject cmisObject : parentFolder.getChildren()) {
			System.out
					.println("name " + name + " cmis " + cmisObject.getName());
			if (cmisObject.getProperty(PropertyIds.OBJECT_TYPE_ID)
					.getValueAsString().equals(ObjectType.FOLDER_BASETYPE_ID)
					&& name.equals(cmisObject.getName())) {

				found = (Folder) cmisObject;
				break;
			}
		}
		return found;
	}
	
	public static void attachDocumentToProcess(String processInstanceId,
			Document document, String fileSuffix, String fileDescription) {
		ProcessEngine processEngine = ProcessEngines.getProcessEngines().get(
				ProcessEngines.NAME_DEFAULT);
		processEngine.getTaskService().createAttachment(
				fileSuffix,
				null,
				processInstanceId,
				document.getName().substring(0,
						document.getName().lastIndexOf(".")), fileDescription,
				document.getContentStream().getStream());
	}
 
}

