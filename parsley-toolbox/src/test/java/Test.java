import java.util.Arrays;

import org.spicefactory.parsley.messaging.FunctionDispatcher;
import org.spicefactory.parsley.messaging.annotation.MessageDispatcher;

public class Test {

	@MessageDispatcher
	FunctionDispatcher dispatcher;

	public Test() {

		// Framework bindings.
		org.spicefactory.parsley.core.messaging.impl.MessageDispatcher instance =
				new org.spicefactory.parsley.core.messaging.impl.MessageDispatcher(null, null);
		dispatcher = instance::dispatchMessage;

		// Client API.
		dispatcher.dispatchMessage("");
	}

	int compare(Test b) {
		return 0;
	}

	public static void main(String[] args) {
		Test[] items = new Test[1];
		Arrays.sort(items, Test::compare);

		Test test = new Test();
	}
}
