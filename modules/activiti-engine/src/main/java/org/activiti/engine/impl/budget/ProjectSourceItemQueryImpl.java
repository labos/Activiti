package org.activiti.engine.impl.budget;

import java.util.List;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.budget.ProjectSourceItem;
import org.activiti.engine.budget.ProjectSourceItemQuery;
import org.activiti.engine.impl.AbstractQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;

public class ProjectSourceItemQueryImpl extends AbstractQuery<ProjectSourceItemQuery, ProjectSourceItem> implements ProjectSourceItemQuery{

	private static final long serialVersionUID = 1L;
	protected String id;
	protected String idProject;
	protected String idSource;
	protected Double total;
	protected Double actual;
	
	public ProjectSourceItemQueryImpl(){
		
	}
	
	public ProjectSourceItemQueryImpl(CommandContext commandContext){
		super(commandContext);
	}

	public ProjectSourceItemQueryImpl(CommandExecutor commandExecutor){
		super(commandExecutor);
	}
	
	@Override
	public ProjectSourceItemQuery projectSourceItemId(String id) {
		if (id == null) {
		      throw new ActivitiIllegalArgumentException("Provided id is null");
		}
		this.id = id;
		return this;
	}	
	

	@Override
	public ProjectSourceItemQuery idProject(String idProject) {
		if (idProject == null) {
		      throw new ActivitiIllegalArgumentException("Provided idProject is null");
		}
		this.idProject = idProject;
		return this;
	}
	
	@Override
	public ProjectSourceItemQuery idSource(String idSource) {
		if (idSource == null) {
		      throw new ActivitiIllegalArgumentException("Provided idSource is null");
		}
		this.idSource = idSource;
		return this;
	}

	@Override
	public long executeCount(CommandContext commandContext) {
		checkQueryOk();
	    return commandContext
	      .getProjectSourceItemEntityManager()
	      .findProjectSourceItemCountByQueryCriteria(this);
	}
	@Override
	public List<ProjectSourceItem> executeList(CommandContext commandContext, Page page) {
		 checkQueryOk();
		 return commandContext
		   .getProjectSourceItemEntityManager()
		   .findProjectSourceItemByQueryCriteria(this, page);
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

	
	public String getIdSource() {
		return idSource;
	}

	public void setIdSource(String idSource) {
		this.idSource = idSource;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getActual() {
		return actual;
	}

	public void setActual(Double actual) {
		this.actual = actual;
	}	
	
}
