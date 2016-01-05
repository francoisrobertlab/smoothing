package ca.qc.ircm.smoothing.test.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Executes multiple rules.
 */
public abstract class MultipleRules implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
	return statement(base, description, getRules());
    }

    protected Statement statement(Statement base, Description description, List<TestRule> rules) {
	List<TestRule> _rules = new ArrayList<>(rules);
	Collections.reverse(_rules);
	Statement ret = base;
	for (TestRule rule : _rules) {
	    ret = rule.apply(ret, description);
	}
	return ret;
    }

    protected List<TestRule> getRules() {
	return new ArrayList<>();
    }
}