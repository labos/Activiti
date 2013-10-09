package org.activiti.engine.impl.budget;

import java.util.List;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.budget.Project;
import org.activiti.engine.budget.ProjectQuery;
import org.activiti.engine.impl.AbstractQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;

/**
 * @author Lab Open Source
 */
public class ProjectQueryImpl extends AbstractQuery<ProjectQuery, Project> implements ProjectQuery{

	private static final long serialVersionUID = 1L;
	protected String id;
	protected String name;
		
	public ProjectQueryImpl(){
	}
	
	public ProjectQueryImpl(CommandContext commandContext){
		super(commandContext);
	}

	public ProjectQueryImpl(CommandExecutor commandExecutor){
		super(commandExecutor);
	}
	
	@Override
	public ProjectQuery projectId(String id) {
		if (id == null) {
		      throw new ActivitiIllegalArgumentException("Provided id is null");
		}
		this.id = id;
		return this;
	}
	
	@Override
	public long executeCount(CommandContext commandContext) {
		checkQueryOk();
	    return commandContext.getProjectEntityManager().findProjectCountByQueryCriteria(this);
	}
	@Override
	public List<Project> executeList(CommandContext commandContext, Page page) {
		 checkQueryOk();
		 return commandContext.getProjectEntityManager().findProjectByQueryCriteria(this, page);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
