package org.activiti.engine.budget;

import java.io.Serializable;

public interface ProjectSourceItem extends Serializable{
	String getId();
	void setId(String id);
		
	String getIdProject();
	void setIdProject(String idProject);
	
	String getIdSource();
	void setIdSource(String idSource);
	
	Double getTotal();
	void setTotal(Double total);
	
	Double getActual();
	void setActual(Double actual);

}
