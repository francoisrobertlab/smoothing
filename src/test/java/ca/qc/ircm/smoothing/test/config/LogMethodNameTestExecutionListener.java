package ca.qc.ircm.smoothing.test.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Log test method name before test.
 */
public class LogMethodNameTestExecutionListener extends AbstractTestExecutionListener {
  private static final Logger logger =
      LoggerFactory.getLogger(LogMethodNameTestExecutionListener.class);

  @Override
  public void beforeTestMethod(TestContext testContext) throws Exception {
    logger.trace("Running test {}", testContext.getTestMethod().getName());
  }
}