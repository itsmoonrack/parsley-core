package org.spicefactory.parsley.core.view;

import java.util.EventListener;

import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.events.EventDispatcher;

/**
 * Represents an object that controls the life-cycle of one particular view instance.
 * <p>
 * The primary purpose of this life-cycle instance is to invoke the <code>init</code> and <code>destroy</code> methods of the associated
 * <code>ViewProcessor</code> based on the life-cycle of the view.
 * <p>
 * Parsley comes with two default implementations. One controls the life-cycle of the view based on stage events (<code>addedToStage</code> and
 * <code>removedFromStage</code>) the other based on custom events (<code>configureView</code> and <code>removeView</code>) dispatched by the
 * view instance. The <code>autoremoveComponents</code> and <code>autoremoveViewRoots</code> attributes of the <code>ViewSettings</code>
 * annotation (or the <code>ViewSettings</code> class in the ContextBuilder API) control which of the two built-in life-cycles will be used.
 * </p>
 * <p>
 * A custom life-cycle can be specified with the <code>ViewLifecycle</code> annotation inside the <code>ContextBuilder</code> annotation.
 * </p>
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public interface ViewLifecycle extends EventDispatcher<EventListener> {

	/**
	 * Starts controlling the life-cycle of the view instance contained in the specified configuration instance. The primary purpose of this
	 * life-cycle instance is to invoke the <code>init</code> and <code>destroy</code> methods of the associated <code>ViewProcessor</code> based
	 * on the life-cycle of the view.
	 * @param config the view configuration that holds the view this life-cycle instance should control
	 * @param context the Context associated with this view
	 */
	void start(ViewConfiguration config, Context context);

	/**
	 * Stops controlling the life-cycle of the view instance. After this method has been called this life-cycle instance should no longer invoke
	 * the <code>init</code> and <code>destroy</code> methods of the associated <code>ViewProcessor</code> and free all resources it is holding.
	 */
	void stop();
}
