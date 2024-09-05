package ca.qc.ircm.smoothing;

import ca.qc.ircm.smoothing.test.config.ServiceTestAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link OperatingSystemService}.
 */
@ServiceTestAnnotations
public class OperatingSystemServiceDefaultTest {
  @SuppressWarnings("unused")
  private OperatingSystemService operatingSystemServiceBean;

  @BeforeEach
  public void beforeTest() {
    operatingSystemServiceBean = new OperatingSystemService();
  }

  @Test
  @Disabled("Cannot test since it would depend on the operating system")
  public void currentOs() throws Throwable {
    // Cannot test since it would depend on the operating system.
  }

  @Test
  @Disabled("Cannot test since it would depend on the operating system")
  public void is64bit() {
    // Cannot test since it would depend on the operating system.
  }
}
