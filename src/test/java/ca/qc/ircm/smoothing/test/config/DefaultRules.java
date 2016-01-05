package ca.qc.ircm.smoothing.test.config;

import java.util.ArrayList;
import java.util.List;

import org.junit.rules.TestRule;

/**
 * Default test rules.
 */
public class DefaultRules extends MultipleRules {
    private final Object target;

    public DefaultRules(Object target) {
	this.target = target;
    }

    @Override
    protected List<TestRule> getRules() {
	List<TestRule> rules = new ArrayList<>();
	rules.add(new LogTestRule());
	rules.add(new MockitoRule(target));
	return rules;
    }
}