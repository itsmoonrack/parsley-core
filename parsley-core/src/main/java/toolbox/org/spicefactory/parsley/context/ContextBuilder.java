package org.spicefactory.parsley.context;

import org.spicefactory.parsley.core.bootstrap.BootstrapProcessor;
import org.spicefactory.parsley.core.bootstrap.ConfigurationProcessor;
import org.spicefactory.parsley.core.context.Context;

/**
 * A ContextBuilder offers the option to create a new Context programmatically using the convenient configuration DSL.
 * <p>
 * A standard ContextBuilder can be created using the <code>newBuilder</code> method:
 *
 * <pre>
 * <code>
 * ContextBuilder.newBuilder()
 *     .config(XmlConfig.forFile(&quot;logging.xml&quot;))
 *     .config(GuiceConfig.forClass(MyConfig.class))
 *     .build();
 * </code>
 * </pre>
 *
 * If you need to specify more options than just the configuration artifacts, you must enter setup mode first:
 *
 * <pre>
 * <code>
 * Container viewRoot = ...;
 * Context parent = ...;
 * ContextBuilder.newSetup()
 *     .viewRoot(viewRoot)
 *     .parent(parent)
 *     .viewSettings().autoremoveComponents(false)
 *     .newBuilder()
 *         .config(XmlConfig.forFile("logging.xml"))
 *         .config(GuiceConfig.forClass(MyConfig.class))
 *         .build();</code>
 * </pre>
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public final class ContextBuilder {

	private final BootstrapProcessor processor;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	ContextBuilder(BootstrapProcessor processor) {
		this.processor = processor;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new setup instance that allows to specify custom options for the ContextBuilder to be created.
	 * @return a new setup instance that allows to specify custom options for the ContextBuilder to be created
	 */
	public static ContextBuilderSetup newSetup() {
		return new ContextBuilderSetup();
	}

	/**
	 * Creates a new ContextBuilder instance applying default settings.
	 * <p>
	 * In case you want to specify options like view root, custom scopes or parent Contexts, use <code>newSetup</code> instead.
	 * @return a new ContextBuilder instance applying default settings
	 */
	public static ContextBuilder newBuilder() {
		return newSetup().newBuilder();
	}

	/**
	 * Adds a configuration processor to this builder.
	 * <p>
	 * The built-in configuration mechanisms offer convenient shortcuts to create such a processor like
	 * <code>GuiceConfig.forModule(new MyConfig())</code> or <code>XmlConfig.forFile("config.xml")</code>.
	 * </p>
	 * @param processor the processor to add
	 * @return this builder instance for method chaining
	 */
	public ContextBuilder config(ConfigurationProcessor processor) {
		this.processor.addProcessor(processor);
		return this;
	}

	/**
	 * Builds and returns the final Context instance, applying all settings specified for this builder.
	 * @return the final Context instance
	 */
	public Context build() {
		Context context = null;
		try {
			context = processor.process();
		}
		catch (Error e) {
			// TODO: catch it.
		}
		return context;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
