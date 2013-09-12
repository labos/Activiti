package org.activiti.engine.impl.budget;

import java.util.List;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.budget.ProjectCostItem;
import org.activiti.engine.budget.ProjectCostItemQuery;
import org.activiti.engine.budget.Source;
import org.activiti.engine.budget.SourceQuery;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.AbstractQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;

public class ProjectCostItemQueryImpl extends AbstractQuery<ProjectCostItemQuery, ProjectCostItem> implements ProjectCostItemQuery{

	private static final long serialVersionUID = 1L;
	protected String id;
	protected String idProject;
	protected String idCostEntry;
	protected Double total;
	
	public ProjectCostItemQueryImpl(){
		
	}
	
	public ProjectCostItemQueryImpl(CommandContext commandContext){
		super(commandContext);
	}

	public ProjectCostItemQueryImpl(CommandExecutor commandExecutor){
		super(commandExecutor);
	}
	
	@Override
	public ProjectCostItemQuery projectCostItemId(String id) {
		if (id == null) {
		      throw new ActivitiIllegalArgumentException("Provided id is null");
		}
		this.id = id;
		return this;
	}
	
	
	@Override
	public long executeCount(CommandContext commandContext) {
		checkQueryOk();
	    return commandContext
	      .getProjectCostItemEntityManager()
	      .findProjectCostItemCountByQueryCriteria(this);
	}
	@Override
	public List<ProjectCostItem> executeList(CommandContext commandContext, Page page) {
		 checkQueryOk();
		 return commandContext
		   .getProjectCostItemEntityManager()
		   .findProjectCostItemByQueryCriteria(this, page);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	

	public String getIdProject() {
		return idProject;
	}

	public void setIdProject(String idProject) {
		this.idProject = idProject;
	}

	public String getIdCostEntry() {
		return idCostEntry;
	}

	public void setIdCostEntry(String idCostEntry) {
		this.idCostEntry = idCostEntry;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
	
}
