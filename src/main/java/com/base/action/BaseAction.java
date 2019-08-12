package com.base.action;

import com.base.Environment;
import com.base.command.BaseCommand;

public class BaseAction {
	protected Environment env;
	protected BaseCommand baseCommand;
	
	public BaseAction(Environment env) {
		this.env = env;	
		this.baseCommand = env.getBaseCommand();
	}
}
