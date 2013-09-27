package org.sr.activiti.explorer.delegates;

import org.activiti.engine.budget.ProjectSourceItem;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author Lab Open Source
 */
public class UpdateBudget implements JavaDelegate {
  
  private Expression sourceToUpdate;
  private Expression amountToDecrease;
  private Expression actualVAT;
  //private Expression vatApplied; 
  public void execute(DelegateExecution execution) {
	  Boolean sourceItemUpdated = false;
//	  ProjectCostItem costItem = (ProjectCostItem) costToUpdate.getValue(execution);
	  ProjectSourceItem sourceItem = (ProjectSourceItem) sourceToUpdate.getValue(execution);
	  Long amount = (Long) amountToDecrease.getValue(execution);
	  Long vat = (Long) actualVAT.getValue(execution);
//    Boolean vatIsApplied = (Boolean) vatApplied.getValue(execution);
	  Double amountToApply = 0.0;
	  if(sourceItem!=null){
		  amountToApply = (double) (amount * (1 + vat/100));
		  Double newActual = sourceItem.getTotal() - amountToApply;
		  sourceItem.setActual(newActual);
		  execution.getEngineServices().getBudgetService().saveProjectSourceItem(sourceItem);
		  sourceItemUpdated = true;
	  }
//	  Project project = execution.getEngineServices().getBudgetService().createProjectQuery().projectId(sourceItem.getIdProject()).singleResult();
//	  Double newTotal = project.getTotal() - amount;
//	  project.setTotal(newTotal);
//	  execution.getEngineServices().getBudgetService().saveProject(project);
	  execution.setVariable("sourceItemUpdated",sourceItemUpdated);
  }
  
}