package org.sr.activiti.explorer.delegates;

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
  private Expression vatApplied; 
  public void execute(DelegateExecution execution) {
	  Boolean sourceUpdated = false;
	  Source source = (Source) sourceToUpdate.getValue(execution);
	  Long amount = (Long) amountToDecrease.getValue(execution);
	  Boolean vatIsApplied = (Boolean) vatApplied.getValue(execution);
	  Double amountToApply;
	  if(source!=null){
		  amountToApply = (vatIsApplied)? amount * (1 + 0.21) : amount;
		  Double newTotal = source.getTotal() - amountToApply;
		  source.setTotal(newTotal);
		  execution.getEngineServices().getBudgetService().saveSource(source);
		  sourceUpdated = true;
	  }
	  execution.setVariable("sourceUpdated",sourceUpdated);
  }
  
}
