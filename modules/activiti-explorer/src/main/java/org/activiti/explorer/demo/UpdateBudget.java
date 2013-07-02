package org.activiti.explorer.demo;

import org.activiti.engine.budget.Source;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.DelegateExecution;

/**
 * @author Lab Open Source
 */
public class UpdateBudget implements JavaDelegate {
  
  private Expression sourceToUpdate;
  private Expression amountToDecrease;
  
  public void execute(DelegateExecution execution) {
	  Boolean sourceUpdated = false;
	  Source source = (Source) sourceToUpdate.getValue(execution);
	  Long amount = (Long) amountToDecrease.getValue(execution);
	  if(source!=null){
		  Double newTotal = source.getTotal() - amount;
		  source.setTotal(newTotal);
		  execution.getEngineServices().getBudgetService().saveSource(source);
		  sourceUpdated = true;
	  }
	  execution.setVariable("sourceUpdated",sourceUpdated);
  }
  
}
