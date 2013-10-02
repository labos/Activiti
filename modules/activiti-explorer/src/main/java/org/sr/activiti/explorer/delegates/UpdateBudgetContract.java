package org.sr.activiti.explorer.delegates;

import org.activiti.engine.budget.ProjectSourceItem;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author Lab Open Source
 */
public class UpdateBudgetContract implements JavaDelegate {
  
  private Expression sourceToUpdate;
  private Expression estimatedAmount;
  private Expression actualAmount;
  public void execute(DelegateExecution execution) {
	  Boolean sourceItemUpdated = false;
//	  ProjectCostItem costItem = (ProjectCostItem) costToUpdate.getValue(execution);
	  ProjectSourceItem sourceItem = (ProjectSourceItem) sourceToUpdate.getValue(execution);
	  Double oldAmount = (Double) estimatedAmount.getValue(execution);
	  Double amount = (Double) actualAmount.getValue(execution);
	 
	  if(sourceItem!=null){
		  Double delta = amount - oldAmount;
		  Double newActual = sourceItem.getActual() - delta;
		  sourceItem.setActual(newActual);
		  execution.getEngineServices().getBudgetService().saveProjectSourceItem(sourceItem);
		  sourceItemUpdated = true;
	  }
//	  Project project = execution.getEngineServices().getBudgetService().createProjectQuery().projectId(sourceItem.getIdProject()).singleResult();
//	  Double newTotal = project.getTotal() - amount;
//	  project.setTotal(newTotal);
//	  execution.getEngineServices().getBudgetService().saveProject(project);
	  execution.setVariable("sourceUpdated",sourceItem);
	  execution.setVariable("sourceItemUpdated",sourceItemUpdated);
  }
  
}