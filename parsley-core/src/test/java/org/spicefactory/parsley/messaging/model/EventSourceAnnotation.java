package org.spicefactory.parsley.messaging.model;

import org.spicefactory.parsley.messaging.annotation.ManagedEvents;
import org.spicefactory.parsley.messaging.messages.TestEvent;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
@ManagedEvents({TestEvent.TEST1, TestEvent.TEST2, 0x00})
public class EventSourceAnnotation extends EventSource {

}
