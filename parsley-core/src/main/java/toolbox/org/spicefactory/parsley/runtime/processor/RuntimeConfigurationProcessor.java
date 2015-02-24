package org.spicefactory.parsley.runtime.processor;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import org.spicefactory.parsley.core.bootstrap.ConfigurationProcessor;
import org.spicefactory.parsley.core.registry.Registry;

/**
 * ConfigurationProcessor implementation that adds existing instances or simple class references to the Context.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class RuntimeConfigurationProcessor implements ConfigurationProcessor {

	private final List<InstanceDefinition> instances = new LinkedList<InstanceDefinition>();

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds the specified instance to the Context.
	 * @param instance the instance to add to the Context
	 * @param id the optional id to register the instance with
	 */
	public void addInstance(Object instance, @Nullable String id) {
		instances.add(new InstanceDefinition(instance, id));
	}

	@Override
	public void processConfiguration(Registry registry) {
		// TODO Auto-generated method stub

	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private void processInstances(Registry registry) {

	}

	class InstanceDefinition {

		Object instance;
		String id;

		InstanceDefinition(Object instance, String id) {
			this.instance = instance;
			this.id = id;
		}

	}
}
