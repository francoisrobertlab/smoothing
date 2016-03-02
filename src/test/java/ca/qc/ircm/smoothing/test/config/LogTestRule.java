package ca.qc.ircm.smoothing.test.config;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs test method's name.
 */
public class LogTestRule implements TestRule {
  private final Logger logger = LoggerFactory.getLogger(LogTestRule.class);

  @Override
  public Statement apply(final Statement base, final Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        logger.trace("Running test " + description.getMethodName());
        base.evaluate();
      }
    };
  }
}
