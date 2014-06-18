import org.spicefactory.parsley.messaging.FunctionDispatcher;
import org.spicefactory.parsley.messaging.annotation.MessageDispatcher;

public class Test {

	@MessageDispatcher
	FunctionDispatcher dispatcher;

	public Test() {

		// Framework bindings.
		dispatcher = new org.spicefactory.parsley.messaging.MessageDispatcher(null, null);

		// Client API.
		dispatcher.dispatchMessage("");
	}

	int compare(Test b) {
		return 0;
	}
}
