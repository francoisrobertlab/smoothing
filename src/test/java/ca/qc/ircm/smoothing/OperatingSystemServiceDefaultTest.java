/*
 * Copyright (c) 2015 Institut de recherches cliniques de Montreal (IRCM)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
