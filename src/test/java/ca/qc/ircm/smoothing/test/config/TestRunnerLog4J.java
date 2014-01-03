package ca.qc.ircm.smoothing.test.config;

import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Runner that configures Log4J.
 */
public class TestRunnerLog4J extends BlockJUnit4ClassRunner {
	public TestRunnerLog4J(final Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	@Override
	public void run(RunNotifier notifier) {
		Log4JTestInit.init();
		super.run(notifier);
		Log4JTestInit.shutdown();
	}

	@Override
	protected List<TestRule> getTestRules(final Object target) {
		List<TestRule> rules = super.getTestRules(target);
		rules.add(0, new LogTestRule());
		rules.add(0, new MockitoRule(target));
		return rules;
	}
}
