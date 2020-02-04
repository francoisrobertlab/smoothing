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

import static ca.qc.ircm.smoothing.OperatingSystem.LINUX;
import static ca.qc.ircm.smoothing.OperatingSystem.MAC;
import static ca.qc.ircm.smoothing.OperatingSystem.OTHER;
import static ca.qc.ircm.smoothing.OperatingSystem.UNIX;
import static ca.qc.ircm.smoothing.OperatingSystem.WINDOWS;
import static ca.qc.ircm.smoothing.OperatingSystem.WINDOWS_XP_OLDER;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link OperatingSystemService}.
 */
@Service
public class OperatingSystemService {
  /**
   * Returns current operating system.
   *
   * @return current operating system
   */
  public OperatingSystem currentOs() {
    if (SystemUtils.IS_OS_WINDOWS) {
      try {
        Double version = Double.parseDouble(SystemUtils.OS_VERSION);
        if (version < 6.0) {
          return WINDOWS_XP_OLDER;
        }
      } catch (Throwable e) {
        // Assume WINDOWS.
      }
      return WINDOWS;
    } else if (SystemUtils.IS_OS_MAC) {
      return MAC;
    } else if (SystemUtils.IS_OS_LINUX) {
      return LINUX;
    } else if (SystemUtils.IS_OS_UNIX) {
      return UNIX;
    }
    return OTHER;
  }

  /**
   * Returns true if underling OS is 64 bits, false otherwise.
   * <p>
   * There is no easy way to determine if OS is 64 bits in java. See
   * http://stackoverflow.com/questions/1856565/how-do-you-determine-32-or-64-bit-architecture-of-
   * windows-using-java
   * </p>
   *
   * @param os
   *          operating system
   * @return true if underling OS is 64 bits, false otherwise.
   */
  public boolean is64bit(OperatingSystem os) {
    boolean is64bit = System.getProperty("os.arch").indexOf("64") != -1;
    switch (os) {
      case WINDOWS:
      case WINDOWS_XP_OLDER:
        return System.getenv("ProgramFiles(x86)") != null || is64bit;
      case MAC:
        return is64bit;
      case UNIX:
        return is64bit;
      case OTHER:
        return is64bit;
      default:
        throw new AssertionError("");
    }
  }
}
