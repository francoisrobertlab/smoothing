package ca.qc.ircm.smoothing;

import ca.qc.ircm.smoothing.test.config.Rules;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

/**
 * Tests {@link OperatingSystemServiceDefault}.
 */
public class OperatingSystemServiceDefaultTest {
  private OperatingSystemServiceBean operatingSystemServiceBean;
  @Rule
  public RuleChain rules = Rules.defaultRules(this);

  @Before
  public void beforeTest() {
    operatingSystemServiceBean = new OperatingSystemServiceBean();
  }

  @Test
  @Ignore("Cannot test since it would depend on the operating system")
  public void currentOS() throws Throwable {
    // Cannot test since it would depend on the operating system.
  }

  @Test
  @Ignore("Cannot test since it would depend on the operating system")
  public void is64bit() {
    // Cannot test since it would depend on the operating system.
  }
}
