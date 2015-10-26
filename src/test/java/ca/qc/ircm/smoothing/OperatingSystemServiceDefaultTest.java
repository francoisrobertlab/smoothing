package ca.qc.ircm.smoothing;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.qc.ircm.smoothing.test.config.TestRunnerLog4J;

/**
 * Tests {@link OperatingSystemServiceDefault}.
 */
@RunWith(TestRunnerLog4J.class)
public class OperatingSystemServiceDefaultTest {
    private OperatingSystemServiceBean operatingSystemServiceBean;

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
