package org.sr.activiti.explorer.delegates;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

public class CreateUrlFolderAlfresco implements JavaDelegate {

	  private Expression urlName;
	  private String urlNameString = "ARCHIVIO";
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		if (urlName != null
				&& urlName.getValue(execution) != null) {
			urlNameString = (String) urlName
					.getValue(execution);
		}
		 execution.getEngineServices().getTaskService().createAttachment("url", null, execution.getProcessInstanceId(),
				 urlNameString, "ARCHIVIO DELLA DOCUMENTAZIONE DELLA PROCEDURA",""
				 		+ "http://alfrescotest.consorzio21.it:8080/share/page/site/sardegna-ricerche/documentlibrary#filter=path|%2FProcedure%2F" + execution.getProcessInstanceId() + "|&page=1");
   

	}

}