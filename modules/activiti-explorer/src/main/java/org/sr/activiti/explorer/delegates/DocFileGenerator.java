package org.sr.activiti.explorer.delegates;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.sr.activiti.explorer.delegates.cmis.CmisUtil;


public class DocFileGenerator implements JavaDelegate {

	private Expression parentFolder;
	private Expression docText;
	private Expression docName;
	private Session session;
	private String parentFolderName;
	private static final String ALFRESCO_CMIS_URL = "http://alfrescotest.consorzio21.it:8080/alfresco/service/cmis";
	private static final String ALFRESCO_ADMIN_PASSWORD = "tubonero.99";

  @Override
	public void execute(DelegateExecution execution) throws Exception {
	  Boolean isArchived = false;
      /*XWPFDocument doc = new XWPFDocument();
      XWPFParagraph paragraph = doc.createParagraph();
      XWPFRun run = paragraph.createRun(); // Il Run è una porzione di testo con le stesse caratteristiche
      run.setText("Questa è una determinazione");
      run.setFontSize(20);
      doc.write(new FileOutputStream(new File("nomeFile" + ".doc")));
      doc.write(new FileOutputStream(new File("nomeFile" + ".docx")));
      */
	  String filename = "text.docx";
	  XWPFDocument document = new XWPFDocument();

	  XWPFParagraph paragrafoUno = document.createParagraph();
	  paragrafoUno.setAlignment(ParagraphAlignment.CENTER);
	  paragrafoUno.setBorderBottom(Borders.SINGLE);
	  paragrafoUno.setBorderTop(Borders.SINGLE);
	  paragrafoUno.setBorderRight(Borders.SINGLE);
	  paragrafoUno.setBorderLeft(Borders.SINGLE);
	  paragrafoUno.setBorderBetween(Borders.SINGLE);

	  XWPFRun paragrafoUnoRunUno = paragrafoUno.createRun();
	  paragrafoUnoRunUno.setBold(true);
	  paragrafoUnoRunUno.setItalic(true);
	  paragrafoUnoRunUno.setText("Hello world! Questo è il paragrafo Uno, Run Uno!");
	  paragrafoUnoRunUno.addBreak();

	  XWPFRun paragrafoUnoRunDue = paragrafoUno.createRun();
	  paragrafoUnoRunDue.setText("Secondo Run!");
	  paragrafoUnoRunDue.setTextPosition(100);

	  XWPFRun paragrafoUnoRunTre = paragrafoUno.createRun();
	  paragrafoUnoRunTre.setStrike(true);
	  paragrafoUnoRunTre.setFontSize(20);
	  paragrafoUnoRunTre.setSubscript(VerticalAlign.SUBSCRIPT);
	  paragrafoUnoRunTre.setText(" Ancora paragrafo Uno...");

	  XWPFParagraph paragrafoDue = document.createParagraph();
	  paragrafoDue.setAlignment(ParagraphAlignment.DISTRIBUTE);
	  paragrafoDue.setIndentationRight(200);

	  XWPFRun paragraphTwoRunOne = paragrafoDue.createRun();
	  paragraphTwoRunOne.setText("Questo è il paragrafo due!");
	  FileOutputStream outStream = null;
	  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	  try {
		  outStream = new FileOutputStream(filename);
		  document.write(outStream);
	
		//IOUtils.copy(document.getDocument().newInputStream(), byteArrayOutputStream)
		document.write(byteArrayOutputStream);
		
		  outStream.close();
		  byteArrayOutputStream.close();

		//inStream.read(content);
		//inStream.close();
		System.out.println(" IL BYTEARRAYPOUTP::" + byteArrayOutputStream.toString() + ":::" + byteArrayOutputStream.size());
		 execution.getEngineServices().getTaskService().createAttachment("application/vnd.openxmlformats-officedocument.wordprocessingml.document;docx", null, execution.getProcessInstanceId(),
				  "docum_generato.doc", "questo è un documento generato",new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
      //new ByteArrayInputStream(content))
		  
	
	  } catch (FileNotFoundException e) {
	  e.printStackTrace();
	  } catch (IOException e) {
	  e.printStackTrace();
	  }
      
  	  Folder archiveFolder;

      ByteArrayOutputStream customerSheetOutputStream = new ByteArrayOutputStream();
      try {
    	  
			// create a new CMIS session
			this.createCmisSession();
			// set parent folder name
			this.parentFolderName = (String)parentFolder.getValue(execution);
			// set a new folder name for current process instance

			archiveFolder = CmisUtil.getFolder(session, this.parentFolderName );
    	  
			
			document.write(customerSheetOutputStream);
              byte[] content = byteArrayOutputStream.toByteArray();
          
				Document aDocument = this.saveDocumentToFolder(
						content,
						archiveFolder.getId(), "documento1" , "_" + "." + "docx",
						"application/vnd.openxmlformats-officedocument.wordprocessingml.document");
              
				if (aDocument == null) {
					isArchived = false;
					}else{
					isArchived = true;
					}
   
      } catch(Exception e) {
          throw new ActivitiException("Error storing document generated by using POI in CMIS repository", e);
      } finally {
          try {
        	 
                      customerSheetOutputStream.close();
                   
          } catch(Exception e) {}
      }

      
      execution.setVariable("isArchived", isArchived);
	}
  
	public Document saveDocumentToFolder(byte[] documentStreamByteArray,
			String folderId, String name, String fileSuffix, String mimeType) {
		try {
			byte[] content = documentStreamByteArray;
			Folder folder = (Folder) session.getObject(folderId);
			return CmisUtil.createDocument(session, folder, name + "_"
					+ fileSuffix, mimeType, content);
		} catch (Exception e) {
			throw new ActivitiException(
					"Error storing document in CMIS repository", e);
		} finally {
			try {
				// documentStreamByteArray.close();
			} catch (Exception e) {
			}
		}
	}
	public void createCmisSession() {
		session = CmisUtil.createCmisSession("admin", ALFRESCO_ADMIN_PASSWORD,
				ALFRESCO_CMIS_URL);
	}


}
