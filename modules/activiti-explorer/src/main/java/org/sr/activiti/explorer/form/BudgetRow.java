package org.sr.activiti.explorer.form;

import java.io.Serializable;

public class BudgetRow implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String projectName;
	private double projectAmount;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public double getProjectAmount() {
		return projectAmount;
	}
	public void setProjectAmount(double projectAmount) {
		this.projectAmount = projectAmount;
	}

}