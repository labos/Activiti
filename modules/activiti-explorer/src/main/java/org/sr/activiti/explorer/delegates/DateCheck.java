package org.sr.activiti.explorer.delegates;

import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang.time.DateUtils;

/**
 * @author Lab Open Source
 */
public class DateCheck implements JavaDelegate {
  
  private Expression scadenzaDURC;
  
  public void execute(DelegateExecution execution) {
	  Boolean validDURC = false;
	  Date deadLine = (Date) scadenzaDURC.getValue(execution);
	  Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
	  
	  if(deadLine.after(today)){
		  validDURC = true;
	  }
  }
}