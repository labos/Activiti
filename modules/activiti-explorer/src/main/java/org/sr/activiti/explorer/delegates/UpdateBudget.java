package org.sr.activiti.explorer.delegates;

import org.activiti.engine.budget.Project;
import org.activiti.engine.budget.ProjectCostItem;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author Lab Open Source
 */
public class UpdateBudget implements JavaDelegate {
  
  private Expression costToUpdate;
  private Expression amountToDecrease;
  private Expression vatApplied; 
  public void execute(DelegateExecution execution) {
	  Boolean costItemUpdated = false;
	  ProjectCostItem costItem = (ProjectCostItem) costToUpdate.getValue(execution);
	  Long amount = (Long) amountToDecrease.getValue(execution);
	  Boolean vatIsApplied = (Boolean) vatApplied.getValue(execution);
	  Double amountToApply = 0.0;
	  if(costItem!=null){
		  amountToApply = (vatIsApplied)? amount * (1 + 0.21) : amount;
		  Double newActual = costItem.getTotal() - amountToApply;
		  costItem.setActual(newActual);
		  execution.getEngineServices().getBudgetService().saveProjectCostItem(costItem);
		  costItemUpdated = true;
	  }
	  Project project = execution.getEngineServices().getBudgetService().createProjectQuery().projectId(costItem.getIdProject()).singleResult();
	  Double newTotal = project.getTotal() - amountToApply;
	  project.setTotal(newTotal);
	  execution.getEngineServices().getBudgetService().saveProject(project);
	  execution.setVariable("costItemUpdated",costItemUpdated);
  }
  
  
  
}
