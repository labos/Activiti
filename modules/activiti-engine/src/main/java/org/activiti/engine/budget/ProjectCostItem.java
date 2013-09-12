package org.activiti.engine.budget;

import java.io.Serializable;

public interface ProjectCostItem extends Serializable{
	String getId();
	void setId(String id);
		
	String getIdProject();
	void setIdProject(String idProject);
	
	String getIdCostEntry();
	void setIdCostEntry(String idCostEntry);
	
	Double getTotal();
	void setTotal(Double total);
	
	Double getActual();
	void setActual(Double actual);

}
