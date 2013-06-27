package org.activiti.engine.impl.budget;

import java.util.List;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.budget.Program;
import org.activiti.engine.budget.ProgramQuery;
import org.activiti.engine.budget.Source;
import org.activiti.engine.budget.SourceQuery;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.AbstractQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;

public class ProgramQueryImpl extends AbstractQuery<ProgramQuery, Program> implements ProgramQuery{

	private static final long serialVersionUID = 1L;
	protected String id;
	protected String name;
	protected Double total;
	
	public ProgramQueryImpl(){
		
	}
	
	public ProgramQueryImpl(CommandContext commandContext){
		super(commandContext);
	}

	public ProgramQueryImpl(CommandExecutor commandExecutor){
		super(commandExecutor);
	}
	
	@Override
	public ProgramQuery programId(String id) {
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
	      .getProgramEntityManager()
	      .findProgramCountByQueryCriteria(this);
	}
	@Override
	public List<Program> executeList(CommandContext commandContext, Page page) {
		 checkQueryOk();
		 return commandContext
		   .getProgramEntityManager()
		   .findProgramByQueryCriteria(this, page);
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

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
	
}
