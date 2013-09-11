package org.activiti.engine.impl.budget;

import java.util.List;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.budget.CostEntryQuery;
import org.activiti.engine.budget.CostEntry;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.AbstractQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;

public class CostEntryQueryImpl extends AbstractQuery<CostEntryQuery, CostEntry> implements CostEntryQuery{

	private static final long serialVersionUID = 1L;
	protected String id;
	protected String name;
	
	public CostEntryQueryImpl(){
	}
	
	public CostEntryQueryImpl(CommandContext commandContext){
		super(commandContext);
	}

	public CostEntryQueryImpl(CommandExecutor commandExecutor){
		super(commandExecutor);
	}
	
	@Override
	public CostEntryQuery costEntryId(String id) {
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
	      .getCostEntryEntityManager()
	      .findCostEntryCountByQueryCriteria(this);
	}
	@Override
	public List<CostEntry> executeList(CommandContext commandContext, Page page) {
		 checkQueryOk();
		 return commandContext
		   .getCostEntryEntityManager()
		   .findCostEntryByQueryCriteria(this, page);
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
