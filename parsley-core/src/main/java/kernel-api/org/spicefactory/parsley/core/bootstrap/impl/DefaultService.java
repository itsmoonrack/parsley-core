package org.spicefactory.parsley.core.bootstrap.impl;

import java.lang.reflect.Constructor;

import org.spicefactory.parsley.core.bootstrap.Service;

public class DefaultService<T> implements Service<T> {

	private Constructor<? extends T> constructor;

	@Override
	public void setImplementation(Class<? extends T> type, Class<?>... parameterTypes) {
		try {
			constructor = type.getConstructor(parameterTypes);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public T newInstance(Object... parameters) {
		try {
			return constructor.newInstance(parameters);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
