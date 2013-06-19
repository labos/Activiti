package org.activiti.engine.impl;


import org.activiti.engine.UserService;
import org.activiti.engine.impl.cmd.GetUserEmailByIdCmd;

public class UserServiceImpl  extends ServiceImpl implements UserService {

	@Override
	public String getUserEmail(String userId) {
		 return commandExecutor.execute(new GetUserEmailByIdCmd(userId));
	}

}
