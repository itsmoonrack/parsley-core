package org.spicefactory.parsley.core.events;

import java.awt.AWTEvent;

import javax.annotation.Nullable;

import org.spicefactory.parsley.core.view.ViewConfiguration;
import org.spicefactory.parsley.core.view.ViewConfiguration.CompleteHandler;

/**
 * Event that fires when one or more view components wish to get processed by the nearest Context.
 * <p>
 * The event allows to specify a custom <code>ViewProcessor</code> or <code>ViewLifecycle</code> for each object getting processed. If not
 * specified the default set by the Context will be used. Processing a view can mean adding it to the Context or just injecting something into it
 * without making the view itself a managed component. It is a very generic event that just allows to pass object from the view to the nearest
 * Context for any kind of processing.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class ViewConfigurationEvent extends AWTEvent {

	/*
	 * JDK 1.1 serialVersionUID
	 */
	private static final long serialVersionUID = 5510272757860532034L;

	/**
	 * Constant for the type of bubbling event explicitly fired by a view component that wishes to get processed by the nearest Context in the
	 * view hierarchy.
	 */
	public static final int CONFIGURE_VIEW = 2000;

	/**
	 * Constant for the type of bubbling event automatically fired when a view component is a candidate for getting processed by the nearest
	 * Context in the view hierarchy.
	 * <p>
	 * A candidate is a view component that passed the <code>prefilter</code> method of the active <code>ViewAutowireFilter</code> when
	 * auto-wiring is enabled.
	 */
	public static final int AUTOWIRE_VIEW = 2001;

	private final ViewConfiguration[] configurations;
	private boolean received;

	/**
	 * Creates a new event instance.
	 * @param id the type of the event
	 * @param configurations one or more ViewConfigurations that should get processed
	 * @param callback the no-arg callback to invoke after processing of this event has completed
	 */
	public ViewConfigurationEvent(int id, ViewConfiguration[] configurations, @Nullable CompleteHandler callback) {
		super(callback, id);
		this.configurations = configurations;
	}

	/**
	 * Creates a new event instance to be used for explicitly passing the targets contained in the specified configuration instances to the
	 * nearest Context in the view hierarchy for processing.
	 * @param configurations one or more ViewConfigurations that should get processed
	 * @param callback the no-arg callback to invoke after processing of this event has completed
	 * @return a new event instance
	 */
	public static ViewConfigurationEvent forConfigurations(ViewConfiguration[] configurations, CompleteHandler callback) {
		return new ViewConfigurationEvent(CONFIGURE_VIEW, configurations, callback);
	}

	/**
	 * Creates a new event instance to be used for explicitly passing the targets contained in the specified configuration instances to the
	 * nearest Context in the view hierarchy for processing.
	 * @param configurations one or more ViewConfigurations that should get processed
	 * @return a new event instance
	 */
	public static ViewConfigurationEvent forConfigurations(ViewConfiguration[] configurations) {
		return new ViewConfigurationEvent(CONFIGURE_VIEW, configurations, null);
	}

	/**
	 * The view configurations to get processed.
	 */
	public ViewConfiguration[] configurations() {
		return configurations;
	}

	/**
	 * Indicates whether this event instance has already been processed by a Context.
	 */
	public boolean received() {
		return received;
	}
}
