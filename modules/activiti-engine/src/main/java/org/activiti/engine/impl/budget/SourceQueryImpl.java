package org.activiti.engine.impl.budget;

import java.util.List;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.budget.Source;
import org.activiti.engine.budget.SourceQuery;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.AbstractQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;

public class SourceQueryImpl extends AbstractQuery<SourceQuery, Source> implements SourceQuery{

	private static final long serialVersionUID = 1L;
	protected String id;
	protected String name;
	
	public SourceQueryImpl(){
		
	}
	
	public SourceQueryImpl(CommandContext commandContext){
		super(commandContext);
	}

	public SourceQueryImpl(CommandExecutor commandExecutor){
		super(commandExecutor);
	}
	
	@Override
	public SourceQuery sourceId(String id) {
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
	      .getSourceEntityManager()
	      .findSourceCountByQueryCriteria(this);
	}
	@Override
	public List<Source> executeList(CommandContext commandContext, Page page) {
		 checkQueryOk();
		 return commandContext
		   .getSourceEntityManager()
		   .findSourceByQueryCriteria(this, page);
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
