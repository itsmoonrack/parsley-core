package org.spicefactory.parsley.messaging.model;

import org.spicefactory.lib.event.Event;
import org.spicefactory.lib.event.EventDispatcher;
import org.spicefactory.lib.event.EventListener;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class EventSource extends EventDispatcher<EventListener<Event>, Event> {

}
