package org.activiti.engine.budget;

import java.io.Serializable;

public interface Project extends Serializable{
	String getId();
	void setId(String Id);
		
	String getName();
	void setName(String name);	
	
	Double getTotal();
	void setTotal(Double total);
	
	Double getActual();
	void setActual(Double actual);

}
