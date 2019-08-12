package com.service.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.base.Environment;
import com.base.action.BaseAction;

import junit.framework.Assert;

public class ActionFactory {
	
	private ActionFactory(Environment env) {
		putAction(RakutenHomeAction.class, new RakutenHomeAction(env));
	}
	
	private final Map<Class<? extends BaseAction>, Object> actions = new HashMap<>();
	
	private <T extends BaseAction> void putAction(Class<T> type, T instance) {
		actions.put(Objects.requireNonNull(type), type.cast(instance));
	}
	
	public <T extends BaseAction> T getAction(Class<T> type) {
		T instance = type.cast(actions.get(type));
		Assert.assertTrue(instance != null);
		return instance;
	}
	
	public static ActionFactory getInstance(Environment env) {
		return new ActionFactory(env);
	}
}
