package org.activiti.engine.impl.budget;

import java.util.List;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.budget.AttachmentCategory;
import org.activiti.engine.budget.AttachmentCategoryQuery;
import org.activiti.engine.impl.AbstractQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;

/**
 * @author Lab Open Source
 */
public class AttachmentCategoryQueryImpl extends AbstractQuery<AttachmentCategoryQuery, AttachmentCategory> implements AttachmentCategoryQuery{

	private static final long serialVersionUID = 1L;
	protected String id;
	protected String name;
		
	public AttachmentCategoryQueryImpl(){
	}
	
	public AttachmentCategoryQueryImpl(CommandContext commandContext){
		super(commandContext);
	}

	public AttachmentCategoryQueryImpl(CommandExecutor commandExecutor){
		super(commandExecutor);
	}
	
	@Override
	public AttachmentCategoryQuery attachmentCategoryId(String id) {
		if (id == null) {
		      throw new ActivitiIllegalArgumentException("Provided id is null");
		}
		this.id = id;
		return this;
	}
	
	@Override
	public long executeCount(CommandContext commandContext) {
		checkQueryOk();
	    return commandContext.getAttachmentCategoryEntityManager().findAttachmentCategoryCountByQueryCriteria(this);
	}
	@Override
	public List<AttachmentCategory> executeList(CommandContext commandContext, Page page) {
		 checkQueryOk();
		 return commandContext.getAttachmentCategoryEntityManager().findAttachmentCategoryByQueryCriteria(this, page);
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

