package org.spicefactory.parsley;

import java.util.Arrays;

import org.spicefactory.parsley.guice.processor.GuiceConfigurationProcessor;

import com.google.inject.CreationException;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * The entry point to the Parsley Guice integration framework. Creates {@link Injector}s from {@link Module}s.
 * <p>
 * Guice supports a model of development that draws clear boundaries between APIs, Implementations of these APIs, Modules which configure these
 * implementations, and finally Applications which consist of a collection of Modules. It is the Application, which typically defines your
 * {@code main()} method, that bootstraps the Guice Injector using the {@code Guice} class, as in this example:
 * 
 * <pre>
 * public class FooApplication {
 * 	public static void main(String[] args) {
 *         Injector injector = Guice.createInjector(
 *             new ModuleA(),
 *             new ModuleB(),
 *             . . .
 *             new FooApplicationFlagsModule(args)
 *         );
 * 
 *         // Now just bootstrap the application and you're done
 *         FooStarter starter = injector.getInstance(FooStarter.class);
 *         starter.runApplication();
 *       }
 * }
 * </pre>
 */
public final class GuiceParsley {

	/**
	 * Creates an injector for the given set of modules.
	 * @throws CreationException if one or more errors occur during injector construction
	 */
	public static Injector createInjector(Module... modules) {
		return createInjector(Arrays.asList(modules));
	}

	/**
	 * Creates an injector for the given set of modules.
	 * @throws CreationException if one or more errors occur during injector creation
	 */
	public static Injector createInjector(Iterable<? extends Module> modules) {
		return createInjector(Stage.DEVELOPMENT, modules);
	}

	/**
	 * Creates an injector for the given set of modules, in a given development stage.
	 * @throws CreationException if one or more errors occur during injector creation.
	 */
	public static Injector createInjector(Stage stage, Module... modules) {
		return createInjector(stage, Arrays.asList(modules));
	}

	/**
	 * Creates an injector for the given set of modules, in a given development stage.
	 * @throws CreationException if one or more errors occur during injector construction
	 */
	public static Injector createInjector(Stage stage, Iterable<? extends Module> modules) {
		GuiceConfigurationProcessor processor = new GuiceConfigurationProcessor(stage, modules);
		processor.processConfiguration(null /* new DefaultContext(); */);
		return processor.getInjector();
	}

}
